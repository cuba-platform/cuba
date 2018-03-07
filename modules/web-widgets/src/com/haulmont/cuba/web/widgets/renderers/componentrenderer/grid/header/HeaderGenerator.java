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

package com.haulmont.cuba.web.widgets.renderers.componentrenderer.grid.header;

import java.io.Serializable;

/**
 * Typed lambda-compatible interface to generate grid headers.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public interface HeaderGenerator<T> extends Serializable {

    /**
     * Called to generate the header field of a column.
     *
     * @param propertyId the propertyId of the column for which the header is created
     */
    T getHeader(Object propertyId);
}
