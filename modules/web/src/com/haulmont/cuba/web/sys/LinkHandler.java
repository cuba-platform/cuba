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
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.exception.AccessDeniedHandler;
import com.haulmont.cuba.gui.exception.EntityAccessExceptionHandler;
import com.haulmont.cuba.gui.exception.NoSuchScreenHandler;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.actions.ChangeSubstUserAction;
import com.haulmont.cuba.web.actions.DoNotChangeSubstUserAction;
import com.haulmont.cuba.web.app.folders.Folders;
import com.vaadin.server.Page;
import com.vaadin.ui.JavaScript;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Handles links from outside of the application.
 * <p/> This bean is used particularly when a request URL contains one of
 * {@link com.haulmont.cuba.web.WebConfig#getLinkHandlerActions()} actions.
 */
@org.springframework.stereotype.Component(LinkHandler.NAME)
@Scope("prototype")
public class LinkHandler {

    public static final String NAME = "cuba_LinkHandler";

    private Logger log = LoggerFactory.getLogger(LinkHandler.class);

    @Inject
    protected Messages messages;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected DataService dataService;

    @Inject
    protected Folders folders;

    @Inject
    protected Metadata metadata;

    @Inject
    protected AccessDeniedHandler accessDeniedHandler;
    @Inject
    protected NoSuchScreenHandler noSuchScreenHandler;
    @Inject
    protected EntityAccessExceptionHandler entityAccessExceptionHandler;

    protected App app;
    protected String action;
    protected Map<String, String> requestParams;

    public LinkHandler(App app, String action, Map<String, String> requestParams) {
        this.app = app;
        this.action = action;
        this.requestParams = requestParams;
    }

    /**
     * Check state of LinkHandler and application.
     *
     * @return true if application and LinkHandler in an appropriate state.
     */
    public boolean canHandleLink() {
        return app.getTopLevelWindow() instanceof Window.HasWorkArea;
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
                    log.warn("Folder not found: {}", folderId);
                }
                return;
            }

            String screenName = requestParams.get("screen");
            if (screenName == null) {
                log.warn("ScreenId not found in request parameters");
                return;
            }

            WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
            final WindowInfo windowInfo = windowConfig.getWindowInfo(screenName);
            if (windowInfo == null) {
                log.warn("WindowInfo not found for screen: {}", screenName);
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
            } else {
                openWindow(windowInfo, requestParams);
            }
        } catch (AccessDeniedException e) {
            accessDeniedHandler.handle(e, app.getWindowManager());
        } catch (NoSuchScreenException e) {
            noSuchScreenHandler.handle(e, app.getWindowManager());
        } catch (EntityAccessException e) {
            entityAccessExceptionHandler.handle(e, app.getWindowManager());
        } finally {
            requestParams.clear();
        }
    }

    protected void substituteUserAndOpenWindow(final WindowInfo windowInfo, UUID userId) {
        UserSession userSession = app.getConnection().getSession();
        final User substitutedUser = loadUser(userId, userSession.getUser());
        if (substitutedUser != null) {
            final Map<String, String> currentRequestParams = new HashMap<>(requestParams);

            app.getWindowManager().showOptionDialog(
                    messages.getMainMessage("toSubstitutedUser.title"),
                    getDialogMessage(substitutedUser),
                    Frame.MessageType.CONFIRMATION_HTML,
                    new Action[]{
                            new ChangeSubstUserAction(substitutedUser) {
                                @Override
                                public void doAfterChangeUser() {
                                    super.doAfterChangeUser();
                                    openWindow(windowInfo, currentRequestParams);
                                }

                                @Override
                                public void doRevert() {
                                    super.doRevert();

                                    JavaScript js = Page.getCurrent().getJavaScript();
                                    js.execute("window.close();");
                                }

                                @Override
                                public String getCaption() {
                                    return messages.getMainMessage("action.switch");
                                }
                            },
                            new DoNotChangeSubstUserAction() {
                                @Override
                                public void actionPerform(Component component) {
                                    super.actionPerform(component);

                                    JavaScript js = Page.getCurrent().getJavaScript();
                                    js.execute("window.close();");
                                }

                                @Override
                                public String getCaption() {
                                    return messages.getMainMessage("action.cancel");
                                }
                            }
                    });
        } else {
            User user = loadUser(userId);
            app.getWindowManager().showOptionDialog(
                    messages.getMainMessage("warning.title"),
                    getWarningMessage(user),
                    Frame.MessageType.WARNING_HTML,
                    new Action[]{
                            new DialogAction(DialogAction.Type.OK) {
                                @Override
                                public void actionPerform(Component component) {
                                    JavaScript js = Page.getCurrent().getJavaScript();
                                    js.execute("window.close();");
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
            return messages.getMainMessage("warning.userNotFound");
        return messages.formatMainMessage(
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
        LoadContext<User> loadContext = new LoadContext<>(User.class);
        LoadContext.Query query = new LoadContext.Query("select u from sec$User u where u.id = :userId");
        query.setParameter("userId", userId);
        loadContext.setQuery(query);
        List<User> users = dataService.loadList(loadContext);
        return users.isEmpty() ? null : users.get(0);
    }

    protected String getDialogMessage(User user) {
        return messages.formatMainMessage(
                "toSubstitutedUser.msg",
                StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName()
        );
    }

    protected void openWindow(WindowInfo windowInfo, Map<String, String> requestParams) {
        String itemStr = requestParams.get("item");
        String openTypeParam = requestParams.get("openType");
        OpenType openType = OpenType.NEW_TAB;

        if (StringUtils.isNotEmpty(openTypeParam)) {
            try {
                openType = OpenType.valueOf(openTypeParam);
            } catch (IllegalArgumentException e) {
                log.warn("Unknown open type ({}) in request parameters", openTypeParam);
            }
        }

        if (itemStr == null) {
            app.getWindowManager().openWindow(windowInfo, openType, getParamsMap(requestParams));
        } else {
            EntityLoadInfo info = EntityLoadInfo.parse(itemStr);
            if (info == null) {
                log.warn("Invalid item definition: {}", itemStr);
            } else {
                Entity entity = loadEntityInstance(info);
                if (entity != null)
                    app.getWindowManager().openEditor(windowInfo, entity, openType, getParamsMap(requestParams));
                else
                    throw new EntityAccessException();
            }
        }
    }

    protected Map<String, Object> getParamsMap(Map<String, String> requestParams) {
        Map<String, Object> params = new HashMap<>();
        String paramsStr = requestParams.get("params");
        if (paramsStr == null)
            return params;

        String[] entries = paramsStr.split(",");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length != 2) {
                log.warn("Invalid parameter: {}", entry);
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
        if (info.isNewEntity()) {
            return metadata.create(info.getMetaClass());
        }

        LoadContext ctx = new LoadContext(info.getMetaClass()).setId(info.getId());
        if (info.getViewName() != null)
            ctx.setView(info.getViewName());
        Entity entity;
        try {
            entity = dataService.load(ctx);
        } catch (Exception e) {
            log.warn("Unable to load item: {}", info, e);
            return null;
        }
        return entity;
    }

    protected AbstractSearchFolder loadFolder(UUID folderId) {
        return dataService.load(new LoadContext<>(AbstractSearchFolder.class).setId(folderId));
    }
}