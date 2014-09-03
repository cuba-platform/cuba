/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.dev;

import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class LayoutAnalyzerWindow extends AbstractWindow {

    protected Log log = LogFactory.getLog(getClass());

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected TextArea analyzeResultBox;

    @WindowParam(name = "window", required = true)
    protected Window analysisWindow;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams()
                .setResizable(true)
                .setWidth(themeConstants.getInt("cuba.gui.LayoutAnalyzerWindow.width"))
                .setHeight(themeConstants.getInt("cuba.gui.LayoutAnalyzerWindow.height"));

        LayoutAnalyzer analyzer = new LayoutAnalyzer();

        StringBuilder analysisText = new StringBuilder();
        List<LayoutTip> tips = analyzer.analyze(analysisWindow);
        if (tips.isEmpty()) {
            analyzeResultBox.setValue("No layout problems found");
        } else {
            for (LayoutTip tip : tips) {
                analysisText.append("[").append(tip.errorType.name()).append("] ")
                        .append(tip.componentPath).append("\n")
                        .append(tip.message).append("\n\n");
            }
            String analysisLog = analysisText.toString().trim();

            log.info("Analyze layout\n" + analysisLog);

            analyzeResultBox.setValue(analysisLog);
        }

        analyzeResultBox.setEditable(false);
    }

    public void closeWindow() {
        close(Window.CLOSE_ACTION_ID);
    }
}