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
import com.vaadin.v7.ui.Grid;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.FocusPreserveExtension;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid.header.ComponentHeaderGenerator;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid.header.HtmlHeaderGenerator;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid.header.TextHeaderGenerator;

import java.util.Collection;

/**
 * A typed version of the grid using a {@link BeanItemContainer} to store
 * the typed grid data and a {@link GeneratedPropertyContainer} to provide
 * generated component-columns.
 *
 * Also offers some convenience methods for this use-case (typed, use of components).
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentGrid<T> extends Grid {

    private final ComponentGridDecorator<T> componentGridDecorator;

    public ComponentGrid(Class<T> typeOfRows) {
        super();
        setContainerDataSource(new BeanItemContainer<T>(typeOfRows));
        componentGridDecorator = new ComponentGridDecorator<T>(this, typeOfRows);
    }

    public ComponentGridDecorator<T> getComponentGridDecorator() {
        return componentGridDecorator;
    }

    public FocusPreserveExtension getFocusPreserveExtension() {
        return componentGridDecorator.getFocusPreserveExtension();
    }

    /**
     * Remove a bean from the grid.
     *
     * @return the decorator for method chaining
     */
    public ComponentGrid<T> remove(T bean) {
        componentGridDecorator.remove(bean);
        return this;
    }

    /**
     * Add a bean to the grid.
     *
     * @return the decorator for method chaining
     */
    public ComponentGrid<T>  add(T bean) {
        componentGridDecorator.add(bean);
        return this;
    }

    /**
     * Add all beans to the decorated grid's container.
     *
     * @param beans a collection of beans
     * @return the grid for method chaining
     */
    public ComponentGrid<T> addAll(Collection<T> beans) {
        componentGridDecorator.addAll(beans);
        return this;
    }

    /**
     * Add a generated component column to the ComponentGrid.
     *
     * @param propertyId the generated column's property-id
     * @param generator  the component-generator
     * @return the grid for method chaining
     */
    public Column  addComponentColumn(Object propertyId, ComponentGenerator<T> generator) {
        return componentGridDecorator.addComponentColumn(propertyId, generator);
    }

    /**
     * Refreshes the grid preserving its current cell focus.
     */
    public ComponentGrid<T>  refresh() {
        componentGridDecorator.refresh();
        return this;
    }

    /**
     * Remove all items from the underlying {@link BeanItemContainer} and add
     * the new beans.
     *
     * @param beans a collection of beans
     * @return the grid for method chaining
     */
    public ComponentGrid<T>  setRows(Collection<T> beans) {
        componentGridDecorator.setRows(beans);
        return this;
    }
    /**
     * Generates component header fields using the passed {@link ComponentHeaderGenerator} and
     * sets them to the columns.
     *
     * @param generator the header generator
     * @return the grid for method chaining
     */
    public ComponentGrid<T> generateHeaders(ComponentHeaderGenerator generator) {
        componentGridDecorator.generateHeaders(generator);
        return this;
    }

    /**
     * Generates text header fields using the passed {@link TextHeaderGenerator} and
     * sets them to the columns.
     *
     * @param generator the header generator
     * @return the grid for method chaining
     */
    public ComponentGrid<T> generateHeaders(TextHeaderGenerator generator) {
        componentGridDecorator.generateHeaders(generator);
        return this;
    }

    /**
     * Generates html header fields using the passed {@link HtmlHeaderGenerator} and
     * sets them to the columns.
     *
     * @param generator the header generator
     * @return the grid for method chaining
     */
    public ComponentGrid<T> generateHeaders(HtmlHeaderGenerator generator) {
        componentGridDecorator.generateHeaders(generator);
        return this;
    }
}
