/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.credits;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class CreditsFrame extends AbstractFrame {

    public interface Companion {
        void initWebPageButton(LinkButton button, CreditsItem item);
    }

    protected Companion companion;

    protected ScrollBoxLayout scrollBox;

    public CreditsFrame(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(final Map<String, Object> params) {
        scrollBox = getComponent("scroll");
        companion = getCompanion();

        ComponentsFactory factory = AppConfig.getFactory();
        GridLayout grid = factory.createComponent(GridLayout.NAME);
        grid.setSpacing(true);
        grid.setColumns(5);

        List<CreditsItem> items = new CreditsLoader().load().getItems();
        if (items.size() > 0) {
            grid.setRows(items.size());
            for (int i = 0, itemsSize = items.size(); i < itemsSize; i++) {
                final CreditsItem item = items.get(i);

                Label nameLab = factory.createComponent(Label.NAME);
                nameLab.setValue(item.getName());
                grid.add(nameLab, 0, i);

                Label dash = factory.createComponent(Label.NAME);
                dash.setValue("-");
                grid.add(dash, 1, i);

                LinkButton webpage = factory.createComponent(LinkButton.NAME);
                webpage.setCaption(getMessage("webpage"));
                if (companion != null) {
                    companion.initWebPageButton(webpage, item);
                }
                grid.add(webpage, 2, i);

                dash = factory.createComponent(Label.NAME);
                dash.setValue("-");
                grid.add(dash, 3, i);

                LinkButton license = factory.createComponent(LinkButton.NAME);
                license.setCaption(getMessage("license"));
                license.setAction(new AbstractAction("license") {
                    @Override
                    public void actionPerform(Component component) {
                        openWindow("license", WindowManager.OpenType.DIALOG, Collections.<String, Object>singletonMap("licenseText", item.getLicense()));
                    }
                });
                grid.add(license, 4, i);
            }

            scrollBox.add(grid);
        }
    }
}
