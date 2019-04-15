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
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;

/**
 * A GWT adaptation of Ace editor.
 * 
 * @see <a href="http://ace.ajax.org">Ace Editor</a>
 */
public class GwtAceEditor extends JavaScriptObject {

	protected GwtAceEditor() {
	}

	public static native GwtAceEditor create(Element parent, String editorId) /*-{
		var aceDiv = $doc.createElement("div");
		if (editorId !== null) {
			aceDiv.id = editorId;
		}
		aceDiv.style.width = "100%";
		aceDiv.style.height = "100%";
		parent.appendChild(aceDiv);
		
		var editor = $wnd.ace.edit(aceDiv);

		return editor;
	}-*/;
	
	native static public void setAceConfig(String key, String value) /*-{
		$wnd.ace.config.set(key, value);
	}-*/;

	public final void setMode(String mode) {
		setModeFullPath(getModeString(mode));
	}
	
	private final native void setModeFullPath(String mode) /*-{
		this.getSession().setMode(mode);
	}-*/;

	public final void setTheme(String theme) {
		setThemeFullPath(getThemeString(theme));
	}

	private final native void setThemeFullPath(String theme) /*-{
		this.setTheme(theme);
	}-*/;
	
	private static final String getModeString(String mode) {
		return "ace/mode/" + mode;
	}
	
	private static final String getThemeString(String theme) {
		return "ace/theme/" + theme;
	}

	public final native String getText() /*-{
		return this.getSession().getDocument().getValue();
	}-*/;

	public final native void setText(String text) /*-{
		this.getSession().getDocument().setValue(text);
	}-*/;

	public final native void replace(GwtAceRange range, String text) /*-{
		this.getSession().getDocument().replace(range,text);
	}-*/;

	public final native void insertLine(int index, String text) /*-{
		this.getSession().getDocument().insertLines(index,[text]);
	}-*/;

	public final native void removeLines(int from, int to) /*-{
		this.getSession().getDocument().removeLines(from, to);
	}-*/;

	public final native void addChangeHandler(GwtAceChangeHandler handler) /*-{
		var cb = function(event) {
			handler.@com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceChangeHandler::onChange(Lcom/haulmont/cuba/web/widgets/client/addons/aceeditor/gwt/GwtAceChangeEvent;)(event);
		}
		this.getSession().addEventListener("change", cb, true);
	}-*/;

	public final native void addChangeCursorHandler(
			GwtAceChangeCursorHandler handler) /*-{
		var cb = function(event) {
			handler.@com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceChangeCursorHandler::onChangeCursor(Lcom/haulmont/cuba/web/widgets/client/addons/aceeditor/gwt/GwtAceEvent;)(event);
		}
		this.getSession().getSelection().addEventListener("changeCursor" ,cb, true);
	}-*/;

	public final native void addChangeSelectionHandler(
			GwtAceChangeSelectionHandler handler) /*-{
		var cb = function(event) {
			handler.@com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceChangeSelectionHandler::onChangeSelection(Lcom/haulmont/cuba/web/widgets/client/addons/aceeditor/gwt/GwtAceEvent;)(event);
		}
		this.getSession().getSelection().addEventListener("changeSelection" ,cb, true);
	}-*/;

	public final native void setKeyboardHandler(GwtAceKeyboardHandler handler) /*-{
		var h = { handleKeyboard: function(data, hashId, keyString, keyCode, e) {
			
			// For most keypresses, Ace sends two events where keyCode===undefined in one of them.
			// There are some keys where it sends only one,
			// so we can't just ignore the undefined keycode events, after all. (?)
			// 	if (keyCode===undefined) {
			//		return;
			//	}
			
			var command = handler.@com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceKeyboardHandler::handleKeyboard(Lcom/google/gwt/core/client/JavaScriptObject;ILjava/lang/String;ILcom/haulmont/cuba/web/widgets/client/addons/aceeditor/gwt/GwtAceKeyboardEvent;)(data, hashId, keyString, keyCode, e);
			if (command===@com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceKeyboardHandler$Command::NULL) {
				return {command: "null"};
			}
		}};
		this.setKeyboardHandler(h);
	}-*/;

	public final native void addFocusListener(GwtAceFocusBlurHandler handler) /*-{
		var focus = function(event) {
			handler.@com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceFocusBlurHandler::onFocus(Lcom/haulmont/cuba/web/widgets/client/addons/aceeditor/gwt/GwtAceEvent;)(event);
		}
		var blur = function(event) {
			handler.@com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceFocusBlurHandler::onBlur(Lcom/haulmont/cuba/web/widgets/client/addons/aceeditor/gwt/GwtAceEvent;)(event);
		}
		this.addEventListener("focus", focus, true);
		this.addEventListener("blur", blur, true);
	}-*/;

	public final native void moveCursorTo(int row, int col) /*-{
		this.moveCursorTo(row, col);
	}-*/;

	public final native void moveCursorToPosition(GwtAcePosition pos) /*-{
		this.moveCursorToPosition(pos);
	}-*/;

	public final native GwtAcePosition getCursorPosition() /*-{
		return this.getCursorPosition();
	}-*/;

	public final native void scrollToRow(int row) /*-{
		this.scrollToRow(row);
	}-*/;

	public final native JsArrayInteger getCursorCoords() /*-{
		var p = this.getCursorPositionScreen();
		var c = this.renderer.textToScreenCoordinates(p.row,p.column);
		return [c.pageX,c.pageY];
	}-*/;

	public final native JsArrayInteger getCoordsOfRowCol(int row, int column) /*-{
		var p = this.getSession().documentToScreenPosition({row:row, column:column});
		var c = this.renderer.textToScreenCoordinates(p.row, p.column);
		return [c.pageX,c.pageY];
	}-*/;

