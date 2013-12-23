/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.credits;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CreditsFrame extends AbstractFrame {

    @Named("scroll")
    protected ScrollBoxLayout scrollBox;

    @Override
    public void init(final Map<String, Object> params) {
        ComponentsFactory factory = AppConfig.getFactory();

        GridLayout grid = factory.createComponent(GridLayout.NAME);
        grid.setSpacing(true);
        grid.setColumns(5);
        grid.setFrame(frame);

        List<CreditsItem> items = new CreditsLoader().load().getItems();
        if (items.size() > 0) {
            grid.setRows(items.size());
            for (int i = 0, itemsSize = items.size(); i < itemsSize; i++) {
                final CreditsItem item = items.get(i);

                Label nameLab = factory.createComponent(Label.NAME);
                nameLab.setValue(item.getName());
                nameLab.setFrame(frame);
                grid.add(nameLab, 0, i);

                Label dash = factory.createComponent(Label.NAME);
                dash.setValue("-");
                dash.setFrame(frame);
                grid.add(dash, 1, i);

                Link webpage = factory.createComponent(Link.NAME);
                webpage.setCaption(getMessage("webpage"));
                webpage.setUrl(item.getWebPage());
                webpage.setTarget("_blank");
                webpage.setFrame(frame);
                grid.add(webpage, 2, i);

                dash = factory.createComponent(Label.NAME);
                dash.setValue("-");
                dash.setFrame(frame);
                grid.add(dash, 3, i);

                LinkButton license = factory.createComponent(LinkButton.NAME);
                license.setFrame(frame);
                license.setCaption(getMessage("license"));
                license.setAction(new AbstractAction("license") {
                    @Override
                    public void actionPerform(Component component) {
                        openWindow("thirdpartyLicenseWindow", WindowManager.OpenType.DIALOG,
                                Collections.<String, Object>singletonMap("licenseText", item.getLicense()));
                    }
                });
                grid.add(license, 4, i);
            }

            scrollBox.add(grid);
        }
    }
}
