/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.plaf.nimbus;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.awt.*;

/**
 */
public abstract class BaseMandatoryRegionPainter extends AbstractRegionPainter {

    /**
     * The only reason to have this is to access AbstractRegionPainter.PaintContextCacheMode which has protected access.
     */
    public static class AbstractRegionPainterPaintContext extends PaintContext {
        public AbstractRegionPainterPaintContext(Insets insets, Dimension canvasSize,
                                                 boolean inverted, String cacheMode, double maxH, double maxV) {
            super(insets, canvasSize, inverted, CacheMode.valueOf(cacheMode), maxH, maxV);
        }
    }
}