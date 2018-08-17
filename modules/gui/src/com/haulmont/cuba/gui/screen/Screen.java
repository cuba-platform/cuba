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

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs.MessageType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.screen.events.*;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.gui.util.UnknownOperationResult;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.walkComponents;

/**
 * Base class for all screen controllers.
 */
public abstract class Screen implements FrameOwner {

    private String id;

    private ScreenContext screenContext;

    private Window window;

    private Settings settings;

    private EventHub eventHub = new EventHub();

    private BeanLocator beanLocator;

    @Inject
    protected void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    protected EventHub getEventHub() {
        return eventHub;
    }

    public String getId() {
        return id;
    }

    /**
     * JavaDoc
     *
     * @param id
     */
    protected void setId(String id) {
        this.id = id;
    }

    protected void setScreenContext(ScreenContext screenContext) {
        this.screenContext = screenContext;
    }

    protected ScreenContext getScreenContext() {
        return screenContext;
    }

    protected <E> void fireEvent(Class<E> eventType, E event) {
        eventHub.publish(eventType, event);
    }

    public Window getWindow() {
        return window;
    }

    protected void setWindow(Window window) {
        checkNotNullArgument(window);

        if (this.window != null) {
            throw new IllegalStateException("Screen already has Window");
        }
        this.window = window;
    }

    /**
     * JavaDoc
     *
     * @param listener
     * @return
     */
    protected Subscription addInitListener(Consumer<InitEvent> listener) {
        return eventHub.subscribe(InitEvent.class, listener);
    }

    /**
     * JavaDoc
     *
     * @param listener
     * @return
     */
    protected Subscription addAfterInitListener(Consumer<AfterInitEvent> listener) {
        return eventHub.subscribe(AfterInitEvent.class, listener);
    }

    /**
     * JavaDoc
     *
     * @param listener
     * @return
     */
    protected Subscription addBeforeCloseListener(Consumer<BeforeCloseEvent> listener) {
        return eventHub.subscribe(BeforeCloseEvent.class, listener);
    }

    /**
     * JavaDoc
     *
     * @param listener listener
     * @return
     */
    public Subscription addAfterCloseListener(Consumer<AfterCloseEvent> listener) {
        return eventHub.subscribe(AfterCloseEvent.class, listener);
    }

    protected OperationResult showUnsavedChangesDialog() {
        UnknownOperationResult result = new UnknownOperationResult();
        Messages messages = beanLocator.get(Messages.NAME);

        screenContext.getDialogs().createOptionDialog()
                .setCaption(messages.getMainMessage("closeUnsaved.caption"))
                .setMessage(messages.getMainMessage("saveUnsaved"))
                .setType(MessageType.WARNING)
                .setActions(
                        new DialogAction(DialogAction.Type.YES)
                                .withHandler(e -> {
                                    closeWithDiscard()
                                            .then(result::success)
                                            .otherwise(result::fail);
                                }),
                        new DialogAction(DialogAction.Type.NO, Action.Status.PRIMARY)
                                .withHandler(e -> {
                                    // todo try to move focus back
                                    // findAndFocusChildComponent();
                                })
                )
                .show();

        return result;
    }

    protected OperationResult showSaveConfirmationDialog() {
        UnknownOperationResult result = new UnknownOperationResult();
        Messages messages = beanLocator.get(Messages.NAME);

        Icons icons = beanLocator.get(Icons.NAME);

        screenContext.getDialogs().createOptionDialog()
                .setCaption(messages.getMainMessage("closeUnsaved.caption"))
                .setMessage(messages.getMainMessage("saveUnsaved"))
                .setActions(
                        new DialogAction(DialogAction.Type.OK, Action.Status.PRIMARY)
                                .withCaption(messages.getMainMessage("closeUnsaved.save"))
                                .withHandler(e -> {
                                    closeWithCommit()
                                            .then(result::success)
                                            .otherwise(result::fail);
                                }),
                        new BaseAction("discard")
                                .withIcon(icons.get(CubaIcon.DIALOG_CANCEL))
                                .withCaption(messages.getMainMessage("closeUnsaved.discard"))
                                .withHandler(e -> {
                                    closeWithDiscard()
                                            .then(result::success)
                                            .otherwise(result::fail);
                                }),
                        new DialogAction(DialogAction.Type.CANCEL)
                                .withIcon(null)
                                .withHandler(e -> {
                                    // todo try to move focus back
                                    // findAndFocusChildComponent();

                                    result.fail();
                                })
                )
                .show();

        return result;
    }

