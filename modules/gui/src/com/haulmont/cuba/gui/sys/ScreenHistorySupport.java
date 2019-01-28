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

package com.haulmont.cuba.gui.sys;

import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.TrackEditScreenHistory;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.events.AppContextInitializedEvent;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.ScreenHistoryEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;

/**
 * Encapsulates screen opening history functionality. Should not be invoked from application code.
 */
@Component(ScreenHistorySupport.NAME)
public class ScreenHistorySupport {

    public static final String NAME = "cuba_ScreenHistorySupport";

    protected final Set<String> screenIds = new HashSet<>();

    @Inject
    protected Metadata metadata;
    @Inject
    protected Messages messages;
    @Inject
    protected Configuration configuration;
    @Inject
    protected Security security;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected ReferenceToEntitySupport referenceToEntitySupport;

    @EventListener(AppContextInitializedEvent.class)
    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 300)
    protected void init() {
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        String property = clientConfig.getScreenIdsToSaveHistory();
        if (StringUtils.isNotBlank(property)) {
            screenIds.addAll(Arrays.asList(StringUtils.split(property, ',')));
        }

        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            Map<String, Object> attributes = metadata.getTools().getMetaAnnotationAttributes(metaClass.getAnnotations(),
                    TrackEditScreenHistory.class);
            if (Boolean.TRUE.equals(attributes.get("value"))) {
                screenIds.add(windowConfig.getEditorScreenId(metaClass));
            }
        }
    }

    public void saveScreenHistory(Screen frameOwner) {
        WindowContext windowContext = frameOwner.getWindow().getContext();

        if (security.isEntityOpPermitted(ScreenHistoryEntity.class, EntityOp.CREATE)
                && (frameOwner instanceof EditorScreen)
                && windowContext.getLaunchMode() != OpenMode.DIALOG
                && (screenIds.contains(frameOwner.getId()))) {
            String caption = frameOwner.getWindow().getCaption();
            Object entityId = null;

            Entity entity = ((EditorScreen) frameOwner).getEditedEntity();
            if (entity != null) {
                if (PersistenceHelper.isNew(entity)) {
                    return;
                }

                if (StringUtils.isBlank(caption)) {
                    caption = messages.getTools().getEntityCaption(entity.getMetaClass()) + " " +
                            metadata.getTools().getInstanceName(entity);
                }
                entityId = referenceToEntitySupport.getReferenceIdForLink(entity);
            }

            ScreenHistoryEntity screenHistoryEntity = metadata.create(ScreenHistoryEntity.class);
            screenHistoryEntity.setCaption(StringUtils.abbreviate(caption, 255));
            screenHistoryEntity.setUrl(makeLink(frameOwner));
            screenHistoryEntity.setObjectEntityId(entityId);
            addAdditionalFields(screenHistoryEntity, entity);

            CommitContext cc = new CommitContext(Collections.singleton(screenHistoryEntity));
            dataManager.commit(cc);
        }
    }

    protected void addAdditionalFields(ScreenHistoryEntity screenHistoryEntity, Entity entity) {

    }

    protected String makeLink(Screen frameOwner) {
        Window window = frameOwner.getWindow();

        Entity entity = null;
        if (window.getFrameOwner() instanceof EditorScreen) {
            entity = ((EditorScreen) frameOwner).getEditedEntity();
        }

        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
        String url = String.format("%s/open?screen=%s", globalConfig.getWebAppUrl(), frameOwner.getId());

        Object entityId = referenceToEntitySupport.getReferenceIdForLink(entity);

        if (entity != null) {
            String item;
            if (entityId instanceof String) {
                item = metadata.getClassNN(entity.getClass()).getName() + "-{" + entityId + "}";
            } else {
                item = metadata.getClassNN(entity.getClass()).getName() + "-" + entityId;
            }

            url += String.format("&item=%s&params=item:%s", item, item);
        }
        Map<String, Object> params = getWindowParams(window);
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                Object value = param.getValue();
                if (value instanceof String /*|| value instanceof Integer || value instanceof Double*/
                        || value instanceof Boolean) {
                    sb.append(",").append(param.getKey()).append(":")
                            .append(URLEncodeUtils.encodeUtf8(value.toString()));
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