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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.gui.components.UploadField;

import java.util.Set;

public abstract class WebAbstractUploadField<T extends com.vaadin.v7.ui.AbstractField>
        extends WebAbstractField<T, FileDescriptor>
        implements UploadField {

    protected static final int BYTES_IN_MEGABYTE = 1048576;

    protected long fileSizeLimit = 0;

    protected Set<String> permittedExtensions;

    protected DropZone dropZone;
    protected ComponentContainer pasteZone;
    protected String dropZonePrompt;

    @Override
    public long getFileSizeLimit() {
        return fileSizeLimit;
    }

    protected long getActualFileSizeLimit() {
        if (fileSizeLimit > 0) {
            return fileSizeLimit;
        } else {
            Configuration configuration = beanLocator.get(Configuration.NAME);
            long maxUploadSizeMb = configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb();

            return maxUploadSizeMb * BYTES_IN_MEGABYTE;
        }
    }

    @Override
    public Set<String> getPermittedExtensions() {
        return permittedExtensions;
    }

    @Override
    public void setPermittedExtensions(Set<String> permittedExtensions) {
        this.permittedExtensions = permittedExtensions;
    }

    protected String getFileSizeLimitString() {
        String fileSizeLimitString;
        if (fileSizeLimit > 0) {
            if (fileSizeLimit % BYTES_IN_MEGABYTE == 0) {
                fileSizeLimitString = String.valueOf(fileSizeLimit / BYTES_IN_MEGABYTE);
            } else {
                DatatypeRegistry datatypeRegistry = beanLocator.get(DatatypeRegistry.NAME);
                Datatype<Double> doubleDatatype = datatypeRegistry.getNN(Double.class);
                double fileSizeInMb = fileSizeLimit / ((double) BYTES_IN_MEGABYTE);
                fileSizeLimitString = doubleDatatype.format(fileSizeInMb);
            }
        } else {
            Configuration configuration = beanLocator.get(Configuration.NAME);
            fileSizeLimitString = String.valueOf(configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb());
        }
        return fileSizeLimitString;
    }

    @Override
    public DropZone getDropZone() {
        return dropZone;
    }

    @Override
    public void setDropZone(DropZone dropZone) {
        this.dropZone = dropZone;
    }

    @Override
    public void setPasteZone(ComponentContainer pasteZone) {
        this.pasteZone = pasteZone;
    }

    @Override
    public ComponentContainer getPasteZone() {
        return pasteZone;
    }

    @Override
    public String getDropZonePrompt() {
        return dropZonePrompt;
    }

    @Override
    public void setDropZonePrompt(String dropZonePrompt) {
        this.dropZonePrompt = dropZonePrompt;
    }
}