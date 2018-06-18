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

import java.util.List;

import com.haulmont.cuba.web.widgets.addons.aceeditor.SuggestionExtension;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceEditorWidget.SelectionChangeListener;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.SuggestPopup.SuggestionSelectedListener;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceKeyboardEvent;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceKeyboardHandler;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/*
 * When a user requests suggestions an invisible marker is created at the cursor position
 * and a SuggestPopup is shown. When the user types while suggesting,
 * the invisible marker auto-adjusts to contain what's typed.
 * (This takes advantage of how AceEditorWidget.moveMarkerOnInsert happens
 * to be implemented. It's bit of a mess...)
 * 
 * When a suggestion is selected what's inside of the invisible marker is deleted
 * before applying the suggestion.
 * 
 * 
 */
@SuppressWarnings("serial")
@Connect(SuggestionExtension.class)
public class SuggesterConnector extends AbstractExtensionConnector implements
		GwtAceKeyboardHandler, SuggestionSelectedListener, SelectionChangeListener {

	protected static final int Y_OFFSET = 20;

//	private final Logger logger = Logger.getLogger(SuggesterConnector.class.getName());

    protected AceEditorConnector connector;
    protected AceEditorWidget widget;

    protected SuggesterServerRpc serverRpc = RpcProxy.create(
			SuggesterServerRpc.class, this);

	protected String suggStartText;
    protected AceRange suggStartCursor;
	
	private SuggesterClientRpc clientRpc = new SuggesterClientRpc() {
		@Override
		public void showSuggestions(List<TransportSuggestion> suggs) {
			setSuggs(suggs);
		}

		@Override
		public void applySuggestionDiff(TransportDiff td) {
			stopSuggesting();
			ClientSideDocDiff diff = ClientSideDocDiff.fromTransportDiff(td);
			widget.setTextAndAdjust(diff.applyTo(widget.getDoc()).getText());
			widget.fireTextChanged(); // XXX we need to do this here to alert AceEditorConnector...
		}
	};

	protected boolean suggesting = false;

	protected SuggestPopup popup;

	protected Integer suggestionStartId;

	protected boolean startSuggestingOnNextSelectionChange;

	protected boolean suggestOnDot = true;

    protected boolean showDescriptions = true;

	public SuggesterConnector() {
		registerRpc(SuggesterClientRpc.class, clientRpc);
	}
	
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		this.suggestOnDot = getState().suggestOnDot;
        this.showDescriptions = getState().showDescriptions;
	}
	
	@Override
	public SuggesterState getState() {
		return (SuggesterState) super.getState();
	}

	protected void setSuggs(List<TransportSuggestion> suggs) {
		if (suggesting) {
			popup.setSuggestions(suggs);
		}
	}

	protected SuggestPopup createSuggestionPopup() {
		SuggestPopup sp = new SuggestPopup();
		sp.setOwner(widget);
		updatePopupPosition(sp);
		sp.setSuggestionSelectedListener(this);
		sp.show();
		return sp;
	}

	@Override
	protected void extend(ServerConnector target) {
		connector = (AceEditorConnector) target;
		widget = connector.getWidget();
		widget.setKeyboardHandler(this);
	}

	@Override
	public Command handleKeyboard(JavaScriptObject data, int hashId,
			String keyString, int keyCode, GwtAceKeyboardEvent e) {
		if (suggesting) {
			return keyPressWhileSuggesting(keyCode);
		}
		if (e == null) {
			return Command.DEFAULT;
		}
//		logger.info("handleKeyboard(" + data + ", " + hashId + ", " + keyString
//				+ ", " + keyCode + ", " + e.getKeyCode() + "---"
//				+ e.isCtrlKey() + ")");

		if (keyCode == 32 && e.isCtrlKey()) {
			startSuggesting();
			return Command.NULL;
		} else if (suggestOnDot && ".".equals(keyString)) {
			startSuggestingOnNextSelectionChange = true;
			widget.addSelectionChangeListener(this);
			return Command.DEFAULT;
		}

		return Command.DEFAULT;
	}

	protected void startSuggesting() {
        // ensure valid value of component on server before suggesting
        connector.sendToServerImmediately();

		AceRange sel = widget.getSelection();

		suggStartText = widget.getText();
		suggStartCursor = new AceRange(sel.getEndRow(), sel.getEndCol(), sel.getEndRow(), sel.getEndCol());
		serverRpc.suggest(suggStartText, suggStartCursor.asTransport());

		suggestionStartId = widget.addInvisibleMarker(suggStartCursor);
		widget.addSelectionChangeListener(this);
		popup = createSuggestionPopup();
        popup.showDescriptions = this.showDescriptions;
		suggesting = true;
	}

	@Override
	public void suggestionSelected(TransportSuggestion s) {
		// ???
		//connector.setOnRoundtrip(true);
//		AceRange suggMarker = widget.getInvisibleMarker(suggestionStartId);
		serverRpc.suggestionSelected(s.index);
		stopAskingForSuggestions();
	}

	@Override
	public void noSuggestionSelected() {
		stopAskingForSuggestions();
	}

	protected void stopAskingForSuggestions() {
		widget.removeSelectionChangeListener(this);
		suggesting = false;
		widget.setFocus(true);
	}
	
	protected void stopSuggesting() {
		if (popup!=null) {
			popup.hide();
			popup = null;
		}
		if (suggestionStartId != null) {
			widget.removeContentsOfInvisibleMarker(suggestionStartId);
			widget.removeInvisibleMarker(suggestionStartId);
		}
	}

	protected Command keyPressWhileSuggesting(int keyCode) {
		if (keyCode == 38 /* UP */) {
			popup.up();
		} else if (keyCode == 40 /* DOWN */) {
			popup.down();
		} else if (keyCode == 13 /* ENTER */) {
			popup.select();
		} else if (keyCode == 27 /* ESC */) {
			popup.close();
		} else {
			return Command.DEFAULT;
		}
		return Command.NULL;
	}

	protected String getWord(String text, int row, int col1, int col2) {
		if (col1 == col2) {
			return "";
		}
		String[] lines = text.split("\n", -1);
		int start = Util.cursorPosFromLineCol(lines, row, col1, 0);
		int end = Util.cursorPosFromLineCol(lines, row, col2, 0);
		return text.substring(start, end);
	}

	@Override
	public void selectionChanged() {
		if (startSuggestingOnNextSelectionChange) {
			widget.removeSelectionChangeListener(this);
			startSuggesting();
			startSuggestingOnNextSelectionChange = false;
			return;
		}
		
		AceRange sel = widget.getSelection();
		
		AceRange sug = widget.getInvisibleMarker(suggestionStartId);
		if (sug.getStartRow()!=sug.getEndRow()) {
			popup.close();
		}
		else if (sel.getEndRow() != sug.getStartRow() || sel.getEndRow() != sug.getEndRow()) {
			popup.close();
		} else if (sel.getEndCol()<sug.getStartCol() || sel.getEndCol()>sug.getEndCol()) {
			popup.close();
		} else {
			updatePopupPosition(popup);
			String s = getWord(widget.getText(), sug.getEndRow(),
					sug.getStartCol(), sug.getEndCol());
			popup.setStartOfValue(s);
		}
	}

	protected void updatePopupPosition(SuggestPopup popup) {
		int[] coords = widget.getCursorCoords();
		int sx = Window.getScrollLeft();
		int sy = Window.getScrollTop();
		int x = coords[0] - sx;
		int y = coords[1] - sy + Y_OFFSET;
		/*
		int wx = Window.getClientWidth();
		int wy = Window.getClientHeight();
		int maxx = wx - SuggestPopup.WIDTH - (showDescriptions ? SuggestPopup.DESCRIPTION_WIDTH : 0);
		if (x > maxx) {
			x -= SuggestPopup.WIDTH + (showDescriptions ? SuggestPopup.DESCRIPTION_WIDTH : 0) + 50;
		}
		int maxy = wy - SuggestPopup.HEIGHT;
		if (y > maxy) {
			y -= SuggestPopup.HEIGHT + 50;
		}
		*/
		popup.setPopupPosition(x, y);
	}
}