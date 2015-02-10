/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.TrackEditScreenHistory;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.ScreenHistoryEntity;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Class that encapsulates screen opening history functionality. It is used by WindowManager and should not be invoked
 * from application code.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ScreenHistorySupport {

    protected Set<String> screenIds = new HashSet<>();

    protected Metadata metadata;
    protected Messages messages;
    protected UserSessionSource uss;
    protected Configuration configuration;

    public ScreenHistorySupport() {
        metadata = AppBeans.get(Metadata.NAME);
        messages = AppBeans.get(Messages.NAME);
        uss = AppBeans.get(UserSessionSource.NAME);
        configuration = AppBeans.get(Configuration.NAME);

        String property = configuration.getConfig(ClientConfig.class).getScreenIdsToSaveHistory();
        if (StringUtils.isNotBlank(property)) {
            screenIds.addAll(Arrays.asList(StringUtils.split(property, ',')));
        }

        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            Boolean value = (Boolean) metaClass.getAnnotations().get(TrackEditScreenHistory.class.getName());
            if (BooleanUtils.isTrue(value)) {
                screenIds.add(windowConfig.getEditorScreenId(metaClass));
            }
        }
    }

    public void saveScreenHistory(Window window, WindowManager.OpenType openType) {
        Security security = AppBeans.get(Security.NAME);
        if (security.isEntityOpPermitted(ScreenHistoryEntity.class, EntityOp.CREATE)
                && window.getFrame() != null
                && (window.getFrame() instanceof Window.Editor)
                && !openType.equals(WindowManager.OpenType.DIALOG)
                && (screenIds == null || screenIds.contains(window.getId())))
        {
            String caption = window.getCaption();
            UUID entityId = null;
            IFrame frame = window.getFrame();
            Entity entity = null;
            if (frame instanceof Window.Editor) {
                entity = ((Window.Editor) frame).getItem();
                if (entity != null) {
                    if (PersistenceHelper.isNew(entity)) {
                        return;
                    }
                    if (StringUtils.isBlank(caption))
                        caption = messages.getTools().getEntityCaption(entity.getMetaClass()) + " " + entity.getInstanceName();
                    entityId = (UUID) entity.getId();
                }
            }
            ScreenHistoryEntity screenHistoryEntity = metadata.create(ScreenHistoryEntity.class);
            screenHistoryEntity.setCaption(StringUtils.abbreviate(caption, 255));
            screenHistoryEntity.setUrl(makeLink(window));
            screenHistoryEntity.setEntityId(entityId);
            addAdditionalFields(screenHistoryEntity, entity);

            CommitContext cc = new CommitContext(Collections.singleton(screenHistoryEntity));
            DataService dataService = AppBeans.get(DataService.NAME);
            dataService.commit(cc);
        }
    }

    protected void addAdditionalFields(ScreenHistoryEntity screenHistoryEntity, Entity entity) {

    }

    protected String makeLink(Window window) {
        Entity entity = null;
        if (window.getFrame() instanceof Window.Editor)
            entity = ((Window.Editor) window.getFrame()).getItem();
        String url = configuration.getConfig(GlobalConfig.class).getWebAppUrl() + "/open?" +
                "screen=" + window.getFrame().getId();
        if (entity != null) {
            String item = metadata.getSession().getClassNN(entity.getClass()).getName() + "-" + entity.getId();
            url += "&" + "item=" + item + "&" + "params=item:" + item;
        }
        Map<String, Object> params = getWindowParams(window);
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                Object value = param.getValue();
                if (value instanceof String /*|| value instanceof Integer || value instanceof Double*/
                        || value instanceof Boolean) {
                    try {
                        sb.append(",").append(param.getKey()).append(":").append(URLEncoder.encode(value.toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        // impossible
                    }
                }
            }
        }
        if (sb.length() > 0) {
            if (entity != null) {
                url += sb.toString();
            } else {
                url += "&params=" + sb.deleteCharAt(0).toString();
            }
        }

        return url;
    }

    protected Map<String, Object> getWindowParams(Window window) {
        return window.getContext().getParams();
    }

}
