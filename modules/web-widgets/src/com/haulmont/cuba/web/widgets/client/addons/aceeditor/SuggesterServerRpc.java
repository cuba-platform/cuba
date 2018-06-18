/*
 * Copyright 2017 Antti Nieminen
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
package com.haulmont.cuba.web.widgets.client.addons.aceeditor;


import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportRange;

import com.vaadin.shared.communication.ServerRpc;

public interface SuggesterServerRpc extends ServerRpc {
	
	// TODO: it may not be necessary to send the whole text here
	// but I guess it's simplest...
	
	public void suggest(String text, TransportRange selection);

	public void suggestionSelected(int index);
	
	
}
