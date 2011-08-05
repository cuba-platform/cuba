/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Service(TestingService.NAME)
public class TestingServiceBean implements TestingService {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public String executeFor(int timeMillis) {
        log.debug("executeFor " + timeMillis  + " started");
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.debug("executeFor " + timeMillis + " finished");
        return "Done";
    }
}
