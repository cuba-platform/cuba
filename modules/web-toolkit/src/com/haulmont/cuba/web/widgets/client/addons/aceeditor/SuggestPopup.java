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

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.vaadin.client.ui.VOverlay;

public class SuggestPopup extends VOverlay implements KeyDownHandler,
		DoubleClickHandler, ChangeHandler {
	protected ListBox choiceList;

    protected String startOfValue = "";

	public interface SuggestionSelectedListener {
		void suggestionSelected(TransportSuggestion s);
		void noSuggestionSelected();
	}

    protected SuggestionSelectedListener listener;

    protected VOverlay descriptionPopup;

    protected List<TransportSuggestion> suggs;
    protected List<TransportSuggestion> visibleSuggs = new LinkedList<TransportSuggestion>();

    protected boolean showDescriptions = true;

    protected Image loadingImage;

	public static final int WIDTH = 150;
	public static final int HEIGHT = 200;

	public static final int DESCRIPTION_WIDTH = 225;

	// TODO addSuggestionSelectedListener?
	public void setSuggestionSelectedListener(SuggestionSelectedListener ssl) {
		listener = ssl;
	}

	public SuggestPopup() {
		super(true);
		setWidth(WIDTH + "px");
		SuggestionResources resources = GWT.create(SuggestionResources.class);
		loadingImage = new Image(resources.loading());
		setWidget(loadingImage);
	}
	
	protected void createChoiceList() {
		choiceList = new ListBox();
		choiceList.setStyleName("list");
		choiceList.addKeyDownHandler(this);
		choiceList.addDoubleClickHandler(this);
		choiceList.addChangeHandler(this);
		choiceList.setStylePrimaryName("aceeditor-suggestpopup-list");
		setWidget(choiceList);
	}
	
	protected void startLoading() {
		if (descriptionPopup!=null) {
			descriptionPopup.hide();
		}
		setWidget(loadingImage);
	}
	
	public void setSuggestions(List<TransportSuggestion> suggs) {
		this.suggs = suggs;
		createChoiceList();
		populateList();
		if (choiceList.getItemCount() == 0) {
			close();
		}
	}

    protected void populateList() {
		choiceList.clear();
		visibleSuggs.clear();
		int i = 0;
		for (TransportSuggestion s : suggs) {
			if (s.suggestionText.toLowerCase().startsWith(startOfValue)) {
				visibleSuggs.add(s);
				choiceList.addItem(s.displayText, "" + i);
			}
			i++;
		}
		if (choiceList.getItemCount() > 0) {
			int vic = Math.max(2, Math.min(10, choiceList.getItemCount()));
			choiceList.setVisibleItemCount(vic);
			choiceList.setSelectedIndex(0);
			this.onChange(null);
		}
	}

	public void close() {
		hide();
		if (listener != null)
			listener.noSuggestionSelected();
	}

	@Override
	public void hide() {
		super.hide();
		if (descriptionPopup != null)
			descriptionPopup.hide();
		descriptionPopup = null;
	}

	@Override
	public void hide(boolean ac) {
		super.hide(ac);
		if (ac) {
			// This happens when user clicks outside this popup (or something
			// similar) while autohide is on. We must cancel the suggestion.
			if (listener != null)
				listener.noSuggestionSelected();
		}
		if (descriptionPopup != null)
			descriptionPopup.hide();
		descriptionPopup = null;
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		int keyCode = event.getNativeKeyCode();
		if (keyCode == KeyCodes.KEY_ENTER
				&& choiceList.getSelectedIndex() != -1) {
			event.preventDefault();
			event.stopPropagation();
			select();
		} else if (keyCode == KeyCodes.KEY_ESCAPE) {
			event.preventDefault();
			close();
		}
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		event.preventDefault();
		event.stopPropagation();
		select();
	}

	@Override
	public void onBrowserEvent(Event event) {
		if (event.getTypeInt() == Event.ONCONTEXTMENU) {
			event.stopPropagation();
			event.preventDefault();
			return;
		}
		super.onBrowserEvent(event);
	}

	public void up() {
		if (suggs==null) {
			return;
		}
		int current = this.choiceList.getSelectedIndex();
		int next = (current - 1 >= 0) ? current - 1 : 0;
		this.choiceList.setSelectedIndex(next);
		// Note that setting the selection programmatically does not cause the
		// ChangeHandler.onChange(ChangeEvent) event to be fired.
		// Doing it manually.
		this.onChange(null);
	}

	public void down() {
		if (suggs==null) {
			return;
		}
		int current = this.choiceList.getSelectedIndex();
		int next = (current + 1 < choiceList.getItemCount()) ? current + 1
				: current;
		this.choiceList.setSelectedIndex(next);
		// Note that setting the selection programmatically does not cause the
		// ChangeHandler.onChange(ChangeEvent) event to be fired.
		// Doing it manually.
		this.onChange(null);
	}

	public void select() {
		if (suggs==null) {
			return;
		}
		
		int selected = choiceList.getSelectedIndex();
		if (listener != null) {
			if (selected == -1) {
				this.hide();
				listener.noSuggestionSelected();
			} else {
				startLoading();
				listener.suggestionSelected(visibleSuggs.get(selected));
			}
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		if (descriptionPopup == null) {
			createDescriptionPopup();
		}

		int selected = choiceList.getSelectedIndex();
		String descr = visibleSuggs.get(selected).descriptionText;

		if (descr != null && !descr.isEmpty()) {
			((HTML) descriptionPopup.getWidget()).setHTML(descr);
            if (showDescriptions) {
                descriptionPopup.show();
            }
        } else {
			descriptionPopup.hide();
		}
	}

	@Override
	public void setPopupPosition(int left, int top) {
		super.setPopupPosition(left, top);
		if (descriptionPopup!=null) {
			updateDescriptionPopupPosition();
		}
	}
	
	protected void updateDescriptionPopupPosition() {
		int x = getAbsoluteLeft() + WIDTH;
		int y = getAbsoluteTop();

		if (descriptionPopup != null) {
			descriptionPopup.setPopupPosition(x, y);
		}
	}

	protected void createDescriptionPopup() {
		descriptionPopup = new VOverlay();
		descriptionPopup.setOwner(getOwner());
		descriptionPopup.setStylePrimaryName("aceeditor-suggestpopup-description");
		HTML lbl = new HTML();
		lbl.setWordWrap(true);
		descriptionPopup.setWidget(lbl);
		updateDescriptionPopupPosition();
		descriptionPopup.setWidth(DESCRIPTION_WIDTH + "px");
	}

	public void setStartOfValue(String startOfValue) {
		this.startOfValue = startOfValue.toLowerCase();
		if (suggs==null) {
			return;
		}
		populateList();
		if (choiceList.getItemCount() == 0) {
			close();
		}
	}
}