/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.components;

public interface Slider<V extends Number> extends Field<V>, HasDatatype<V>, HasOrientation {

    String NAME = "slider";

    /**
     * Sets the minimum value of the slider.
     *
     * @param min the minimum value of the slider
     */
    void setMin(V min);

    /**
     * @return the minimum value of the slider
     */
    V getMin();

    /**
     * Sets the maximum value of the slider.
     *
     * @param max the maximum value of the slider
     */
    void setMax(V max);

    /**
     * @return the maximum value of the slider
     */
    V getMax();

    /**
     * Sets the number of digits after the decimal point.
     *
     * @param resolution the number of digits after the decimal point
     */
    void setResolution(int resolution);

    /**
     * @return resolution the number of digits after the decimal point
     */
    int getResolution();

    /**
     * Sets the slider to update its value when the user clicks on it.
     * <p>
     * By default this behavior is disabled.
     *
     * @param updateValueOnClick {@code true} to update the value of the slider on click
     */
    void setUpdateValueOnClick(boolean updateValueOnClick);

    /**
     * @return {@code true} if the slider updates its value on click
     */
    boolean isUpdateValueOnCLick();
}
