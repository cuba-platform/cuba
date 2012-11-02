/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

/**
 * Information about remote invocation client.
 *
 * @author artamonov
 * @version $Id$
 */
public class RemoteClientInfo {

    private String host;
    private Integer port;
    private String address;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private static final ThreadLocal<RemoteClientInfo> clientInfo = new ThreadLocal<>();

    public static RemoteClientInfo get() {
        return clientInfo.get();
    }

    public static void set(RemoteClientInfo info) {
        clientInfo.set(info);
    }

    public static void clear() {
        clientInfo.set(null);
    }
}