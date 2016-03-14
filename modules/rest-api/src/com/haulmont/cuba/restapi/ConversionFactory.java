/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.restapi;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;

import javax.activation.MimeType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chevelev
 * @version $Id$
 */
public class ConversionFactory {
    private List<Convertor> convertors = new ArrayList<>();
    protected final int restApiVersion;

    public ConversionFactory() {
        convertors.add(new JSONConvertor());
        convertors.add(new XMLConvertor());
        convertors.add(new XMLConvertor2());

        RestConfig restConfig = AppBeans.get(Configuration.class).getConfig(RestConfig.class);
        restApiVersion = restConfig.getRestApiVersion();
    }

    public Convertor getConvertor(MimeType requestedForm) {
        if (requestedForm != null) {
            for (Convertor convertor : convertors) {
                if (requestedForm.match(convertor.getMimeType()) && convertor.getApiVersions().contains(restApiVersion))
                    return convertor;
            }
        }
        throw new RuntimeException("Convertor not found");
    }

    public Convertor getConvertor(String type) {
        for (Convertor convertor : convertors) {
            if (convertor.getType().equals(type) && convertor.getApiVersions().contains(restApiVersion))
                return convertor;
        }
        throw new RuntimeException("Convertor not found");
    }
}