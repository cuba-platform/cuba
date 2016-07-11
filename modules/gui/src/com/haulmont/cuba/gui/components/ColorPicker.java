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
 */

package com.haulmont.cuba.gui.components;

public interface ColorPicker extends Field  {

    String NAME = "colorPicker";

    void setPopupCaption(String caption);
    String getPopupCaption();

    void setConfirmButtonCaption(String caption);
    String getConfirmButtonCaption();

    void setCancelButtonCaption(String caption);
    String getCancelButtonCaption();

    void setSwatchesTabCaption(String caption);
    String getSwatchesTabCaption();

    void setLookupAllCaption(String caption);
    String getLookupAllCaption();

    void setLookupRedCaption(String caption);
    String getLookupRedCaption();

    void setLookupGreenCaption(String caption);
    String getLookupGreenCaption();

    void setLookupBlueCaption(String caption);
    String getLookupBlueCaption();

    void setRedSliderCaption(String caption);
    String getRedSliderCaption();

    void setGreenSliderCaption(String caption);
    String getGreenSliderCaption();

    void setBlueSliderCaption(String caption);
    String getBlueSliderCaption();

    void setHueSliderCaption(String caption);
    String getHueSliderCaption();

    void setSaturationSliderCaption(String caption);
    String getSaturationSliderCaption();

    void setValueSliderCaption(String caption);
    String getValueSliderCaption();

    void setSwatchesVisible(boolean value);
    boolean getSwatchesVisible();

    void setRGBVisible(boolean value);
    boolean getRGBVisible();

    void setHSVVisible(boolean value);
    boolean getHSVVisible();

    @Override
    String getValue();
}
