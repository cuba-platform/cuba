package com.haulmont.cuba.web.toolkit.ui;

//import com.haulmont.cuba.toolkit.gwt.client.ui.IScrollablePanel;
//import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * User: Nikolay Gorodnov
 * Date: 14.04.2009
 *
 * TODO artamonov just remove
 */
@SuppressWarnings("serial")
//@ClientWidget(IScrollablePanel.class)
public class ScrollablePanel extends Panel {

    public ScrollablePanel() {
        setContent(new VerticalLayout());
    }
}