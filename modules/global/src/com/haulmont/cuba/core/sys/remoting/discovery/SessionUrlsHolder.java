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

package com.haulmont.cuba.core.sys.remoting.discovery;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Interface to be implemented by objects storing the list of server URLs for the current session.
 */
public interface SessionUrlsHolder {

    @Nullable
    List<String> getUrls(String selectorId);

    void setUrls(String selectorId, List<String> urls);
}
