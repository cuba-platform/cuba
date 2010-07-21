/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 02.06.2010 17:07:36
 *
 * $Id$
 */
package haulmont.report;

import com.haulmont.cuba.report.CustomReport;
import com.haulmont.cuba.report.Report;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class CustomReport1 implements CustomReport {
    public byte[] createReport(Report report, Map<String, Object> params) {
        HSSFWorkbook resultWorkbook = new HSSFWorkbook();
        HSSFSheet resultSheet = resultWorkbook.createSheet();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resultWorkbook.write(byteArrayOutputStream);
            byte[] result = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
