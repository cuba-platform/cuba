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

package org.eclipse.persistence.tools.weaving.jpa;

import com.haulmont.cuba.core.sys.persistence.EclipseLinkCustomizer;
import org.eclipse.persistence.exceptions.StaticWeaveException;

/**
 */
public class CubaStaticWeave extends StaticWeave {

    public CubaStaticWeave(String[] argv) {
        super(argv);
    }

    public static void main(String[] argv) {
        EclipseLinkCustomizer.initTransientCompatibleAnnotations();

        StaticWeave staticweaver = new StaticWeave(argv);
        try {
            staticweaver.processCommandLine();
            staticweaver.start();
        } catch (Exception e) {
            throw StaticWeaveException.exceptionPerformWeaving(e, argv);
        }
    }
}
