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

import com.haulmont.cuba.gui.components.ResourceView;

public abstract class DesktopAbstractStreamSettingsResource extends DesktopAbstractResource
        implements ResourceView.HasStreamSettings {

    // just stub
    protected long cacheTime = 1000 * 60 * 60 * 24;
    // just stub
    protected int bufferSize;
    // just stub
    protected String fileName;

    @Override
    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    @Override
    public long getCacheTime() {
        return cacheTime;
    }

    @Override
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
