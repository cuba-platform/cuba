/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.security.app;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.EmailerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.app.role.RolesRepository;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.RoleDefinition;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Service(UserManagementService.NAME)
public class UserManagementServiceBean implements UserManagementService {

    private static final Logger log = LoggerFactory.getLogger(UserManagementServiceBean.class);

    protected static final String GROUP_COPY_VIEW = "group.copy";

    protected static final String ROLE_COPY_VIEW = "role.copy";

    protected static final String USER_MOVE_TO_GROUP_VIEW = "user.moveToGroup";
    protected static final String USER_RESET_PASSWORD_VIEW = "user.resetPassword";
    protected static final String USER_CHANGE_PASSWORD_VIEW = "user.changePassword";
    protected static final String USER_CHECK_PASSWORD_VIEW = "user.check";

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected EmailerAPI emailerAPI;

    @Inject
    protected Resources resources;

    @Inject
    protected Scripting scripting;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Security security;

    @Inject
    protected Messages messages;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected RolesRepository rolesRepository;

    protected void checkUpdatePermission(Class entityClass) {
        checkPermission(entityClass, EntityOp.UPDATE);
    }

    protected void checkPermission(Class entityClass, EntityOp op) {
        MetaClass metaClass = metadata.getClassNN(entityClass);

        if (!security.isEntityOpPermitted(metaClass, op)) {
            throw new AccessDeniedException(PermissionType.ENTITY_OP, metaClass.getName());
        }
    }

    @Override
    public Group copyAccessGroup(UUID accessGroupId) {
        checkNotNullArgument(accessGroupId, "Null access group id");
        checkUpdatePermission(Group.class);

        Group clone;

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Query groupNamesQuery = em.createQuery("select g.name from sec$Group g");
            @SuppressWarnings("unchecked")
            Set<String> groupNames = new HashSet<>(groupNamesQuery.getResultList());

            Group accessGroup = em.find(Group.class, accessGroupId, GROUP_COPY_VIEW);
            if (accessGroup == null)
                throw new IllegalStateException("Unable to find specified access group with id: " + accessGroupId);

            clone = cloneGroup(accessGroup, accessGroup.getParent(), groupNames, em);

            tx.commit();
        }

