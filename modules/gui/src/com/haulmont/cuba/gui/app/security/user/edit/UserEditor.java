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
package com.haulmont.cuba.gui.app.security.user.edit;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.app.security.events.UserPasswordChangedEvent;
import com.haulmont.cuba.gui.app.security.user.NameBuilderListener;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.AbstractDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.events.UserSubstitutionsChangedEvent;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.role.RolesService;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

import static com.haulmont.cuba.gui.components.PickerField.LookupAction;

public class UserEditor extends AbstractEditor<User> {

    @Inject
    protected DsContext dsContext;

    @Inject
    protected DataSupplier dataSupplier;

    @Inject
    protected Datasource<User> userDs;

    @Inject
    protected UserManagementService userManagementService;

    @Inject
    protected CollectionDatasource<UserRole, UUID> rolesDs;

    @Inject
    protected CollectionDatasource<UserSubstitution, UUID> substitutionsDs;

    @Inject
    protected Table<UserRole> rolesTable;

    @Inject
    protected Table<UserSubstitution> substTable;

    @Inject
    protected FieldGroup fieldGroupLeft;

    @Inject
    protected FieldGroup fieldGroupRight;

    @Inject
    protected Icons icons;

    protected PasswordField passwField;
    protected PasswordField confirmPasswField;
    protected LookupField<String> languageLookup;
    protected LookupField<String> timeZoneLookup;

    @Inject
    protected UserSession userSession;

    @Inject
    protected UiComponents uiComponents;

    @Inject
    protected Configuration configuration;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Security security;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected TimeZones timeZones;

    @Inject
    protected Events events;

    @Inject
    protected RolesService rolesService;

    @Named("fieldGroupRight.active")
    private CheckBox activeField;

    @WindowParam(name = "initCopy")
    protected Boolean initCopy;

    public interface Companion {
        void initPasswordField(PasswordField passwordField);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        userDs.addItemPropertyChangeListener(new NameBuilderListener<>(userDs));
        userDs.addItemPropertyChangeListener(e -> {
            if ("timeZoneAuto".equals(e.getProperty())) {
                timeZoneLookup.setEnabled(!Boolean.TRUE.equals(e.getValue()));
            }
        });

        AddRoleAction addRoleAction = new AddRoleAction();
        addRoleAction.setEnabled(security.isEntityOpPermitted(UserRole.class, EntityOp.CREATE));
        rolesTable.addAction(addRoleAction);
        EditRoleAction editRoleAction = new EditRoleAction();
        rolesTable.addAction(editRoleAction);

        RemoveRoleAction removeRoleAction = new RemoveRoleAction(rolesTable, false);
        boolean isUserRoleDeletePermitted = security.isEntityOpPermitted(UserRole.class, EntityOp.DELETE);
        boolean isUserUpdatePermitted = security.isEntityOpPermitted(User.class, EntityOp.UPDATE);
        removeRoleAction.setEnabled(isUserRoleDeletePermitted && isUserUpdatePermitted);
        rolesTable.addAction(removeRoleAction);

        AddSubstitutedAction addSubstitutedAction = new AddSubstitutedAction();
        addSubstitutedAction.setEnabled(security.isEntityOpPermitted(UserSubstitution.class, EntityOp.CREATE));

        substTable.addAction(addSubstitutedAction);
        EditSubstitutedAction editSubstitutedAction = new EditSubstitutedAction();
        substTable.addAction(editSubstitutedAction);
        RemoveAction removeSubstitutedAction = new RemoveAction(substTable, false);
        substTable.addAction(removeSubstitutedAction);

        boolean isUserRoleCreatePermitted = security.isEntityOpPermitted(UserRole.class, EntityOp.CREATE);
        addRoleAction.setEnabled(isUserRoleCreatePermitted && isUserUpdatePermitted);

        boolean isSubstitutedUserCreatePermitted = security.isEntityOpPermitted(UserSubstitution.class, EntityOp.CREATE);
        addSubstitutedAction.setEnabled(isSubstitutedUserCreatePermitted && isUserUpdatePermitted);

        boolean isSubstitutedUserDeletePermitted = security.isEntityOpPermitted(UserSubstitution.class, EntityOp.DELETE);
        removeSubstitutedAction.setEnabled(isSubstitutedUserDeletePermitted && isUserUpdatePermitted);

        boolean isRoleUpdatePermitted = security.isEntityOpPermitted(Role.class, EntityOp.UPDATE);
        editRoleAction.setEnabled(isRoleUpdatePermitted);

