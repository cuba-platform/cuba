/*
 * Copyright 2015 Nikita Petunin, Yuriy Artamonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.addons.dragdroplayouts.interfaces;

import com.vaadin.ui.Component;

import java.io.Serializable;

/**
 * Grab filter. If filter is set and returned false for some child/nested component then you will not be able to
 * drag component grabbing this child. E.g. if we have composite Panel inside of some layout that includes Button and
 * Label you can deny dragging of Panel using Button. <br>
 *
 * This feature is similar to {@link DragFilter} but more powerful, since it can filter out deep nested components.
 */
public interface DragGrabFilter extends Serializable {
    /**
     * @param component nested component (can be deep child of Layout)
     * @return true if this nested/deep child can be grabbed to drag child component
     */
    boolean canBeGrabbed(Component component);
}