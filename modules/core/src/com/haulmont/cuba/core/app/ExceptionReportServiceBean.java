/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.core.app;

import com.haulmont.bali.util.HtmlUtils;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import org.codehaus.groovy.runtime.MethodClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service(ExceptionReportService.NAME)
public class ExceptionReportServiceBean implements ExceptionReportService {

    private static final Logger log = LoggerFactory.getLogger(ExceptionReportServiceBean.class);

    protected EmailerConfig emailerConfig;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected ResourceService resourceService;

    @Inject
    protected Scripting scripting;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected EmailerAPI emailer;

    @Inject
    protected void setEmailerConfig(EmailerConfig emailerConfig) {
        this.emailerConfig = emailerConfig;
    }

    @Override
    public void sendExceptionReport(String supportEmail, Map<String, Object> binding) {
        try {
            Map<String, Object> map = new HashMap<>(binding);
            User user = userSessionSource.getUserSession().getUser();
            map.put("userEmail", user.getEmail() != null ? user.getEmail() : emailerConfig.getFromAddress());
            map.put("toHtml", new MethodClosure(HtmlUtils.class, "convertToHtml"));

            String body = getExceptionReportBody(map);
            String subject = getExceptionReportSubject(map);

            EmailInfo info = new EmailInfo(supportEmail, subject, body);

            emailer.sendEmail(info);
        } catch (Exception e) {
            log.error("Error sending exception report", e);
            throw new RuntimeException("Error sending exception report");
        }
    }

    public String getExceptionReportBody(Map<String, Object> binding) {
        String bodyTemplate = resourceService.getResourceAsString(serverConfig.getExceptionReportEmailBodyTemplate());
        if (bodyTemplate == null) {
            throw new IllegalStateException("Unable to find template of exception report body");
        }
        //noinspection UnnecessaryLocalVariable
        String reportBody = getTemplate(binding, bodyTemplate);
        return reportBody;
    }


    public String getExceptionReportSubject(Map<String, Object> binding) {
        String subjectTemplate = resourceService.getResourceAsString(serverConfig.getExceptionReportEmailSubjectTemplate());
        if (subjectTemplate == null) {
            throw new IllegalStateException("Unable to find template of exception report subject");
        }
        //noinspection UnnecessaryLocalVariable
        String subjectBody = getTemplate(binding, subjectTemplate);
        return subjectBody;
    }

    protected String getTemplate(Map<String, Object> binding, String template) {
        SimpleTemplateEngine templateEngine = new SimpleTemplateEngine(scripting.getClassLoader());
        Template bodyTemplate;
        try {
            bodyTemplate = templateEngine.createTemplate(template);
        } catch (Exception e) {
            throw new RuntimeException("Unable to compile Groovy template", e);
        }

        String reportBody;
        try {
            reportBody = bodyTemplate.make(binding).writeTo(new StringWriter(0)).toString();
        } catch (IOException e) {
            throw new RuntimeException("Unable to write Groovy template content", e);
        }

        return reportBody;
    }
}