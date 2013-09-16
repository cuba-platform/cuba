/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import java.io.Serializable;

public class EmailAttachment implements Serializable
{
    private static final long serialVersionUID = 8201729520638588939L;
    
    private byte[] data;
    private String name;
    private String contentId;

    public EmailAttachment(byte[] data, String name) {
        this(data, name, null);
    }

    public EmailAttachment(byte[] data, String name, String contentId) {
        this.data = data;
        this.name = name;
        this.contentId = contentId;
    }

    public byte[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getContentId() {
        return contentId;
    }
}
