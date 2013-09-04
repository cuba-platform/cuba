/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
