/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.EmailerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.*;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author artamonov
 * @version $Id$
 */
@Service(UserManagementService.NAME)
public class UserManagementServiceBean implements UserManagementService {

    protected static final String GROUP_COPY_VIEW = "group.copy";

    protected static final String MOVE_USER_TO_GROUP_VIEW = "user.moveToGroup";

    protected static final String RESET_PASSWORD_VIEW = "user.resetPassword";

    protected static final String CHANGE_PASSWORD_VIEW = "user.changePassword";

    protected static final String CHECK_PASSWORD_VIEW = "user.check";

    protected Logger log = LoggerFactory.getLogger(getClass());

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
    protected ServerConfig serverConfig;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Security security;

    protected void checkUpdatePermission(Class entityClass) {
        MetaClass metaClass = metadata.getClassNN(entityClass);

        if (!security.isEntityOpPermitted(metaClass, EntityOp.UPDATE)) {
            throw new AccessDeniedException(PermissionType.ENTITY_OP, metaClass.getName());
        }
    }

    @Override
    public Group copyAccessGroup(UUID accessGroupId) {
        checkNotNullArgument(accessGroupId, "Null access group id");
        checkUpdatePermission(Group.class);

        Group clone = null;

        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query groupNamesQuery = em.createQuery("select g.name from sec$Group g");
            @SuppressWarnings("unchecked")
            Set<String> groupNames = new HashSet<>(groupNamesQuery.getResultList());

            Group accessGroup = em.find(Group.class, accessGroupId, GROUP_COPY_VIEW);
            if (accessGroup == null)
                throw new IllegalStateException("Unable to find specified access group with id: " + accessGroupId);

            clone = cloneGroup(accessGroup, accessGroup.getParent(), groupNames, em);

            tx.commit();
        } finally {
            tx.end();
        }

