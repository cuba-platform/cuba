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

package com.haulmont.cuba.gui.app.core.dev;

import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 */
public class LayoutAnalyzerWindow extends AbstractWindow {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected TextArea analyzeResultBox;

    @WindowParam(required = true)
    protected List<LayoutTip> tipsList;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams()
                .setResizable(true)
                .setWidth(themeConstants.getInt("cuba.gui.LayoutAnalyzerWindow.width"))
                .setHeight(themeConstants.getInt("cuba.gui.LayoutAnalyzerWindow.height"));

        StringBuilder analysisText = new StringBuilder();
        for (LayoutTip tip : tipsList) {
            analysisText.append("[").append(tip.errorType.name()).append("] ")
                    .append(tip.componentPath).append("\n")
                    .append(tip.message).append("\n\n");
        }
        String analysisLog = analysisText.toString().trim();

        log.info("Analyze layout\n" + analysisLog);

        analyzeResultBox.setValue(analysisLog);

        analyzeResultBox.setEditable(false);
    }

    public void closeWindow() {
        close(Window.CLOSE_ACTION_ID);
    }
}