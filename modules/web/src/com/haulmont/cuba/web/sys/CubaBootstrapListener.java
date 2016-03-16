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
 *
 */

package com.haulmont.cuba.web.sys;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import org.jsoup.nodes.Element;

import org.springframework.stereotype.Component;

/**
 * Event listener notified when the bootstrap HTML is about to be generated and
 * send to the client. The bootstrap HTML is first constructed as an in-memory
 * DOM representation which registered listeners can modify before the final
 * HTML is generated.
 *
 */
@Component(CubaBootstrapListener.NAME)
public class CubaBootstrapListener implements BootstrapListener {

    public static final String NAME = "cuba_BootstrapListener";

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Element head = response.getDocument().getElementsByTag("head").get(0);

        includeScript("VAADIN/resources/jquery/jquery-1.11.3.min.js", response, head);
    }

    protected void includeScript(String src, BootstrapPageResponse response, Element head) {
        Element script = response.getDocument().createElement("script");
        script.attr("src", src);
        head.appendChild(script);
    }
}