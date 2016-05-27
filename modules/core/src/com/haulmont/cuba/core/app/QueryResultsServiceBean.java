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

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.app.queryresults.QueryResultsManagerAPI;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service(QueryResultsService.NAME)
public class QueryResultsServiceBean implements QueryResultsService {

    @Inject
    protected QueryResultsManagerAPI queryResultsManagerAPI;

    @Override
    public void insert(int queryKey, List<UUID> idList) {
        queryResultsManagerAPI.insert(queryKey, idList);
    }

    @Override
    public void delete(int queryKey) {
        queryResultsManagerAPI.delete(queryKey);
    }
}