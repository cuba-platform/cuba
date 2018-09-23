/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.web.screens.injection;

import com.haulmont.cuba.gui.events.UiEvent;
import org.springframework.context.ApplicationEvent;

public class TestGlobalEvent extends ApplicationEvent implements UiEvent {
    public TestGlobalEvent(Object source) {
        super(source);
    }
}