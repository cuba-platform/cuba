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

import java.util.EventObject;

public interface SplitPanel extends Component.Container, Component.BelongToFrame, Component.HasIcon,
        Component.HasCaption, Component.HasSettings {

    String NAME = "split";

    int ORIENTATION_VERTICAL = 0;
    int ORIENTATION_HORIZONTAL = 1;

    int getOrientation();
    void setOrientation(int orientation);

    void setSplitPosition(int pos);
    void setSplitPosition(int pos, int unit);

    /**
     * Set position of split from the left side by default.
     * If reversePosition is true position will be set from right.
     */
    void setSplitPosition(int pos, int unit, boolean reversePosition);

    /**
     * @return position of the splitter.
     */
    float getSplitPosition();

    /**
     * @return unit of the splitter position.
     * See {@link Component#UNITS_PIXELS} and {@link Component#UNITS_PERCENTAGE}
     */
    int getSplitPositionUnit();

    /**
     * Return from which side position is set.
     */
    boolean isSplitPositionReversed();

    /**
     * Set minimum available position of split.
     * Minimum position of split will be set from the right if position is reversed.
     */
    void setMinSplitPosition(int pos, int unit);

    /**
     * Set maximum available position of split.
     * Maximum position of split will be set from the right if position is reversed.
     */
    void setMaxSplitPosition(int pos, int unit);

    void setLocked(boolean locked);
    boolean isLocked();

    /**
     * @deprecated Use {@link #addSplitPositionChangeListener}
     */
    @Deprecated
    void setPositionUpdateListener(PositionUpdateListener positionListener);
    /**
     * @deprecated Use {@link #removeSplitPositionChangeListener}
     */
    @Deprecated
    PositionUpdateListener getPositionUpdateListener();

    /**
     * @deprecated Use {@link SplitPositionChangeListener}
     */
    @Deprecated
    interface PositionUpdateListener {
        void updatePosition(float previousPosition, float newPosition);
    }

    /**
     * Event that indicates a change in SplitPanel's splitter position.
     */
    class SplitPositionChangeEvent extends EventObject {
        private final float previousPosition;
        private final float newPosition;

        public SplitPositionChangeEvent(SplitPanel splitPanel, float previousPosition, float newPosition) {
            super(splitPanel);
            this.previousPosition = previousPosition;
            this.newPosition = newPosition;
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
    }

    /**
     * Interface for listening for {@link SplitPositionChangeEvent}s fired by a SplitPanel.
     */
    @FunctionalInterface
    interface SplitPositionChangeListener {
        void onSplitPositionChanged(SplitPositionChangeEvent event);
    }

    void addSplitPositionChangeListener(SplitPositionChangeListener listener);
    void removeSplitPositionChangeListener(SplitPositionChangeListener listener);
}