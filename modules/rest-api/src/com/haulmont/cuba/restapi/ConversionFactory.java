/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.restapi;

import com.haulmont.cuba.core.global.Configuration;
import org.springframework.stereotype.Component;

import javax.activation.MimeType;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Component
public class ConversionFactory {
    private List<Convertor> convertors = new ArrayList<>();

    protected int restApiVersion;

    @Inject
    protected Configuration configuration;

    @Inject
    protected JSONConvertor jsonConvertor;

    @Inject
    protected XMLConvertor xmlConvertor;

    @Inject
    protected XMLConvertor2 xmlConvertor2;

    @PostConstruct
    private int init() {
        convertors.add(jsonConvertor);
        convertors.add(xmlConvertor);
        convertors.add(xmlConvertor2);

        RestConfig restConfig = configuration.getConfig(RestConfig.class);
        restApiVersion = restConfig.getRestApiVersion();
        return restApiVersion;
    }

    public Convertor getConvertor(MimeType requestedForm) {
        if (requestedForm != null) {
            for (Convertor convertor : getConvertors()) {
                if (requestedForm.match(convertor.getMimeType()) && convertor.getApiVersions().contains(restApiVersion))
                    return convertor;
            }
        }
        throw new RuntimeException("Convertor not found");
    }

    private List<Convertor> getConvertors() {return convertors;}

    public Convertor getConvertor(String type) {
        for (Convertor convertor : getConvertors()) {
            if (convertor.getType().equals(type) && convertor.getApiVersions().contains(restApiVersion))
                return convertor;
        }
        throw new RuntimeException("Convertor not found");
    }
}