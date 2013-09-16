/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.HierarchicalDatasource;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface Tree extends ListComponent {
    String NAME = "tree";

    void expandTree();
    void expand(Object itemId);

    void collapseTree();
    void collapse(Object itemId);

    boolean isExpanded(Object itemId);

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    String getHierarchyProperty();
    void setDatasource(HierarchicalDatasource datasource);
}