        boolean isSubstitutedUserUpdatePermitted = security.isEntityOpPermitted(UserSubstitution.class, EntityOp.UPDATE);
        editSubstitutedAction.setEnabled(isSubstitutedUserUpdatePermitted);

        initCustomFields(PersistenceHelper.isNew(WindowParams.ITEM.getEntity(params)));

        dsContext.addAfterCommitListener((context, result) -> {
            updateSessionSubstitutions(result);

            if (passwField.getValue() != null) {
                publishPasswordChangedEvent(getItem(), passwField.getValue());
            }
        });
    }

    protected void updateSessionSubstitutions(Set<Entity> committedEntities) {
        for (Entity entity : committedEntities) {
            if (entity.equals(userSession.getUser())) {
                userSession.setUser((User) entity);
            }
            if (entity.equals(userSession.getSubstitutedUser())) {
                userSession.setSubstitutedUser((User) entity);
            }
        }

        if (userSession.getUser().equals(getItem())) {
            for (Entity entity : committedEntities) {
                if (entity instanceof UserSubstitution) {
                    publishUserSubstitutionsChanged(userSession.getUser());

                    break;
                }
            }
        }
    }

    @Override
    protected void postInit() {
        activeField.setEnabled(!userManagementService.isAnonymousUser(getItem().getLogin()));

        setCaption(PersistenceHelper.isNew(getItem()) ?
                getMessage("createCaption") : formatMessage("editCaption", getItem().getLogin()));

        timeZoneLookup.setEnabled(!Boolean.TRUE.equals(getItem().getTimeZoneAuto()));

        // Do not show roles which are not allowed by security constraints
        LoadContext<Role> lc = new LoadContext<>(Role.class);
        lc.setQueryString("select r from sec$Role r");
        lc.setView(View.MINIMAL);
        List<Role> allowedRoles = dataSupplier.loadList(lc);

        filterRolesDs(allowedRoles);

        if (BooleanUtils.isTrue(initCopy)) {
            initCopy();
        }

        // if we add default roles, rolesDs becomes modified on setItem
        ((AbstractDatasource) rolesDs).setModified(false);
    }

    protected void filterRolesDs(List<Role> allowedRoles) {
        Collection<UserRole> userRoles = new ArrayList<>(rolesDs.getItems());
        Map<String, UserRole> notExcludedUserRoles = new HashMap<>();

        for (UserRole userRole : userRoles) {
            if ((!rolesService.isDatabaseModeAvailable() && userRole.getRole() != null)
                    || (!rolesService.isPredefinedRolesModeAvailable() && userRole.getRoleName() != null)) {
                rolesDs.excludeItem(userRole);
                continue;
            }
            if (userRole.getRole() != null && !allowedRoles.contains(userRole.getRole())) {
                rolesDs.excludeItem(userRole);
                continue;
            }
            if (userRole.getRoleName() != null) {
                userRole.setRole(rolesService.getRoleByName(userRole.getRoleName()));
                rolesDs.modifyItem(userRole);

                ((AbstractDatasource) rolesDs).getItemsToUpdate().remove(userRole);
                ((AbstractDatasource) userDs).setModified(false);
            }
            if (notExcludedUserRoles.keySet().contains(userRole.getRole().getName())) {
                if (userRole.getRoleName() != null) {
                    rolesDs.excludeItem(userRole);
                    continue;
                } else {
                    rolesDs.excludeItem(notExcludedUserRoles.get(userRole.getRole().getName()));
                }
            }
            notExcludedUserRoles.put(userRole.getRole().getName(), userRole);
        }
    }

    @Override
    protected void initNewItem(User item) {
        addDefaultRoles(item);
        item.setLanguage(messages.getTools().localeToString(userSession.getLocale()));
        initUserGroup(item);
        item.setDisabledDefaultRoles(true);
    }

    protected void initUserGroup(User user) {
        LoadContext<Group> ctx = new LoadContext<>(Group.class);
        ctx.setQueryString("select g from sec$Group g");
        ctx.setView(View.MINIMAL);
        List<Group> groups = dataSupplier.loadList(ctx);
        if (groups.size() == 1) {
            user.setGroup(groups.get(0));
        }
    }

