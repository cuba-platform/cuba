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

import java.util.List;


public interface Suggester {
	
	/**
	 * Returns a list of {@link Suggestion}s based on text and cursor position.
	 * 
	 * @param text
	 * @param cursor
	 * @return list of {@link Suggestion}s, empty list = no suggestions
	 */
	public List<Suggestion> getSuggestions(String text, int cursor);
	
	/**
	 * Applies the suggestion to the text.
	 * 
	 * text and cursor are the same that were given to {@link #getSuggestions(String, int)} earlier.
	 * 
	 * sugg is one of the objects received from {@link #getSuggestions(String, int)}
	 * So if you gave a subclass of {@link Suggestion}, that you shall receive.
	 * 
	 * @param sugg 
	 * @param text
	 * @param cursor
	 * @return Text after the suggestion has been applied.
	 */
	// TODO: It might be nice to also specify a cursor position (or selection)
	// after applying a suggestion.
	public String applySuggestion(Suggestion sugg, String text, int cursor); 
}
