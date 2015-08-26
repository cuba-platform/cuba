/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.springframework.view;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
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

/**
 * @author artamonov
 * @version $Id$
 */
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
        if (AppContext.getSecurityContext() != null)
            context.put("userSession", AppContext.getSecurityContext().getSession());
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
