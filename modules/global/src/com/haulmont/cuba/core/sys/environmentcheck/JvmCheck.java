/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.core.sys.environmentcheck;

import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JvmCheck implements EnvironmentCheck {

    @Override
    public List<CheckFailedResult> doCheck() {
        List<CheckFailedResult> result = new ArrayList<>();
        String javaVersion = SystemUtils.JAVA_VERSION;
        if (!SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_8) || !SystemUtils.isJavaVersionAtMost(JavaVersion.JAVA_11)) {
            result.add(new CheckFailedResult(
                    String.format("Unsupported Java version detected: %s; Cuba supports Java 8, 9 or 11", javaVersion),
                    null));
        }

        Properties prop = System.getProperties();
        String vendor = prop.getProperty("java.vendor");
        // Since OpenJ9 JVM is unsupported
        if (vendor != null && vendor.contains("OpenJ9")) {
            result.add(new CheckFailedResult(
                    String.format("Possibly unsupported JVM from vendor: %s", vendor), null));
        }
        return result;
    }
}
