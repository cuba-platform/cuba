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

package com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid;

import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.server.Extension;
import com.vaadin.v7.server.communication.data.RpcDataProviderExtension;
import com.vaadin.v7.ui.Grid;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.ComponentCellKeyExtension;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.ComponentRenderer;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.DetailsKeysExtension;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.FocusPreserveExtension;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid.editor.ComponentCustomField;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid.header.ComponentHeaderGenerator;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid.header.HtmlHeaderGenerator;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid.header.TextHeaderGenerator;

import java.io.Serializable;
import java.util.Collection;

/**
 * A typed decorator which makes it very easy to bring the ComponentGrid features
 * to other grids.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentGridDecorator<T> implements Serializable {

    private final FocusPreserveExtension focusPreserveExtension;
    private final Grid grid;
    private GeneratedPropertyContainer gpc = null;
    private final Class<T> typeOfRows;

    public ComponentGridDecorator(Grid grid, Class<T> typeOfRows) {
        this.grid = grid;
        this.typeOfRows = typeOfRows;
        focusPreserveExtension = FocusPreserveExtension.extend(grid);
        DetailsKeysExtension.extend(grid);
        ComponentCellKeyExtension.extend(grid);
        initGpc();
    }

    public FocusPreserveExtension getFocusPreserveExtension() {
        return focusPreserveExtension;
    }

    public Grid getGrid() {
        return grid;
    }

    /**
     * Replaces the current grid container with a {@link GeneratedPropertyContainer}
     * while preserving the {@link Grid.DetailsGenerator}.
     */
    private void initGpc() {
        gpc = new GeneratedPropertyContainer(grid.getContainerDataSource());
        Grid.DetailsGenerator details = grid.getDetailsGenerator();
        grid.setContainerDataSource(gpc);
        grid.setDetailsGenerator(details);
    }


    /**
     * Add a generated component column to the ComponentGrid.
     *
     * @param propertyId the generated column's property-id
     * @param generator  the component-generator
     * @return the decorator for method chaining
     */
    public Grid.Column addComponentColumn(Object propertyId, ComponentGenerator<T> generator) {
        gpc.addGeneratedProperty(propertyId, new ComponentPropertyGenerator<>(typeOfRows, generator));
        return grid.getColumn(propertyId)
                .setRenderer(new ComponentRenderer())
                .setEditorField(new ComponentCustomField());
    }


    /**
     * Remove all items from the underlying {@link BeanItemContainer} and add
     * the new beans.
     *
     * @param beans a collection of beans
     * @return the decorator for method chaining
     */
    public ComponentGridDecorator<T> setRows(Collection<T> beans) {
        gpc.removeAllItems();
        addAll(beans);
        return this;
    }

    /**
     * Add all beans to the decorated grid's container.
     *
     * @param beans a collection of beans
     * @return the decorator for method chaining
     */
    public ComponentGridDecorator<T> addAll(Collection<T> beans) {
        for (T bean : beans) {
            gpc.addItem(bean);
        }
        return this;
    }

    /**
     * Remove a bean from the grid.
     *
     * @return the decorator for method chaining
     */
    public ComponentGridDecorator<T> remove(T bean) {
        gpc.removeItem(bean);
        return this;
    }

    /**
     * Add a bean to the grid.
     *
     * @return the decorator for method chaining
     */
    public ComponentGridDecorator<T> add(T bean) {
        gpc.addItem(bean);
        return this;
    }

    /**
     * Refreshes the grid preserving its current cell focus.
     *
     * @return the decorator for method chaining
     */
    public ComponentGridDecorator<T> refresh() {

        focusPreserveExtension.saveFocus();

        // inspired by the awesome viritin extension
        // https://github.com/viritin/viritin/blob/viritin-1.44/src/main/java/org/vaadin/viritin/grid/MGrid.java#L218
        for (Extension extension : grid.getExtensions()) {
            if (extension instanceof RpcDataProviderExtension) {
                ((RpcDataProviderExtension) extension).refreshCache();
                break;
            }
        }

        focusPreserveExtension.restoreFocus();
        return this;
    }

    /**
     * Generates component header fields using the passed {@link ComponentHeaderGenerator} and
     * sets them to the columns.
     *
     * @param generator the header generator
     * @return the decorator for method chaining
     */
    public ComponentGridDecorator<T> generateHeaders(ComponentHeaderGenerator generator) {
        for (Grid.Column column : grid.getColumns()) {
            grid.getDefaultHeaderRow()
                    .getCell(column.getPropertyId())
                    .setComponent(generator.getHeader(column.getPropertyId()));
        }
        return this;
    }

    /**
     * Generates text header fields using the passed {@link TextHeaderGenerator} and
     * sets them to the columns.
     *
     * @param generator the header generator
     * @return the decorator for method chaining
     */
    public ComponentGridDecorator<T> generateHeaders(TextHeaderGenerator generator) {
        for (Grid.Column column : grid.getColumns()) {
            grid.getDefaultHeaderRow()
                    .getCell(column.getPropertyId())
                    .setText(generator.getHeader(column.getPropertyId()));
        }
        return this;
    }

    /**
     * Generates html header fields using the passed {@link HtmlHeaderGenerator} and
     * sets them to the columns.
     *
     * @param generator the header generator
     * @return the decorator for method chaining
     */
    public ComponentGridDecorator<T> generateHeaders(HtmlHeaderGenerator generator) {
        for (Grid.Column column : grid.getColumns()) {
            grid.getDefaultHeaderRow()
                    .getCell(column.getPropertyId())
                    .setHtml(generator.getHeader(column.getPropertyId()));
        }
        return this;
    }
}