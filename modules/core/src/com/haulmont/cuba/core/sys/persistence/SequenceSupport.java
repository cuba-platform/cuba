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

/*
 * Author: Konstantin Krivopustov
 * Created: 15.05.2009 22:25:05
 * 
 */
package com.haulmont.cuba.core.sys.persistence;

/**
 * Interface defining methods for managing database sequences.
 *
 */
public interface SequenceSupport {

    String SQL_DELIMITER = "^";

    String sequenceExistsSql(String sequenceName);

    String createSequenceSql(String sequenceName, long startValue, long increment);

    String modifySequenceSql(String sequenceName, long startWith);

    String deleteSequenceSql(String sequenceName);

    String getNextValueSql(String sequenceName);

    String getCurrentValueSql(String sequenceName);
}
