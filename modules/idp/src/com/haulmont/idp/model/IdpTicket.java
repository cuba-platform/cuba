/*
 * Copyright (c) 2008-2017 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.idp.model;

public class IdpTicket {
    private String idpTicket;

    public IdpTicket(String idpTicket) {
        this.idpTicket = idpTicket;
    }

    public String getIdpTicket() {
        return idpTicket;
    }

    public void setIdpTicket(String idpTicket) {
        this.idpTicket = idpTicket;
    }
}