    protected void addDefaultRoles(User user) {
        Map<String, Role> defaultRoles = rolesService.getDefaultRoles();

        if (defaultRoles == null || defaultRoles.isEmpty()) {
            return;
        }

        List<UserRole> newRoles = new ArrayList<>();
        if (user.getUserRoles() != null) {
            newRoles.addAll(user.getUserRoles());
        }

        MetaClass metaClass = rolesDs.getMetaClass();

        for (Map.Entry<String, Role> entry : defaultRoles.entrySet()) {
            UserRole userRole = dataSupplier.newInstance(metaClass);
            userRole.setUser(user);

            if (entry.getValue() != null) {
                userRole.setRole(entry.getValue());
            } else {
                userRole.setRoleName(entry.getKey());
            }
            newRoles.add(userRole);
        }

        user.setUserRoles(newRoles);
    }

    protected void initCustomFields(boolean isNew) {
        createPasswordFields(isNew);

        createLanguageLookup();

        createTimeZoneField();

        createGroupField();
    }

    protected void createTimeZoneField() {
        FieldGroup.FieldConfig timeZoneFc = fieldGroupRight.getFieldNN("timeZone");

        HBoxLayout hbox = uiComponents.create(HBoxLayout.class);
        hbox.setSpacing(true);

        timeZoneLookup = uiComponents.create(LookupField.TYPE_STRING);

        timeZoneLookup.setDatasource(timeZoneFc.getTargetDatasource(), timeZoneFc.getProperty());
        timeZoneLookup.setRequired(false);

        MetaClass userMetaClass = userDs.getMetaClass();
        timeZoneLookup.setEditable(fieldGroupRight.isEditable()
                && security.isEntityAttrUpdatePermitted(userMetaClass, timeZoneFc.getProperty()));

        Map<String, String> options = new TreeMap<>();
        for (String id : TimeZone.getAvailableIDs()) {
            TimeZone timeZone = TimeZone.getTimeZone(id);
            options.put(timeZones.getDisplayNameLong(timeZone), id);
        }
        timeZoneLookup.setOptionsMap(options);

        hbox.add(timeZoneLookup);

        CheckBox autoDetectField = uiComponents.create(CheckBox.class);
        autoDetectField.setDatasource(timeZoneFc.getTargetDatasource(), "timeZoneAuto");
        autoDetectField.setCaption(messages.getMainMessage("timeZone.auto"));
        autoDetectField.setDescription(messages.getMainMessage("timeZone.auto.descr"));
        autoDetectField.setAlignment(Alignment.MIDDLE_RIGHT);

        autoDetectField.setEditable(fieldGroupRight.isEditable()
                && security.isEntityAttrUpdatePermitted(userMetaClass, "timeZoneAuto"));

        hbox.add(autoDetectField);
        hbox.expand(timeZoneLookup);

        timeZoneFc.setComponent(hbox);
    }

    protected void createGroupField() {
        FieldGroup.FieldConfig groupFc = fieldGroupRight.getFieldNN("group");

        PickerField<?> pickerField = uiComponents.create(PickerField.class);

        pickerField.setDatasource(groupFc.getTargetDatasource(), groupFc.getProperty());
        pickerField.setRequired(true);
        pickerField.setRequiredMessage(getMessage("groupMsg"));

        LookupAction action = LookupAction.create(pickerField);
        action.setLookupScreenOpenType(OpenType.DIALOG);
        action.setLookupScreenParamsSupplier(() -> {
            if (getItem().getGroup() != null) {
                return ParamsMap.of("selectedGroup", getItem().getGroup());
            }
            return Collections.emptyMap();
        });
        pickerField.addAction(action);

        groupFc.setComponent(pickerField);
    }

    protected void createPasswordFields(boolean isNew) {
        passwField = uiComponents.create(PasswordField.class);
        if (isNew) {
            passwField.setRequiredMessage(getMessage("passwMsg"));

            Companion companion = getCompanion();
            if (companion != null) {
                companion.initPasswordField(passwField);
            } else {
                passwField.setRequired(true);
            }
            passwField.addValueChangeListener(e ->
                    ((DatasourceImplementation) userDs).setModified(true)
            );
        } else {
            passwField.setVisible(false);
        }
        fieldGroupLeft.getFieldNN("passw").setComponent(passwField);

        confirmPasswField = uiComponents.create(PasswordField.class);
        if (isNew) {
            confirmPasswField.setRequiredMessage(getMessage("confirmPasswMsg"));

            Companion companion = getCompanion();
            if (companion != null) {
                companion.initPasswordField(confirmPasswField);
            } else {
                confirmPasswField.setRequired(true);
            }
            confirmPasswField.addValueChangeListener(e ->
                    ((DatasourceImplementation) userDs).setModified(true)
            );
        } else {
            confirmPasswField.setVisible(false);
        }
        fieldGroupLeft.getFieldNN("confirmPassw").setComponent(confirmPasswField);
    }

