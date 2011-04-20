/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.layout;

import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.UnitValue;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class MigLayoutHelper {

    public static UnitValue[] makeInsets(boolean[] margins) {
        UnitValue[] unitValues = new UnitValue[4];
        for (int i = 0; i < unitValues.length; i++) {
            unitValues[i] = margins[i] ? PlatformDefaults.getPanelInsets(i) : new UnitValue(0);
        }
        return unitValues;
    }
}
