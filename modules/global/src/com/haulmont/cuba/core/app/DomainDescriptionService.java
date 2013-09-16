/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

/**
 * Service interface providing domain description.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface DomainDescriptionService {

    String NAME = "cuba_DomainDescriptionService";

    /**
     * @return domain description in HTML format
     */
    String getDomainDescription();
}
