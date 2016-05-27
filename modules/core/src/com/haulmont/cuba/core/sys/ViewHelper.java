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
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;

import java.util.Collection;

public final class ViewHelper {

    private ViewHelper() {
    }

    public static View intersectViews(View first, View second) {
        if (first == null)
            throw new IllegalArgumentException("View is null");
        if (second == null)
            throw new IllegalArgumentException("View is null");

        View resultView = new View(first.getEntityClass());

        Collection<ViewProperty> firstProps = first.getProperties();

        for (ViewProperty firstProperty : firstProps) {
            if (second.containsProperty(firstProperty.getName())) {
                View resultPropView = null;
                ViewProperty secondProperty = second.getProperty(firstProperty.getName());
                if ((firstProperty.getView() != null) && (secondProperty.getView() != null)) {
                    resultPropView = intersectViews(firstProperty.getView(), secondProperty.getView());
                }
                resultView.addProperty(firstProperty.getName(), resultPropView);
            }
        }

        return resultView;
    }
}