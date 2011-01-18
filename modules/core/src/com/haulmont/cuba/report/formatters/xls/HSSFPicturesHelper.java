/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 18.01.2011 12:17:55
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.xls;

import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class HSSFPicturesHelper {
    private HSSFPicturesHelper() {
    }

    public static List<HSSFClientAnchor> getAllAnchors(EscherAggregate escherAggregate) {
        List<HSSFClientAnchor> pictures = new ArrayList<HSSFClientAnchor>();
        if (escherAggregate == null) return Collections.emptyList();
        List<EscherRecord> escherRecords = escherAggregate.getEscherRecords();
        searchForAnchors(escherRecords, pictures);
        return pictures;
    }

    public static void searchForAnchors(List escherRecords, List<HSSFClientAnchor> pictures) {
        Iterator recordIter = escherRecords.iterator();
        HSSFClientAnchor anchor = null;
        while (recordIter.hasNext()) {
            Object obj = recordIter.next();
            if (obj instanceof EscherRecord) {
                EscherRecord escherRecord = (EscherRecord) obj;
                if (escherRecord instanceof EscherClientAnchorRecord) {
                    EscherClientAnchorRecord anchorRecord = (EscherClientAnchorRecord) escherRecord;
                    if (anchor == null) anchor = new HSSFClientAnchor();
                    anchor.setDx1(anchorRecord.getDx1());
                    anchor.setDx2(anchorRecord.getDx2());
                    anchor.setDy1(anchorRecord.getDy1());
                    anchor.setDy2(anchorRecord.getDy2());
                    anchor.setRow1(anchorRecord.getRow1());
                    anchor.setRow2(anchorRecord.getRow2());
                    anchor.setCol1(anchorRecord.getCol1());
                    anchor.setCol2(anchorRecord.getCol2());
                }
                // Recursive call.
                searchForAnchors(escherRecord.getChildRecords(), pictures);
            }
        }
        if (anchor != null)
            pictures.add(anchor);
    }
}