	public final native JsArrayInteger getCoordsOf(GwtAcePosition pos) /*-{
		var p = this.getSession().documentToScreenPosition(pos);
		var c = this.renderer.textToScreenCoordinates(p.row, p.column);
		return [c.pageX,c.pageY];
	}-*/;

	public final native void focus() /*-{
		this.focus();
	}-*/;

	public final native void blur() /*-{
		this.blur();
	}-*/;

	public final native void setAnnotations(JsArray<GwtAceAnnotation> anns) /*-{
		this.getSession().setAnnotations(anns);
	}-*/;

	public final native void clearAnnotations() /*-{
		this.getSession().clearAnnotations();
	}-*/;

	public final native JsArray<GwtAceAnnotation> getAnnotations() /*-{
		return this.getSession().getAnnotations();
	}-*/;

	public final native void removeMarker(String markerId) /*-{
		this.getSession().removeMarker(markerId);
	}-*/;

	public final native String addMarker(GwtAceRange range, String cls,
			String type, boolean inFront) /*-{
		var id = this.getSession().addMarker(range, cls, type, inFront);
		return "" + id; // making sure it's a string
	}-*/;

	public final native void setWidth(String width) /*-{
		this.container.style.width = width;
	}-*/;

	public final native void setHeight(String height) /*-{
		this.container.style.height = height;
	}-*/;

	public final native void resize() /*-{
		this.resize();
	}-*/;

	public static native String keyName(int keyCode, boolean shift,
			boolean ctrl, boolean alt) /*-{	
		var keys = $wnd.ace.require("pilot/keys");
		
		var key = ""
		if (shift) { key += "Shift-"; }
		if (ctrl) { key += "Ctrl-"; }
		if (alt) { key += "Alt-"; }
		key += keys[keyCode];
		
		return key;
	}-*/;

	public final native int getLength() /*-{
		return this.getSession().getLength();
	}-*/;

	public final native String getLine(int row) /*-{
		return this.getSession().getLine(row);
	}-*/;

	public final native JsArrayString getLines(int startRow, int endRow) /*-{
		return this.getSession().getLines(startRow, endRow);
	}-*/;

	public final native int getLineLength(int row) /*-{
		return this.getSession().getLine(row).length;
	}-*/;

	public final native int getLongestLineLength(int row1, int row2) /*-{
		var maxLen = 0;
		var rowLen, i;
		for (i=row1; i<=row2; ++i) {
			rowLen = this.getSession().getLine(i).length;
			if (rowLen>maxLen) {
				maxLen = rowLen;
			}
		}
		return maxLen;
	}-*/;

	public final native GwtAceSelection getSelection() /*-{
		return this.getSelection();
	}-*/;

	public final native void setSelection(GwtAceRange range, boolean reverse) /*-{
		this.getSelection().setSelectionRange(range, reverse);
	}-*/;

	public final native void setReadOnly(boolean readOnly) /*-{
		this.setReadOnly(readOnly);
	}-*/;

	public final native void setShowInvisibles(boolean showInvisibles) /*-{
		this.setShowInvisibles(showInvisibles);
	}-*/;

	public final native String getNewLineCharacter() /*-{
		return this.getSession().getDocument().getNewLineCharacter();
	}-*/;

	public static native String keyCodeToString(int keyCode) /*-{
		return $wnd.ace.require("pilot/keys").keyCodeToString(keyCode);
	}-*/;

	public static native int keyModsToHashId(boolean shift, boolean ctrl,
			boolean alt) /*-{
		var ret = 0;
		if (ctrl) ret += 1;
		if (alt) ret += 2;
		if (shift) ret += 4;
		// TODO? other mods like Command
		return ret;
	}-*/;

	public final native void setHScrollBarAlwaysVisible(boolean visible) /*-{
		this.renderer.setHScrollBarAlwaysVisible(visible);
	}-*/;

	public final native void setFontSize(String size) /*-{
		this.container.style.fontSize = size;
	}-*/;

	public final native void scrollToY(int y) /*-{
		this.renderer.scrollToY(y);
	}-*/;

	public final native void scrollToRow(double row) /*-{
		this.scrollToRow(row);
	}-*/;

	public final native int getScrollTop() /*-{
		return this.renderer.getScrollTop();
	}-*/;

	public final native double getScrollTopRow() /*-{
		return this.renderer.getScrollTopRow();
	}-*/;

    public final native void setShowGutter(boolean showGutter) /*-{
        this.renderer.setShowGutter(showGutter);
    }-*/;

    public final native void setShowPrintMargin(boolean showPrintMargin) /*-{
        this.renderer.setShowPrintMargin(showPrintMargin);
    }-*/;

    public final native void setHighlightActiveLineEnabled(boolean highlightActiveLine) /*-{
        this.setHighlightActiveLine(highlightActiveLine);
    }-*/;

	//WRAPMODE
	public final native void setUseWrapMode(boolean useWrapMode) /*-{
		this.getSession().setUseWrapMode(useWrapMode);
	}-*/;

	public final native void setUseWorker(boolean use) /*-{
		this.getSession().setUseWorker(use);
	}-*/;

    public final native void setHighlightSelectedWord(boolean highlightSelectedWord) /*-{
        this.setHighlightSelectedWord(highlightSelectedWord);
    }-*/;
    
    public final native void setDisplayIndentGuides(boolean displayIndentGuides) /*-{
        this.setDisplayIndentGuides(displayIndentGuides);
    }-*/;

    public final native void setUseSoftTabs(boolean softTabs) /*-{
        this.getSession().setUseSoftTabs(softTabs);
    }-*/;

    public final native void setTabSize(int tabSize) /*-{
        this.getSession().setTabSize(tabSize);
    }-*/;
}