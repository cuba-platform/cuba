/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.plaf.nimbus;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.awt.*;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public abstract class BaseMandatoryRegionPainter extends AbstractRegionPainter {

    protected Color colorRequired = decodeColor("cubaRequired", 0.0f, 0.0f, 0.0f, 0);
    /**
     * The only reason to have this is to access AbstractRegionPainter.PaintContextCacheMode which has protected access.
     */
    public static class AbstractRegionPainterPaintContext extends PaintContext {
        public AbstractRegionPainterPaintContext(Insets insets, Dimension canvasSize, boolean inverted, String cacheMode, double maxH, double maxV) {
            super(insets, canvasSize, inverted, CacheMode.valueOf(cacheMode), maxH, maxV);
        }
    }
}
