/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import java.util.UUID;

/**
 * Global interface to create UUIDs.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface UuidSource {

    String NAME = "cuba_UuidSource";

    UUID createUuid();
}
