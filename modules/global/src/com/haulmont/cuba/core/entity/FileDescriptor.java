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
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import javax.persistence.Column;
import javax.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@javax.persistence.Entity(name = "sys$FileDescriptor")
@Table(name = "SYS_FILE")
@NamePattern("%s (%s)|name,createDate")
@SystemLevel
public class FileDescriptor extends StandardEntity {

    private static final long serialVersionUID = 564683944299730504L;
    public static final String DATE_FMT = "yyyy-MM-dd";

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

    public String toUrlParam() {
        return new StrBuilder()
                .append(id).append(",")
                .append(extension).append(",")
                .append(new SimpleDateFormat(DATE_FMT).format(createDate))
                .toString();
    }

    public static FileDescriptor fromUrlParam(String urlParam) {
        String[] parts = urlParam.split(",");
        if (parts.length != 3)
            throw new IllegalArgumentException("Invalid FileDescriptor format");
        FileDescriptor fd = new FileDescriptor();
        fd.setId(UUID.fromString(parts[0]));
        fd.setExtension(parts[1]);
        try {
            fd.setCreateDate(new SimpleDateFormat(DATE_FMT).parse(parts[2]));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid FileDescriptor format", e);
        }
        return fd;
    }
}
