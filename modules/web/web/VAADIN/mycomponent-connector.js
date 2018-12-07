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

com_haulmont_cuba_web_sandbox_js_MyComponent = function () {
    // Create the component
    var mycomponent =
        new mylibrary.MyComponent(this.getElement());

    // Handle changes from the server-side
    this.onStateChange = function () {
        let state = this.getState();
        if (state.data) {
            let value = state.data.value;
            mycomponent.setValue(value);
        }
    };

    // Pass user interaction to the server-side
    var self = this;
    mycomponent.click = function () {
        let date = Date.now();
        self.onClick(date);
    };

    this.showNotification = function () {
        alert("TEST");
    };
};