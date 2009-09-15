/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 11.08.2009 11:00:43
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.utils;

import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.KeyMapper;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

import java.util.List;
import java.util.LinkedList;

public abstract class ActionsHandler implements Action.Container {

    protected List<Action.Handler> actionHandlers = null;

    protected KeyMapper actionMapper = null;

    public void paintActions(PaintTarget target) throws PaintException {
        if (actionHandlers != null && !actionHandlers.isEmpty()) {
            target.startTag("actions");

            for (Object actionHandler : actionHandlers) {
                final Action[] aa = ((Action.Handler) actionHandler).getActions(
                        null, this);
                if (aa != null) {
                    for (final Action a : aa) {
                        target.startTag("action");
                        final String akey = actionMapper.key(a);
                        target.addAttribute("key", akey);
                        if (a.getCaption() != null) {
                            target.addAttribute("caption", a.getCaption());
                        }
                        if (a.getIcon() != null) {
                            target.addAttribute("icon", a.getIcon());
                        }
                        if (a instanceof ShortcutAction) {
                            final ShortcutAction sa = (ShortcutAction) a;
                            target.addAttribute("kc", sa.getKeyCode());
                            final int[] modifiers = sa.getModifiers();
                            if (modifiers != null) {
                                final String[] smodifiers = new String[modifiers.length];
                                for (int i = 0; i < modifiers.length; i++) {
                                    smodifiers[i] = String
                                            .valueOf(modifiers[i]);
                                }
                                target.addAttribute("mk", smodifiers);
                            }
                        }
                        target.endTag("action");
                    }
                }
            }
            target.endTag("actions");
        }
    }

    public void addActionHandler(Action.Handler actionHandler) {
        if (actionHandler != null) {
            if (actionHandlers == null) {
                actionHandlers = new LinkedList<Action.Handler>();
                actionMapper = new KeyMapper();
            }

            if (!actionHandlers.contains(actionHandler)) {
                actionHandlers.add(actionHandler);
            }
        }
    }

    public void removeActionHandler(Action.Handler actionHandler) {
        if (actionHandlers != null && actionHandlers.contains(actionHandler)) {

            actionHandlers.remove(actionHandler);

            if (actionHandlers.isEmpty()) {
                actionHandlers = null;
                actionMapper = null;
            }
        }
    }
}
