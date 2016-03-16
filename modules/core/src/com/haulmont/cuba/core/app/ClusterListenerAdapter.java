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

package com.haulmont.cuba.core.app;

/**
 * Adapter containing default implementation for simple cluster listeners that don't need state transfer.
 *
 */
public abstract class ClusterListenerAdapter<T> implements ClusterListener<T> {

    @Override
    public byte[] getState() {
        return new byte[0];
    }

    @Override
    public void setState(byte[] state) {
    }
}
