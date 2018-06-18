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

import java.util.LinkedList;
import java.util.List;

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceDoc;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceRange;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.SuggesterClientRpc;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.SuggesterServerRpc;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.SuggesterState;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportRange;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportSuggestion;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.AbstractExtension;

/**
 * Extends {@link AceEditor} with suggestion possibility.
 * 
 * By default Ctrl+Space and dot (".") triggers a suggester.
 * 
 * A {@link Suggester} is queried for {@link Suggestion}s.
 * 
 */
@SuppressWarnings("serial")
public class SuggestionExtension extends AbstractExtension {

	protected Suggester suggester;

    protected String suggStartText;
    protected int suggStartCursor;
    protected List<Suggestion> suggestions;
    protected AceRange suggRange;

	public SuggestionExtension(Suggester suggester) {
		this.suggester = suggester;
	}

	protected SuggesterServerRpc serverRpc = new SuggesterServerRpc() {

		@Override
		public void suggest(String text, TransportRange sel) {
			suggStartText = text;
			suggRange = AceRange.fromTransport(sel);
			suggStartCursor = new TextRange(text, suggRange).getEnd();
			suggestions = suggester.getSuggestions(text, suggStartCursor);
			getRpcProxy(SuggesterClientRpc.class).showSuggestions(
					asTransport(suggestions));
		}

		@Override
		public void suggestionSelected(int index) {

			Suggestion sugg = suggestions.get(index);
			String text2 = suggester.applySuggestion(sugg, suggStartText,
					suggStartCursor);
			// XXX too much work
			AceDoc doc1 = new AceDoc(suggStartText);
			AceDoc doc2 = new AceDoc(text2);
			ServerSideDocDiff diff = ServerSideDocDiff.diff(doc1, doc2);
			getRpcProxy(SuggesterClientRpc.class).applySuggestionDiff(
					diff.asTransport());
		}
	};

	@Override
	public SuggesterState getState() {
		return (SuggesterState) super.getState();
	}

    @Override
    protected SuggesterState getState(boolean markAsDirty) {
        return (SuggesterState) super.getState(markAsDirty);
    }

    protected List<TransportSuggestion> asTransport(List<Suggestion> suggs) {
		LinkedList<TransportSuggestion> tl = new LinkedList<TransportSuggestion>();
		int i = 0;
		for (Suggestion s : suggs) {
			tl.add(s.asTransport(i++));
		}
		return tl;
	}

	public void setSuggestOnDot(boolean on) {
		getState().suggestOnDot = on;
	}

	public void extend(AceEditor editor) {
		super.extend(editor);
		registerRpc(serverRpc);
	}

    public void setShowDescriptions(boolean showDescriptions) {
        getState().showDescriptions = showDescriptions;
    }

    public boolean isShowDescriptions() {
        return getState(false).showDescriptions;
    }
}