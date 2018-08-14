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
package com.haulmont.cuba.web.widgets.addons.aceeditor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EventObject;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haulmont.cuba.web.widgets.WebJarResource;
import com.vaadin.shared.Registration;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceAnnotation.MarkerAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceAnnotation.RowAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceDoc;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceEditorClientRpc;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceEditorServerRpc;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceEditorState;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceMarker;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceMarker.OnTextChange;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceMarker.Type;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceRange;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDiff;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportRange;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.Util;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.BlurNotifier;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.FocusNotifier;
import com.vaadin.ui.AbstractField;
import com.vaadin.util.ReflectTools;

/**
 * 
 * AceEditor wraps an Ace code editor inside a TextField-like Vaadin component.
 * 
 */
@SuppressWarnings("serial")
@WebJarResource(value = {
		"ace-builds:ace.js",
		"ace-builds:ext-searchbox.js",
        "diff-match-patch:diff_match_patch.js"
})
public class AceEditor extends AbstractField<String> implements BlurNotifier,
		FocusNotifier {

    private String value;

    public static class DiffEvent extends Event {
		public static String EVENT_ID = "aceeditor-diff";
		private final ServerSideDocDiff diff;

		public DiffEvent(AceEditor ed, ServerSideDocDiff diff) {
			super(ed);
			this.diff = diff;
		}

		public ServerSideDocDiff getDiff() {
			return diff;
		}
	}

	public interface DiffListener extends Serializable {
		Method diffMethod = ReflectTools.findMethod(
				DiffListener.class, "diff", DiffEvent.class);

		void diff(DiffEvent e);
	}

	public static class SelectionChangeEvent extends Event {
		public static String EVENT_ID = "aceeditor-selection";
		private final TextRange selection;

		public SelectionChangeEvent(AceEditor ed) {
			super(ed);
			this.selection = ed.getSelection();
		}

		public TextRange getSelection() {
			return selection;
		}
	}

	public interface SelectionChangeListener extends Serializable {
		Method selectionChangedMethod = ReflectTools
				.findMethod(SelectionChangeListener.class, "selectionChanged",
						SelectionChangeEvent.class);

		void selectionChanged(SelectionChangeEvent e);
	}

	public static class TextChangeEventImpl extends EventObject {
		private final TextRange selection;
		private final String text;

		private TextChangeEventImpl(final AceEditor ace, String text,
				AceRange selection) {
			super(ace);
			this.text = text;
			this.selection = ace.getSelection();
		}

//		@Override
//		public AbstractTextField getComponent() {
//			return (AbstractTextField) super.getComponent();
//		}

//		@Override
		public int getCursorPosition() {
			return selection.getEnd();
		}

//		@Override
		public String getText() {
			return text;
		}
	}

	// By default, using the version 1.1.9 of Ace from GitHub via rawgit.com.
	// It's recommended to host the Ace files yourself as described in README.
	private static final String DEFAULT_ACE_PATH = "//cdn.rawgit.com/ajaxorg/ace-builds/e3ccd2c654cf45ee41ffb09d0e7fa5b40cf91a8f/src-min-noconflict";

	private AceDoc doc = new AceDoc();

	private boolean isFiringTextChangeEvent;

	private boolean latestFocus = false;
	private long latestMarkerId = 0L;

	private static final Logger logger = Logger.getLogger(AceEditor.class
			.getName());

	private boolean onRoundtrip = false;

	private AceEditorServerRpc rpc = new AceEditorServerRpc() {
		@Override
		public void changed(TransportDiff diff, TransportRange selection,
				boolean focused) {
			clientChanged(diff, selection, focused);
		}

		@Override
		public void changedDelayed(TransportDiff diff,
				TransportRange selection, boolean focused) {
			clientChanged(diff, selection, focused);
		}
	};

	private TextRange selection = new TextRange("", 0, 0, 0, 0);
	// {startPos,endPos} or {startRow,startCol,endRow,endCol}
	private Integer[] selectionToClient = null;
	private AceDoc shadow = new AceDoc();

	{
		logger.setLevel(Level.WARNING);
	}

	public AceEditor() {
		super();
		setWidth("300px");
		setHeight("200px");

		setModePath(DEFAULT_ACE_PATH);
		setThemePath(DEFAULT_ACE_PATH);
		setWorkerPath(DEFAULT_ACE_PATH);

		registerRpc(rpc);
	}

    @Override
    protected void doSetValue(String s) {
	    this.value = s;
    }

    public void addDiffListener(DiffListener listener) {
		addListener(DiffEvent.EVENT_ID, DiffEvent.class, listener,
				DiffListener.diffMethod);
	}

	@Override
	public Registration addFocusListener(FocusListener listener) {
        Registration registration = addListener(FocusEvent.EVENT_ID, FocusEvent.class, listener,
                FocusListener.focusMethod);
        getState().listenToFocusChanges = true;
        return registration;
    }

	@Override
	public Registration addBlurListener(BlurListener listener) {
        Registration registration = addListener(BlurEvent.EVENT_ID, BlurEvent.class, listener,
                BlurListener.blurMethod);
        getState().listenToFocusChanges = true;
        return registration;
    }

	/**
	 * Adds an ace marker. The id of the marker must be unique within this
	 * editor.
	 * 
	 * @param marker
	 * @return marker id
	 */
	public String addMarker(AceMarker marker) {
		doc = doc.withAdditionalMarker(marker);
		markAsDirty();
		return marker.getMarkerId();
	}

	/**
	 * Adds an ace marker with a generated id. The id is unique within this
	 * editor.
	 * 
	 * @param range
	 * @param cssClass
	 * @param type
	 * @param inFront
	 * @param onChange
	 * @return marker id
	 */
	public String addMarker(AceRange range, String cssClass, Type type,
			boolean inFront, OnTextChange onChange) {
		return addMarker(new AceMarker(newMarkerId(), range, cssClass, type,
				inFront, onChange));
	}

	public void addMarkerAnnotation(AceAnnotation ann, AceMarker marker) {
		addMarkerAnnotation(ann, marker.getMarkerId());
	}

	public void addMarkerAnnotation(AceAnnotation ann, String markerId) {
		doc = doc.withAdditionalMarkerAnnotation(new MarkerAnnotation(markerId,
				ann));
		markAsDirty();
	}

	public void addRowAnnotation(AceAnnotation ann, int row) {
		doc = doc.withAdditionalRowAnnotation(new RowAnnotation(row, ann));
		markAsDirty();
	}

	public void addSelectionChangeListener(SelectionChangeListener listener) {
		addListener(SelectionChangeEvent.EVENT_ID, SelectionChangeEvent.class,
				listener, SelectionChangeListener.selectionChangedMethod);
		getState().listenToSelectionChanges = true;
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		super.beforeClientResponse(initial);
		if (initial) {
			getState().initialValue = doc.asTransport();
			shadow = doc;
		} else if (onRoundtrip) {
			ServerSideDocDiff diff = ServerSideDocDiff.diff(shadow, doc);
			shadow = doc;
			TransportDiff td = diff.asTransport();
			getRpcProxy(AceEditorClientRpc.class).diff(td);

			onRoundtrip = false;
		} else if (true /* TODO !shadow.equals(doc) */) {
			getRpcProxy(AceEditorClientRpc.class).changedOnServer();
		}

		if (selectionToClient != null) {
			// {startPos,endPos}
			if (selectionToClient.length == 2) {
				AceRange r = AceRange.fromPositions(selectionToClient[0],
						selectionToClient[1], doc.getText());
				getState().selection = r.asTransport();
			}
			// {startRow,startCol,endRow,endCol}
			else if (selectionToClient.length == 4) {
				TransportRange tr = new TransportRange(selectionToClient[0],
						selectionToClient[1], selectionToClient[2],
						selectionToClient[3]);
				getState().selection = tr;
			}
			selectionToClient = null;
		}
	}

	public void clearMarkerAnnotations() {
		Set<MarkerAnnotation> manns = Collections.emptySet();
		doc = doc.withMarkerAnnotations(manns);
		markAsDirty();
	}

	public void clearMarkers() {
		doc = doc.withoutMarkers();
		markAsDirty();
	}

	public void clearRowAnnotations() {
		Set<RowAnnotation> ranns = Collections.emptySet();
		doc = doc.withRowAnnotations(ranns);
		markAsDirty();
	}

	public int getCursorPosition() {
		return selection.getEnd();
	}

	public AceDoc getDoc() {
		return doc;
	}

	public TextRange getSelection() {
		return selection;
	}

	public Class<? extends String> getType() {
		return String.class;
	}

	public void removeDiffListener(DiffListener listener) {
		removeListener(DiffEvent.EVENT_ID, DiffEvent.class, listener);
	}

	public void removeFocusListener(FocusListener listener) {
		removeListener(FocusEvent.EVENT_ID, FocusEvent.class, listener);
		getState().listenToFocusChanges = !getListeners(FocusEvent.class)
				.isEmpty() || !getListeners(BlurEvent.class).isEmpty();
	}

	public void removeBlurListener(BlurListener listener) {
		removeListener(BlurEvent.EVENT_ID, BlurEvent.class, listener);
		getState().listenToFocusChanges = !getListeners(FocusEvent.class)
				.isEmpty() || !getListeners(BlurEvent.class).isEmpty();
	}


	public void removeMarker(AceMarker marker) {
		removeMarker(marker.getMarkerId());
	}

	public void removeMarker(String markerId) {
		doc = doc.withoutMarker(markerId);
		markAsDirty();
	}

	public void removeSelectionChangeListener(SelectionChangeListener listener) {
		removeListener(SelectionChangeEvent.EVENT_ID,
				SelectionChangeEvent.class, listener);
		getState().listenToSelectionChanges = !getListeners(
				SelectionChangeEvent.class).isEmpty();
	}

//	@Override
//	public void removeTextChangeListener(ValueChangeListener<String> listener) {
//		removeListener(listener);
//	}

	public void setBasePath(String path) {
		setAceConfig("basePath", path);
	}

	/**
	 * Sets the cursor position to be pos characters from the beginning of the
	 * text.
	 * 
	 * @param pos
	 */
	public void setCursorPosition(int pos) {
		setSelection(pos, pos);
	}

	/**
	 * Sets the cursor on the given row and column.
	 * 
	 * @param row
	 *            starting from 0
	 * @param col
	 *            starting from 0
	 */
	public void setCursorRowCol(int row, int col) {
		setSelectionRowCol(row, col, row, col);
	}

	public void setDoc(AceDoc doc) {
		if (this.doc.equals(doc)) {
			return;
		}
		this.doc = doc;
		boolean wasReadOnly = isReadOnly();
		setReadOnly(false);
		setValue(doc.getText());
		setReadOnly(wasReadOnly);
		markAsDirty();
	}

	public void setMode(AceMode mode) {
		getState().mode = mode.toString();
	}

	public void setMode(String mode) {
		getState().mode = mode;
	}

	public void setModePath(String path) {
		setAceConfig("modePath", path);
	}

	/**
	 * Sets the selection to be between characters [start,end).
	 * 
	 * The cursor will be at the end.
	 * 
	 * @param start
	 * @param end
	 */
	// TODO
	public void setSelection(int start, int end) {
		setSelectionToClient(new Integer[] { start, end });
		setInternalSelection(new TextRange(getValue(), start, end));
	}

	/**
	 * Sets the selection to be between the given (startRow,startCol) and
	 * (endRow, endCol).
	 * 
	 * The cursor will be at the end.
	 * 
	 * @param startRow
	 *            starting from 0
	 * @param startCol
	 *            starting from 0
	 * @param endRow
	 *            starting from 0
	 * @param endCol
	 *            starting from 0
	 */
	public void setSelectionRowCol(int startRow, int startCol, int endRow,
			int endCol) {
		setSelectionToClient(new Integer[] { startRow, startCol, endRow, endCol });
		setInternalSelection(new TextRange(doc.getText(), startRow, startCol,
				endRow, endCol));
	}

//	/**
//	 * Sets the mode how the TextField triggers {@link TextChangeEvent}s.
//	 *
//	 * @param inputEventMode
//	 *            the new mode
//	 *
//	 * @see TextChangeEventMode
//	 */
//	public void setTextChangeEventMode(TextChangeEventMode inputEventMode) {
//		getState().changeMode = inputEventMode.toString();
//	}
//
//	/**
//	 * The text change timeout modifies how often text change events are
//	 * communicated to the application when {@link #setTextChangeEventMode} is
//	 * {@link TextChangeEventMode#LAZY} or {@link TextChangeEventMode#TIMEOUT}.
//	 *
//	 *
//	 * @param timeoutMs
//	 *            the timeout in milliseconds
//	 */
//	public void setTextChangeTimeout(int timeoutMs) {
//		getState().changeTimeout = timeoutMs;
//
//	}

	/**
	 * Scrolls to the given row. First row is 0.
	 * 
	 */
	public void scrollToRow(int row) {
		getState().scrollToRow = row;
	}

	/**
	 * Scrolls the to the given position (characters from the start of the
	 * file).
	 * 
	 */
	public void scrollToPosition(int pos) {
		int[] rowcol = Util.lineColFromCursorPos(getValue(), pos, 0);
		scrollToRow(rowcol[0]);
	}

	public void setTheme(AceTheme theme) {
		getState().theme = theme.toString();
	}

	public void setTheme(String theme) {
		getState().theme = theme;
	}

	public void setThemePath(String path) {
		setAceConfig("themePath", path);
	}

	public void setUseWorker(boolean useWorker) {
		getState().useWorker = useWorker;
	}

	public void setWordWrap(boolean ww) {
		getState().wordwrap = ww;
	}

	public void setShowGutter(boolean showGutter) {
		getState().showGutter = showGutter;
	}

	public boolean isShowGutter() {
		return getState(false).showGutter;
	}

	public void setShowPrintMargin(boolean showPrintMargin) {
		getState().showPrintMargin = showPrintMargin;
	}

	public boolean isShowPrintMargin() {
		return getState(false).showPrintMargin;
	}

	public void setHighlightActiveLine(boolean highlightActiveLine) {
		getState().highlightActiveLine = highlightActiveLine;
	}

	public boolean isHighlightActiveLine() {
		return getState(false).highlightActiveLine;
	}

	public void setWorkerPath(String path) {
		setAceConfig("workerPath", path);
	}

	/**
	 * Use "auto" if you want to detect font size from CSS
	 *
	 * @param size
	 *            auto or font size
	 */
	public void setFontSize(String size) {
		getState().fontSize = size;
	}

	public String getFontSize() {
		return getState(false).fontSize;
	}

	public void setHighlightSelectedWord(boolean highlightSelectedWord) {
		getState().highlightSelectedWord = highlightSelectedWord;
	}

	public boolean isHighlightSelectedWord() {
		return getState(false).highlightSelectedWord;
	}

	public void setShowInvisibles(boolean showInvisibles) {
		getState().showInvisibles = showInvisibles;
	}

	public boolean isShowInvisibles() {
		return getState(false).showInvisibles;
	}

	public void setDisplayIndentGuides(boolean displayIndentGuides) {
		getState().displayIndentGuides = displayIndentGuides;
	}

	public boolean isDisplayIndentGuides() {
		return getState(false).displayIndentGuides;
	}
	
	public void setTabSize(int size) {
		getState().tabSize = size;
	}

	public void setUseSoftTabs(boolean softTabs) {
		getState().softTabs = softTabs;
	}

	protected void clientChanged(TransportDiff diff, TransportRange selection,
			boolean focused) {
		diffFromClient(diff);
		selectionFromClient(selection);
		if (latestFocus != focused) {
			latestFocus = focused;
			if (focused) {
				fireFocus();
			} else {
				fireBlur();
			}
		}

		clearStateFromServerToClient();
	}

	// Here we clear the selection etc. we sent earlier.
	// The client has already received the values,
	// and we must clear them at some point to not keep
	// setting the same selection etc. over and over.
	// TODO: this is a bit messy...
	private void clearStateFromServerToClient() {
		getState().selection = null;
		getState().scrollToRow = -1;
	}

	@Override
	protected AceEditorState getState() {
		return (AceEditorState) super.getState();
	}

	@Override
	protected AceEditorState getState(boolean markAsDirty) {
		return (AceEditorState) super.getState(markAsDirty);
	}

	@Override
    public void setValue(String newValue) {
		super.setValue(newValue);
		doc = doc.withText(newValue);
	}

    @Override
    public String getValue() {
        return value;
    }

    private void diffFromClient(TransportDiff d) {
		String previousText = doc.getText();
		ServerSideDocDiff diff = ServerSideDocDiff.fromTransportDiff(d);
		shadow = diff.applyTo(shadow);
		doc = diff.applyTo(doc);
		if (!TextUtils.equals(doc.getText(), previousText)) {
			setValue(doc.getText(), true);
			fireTextChangeEvent();
		}
		if (!diff.isIdentity()) {
			fireDiff(diff);
		}
		onRoundtrip = true;
		markAsDirty();
	}

	private void fireBlur() {
		fireEvent(new BlurEvent(this));
	}

	private void fireDiff(ServerSideDocDiff diff) {
		fireEvent(new DiffEvent(this, diff));
	}

	private void fireFocus() {
		fireEvent(new FocusEvent(this));
	}

	private void fireSelectionChanged() {
		fireEvent(new SelectionChangeEvent(this));
	}

	private void fireTextChangeEvent() {
		if (!isFiringTextChangeEvent) {
			isFiringTextChangeEvent = true;
			try {
				fireEvent(new TextChangeEventImpl(this, getValue(), selection));
			} finally {
				isFiringTextChangeEvent = false;
			}
		}
	}

	private String newMarkerId() {
		return "m" + (++latestMarkerId);
	}

	private void selectionFromClient(TransportRange sel) {
		TextRange newSel = new TextRange(doc.getText(),
				AceRange.fromTransport(sel));
		if (newSel.equals(selection)) {
			return;
		}
		setInternalSelection(newSel);
		fireSelectionChanged();
	}

	private void setAceConfig(String key, String value) {
		getState().config.put(key, value);
	}

	private void setInternalSelection(TextRange selection) {
		this.selection = selection;
		getState().selection = selection.asTransport();
	}

	private void setSelectionToClient(Integer[] stc) {
		selectionToClient = stc;
		markAsDirty();
	}

}
