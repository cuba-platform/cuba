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

package com.haulmont.cuba.web.app.ui.jmxcontrol.inspect.operation;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.jmx.JmxControlException;
import com.haulmont.cuba.web.jmx.entity.AttributeHelper;
import com.vaadin.shared.ui.label.ContentMode;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.inject.Inject;
import javax.management.MBeanException;
import java.lang.reflect.UndeclaredThrowableException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class OperationResultWindow extends AbstractWindow {

    @Inject
    protected Label resultLabel;

    @Inject
    protected ScrollBoxLayout resultContainer;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected FileUploadingAPI fileUploading;

    @Inject
    protected ExportDisplay exportDisplay;

    @Inject
    protected TimeSource timeSource;

    @WindowParam
    protected Object result;

    @WindowParam
    protected String methodName;

    @WindowParam
    protected String beanName;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogOptions()
                .setResizable(true)
                .setWidth(themeConstants.getInt("cuba.web.jmx.OperationResultWindow.width"))
                .setHeight(themeConstants.getInt("cuba.web.jmx.OperationResultWindow.height"));

        Throwable ex = (Throwable) params.get("exception");

        ComponentsFactory componentsFactory = AppConfig.getFactory();

        if (ex != null) {
            if (ex instanceof UndeclaredThrowableException)
                ex = ex.getCause();

            if (ex instanceof JmxControlException) {
                ex = ex.getCause();

                if (ex instanceof MBeanException) {
                    ex = ex.getCause();
                }
            }

            String msg;
            if (ex != null) {
                msg = ex.getClass().getName() +
                        ": \n" + ex.getMessage() +
                        "\n" + ExceptionUtils.getFullStackTrace(ex);
            } else {
                msg = "";
            }

            Label trace = componentsFactory.createComponent(Label.class);
            trace.setFrame(getFrame());
            trace.setValue(msg);

            resultLabel.setValue(getMessage("operationResult.exception"));
            resultContainer.add(trace);

        } else if (result != null) {
            Label valueHolder = componentsFactory.createComponent(Label.class);
            valueHolder.setFrame(getFrame());

            com.vaadin.ui.Label vaadinLbl = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(valueHolder);
            vaadinLbl.setContentMode(ContentMode.PREFORMATTED);
            valueHolder.setValue(AttributeHelper.convertToString(result));

            resultLabel.setValue(getMessage("operationResult.result"));
            resultContainer.add(valueHolder);
        } else {
            resultLabel.setValue(getMessage("operationResult.void"));
        }
    }

    public void close() {
        close(CLOSE_ACTION_ID);
    }

    public void exportToFile() {
        if (result != null) {
            byte[] bytes = AttributeHelper.convertToString(result).getBytes();
            exportDisplay.show(new ByteArrayDataProvider(bytes),
                    String.format("jmx.%s-%s-%s.log",
                            beanName,
                            methodName,
                            new SimpleDateFormat("HH:mm:ss").format(
                                    timeSource.currentTimestamp())));
        } else {
            showNotification(getMessage("operationResult.resultIsEmpty"), NotificationType.HUMANIZED);
        }
    }
}