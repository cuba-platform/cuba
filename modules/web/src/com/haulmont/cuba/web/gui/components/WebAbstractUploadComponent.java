/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.UploadComponentSupport;

/**
 * @author petunin
 */
public abstract class WebAbstractUploadComponent<T extends com.vaadin.ui.Component>
        extends WebAbstractComponent<T>
        implements UploadComponentSupport {

    protected static final int BYTES_IN_MEGABYTE = 1048576;

    protected long fileSizeLimit = 0;

    @Override
    public long getFileSizeLimit() {
        return fileSizeLimit;
    }

    protected long getActualFileSizeLimit() {
        final long maxSize;
        if (fileSizeLimit > 0) {
            maxSize = fileSizeLimit;
        } else {
            Configuration configuration = AppBeans.get(Configuration.NAME);
            final long maxUploadSizeMb = configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb();
            maxSize = maxUploadSizeMb * BYTES_IN_MEGABYTE;
        }
        return maxSize;
    }

    protected String getFileSizeLimitString() {
        String fileSizeLimitString;
        if (fileSizeLimit > 0) {
            if (fileSizeLimit % BYTES_IN_MEGABYTE == 0) {
                fileSizeLimitString = String.valueOf(fileSizeLimit / BYTES_IN_MEGABYTE);
            } else {
                Datatype<Double> doubleDatatype = Datatypes.getNN(Double.class);
                double fileSizeInMb = fileSizeLimit / ((double) BYTES_IN_MEGABYTE);
                fileSizeLimitString = doubleDatatype.format(fileSizeInMb);
            }
        } else {
            Configuration configuration = AppBeans.get(Configuration.NAME);
            fileSizeLimitString = String.valueOf(configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb());
        }
        return fileSizeLimitString;
    }
}