        return clone;
    }

    @Override
    public Role copyRole(String predefinedRoleName) {
        checkNotNullArgument(predefinedRoleName, "Null access role id");
        checkUpdatePermission(Role.class);

        if (!rolesRepository.isDatabaseModeAvailable()) {
            throw new IllegalStateException("Unable to copy predefined role. Database mode for roles is unavailable.");
        }

        Role clone;

        RoleDefinition predefinedRole = rolesRepository.getRoleDefinitionByName(predefinedRoleName);
        if (predefinedRole == null) {
            throw new IllegalStateException("Unable to find specified role with name: " + predefinedRoleName);
        }

        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            clone = rolesRepository.getRoleWithPermissions(predefinedRole);

            clone.setName(generateName(em, predefinedRole.getName()));
            clone.setDefaultRole(false);
            clone.setPredefined(false);

            em.persist(clone);

            for (Permission permission : clone.getPermissions()) {
                em.persist(permission);
            }

            tx.commit();
        } finally {
            tx.end();
        }

        return clone;
    }

    @Override
    public Role copyRole(UUID roleId) {
        checkNotNullArgument(roleId, "Null access role id");
        checkUpdatePermission(Role.class);

        Role clone;

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Role role = em.find(Role.class, roleId, ROLE_COPY_VIEW);
            if (role == null)
                throw new IllegalStateException("Unable to find specified role with id: " + roleId);

            clone = cloneRole(role, generateName(em, role.getName()), em);
            clone.setDefaultRole(false);

            tx.commit();
        }

        return clone;
    }

    @Override
    public Integer moveUsersToGroup(List<UUID> userIds, @Nullable UUID targetAccessGroupId) {
        checkNotNullArgument(userIds, "Null users list");
        checkUpdatePermission(User.class);

        if (userIds.isEmpty())
            return 0;

        int modifiedUsers = 0;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Group targetAccessGroup = null;
            if (targetAccessGroupId != null) {
                targetAccessGroup = em.find(Group.class, targetAccessGroupId);
                if (targetAccessGroup == null)
                    throw new IllegalStateException("Could not found target access group with id: " + targetAccessGroupId);
            }

            TypedQuery<User> query = em.createQuery("select u from sec$User u where u.id in :userIds", User.class);
            query.setParameter("userIds", userIds);
            query.setViewName(USER_MOVE_TO_GROUP_VIEW);

            List<User> users = query.getResultList();
            if (users == null || users.size() != userIds.size())
                throw new IllegalStateException("Not all users found in database");

            for (User user : users) {
                if (!Objects.equals(user.getGroup(), targetAccessGroup)) {
                    user.setGroup(targetAccessGroup);
                    modifiedUsers++;
                }
            }

            tx.commit();
        }
        return modifiedUsers;
    }

    @Override
    public Integer changePasswordsAtLogonAndSendEmails(List<UUID> userIds) {
        checkNotNullArgument(userIds, "Null users list");
        checkUpdatePermission(User.class);

        if (userIds.isEmpty())
            return 0;

        Map<User, String> modifiedUsers = updateUserPasswords(userIds, true);

        // email templates
        String resetPasswordBodyTemplate = serverConfig.getResetPasswordEmailBodyTemplate();
        String resetPasswordSubjectTemplate = serverConfig.getResetPasswordEmailSubjectTemplate();

        SimpleTemplateEngine templateEngine = new SimpleTemplateEngine(scripting.getClassLoader());

        Map<String, Template> localizedBodyTemplates = new HashMap<>();
        Map<String, Template> localizedSubjectTemplates = new HashMap<>();

        // load default
        Template bodyDefaultTemplate = loadDefaultTemplate(resetPasswordBodyTemplate, templateEngine);
        Template subjectDefaultTemplate = loadDefaultTemplate(resetPasswordSubjectTemplate, templateEngine);

        for (Map.Entry<User, String> userPasswordEntry : modifiedUsers.entrySet()) {
            User user = userPasswordEntry.getKey();
            if (StringUtils.isNotEmpty(user.getEmail())) {
                EmailTemplate template = getResetPasswordTemplate(user, templateEngine,
                        resetPasswordSubjectTemplate, resetPasswordBodyTemplate,
                        subjectDefaultTemplate, bodyDefaultTemplate,
                        localizedSubjectTemplates, localizedBodyTemplates);

                String password = userPasswordEntry.getValue();
                sendResetPasswordEmail(user, password, template.getSubjectTemplate(), template.getBodyTemplate());
            }
        }

        return modifiedUsers.size();
    }

    @Override
    public Map<UUID, String> changePasswordsAtLogon(List<UUID> userIds, boolean generatePassword) {
        checkNotNullArgument(userIds, "Null users list");

        checkUpdatePermission(User.class);

        if (userIds.isEmpty())
            return Collections.emptyMap();

        Map<User, String> modifiedUsers = updateUserPasswords(userIds, generatePassword);
        Map<UUID, String> userPasswords = new LinkedHashMap<>();
        for (Map.Entry<User, String> entry : modifiedUsers.entrySet())
            userPasswords.put(entry.getKey().getId(), entry.getValue());

        return userPasswords;
    }

    @Override
    public boolean checkPassword(UUID userId, String passwordHash) {
        checkNotNullArgument(userId, "Null userId");
        checkNotNullArgument(passwordHash, "Null new password hash");

        User user;

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            user = em.find(User.class, userId, USER_CHECK_PASSWORD_VIEW);
            if (user == null)
                throw new RuntimeException("Unable to find user with id: " + userId);

            tx.commit();
        }

        return passwordEncryption.checkPassword(user, passwordHash);
    }

    @Override
    public void resetRememberMeTokens(List<UUID> userIds) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("delete from sec$RememberMeToken rt where rt.user.id in :userIds");
            query.setParameter("userIds", userIds);
            query.executeUpdate();

            tx.commit();
        }
    }

    @Override
    public void resetRememberMeTokens() {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("delete from sec$RememberMeToken rt");
            query.executeUpdate();

            tx.commit();
        }
    }

    @Override
    public void removeRememberMeTokens(List<String> rememberMeTokens) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("delete from sec$RememberMeToken rt where rt.token in :tokens");
            query.setParameter("tokens", rememberMeTokens);
            query.executeUpdate();

            tx.commit();
        }
    }

    @Override
    public boolean isRememberMeTokenValid(String login, String rememberMeToken) {
        RememberMeToken token = loadRememberMeToken(login, rememberMeToken);
        if (token == null) {
            log.debug("Remember me token '{}' is not found. Consider it as not valid", rememberMeToken);
            return false;
        }

        if (token.getCreateTs() == null) {
            log.debug("Remember me token '{}' doesn't have createTs and will be considered as not valid",
                    rememberMeToken);
            return false;
        }

        long tokenCreated = token.getCreateTs().getTime();
        long now = timeSource.currentTimeMillis();
        int expirationTimeout = globalConfig.getRememberMeExpirationTimeoutSec();

        return tokenCreated + expirationTimeout > now;
    }

    @Override
    public String generateRememberMeToken(UUID userId) {
        String token = RandomStringUtils.randomAlphanumeric(RememberMeToken.TOKEN_LENGTH);

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            RememberMeToken rememberMeToken = metadata.create(RememberMeToken.class);
            rememberMeToken.setToken(token);
            rememberMeToken.setUser(em.getReference(User.class, userId));
            rememberMeToken.setCreateTs(new Date(timeSource.currentTimeMillis()));

            em.persist(rememberMeToken);

            tx.commit();
        }

        return token;
    }

    @Override
    public List<String> getSessionAttributeNames(UUID groupId) {
        Preconditions.checkNotNullArgument(groupId, "groupId is null");
        checkPermission(SessionAttribute.class, EntityOp.READ);
        checkUpdatePermission(Group.class);
        checkUpdatePermission(Constraint.class);

        Set<String> attributes;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("select a.name from sec$SessionAttribute a where a.group.id = ?1");
            query.setParameter(1, groupId);
            //noinspection unchecked
            attributes = new HashSet<>(query.getResultList());

            query = em.createQuery("select a.name from sec$GroupHierarchy h join h.parent.sessionAttributes a where h.group.id = ?1");
            query.setParameter(1, groupId);
            //noinspection unchecked
            attributes.addAll(query.getResultList());

            tx.commit();
        }
        return new ArrayList<>(attributes);
    }

    @Override
    public UserTimeZone loadOwnTimeZone() {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userSessionSource.getUserSession().getUser().getId(), "user.timeZone");
            if (user == null)
                throw new EntityAccessException(User.class, userSessionSource.getUserSession().getUser().getId());
            tx.commit();
            return new UserTimeZone(user.getTimeZone(), Boolean.TRUE.equals(user.getTimeZoneAuto()));
        }
    }

    @Override
    public void saveOwnTimeZone(UserTimeZone timeZone) {
        log.debug("Saving user's time zone settings: " + timeZone);
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userSessionSource.getUserSession().getUser().getId(), "user.timeZone");
            if (user == null)
                throw new EntityAccessException(User.class, userSessionSource.getUserSession().getUser().getId());
            user.setTimeZone(timeZone.name);
            user.setTimeZoneAuto(timeZone.auto);
            tx.commit();
        }
    }

    @Override
    public String loadOwnLocale() {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userSessionSource.getUserSession().getUser().getId(), "user.locale");
            if (user == null)
                throw new EntityAccessException(User.class, userSessionSource.getUserSession().getUser().getId());
            tx.commit();
            return user.getLanguage();
        }
    }

    @Override
    public void saveOwnLocale(String locale) {
        UUID userId = userSessionSource.getUserSession().getUser().getId();
        log.debug("Saving user's {} language settings: {}", userId, locale);

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userId, "user.locale");
            if (user == null)
                throw new EntityAccessException(User.class, userId);

            user.setLanguage(locale);
            tx.commit();
        }
    }

    @Override
    public void changeUserPassword(UUID userId, String newPasswordHash) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userId, USER_CHANGE_PASSWORD_VIEW);
            if (user == null) {
                throw new EntityAccessException(User.class, userId);
            }

            user.setPasswordEncryption(passwordEncryption.getHashMethod());
            user.setPassword(newPasswordHash);
            user.setChangePasswordAtNextLogon(false);

            // reset remember me for user
            Query query = em.createQuery("delete from sec$RememberMeToken rt where rt.user.id=:userId");
            query.setParameter("userId", userId);
            query.executeUpdate();

            tx.commit();
        }
    }

    @Override
    public void changeGroupParent(UUID groupId, UUID newParentId) {
        checkUpdatePermission(Group.class);

        DataManager dataManager = this.dataManager.secure();

        Group group = dataManager.load(LoadContext.create(Group.class)
                .setId(groupId)
                .setView(GROUP_COPY_VIEW));

        LoadContext<Group> context = LoadContext.create(Group.class)
                .setId(newParentId)
                .setView(GROUP_COPY_VIEW);

        Group newParent = newParentId != null ? dataManager.load(context) : null;

        if (group != null) {
            group.setParent(newParent);
        }

        dataManager.commit(group);
    }

    protected EmailTemplate getResetPasswordTemplate(User user,
                                                     SimpleTemplateEngine templateEngine,
                                                     String resetPasswordSubjectTemplate,
                                                     String resetPasswordBodyTemplate,
                                                     Template subjectDefaultTemplate,
                                                     Template bodyDefaultTemplate,
                                                     Map<String, Template> localizedSubjectTemplates,
                                                     Map<String, Template> localizedBodyTemplates) {

        boolean userLocaleIsUnknown = StringUtils.isEmpty(user.getLanguage());
        String locale = userLocaleIsUnknown ?
                messageTools.getDefaultLocale().getLanguage() : user.getLanguage();

        Template bodyTemplate;
        if (userLocaleIsUnknown) {
            bodyTemplate = bodyDefaultTemplate;
        } else {
            if (localizedBodyTemplates.containsKey(locale))
                bodyTemplate = localizedBodyTemplates.get(locale);
            else {
                String templateString = getLocalizedTemplateContent(resetPasswordBodyTemplate, locale);
                if (templateString == null) {
                    log.debug("Reset passwords: Not found email body template for locale: '{}'", locale);
                    bodyTemplate = bodyDefaultTemplate;
                } else {
                    bodyTemplate = getTemplate(templateEngine, templateString);
                }
                localizedBodyTemplates.put(locale, bodyTemplate);
            }
        }

        Template subjectTemplate;
        if (userLocaleIsUnknown) {
            subjectTemplate = subjectDefaultTemplate;
        } else {
            if (localizedSubjectTemplates.containsKey(locale))
                subjectTemplate = localizedSubjectTemplates.get(locale);
            else {
                String templateString = getLocalizedTemplateContent(resetPasswordSubjectTemplate, locale);
                if (templateString == null) {
                    log.debug("Reset passwords: Not found email subject template for locale '{}'", locale);
                    subjectTemplate = subjectDefaultTemplate;
                } else {
                    subjectTemplate = getTemplate(templateEngine, templateString);
                }
                localizedSubjectTemplates.put(locale, subjectTemplate);
            }
        }

        return new EmailTemplate(subjectTemplate, bodyTemplate);
    }

    private String getLocalizedTemplateContent(String defaultTemplateName, String locale) {
        String localizedTemplate = FilenameUtils.getFullPath(defaultTemplateName)
                + FilenameUtils.getBaseName(defaultTemplateName) +
                "_" + locale +
                "." + FilenameUtils.getExtension(defaultTemplateName);

        return resources.getResourceAsString(localizedTemplate);
    }

    protected Template getTemplate(SimpleTemplateEngine templateEngine, String templateString) {
        Template bodyTemplate;
        try {
            bodyTemplate = templateEngine.createTemplate(templateString);
        } catch (Exception e) {
            throw new RuntimeException("Unable to compile Groovy template", e);
        }
        return bodyTemplate;
    }

    protected Template loadDefaultTemplate(String templatePath, SimpleTemplateEngine templateEngine) {
        String defaultTemplateContent = resources.getResourceAsString(templatePath);
        if (defaultTemplateContent == null) {
            throw new IllegalStateException("Not found default email template for reset passwords operation");
        }

        //noinspection UnnecessaryLocalVariable
        Template template = getTemplate(templateEngine, defaultTemplateContent);
        return template;
    }

    protected void sendResetPasswordEmail(User user, String password, Template subjectTemplate, Template bodyTemplate) {
        String emailBody;
        String emailSubject;
        try (Transaction tx = persistence.createTransaction()) {
            Map<String, Object> binding = new HashMap<>();
            binding.put("user", user);
            binding.put("password", password);
            binding.put("persistence", persistence);

            emailBody = bodyTemplate.make(binding).writeTo(new StringWriter(0)).toString();
            emailSubject = subjectTemplate.make(binding).writeTo(new StringWriter(0)).toString();

            tx.commit();
        } catch (IOException e) {
            throw new RuntimeException("Unable to write Groovy template content", e);
        }

        EmailInfo emailInfo = EmailInfoBuilder.create()
                .setAddresses(user.getEmail())
                .setCaption(emailSubject)
                .setBody(emailBody)
                .build();
        emailerAPI.sendEmailAsync(emailInfo);
    }

    protected Map<User, String> updateUserPasswords(List<UUID> userIds, boolean generatePassword) {
        Map<User, String> modifiedUsers = new LinkedHashMap<>();

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<User> query = em.createQuery("select u from sec$User u where u.id in :userIds", User.class);
            query.setParameter("userIds", userIds);
            query.setViewName(USER_RESET_PASSWORD_VIEW);

            List<User> users = query.getResultList();

            if (users == null || users.size() != userIds.size())
                throw new IllegalStateException("Not all users found in database");

            for (User user : users) {
                String password = null;
                if (generatePassword) {
                    password = passwordEncryption.generateRandomPassword();

                    user.setPasswordEncryption(passwordEncryption.getHashMethod());

                    String passwordHash = passwordEncryption.getPasswordHash(user.getId(), password);
                    user.setPassword(passwordHash);
                }
                user.setChangePasswordAtNextLogon(true);

                modifiedUsers.put(user, password);
            }

            resetRememberMeTokens(userIds);

            tx.commit();
        }

        return modifiedUsers;
    }

    protected String generateName(EntityManager em, String roleName) {
        Query roleNamesQuery = em.createQuery("select g.name from sec$Role g");
        @SuppressWarnings("unchecked")
        Set<String> roleNames = new HashSet<>(roleNamesQuery.getResultList());

        return generateName(roleName, roleNames);
    }

    protected Role cloneRole(Role role, String newRoleName, EntityManager em) {
        Role roleClone = metadata.create(Role.class);

        roleClone.setName(newRoleName);
        roleClone.setType(role.getType());
        roleClone.setDefaultRole(role.getDefaultRole());
        roleClone.setLocName(role.getLocName());
        roleClone.setDescription(role.getDescription());

        em.persist(roleClone);

        if (role.getPermissions() != null) {
            for (Permission permission : role.getPermissions()) {
                Permission permissionClone = clonePermission(permission, roleClone);
                em.persist(permissionClone);
            }
        }

        return roleClone;
    }

    protected Group cloneGroup(Group group, Group parent, Set<String> groupNames, EntityManager em) {
        Group groupClone = metadata.create(Group.class);

        String newGroupName = generateName(group.getName(), groupNames);
        groupClone.setName(newGroupName);
        groupNames.add(newGroupName);

        groupClone.setParent(parent);

        em.persist(groupClone);
        // fire hierarchy listeners
        em.flush();

        if (group.getConstraints() != null) {
            for (Constraint constraint : group.getConstraints()) {
                Constraint constraintClone = cloneConstraint(constraint, groupClone);
                em.persist(constraintClone);
            }
        }

        if (group.getSessionAttributes() != null) {
            for (SessionAttribute attribute : group.getSessionAttributes()) {
                SessionAttribute attributeClone = cloneSessionAttribute(attribute, groupClone);
                em.persist(attributeClone);
            }
        }

        TypedQuery<Group> query = em.createQuery("select g from sec$Group g where g.parent.id = :groupId", Group.class);
        query.setParameter("groupId", group.getId());

        List<Group> subGroups = query.getResultList();
        if (subGroups != null && subGroups.size() > 0) {
            for (Group subGroup : subGroups) {
                cloneGroup(subGroup, groupClone, groupNames, em);
            }
        }

        return groupClone;
    }

    protected String generateName(String originalGroupName, Set<String> groupNames) {
        String newGroupName;

        int i = 1;
        do {
            newGroupName = originalGroupName + " (" + i + ")";
            i++;
        } while (groupNames.contains(newGroupName));

        return newGroupName;
    }

    protected SessionAttribute cloneSessionAttribute(SessionAttribute attribute, Group group) {
        SessionAttribute resultAttribute = metadata.create(SessionAttribute.class);
        resultAttribute.setName(attribute.getName());
        resultAttribute.setDatatype(attribute.getDatatype());
        resultAttribute.setStringValue(attribute.getStringValue());
        resultAttribute.setGroup(group);
        return resultAttribute;
    }

    protected Constraint cloneConstraint(Constraint constraint, Group group) {
        Constraint resultConstraint = metadata.create(Constraint.class);
        resultConstraint.setEntityName(constraint.getEntityName());
        resultConstraint.setCode(constraint.getCode());
        resultConstraint.setCheckType(constraint.getCheckType());
        resultConstraint.setOperationType(constraint.getOperationType());
        resultConstraint.setJoinClause(constraint.getJoinClause());
        resultConstraint.setWhereClause(constraint.getWhereClause());
        resultConstraint.setGroovyScript(constraint.getGroovyScript());
        resultConstraint.setFilterXml(constraint.getFilterXml());
        resultConstraint.setIsActive(constraint.getIsActive());
        resultConstraint.setGroup(group);
        return resultConstraint;
    }

    protected Permission clonePermission(Permission permission, Role role) {
        Permission resultPermission = metadata.create(Permission.class);
        resultPermission.setValue(permission.getValue());
        resultPermission.setType(permission.getType());
        resultPermission.setTarget(permission.getTarget());
        resultPermission.setRole(role);

        return resultPermission;
    }

    @Nullable
    protected RememberMeToken loadRememberMeToken(String login, String rememberMeToken) {
        RememberMeToken token;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<RememberMeToken> query = em.createQuery(
                    "select rt from sec$RememberMeToken rt where rt.token = :token and rt.user.login = :userLogin",
                    RememberMeToken.class);
            query.setParameter("token", rememberMeToken);
            query.setParameter("userLogin", login);

            token = query.getFirstResult();

            tx.commit();
        }
        return token;
    }

    /**
     * Template pair : subject + body
     */
    protected static class EmailTemplate {

        private Template subjectTemplate;
        private Template bodyTemplate;

        private EmailTemplate(Template subjectTemplate, Template bodyTemplate) {
            this.subjectTemplate = subjectTemplate;
            this.bodyTemplate = bodyTemplate;
        }

        public Template getSubjectTemplate() {
            return subjectTemplate;
        }

        public Template getBodyTemplate() {
            return bodyTemplate;
        }
    }

    @Override
    public boolean isUsersRemovingAllowed(Collection<String> userLogins) {
        return !userLogins.contains(serverConfig.getJmxUserLogin()) && !userLogins.contains(serverConfig.getAnonymousLogin());
    }

    @Override
    public boolean isAnonymousUser(String userLogin) {
        return serverConfig.getAnonymousLogin().equalsIgnoreCase(userLogin);
    }

    @Override
    public List<UserSubstitution> getSubstitutedUsers(UUID userId) {
        LoadContext<UserSubstitution> ctx = new LoadContext<>(UserSubstitution.class);
        LoadContext.Query query = ctx.setQueryString("select us from sec$UserSubstitution us " +
                "where us.user.id = :userId and (us.endDate is null or us.endDate >= :currentDate) " +
                "and (us.startDate is null or us.startDate <= :currentDate) " +
                "and (us.substitutedUser.active = true or us.substitutedUser.active is null) order by us.substitutedUser.name");
        query.setParameter("userId", userId);
        query.setParameter("currentDate", timeSource.currentTimestamp());
        ctx.setView("app");
        return dataManager.loadList(ctx);
    }
}