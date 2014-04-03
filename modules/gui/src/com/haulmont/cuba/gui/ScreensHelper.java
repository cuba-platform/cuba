/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.config.WindowInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class ScreensHelper {

    /**
     * Sorts window infos alphabetically, takes into account $ mark
     */
    public static void sortWindowInfos(List<WindowInfo> windowInfoCollection) {
        Collections.sort(windowInfoCollection, new Comparator<WindowInfo>() {
            @Override
            public int compare(WindowInfo w1, WindowInfo w2) {
                int w1DollarIndex = w1.getId().indexOf("$");
                int w2DollarIndex = w2.getId().indexOf("$");

                if ((w1DollarIndex > 0 && w2DollarIndex > 0) || (w1DollarIndex < 0 && w2DollarIndex < 0)) {
                    return w1.getId().compareTo(w2.getId());
                } else if (w1DollarIndex > 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }
}