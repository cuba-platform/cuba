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

package com.haulmont.cuba.portal.springframework.view;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.portal.config.PortalConfig;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FreeMarkerView extends org.springframework.web.servlet.view.freemarker.FreeMarkerView {

    @Inject
    protected Messages messages;

    protected Logger log = LoggerFactory.getLogger(FreeMarkerView.class);

    @Override
    protected SimpleHash buildTemplateModel(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        PortalConfig config = AppBeans.get(Configuration.class).getConfig(PortalConfig.class);

        SimpleHash context = super.buildTemplateModel(model, request, response);
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext != null)
            context.put("userSession", securityContext.getSession());
        context.put("messages", messages);
        context.put("message", new MessageMethod());
        context.put("theme", config.getTheme());
        return context;
    }

    @Override
    protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response)
            throws IOException, TemplateException {
        CharArrayWriter printWriter;
        printWriter = new CharArrayWriter();
        try {
            template.process(model, printWriter);
            response.getWriter().write(printWriter.toCharArray());
        } catch (IOException e) {
            log.error("IO Exception", e);
        } catch (TemplateException e) {
            log.error("Template exception", e);
        }
    }

    public class MessageMethod implements TemplateMethodModel {

        @Override
        public Object exec(List args) throws TemplateModelException {
            if (args.size() == 2)
                return AppBeans.get(Messages.class).getMessage((String) args.get(0), (String) args.get(1));
            else if (args.size() == 1) {
                return AppBeans.get(Messages.class).getMessage((Enum) args.get(0));
            } else
                throw new TemplateModelException("Wrong arguments");
        }
    }
}