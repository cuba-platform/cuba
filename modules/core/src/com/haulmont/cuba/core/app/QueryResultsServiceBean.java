/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.app.queryresults.QueryResultsManagerAPI;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * @author gorbunkov
 * @version $Id$
 */
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
