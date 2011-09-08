/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface UuidSource {

    String NAME = "cuba_UuidSource";

    UUID createUuid();
}
