/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import javax.activation.MimeType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chevelev
 * @version $Id$
 */
public class ConversionFactory {
    private Map<String, Convertor> convertors = new HashMap<>();

    public ConversionFactory() {
        convertors.put("xml", new XMLConvertor());
        convertors.put("json", new JSONConvertor());
    }

    public Convertor getConvertor(MimeType requestedForm) {
        if (requestedForm == null) {
            return convertors.values().iterator().next();
        }

        for (Convertor convertor : convertors.values()) {
            if (requestedForm.match(convertor.getMimeType())) {
                return convertor;
            }
        }
        return convertors.values().iterator().next();
    }

    public Convertor getConvertor(String type) {
        Convertor convertor = convertors.get(type);
        if (convertor == null)
            convertors.values().iterator().next();
        return convertor;
    }
}