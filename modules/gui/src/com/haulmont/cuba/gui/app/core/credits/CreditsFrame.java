/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author krivopustov
 * @version $Id$
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

        GridLayout grid = componentsFactory.createComponent(GridLayout.NAME);
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

                Label nameLab = componentsFactory.createComponent(Label.NAME);
                nameLab.setValue(item.getName());
                nameLab.setFrame(frame);
                nameLab.setAlignment(Alignment.MIDDLE_LEFT);
                grid.add(nameLab, 0, i);

                Label dash = componentsFactory.createComponent(Label.NAME);
                dash.setValue("-");
                dash.setFrame(frame);
                dash.setAlignment(Alignment.MIDDLE_LEFT);
                grid.add(dash, 1, i);

                Link webpage = componentsFactory.createComponent(Link.NAME);
                webpage.setCaption(getMessage("webpage"));
                webpage.setUrl(item.getWebPage());
                webpage.setTarget("_blank");
                webpage.setFrame(frame);
                webpage.setAlignment(Alignment.MIDDLE_LEFT);
                grid.add(webpage, 2, i);

                dash = componentsFactory.createComponent(Label.NAME);
                dash.setValue("-");
                dash.setFrame(frame);
                dash.setAlignment(Alignment.MIDDLE_LEFT);
                grid.add(dash, 3, i);

                LinkButton license = componentsFactory.createComponent(LinkButton.NAME);
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

            if (acknowledgements.length() > 0) {
                Label ackLab = componentsFactory.createComponent(Label.NAME);
                ackLab.setWidth("420px");
                ackLab.setHtmlEnabled(true);
                ackLab.setValue(acknowledgements.toString());
                scrollBox.add(ackLab);
            }

            scrollBox.add(grid);
        }
    }

    public void exportLicenses() {
        List<CreditsItem> items = new CreditsLoader().load().getItems();

        Map<String, String> licenses = new TreeMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>\n");
        sb.append("<h1>Credits</h1>\n");

        StringBuilder acknowledgements = new StringBuilder();
        for (CreditsItem item : items) {
            if (item.getAcknowledgement() != null)
                acknowledgements.append("<p>").append(item.getAcknowledgement());
        }
        if (acknowledgements.length() > 0) {
            sb.append("<h2>Acknowledgements</h1>\n");
            sb.append(acknowledgements);
        }

//        sb.append("<p><a href='#licenses'>Common Licenses</a></p>\n");

        sb.append("<h2>Third-party products</h1>\n");
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

        sb.append("<a name='licenses'></a><h2>Common Licenses</h1>\n");
        sb.append("<ol>\n");
        for (Map.Entry<String, String> entry : licenses.entrySet()) {
            sb.append("<li><a name='").append(entry.getKey()).append("'></a><b>").append(entry.getKey()).append("</b>\n");
            sb.append("<p>").append(entry.getValue().replace("\n", "<br>")).append("</p>");
            sb.append("</li>\n");
        }
        sb.append("</ol>\n");
        sb.append("</body></html>\n");

        try {
            exportDisplay.show(new ByteArrayDataProvider(sb.toString().getBytes("UTF-8")), "Credits", ExportFormat.HTML);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}