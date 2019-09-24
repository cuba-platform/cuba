/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

public class JdkVersionTest {
    @Test
    public void test() {
        if (SystemUtils.IS_JAVA_1_8) {
            System.out.println(
                    "---------------tests are running on JAVA 8---------------"
            );
        } else if (SystemUtils.IS_JAVA_10) {
            System.out.println(
                    "---------------tests are running on JAVA 10---------------"
            );
        }
    }
}
