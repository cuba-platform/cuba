/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.App;

/**
 * @author novikov
 * @version $Id$
 */
public class ShowLinkAction extends AbstractAction {

    public static final String ACTION_ID = "showLink";

    public interface Handler {
        String makeLink(Entity entity);
    }

    protected CollectionDatasource ds;
    protected String mp;
    protected Handler handler;

    public ShowLinkAction(CollectionDatasource ds, Handler handler) {
        super(ACTION_ID);
        mp = AppConfig.getMessagesPack();
        this.ds = ds;
        this.handler = handler;
    }

    @Override
    public String getCaption() {
        return AppBeans.get(Messages.class).getMessage(mp, "table.showLinkAction");
    }

    @Override
    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
        if (ds == null)
            return;

        App.getInstance().getWindowManager().showMessageDialog(
                AppBeans.get(Messages.class).getMessage(mp, "table.showLinkAction"),
                compileLink(ds),
                IFrame.MessageType.CONFIRMATION_HTML
        );
    }

    protected String compileLink(CollectionDatasource ds) {
        StringBuilder sb = new StringBuilder();

        sb.append(AppBeans.get(Messages.class).getMessage(mp, "table.showLinkAction.link")).append("<br/>");
        sb.append("<textarea cols=\"55\" rows=\"5\" autofocus=\"true\" readonly=\"true\">").
                append(handler.makeLink(ds.getItem()).replace("&", "&amp")).append(" </textarea>");

        return sb.toString();
    }
}