/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
@javax.persistence.Entity(name = "sys$FileDescriptor")
@Table(name = "SYS_FILE")
@NamePattern("%s (%s)|name,createDate")
@SystemLevel
public class FileDescriptor extends StandardEntity {

    private static final long serialVersionUID = 564683944299730504L;

    @Column(name = "NAME", length = 500, nullable = false)
    private String name;

    @Column(name = "EXT", length = 20)
    private String extension;

    @Column(name = "FILE_SIZE")
    private Long size;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * @return file uploading timestamp
     */
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return file name including extension
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return file extension, i.e. the part of name after the last dot
     */
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = StringUtils.substring(extension, 0, 20);
    }

    /**
     * @return file size in bytes
     */
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Used by the framework to transfer file between application tiers.
     */
    public String toUrlParam() {
        return new StrBuilder()
                .append(id).append(",")
                .append(extension).append(",")
                .append(createDate.getTime())
                .toString();
    }

    /**
     * Used by the framework to transfer file between application tiers.
     */
    public static FileDescriptor fromUrlParam(String urlParam) {
        String[] parts = urlParam.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid FileDescriptor format");
        }
        FileDescriptor fd = new FileDescriptor();
        fd.setId(UuidProvider.fromString(parts[0]));
        fd.setExtension(parts[1]);
        fd.setCreateDate(new Date(Long.parseLong(parts[2])));
        return fd;
    }
}