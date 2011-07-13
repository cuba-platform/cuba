/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

/**
 * User space FileNotFoundException
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class FileMissingException extends ResourceException {

    private String fileName;

    public FileMissingException(String fileName) {
        this.fileName = fileName;
    }

    public FileMissingException(String message, String fileName) {
        super(message);
        this.fileName = fileName;
    }

    public FileMissingException(String message, Throwable cause, String fileName) {
        super(message, cause);
        this.fileName = fileName;
    }

    public FileMissingException(Throwable cause, String fileName) {
        super(cause);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getResourceName() {
        return fileName;
    }
}
