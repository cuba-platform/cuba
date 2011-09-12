/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.security.entity.ScreenHistoryEntity;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ScreenHistorySupport {

    private List<String> screenIds;

    public ScreenHistorySupport() {
        ClientConfig config = ConfigProvider.getConfig(ClientConfig.class);
        String property = config.getScreenIdsToSaveHistory();
        if (property != null && StringUtils.isNotBlank(property))
            screenIds = Arrays.asList(StringUtils.split(property, ','));
    }

    public void saveScreenHistory(Window window, WindowManager.OpenType openType){
        if (window.getFrame() != null
                && (window.getFrame() instanceof Window.Editor)
                && !openType.equals(WindowManager.OpenType.DIALOG)
                && (screenIds == null || screenIds.contains(window.getId())))
        {
            String caption = window.getCaption();
            IFrame frame = window.getFrame();
            if (frame instanceof Window.Editor) {
                Instance entity = ((Window.Editor) frame).getItem();
                if (entity != null) {
                    if (PersistenceHelper.isNew(entity)) {
                        return;
                    }
                    caption = MessageUtils.getEntityCaption(entity.getMetaClass()) + " " + entity.getInstanceName();
                }
            }
            ScreenHistoryEntity screenHistoryEntity = MetadataProvider.create(ScreenHistoryEntity.class);
            screenHistoryEntity.setCaption(StringUtils.abbreviate(caption, 255));
            screenHistoryEntity.setUser(UserSessionProvider.getUserSession().getCurrentOrSubstitutedUser());
            screenHistoryEntity.setUrl(makeLink(window));

            CommitContext cc = new CommitContext(Collections.singleton(screenHistoryEntity));
            ServiceLocator.getDataService().commit(cc);
        }
    }

    protected String makeLink(Window window) {
        GlobalConfig c = ConfigProvider.getConfig(GlobalConfig.class);
        Entity entity = null;
        if (window.getFrame() instanceof Window.Editor)
            entity = ((Window.Editor) window.getFrame()).getItem();
        String url = "http://" + c.getWebHostName() + ":" + c.getWebPort() + "/" + c.getWebContextName() + "/open?" +
                "screen=" + window.getFrame().getId();
        if (entity != null) {
            String item = MetadataProvider.getSession().getClass(entity.getClass()).getName() + "-" + entity.getId();
            url += "&" + "item=" + item + "&" + "params=item:" + item;
        }
        Map<String, Object> params = window.getContext().getParams();
        StringBuilder sb = new StringBuilder();
        sb.append(",openFromScreenHistory:true");
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                Object value = param.getValue();
                if (value instanceof String /*|| value instanceof Integer || value instanceof Double*/
                        || value instanceof Boolean) {
                    try {
                        sb.append(",").append(param.getKey()).append(":").append(URLEncoder.encode(value.toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
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
}
