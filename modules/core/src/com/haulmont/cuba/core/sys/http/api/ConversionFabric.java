/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.http.api;

import javax.activation.MimeType;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Author: Alexander Chevelev
 * Date: 26.04.2011
 * Time: 2:00:09
 */
public class ConversionFabric {
    private Collection<Convertor> convertors = new ArrayList<Convertor>();

    public ConversionFabric() {
        convertors.add(new XMLConvertor());
        convertors.add(new JSONConvertor());
    }

    public Convertor getConvertor(MimeType requestedForm) {
        if (requestedForm == null) {
            return convertors.iterator().next();
        }

        for (Convertor convertor : convertors) {
            if (requestedForm.match(convertor.getMimeType())) {
                return convertor;
            }
        }
        return convertors.iterator().next();
    }
}
