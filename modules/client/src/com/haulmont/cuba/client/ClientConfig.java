/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInteger;
import com.haulmont.cuba.core.config.defaults.DefaultString;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Source(type = SourceType.APP)
public interface ClientConfig extends Config {

    @Property("cuba.connectionUrl")
    String getConnectionUrl();

    @Property("cuba.fileDownloadContext")
    String getFileDownloadContext();

    @Property("cuba.client.maxUploadSizeMb")
    @DefaultInteger(20)
    Integer getMaxUploadSizeMb();

    @Property("cuba.collectionDatasourceDbSortEnabled")
    @DefaultBoolean(true)
    boolean getCollectionDatasourceDbSortEnabled();

    @Property("cuba.screenIdsToSaveHistory")
    @Default("sec$User.edit,sec$Group.edit,sec$Role.edit")
    String getScreenIdsToSaveHistory();

    @Property("cuba.passwordPolicyEnabled")
    @DefaultBoolean(false)
    public boolean getPasswordPolicyEnabled();

    @Property("cuba.passwordPolicyRegExp")
    @DefaultString("((?=.*\\d)(?=.*\\p{javaLowerCase})(?=.*\\p{javaUpperCase}).{6,20})")
    public String getPasswordPolicyRegExp();
}
