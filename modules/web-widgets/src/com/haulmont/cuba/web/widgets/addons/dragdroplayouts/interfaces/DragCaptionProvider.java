/*
 * Copyright 2017 Nikita Petunin, Yuriy Artamonov
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
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DragCaption;

/**
 * Interface that provides custom {@link DragCaption} for a child component. <br>
 * The drag caption will be shown instead of the component when the user drag a component in the layout. <br>
 * Drag caption is shown as simple DIV with icon and SPAN with caption on client side for a dragged component. <br>
 * It is the analogue of {@link DragImageProvider} but it does not require additional components for a dragged caption.
 */
public interface DragCaptionProvider {
    DragCaption getDragCaption(Component component);
}