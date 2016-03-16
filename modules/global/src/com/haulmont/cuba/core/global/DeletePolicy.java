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
package com.haulmont.cuba.core.global;

/**
 * This enum defines a behaviour to deal with linked objects in case of soft delete<br>
 * <ul>
 * <li>DENY - throw {@link DeletePolicyException} when linked object exists
 * <li>CASCADE - soft delete the linked object
 * <li>UNLINK - remove link
 * </ul>
 *
 */
public enum DeletePolicy {
    DENY,
    CASCADE,
    UNLINK
}