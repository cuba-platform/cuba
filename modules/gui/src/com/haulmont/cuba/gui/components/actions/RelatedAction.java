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

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.RelatedEntities;
import com.haulmont.cuba.gui.relatedentities.RelatedEntitiesAPI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;

/**
 * Action used in {@link RelatedEntities} visual component.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 * &lt;bean id="cuba_RelatedAction" class="com.company.sample.gui.MyRelatedAction" scope="prototype"/&gt;
 * </pre>
 */
@org.springframework.stereotype.Component("cuba_RelatedAction")
@Scope("prototype")
public class RelatedAction extends ListAction implements Action.HasBeforeActionPerformedHandler {

    public static final String ACTION_ID = "related";

    protected final MetaProperty metaProperty;
    protected final MetaClass metaClass;

    protected String screen;
    protected String filterCaption;

    protected OpenType openType = OpenType.THIS_TAB;

    @Inject
    protected RelatedEntitiesAPI relatedEntitiesApi;

    protected BeforeActionPerformedHandler beforeActionPerformedHandler;

    /**
     * Creates an action with the given id.
     * @param target    component containing this action
     */
    public static RelatedAction create(String id, ListComponent target, MetaClass metaClass, MetaProperty metaProperty) {
        return AppBeans.getPrototype("cuba_RelatedAction", id, target, metaClass, metaProperty);
    }

    public RelatedAction(String id, ListComponent target, MetaClass metaClass, MetaProperty metaProperty) {
        super(id);

        this.target = target;
        this.metaClass = metaClass;
        this.metaProperty = metaProperty;
    }

    @Inject
    protected void setMessageTools(MessageTools messageTools) {
        if (messageTools != null) {
            setCaption(StringUtils.capitalize(messageTools.getPropertyCaption(metaClass, metaProperty.getName())));
        }
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public void setFilterCaption(String filterCaption) {
        this.filterCaption = filterCaption;
    }

    public void setOpenType(OpenType openType) {
        this.openType = openType;
    }

    @Override
    public void actionPerform(Component component) {
        if (beforeActionPerformedHandler != null) {
            if (!beforeActionPerformedHandler.beforeActionPerformed())
                return;
        }

        RelatedEntitiesAPI.RelatedScreenDescriptor descriptor = new RelatedEntitiesAPI.RelatedScreenDescriptor(screen, openType);
        descriptor.setFilterCaption(filterCaption);

        //noinspection unchecked
        relatedEntitiesApi.openRelatedScreen(target.getSelected(), metaClass, metaProperty, descriptor);
    }

    @Override
    public BeforeActionPerformedHandler getBeforeActionPerformedHandler() {
        return beforeActionPerformedHandler;
    }

    @Override
    public void setBeforeActionPerformedHandler(BeforeActionPerformedHandler handler) {
        beforeActionPerformedHandler = handler;
    }
}