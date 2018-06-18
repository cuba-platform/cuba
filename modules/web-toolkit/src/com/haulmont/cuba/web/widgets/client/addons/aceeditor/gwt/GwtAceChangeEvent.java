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
 * A text change event received from Ace.
 * 
 */
public class GwtAceChangeEvent extends GwtAceEvent {
	protected GwtAceChangeEvent() {
	}

	public final native Data getData() /*-{
										return this.data;
										}-*/;

	public static class Data extends JavaScriptObject {
		protected Data() {
		}

		public enum Action {
			insertText, insertLines, removeText, removeLines
		}

		public final Action getAction() {
			return Action.valueOf(getActionString());
		}

		private final native String getActionString() /*-{
			return this.action;
		}-*/;

		public final native GwtAceRange getRange() /*-{
			return this.range;
		}-*/;

		public final native String getText() /*-{
			return this.text;
		}-*/;
	}

}
