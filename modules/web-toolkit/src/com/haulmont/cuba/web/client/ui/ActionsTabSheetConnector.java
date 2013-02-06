package com.haulmont.cuba.web.client.ui;

import com.haulmont.cuba.web.toolkit.ui.ActionsTabSheet;
import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(value = ActionsTabSheet.class, loadStyle = Connect.LoadStyle.EAGER)
public class ActionsTabSheetConnector extends TabsheetConnector {
}