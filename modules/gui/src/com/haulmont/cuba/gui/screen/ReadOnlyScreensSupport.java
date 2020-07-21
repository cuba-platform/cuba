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

package com.haulmont.cuba.gui.screen;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ActionsHolder;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Component.Editable;
import com.haulmont.cuba.gui.components.data.HasValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;

import javax.inject.Inject;
import java.util.Collection;
import java.util.function.Predicate;

import static com.haulmont.cuba.gui.screen.EditorScreen.ENABLE_EDITING;
import static com.haulmont.cuba.gui.screen.EditorScreen.WINDOW_CLOSE;

/**
 * Bean that encapsulates the default logic of changing screens read-only mode.
 */
@org.springframework.stereotype.Component(ReadOnlyScreensSupport.NAME)
public class ReadOnlyScreensSupport {
    public static final String NAME = "cuba_ReadOnlyScreensSupport";

    protected Security security;

    @Inject
    public void setSecurity(Security security) {
        this.security = security;
    }

    /**
     * Changes the read-only mode of the given screen.
     * <p>
     * The following components and actions change their state:
     * <ul>
     *     <li>All {@link Editable} components that has a not null {@link ValueSource}</li>
     *     <li>All {@link Action.DisabledWhenScreenReadOnly} actions obtained from {@link ActionsHolder} components</li>
     *     <li>All own screen actions except {@link EditorScreen#WINDOW_CLOSE}
     *     and {@link EditorScreen#ENABLE_EDITING}</li>
     * </ul>
     * <p>
     *
     * @param screen   a screen to set the read-only mode
     * @param readOnly whether a screen in the read-only mode
     */
    public void setScreenReadOnly(Screen screen, boolean readOnly) {
        setScreenReadOnly(screen, readOnly, true);
    }

    /**
     * Changes the read-only mode of the given screen.
     * <p>
     * The following components and actions change their state:
     * <ul>
     *     <li>All {@link Editable} components that has a not null {@link ValueSource}</li>
     *     <li>All {@link Action.DisabledWhenScreenReadOnly} actions obtained from {@link ActionsHolder} components</li>
     *     <li>All own screen actions except {@link EditorScreen#WINDOW_CLOSE}
     *     and {@link EditorScreen#ENABLE_EDITING}</li>
     * </ul>
     * <p>
     *
     * @param screen               a screen to set the read-only mode
     * @param readOnly             whether a screen in the read-only mode
     * @param showEnableEditingBtn whether or not the {@link EditorScreen#ENABLE_EDITING}
     *                             should be displayed in the read-only mode
     */
    public void setScreenReadOnly(Screen screen, boolean readOnly, boolean showEnableEditingBtn) {
        updateComponentsEditableState(screen, readOnly);
        updateOwnActionsEnableState(screen, readOnly);

        Action action = screen.getWindow().getAction(ENABLE_EDITING);
        if (action != null) {
            action.setVisible(showEnableEditingBtn && readOnly);
        }
    }

    protected void updateComponentsEditableState(Screen screen, boolean readOnly) {
        ComponentsHelper.walkComponents(screen.getWindow(), (component, name) -> {
            if (component instanceof Editable
                    && isChangeEditable(component)) {
                boolean editable = isEditableConsideringDataBinding(component, !readOnly);
                ((Editable) component).setEditable(editable);
            }

            if (component instanceof ActionsHolder) {
                updateActionsEnableState(((ActionsHolder) component).getActions(), readOnly,
                        this::isChangeComponentActionEnabled);
            }
        });
    }

    protected boolean isEditableConsideringDataBinding(Component component, boolean editable) {
        boolean shouldBeEditable = true;

        if (component instanceof HasValueSource
                && ((HasValueSource) component).getValueSource() != null) {
            ValueSource valueSource = ((HasValueSource) component).getValueSource();
            shouldBeEditable = !valueSource.isReadOnly();

            if (valueSource instanceof EntityValueSource
                    && ((EntityValueSource) valueSource).isDataModelSecurityEnabled()) {
                MetaPropertyPath metaPropertyPath = ((EntityValueSource) valueSource).getMetaPropertyPath();

                if (!security.isEntityAttrUpdatePermitted(metaPropertyPath)
                        || !security.isEntityAttrReadPermitted(metaPropertyPath)) {
                    shouldBeEditable = false;
                }
            }
        }

        return editable && shouldBeEditable;
    }

    protected boolean isChangeComponentActionEnabled(Action action) {
        if (action instanceof Action.AdjustWhenScreenReadOnly) {
            return ((Action.AdjustWhenScreenReadOnly) action).isDisabledWhenScreenReadOnly();
        }

        return action instanceof Action.DisabledWhenScreenReadOnly;
    }

    protected boolean isChangeOwnActionEnabled(Action action) {
        return !WINDOW_CLOSE.equals(action.getId())
                && !ENABLE_EDITING.equals(action.getId());
    }

    protected boolean isChangeEditable(Component component) {
        return component instanceof HasValueSource
                && ((HasValueSource) component).getValueSource() != null;
    }

    protected void updateOwnActionsEnableState(Screen screen, boolean readOnly) {
        updateActionsEnableState(screen.getWindow().getActions(), readOnly, this::isChangeOwnActionEnabled);
    }

    protected void updateActionsEnableState(Collection<Action> actions, boolean readOnly,
                                            Predicate<Action> shouldChangeEnabled) {
        actions.stream()
                .filter(shouldChangeEnabled)
                .forEach(action ->
                        action.setEnabled(!readOnly));
    }
}
