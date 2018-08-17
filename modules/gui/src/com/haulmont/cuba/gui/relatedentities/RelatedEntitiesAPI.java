/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.relatedentities;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;

import java.util.Collection;
import java.util.Map;

public interface RelatedEntitiesAPI {

    String NAME = "cuba_RelatedEntities";

    /**
     * Shows found related entities in default browse screen.
     *
     * @param selectedEntities set of entities which represents one side of relation
     * @param metaClass        metaClass of single entity from <code>selectedEntities</code>
     * @param metaProperty     chosen field to find related entities. Can be obtained from <code>metaClass</code>
     */
    void openRelatedScreen(Collection<? extends Entity> selectedEntities, MetaClass metaClass, MetaProperty metaProperty);

    /**
     * Shows found related entities in chosen screen.
     *
     * @param selectedEntities set of entities which represents one side of relation
     * @param metaClass        metaClass of single entity from <code>selectedEntities</code>
     * @param metaProperty     chosen field to find related entities. Can be obtained from <code>metaClass</code>
     * @param descriptor       descriptor contains screen id, {@link WindowManager.OpenType} and
     *                         generated filter caption
     */
    void openRelatedScreen(Collection<? extends Entity> selectedEntities, MetaClass metaClass, MetaProperty metaProperty,
                           RelatedScreenDescriptor descriptor);

    /**
     * Shows found related entities in default browse screen.
     *
     * @param selectedEntities set of entities which represents one side of relation
     * @param clazz            class of single entity from <code>selectedEntities</code>
     * @param property         chosen field to find related entities
     */
    <T extends Entity> void openRelatedScreen(Collection<T> selectedEntities, Class<T> clazz, String property);

    /**
     * Shows found related entities in chosen screen.
     *
     * @param selectedEntities set of entities which represents one side of relation
     * @param clazz            class of single entity from <code>selectedEntities</code>
     * @param property         chosen field to find related entities
     * @param descriptor       descriptor contains screen id, {@link WindowManager.OpenType} and
     *                         generated filter caption
     */
    <T extends Entity> void openRelatedScreen(Collection<T> selectedEntities, Class<T> clazz, String property,
                           RelatedScreenDescriptor descriptor);

    class RelatedScreenDescriptor {

        protected String screenId;
        protected WindowManager.OpenType openType = WindowManager.OpenType.THIS_TAB;
        protected String filterCaption;
        protected Map<String, Object> screenParams;

        public RelatedScreenDescriptor(String screenId, WindowManager.OpenType openType) {
            this.screenId = screenId;
            this.openType = openType;
        }

        public RelatedScreenDescriptor(String screenId) {
            this(screenId, WindowManager.OpenType.THIS_TAB);
        }

        public RelatedScreenDescriptor() {
        }

        public String getScreenId() {
            return screenId;
        }

        public WindowManager.OpenType getOpenType() {
            return openType;
        }

        public String getFilterCaption() {
            return filterCaption;
        }

        public Map<String, Object> getScreenParams() {
            return screenParams;
        }

        public void setScreenId(String screenId) {
            this.screenId = screenId;
        }

        public void setOpenType(WindowManager.OpenType openType) {
            this.openType = openType;
        }

        public void setFilterCaption(String filterCaption) {
            this.filterCaption = filterCaption;
        }

        public void setScreenParams(Map<String, Object> screenParams) {
            this.screenParams = screenParams;
        }
    }
}