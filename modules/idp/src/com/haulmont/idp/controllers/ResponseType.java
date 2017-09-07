/*
 * Copyright (c) 2008-2017 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.idp.controllers;

/**
 * Available IDP ticket response types.
 */
public enum ResponseType {
    /**
     * IDP ticket is sent using URL hash: {@code //service-provider-url#idp_ticket=value}
     */
    CLIENT_TICKET("client-ticket"),
    /**
     * IDP ticket is sent using URL parameter: {@code //service-provider-url?idp_ticket=value}
     */
    SERVER_TICKET("server-ticket");

    private String code;

    ResponseType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}