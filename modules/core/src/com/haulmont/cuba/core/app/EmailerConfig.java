/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;

/**
 * Configuration parameters interface used for sending emails.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Prefix("cuba.email.")
@Source(type = SourceType.DATABASE)
public interface EmailerConfig extends Config
{
    /**
     * @return Default "from" address
     */
    @Default("DoNotReply@haulmont.com")
    String getFromAddress();
    void setFromAddress(String fromAddress);

    /**
     * @return SMTP host name
     */
    @Default("test.host")
    String getSmtpHost();

    /**
     * @return
     */
    @Default("2")
    int getDelayCallCount();

    /**
     * @return
     */
    @Default("100")
    int getMessageQueueCapacity();
}
