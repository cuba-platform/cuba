/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.web.widgets.client.tableshared;

public interface GroupAggregationInputListener {
    void onInputChange(String columnKey, String groupKey, String value, boolean isFocused);
}
