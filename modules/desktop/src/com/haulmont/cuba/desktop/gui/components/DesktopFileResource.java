/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.components.FileResource;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DesktopFileResource extends DesktopAbstractStreamSettingsResource
        implements DesktopResource, FileResource {

    protected File file;

    @Override
    public FileResource setFile(File file) {
        Preconditions.checkNotNullArgument(file);

        this.file = file;
        hasSource = true;

        fireResourceUpdateEvent();

        return this;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    protected void createResource() {
        try {
            resource = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while loading an image.", e);
        }
    }
}
