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

package com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid.editor;

import com.vaadin.v7.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

/**
 * Fake-Field for the grid-editor which just displays the component
 * from the table-cell.
 *
 * Caution! Does NOT support buffered-mode/commit. It just hands through
 * the component from the cell.
 *
 * Use it for readonly fields (e.g. Label, etc.) or in unbuffered
 * grid-editor-mode.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentCustomField extends CustomField<Component> {

    private final HorizontalLayout layout = new HorizontalLayout();

    /**
     * Setup a empty layout, which later is filled with
     * the real component from the cell.
     *
     * @return the layout
     */
    @Override
    protected Component initContent() {
        layout.setSizeFull();
        layout.addStyleName("cr-editor-field");
        return layout;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource)
    {
        super.setPropertyDataSource(newDataSource);

        if (newDataSource != null) {
            layout.removeAllComponents();
            Component value = (Component) newDataSource.getValue();
            if (value != null) {
                layout.addComponent(value);
            }
        }
    }

    @Override
    public Class<? extends Component> getType() {
        return Component.class;
    }
}
