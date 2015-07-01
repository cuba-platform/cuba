/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package org.eclipse.persistence.tools.weaving.jpa;

import com.haulmont.cuba.core.sys.persistence.EclipseLinkCustomizer;
import org.eclipse.persistence.exceptions.StaticWeaveException;

/**
 * @author krivopustov
 * @version $Id$
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
