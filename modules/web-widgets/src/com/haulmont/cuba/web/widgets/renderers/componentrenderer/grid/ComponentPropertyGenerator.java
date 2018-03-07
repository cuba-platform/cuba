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

import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.sort.SortOrder;
import com.vaadin.v7.data.util.PropertyValueGenerator;
import com.vaadin.ui.Component;
import org.apache.commons.lang.reflect.FieldUtils;

/**
 * A property value generator taylored to the needs of the typed {@link ComponentGrid}.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentPropertyGenerator<T> extends PropertyValueGenerator<Component> {

    private final Class<T> typeOfRows;
    private final ComponentGenerator<T> componentGenerator;

    /**
     * Create a new {@link ComponentPropertyGenerator}.
     *
     * @param typeOfRows the type of the beans used in the grid
     * @param generator  the generator used to create the components
     */
    public ComponentPropertyGenerator(Class<T> typeOfRows, ComponentGenerator<T> generator) {
        this.typeOfRows = typeOfRows;
        this.componentGenerator = generator;
    }

    @Override
    public Component getValue(Item item, Object itemId, Object propertyId) {
        return componentGenerator.getComponent((T) itemId);
    }

    /**
     * If the generated property is hiding a property existing in the bean,
     * the underlying property of the bean is used for sorting. Otherwise
     * no sorting is possible (clicking on the header-cell for sorting then
     * will have no effect).
     *
     * {@inheritDoc}
     */
    @Override
    public SortOrder[] getSortProperties(SortOrder order) {

        if (hidesBeanField(order)) {
            return new SortOrder[]{order};
        } else {
            return new SortOrder[0];
        }
    }

    /**
     * Checks if the generated property hides an existing property of the bean.
     * If that's the case the underlying bean-property can be used for sorting.
     *
     * @param order the sort-order
     * @return true when the property of the column to be sorted hides a bean-field, false otherwise
     */
    private boolean hidesBeanField(SortOrder order) {
        return typeOfRows != null && typeHasProperty((String) order.getPropertyId());
    }

    private boolean typeHasProperty(String propertyId) {
        boolean hasProperty = FieldUtils.getField(typeOfRows, propertyId, true) != null;
        String prefixedProperty = "is" + propertyId.substring(0, 1).toUpperCase() + propertyId.substring(1);
        boolean hasPrefixedProperty = FieldUtils.getField(typeOfRows, prefixedProperty, true) != null;
        return hasProperty || hasPrefixedProperty;
    }

    @Override
    public Class<Component> getType() {
        return Component.class;
    }
}
