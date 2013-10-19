/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.event.dom.client.ClickHandler;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Pager
        extends Composite
        implements ClickHandler
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
            for (Object o : this) {
                ((PageChangeListener) o).onFirstPage();
            }
        }

        void firePrevPage() {
            for (Object o : this) {
                ((PageChangeListener) o).onPrevPage();
            }
        }

        void fireNextPage() {
            for (Object o : this) {
                ((PageChangeListener) o).onNextPage();
            }
        }

        void fireLastPage() {
            for (Object o : this) {
                ((PageChangeListener) o).onLastPage();
            }
        }

        void firePage(int page) {
            for (Object o : this) {
                ((PageChangeListener) o).onPage(page);
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
