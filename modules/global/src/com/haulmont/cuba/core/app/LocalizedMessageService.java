/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */

public interface LocalizedMessageService {

    String NAME = "cuba_LocalizedMessageService";

    String getMessage(String pack, String key, Locale locale);

}
