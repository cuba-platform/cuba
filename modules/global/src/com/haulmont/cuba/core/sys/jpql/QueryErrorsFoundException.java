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

package com.haulmont.cuba.core.sys.jpql;

import java.util.ArrayList;
import java.util.List;

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