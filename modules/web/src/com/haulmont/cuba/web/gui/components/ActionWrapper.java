package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Action;

/**
 * Created by IntelliJ IDEA.
* User: shamrock
* Date: 20.02.2009
* Time: 23:11:14
* To change this template use File | Settings | File Templates.
*/
class ActionWrapper extends com.vaadin.event.Action {
    private final Action action;

    public ActionWrapper(Action action) {
        super(action.getCaption());
        this.action = action;
    }

    @Override
    public String getCaption() {
        return action.getCaption();
    }
}
