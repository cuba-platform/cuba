/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.12.2009 17:14:06
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import java.util.Collection;

public interface AccessControl extends QuasiComponent {

    String NAME = "accessControl";

    void setRealComponents(Collection<Component> realComponents);

}
