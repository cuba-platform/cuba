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

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import org.apache.commons.lang.StringUtils;

public class ViewRepositoryInfo {
    protected final AbstractViewRepository viewRepository;
    protected final Metadata metadata;

    public ViewRepositoryInfo(Metadata metadata) {
        this.metadata = metadata;
        this.viewRepository = (AbstractViewRepository) metadata.getViewRepository();
    }

    protected void dump(ViewProperty viewProperty, StringBuilder content, String indent) {
        content.append("\n");
        content.append(indent);
        content.append("- ").append(viewProperty.getName());
        View innerView = viewProperty.getView();
        if (innerView != null) {
            if (StringUtils.isNotEmpty(innerView.getName())) {
                content.append(" -> ")
                        .append(metadata.getSession().getClass(innerView.getEntityClass()).getName())
                        .append("/")
                        .append(innerView.getName());
            } else {
                for (ViewProperty innerProperty : innerView.getProperties()) {
                    dump(innerProperty, content, "  " + indent);
                }
            }
        }
    }

    protected void dump(View view, StringBuilder content) {
        content.append("\n");
        content.append("+ ")
                .append(metadata.getSession().getClass(view.getEntityClass()).getName())
                .append("/")
                .append(view.getName());

        for (ViewProperty viewProperty : view.getProperties()) {
            dump(viewProperty, content, "  ");
        }

        content.append("\n");
    }

    public String dump() {
        StringBuilder content = new StringBuilder();

        for (View view : viewRepository.getAll()) {
            dump(view, content);
        }

        return content.toString();
    }

    protected void dumpHtml(ViewProperty viewProperty, StringBuilder content, String indent) {
        content.append("<br/>").append("\n");
        content.append(indent);
        content.append("- ").append(viewProperty.getName());
        View innerView = viewProperty.getView();
        if (innerView != null) {
            if (StringUtils.isNotEmpty(innerView.getName())) {
                String metaClassName = metadata.getSession().getClass(innerView.getEntityClass()).getName();
                content.append(" -> <a href=\"#").append(metaClassName).append("__").append(innerView.getName()).append("\">")
                        .append(metaClassName)
                        .append("/")
                        .append(innerView.getName())
                        .append("</a>");
            } else {
                for (ViewProperty innerProperty : innerView.getProperties()) {
                    dumpHtml(innerProperty, content, "&nbsp;&nbsp;&nbsp;&nbsp;" + indent);
                }
            }
        }
    }

    protected void dumpHtml(View view, StringBuilder content) {
        content.append("<br/>").append("\n");
        String metaClassName = metadata.getSession().getClass(view.getEntityClass()).getName();
        String viewHtmlId = metaClassName + "__" + view.getName();

        content.append("+ <a href=\"#").append(viewHtmlId).append("\"><span id=\"").append(viewHtmlId).append("\">")
                .append(metaClassName).append("/").append(view.getName())
                .append("</span></a>");

        for (ViewProperty viewProperty : view.getProperties()) {
            dumpHtml(viewProperty, content, "&nbsp;&nbsp;&nbsp;&nbsp;");
        }

        content.append("<br/>").append("\n");
    }

    public String dumpHtml() {
        StringBuilder content = new StringBuilder("<html><head><title>Views</title></head><body>");

        for (View view : viewRepository.getAll()) {
            dumpHtml(view, content);
        }

        content.append("</body></html>");

        return content.toString();
    }
}