/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.actions.ChangeSubstUserAction;
import com.haulmont.cuba.web.actions.DoNotChangeSubstUserAction;
import com.haulmont.cuba.web.app.folders.Folders;
import com.haulmont.cuba.web.exception.AccessDeniedHandler;
import com.haulmont.cuba.web.exception.EntityAccessExceptionHandler;
import com.haulmont.cuba.web.exception.NoSuchScreenHandler;
import com.vaadin.server.Page;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Handles links from outside of the application.
 * <p/> This bean is used particularly when a request URL contains one of
 * {@link com.haulmont.cuba.web.WebConfig#getLinkHandlerActions()} actions.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(LinkHandler.NAME)
@Scope("prototype")
public class LinkHandler {

    public static final String NAME = "cuba_LinkHandler";

    protected Log log = LogFactory.getLog(getClass());

    @Inject
    protected Messages messages;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected DataService dataService;

    @Inject
    protected Folders folders;

    protected App app;
    protected String action;
    protected Map<String, String> requestParams;

    public LinkHandler(App app, String action, Map<String, String> requestParams) {
        this.app = app;
        this.action = action;
        this.requestParams = requestParams;
    }

    /**
     * Called to handle the link.
     */
    public void handle() {
        try {
            String folderId = requestParams.get("folder");
            if (!StringUtils.isEmpty(folderId)) {
                AbstractSearchFolder folder = loadFolder(UUID.fromString(folderId));
                if (folder != null) {
                    folders.openFolder(folder);
                } else {
                    log.warn("Folder not found: " + folderId);
                }
                return;
            }


            String screenName = requestParams.get("screen");
            if (screenName == null) {
                log.warn("ScreenId not found in request parameters");
                return;
            }

            WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
            final WindowInfo windowInfo = windowConfig.getWindowInfo(screenName);
            if (windowInfo == null) {
                log.warn("WindowInfo not found for screen: " + screenName);
                return;
            }

            UUID userId = getUUID(requestParams.get("user"));
            UserSession userSession = app.getConnection().getSession();
            if (userSession == null) {
                log.warn("No user session");
                return;
            }
            if (!(userId == null || userSession.getCurrentOrSubstitutedUser().getId().equals(userId))) {
                substituteUserAndOpenWindow(windowInfo, userId);
            } else
                openWindow(windowInfo);
        } catch (AccessDeniedException e) {
            new AccessDeniedHandler().handle(e, app);
        } catch (NoSuchScreenException e) {
            new NoSuchScreenHandler().handle(e, app);
        } catch (EntityAccessException e) {
            new EntityAccessExceptionHandler().handle(e, app);
        }
    }

    protected void substituteUserAndOpenWindow(final WindowInfo windowInfo, UUID userId) {
        UserSession userSession = app.getConnection().getSession();
        final User substitutedUser = loadUser(userId, userSession.getUser());
        if (substitutedUser != null)
            app.getWindowManager().showOptionDialog(
                    messages.getMessage(getClass(), "toSubstitutedUser.title"),
                    getDialogMessage(substitutedUser),
                    IFrame.MessageType.CONFIRMATION,
                    new Action[]{
                            new ChangeSubstUserAction(substitutedUser) {
                                @Override
                                public void doAfterChangeUser() {
                                    super.doAfterChangeUser();
                                    openWindow(windowInfo);
                                }

                                @Override
                                public void doRevert() {
                                    super.doRevert();
                                    Page.getCurrent().getJavaScript().execute("window.close();");
                                }

                                @Override
                                public String getCaption() {
                                    return messages.getMessage(getClass(), "action.switch");
                                }
                            },
                            new DoNotChangeSubstUserAction() {
                                @Override
                                public void actionPerform(Component component) {
                                    super.actionPerform(component);

                                    Page.getCurrent().getJavaScript().execute("window.close();");
                                }

                                @Override
                                public String getCaption() {
                                    return messages.getMessage(getClass(), "action.cancel");
                                }
                            }
                    });
        else {
            User user = loadUser(userId);
            app.getWindowManager().showOptionDialog(
                    messages.getMessage(getClass(), "warning.title"),
                    getWarningMessage(user),
                    IFrame.MessageType.WARNING,
                    new Action[]{
                            new DialogAction(DialogAction.Type.OK) {
                                @Override
                                public void actionPerform(Component component) {
                                    Page.getCurrent().getJavaScript().execute("window.close();");
                                }
                            }
                    });
        }
    }

    protected UUID getUUID(String id) {
        if (StringUtils.isBlank(id))
            return null;

        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            uuid = null;
        }
        return uuid;
    }

    protected String getWarningMessage(User user) {
        if (user == null)
            return messages.getMessage(getClass(), "warning.userNotFound");
        return messages.formatMessage(
                getClass(),
                "warning.msg",
                StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName()
        );
    }

    protected User loadUser(UUID userId, User user) {
        if (user.getId().equals(userId))
            return user;
        LoadContext loadContext = new LoadContext(UserSubstitution.class);
        LoadContext.Query query = new LoadContext.Query("select su from sec$UserSubstitution us join us.user u " +
                "join us.substitutedUser su where u.id = :id and su.id = :userId and " +
                "(us.endDate is null or us.endDate >= :currentDate) and (us.startDate is null or us.startDate <= :currentDate)");
        query.setParameter("id", user);
        query.setParameter("userId", userId);
        query.setParameter("currentDate", timeSource.currentTimestamp());
        loadContext.setQuery(query);
        List<User> users = dataService.loadList(loadContext);
        return users.isEmpty() ? null : users.get(0);
    }
    
    protected User loadUser(UUID userId) {
        LoadContext loadContext = new LoadContext(User.class);
        LoadContext.Query query = new LoadContext.Query("select u from sec$User u where u.id = :userId");
        query.setParameter("userId", userId);
        loadContext.setQuery(query);
        List<User> users = dataService.loadList(loadContext);
        return users.isEmpty() ? null : users.get(0);
    }

    protected String getDialogMessage(User user) {
        return messages.formatMessage(
                getClass(),
                "toSubstitutedUser.msg",
                StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName()
        );
    }

    protected void openWindow(WindowInfo windowInfo) {
        String itemStr = requestParams.get("item");
        String openTypeParam = requestParams.get("openType");
        WindowManager.OpenType openType = WindowManager.OpenType.NEW_TAB;

        if (StringUtils.isNotBlank(openTypeParam)) {
            try {
                openType = WindowManager.OpenType.valueOf(openTypeParam);
            } catch (IllegalArgumentException e) {
                log.warn("Unknown open type (" + openTypeParam + ") in request parameters");
            }
        }

        if (itemStr == null) {
            app.getWindowManager().openWindow(windowInfo, openType, getParamsMap());
        } else {
            EntityLoadInfo info = EntityLoadInfo.parse(itemStr);
            if (info == null) {
                log.warn("Invalid item definition: " + itemStr);
            } else {
                Entity entity = loadEntityInstance(info);
                if (entity != null)
                    app.getWindowManager().openEditor(windowInfo, entity, openType, getParamsMap());
                else
                    throw new EntityAccessException();
            }
        }
    }

    protected Map<String, Object> getParamsMap() {
        Map<String, Object> params = new HashMap<>();
        String paramsStr = requestParams.get("params");
        if (paramsStr == null)
            return params;

        String[] entries = paramsStr.split(",");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length != 2) {
                log.warn("Invalid parameter: " + entry);
                return params;       
            }
            String name = parts[0];
            String value = parts[1];
            EntityLoadInfo info = EntityLoadInfo.parse(value);
            if (info != null) {
                Entity entity = loadEntityInstance(info);
                if (entity != null)
                    params.put(name, entity);
            } else if (Boolean.TRUE.toString().equals(value) || Boolean.FALSE.toString().equals(value)) {
                params.put(name, BooleanUtils.toBoolean(value));
            } else {
                params.put(name, value);
            }
        }
        return params;
    }

    protected Entity loadEntityInstance(EntityLoadInfo info) {
        LoadContext ctx = new LoadContext(info.getMetaClass()).setId(info.getId());
        if (info.getViewName() != null)
            ctx.setView(info.getViewName());
        Entity entity;
        try {
            entity = dataService.load(ctx);
        } catch (Exception e) {
            log.warn("Unable to load item: " + info, e);
            return null;
        }
        return entity;
    }

    protected AbstractSearchFolder loadFolder(UUID folderId) {
        LoadContext ctx = new LoadContext(Folder.class).setId(folderId);
        return dataService.load(ctx);
    }
}