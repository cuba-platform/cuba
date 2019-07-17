/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.core.sys.serialization;

import com.haulmont.cuba.core.sys.events.AppContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SerializationShutdownListener {

    @EventListener(AppContextStoppedEvent.class)
    public void appContextStopped() {
        SerializationSupport.getKryoSerialization().shutdown();
    }
    
}
