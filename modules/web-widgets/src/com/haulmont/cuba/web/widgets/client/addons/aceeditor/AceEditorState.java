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

import java.util.HashMap;
import java.util.Map;

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportRange;

import com.vaadin.shared.AbstractFieldState;

@SuppressWarnings("serial")
public class AceEditorState extends AbstractFieldState {
	
	public String changeMode = "LAZY";
	public int changeTimeout = 400;
	
	public String mode = "text";
	
	public String theme = "textmate";
	
	public TransportRange selection = null;
	
	public boolean listenToSelectionChanges = false;
	
	public boolean listenToFocusChanges = false;
	
	public boolean useWorker = true;
	
	public boolean wordwrap = false;

    public boolean showGutter = true;

    public boolean showPrintMargin = true;

    public boolean highlightActiveLine = true;

	public Map<String,String> config = new HashMap<String,String>();
	
	public int diff_editCost = 4;
	
	public TransportDoc initialValue = null;
	
	public int scrollToRow = -1;

    public String fontSize= "12px";

    public boolean highlightSelectedWord = true;

    public boolean showInvisibles = false;
    
    public boolean displayIndentGuides = true;
    
	public int tabSize = 4;

	public boolean softTabs = true;
}