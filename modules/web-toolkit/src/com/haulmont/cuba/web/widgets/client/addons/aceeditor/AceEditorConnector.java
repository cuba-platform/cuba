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

import java.util.Map;
import java.util.Map.Entry;

import com.haulmont.cuba.web.widgets.addons.aceeditor.AceEditor;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceEditorWidget.FocusChangeListener;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceEditorWidget.SelectionChangeListener;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceEditorWidget.TextChangeListener;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.gwt.GwtAceEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ComputedStyle;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractHasComponentsConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(AceEditor.class)
public class AceEditorConnector extends AbstractHasComponentsConnector
		implements TextChangeListener, SelectionChangeListener, FocusChangeListener {

//	private static Logger logger = Logger.getLogger(AceEditorConnector.class.getName());

    protected AceEditorServerRpc serverRpc =
            RpcProxy.create(AceEditorServerRpc.class, this);


    protected enum TextChangeEventMode {
		EAGER, TIMEOUT, LAZY
	}

    protected TextChangeEventMode changeMode = null;
    protected int changeTimeout = 400;

    protected class SendTimer extends Timer {
		private boolean scheduled;
		private SendCond send = SendCond.NO;
		
		public void schedule(int ms, SendCond send) {
			super.schedule(ms);
			this.send = this.send.or(send);
			scheduled = true;
		}

		public void scheduleIfNotAlready(int ms, SendCond send) {
			if (!scheduled) {
				schedule(ms,send);
			}
		}

		@Override
		public void run() {
			scheduled = false;
			sendToServerImmediately(send);
			send = SendCond.NO;
		}
		
		@Override
		public void cancel() {
			super.cancel();
			send = SendCond.NO;
		}
	}

	protected SendTimer sendTimer = null;

    protected AceDoc shadow;

    protected boolean onRoundtrip = false;
    
    protected enum SendCond {
    	NO, IF_CHANGED, ALWAYS;
		public SendCond or(SendCond sw2) {
			return this.ordinal() > sw2.ordinal() ? this : sw2;
		}
    }
    
    protected SendCond sendAfterRoundtrip = SendCond.NO;

    protected AceEditorClientRpc clientRpc = new AceEditorClientRpc() {
		@Override
		public void diff(TransportDiff ad) {
//			VConsole.log("diff!!!");
			ClientSideDocDiff diff = ClientSideDocDiff.fromTransportDiff(ad);
			shadow = diff.applyTo(shadow);
			
			AceDoc doc1 = getWidget().getDoc();
			AceDoc doc2 = diff.applyTo(doc1);

			getWidget().setDoc(doc2);

			if (selectionAfterApplyingDiff!=null) {
				getWidget().setSelection(selectionAfterApplyingDiff);
				selectionAfterApplyingDiff = null;
			}

			if (scrollToRowAfterApplyingDiff != -1) {
				getWidget().scrollToRow(scrollToRowAfterApplyingDiff);
				scrollToRowAfterApplyingDiff = -1;
			}
			
			if (!doc1.getText().equals(doc2.getText())) {
				sendAfterRoundtrip = sendAfterRoundtrip.or(SendCond.ALWAYS);
			}
			setOnRoundtrip(false);
		}

		@Override
		public void changedOnServer() {
			if (!isOnRoundtrip()) {
				sendToServer(SendCond.ALWAYS, true);
			}
			// else ? should we send after roundtrip or not?
		}

	};

    protected boolean listenToSelectionChanges;
    protected boolean listenToFocusChanges;

	// When setting selection or scrollToRow, we must make
	// sure that the text value is set before that.
	// That is, we must make the diff sync roundtrip and set
	// these things after that.
	// That's why this complication.
	// TODO: this may not be the cleanest way to do it...
	protected int scrollToRowAfterApplyingDiff = -1;
	protected AceRange selectionAfterApplyingDiff;

	public AceEditorConnector() {
		super();
		registerRpc(AceEditorClientRpc.class, clientRpc);
	}
	
	@Override
	public void init() {
		super.init();
		
		// Needed if inside a resizable subwindow.
		// Should we check that and only listen if yes?
		getLayoutManager().addElementResizeListener(getWidget().getElement(), new ElementResizeListener() {
			@Override
			public void onElementResize(ElementResizeEvent e) {
                getWidget().resize();
			}
		});
	}
	
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		setTextChangeEventMode(getState().changeMode);
		setTextChangeTimeout(getState().changeTimeout);
		
		ClientSideDocDiff.dmp.setDiff_EditCost(getState().diff_editCost);

		// TODO: are these needed?
//		widget.setHideErrors(getState().hideErrors);
//		widget.setRequired(getState().required);
//		widget.setModified(getState().modified);
		
		boolean firstTime = !getWidget().isInitialized();
		if (firstTime) {
			// To make sure Ace config is applied before the editor is created,
			// we delay the initialization till then first call to onStateChanged,
			// not initializing in createWidget() right away.
			applyConfig(getState().config);
            getWidget().initialize();
		}

        getWidget().setMode(getState().mode);
        getWidget().setTheme(getState().theme);
		listenToSelectionChanges = getState().listenToSelectionChanges;
		listenToFocusChanges = getState().listenToFocusChanges;
        getWidget().setUseWorker(getState().useWorker);
        getWidget().setWordwrap(getState().wordwrap);

        getWidget().setShowGutter(getState().showGutter);
        getWidget().setShowPrintMargin(getState().showPrintMargin);
        getWidget().setHighlightActiveLineEnabled(getState().highlightActiveLine);

        getWidget().setEnabled(getState().enabled);
//        getWidget().setPropertyReadOnly(getState().propertyReadOnly);
        getWidget().setTabIndex(getState().tabIndex);
        getWidget().setReadOnly(getState().readOnly);

        if (stateChangeEvent.hasPropertyChanged("fontSize")) {
            String fontSize = getState().fontSize;

            if ("auto".equals(fontSize)) {
                // detect font size from CSS
                Element fontSizeMeasureElement = Document.get().createDivElement();
                fontSizeMeasureElement.setClassName("ace_editor");
                fontSizeMeasureElement.getStyle().setPosition(Style.Position.FIXED);
                fontSizeMeasureElement.getStyle().setVisibility(Style.Visibility.HIDDEN);
                getWidget().getElement().appendChild(fontSizeMeasureElement);

                ComputedStyle cs = new ComputedStyle(fontSizeMeasureElement);
                fontSize = cs.getProperty("fontSize");

                getWidget().getElement().removeChild(fontSizeMeasureElement);
            }

            getWidget().setFontSize(fontSize);
        }

        getWidget().setHighlightSelectedWord(getState().highlightSelectedWord);
        getWidget().setShowInvisibles(getState().showInvisibles);
        getWidget().setDisplayIndentGuides(getState().displayIndentGuides);

        getWidget().setUseSoftTabs(getState().softTabs);
        getWidget().setTabSize(getState().tabSize);
		
		// TODO: How should we deal with immediateness. Since there's already textChangeEventMode...
		//immediate = getState().immediate;
		
		if (firstTime) {
			shadow = AceDoc.fromTransport(getState().initialValue);
            getWidget().setDoc(shadow);
		}
		
		if (getState().selection != null) {
			AceRange sel = AceRange.fromTransport(getState().selection);
			if (firstTime) {
				getWidget().setSelection(sel);
			}
			else {
				selectionAfterApplyingDiff = sel;
			}
		}
		
		if (getState().scrollToRow != -1) {
			if (firstTime) {
				getWidget().scrollToRow(getState().scrollToRow);
			}
			else {
				scrollToRowAfterApplyingDiff = getState().scrollToRow;
			}
		}
	}
	
	protected static void applyConfig(Map<String, String> config) {
		for (Entry<String, String> e : config.entrySet()) {
			GwtAceEditor.setAceConfig(e.getKey(), e.getValue());
		}
	}

	@Override
	protected Widget createWidget() {
        AceEditorWidget widget = GWT.create(AceEditorWidget.class);
		widget.addTextChangeListener(this);
		widget.addSelectionChangeListener(this);
		widget.setFocusChangeListener(this);
		return widget;
	}

	@Override
	public AceEditorWidget getWidget() {
		return (AceEditorWidget) super.getWidget();
	}

	@Override
	public AceEditorState getState() {
		return (AceEditorState) super.getState();
	}
	
	@Override
	public void focusChanged(boolean focused) {
		// TODO: it'd be better if we didn't register as listener
		// if !listenToFocusChanges in the first place...
		if (!listenToFocusChanges) {
			return;
		}
		
		if (isOnRoundtrip()) {
			sendAfterRoundtrip = SendCond.ALWAYS;
		}
		else {
			sendToServerImmediately(SendCond.ALWAYS);
		}
	}
	
	public void setTextChangeEventMode(String mode) {
		TextChangeEventMode newMode = TextChangeEventMode.valueOf(mode);
		if (newMode!=changeMode) {
			changeTextChangeEventMode(newMode);
		}
	}

    protected void setTextChangeTimeout(int timeout) {
		changeTimeout = timeout;
	}

    protected void changeTextChangeEventMode(TextChangeEventMode newMode) {
		if (sendTimer != null) {
			sendTimer.cancel();
			sendTimer = null;
		}
		this.changeMode = newMode;
	}
    
    protected void sendChangeAccordingToMode(SendCond send) {
    	sendChangeAccordingToMode(send, changeMode);
    }
	
	protected void sendChangeAccordingToMode(SendCond send, TextChangeEventMode mode) {
		if (mode == TextChangeEventMode.EAGER) {
			if (sendTimer != null) {
				sendTimer.cancel();
			}
			sendToServerImmediately(send);
		} else if (mode == TextChangeEventMode.LAZY) {
			if (sendTimer == null) {
				sendTimer = new SendTimer();
			}
			sendTimer.schedule(changeTimeout, send);
		} else if (mode == TextChangeEventMode.TIMEOUT) {
			if (sendTimer == null) {
				sendTimer = new SendTimer();
			}
			sendTimer.scheduleIfNotAlready(changeTimeout, send);
		}
	}

    protected void sendToServer(SendCond send, boolean immediately) {
//    	VConsole.log("sendToServer: send=" + send + ", immediately="+immediately);
    	if (send==SendCond.NO) {
    		return;
    	}
    	
		AceDoc doc = getWidget().getDoc();
		ClientSideDocDiff diff = ClientSideDocDiff.diff(shadow, doc);
		if (send==SendCond.ALWAYS) {
			// Go on...
		}
		else if (send==SendCond.IF_CHANGED && !diff.isIdentity()) {
			// Go on...
		}
		else {
			return;
		}
		
		TransportDiff td = diff.asTransport();
		
		if (immediately) {
			serverRpc.changed(td, getWidget().getSelection().asTransport(), getWidget().isFocused());
		} else {
			serverRpc.changedDelayed(td, getWidget().getSelection().asTransport(), getWidget().isFocused());
		}
		
		shadow = doc;
		setOnRoundtrip(true); // What if delayed???
		sendAfterRoundtrip = SendCond.NO;
	}

    protected void sendToServerDelayed(SendCond send) {
		sendToServer(send, false);
	}
    
    public void sendToServerImmediately() {
    	sendToServerImmediately(SendCond.ALWAYS);
    }

    protected void sendToServerImmediately(SendCond send) {
		sendToServer(send, true);
	}
	
	@Override
	public void flush() {
		super.flush();
		sendWhenPossible(SendCond.ALWAYS, TextChangeEventMode.EAGER); // ???
	}

	@Override
	public void changed() {
		if (isOnRoundtrip()) {
			sendAfterRoundtrip = sendAfterRoundtrip.or(SendCond.IF_CHANGED);
		}
		else {
			sendChangeAccordingToMode(SendCond.IF_CHANGED);
		}
	}

	@Override
	public void updateCaption(ComponentConnector connector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectorHierarchyChange(
			ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectionChanged() {
		// TODO: it'd be better if we didn't register as listener
		// if !listenToSelectionChanges in the first place...
		if (listenToSelectionChanges) {
			sendWhenPossible(SendCond.ALWAYS);
		}
	}
	
	protected void sendWhenPossible(SendCond send) {
		if (isOnRoundtrip()) {
			sendAfterRoundtrip = sendAfterRoundtrip.or(send);
		}
		else {
			sendChangeAccordingToMode(send);
		}
	}
	
	protected void sendWhenPossible(SendCond send, TextChangeEventMode mode) {
		if (isOnRoundtrip()) {
			sendAfterRoundtrip = sendAfterRoundtrip.or(send);
		}
		else {
			sendChangeAccordingToMode(send, mode);
		}
	}

	// TODO XXX not sure if this roundtrip thing is correct, seems to work ok...
	private void setOnRoundtrip(boolean on) {
		if (on==onRoundtrip) {
			return;
		}
		onRoundtrip = on;
		if (!onRoundtrip) {
			sendToServerImmediately(sendAfterRoundtrip);
		}
	}
	
	public boolean isOnRoundtrip() {
		return onRoundtrip;
	}
}