    protected void createLanguageLookup() {
        languageLookup = uiComponents.create(LookupField.TYPE_STRING);
        FieldGroup.FieldConfig languageLookupFc = fieldGroupRight.getFieldNN("language");
        languageLookup.setDatasource(languageLookupFc.getTargetDatasource(), languageLookupFc.getProperty());
        languageLookup.setRequired(false);

        Map<String, Locale> locales = configuration.getConfig(GlobalConfig.class).getAvailableLocales();
        Map<String, String> options = new TreeMap<>();
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            options.put(entry.getKey(), messages.getTools().localeToString(entry.getValue()));
        }
        languageLookup.setOptionsMap(options);
        languageLookupFc.setComponent(languageLookup);
    }

    @Override
    protected boolean preCommit() {

        boolean isDsModified = rolesDs.isModified();
        Collection<UserRole> userRoles = new ArrayList<>(rolesDs.getItems());
        for (UserRole userRole : userRoles) {
            if (userRole.getRole().isPredefined()) {
                if (userRole.getRoleName() == null) {
                    userRole.setRoleName(userRole.getRole().getName());
                }
                userRole.setRole(null);
                rolesDs.modifyItem(userRole);
            }
        }
        for (Object itemToDelete : ((AbstractDatasource) rolesDs).getItemsToDelete()) {
            if (itemToDelete instanceof UserRole && ((UserRole) itemToDelete).getRoleName() != null) {
                ((UserRole) itemToDelete).setRole(null);
            }
        }
        ((AbstractDatasource) rolesDs).setModified(isDsModified);

        if (rolesDs.isModified()) {
            @SuppressWarnings("unchecked")
            DatasourceImplementation<UserRole> rolesDsImpl = (DatasourceImplementation) rolesDs;

            CommitContext ctx = new CommitContext(Collections.emptyList(), rolesDsImpl.getItemsToDelete());
            dataSupplier.commit(ctx);

            List<UserRole> modifiedRoles = new ArrayList<>(rolesDsImpl.getItemsToCreate());
            modifiedRoles.addAll(rolesDsImpl.getItemsToUpdate());
            rolesDsImpl.committed(Collections.emptySet());
            for (UserRole userRole : modifiedRoles) {
                rolesDsImpl.modified(userRole);
            }
        }

        User user = getItem();

        if (PersistenceHelper.isNew(user)) {
            String password = passwField.getValue();
            String passwordConfirmation = confirmPasswField.getValue();

            if (passwField.isRequired() && (StringUtils.isBlank(password) || StringUtils.isBlank(passwordConfirmation))) {
                showNotification(getMessage("emptyPassword"), NotificationType.WARNING);
                return false;
            } else {
                if (Objects.equals(password, passwordConfirmation)) {
                    if (StringUtils.isNotEmpty(password)) {
                        ClientConfig passwordPolicyConfig = configuration.getConfig(ClientConfig.class);
                        if (passwordPolicyConfig.getPasswordPolicyEnabled()) {
                            String regExp = passwordPolicyConfig.getPasswordPolicyRegExp();
                            if (!password.matches(regExp)) {
                                showNotification(getMessage("simplePassword"), NotificationType.WARNING);
                                return false;
                            }
                        }

                        String passwordHash = passwordEncryption.getPasswordHash(user.getId(), password);
                        user.setPassword(passwordHash);
                    }

                    return true;
                } else {
                    showNotification(getMessage("passwordsDoNotMatch"), NotificationType.WARNING);
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    public void initCopy() {
        @SuppressWarnings("unchecked")
        DatasourceImplementation<UserRole> rolesDsImpl = (DatasourceImplementation) rolesDs;
        for (UserRole item : rolesDs.getItems()) {
            rolesDsImpl.modified(item);
        }
    }

    protected void publishUserSubstitutionsChanged(User user) {
        events.publish(new UserSubstitutionsChangedEvent(user));
    }

    protected void publishPasswordChangedEvent(User user, String newPassword) {
        events.publish(new UserPasswordChangedEvent(this, user, newPassword));
    }

    protected class AddRoleAction extends BaseAction {
        public AddRoleAction() {
            super("add");

            icon = icons.get(CubaIcon.ADD_ACTION);

            setCaption(messages.getMainMessage("actions.Add"));

            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
            setShortcut(clientConfig.getTableAddShortcut());
        }

        protected Collection<String> getExistingRoleNames() {
            User user = userDs.getItem();
            Collection<String> existingRoleNames = new HashSet<>();
            if (user.getUserRoles() != null) {
                for (UserRole userRole : user.getUserRoles()) {
                    if (userRole.getRole() != null)
                        existingRoleNames.add(userRole.getRole().getName());
                }
            }
            return existingRoleNames;
        }

        @Override
        public void actionPerform(Component component) {
            AbstractLookup roleLookupWindow = openLookup(Role.class, items -> {
                Collection<String> existingRoleNames = getExistingRoleNames();
                rolesDs.suspendListeners();
                try {
                    for (Object item : items) {
                        Role role = (Role) item;

                        if (existingRoleNames.contains(role.getName())) {
                            continue;
                        }

                        MetaClass metaClass = rolesDs.getMetaClass();
                        UserRole userRole = dataSupplier.newInstance(metaClass);
                        userRole.setRole(role);
                        userRole.setUser(userDs.getItem());

                        rolesDs.addItem(userRole);
                        existingRoleNames.add(role.getName());
                    }
                } finally {
                    rolesDs.resumeListeners();
                }
            }, OpenType.THIS_TAB, ParamsMap.of("windowOpener", "sec$User.edit"));

            roleLookupWindow.addCloseListener(actionId -> {
                rolesTable.focus();
            });

            Component lookupComponent = roleLookupWindow.getLookupComponent();
            if (lookupComponent instanceof Table) {
                ((Table) lookupComponent).setMultiSelect(true);
            }
        }
    }

    protected class EditRoleAction extends ItemTrackingAction {
        public EditRoleAction() {
            super("edit");

            icon = icons.get(CubaIcon.EDIT_ACTION);

            setCaption(getMessage("actions.Edit"));
        }

        @Override
        public void actionPerform(Component component) {
            if (rolesDs.getItem() == null)
                return;

            Window window = openEditor("sec$Role.edit", rolesDs.getItem().getRole(), OpenType.THIS_TAB);
            window.addCloseListener(actionId -> {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    rolesDs.refresh();
                }
                rolesTable.focus();
            });
        }
    }

    protected class RemoveRoleAction extends RemoveAction {

        private boolean hasDefaultRole = false;

        public RemoveRoleAction(ListComponent owner, boolean autocommit) {
            super(owner, autocommit);
        }

        @Override
        protected void confirmAndRemove(Set<Entity> selected) {
            hasDefaultRole = hasDefaultRole(selected);

            super.confirmAndRemove(selected);
        }

        @Override
        public String getConfirmationMessage() {
            if (hasDefaultRole)
                return getMessage("dialogs.Confirmation.RemoveDefaultRole");
            else
                return super.getConfirmationMessage();
        }

        private boolean hasDefaultRole(Set selected) {
            for (Object roleObj : selected) {
                UserRole role = (UserRole) roleObj;
                if (Boolean.TRUE.equals(role.getRole().getDefaultRole()))
                    return true;
            }
            return false;
        }
    }

    protected class AddSubstitutedAction extends BaseAction {
        public AddSubstitutedAction() {
            super("add");

            this.icon = icons.get(CubaIcon.ADD_ACTION);
            this.caption = messages.getMainMessage("actions.Add");

            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
            setShortcut(clientConfig.getTableAddShortcut());
        }

        @Override
        public void actionPerform(Component component) {
            UserSubstitution substitution = metadata.create(UserSubstitution.class);
            substitution.setUser(userDs.getItem());

            Window editor = openEditor(substitution, OpenType.DIALOG, ParamsMap.empty(), substitutionsDs);
            editor.addCloseListener(actionId -> {
                substTable.focus();
            });
        }
    }

    protected class EditSubstitutedAction extends ItemTrackingAction {
        public EditSubstitutedAction() {
            super("edit");

            this.icon = icons.get(CubaIcon.EDIT_ACTION);
            this.caption = messages.getMainMessage("actions.Edit");
        }

        @Override
        public void actionPerform(Component component) {
            if (substitutionsDs.getItem() != null) {
                Window editor = openEditor(substitutionsDs.getItem(), OpenType.DIALOG, ParamsMap.empty(), substitutionsDs);
                editor.addCloseListener(actionId -> {
                    substTable.focus();
                });
            }
        }
    }
}