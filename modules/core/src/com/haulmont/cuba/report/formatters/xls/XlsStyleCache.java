/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report.formatters.xls;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class XlsStyleCache {

    private List<HSSFCellStyle> cellStyles = new ArrayList<HSSFCellStyle>();

    public XlsStyleCache() { }

    public HSSFCellStyle processCellStyle(HSSFCellStyle cellStyle) {
        for (HSSFCellStyle cacheStyle : cellStyles) {
            if (cacheStyle.formatEquals(cellStyle))
                return cacheStyle;
        }
        cellStyles.add(cellStyle);
        return cellStyle;
    }
}
