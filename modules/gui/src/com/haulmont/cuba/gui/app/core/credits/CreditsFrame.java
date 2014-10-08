/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.credits;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CreditsFrame extends AbstractFrame {

    @Named("scroll")
    protected ScrollBoxLayout scrollBox;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Override
    public void init(final Map<String, Object> params) {
        getDialogParams().setResizable(true);

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

            scrollBox.add(grid);
        }
    }
}