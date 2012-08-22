/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ViewRepositoryClient extends ViewRepository {

    private Log log = LogFactory.getLog(getClass());

    private boolean lazyLoadServerViews;
    private ServerInfoService serverInfoService;

    public ViewRepositoryClient(Metadata metadata, boolean lazyLoadServerViews, ServerInfoService serverInfoService) {
        super(metadata);
        this.lazyLoadServerViews = lazyLoadServerViews;
        this.serverInfoService = serverInfoService;
    }

    @Override
    protected View findView(MetaClass metaClass, String name) {
        View view = super.findView(metaClass, name);
        if (view == null && lazyLoadServerViews) {
            log.trace("Search for view " + metaClass + "/" + name + " on server");
            view = serverInfoService.getView(metaClass.getJavaClass(), name);
            if (view != null)
                storeView(metaClass, view);
        }
        return view;
    }
}
