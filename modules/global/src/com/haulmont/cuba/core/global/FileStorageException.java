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
package com.haulmont.cuba.core.global;

import javax.annotation.Nullable;

@SupportedByClient
public class FileStorageException extends Exception {

    public enum Type {
        IO_EXCEPTION("I/O error", 500),
        FILE_ALREADY_EXISTS("File already exists", 500),
        FILE_NOT_FOUND("File not found", 404),
        MORE_THAN_ONE_FILE("More than one file with this name exists", 500),
        STORAGE_INACCESSIBLE("Can not get access to the storage", 503);

        private String message;

        private int httpStatus;

        Type(String message, int httpStatus) {
            this.message = message;
            this.httpStatus = httpStatus;
        }

        public String getMessage() {
            return message;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public static Type fromHttpStatus(int status) {
            for (Type type : values()) {
                if (type.getHttpStatus() == status)
                    return type;
            }
            return IO_EXCEPTION;
        }
    }

    private Type type;
    private String fileName;

    public FileStorageException(Type type, String fileName) {
        this(type, fileName, null);
    }

    public FileStorageException(Type type, String fileName, @Nullable Throwable cause) {
        super(type.getMessage() + ": " + fileName, cause);
        this.type = type;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public Type getType() {
        return type;
    }
}