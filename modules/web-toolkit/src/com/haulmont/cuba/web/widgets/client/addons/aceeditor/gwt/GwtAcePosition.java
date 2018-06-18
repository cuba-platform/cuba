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
 * {row: int, column: int}
 * 
 */
public class GwtAcePosition extends JavaScriptObject {
	protected GwtAcePosition() {
	}

	public static final native GwtAcePosition create(int row, int column) /*-{
		return {row:row, column:column};
	}-*/;

	public final native int getRow() /*-{
		return this.row;
	}-*/;

	public final native int getColumn() /*-{
		return this.column;
	}-*/;
}
