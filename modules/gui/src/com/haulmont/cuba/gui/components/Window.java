/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:11:57
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.DsContext;

import java.util.Collection;

public interface Window extends IFrame, Component.HasCaption {
    DsContext getDsContext();
    void setDsContext(DsContext dsContext);

    boolean close();

    interface Editor extends Window {
        Object getItem();
        void setItem(Object item);

        void commit();
    }

    interface Lookup extends Window {
        Component getLookupComponent();
        void setLookupComponent(Component lookupComponent);

        interface Handler {
            void handleLookup(Collection items);
        }

        Handler getLookupHandler();
        void setLookupHandler(Handler handler);
    }
}
