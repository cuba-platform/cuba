/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.10.2009 12:15:26
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity(name = "core$FileDescriptor")
@Table(name = "SYS_FILE")
@NamePattern("%s (%s)|name,createDate")
public class FileDescriptor extends StandardEntity {

    @Column(name = "NAME", length = 500)
    private String name;

    @Column(name = "EXT", length = 20)
    private String extension;

    @Column(name = "SIZE")
    private Integer size;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getFileExt() {
        if (name != null) {
            int i = name.lastIndexOf('.');
            if (i > -1) {
                return StringUtils.substring(name, i + 1).toLowerCase();
            }
        }
        return "";
    }

    public String getFileName() {
        return id.toString() + "." + getExtension();
    }
}
