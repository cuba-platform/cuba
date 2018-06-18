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

import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ServerRpc;

public interface AceEditorServerRpc extends ServerRpc {
	
	public void changed(TransportDiff diff, TransportRange selection, boolean focused);
	
	@Delayed(lastOnly=true)
	public void changedDelayed(TransportDiff diff, TransportRange selection, boolean focused);
	
}
