/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.10.2009 18:25:15
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

public class FileStorageException extends Exception {

    public enum Type {
        IO_EXCEPTION("I/O error"),
        FILE_ALREADY_EXISTS("File already exists"),
        FILE_NOT_FOUND("File not found"),
        MORE_THAN_ONE_FILE("More than one file with this name exists");

        private String message;

        Type(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private Type type;
    private String fileName;

    public FileStorageException(Type type, String fileName) {
        this(type, fileName, null);
    }

    public FileStorageException(Type type, String fileName, Throwable cause) {
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
