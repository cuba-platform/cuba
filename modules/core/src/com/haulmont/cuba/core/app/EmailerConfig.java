/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.05.2009 12:27:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;

/**
 * Parameters
 */
@Prefix("cuba.email.")
@Source(type = SourceType.DATABASE)
public interface EmailerConfig extends Config
{
    @Default("DoNotReply@haulmont.com")
    String getFromAddress();
    void setFromAddress(String fromAddress);

    @Default("test.host")
    String getSmtpHost();

    @Default("2")
    int getDelayCallCount();

    @Default("100")
    int getMessageQueueCapacity();
}
