/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.screen;

import com.google.common.base.Strings;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.LookupComponent;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.LookupComponent.LookupSelectionChangeNotifier;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.util.OperationResult;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Base class for lookup screens.
 *
 * @param <T> type of entity
 */
public class StandardLookup<T extends Entity> extends Screen implements LookupScreen<T>, MultiSelectLookupScreen {
    protected Consumer<Collection<T>> selectHandler;
    protected Predicate<ValidationContext<T>> selectValidator;

    public StandardLookup() {
        addInitListener(this::initActions);
        addBeforeShowListener(this::beforeShow);
    }

    protected void initActions(@SuppressWarnings("unused") InitEvent event) {
        Window window = getWindow();

        Configuration configuration = getBeanLocator().get(Configuration.NAME);
        Messages messages = getBeanLocator().get(Messages.NAME);
        Icons icons = getBeanLocator().get(Icons.NAME);

        String commitShortcut = configuration.getConfig(ClientConfig.class).getCommitShortcut();

        Action commitAction = new BaseAction(LOOKUP_SELECT_ACTION_ID)
                .withCaption(messages.getMainMessage("actions.Select"))
                .withIcon(icons.get(CubaIcon.LOOKUP_OK))
                .withPrimary(true)
                .withShortcut(commitShortcut)
                .withHandler(this::select);

        window.addAction(commitAction);

        Action closeAction = new BaseAction(LOOKUP_CANCEL_ACTION_ID)
                .withCaption(messages.getMainMessage("actions.Cancel"))
                .withIcon(icons.get(CubaIcon.LOOKUP_CANCEL))
                .withHandler(this::cancel);

        window.addAction(closeAction);
    }

    private void beforeShow(@SuppressWarnings("unused") BeforeShowEvent beforeShowEvent) {
        setupLookupComponent();
        setupCommitShortcut();
    }

    protected void setupCommitShortcut() {
        if (selectHandler == null) {
            // window opened not as Lookup
            Action lookupAction = getWindow().getAction(LOOKUP_SELECT_ACTION_ID);
            if (lookupAction != null) {
                lookupAction.setShortcut(null);
            }
        }
    }

    protected void setupLookupComponent() {
        if (this.selectHandler != null) {
            getLookupComponent().setLookupSelectHandler(this::select);
        }
    }

    @Override
    public Consumer<Collection<T>> getSelectHandler() {
        return selectHandler;
    }

    @Override
    public void setSelectHandler(Consumer<Collection<T>> selectHandler) {
        this.selectHandler = selectHandler;

        Component lookupActionsLayout = getLookupActionsLayout();
        if (lookupActionsLayout != null) {
            lookupActionsLayout.setVisible(true);

            Component lookupComponent = getLookupComponent();
            if (lookupComponent instanceof LookupSelectionChangeNotifier) {
                LookupSelectionChangeNotifier selectionNotifier = (LookupSelectionChangeNotifier) lookupComponent;

                Action commitAction = getWindow().getAction(LOOKUP_SELECT_ACTION_ID);
                if (commitAction != null) {
                    //noinspection unchecked
                    selectionNotifier.addLookupValueChangeListener(valueChangeEvent ->
                            commitAction.setEnabled(!selectionNotifier.getLookupSelectedItems().isEmpty()));

                    commitAction.setEnabled(!selectionNotifier.getLookupSelectedItems().isEmpty());
                }
            }
        }
    }

    @Override
    public Predicate<ValidationContext<T>> getSelectValidator() {
        return selectValidator;
    }

    @Override
    public void setSelectValidator(Predicate<ValidationContext<T>> selectValidator) {
        this.selectValidator = selectValidator;
    }

    @Nullable
    protected Component getLookupActionsLayout() {
        return getWindow().getComponent("lookupActions");
    }

    @SuppressWarnings("unchecked")
    protected LookupComponent<T> getLookupComponent() {
        com.haulmont.cuba.gui.screen.LookupComponent annotation =
                getClass().getAnnotation(com.haulmont.cuba.gui.screen.LookupComponent.class);
        if (annotation == null || Strings.isNullOrEmpty(annotation.value())) {
            throw new IllegalStateException(
                    String.format("StandardLookup %s does not declare @LookupComponent", getClass())
            );
        }
        return (LookupComponent) getWindow().getComponentNN(annotation.value());
    }

    protected void select(@SuppressWarnings("unused") Action.ActionPerformedEvent event) {
        if (selectHandler == null) {
            // window opened not as Lookup
            return;
        }

        LookupComponent<T> lookupComponent = getLookupComponent();
        Collection<T> lookupSelectedItems = lookupComponent.getLookupSelectedItems();
        select(lookupSelectedItems);
    }

    protected void cancel(@SuppressWarnings("unused") Action.ActionPerformedEvent event) {
        close(WINDOW_DISCARD_AND_CLOSE_ACTION);
    }

    protected void select(Collection<T> items) {
        boolean valid = true;
        if (selectValidator != null) {
            valid = selectValidator.test(new ValidationContext<>(this, items));
        }

        if (valid) {
            OperationResult result = close(LOOKUP_SELECT_CLOSE_ACTION);
            if (selectHandler != null) {
                result.then(() -> selectHandler.accept(items));
            }
        }
    }

    @Override
    public void setLookupComponentMultiSelect(boolean multiSelect) {
        LookupComponent<T> lookupComponent = getLookupComponent();
        if (lookupComponent instanceof Table) {
            ((Table<T>) lookupComponent).setMultiSelect(multiSelect);
        } else if (lookupComponent instanceof DataGrid) {
            ((DataGrid<T>) lookupComponent).setSelectionMode(multiSelect
                    ? DataGrid.SelectionMode.MULTI
                    : DataGrid.SelectionMode.SINGLE);
        } else if (lookupComponent instanceof Tree) {
            ((Tree<T>) lookupComponent).setSelectionMode(multiSelect
                    ? Tree.SelectionMode.MULTI
                    : Tree.SelectionMode.SINGLE);
        }
    }
}