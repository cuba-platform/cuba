/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Alexander Chevelev
 * Date: 01.04.2011
 * Time: 18:36:09
 */
public class ErrorsFoundException extends RuntimeException{
    private List<ErrorRec> errorRecs;

    public ErrorsFoundException() {
    }

    public ErrorsFoundException(String message, List<ErrorRec> errorRecs) {
        super(message);
        this.errorRecs = new ArrayList<ErrorRec>(errorRecs);
    }

    public List<ErrorRec> getErrorRecs() {
        return errorRecs;
    }

    @Override
    public String toString() {
        String result = "";
        for (ErrorRec rec : errorRecs) {
            result += rec + "\n"; 
        }
        return result;
    }
}
