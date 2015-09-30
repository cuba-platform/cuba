/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.entity.Server;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_TestDetachAttachListener")
public class TestDetachAttachListener implements
        BeforeDetachEntityListener<Server>, BeforeAttachEntityListener<Server> {

    public final List<String> events = new ArrayList<>();

    @Override
    public void onBeforeAttach(Server entity) {
        events.add("onBeforeAttach: " + entity.getId());
    }

    @Override
    public void onBeforeDetach(Server entity, EntityManager entityManager) {
        events.add("onBeforeDetach: " + entity.getId());
    }
}
