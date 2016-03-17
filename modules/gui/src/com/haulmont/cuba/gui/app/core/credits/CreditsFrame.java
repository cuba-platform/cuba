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

package com.haulmont.cuba.gui.app.core.credits;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 */
public class CreditsFrame extends AbstractFrame {

    @Named("scroll")
    protected ScrollBoxLayout scrollBox;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected ExportDisplay exportDisplay;

    @Override
    public void init(final Map<String, Object> params) {
        getDialogParams().setResizable(true);

        StringBuilder acknowledgements = new StringBuilder();

        GridLayout grid = componentsFactory.createComponent(GridLayout.class);
        grid.setSpacing(true);
        grid.setMargin(false, true, false, true);
        grid.setColumns(5);
        grid.setFrame(frame);

        List<CreditsItem> items = new CreditsLoader().load().getItems();
        if (items.size() > 0) {
            grid.setRows(items.size());

            for (int i = 0, itemsSize = items.size(); i < itemsSize; i++) {
                final CreditsItem item = items.get(i);

                if (item.getAcknowledgement() != null)
                    acknowledgements.append("<p>").append(item.getAcknowledgement());

                Label nameLab = componentsFactory.createComponent(Label.class);
                nameLab.setValue(item.getName());
                nameLab.setFrame(frame);
                nameLab.setAlignment(Alignment.MIDDLE_LEFT);
                grid.add(nameLab, 0, i);

                Label dash = componentsFactory.createComponent(Label.class);
                dash.setValue("-");
                dash.setFrame(frame);
                dash.setAlignment(Alignment.MIDDLE_LEFT);
                grid.add(dash, 1, i);

                Link webpage = componentsFactory.createComponent(Link.class);
                webpage.setCaption(getMessage("webpage"));
                webpage.setUrl(item.getWebPage());
                webpage.setTarget("_blank");
                webpage.setFrame(frame);
                webpage.setAlignment(Alignment.MIDDLE_LEFT);
                grid.add(webpage, 2, i);

                dash = componentsFactory.createComponent(Label.class);
                dash.setValue("-");
                dash.setFrame(frame);
                dash.setAlignment(Alignment.MIDDLE_LEFT);
                grid.add(dash, 3, i);

                LinkButton license = componentsFactory.createComponent(LinkButton.class);
                license.setFrame(frame);
                license.setCaption(getMessage("license"));
                license.setAlignment(Alignment.MIDDLE_LEFT);
                license.setAction(new AbstractAction("license") {
                    @Override
                    public void actionPerform(Component component) {
                        openWindow("thirdpartyLicenseWindow",
                                   WindowManager.OpenType.DIALOG,
                                   ParamsMap.of("licenseText", item.getLicense()));
                    }
                });
                grid.add(license, 4, i);
            }

            scrollBox.add(grid);

            if (acknowledgements.length() > 0) {
                Label ackLab = componentsFactory.createComponent(Label.class);
                ackLab.setWidth("420px");
                ackLab.setHtmlEnabled(true);
                ackLab.setValue(acknowledgements.toString());
                scrollBox.add(ackLab);
            }
        }
    }

    public void exportLicenses() {
        List<CreditsItem> items = new CreditsLoader().load().getItems();

        Map<String, String> licenses = new TreeMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>\n");
        sb.append("<h1>Credits</h1>\n");

        StringBuilder acknowledgements = new StringBuilder();
        StringBuilder forks = new StringBuilder();
        for (CreditsItem item : items) {
            if (item.getAcknowledgement() != null)
                acknowledgements.append("<p>").append(item.getAcknowledgement());
            if (item.isFork())
                forks.append("<li>").append(item.getName());
        }
        if (acknowledgements.length() > 0) {
            sb.append("<h2>Acknowledgements</h2>\n");
            sb.append(acknowledgements);
        }
        if (forks.length() > 0) {
            sb.append("<h2>Forks and modifications</h2>\n");
            sb.append("<p>The following libraries have been modified by Haulmont:");
            sb.append("<ul>").append(forks).append("</ul>");
            sb.append("<p>All modifications are distributed under the same license as the corresponding library.");
        }

        sb.append("<h2>Third-party products</h2>\n");
        sb.append("<ol>\n");
        for (CreditsItem item : items) {
            sb.append("<li><b>").append(item.getName()).append("</b>\n");
            sb.append("<p>Web site: <a href='").append(item.getWebPage()).append("' target='_blank'>").append(item.getWebPage()).append("</a></p>\n");
            sb.append("<p>License: ");
            if (item.getLicenseId() == null) {
                sb.append("<br>").append(item.getLicense().replace("\n", "<br>"));
            } else {
                sb.append("<a href='#").append(item.getLicenseId()).append("'>").append(item.getLicenseId()).append("</a>");
                licenses.put(item.getLicenseId(), item.getLicense());
            }
            sb.append("</p>");
            sb.append("</li>\n");
        }
        sb.append("</ol>\n");

        sb.append("<a name='licenses'></a><h2>Common Licenses</h2>\n");
        sb.append("<ol>\n");
        for (Map.Entry<String, String> entry : licenses.entrySet()) {
            sb.append("<li><a name='").append(entry.getKey()).append("'></a><b>").append(entry.getKey()).append("</b>\n");
            sb.append("<p>").append(entry.getValue().replace("\n", "<br>")).append("</p>");
            sb.append("</li>\n");
        }
        sb.append("</ol>\n");
        sb.append("</body></html>\n");

        ByteArrayDataProvider dataProvider = new ByteArrayDataProvider(sb.toString().getBytes(StandardCharsets.UTF_8));
        exportDisplay.show(dataProvider, "Credits", ExportFormat.HTML);
    }
}