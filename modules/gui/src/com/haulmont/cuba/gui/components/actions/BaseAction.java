/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.components.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Action that can change its enabled and visible properties depending on the user permissions and current context.
 * <p> The BaseAction is visible if the following conditions are met:
 * <ul>
 *     <li>setVisible(false) method was not called;</li>
 *     <li>there is no "hide" UI permission for this action.</li>
 * </ul>
 * <p>The action is enabled if the following conditions are met:
 * <ul>
 *     <li>setEnabled(false) method was not called;</li>
 *     <li>there are no "hide" and "read-only" UI permissions for this action;</li>
 *     <li>isPermitted() method returns true;</li>
 *     <li>isApplicable() method returns true;</li>
 *     <li>all {@link EnabledRule}s (if any) return true.</li>
 * </ul>
 * <p> Descendants may override {@link #isPermitted()} and {@link #isApplicable()} methods to define conditions in which
 * action will be enabled.
 *
 * Also, you can use fluent API to create instances of BaseAction and assign handlers to them:
 * <pre>{@code
 *     Action action = new BaseAction("printAll")
 *             .withCaption("Print all")
 *             .withIcon("icons/print.png")
 *             .withHandler(event -> {
 *                 // action logic here
 *              });
 *     docsTable.addAction(action);
 * }</pre>
 */
public class BaseAction extends AbstractAction implements Action.HasTarget, Action.UiPermissionAware {

    private boolean enabledByUiPermissions = true;
    private boolean visibleByUiPermissions = true;

    private boolean enabledExplicitly = true;
    private boolean visibleExplicitly = true;

    private List<EnabledRule> enabledRules; // lazy initialized list

    protected ListComponent target;

    protected Consumer<ActionPerformedEvent> actionPerformHandler;

    public BaseAction(String id) {
        this(id, null);
    }

    protected BaseAction(String id, @Nullable String shortcut) {
        super(id, shortcut);
    }

    /**
     * Callback method which is invoked by the action to determine its enabled state.
     *
     * @return true if the action is enabled for the current user
     */
    protected boolean isPermitted() {
        return true;
    }

    /**
     * Callback method which is invoked by the action to determine its enabled state.
     *
     * @return true if the action is enabled for the current context, e.g. there is a selected row in a table
     */
    protected boolean isApplicable() {
        return true;
    }

    protected boolean isEnabledByRule() {
        if (enabledRules == null) {
            return true;
        }

        for (EnabledRule rule : enabledRules) {
            if (!rule.isActionEnabled()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.visibleExplicitly != visible) {
            this.visibleExplicitly = visible;

            refreshState();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabledExplicitly != enabled) {
            this.enabledExplicitly = enabled;

            refreshState();
        }
    }

    protected void setVisibleInternal(boolean visible) {
        super.setVisible(visible);
    }

    protected void setEnabledInternal(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void refreshState() {
        super.refreshState();

        setVisibleInternal(visibleExplicitly && visibleByUiPermissions);

        setEnabledInternal(enabledExplicitly && enabledByUiPermissions && visibleByUiPermissions
                && isPermitted() && isApplicable() && isEnabledByRule());
    }

    @Override
    public ListComponent getTarget() {
        return target;
    }

    @Override
    public void setTarget(ListComponent target) {
        if (this.target != target) {
            this.target = target;

            refreshState();
        }
    }

    @Override
    public boolean isEnabledByUiPermissions() {
        return enabledByUiPermissions;
    }

    @Override
    public void setEnabledByUiPermissions(boolean enabledByUiPermissions) {
        if (this.enabledByUiPermissions != enabledByUiPermissions) {
            this.enabledByUiPermissions = enabledByUiPermissions;

            refreshState();
        }
    }

    @Override
    public boolean isVisibleByUiPermissions() {
        return visibleByUiPermissions;
    }

    @Override
    public void setVisibleByUiPermissions(boolean visibleByUiPermissions) {
        if (this.visibleByUiPermissions != visibleByUiPermissions) {
            this.visibleByUiPermissions = visibleByUiPermissions;

            refreshState();
        }
    }

    /**
     * Add new enabled rule for the action.
     *
     * @param enabledRule boolean rule for the action enabled state
     */
    public void addEnabledRule(EnabledRule enabledRule) {
        Preconditions.checkNotNullArgument(enabledRule);

        if (enabledRules == null) {
            enabledRules = new ArrayList<>();
        }
        if (!enabledRules.contains(enabledRule)) {
            enabledRules.add(enabledRule);
        }
    }

    /**
     * Remove enabled rule.
     *
     * @param enabledRule boolean rule for the action enabled state
     */
    public void removeEnabledRule(EnabledRule enabledRule) {
        if (enabledRules != null) {
            enabledRules.remove(enabledRule);
        }
    }

    /**
     * Callback interface which is invoked by the action to determine its enabled state.
     *
     * @see #addEnabledRule(EnabledRule)
     */
    public interface EnabledRule {
        boolean isActionEnabled();
    }

    @Override
    public void actionPerform(Component component) {
        if (actionPerformHandler != null) {
            actionPerformHandler.accept(new ActionPerformedEvent(this, component));
        }
    }

    /**
     * Set caption using fluent API method.
     *
     * @param caption caption
     * @return current instance of action
     */
    public BaseAction withCaption(String caption) {
        this.caption = caption;
        return this;
    }

    /**
     * Set description using fluent API method.
     *
     * @param description description
     * @return current instance of action
     */
    public BaseAction withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set icon using fluent API method.
     *
     * @param icon icon
     * @return current instance of action
     */
    public BaseAction withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Set shortcut using fluent API method.
     *
     * @param shortcut shortcut
     * @return current instance of action
     */
    public BaseAction withShortcut(String shortcut) {
        if (shortcut != null) {
            this.shortcut = KeyCombination.create(shortcut);
        }
        return this;
    }

    /**
     * Set actionPerformed handler using fluent API method. Can be used instead of subclassing BaseAction class.
     *
     * @param handler action performed handler
     * @return current instance of action
     */
    public BaseAction withHandler(Consumer<ActionPerformedEvent> handler) {
        this.actionPerformHandler = handler;
        return this;
    }

    /**
     * Set whether this action is primary using fluent API method. Can be used instead of subclassing BaseAction class.
     *
     * @param primary primary
     * @return current instance of action
     */
    public BaseAction withPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }
}