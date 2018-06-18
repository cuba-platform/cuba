/*
 * Copyright 2015 John Ahlroos
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

/**
 * Adds Iframe shimming support for layout components. This means that when
 * shimming is turned on a element is placed on top of the component and acts as
 * a delegate for the component. No clicks or other mouse events go through the
 * shim delegate. This allows dragging an iframe based component. When shimming
 * is turned off all mouse event passes into the iframe and you cannot drag the
 * component.
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.3
 */
public interface ShimSupport {

    /**
     * False to disable the iframe shim used to enable dragging iframe based
     * components (defaults to true).
     * 
     * @param shim Are the iframes shimmed
     */
    void setShim(boolean shim);

    /**
     * Are shims used over iframes so dragging is possible
     * 
     * @return Are the iframes shimmed
     */
    boolean isShimmed();

}
