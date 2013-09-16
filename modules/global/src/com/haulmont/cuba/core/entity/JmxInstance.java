/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author artamonov
 * @version $Id$
 */
@Entity(name = "sys$JmxInstance")
@Table(name = "SYS_JMX_INSTANCE")
@NamePattern("#getCaption|nodeName,address")
@SystemLevel
public class JmxInstance extends StandardEntity {

    @Column(name = "NODE_NAME", length = 255)
    protected String nodeName;

    @Column(name = "ADDRESS", length = 500, nullable = false)
    protected String address;

    @Column(name = "LOGIN", length = LOGIN_FIELD_LEN, nullable = false)
    protected String login;

    @Column(name = "PASSWORD", length = 255)
    protected String password;

    public JmxInstance() {
    }

    public JmxInstance(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaption() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(nodeName))
            sb.append(nodeName);

        if (StringUtils.isNotEmpty(address))
            sb.append(" (").append(address).append(")");

        return sb.toString();
    }
}