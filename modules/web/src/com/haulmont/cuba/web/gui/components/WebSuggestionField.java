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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.OptionsStyleProvider;
import com.haulmont.cuba.gui.components.SuggestionField;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.gui.components.converters.StringToEntityConverter;
import com.haulmont.cuba.web.widgets.CubaSuggestionField;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WebSuggestionField<V> extends WebAbstractField<CubaSuggestionField, V> implements SuggestionField<V> {

    private static final Logger log = LoggerFactory.getLogger(WebSuggestionField.class);

    protected BackgroundWorker backgroundWorker = AppBeans.get(BackgroundWorker.NAME);
    protected UserSession userSession = AppBeans.get(UserSession.class);
    protected BackgroundTaskHandler<List<?>> handler;

    protected SearchExecutor<?> searchExecutor;

    protected EnterActionHandler enterActionHandler;
    protected ArrowDownActionHandler arrowDownActionHandler;

    protected StringToEntityConverter entityConverter = new StringToEntityConverter();

    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
    protected OptionsStyleProvider optionsStyleProvider;

    public WebSuggestionField() {
        component = new CubaSuggestionField();

        component.setTextViewConverter(this::convertToTextView);

        component.setSearchExecutor(query -> {
            cancelSearch();
            searchSuggestions(query);
        });

        component.setCancelSearchHandler(this::cancelSearch);

        attachListener(component);
    }

    protected String convertToTextView(Object value) {
        if (value == null) {
            return StringUtils.EMPTY;
        }

        if (value instanceof Entity) {
            Entity entity = (Entity) value;
            if (captionMode == CaptionMode.ITEM) {
                return entityConverter.convertToPresentation(entity, String.class, userSession.getLocale());
            }

            if (StringUtils.isNotEmpty(captionProperty)) {
                MetaPropertyPath propertyPath = entity.getMetaClass().getPropertyPath(captionProperty);
                if (propertyPath == null) {
                    throw new IllegalArgumentException(String.format("Can't find property for given caption property: %s", captionProperty));
                }

                return metadataTools.format(entity.getValueEx(captionProperty), propertyPath.getMetaProperty());
            }

            log.warn("Using StringToEntityConverter to get entity text presentation. Caption property is not defined " +
                    "while caption mode is \"PROPERTY\"");
            return entityConverter.convertToPresentation(entity, String.class, userSession.getLocale());
        }

        return metadataTools.format(value);
    }

    @Override
    public V getValue() {
        V value = super.getValue();
        return value instanceof OptionWrapper
                ? (V) ((OptionWrapper) value).getValue()
                : value;
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;

        if (StringUtils.isNotEmpty(captionProperty)) {
            captionMode = CaptionMode.PROPERTY;
        } else {
            captionMode = CaptionMode.ITEM;
        }
    }

    protected void cancelSearch() {
        if (handler != null) {
            log.debug("Cancel previous search");

            handler.cancel();
            handler = null;
        }
    }

    protected void searchSuggestions(final String query) {
        BackgroundTask<Long, List<?>> task = getSearchSuggestionsTask(query);
        if (task != null) {
            handler = backgroundWorker.handle(task);
            handler.execute();
        }
    }

    protected BackgroundTask<Long, List<?>> getSearchSuggestionsTask(final String query) {
        if (this.searchExecutor == null)
            return null;

        final SearchExecutor<?> currentSearchExecutor = this.searchExecutor;

        Map<String, Object> params;
        if (currentSearchExecutor instanceof ParametrizedSearchExecutor) {
            params = ((ParametrizedSearchExecutor<?>) currentSearchExecutor).getParams();
        } else {
            params = Collections.emptyMap();
        }

        return new BackgroundTask<Long, List<?>>(0) {
            @Override
            public List<?> run(TaskLifeCycle<Long> taskLifeCycle) throws Exception {
                List<?> result;
                try {
                    // todo: remove after fixing #PLI-213
                    //noinspection ChangingGuiFromBackgroundTask
                    result = asyncSearch(currentSearchExecutor, query, params);
                } catch (RuntimeException e) {
                    log.error("Error in async search thread", e);

                    result = Collections.emptyList();
                }

                return result;
            }

            @Override
            public void done(List<?> result) {
                log.debug("Search results for '{}'", query);

                handleSearchResult(result);
            }

            @Override
            public void canceled() {
            }

            @Override
            public boolean handleException(Exception ex) {
                log.error("Error in async search thread", ex);
                return true;
            }
        };
    }

    protected List<?> asyncSearch(SearchExecutor<?> searchExecutor, String searchString,
                                       Map<String, Object> params) throws Exception {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        log.debug("Search '{}'", searchString);

        List<?> searchResultItems;
        if (searchExecutor instanceof ParametrizedSearchExecutor) {
            //noinspection unchecked
            ParametrizedSearchExecutor<?> pSearchExecutor = (ParametrizedSearchExecutor<?>) searchExecutor;
            searchResultItems = pSearchExecutor.search(searchString, params);
        } else {
            searchResultItems = searchExecutor.search(searchString, Collections.emptyMap());
        }

        return searchResultItems;
    }

    protected void handleSearchResult(List<?> results) {
        showSuggestions(results);
    }

    @Override
    public int getMinSearchStringLength() {
        return component.getMinSearchStringLength();
    }

    @Override
    public void setMinSearchStringLength(int minSearchStringLength) {
        component.setMinSearchStringLength(minSearchStringLength);
    }

    @Override
    public int getSuggestionsLimit() {
        return component.getSuggestionsLimit();
    }

    @Override
    public void setSuggestionsLimit(int suggestionsLimit) {
        component.setSuggestionsLimit(suggestionsLimit);
    }

    @Override
    @Deprecated
    public int getAsyncSearchTimeoutMs() {
        return component.getAsyncSearchDelayMs();
    }

    @Override
    @Deprecated
    public void setAsyncSearchTimeoutMs(int asyncSearchTimeoutMs) {
        component.setAsyncSearchDelayMs(asyncSearchTimeoutMs);
    }

    @Override
    public int getAsyncSearchDelayMs() {
        return component.getAsyncSearchDelayMs();
    }

    @Override
    public void setAsyncSearchDelayMs(int asyncSearchDelayMs) {
        component.setAsyncSearchDelayMs(asyncSearchDelayMs);
    }

    @Override
    public EnterActionHandler getEnterActionHandler() {
        return enterActionHandler;
    }

    @Override
    public void setEnterActionHandler(EnterActionHandler enterActionHandler) {
        this.enterActionHandler = enterActionHandler;
        component.setEnterActionHandler(enterActionHandler::onEnterKeyPressed);
    }

    @Override
    public ArrowDownActionHandler getArrowDownActionHandler() {
        return arrowDownActionHandler;
    }

    @Override
    public void setArrowDownActionHandler(ArrowDownActionHandler arrowDownActionHandler) {
        this.arrowDownActionHandler = arrowDownActionHandler;
        component.setArrowDownActionHandler(arrowDownActionHandler::onArrowDownKeyPressed);
    }

    @Override
    public void showSuggestions(List<?> suggestions) {
        component.showSuggestions(suggestions);
    }

    @Override
    public SearchExecutor getSearchExecutor() {
        return searchExecutor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSearchExecutor(SearchExecutor searchExecutor) {
        this.searchExecutor = searchExecutor;
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void commit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void discard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBuffered() {
        return false;
    }

    @Override
    public void setBuffered(boolean buffered) {
        throw new UnsupportedOperationException("Buffered mode isn't supported");
    }

    @Override
    public boolean isModified() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInputPrompt() {
        return component.getInputPrompt();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        component.setInputPrompt(inputPrompt);
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.setPopupStyleName(name);
    }

    @Override
    public void addStyleName(String styleName) {
        super.addStyleName(styleName);

        component.addPopupStyleName(styleName);
    }

    @Override
    public void removeStyleName(String styleName) {
        super.removeStyleName(styleName);

        component.removePopupStyleName(styleName);
    }

    @Override
    public void setPopupWidth(String popupWidth) {
        component.setPopupWidth(popupWidth);
    }

    @Override
    public String getPopupWidth() {
        return component.getPopupWidth();
    }

    @Override
    public void setOptionsStyleProvider(OptionsStyleProvider optionsStyleProvider) {
        this.optionsStyleProvider = optionsStyleProvider;

        if (optionsStyleProvider != null) {
            component.setOptionsStyleProvider(item ->
                    optionsStyleProvider.getItemStyleName(this, item));
        } else {
            component.setOptionsStyleProvider(null);
        }
    }

    @Override
    public OptionsStyleProvider getOptionsStyleProvider() {
        return optionsStyleProvider;
    }
}