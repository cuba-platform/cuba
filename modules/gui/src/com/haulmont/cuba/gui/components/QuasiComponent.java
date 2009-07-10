/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.07.2009 11:04:07
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import java.util.Collection;

public interface QuasiComponent extends Component {

    Collection<Component> getRealComponents();
}
