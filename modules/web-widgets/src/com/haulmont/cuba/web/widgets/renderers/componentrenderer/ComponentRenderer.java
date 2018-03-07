/*
 * Licensed under the Apache License,Version2.0(the"License");you may not
 * use this file except in compliance with the License.You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.haulmont.cuba.web.widgets.renderers.componentrenderer;

import com.vaadin.v7.data.Item;
import com.vaadin.server.ClientConnector;
import com.vaadin.v7.server.communication.data.DataGenerator;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Grid;
import com.vaadin.ui.UI;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

import java.util.HashMap;

/**
 * A renderer for vaadin trackedComponents.
 *
 * Every component column must use its own renderer, so tracking the trackedComponents by column works correctly.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends Grid.AbstractRenderer<Component> implements DataGenerator {

    /** Tracks this renderers trackedComponents to be able to unregister them from the grid upon removal
     *  of the renderers column */
    private final HashMap<Object, Component> trackedComponents = new HashMap<>();
    private boolean isRemovalInProgress = false;

    public ComponentRenderer() {
        super(Component.class, null);
    }

    @Override
    public JsonValue encode(Component component) {

        // 1: add component to grid, so connector id can be encoded
        if (component != null) {
            addComponentToGrid(component);
            return Json.create(component.getConnectorId());
        } else {
            return Json.createNull();
        }
    }

    /**
     * When the renderer is detached from the grid (e.g. when the column is removed)
     * release all trackedComponents to make them eligible for garbage collection and remove
     * the DataGenerator extension for this renderer from the Grid.
     *
     * @param parent the parent connector
     */
    @Override
    public void setParent(ClientConnector parent) {

        // detect a column removal (the renderer is being detached)
        if (getParent() != null && parent == null) {

            if (!isRemovalInProgress) {

                removeAllRendererComponentsFromGrid();

                // it is important to also detach the renderers @link{DataGenerator}
                // when the renderer is detached. The @link{com.vaadin.ui.Grid.AbstractGridExtension#remove}
                // does that, but calls setParent(null) again, which would lead to endless recursion
                // so we set a flag that we currently are already in removal
                // and stop further calls to remove().
                //
                isRemovalInProgress = true;
                remove();
            } else {
                isRemovalInProgress = false;
            }
        }

        super.setParent(parent);

        // VERY IMPORTANT: registers the DataGenerator extension
        // with the grid. The reason the extend method is deprecated
        // with renderers is that normal gwt-based renderers should not have
        // a direct dependency to the grid. In case of the componentrenderer
        // it must have this dependency to function properly.
        if (parent != null) {
            extend(getParentGrid());
        }
    }


    private void trackComponent(Object itemId, Component component) {

        // before tracking a new component for a row, it is necessary to remove the
        // previously tracked one from the grid to make the component eligible for
        // garbage collection.
        //
        // IMPORTANT: Only remove the component from the grid if
        // the new component is not the same as the old one. Otherwise
        // cached components also get removed.

        Component previousComponent = trackedComponents.get(itemId);

        if (previousComponent != component) {
            if(previousComponent != null) {
                safeRemoveComponentFromGrid(previousComponent);
            }
            trackedComponents.put(itemId, component);
        }
    }

    @Override
    public void generateData(Object itemId, Item item, JsonObject jsonRow) {

        JsonObject jsonData = jsonRow.getObject("d");

        for (String key : jsonData.keys()) {

            if (getColumn(key).getRenderer() == this) {
                // 2: VERY IMPORTANT get the component from the connector tracker !!!
                //    if you use a GeneratedPropertyContainer and call get Value you will
                //    get a different component

                if (jsonData.get(key) != Json.createNull()) {
                    Component current = lookupComponent(jsonData, key);
                    trackComponent(itemId, current);
                }
            }
        }
    }

    private Component lookupComponent(JsonObject jsonData, String key) {
        return (Component) UI.getCurrent()
                .getConnectorTracker()
                .getConnector(jsonData.getString(key));
    }

    @Override
    public void destroyData(Object itemId) {
        if (trackedComponents.containsKey(itemId)) {
            safeRemoveComponentFromGrid(trackedComponents.get(itemId));
            trackedComponents.remove(itemId);
        }

    }

    private void safeRemoveComponentFromGrid(Component component) {
        if (component != null) {
            removeComponentFromGrid(component);
        }
    }

    private void removeAllRendererComponentsFromGrid() {

        for (Component component : trackedComponents.values()) {
            safeRemoveComponentFromGrid(component);
        }

        trackedComponents.clear();
    }

}
