/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.widgets;

import com.google.common.base.Strings;
import com.haulmont.cuba.web.widgets.client.suggestionfield.CubaSuggestionFieldClientRpc;
import com.haulmont.cuba.web.widgets.client.suggestionfield.CubaSuggestionFieldServerRpc;
import com.haulmont.cuba.web.widgets.client.suggestionfield.CubaSuggestionFieldState;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.SizeWithUnit;
import com.vaadin.ui.AbstractField;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.function.Function;

public class CubaSuggestionField<T> extends AbstractField<T> {
    protected static final String SUGGESTION_ID = "id";
    protected static final String SUGGESTION_CAPTION = "caption";
    protected static final String SUGGESTION_STYLE_NAME = "styleName";

    protected Consumer<String> searchExecutor;
    protected Consumer<String> enterActionHandler;
    protected Consumer<String> arrowDownActionHandler;
    protected Runnable cancelSearchHandler;

    protected KeyMapper<T> keyMapper = new KeyMapper<>();

    protected com.vaadin.event.FieldEvents.FocusAndBlurServerRpcImpl focusBlurRpc;
    protected CubaSuggestionFieldServerRpc serverRpc;

    protected Function<T, String> textViewConverter;
    protected int suggestionsLimit = 10;
    protected Function<Object, String> optionsStyleProvider;

    protected T internalValue;

    public CubaSuggestionField() {
        serverRpc = new CubaSuggestionFieldServerRpc() {
            @Override
            public void searchSuggestions(String query) {
                if (searchExecutor != null) {
                    searchExecutor.accept(query);
                }
            }

            @Override
            public void selectSuggestion(String suggestionId) {
                T suggestion = keyMapper.get(suggestionId);
                setValue(suggestion, true);

                updateTextPresentation(getValue());
            }

            @Override
            public void onEnterKeyPressed(String currentSearchString) {
                if (enterActionHandler != null) {
                    enterActionHandler.accept(currentSearchString);
                }
            }

            @Override
            public void onArrowDownKeyPressed(String currentSearchString) {
                if (arrowDownActionHandler != null) {
                    arrowDownActionHandler.accept(currentSearchString);
                }
            }

            @Override
            public void cancelSearch() {
                cancelSearchHandler.run();
            }
        };
        registerRpc(serverRpc);

        focusBlurRpc = new com.vaadin.event.FieldEvents.FocusAndBlurServerRpcImpl(this) {
            private static final long serialVersionUID = -780524775769549747L;

            @Override
            protected void fireEvent(Event event) {
                CubaSuggestionField.this.fireEvent(event);
            }
        };
        registerRpc(focusBlurRpc);
    }

    @Override
    protected void doSetValue(T value) {
        this.internalValue = value;
        updateTextPresentation(value);
    }

    @Override
    public T getValue() {
        return internalValue;
    }

    public void updateTextPresentation(T value) {
        String stringValue = textViewConverter.apply(value);

        if (!Objects.equals(getState(false).text, stringValue)) {
            getState().text = stringValue;
        }
    }

    public void setTextViewConverter(Function<T, String> converter) {
        this.textViewConverter = converter;
    }

    public int getAsyncSearchDelayMs() {
        return getState(false).asyncSearchDelayMs;
    }

    public void setAsyncSearchDelayMs(int asyncSearchDelayMs) {
        if (getState(false).asyncSearchDelayMs != asyncSearchDelayMs) {
            getState().asyncSearchDelayMs = asyncSearchDelayMs;
        }
    }

    public void setEnterActionHandler(Consumer<String> enterActionHandler) {
        this.enterActionHandler = enterActionHandler;
    }

    public void setArrowDownActionHandler(Consumer<String> arrowDownActionHandler) {
        this.arrowDownActionHandler = arrowDownActionHandler;
    }

    public int getMinSearchStringLength() {
        return getState(false).minSearchStringLength;
    }

    public void setMinSearchStringLength(int minSearchStringLength) {
        if (getState(false).minSearchStringLength != minSearchStringLength) {
            getState().minSearchStringLength = minSearchStringLength;
        }
    }

    public void setSearchExecutor(Consumer<String> searchExecutor) {
        this.searchExecutor = searchExecutor;
    }

