package com.haulmont.cuba.web.widgets.addons.aceeditor;

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportSuggestion;

/**
 * A single suggestion.
 * 
 * Feel free to subclass.
 */
public class Suggestion {

	protected final String displayText;
	protected final String descriptionText;
	protected final String suggestionText;
	protected final int startPosition;
	protected final int endPosition;

	/**
	 * 
	 * @param displayText
	 *            the text shown in the popup list
	 * @param descriptionText
	 *            a longer description
	 */
	public Suggestion(String displayText,
			String descriptionText) {
		this(displayText, descriptionText, "");
	}
	
	/**
	 * 
	 * If suggestionText is "cat", the suggestion popup will stay there
	 * if user types "c" "ca" or "cat".
	 * 
	 * @param displayText
	 *            the text shown in the popup list
	 * @param descriptionText
	 *            a longer description
	 * @param suggestionText
	 */
	public Suggestion(String displayText,
			String descriptionText, String suggestionText) {
		this.displayText = displayText;
		this.descriptionText = descriptionText;
		this.suggestionText = suggestionText;
		this.startPosition = -1;
		this.endPosition = -1;
	}

	public Suggestion(String displayText, String descriptionText, String suggestionText,
					  int startPosition, int endPosition) {
		this.displayText = displayText;
		this.descriptionText = descriptionText;
		this.suggestionText = suggestionText;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}
	
	public TransportSuggestion asTransport(int index) {
		TransportSuggestion ts = new TransportSuggestion();
		ts.displayText = displayText;
		ts.descriptionText = descriptionText;
		ts.suggestionText = suggestionText;
		ts.index = index;
		return ts;
	}

	public String getDisplayText() {
		return displayText;
	}

	public String getDescriptionText() {
		return descriptionText;
	}

	public String getSuggestionText() {
		return suggestionText;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}
}
