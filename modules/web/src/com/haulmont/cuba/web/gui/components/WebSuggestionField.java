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
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.SuggestionField;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import com.haulmont.cuba.web.gui.components.converters.StringToEntityConverter;
import com.haulmont.cuba.web.widgets.CubaSuggestionField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static com.haulmont.cuba.web.gui.components.WebLookupField.NULL_STYLE_GENERATOR;

public class WebSuggestionField<V> extends WebV8AbstractField<CubaSuggestionField<V>, V, V>
        implements SuggestionField<V>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(WebSuggestionField.class);

    protected BackgroundTaskHandler<List<V>> handler;

    protected SearchExecutor<V> searchExecutor;

    protected EnterActionHandler enterActionHandler;
    protected ArrowDownActionHandler arrowDownActionHandler;

    protected StringToEntityConverter entityConverter = new StringToEntityConverter();

    protected CaptionMode captionMode = CaptionMode.ITEM; // todo remove
    protected String captionProperty; // todo remove

    protected Function<? super V, String> optionCaptionProvider;
    protected Function<? super V, String> optionStyleProvider;

    protected BackgroundWorker backgroundWorker;
    protected MetadataTools metadataTools;
    protected Locale locale;

    public WebSuggestionField() {
        component = createComponent();

        attachValueChangeListener(component);
    }

    @Inject
    protected void setBackgroundWorker(BackgroundWorker backgroundWorker) {
        this.backgroundWorker = backgroundWorker;
    }

    @Inject
    protected void setUserSessionSource(UserSessionSource userSessionSource) {
        this.locale = userSessionSource.getLocale();
    }

    @Inject
    protected void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    protected CubaSuggestionField<V> createComponent() {
        return new CubaSuggestionField<>();
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);
    }

    protected void initComponent(CubaSuggestionField<V> component) {
        component.setTextViewConverter(this::convertToTextView);

        component.setSearchExecutor(query -> {
            cancelSearch();
            searchSuggestions(query);
        });

        component.setCancelSearchHandler(this::cancelSearch);
    }

    @Override
    public V getValue() {
        V value = super.getValue();

        // todo rework OptionWrapper compatibility
        return value instanceof OptionWrapper
                ? (V) ((OptionWrapper) value).getValue()
                : value;
    }

    protected String generateItemStylename(Object item) {
        if (optionStyleProvider == null) {
            return null;
        }

        return this.optionStyleProvider.apply((V)item);
    }

    protected String convertToTextView(V value) {
        if (value == null) {
            return "";
        }

        if (optionCaptionProvider != null) {
            return optionCaptionProvider.apply(value);
        }

        if (value instanceof Entity) {
            Entity entity = (Entity) value;
            if (captionMode == CaptionMode.ITEM) {
                return entityConverter.convertToPresentation(entity, String.class, locale);
            }

            if (captionProperty != null && !"".equals(captionProperty)) {
                MetaPropertyPath propertyPath = entity.getMetaClass().getPropertyPath(captionProperty);
                if (propertyPath == null) {
                    throw new IllegalArgumentException(String.format("Can't find property for given caption property: %s", captionProperty));
                }

                return metadataTools.format(entity.getValueEx(captionProperty), propertyPath.getMetaProperty());
            }

            log.warn("Using StringToEntityConverter to get entity text presentation. Caption property is not defined " +
                    "while caption mode is \"PROPERTY\"");
            return entityConverter.convertToPresentation(entity, String.class, locale);
        }

        return metadataTools.format(value);
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

        if (captionProperty != null && !"".equals(captionProperty)) {
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
        BackgroundTask<Long, List<V>> task = getSearchSuggestionsTask(query);
        if (task != null) {
            handler = backgroundWorker.handle(task);
            handler.execute();
        }
    }

    protected BackgroundTask<Long, List<V>> getSearchSuggestionsTask(final String query) {
        if (this.searchExecutor == null) {
            return null;
        }

        SearchExecutor<V> currentSearchExecutor = this.searchExecutor;

        Map<String, Object> params;
        if (currentSearchExecutor instanceof ParametrizedSearchExecutor) {
            params = ((ParametrizedSearchExecutor<?>) currentSearchExecutor).getParams();
        } else {
            params = Collections.emptyMap();
        }

        return new BackgroundTask<Long, List<V>>(0) {
            @Override
            public List<V> run(TaskLifeCycle<Long> taskLifeCycle) throws Exception {
                List<V> result;
                try {
                    result = asyncSearch(currentSearchExecutor, query, params);
                } catch (RuntimeException e) {
                    log.error("Error in async search thread", e);

                    result = Collections.emptyList();
                }

                return result;
            }

            @Override
            public void done(List<V> result) {
                log.debug("Search results for '{}'", query);
                handleSearchResult(result);
            }

            @Override
            public boolean handleException(Exception ex) {
                log.error("Error in async search thread", ex);
                return true;
            }
        };
    }

    protected List<V> asyncSearch(SearchExecutor<V> searchExecutor, String searchString,
                                  Map<String, Object> params) throws Exception {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        log.debug("Search '{}'", searchString);

        List<V> searchResultItems;
        if (searchExecutor instanceof ParametrizedSearchExecutor) {
            //noinspection unchecked
            ParametrizedSearchExecutor<V> pSearchExecutor = (ParametrizedSearchExecutor<V>) searchExecutor;
            searchResultItems = pSearchExecutor.search(searchString, params);
        } else {
            searchResultItems = searchExecutor.search(searchString, Collections.emptyMap());
        }

        return searchResultItems;
    }

    protected void handleSearchResult(List<V> results) {
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
    public void showSuggestions(List<V> suggestions) {
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
    public void focus() {
        component.focus();
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

    @SuppressWarnings("unchecked")
    @Override
    public void setOptionStyleProvider(Function<? super V, String> optionStyleProvider) {
        if (this.optionStyleProvider != optionStyleProvider) {
            this.optionStyleProvider = optionStyleProvider;

            if (optionStyleProvider != null) {
                component.setOptionsStyleProvider(this::generateItemStylename);
            } else {
                component.setOptionsStyleProvider(NULL_STYLE_GENERATOR);
            }
        }
    }

    @Override
    public Function<? super V, String> getOptionStyleProvider() {
        return optionStyleProvider;
    }
}