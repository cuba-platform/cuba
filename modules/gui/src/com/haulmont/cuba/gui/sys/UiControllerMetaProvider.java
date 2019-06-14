/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.gui.screen.FrameOwner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(UiControllerMetaProvider.NAME)
public class UiControllerMetaProvider {

    public static final String NAME = "cuba_UiControllerMetaProvider";

    protected AnnotationScanMetadataReaderFactory metadataReaderFactory;

    @Inject
    public void setMetadataReaderFactory(AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        this.metadataReaderFactory = metadataReaderFactory;
    }

    public UiControllerMeta get(MetadataReader metadataReader) {
        return new UiControllerResourceMeta(metadataReader, metadataReaderFactory);
    }

    public UiControllerMeta get(Class<? extends FrameOwner> screenClass) {
        return new UiControllerClassMeta(screenClass);
    }
}
