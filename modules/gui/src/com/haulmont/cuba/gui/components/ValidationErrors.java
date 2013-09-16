/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates errors found during components validation.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ValidationErrors {

    public static class Item {
        public final Component component;
        public final String description;

        public Item(Component component, String description) {
            this.component = component;
            this.description = description;
        }
    }

    private List<Item> items = new ArrayList<>();

    /**
     * Add an error without reference to component causing it.
     * @param description   error description
     */
    public void add(String description) {
        add(null, description);
    }

    /**
     * Add an error.
     * @param component     component causing the error
     * @param description   error description
     */
    public void add(@Nullable Component component, String description) {
        items.add(new Item(component, description));
    }

    /**
     * @return errors list
     */
    public List<Item> getAll() {
        return items;
    }

    /**
     * @return  true if there are no errors
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
