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

import com.haulmont.bali.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * System-level class for environment sanity checks.
 */
public class EnvironmentChecksRunner {

    private static final Logger log = LoggerFactory.getLogger(EnvironmentChecksRunner.class);
    protected List<EnvironmentCheck> checks;
    protected String moduleName;

    public EnvironmentChecksRunner(String moduleName) {
        this.moduleName = moduleName;
    }

    public EnvironmentChecksRunner(String moduleName, List<EnvironmentCheck> checks) {
        this.moduleName = moduleName;
        this.checks = checks;
    }

    public List<EnvironmentCheck> getChecks() {
        return checks;
    }

    public void setChecks(List<EnvironmentCheck> checks) {
        this.checks = checks;
    }

    public void addCheck(EnvironmentCheck check) {
        if (checks == null)
            checks = new ArrayList<>();
        checks.add(check);
    }

    /**
     * Run all environment sanity checks.
     *
     * @return list of failed checks results, empty list if all checks completed successfully
     */
    public List<CheckFailedResult> runChecks() {
        if (checks == null)
            return Collections.emptyList();
        List<CheckFailedResult> results = new ArrayList<>();
        for (EnvironmentCheck check : checks) {
            results.addAll(check.doCheck());
        }
        if (!results.isEmpty()) {
            StringBuilder resultMessage = new StringBuilder();
            resultMessage.append("Some environment checks failed on ");
            resultMessage.append(moduleName);
            resultMessage.append(" module:");
            for (CheckFailedResult result : results) {
                resultMessage.append("\n");
                resultMessage.append(result.getMessage());
                if (result.getException() != null) {
                    resultMessage.append("\n\t");
                    resultMessage.append(result.getException());
                }
            }
            log.warn(StringHelper.wrapLogMessage(resultMessage.toString()));
        } else {
            log.info(String.format("Environment checks on %s module completed successfully", moduleName));
        }
        return results;
    }
}
