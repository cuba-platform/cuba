/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.App;

/**
 * @author novikov
 * @version $Id$
 */
public class ShowLinkAction extends BaseAction {

    public static final String ACTION_ID = "showLink";

    public interface Handler {
        String makeLink(Entity entity);
    }

    protected CollectionDatasource ds;
    protected Handler handler;

    public ShowLinkAction(CollectionDatasource ds, Handler handler) {
        super(ACTION_ID);

        this.ds = ds;
        this.handler = handler;
    }

    @Override
    public String getCaption() {
        return messages.getMainMessage("table.showLinkAction");
    }

    @Override
    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
        if (ds == null)
            return;

        App.getInstance().getWindowManager().showMessageDialog(
                messages.getMainMessage("table.showLinkAction"),
                compileLink(ds),
                IFrame.MessageType.CONFIRMATION_HTML
        );
    }

    private String compileLink(CollectionDatasource ds) {
        StringBuilder sb = new StringBuilder();

        sb.append(messages.getMainMessage("table.showLinkAction.link")).append("<br/>");
        sb.append("<textarea class=\"cuba-table-showlink-textarea\" autofocus=\"true\" readonly=\"true\">").
                append(handler.makeLink(ds.getItem()).replace("&", "&amp")).append("</textarea>");

        return sb.toString();
    }
}