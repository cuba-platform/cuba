/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import java.util.List;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public interface RelatedEntitiesService {

    String NAME = "cuba_RelatedEntitiesService";

    List<UUID> getRelatedIds(List<UUID> parents, String parentMetaClass, String relationProperty);
}