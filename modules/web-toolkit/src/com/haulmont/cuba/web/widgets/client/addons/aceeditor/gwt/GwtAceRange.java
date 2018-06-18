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
package com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * {start: {@link GwtAcePosition}, end: {@link GwtAcePosition}
 * 
 */
public class GwtAceRange extends JavaScriptObject {
	protected GwtAceRange() {
	}

	public final static native GwtAceRange create(int startRow, int startCol,
			int endRow, int endCol) /*-{
		var Range = $wnd.ace.require("ace/range").Range;
		return new Range(startRow,startCol,endRow,endCol);
	}-*/;

	public final native GwtAcePosition getStart() /*-{
		return this.start;
	}-*/;

	public final native GwtAcePosition getEnd() /*-{
		return this.end;
	}-*/;

}