        return clone;
    }

    @Override
    public Integer moveUsersToGroup(List<UUID> userIds, @Nullable UUID targetAccessGroupId) {
        checkNotNullArgument(userIds, "Null users list");
        checkUpdatePermission(User.class);

        if (userIds.isEmpty())
            return 0;

        Transaction tx = persistence.getTransaction();

        int modifiedUsers = 0;
        try {
            EntityManager em = persistence.getEntityManager();

            Group targetAccessGroup = null;
            if (targetAccessGroupId != null) {
                targetAccessGroup = em.find(Group.class, targetAccessGroupId);
                if (targetAccessGroup == null)
                    throw new IllegalStateException("Could not found target access group with id: " + targetAccessGroupId);
            }

            TypedQuery<User> query = em.createQuery("select u from sec$User u where u.id in :userIds", User.class);
            query.setParameter("userIds", userIds);
            query.setViewName(MOVE_USER_TO_GROUP_VIEW);

            List<User> users = query.getResultList();
            if (users == null || users.size() != userIds.size())
                throw new IllegalStateException("Not all users found in database");

            for (User user : users) {
                if (!ObjectUtils.equals(user.getGroup(), targetAccessGroup)) {
                    user.setGroup(targetAccessGroup);
                    modifiedUsers++;
                }
            }

            tx.commit();
        } finally {
            tx.end();
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

        SimpleTemplateEngine templateEngine = new SimpleTemplateEngine();

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

        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            user = em.find(User.class, userId, CHECK_PASSWORD_VIEW);
            if (user == null)
                throw new RuntimeException("Unable to find user with id: " + userId);

            tx.commit();
        } finally {
            tx.end();
        }

        return passwordEncryption.checkPassword(user, passwordHash);
    }

    @Override
    public void resetRememberMeTokens(List<UUID> userIds) {
        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("delete from sec$RememberMeToken rt where rt.user.id in :userIds");
            query.setParameter("userIds", userIds);
            query.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    public void resetRememberMeTokens() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("delete from sec$RememberMeToken rt");
            query.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    public String generateRememberMeToken(UUID userId) {
        String token = RandomStringUtils.randomAlphanumeric(RememberMeToken.TOKEN_LENGTH);

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            RememberMeToken rememberMeToken = metadata.create(RememberMeToken.class);
            rememberMeToken.setToken(token);
            rememberMeToken.setUser(em.getReference(User.class, userId));

            em.persist(rememberMeToken);

            tx.commit();
        } finally {
          tx.end();
        }

        return token;
    }

    @Override
    public UserTimeZone loadOwnTimeZone() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userSessionSource.getUserSession().getUser().getId(), "user.timeZone");
            if (user == null)
                throw new EntityAccessException();
            tx.commit();
            return new UserTimeZone(user.getTimeZone(), Boolean.TRUE.equals(user.getTimeZoneAuto()));
        } finally {
            tx.end();
        }
    }

    @Override
    public void saveOwnTimeZone(UserTimeZone timeZone) {
        log.debug("Saving user's time zone settings: " + timeZone);
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userSessionSource.getUserSession().getUser().getId(), "user.timeZone");
            if (user == null)
                throw new EntityAccessException();
            user.setTimeZone(timeZone.name);
            user.setTimeZoneAuto(timeZone.auto);
            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    public void changeUserPassword(UUID userId, String newPasswordHash) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userId, CHANGE_PASSWORD_VIEW);
            if (user == null) {
                throw new EntityAccessException();
            }

            user.setPassword(newPasswordHash);
            user.setChangePasswordAtNextLogon(false);

            // reset remember me for user
            Query query = em.createQuery("delete from sec$RememberMeToken rt where rt.user.id=:userId");
            query.setParameter("userId", userId);
            query.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
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
                    log.warn("Reset passwords: Not found email body template for locale: '{}'", locale);
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
                    log.warn("Reset passwords: Not found email subject template for locale '{}'", locale);
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
        Transaction tx = persistence.getTransaction();
        String emailBody;
        String emailSubject;
        try {
            Map<String, Object> binding = new HashMap<>();
            binding.put("user", user);
            binding.put("password", password);
            binding.put("persistence", persistence);

            emailBody = bodyTemplate.make(binding).writeTo(new StringWriter(0)).toString();
            emailSubject = subjectTemplate.make(binding).writeTo(new StringWriter(0)).toString();

            tx.commit();
        } catch (IOException e) {
            throw new RuntimeException("Unable to write Groovy template content", e);
        } finally {
            tx.end();
        }

        EmailInfo emailInfo = new EmailInfo(user.getEmail(), emailSubject, emailBody);
        emailerAPI.sendEmailAsync(emailInfo);
    }

    protected Map<User, String> updateUserPasswords(List<UUID> userIds, boolean generatePassword) {
        Map<User, String> modifiedUsers = new LinkedHashMap<>();

        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<User> query = em.createQuery("select u from sec$User u where u.id in :userIds", User.class);
            query.setParameter("userIds", userIds);
            query.setViewName(RESET_PASSWORD_VIEW);

            List<User> users = query.getResultList();

            if (users == null || users.size() != userIds.size())
                throw new IllegalStateException("Not all users found in database");

            for (User user : users) {
                String password = null;
                if (generatePassword) {
                    password = passwordEncryption.generateRandomPassword();

                    String passwordHash = passwordEncryption.getPasswordHash(user.getId(), password);
                    user.setPassword(passwordHash);
                }
                user.setChangePasswordAtNextLogon(true);

                modifiedUsers.put(user, password);
            }

            resetRememberMeTokens(userIds);

            tx.commit();
        } finally {
            tx.end();
        }

        return modifiedUsers;
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

        TypedQuery<Group> query = em.createQuery("select g from sec$Group g where g.parent.id = :group", Group.class);
        query.setParameter("group", group);

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
        resultConstraint.setJoinClause(constraint.getJoinClause());
        resultConstraint.setWhereClause(constraint.getWhereClause());
        resultConstraint.setGroup(group);
        return resultConstraint;
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
}