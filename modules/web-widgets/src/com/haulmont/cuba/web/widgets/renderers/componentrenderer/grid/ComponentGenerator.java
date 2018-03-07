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

import com.vaadin.ui.Component;

import java.io.Serializable;

/**
 * Typed lambda-compatible interface to generate a component out of a
 * bean-item-containers bean-itemId (the bean itself).
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public interface ComponentGenerator<T> extends Serializable {

    /**
     * Called to generate the component for a component cell based
     * on the passed typed bean.
     *
     * @param bean the bean being the source for the current row's data
     * @return a <b>new</b> instance of the component used to display the data
     */
    Component getComponent(T bean);
}
