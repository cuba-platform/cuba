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
package com.haulmont.cuba.gui.export;

import org.apache.poi.hssf.usermodel.HSSFFont;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.HashMap;

/**
 *  Calculates the width of a column, based on the values within it.
 * <p> For each new value added to the column, call {@link #isNotificationRequired}.
 * If the result is true, call {@link #notifyCellValue}.
 */
public class ExcelAutoColumnSizer
{
    private static final short WIDTH_MIN = 40;
    private static final short WIDTH_MAX = 250;

    private static final short WIDTH_PADDING = 5;

    private static final int[] ROW_BAND = {1, 100, 1000, 10000, 65536};
    private static final int[] ROW_BAND_SAMPLE_FREQUENCY = {1, 10, 100, 1000};

    /** Graphics context used for obtaining FontMetrics objects */
    private Graphics2D graphics = null;

    /** Maps a Short (HSSF font index) to a FontMetrics object */
    private Map fontMetrics = new HashMap();

    private short currentWidth = WIDTH_MIN;

    private FontMetrics getFontMetrics(HSSFFont hf){
        FontMetrics fm;
        Short pFont = new Short(hf.getIndex());

        fm = (FontMetrics) fontMetrics.get(pFont);
        if (fm == null) {
            int style;
            if((hf.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD) || hf.getItalic()) { style = 0; if(hf.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD) style ^= Font.BOLD; if(hf.getItalic()) style ^= Font.ITALIC; } else { style = Font.PLAIN; }
            Font f = new java.awt.Font(hf.getFontName(), style, hf.getFontHeightInPoints());

            if (graphics == null) {
                BufferedImage i = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY); graphics = i.createGraphics(); }

            fm = graphics.getFontMetrics(f);
            fontMetrics.put(pFont, fm);
        }

        return fm;
    }

    /**
     * When you add a new value to a column, call this method to ask whether
     * the AutoColumnSizer is interested in it.
    */
    public boolean isNotificationRequired(int row) {
        if (row < 0)
            throw new IllegalArgumentException("illegal row: " + row);

        /* To improve performance, we calculate column widths based on * a SAMPLE of all rows. */

        int rowBand = -1;
        for (int band = 0; band < ROW_BAND.length; band++) {
            if (row < ROW_BAND[band]) {
                rowBand = band - 1;
                break;
            }
        }

        if (rowBand == -1) {
            return false;
        } else if ((row % ROW_BAND_SAMPLE_FREQUENCY[rowBand]) != 0) {
            return false;
        } else {
            return true;
        }
    }

    public void notifyCellValue(String val, HSSFFont font) {
        if (val == null || val.length() == 0) return;
        if (font == null) throw new IllegalArgumentException("font is null");

        short width;
        {
            FontMetrics fm = getFontMetrics(font);
            int w = fm.stringWidth(val);
            width = (w > Short.MAX_VALUE) ? Short.MAX_VALUE : (short) w;
            // TODO - this gives an underestimate with large font-sizes.
            // TODO - What we *should* be considering is the 'display width'.
            // This means we'd have to take into account cell type & format.
        }

        if (width > currentWidth) {
            currentWidth = width;
        }
    }

    public short getWidth() {
        if ((currentWidth + WIDTH_PADDING) <= WIDTH_MAX) return (short) (currentWidth + WIDTH_PADDING);
        else return WIDTH_MAX;
    }

    public void dispose() {
        if (graphics != null) {
            graphics.finalize();
            graphics = null;
        }
        fontMetrics = null;
    }
}
