/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.upload;

import com.vaadin.client.ui.VUpload;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaUploadWidget extends VUpload {

    public void setAccept(String accept) {
        if (accept != null) {
            fu.getElement().setAttribute("accept", accept);
        } else {
            fu.getElement().removeAttribute("accept");
        }
    }
}