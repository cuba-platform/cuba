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
package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.function.Consumer;

public interface SplitPanel extends ComponentContainer, Component.BelongToFrame, Component.HasIcon,
        Component.HasCaption, HasContextHelp, HasSettings, HasHtmlCaption, HasHtmlDescription {

    String NAME = "split";

    int ORIENTATION_VERTICAL = 0;
    int ORIENTATION_HORIZONTAL = 1;

    /**
     * Specifies SplitPanel docking direction.
     */
    enum DockMode {
        LEFT,
        RIGHT
    }

    int getOrientation();
    void setOrientation(int orientation);

    void setSplitPosition(int pos);

    /**
     * @deprecated Use {@link #setSplitPosition(int, SizeUnit)}
     */
    @Deprecated
    void setSplitPosition(int pos, int unit);

    /**
     * Set position of split from the left side by default.
     *
     * @param pos  the new size of the first region.
     * @param unit the unit (from {@link SizeUnit}) in which the size is given.
     */
    void setSplitPosition(int pos, SizeUnit unit);

    /**
     * Set position of split from the left side by default.
     * If reversePosition is true position will be set from right.
     *
     * @deprecated Use {@link #setSplitPosition(int, SizeUnit, boolean)}
     */
    @Deprecated
    void setSplitPosition(int pos, int unit, boolean reversePosition);

    /**
     * Set position of split from the left side by default.
     * If reversePosition is true position will be set from right.
     *
     * @param pos             the new size of the first region.
     * @param unit            the unit (from {@link SizeUnit}) in which the size is given.
     * @param reversePosition if set to true the split splitter position is measured
     *                        by the second region else it is measured by the first region
     */
    void setSplitPosition(int pos, SizeUnit unit, boolean reversePosition);

    /**
     * @return position of the splitter.
     */
    float getSplitPosition();

    /**
     * @return unit of the splitter position.
     * See {@link Component#UNITS_PIXELS} and {@link Component#UNITS_PERCENTAGE}
     */
    @Deprecated
    int getSplitPositionUnit();

    /**
     * Returns the unit of position of the splitter.
     *
     * @return unit of position of the splitter
     */
    SizeUnit getSplitPositionSizeUnit();

    /**
     * Return from which side position is set.
     */
    boolean isSplitPositionReversed();

    /**
     * Set minimum available position of split.
     * Minimum position of split will be set from the right if position is reversed.
     *
     * @deprecated Use {@link #setMinSplitPosition(int, SizeUnit)}
     */
    @Deprecated
    void setMinSplitPosition(int pos, int unit);

    /**
     * Sets the minimum split position to the given position and unit. If the
     * split position is reversed, maximum and minimum are also reversed.
     *
     * @param pos  the new size of the first region.
     * @param unit the unit (from {@link SizeUnit}) in which the size is given.
     */
    void setMinSplitPosition(int pos, SizeUnit unit);

    /**
     * Set maximum available position of split.
     * Maximum position of split will be set from the right if position is reversed.
     *
     * @deprecated Use {@link #setMaxSplitPosition(int, SizeUnit)}
     */
    @Deprecated
    void setMaxSplitPosition(int pos, int unit);

    /**
     * Sets the maximum split position to the given position and unit. If the
     * split position is reversed, maximum and minimum are also reversed.
     *
     * @param pos  the new size of the first region.
     * @param unit the unit (from {@link SizeUnit}) in which the size is given.
     */
    void setMaxSplitPosition(int pos, SizeUnit unit);

    /**
     * Sets whether users are able to change the separator position or not.
     *
     * @param locked locked
     */
    void setLocked(boolean locked);

    /**
     * @return whether users are able to change the separator position or not.
     */
    boolean isLocked();

    /**
     * Enables or disables SplitPanel dock button.
     * <p>
     * Notice that docking is available only for horizontally oriented SplitPanel.
     *
     * @param dockable dockable
     */
    void setDockable(boolean dockable);

    /**
     * @return whether dock button is enabled or not
     */
    boolean isDockable();

    /**
     * Sets docking direction.
     * <p>
     * Notice that docking is available only for horizontally oriented SplitPanel.
     *
     * @param dockMode one of {@link DockMode} options
     */
    void setDockMode(DockMode dockMode);

    /**
     * @return docking direction or null in case of vertically oriented SplitPanel
     */
    @Nullable
    DockMode getDockMode();

    /**
     * Event that indicates a change in SplitPanel's splitter position.
     */
    class SplitPositionChangeEvent extends EventObject implements HasUserOriginated {
        private final float previousPosition;
        private final float newPosition;
        private final boolean userOriginated;

        public SplitPositionChangeEvent(SplitPanel splitPanel, float previousPosition, float newPosition) {
            this(splitPanel, previousPosition, newPosition, false);
        }

        public SplitPositionChangeEvent(Object source,
                                        float previousPosition, float newPosition, boolean userOriginated) {
            super(source);
            this.previousPosition = previousPosition;
            this.newPosition = newPosition;
            this.userOriginated = userOriginated;
        }

        @Override
        public SplitPanel getSource() {
            return (SplitPanel) super.getSource();
        }

        public float getPreviousPosition() {
            return previousPosition;
        }

        public float getNewPosition() {
            return newPosition;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }

    /**
     * Adds a listener for {@link SplitPositionChangeEvent}s fired by a SplitPanel.
     *
     * @param listener a listener to add
     */
    Subscription addSplitPositionChangeListener(Consumer<SplitPositionChangeEvent> listener);

    /**
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeSplitPositionChangeListener(Consumer<SplitPositionChangeEvent> listener);
}