/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(ViewRepository.NAME)
public class ViewRepositoryClientImpl extends AbstractViewRepository implements ViewRepository {

    private boolean lazyLoadServerViews;

    @Inject
    private ServerInfoService serverInfoService;

    @Inject
    private Configuration configuration;

    @Override
    protected void init() {
        lazyLoadServerViews = configuration.getConfig(ClientConfig.class).getLazyLoadServerViews();

        if (!lazyLoadServerViews) {
            List<View> views = serverInfoService.getViews();
            for (View view : views) {
                MetaClass metaClass = metadata.getSession().getClass(view.getEntityClass());
                storeView(metaClass, view);
            }
        }

        super.init();
    }

    @Override
    protected View retrieveView(MetaClass metaClass, String name) {
        View view = super.retrieveView(metaClass, name);
        if (view == null && lazyLoadServerViews) {
            log.trace("Search for view " + metaClass + "/" + name + " on server");
            view = serverInfoService.getView(metaClass.getJavaClass(), name);
            if (view != null)
                storeView(metaClass, view);
        }
        return view;
    }
}
