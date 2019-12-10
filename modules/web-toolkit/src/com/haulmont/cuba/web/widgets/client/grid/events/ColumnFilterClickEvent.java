/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.widgets.client.grid.events;

import com.google.gwt.event.shared.GwtEvent;
import com.vaadin.client.widget.grid.EventCellReference;
import com.vaadin.client.widgets.Grid;

// TODO: gg, JavaDoc
public class ColumnFilterClickEvent<T> extends GwtEvent<ColumnFilterClickHandler<?>> {

    private static final Type<ColumnFilterClickHandler<?>> TYPE = new Type<>();

    protected final Grid<T> grid;
    protected EventCellReference<T> cell;
    protected int clientX;
    protected int clientY;
    protected final boolean userOriginated;

    public ColumnFilterClickEvent(Grid<T> grid, EventCellReference<T> cell,
                                  int clintX, int clintY, boolean userOriginated) {
        this.grid = grid;
        this.cell = cell;
        this.clientX = clintX;
        this.clientY = clintY;
        this.userOriginated = userOriginated;
    }

    @Override
    public Grid<T> getSource() {
        return grid;
    }

    public EventCellReference<T> getCell() {
        return cell;
    }

    public int getClientX() {
        return clientX;
    }

    public int getClientY() {
        return clientY;
    }

    public boolean isUserOriginated() {
        return userOriginated;
    }

    @Override
    public Type<ColumnFilterClickHandler<?>> getAssociatedType() {
        return TYPE;
    }

    public static Type<ColumnFilterClickHandler<?>> getType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void dispatch(ColumnFilterClickHandler<?> handler) {
        ((ColumnFilterClickHandler<T>) handler).click(this);
    }
}
