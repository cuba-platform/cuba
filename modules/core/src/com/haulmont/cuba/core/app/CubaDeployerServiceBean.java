/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.08.2009 12:50:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.config.type.Stringify;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.MetadataBuildHelper;
import org.springframework.stereotype.Service;

import javax.annotation.ManagedBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Service facade for {@link com.haulmont.cuba.core.app.CubaDeployer} MBean
 */
@ManagedBean(CubaDeployerService.NAME)
public class CubaDeployerServiceBean implements CubaDeployerService {

    public String getReleaseNumber() {
        CubaDeployerMBean mBean = Locator.lookup(CubaDeployerMBean.NAME);
        return mBean.getReleaseNumber();
    }

    public String getReleaseTimestamp() {
        CubaDeployerMBean mBean = Locator.lookup(CubaDeployerMBean.NAME);
        return mBean.getReleaseTimestamp();
    }

    public MetadataBuildInfo getMetadataBuildInfo() {
        return new MetadataBuildInfo(
                MetadataBuildHelper.getPersistentEntitiesPackages(),
                MetadataBuildHelper.getTransientEntitiesPackages()
        );
    }

    public List<View> getViews() {
        return MetadataProvider.getViewRepository().getAll();
    }

    public Map<String, String> getReplacedEntities() {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<Class, Class> entry : MetadataProvider.getReplacedEntities().entrySet()) {
            result.put(entry.getKey().getName(), entry.getValue().getName());
        }
        return result;
    }
}
