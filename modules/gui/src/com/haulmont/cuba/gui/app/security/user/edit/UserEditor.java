/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.user.edit;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionsLookup;
import com.haulmont.cuba.gui.app.security.user.NameBuilderListener;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.*;

public class UserEditor extends AbstractEditor {

    @Inject
    protected DsContext dsContext;

    @Inject
    protected DataService dataService;

    @Inject
    protected Datasource<User> userDs;

    @Inject
    protected CollectionDatasource<UserRole, UUID> rolesDs;

    @Inject
    protected CollectionDatasource<UserSubstitution, UUID> substitutionsDs;

    @Inject
    protected Table rolesTable;

    @Inject
    protected Table substTable;

    @Inject
    protected FieldGroup fieldGroup;

    protected TextField passwField;
    protected TextField confirmPasswField;
    protected LookupField languageLookup;
    protected PopupButton popupButton;

    @Inject
    protected Companion companion;

    @Inject
    protected UserSession userSession;

    @Inject
    protected ComponentsFactory factory;

    @Inject
    protected Configuration configuration;

    public interface Companion {
        void initPasswordField(TextField passwordField);
        void initLanguageLook(LookupField languageLook);
    }

    public void init(Map<String, Object> params) {
        userDs.addListener(new NameBuilderListener(fieldGroup));

        rolesTable.addAction(new AddRoleAction());
        rolesTable.addAction(new EditRoleAction());
        rolesTable.addAction(new RemoveRoleAction(rolesTable, false));

        substTable.addAction(new AddSubstitutedAction());
        substTable.addAction(new EditSubstitutedAction());
        substTable.addAction(new RemoveAction(substTable, false));

        setPermissionsShowAction(rolesTable, "show-screens", "sec$Target.screenPermissions.lookup", PermissionType.SCREEN);
        setPermissionsShowAction(rolesTable, "show-entities", "sec$Target.entityPermissions.lookup", PermissionType.ENTITY_OP);
        setPermissionsShowAction(rolesTable, "show-properties", "sec$Target.propertyPermissions.lookup", PermissionType.ENTITY_ATTR);
        setPermissionsShowAction(rolesTable, "show-specific", "sec$Target.specificPermissions.lookup", PermissionType.SPECIFIC);

        initCustomFields();

        dsContext.addListener(
                new DsContext.CommitListener() {
                    public void beforeCommit(CommitContext<Entity> context) {
                    }

                    public void afterCommit(CommitContext<Entity> context, Map<Entity, Entity> result) {
                        for (Map.Entry<Entity, Entity> entry : result.entrySet()) {
                            if (entry.getKey().equals(userSession.getUser())) {
                                userSession.setUser((User) entry.getValue());
                            }
                            if (entry.getKey().equals(userSession.getSubstitutedUser())) {
                                userSession.setSubstitutedUser((User) entry.getValue());
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);
        if (PersistenceHelper.isNew(item)) {
            addDefaultRoles();

            languageLookup.setValue(userSession.getLocale().getLanguage());
        }
    }

    private void addDefaultRoles() {
        LoadContext ctx = new LoadContext(Role.class);
        ctx.setQueryString("select r from sec$Role r where r.defaultRole = true");
        List<Role> defaultRoles = dataService.loadList(ctx);

        for (Role role : defaultRoles) {
            final MetaClass metaClass = rolesDs.getMetaClass();
            UserRole userRole = dataService.newInstance(metaClass);
            userRole.setRole(role);
            userRole.setUser(userDs.getItem());
            rolesDs.addItem(userRole);
        }
    }

    private void initCustomFields() {
        FieldGroup.Field f = fieldGroup.getField("permissionsLookupField");
        fieldGroup.addCustomField(f, new FieldGroup.CustomFieldGenerator() {
            public Component generateField(Datasource datasource, Object propertyId) {
                popupButton = factory.createComponent(PopupButton.NAME);
                popupButton.setCaption(getMessage("permissions"));
                popupButton.addAction(new PermissionLookupAction("screens", getMessage("screens"), "show-screens"));
                popupButton.addAction(new PermissionLookupAction("entities", getMessage("entities"), "show-entities"));
                popupButton.addAction(new PermissionLookupAction("properties", getMessage("properties"), "show-properties"));
                popupButton.addAction(new PermissionLookupAction("specific", getMessage("specific"), "show-specific"));

                return popupButton;
            }
        });

        f = fieldGroup.getField("passw");
        if (f != null) {
            fieldGroup.addCustomField(f, new FieldGroup.CustomFieldGenerator() {
                public Component generateField(Datasource datasource, Object propertyId) {
                    passwField = factory.createComponent(TextField.NAME);
                    passwField.setRequiredMessage(getMessage("passwMsg"));
                    passwField.setSecret(true);
                    if (companion != null)
                        companion.initPasswordField(passwField);
                    return passwField;
                }
            });
        }

        f = fieldGroup.getField("confirmPassw");
        if (f != null) {
            fieldGroup.addCustomField(f, new FieldGroup.CustomFieldGenerator() {
                public Component generateField(Datasource datasource, Object propertyId) {
                    confirmPasswField = factory.createComponent(TextField.NAME);
                    confirmPasswField.setSecret(true);
                    confirmPasswField.setRequiredMessage(getMessage("confirmPasswMsg"));
                    if (companion != null)
                        companion.initPasswordField(confirmPasswField);
                    return confirmPasswField;
                }
            });
        }

        f = fieldGroup.getField("language");
        fieldGroup.addCustomField(f, new FieldGroup.CustomFieldGenerator() {
            public Component generateField(Datasource datasource, Object propertyId) {
                languageLookup = factory.createComponent(LookupField.NAME);

                languageLookup.setDatasource(datasource, (String) propertyId);

                Map<String, Locale> locales = configuration.getConfig(GlobalConfig.class).getAvailableLocales();
                TreeMap<String, Object> options = new TreeMap<String, Object>();
                for (Map.Entry<String, Locale> entry : locales.entrySet()) {
                    options.put(entry.getKey(), entry.getValue().getLanguage());
                }
                languageLookup.setOptionsMap(options);
                if (companion != null)
                    companion.initLanguageLook(languageLookup);
                return languageLookup;
            }
        });
    }


    private void setPermissionsShowAction(ActionsHolder actionsHolder, String actionName, final String lookupAlias, final PermissionType permissionType) {
        actionsHolder.addAction(new AbstractAction(actionName) {
            public void actionPerform(Component component) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("showAccessOptions", false);
                final PermissionsLookup permissionsLookup = openLookup(lookupAlias, null, WindowManager.OpenType.THIS_TAB, params);
                permissionsLookup.setLookupHandler(new Lookup.Handler() {
                    public void handleLookup(Collection items) {
                        if(items.size()==0)
                            return;
                        StringBuilder sb = new StringBuilder();
                        UserSessionService uss = ServiceLocator.lookup(UserSessionService.NAME);
                        for (Object item : items) {
                            if (item == null) continue;
                            PermissionConfig.Target target = (PermissionConfig.Target)item;
                            Integer permissionValue =  uss.getPermissionValue(userDs.getItem(), permissionType, target.getPermissionValue());
                            String permissionStringValue = "";
                            if (permissionType == PermissionType.ENTITY_ATTR) {
                                if (permissionValue == null) permissionValue = 2;
                                permissionStringValue = EntityAttrAccess.fromId(permissionValue).toString();
                            } else {
                                if (permissionValue == null) permissionValue = 1;
                                permissionStringValue = (permissionValue == 1) ? "ALLOW" : "DENY";
                            }
                            sb.append(getMessage("permissionOn") +" "+ target.getPermissionValue()+" ("+target.getCaption()+")"+ " - ")
                                    .append(getMessage(permissionStringValue)).append("\n");
                        }
                        if (sb.length() == 0) {
                            showNotification("Please, ensure you've selected target attributes", NotificationType.WARNING);
                        } else
                            openWindow("sec$Permission.show", WindowManager.OpenType.DIALOG,
                                    Collections.<String,Object>singletonMap("message",sb.toString()));
                            //showNotification(sb.toString(), NotificationType.HUMANIZED);
                        if(popupButton != null)
                            popupButton.setPopupVisible(false);
                    }
                });
            }
        });
    }

    private boolean _commit() {
        if (rolesDs.isModified()) {
            DatasourceImplementation rolesDsImpl = (DatasourceImplementation) rolesDs;

            CommitContext ctx = new CommitContext(Collections.emptyList(), rolesDsImpl.getItemsToDelete());
            dataService.commit(ctx);

            ArrayList modifiedRoles = new ArrayList(rolesDsImpl.getItemsToCreate());
            modifiedRoles.addAll(rolesDsImpl.getItemsToUpdate());
            rolesDsImpl.commited(Collections.<Entity, Entity>emptyMap());
            for (Object userRole : modifiedRoles) {
                rolesDsImpl.modified((Entity) userRole);
            }
        }

        boolean isNew = PersistenceHelper.isNew(userDs.getItem());
        if (isNew) {
            String passw = passwField.getValue();
            String confPassw = confirmPasswField.getValue();
            if (StringUtils.isBlank(passw) || StringUtils.isBlank(confPassw)) {
                showNotification(getMessage("emptyPassword"), NotificationType.WARNING);
                return false;
            } else {
                if (ObjectUtils.equals(passw, confPassw)) {
                    ClientConfig passwordPolicyConfig = configuration.getConfig(ClientConfig.class);
                    if (passwordPolicyConfig.getPasswordPolicyEnabled()) {
                        String regExp = passwordPolicyConfig.getPasswordPolicyRegExp();
                        if (passw.matches(regExp)) {
                            return true;

                        } else {
                            showNotification(getMessage("simplePassword"), NotificationType.WARNING);
                            return false;
                        }
                    } else {
                        userDs.getItem().setPassword(DigestUtils.md5Hex(passw));
                        return true;
                    }
                } else {
                    showNotification(getMessage("passwordsDoNotMatch"), NotificationType.WARNING);
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    public boolean commit() {
        return _commit() && super.commit();
    }

    @Override
    public void commitAndClose() {
        if (_commit()) {
            super.commitAndClose();
        }
    }

    public void initCopy() {
        for (UUID id : rolesDs.getItemIds()) {
            ((DatasourceImplementation)rolesDs).modified(rolesDs.getItem(id));
        }
    }

    private class AddRoleAction extends AbstractAction {

        public AddRoleAction() {
            super("add");
        }

        public void actionPerform(Component component) {
            Map<String, Object> lookupParams = Collections.<String, Object>singletonMap("windowOpener", "sec$User.edit");
            openLookup("sec$Role.browse", new Lookup.Handler() {
                public void handleLookup(Collection items) {
                    Collection<String> existingRoleNames = getExistingRoleNames();
                    for (Object item : items) {
                        Role role = (Role)item;
                        if (existingRoleNames.contains(role.getName())) continue;

                        final MetaClass metaClass = rolesDs.getMetaClass();
                        UserRole userRole = dataService.newInstance(metaClass);
                        userRole.setRole(role);
                        userRole.setUser(userDs.getItem());

                        rolesDs.addItem(userRole);
                        existingRoleNames.add(role.getName());
                    }
                }

                private Collection<String> getExistingRoleNames() {
                    User user = userDs.getItem();
                    Collection<String> existingRoleNames = new HashSet<String>();
                    if (user.getUserRoles() != null) {
                        for (UserRole userRole : user.getUserRoles()) {
                            if (userRole.getRole() != null)
                                existingRoleNames.add(userRole.getRole().getName());
                        }
                    }
                    return existingRoleNames;
                }

            }, WindowManager.OpenType.THIS_TAB, lookupParams);
        }
    }

    private class EditRoleAction extends AbstractAction {

        public EditRoleAction() {
            super("edit");
        }

        public void actionPerform(Component component) {
            if (rolesDs.getItem() == null)
                return;
            Window window = openEditor("sec$Role.edit", rolesDs.getItem().getRole(), WindowManager.OpenType.THIS_TAB);
            window.addListener(new CloseListener() {
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        rolesDs.refresh();
                    }
                }
            });
        }

        @Override
        public String getCaption() {
            return getMessage("actions.Edit");
        }
    }

    private class RemoveRoleAction extends RemoveAction {

        private boolean hasDefaultRole = false;

        public RemoveRoleAction(ListComponent owner, boolean autocommit) {
            super(owner, autocommit);
        }

        @Override
        protected void confirmAndRemove(Set selected) {
            hasDefaultRole = hasDefaultRole(selected);
            super.confirmAndRemove(selected);
        }

        @Override
        protected String getConfirmationMessage(String messagesPackage) {
            if (hasDefaultRole)
                return getMessage("dialogs.Confirmation.RemoveDefaultRole");
            else
                return super.getConfirmationMessage(messagesPackage);
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

    private class AddSubstitutedAction extends AbstractAction {
        public AddSubstitutedAction() {
            super("add");
        }

        public void actionPerform(Component component) {
            final UserSubstitution substitution = MetadataProvider.create(UserSubstitution.class);
            substitution.setUser(userDs.getItem());

            Map<String, Object> params = new HashMap();

            if (!substitutionsDs.getItemIds().isEmpty()) {
                List<UUID> list = new ArrayList();
                for (UUID usId : substitutionsDs.getItemIds()) {
                    list.add(substitutionsDs.getItem(usId).getSubstitutedUser().getId());
                }
                params.put("existingIds", list);
            }

            getDialogParams().setWidth(500);

            openEditor("sec$UserSubstitution.edit", substitution,
                    WindowManager.OpenType.DIALOG, params, substitutionsDs);
        }
    }

    private class EditSubstitutedAction extends AbstractAction {
        public EditSubstitutedAction() {
            super("edit");
        }

        public void actionPerform(Component component) {
            getDialogParams().setWidth(450);

            if (substitutionsDs.getItem() != null)
                openEditor("sec$UserSubstitution.edit", substitutionsDs.getItem(),
                        WindowManager.OpenType.DIALOG, substitutionsDs);
        }
    }

    private class PermissionLookupAction extends AbstractAction{

        private String caption;
        private String screen;

        private PermissionLookupAction(String id, String caption, String screen) {
            super(id);
            this.caption = caption;
            this.screen = screen;
        }

        public void actionPerform(Component component) {
            rolesTable.getAction(screen).actionPerform(rolesTable);
        }

        @Override
        public String getCaption() {
            return caption;
        }
    }
}
