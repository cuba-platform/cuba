/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.events;

/**
 * Marker interface for events that are sent to UIs screens.
 * UiEvent events are passed only to {@link org.springframework.context.event.EventListener} methods declared in UI
 * controllers of windows and frames and they can be fired only from UI thread using
 * {@link com.haulmont.cuba.core.global.Events} bean.
 *
 * @see com.haulmont.cuba.core.global.Events
 * @see org.springframework.context.event.EventListener
 * @see org.springframework.core.annotation.Order
 */
public interface UiEvent {
}