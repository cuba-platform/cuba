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

package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.meta.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.EventObject;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Prepares and shows notifications.
 */
@StudioFacet(
        caption = "Notification",
        description = "Prepares and shows notifications",
        defaultProperty = "caption",
        defaultEvent = "CloseEvent"
)
@StudioProperties(
        properties = {
                @StudioProperty(name = "id", required = true)
        }
)
public interface NotificationFacet extends Facet {

    /**
     * Sets notification caption.
     *
     * @param caption caption
     */
    @StudioProperty(type = PropertyType.LOCALIZED_STRING)
    void setCaption(String caption);

    /**
     * @return notification caption
     */
    String getCaption();

    /**
     * Sets notification description.
     *
     * @param description description
     */
    @StudioProperty(type = PropertyType.LOCALIZED_STRING)
    void setDescription(String description);

    /**
     * @return notification description
     */
    String getDescription();

    /**
     * Sets the delay before the notification disappears.
     *
     * @param delayMs the desired delay in milliseconds
     */
    @StudioProperty
    @PositiveOrZero
    void setDelay(int delayMs);

    /**
     * @return the delay before the notification disappears
     */
    int getDelay();

    /**
     * Sets notification type.
     *
     * @param type type
     */
    @StudioProperty
    void setType(NotificationType type);

    /**
     * @return notification type
     */
    NotificationType getType();

    /**
     * Sets notification content mode.
     *
     * @param contentMode content mode
     */
    @StudioProperty
    void setContentMode(ContentMode contentMode);

    /**
     * @return notification content mode
     */
    ContentMode getContentMode();

    /**
     * Sets notification style name.
     *
     * @param styleName style name
     */
    @StudioProperty(type = PropertyType.STRING)
    void setStyleName(String styleName);

    /**
     * @return notification style name
     */
    String getStyleName();

    /**
     * Sets notification position.
     *
     * @param position position
     */
    @StudioProperty
    void setPosition(Notifications.Position position);

    /**
     * @return notification position
     */
    Notifications.Position getPosition();

    /**
     * @return id of action that triggers notification
     */
    String getActionTarget();

    /**
     * Sets that notification should be shown when action with id {@code actionId}
     * is performed.
     *
     * @param actionId action id
     */
    @StudioProperty(type = PropertyType.COMPONENT_ID)
    void setActionTarget(String actionId);

    /**
     * @return id of button that triggers notification
     */
    String getButtonTarget();

    /**
     * Sets that notification should be shown when button with id {@code actionId}
     * is clicked.
     *
     * @param buttonId button id
     */
    @StudioProperty(type = PropertyType.COMPONENT_ID)
    void setButtonTarget(String buttonId);

    /**
     * Shows notification.
     */
    void show();

    /**
     * Sets the given {@code Supplier} as notification caption provider.
     *
     * @param captionProvider notification caption provider
     */
    @StudioDelegate
    void setCaptionProvider(Supplier<String> captionProvider);

    /**
     * @return notification caption provider
     */
    Supplier<String> getCaptionProvider();

    /**
     * Sets the given {@code Supplier} as notification description provider.
     *
     * @param descriptionProvider notification description provider
     */
    @StudioDelegate
    void setDescriptionProvider(Supplier<String> descriptionProvider);

    /**
     * @return notification description provider
     */
    Supplier<String> getDescriptionProvider();

    /**
     * Adds the given {@code Consumer} as notification {@link CloseEvent} listener.
     *
     * @param listener close event listener
     * @return close event subscription
     */
    @StudioEvent
    Subscription addCloseListener(Consumer<CloseEvent> listener);

    /**
     * Event that is fired when notification is closed.
     */
    class CloseEvent extends EventObject {

        public CloseEvent(NotificationFacet source) {
            super(source);
        }

        @Override
        public NotificationFacet getSource() {
            return (NotificationFacet) super.getSource();
        }
    }
}
