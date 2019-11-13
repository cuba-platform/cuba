package com.haulmont.cuba.web.widgets.client.treegrid;

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.treegrid.TreeGridState;

import java.util.Map;

public class CubaTreeGridState extends TreeGridState {
    public Map<String, String> columnIds = null;

    @NoLayout
    public boolean showEmptyState;

    @NoLayout
    public String emptyStateMessage;

    @NoLayout
    public String emptyStateLinkMessage;

    @NoLayout
    public String selectAllLabel;

    @NoLayout
    public String deselectAllLabel;
}
