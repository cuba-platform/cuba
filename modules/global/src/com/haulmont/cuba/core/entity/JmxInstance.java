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

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
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