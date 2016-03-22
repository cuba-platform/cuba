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

/**
 */
public interface SplitPanel extends Component.Container, Component.BelongToFrame {

    String NAME = "split";

    int ORIENTATION_VERTICAL = 0;
    int ORIENTATION_HORIZONTAL = 1;

    int getOrientation();
    void setOrientation(int orientation);

    void setSplitPosition(int pos);
    void setSplitPosition(int pos, int unit);

    /**
     * Set position of split from left side by default
     * If reversePosition is true position would be set from right
     */
    void setSplitPosition(int pos, int unit, boolean reversePosition);

    /**
     * Return split reversion
     */
    boolean isSplitPositionReversed();

    void setMinSplitPosition(int pos, int unit);
    void setMaxSplitPosition(int pos, int unit);

    void setLocked(boolean locked);
    boolean isLocked();

    void setPositionUpdateListener(PositionUpdateListener positionListener);
    PositionUpdateListener getPositionUpdateListener();

    interface PositionUpdateListener {
        void updatePosition(float previousPosition, float newPosition);
    }
}