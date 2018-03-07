/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.image;

import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VImage;

public class CubaImageWidget extends VImage {
    protected static final String OBJECT_FIT = "object-fit-";
    public static final String OBJECT_FIT_ATTRIBUTE = "object-fit-id";

    protected static int objectFit = 0;
    protected String scaleMode = "none";

    public CubaImageWidget() {
        getElement().setAttribute(OBJECT_FIT_ATTRIBUTE, String.valueOf(objectFit++));

        addStyleName(OBJECT_FIT + scaleMode);
    }

    public void applyScaling(String scaleMode) {
        removeStyleName(OBJECT_FIT + this.scaleMode);

        this.scaleMode = scaleMode;

        addStyleName(OBJECT_FIT + scaleMode);

        if (BrowserInfo.get().isIE() || BrowserInfo.get().isEdge()) {
            applyScaling(Integer.valueOf(getElement().getAttribute(OBJECT_FIT_ATTRIBUTE)));
        }
    }

    protected native void applyScaling(int fitId) /*-{
        var selector = "img[object-fit-id='" + fitId + "']";
        var image = document.querySelector(selector);
        $wnd.objectFitImages(image);
    }-*/;
}