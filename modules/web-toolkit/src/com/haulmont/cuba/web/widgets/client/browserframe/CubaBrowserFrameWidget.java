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

package com.haulmont.cuba.web.widgets.client.browserframe;

import com.google.gwt.dom.client.Document;
import com.vaadin.client.ui.VBrowserFrame;

public class CubaBrowserFrameWidget extends VBrowserFrame {

    protected void setAttribute(String name, String value) {
        if (iframe == null) {
            return;
        }

        if (value != null) {
            iframe.setAttribute(name, value);
        } else {
            iframe.removeAttribute(name);
        }
    }

    protected void setSrcdoc(String srcdoc, String connectorId) {
        if (srcdoc == null || srcdoc.isEmpty()) {
            if (iframe == null) {
                return;
            }

            if (iframe.getSrc() == null
                    || iframe.getSrc().isEmpty()) {
                removeIFrame();
                return;
            }

            iframe.removeAttribute(CubaBrowserFrameState.SRCDOC);
        } else {
            if (iframe == null) {
                createIFrame(connectorId);
            }
            iframe.setAttribute(CubaBrowserFrameState.SRCDOC, srcdoc);
        }
    }

    protected void createIFrame(String connectorId) {
        if (altElement != null) {
            getElement().removeChild(altElement);
            altElement = null;
        }

        iframe = Document.get().createIFrameElement();
        iframe.setFrameBorder(0);
        iframe.setAttribute("width", "100%");
        iframe.setAttribute("height", "100%");
        iframe.setAttribute("allowTransparency", "true");

        setName(connectorId);

        getElement().appendChild(iframe);
    }

    @Override
    public void setSource(String source) {
        if (source == null) {
            if (iframe == null) {
                return;
            }

            String srcdoc = iframe.getAttribute(CubaBrowserFrameState.SRCDOC);
            if (srcdoc == null || srcdoc.isEmpty()) {
                removeIFrame();
                return;
            }

            iframe.removeAttribute("src");
        } else {
            if (iframe == null) {
                createIFrameElement(source);
            } else {
                iframe.setSrc(source);
            }
        }
    }

    protected void removeIFrame() {
        if (iframe != null) {
            getElement().removeChild(iframe);
            iframe = null;
        }
        createAltTextElement();
        setAlternateText(altText);
    }
}
