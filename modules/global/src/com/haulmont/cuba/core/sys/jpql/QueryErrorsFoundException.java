/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chevelev
 * @version $Id$
 */
public class QueryErrorsFoundException extends RuntimeException {

    private List<ErrorRec> errorRecs;

    public QueryErrorsFoundException() {
    }

    public QueryErrorsFoundException(String message, List<ErrorRec> errorRecs) {
        super(message);
        this.errorRecs = new ArrayList<>(errorRecs);
    }

    public List<ErrorRec> getErrorRecs() {
        return errorRecs;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        for (ErrorRec rec : errorRecs) {
            message += "\n" + rec;
        }
        return message;
    }
}
