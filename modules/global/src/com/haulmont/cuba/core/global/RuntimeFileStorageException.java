/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.global;

@SupportedByClient
public class RuntimeFileStorageException extends RuntimeException {
    public RuntimeFileStorageException(FileStorageException cause) {
        super(cause);
    }

    public RuntimeFileStorageException(String message, FileStorageException cause) {
        super(message, cause);
    }

    @Override
    public synchronized FileStorageException getCause() {
        return (FileStorageException) super.getCause();
    }
}