/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 18.12.2008 18:39:11
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Pager
        extends Composite
        implements ClickListener
{
    protected PageChangeListenersCollection pageChangeListeners;

    public void addPageChangeListener(PageChangeListener listener) {
        if (pageChangeListeners == null) {
            pageChangeListeners = new PageChangeListenersCollection();
        }
        pageChangeListeners.add(listener);
    }

    public void removePageChangeListener(PageChangeListener listener) {
        if (pageChangeListeners != null) {
            pageChangeListeners.remove(listener);
        }
    }

    class PageChangeListenersCollection extends ArrayList {
        void fireFirstPage() {
            for (final Iterator it = iterator(); it.hasNext();) {
                ((PageChangeListener) it.next()).onFirstPage();
            }
        }

        void firePrevPage() {
            for (final Iterator it = iterator(); it.hasNext();) {
                ((PageChangeListener) it.next()).onPrevPage();
            }
        }

        void fireNextPage() {
            for (final Iterator it = iterator(); it.hasNext();) {
                ((PageChangeListener) it.next()).onNextPage();
            }
        }

        void fireLastPage() {
            for (final Iterator it = iterator(); it.hasNext();) {
                ((PageChangeListener) it.next()).onLastPage();
            }
        }

        void firePage(int page) {
            for (final Iterator it = iterator(); it.hasNext();) {
                ((PageChangeListener) it.next()).onPage(page);
            }
        }
    }

    public interface PageChangeListener {
        void onFirstPage();

        void onPrevPage();

        void onNextPage();

        void onLastPage();

        void onPage(int page);
    }

}