    public void showSuggestions(List<T> suggestions, boolean userOriginated) {
        final JsonArray jsonArray = Json.createArray();
        for (int i = 0; i < suggestions.size() && i < suggestionsLimit; i++) {
            jsonArray.set(i, getJsonObject(suggestions.get(i)));
        }
        getRpcProxy(CubaSuggestionFieldClientRpc.class).showSuggestions(jsonArray, userOriginated);
    }

    public void setCancelSearchHandler(Runnable cancelSearchHandler) {
        this.cancelSearchHandler = cancelSearchHandler;
    }

    @Override
    protected CubaSuggestionFieldState getState() {
        return (CubaSuggestionFieldState) super.getState();
    }

    @Override
    protected CubaSuggestionFieldState getState(boolean markAsDirty) {
        return (CubaSuggestionFieldState) super.getState(markAsDirty);
    }

    private JsonObject getJsonObject(T suggestion) {
        final JsonObject object = Json.createObject();

        //noinspection unchecked
        object.put(SUGGESTION_ID, Json.create(keyMapper.key(suggestion)));

        String caption = textViewConverter.apply(suggestion);
        object.put(SUGGESTION_CAPTION, Json.create(caption));

        if (optionsStyleProvider != null) {
            String styleName = optionsStyleProvider.apply(suggestion);
            object.put(SUGGESTION_STYLE_NAME, Json.create(styleName));
        }

        return object;
    }

    public void setSuggestionsLimit(int suggestionsLimit) {
        this.suggestionsLimit = suggestionsLimit;
    }

    public int getSuggestionsLimit() {
        return suggestionsLimit;
    }

    public String getInputPrompt() {
        return getState(false).inputPrompt;
    }

    public void setInputPrompt(String inputPrompt) {
        if (!Objects.equals(inputPrompt, getState(false).inputPrompt)) {
            getState().inputPrompt = inputPrompt;
        }
    }

    // copied from com.vaadin.ui.AbstractComponent#setStyleName
    public void setPopupStyleName(String styleName) {
        if (Strings.isNullOrEmpty(styleName)) {
            getState().popupStylename = null;
            return;
        }

        if (getState().popupStylename == null) {
            getState().popupStylename = new ArrayList<>();
        }

        List<String> styles = getState().popupStylename;
        styles.clear();

        StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
        while (tokenizer.hasMoreTokens()) {
            styles.add(tokenizer.nextToken());
        }
    }

    // copied from com.vaadin.ui.AbstractComponent#addStyleName
    public void addPopupStyleName(String styleName) {
        if (Strings.isNullOrEmpty(styleName)) {
            return;
        }
        if (styleName.contains(" ")) {
            // Split space separated stylename names and add them one by one.
            StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
            while (tokenizer.hasMoreTokens()) {
                addPopupStyleName(tokenizer.nextToken());
            }
            return;
        }

        if (getState(false).popupStylename == null) {
            getState().popupStylename = new ArrayList<>();
        }
        List<String> styleNames = getState().popupStylename;
        if (!styleNames.contains(styleName)) {
            styleNames.add(styleName);
        }
    }

    // copied from com.vaadin.ui.AbstractComponent#removeStyleName
    public void removePopupStyleName(String styleName) {
        if (CollectionUtils.isNotEmpty(getState(false).popupStylename)) {
            StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
            while (tokenizer.hasMoreTokens()) {
                getState().popupStylename.remove(tokenizer.nextToken());
            }
        }
    }

    public void setPopupWidth(String popupWidth) {
        if (popupWidth == null || popupWidth.isEmpty()) {
            throw new IllegalArgumentException("Popup width cannot be empty");
        }

        if (Objects.equals(getState(false).popupWidth, popupWidth))
            return;

        if (isPredefinedPopupWidth(popupWidth)) {
            getState().popupWidth = popupWidth;
            return;
        }

        // try to parse to be sure that string is correct
        SizeWithUnit.parseStringSize(popupWidth);

        getState().popupWidth = popupWidth;
    }

    protected boolean isPredefinedPopupWidth(String popupWidth) {
        return "auto".equals(popupWidth) || "parent".equals(popupWidth);
    }

    public String getPopupWidth() {
        return getState(false).popupWidth;
    }

    public void setOptionsStyleProvider(@Nullable Function<Object, String> optionsStyleProvider) {
        this.optionsStyleProvider = optionsStyleProvider;
    }
}