    /**
     * JavaDoc
     *
     * @param action close action
     * @return result of operation
     */
    public OperationResult close(CloseAction action) {
        CloseTriggeredEvent closeTriggeredEvent = new CloseTriggeredEvent(this, action);
        fireEvent(CloseTriggeredEvent.class, closeTriggeredEvent);
        if (closeTriggeredEvent.isClosePrevented()) {
            return OperationResult.fail();
        }

        if (action.isCheckForUnsavedChanges() && hasUnsavedChanges()) {
            Configuration configuration = beanLocator.get(Configuration.NAME);
            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

            if (clientConfig.getUseSaveConfirmation()) {
                return showSaveConfirmationDialog();
            } else {
                return showUnsavedChangesDialog();
            }
        }

        BeforeCloseEvent beforeCloseEvent = new BeforeCloseEvent(this, action);
        fireEvent(BeforeCloseEvent.class, beforeCloseEvent);
        if (beforeCloseEvent.isClosePrevented()) {
            return OperationResult.fail();
        }

        // save settings right before removing
        if (isSaveSettingsOnClose(action)) {
            saveSettings();
        }

        screenContext.getScreens().remove(this);

        AfterCloseEvent afterCloseEvent = new AfterCloseEvent(this, action);
        fireEvent(AfterCloseEvent.class, afterCloseEvent);

        return OperationResult.success();
    }

    protected boolean isSaveSettingsOnClose(@SuppressWarnings("unused") CloseAction action) {
        Configuration configuration = beanLocator.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        return !clientConfig.getManualScreenSettingsSaving();
    }

    /**
     * @return if the screen has unsaved changes
     */
    public boolean hasUnsavedChanges() {
        return false;
    }

    /**
     * JavaDoc
     *
     * @return
     */
    public OperationResult closeWithCommit() {
        return commitChanges()
                .compose(() -> close(WINDOW_COMMIT_AND_CLOSE_ACTION));
    }

    /**
     * JavaDoc
     */
    protected OperationResult commitChanges() {
        return OperationResult.success();
    }

    /**
     * JavaDoc
     */
    public OperationResult closeWithDiscard() {
        return close(WINDOW_DISCARD_AND_CLOSE_ACTION);
    }

    /**
     * JavaDoc
     */
    protected Settings getSettings() {
        return settings;
    }

    /**
     * JavaDoc
     */
    protected void saveSettings() {
        if (settings != null) {
            walkComponents(
                    window,
                    (component, name) -> {
                        if (component.getId() != null
                                && component instanceof HasSettings) {
                            LoggerFactory.getLogger(Screen.class)
                                    .trace("Saving settings for {} : {}", name, component);

                            Element e = settings.get(name);
                            boolean modified = ((HasSettings) component).saveSettings(e);

                            if (component instanceof HasPresentations
                                    && ((HasPresentations) component).isUsePresentations()) {
                                Object def = ((HasPresentations) component).getDefaultPresentationId();
                                e.addAttribute("presentation", def != null ? def.toString() : "");
                                Presentations presentations = ((HasPresentations) component).getPresentations();
                                if (presentations != null) {
                                    presentations.commit();
                                }
                            }
                            settings.setModified(modified);
                        }
                    }
            );
            settings.commit();
        }
    }

    /**
     * JavaDoc
     *
     * @param settings
     */
    protected void applySettings(Settings settings) {
        this.settings = settings;

        walkComponents(
                window,
                (component, name) -> {
                    if (component.getId() != null
                            && component instanceof HasSettings) {
                        LoggerFactory.getLogger(Screen.class)
                                .trace("Applying settings for {} : {} ", name, component);

                        Element e = this.settings.get(name);
                        ((HasSettings) component).applySettings(e);

                        if (component instanceof HasPresentations
                                && e.attributeValue("presentation") != null) {
                            String def = e.attributeValue("presentation");
                            if (!StringUtils.isEmpty(def)) {
                                UUID defaultId = UUID.fromString(def);
                                ((HasPresentations) component).applyPresentationAsDefault(defaultId);
                            }
                        }
                    }
                }
        );
    }

    /**
     * JavaDoc
     */
    protected void deleteSettings() {
        settings.delete();
    }
}