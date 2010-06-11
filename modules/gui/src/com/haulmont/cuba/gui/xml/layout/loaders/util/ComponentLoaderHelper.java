/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 11.06.2010 14:11:54
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders.util;

import com.haulmont.cuba.gui.components.ShortcutAction;

public class ComponentLoaderHelper {

    public static ShortcutAction.KeyCombination keyCombination(String keyString) {
        if (keyString == null) return null;
        keyString = keyString.toUpperCase();

        ShortcutAction.Key key = null;
        ShortcutAction.Modifier[] modifiers = null;

        if (keyString.indexOf("-") > -1) {
            String[] keys = keyString.split("-", -1);

            int modifiersCnt = keys.length;

            //try {
                key = ShortcutAction.Key.valueOf(keys[modifiersCnt - 1]);
                --modifiersCnt;
            /*} catch (IllegalArgumentException e) {
                //ignore
            }*/
            modifiers = new ShortcutAction.Modifier[modifiersCnt];
            for (int i = 0; i < modifiersCnt; i++) {
                modifiers[i] = ShortcutAction.Modifier.valueOf(keys[i]);
            }
        } else {
            //try {
                key = ShortcutAction.Key.valueOf(keyString);
            /*} catch (IllegalArgumentException e) {
                modifiers = new ShortcutAction.Modifier[] {
                        ShortcutAction.Modifier.valueOf(keyString)
                };
            }*/
        }
        return new ShortcutAction.KeyCombination(key, modifiers);
    }
}
