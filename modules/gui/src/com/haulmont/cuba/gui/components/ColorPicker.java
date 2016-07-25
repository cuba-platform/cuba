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

    /**
     * Set caption for the popup window.
     *
     * @param popupCaption caption text.
     */
    void setPopupCaption(String popupCaption);
    /**
     * Return caption of the popup window.
     *
     * @return caption text.
     */
    String getPopupCaption();

    /**
     * Set caption for the confirm button.
     *
     * @param confirmButtonCaption caption text.
     */
    void setConfirmButtonCaption(String confirmButtonCaption);
    /**
     * Return caption of the confirm button.
     *
     * @return caption text.
     */
    String getConfirmButtonCaption();

    /**
     * Set caption for the cancel button.
     *
     * @param cancelButtonCaption caption text.
     */
    void setCancelButtonCaption(String cancelButtonCaption);
    /**
     * Return caption of the cancel button.
     *
     * @return caption text.
     */
    String getCancelButtonCaption();

    /**
     * Set caption for the swatches tab.
     *
     * @param swatchesTabCaption caption text.
     */
    void setSwatchesTabCaption(String swatchesTabCaption);
    /**
     * Return caption of the swatches tab.
     *
     * @return caption text.
     */
    String getSwatchesTabCaption();

    /**
     * Set caption for the all colors in lookup.
     *
     * @param lookupAllCaption caption text.
     */
    void setLookupAllCaption(String lookupAllCaption);
    /**
     * Return caption of the all colors in lookup.
     *
     * @return caption text.
     */
    String getLookupAllCaption();

    /**
     * Set caption for the red colors in lookup.
     *
     * @param lookupRedCaption caption text.
     */
    void setLookupRedCaption(String lookupRedCaption);
    /**
     * Return caption of the red colors in lookup.
     *
     * @return caption text.
     */
    String getLookupRedCaption();

    /**
     * Set caption for the green colors in lookup.
     *
     * @param lookupGreenCaption caption text.
     */
    void setLookupGreenCaption(String lookupGreenCaption);
    /**
     * Return caption of the green colors in lookup.
     *
     * @return caption text.
     */
    String getLookupGreenCaption();

    /**
     * Set caption for the blue colors in lookup.
     *
     * @param lookupBlueCaption caption text.
     */
    void setLookupBlueCaption(String lookupBlueCaption);
    /**
     * Return caption of the blue colors in lookup.
     *
     * @return caption text.
     */
    String getLookupBlueCaption();

    /**
     * Set caption for the slider of red color.
     *
     * @param redSliderCaption caption text.
     */
    void setRedSliderCaption(String redSliderCaption);
    /**
     * Return caption of the slider for red color.
     *
     * @return caption text.
     */
    String getRedSliderCaption();

    /**
     * Set caption for the slider of green color.
     *
     * @param greenSliderCaption caption text.
     */
    void setGreenSliderCaption(String greenSliderCaption);
    /**
     * Return caption of the slider for green color.
     *
     * @return caption text.
     */
    String getGreenSliderCaption();

    /**
     * Set caption for the slider of blue color.
     *
     * @param blueSliderCaption caption text.
     */
    void setBlueSliderCaption(String blueSliderCaption);
    /**
     * Return caption of the slider for blue color.
     *
     * @return caption text.
     */
    String getBlueSliderCaption();

    /**
     * Set caption for the HUE slider.
     *
     * @param hueSliderCaption caption text.
     */
    void setHueSliderCaption(String hueSliderCaption);
    /**
     * Return caption of the slider for HUE.
     *
     * @return caption text.
     */
    String getHueSliderCaption();

    /**
     * Set caption for the saturation slider.
     *
     * @param saturationSliderCaption caption text.
     */
    void setSaturationSliderCaption(String saturationSliderCaption);
    /**
     * Return caption of the slider for saturation.
     *
     * @return caption text.
     */
    String getSaturationSliderCaption();

    /**
     * Set caption for the value slider.
     *
     * @param valueSliderCaption caption text.
     */
    void setValueSliderCaption(String valueSliderCaption);
    /**
     * Return caption of the slider for value.
     *
     * @return caption text.
     */
    String getValueSliderCaption();

    /**
     *  Set visibility for swatches tab
     *
     *  @param swatchesVisible tab visibility.
     */
    void setSwatchesVisible(boolean swatchesVisible);
    /**
     * @return true if swatches tab is visibile.
     */
    boolean isSwatchesVisible();

    /**
     *  Set visibility for RGB tab
     *
     *  @param rgbVisible tab visibility.
     */
    void setRGBVisible(boolean rgbVisible);
    /**
     * @return true if RGB tab is visible.
     */
    boolean isRGBVisible();

    /**
     *  Set visibility for HSV tab
     *
     *  @param hsvVisible tab visibility.
     */
    void setHSVVisible(boolean hsvVisible);
    /**
     * @return true if HSV tab is visible.
     */
    boolean isHSVVisible();

    @Override
    String getValue();
}
