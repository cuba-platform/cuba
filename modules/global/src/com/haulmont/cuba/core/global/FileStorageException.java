/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import javax.annotation.Nullable;

/**
 * @author krivopustov
 * @version $Id$
 */
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
