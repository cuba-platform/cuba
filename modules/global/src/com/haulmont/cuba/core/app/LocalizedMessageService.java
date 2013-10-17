/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import java.util.Locale;

/**
 * Service allowing clients to retrieve localized messages from the middleware.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface LocalizedMessageService {

    String NAME = "cuba_LocalizedMessageService";

    String getMessage(String pack, String key, Locale locale);
}