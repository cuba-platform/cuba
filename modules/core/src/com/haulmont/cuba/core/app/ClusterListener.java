/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.06.2010 19:02:56
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import java.io.Serializable;

public interface ClusterListener<T> {

    void receive(T message);
    
    byte[] getState();

    void setState(byte[] state);
}
