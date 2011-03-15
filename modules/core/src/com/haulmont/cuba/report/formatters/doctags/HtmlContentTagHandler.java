/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 10.03.11 17:11
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.doctags;

import com.haulmont.cuba.report.formatters.oo.OfficeComponent;
import com.sun.star.beans.PropertyValue;
import com.sun.star.document.XDocumentInsertable;
import com.sun.star.io.IOException;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.haulmont.cuba.report.formatters.oo.ODTUnoConverter.asXDocumentInsertable;

/**
 * Handle HTML with format string: ${html}
 */
public class HtmlContentTagHandler implements TagHandler {

    public final static String REGULAR_EXPRESSION = "\\$\\{html\\}";

    private Pattern tagPattern;

    public HtmlContentTagHandler() {
        tagPattern = Pattern.compile(REGULAR_EXPRESSION, Pattern.CASE_INSENSITIVE);
    }

    public Pattern getTagPattern() {
        return tagPattern;
    }

    public void handleTag(OfficeComponent officeComponent,
                          XText destination, XTextRange textRange,
                          Object paramValue, Matcher matcher) throws Exception {
        boolean inserted = false;
        if (paramValue != null) {
            String htmlContent = paramValue.toString();
            if (!StringUtils.isEmpty(htmlContent)) {
                try {
                    insertHTML(destination, textRange, htmlContent);
                    inserted = true;
                } catch (Exception ignored) {
                }
            }
        }
        if (!inserted)
            destination.getText().insertString(textRange, "", true);
    }

    private void insertHTML(XText destination, XTextRange textRange, String htmlContent)
            throws Exception {
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".htm");
        FileOutputStream fileOutput = new FileOutputStream(tempFile);
        try {
            fileOutput.write(htmlContent.getBytes());
        } finally {
            fileOutput.close();
        }

        try {
            String filePath = tempFile.getCanonicalPath().replace("\\", "/");
            StringBuffer sUrl = new StringBuffer("file:///");
            sUrl.append(filePath);

            XTextCursor textCursor = destination.createTextCursorByRange(textRange);
            XDocumentInsertable docInsertable = asXDocumentInsertable(textCursor);

            docInsertable.insertDocumentFromURL(sUrl.toString(), new PropertyValue[0]);
        } finally {
            tempFile.delete();
        }
    }
}
