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

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.entity.FileDescriptor;

import java.io.InputStream;

/**
 * The service might be used for work with filestorage, when you use local service invocation
 * (web and core are deployed to same JVM, or web and core are packaged to single WAR file)
 *
 */
public interface LocalFileExchangeService {
    String NAME = "cuba_LocalFileExchangeService";

    void uploadFile(InputStream inputStream, FileDescriptor fileDescriptor);

    InputStream downloadFile(FileDescriptor fileDescriptor);
}
