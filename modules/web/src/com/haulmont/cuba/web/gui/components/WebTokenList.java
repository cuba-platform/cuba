/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.ui.CubaTokenListLabel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.*;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author gorodnov
 */
public class WebTokenList extends WebAbstractField<WebTokenList.CubaTokenList> implements TokenList {

    protected CollectionDatasource datasource;

    protected String captionProperty;

    protected CaptionMode captionMode;

    protected Position position = Position.TOP;

    protected ItemChangeHandler itemChangeHandler;

    protected ItemClickListener itemClickListener;

    protected boolean inline;

    protected WebButton addButton;

    protected WebButton clearButton;

    protected WebLookupPickerField lookupPickerField;

    protected String lookupScreen;
    protected WindowManager.OpenType lookupOpenMode = WindowManager.OpenType.THIS_TAB;
    protected Map<String, Object> lookupScreenParams;
    protected DialogParams lookupScreenDialogParams;

    protected TokenStyleGenerator tokenStyleGenerator;

    protected boolean lookup = false;

    protected boolean clearEnabled = true;

    protected boolean editable = true;

    protected boolean simple = false;

    protected boolean multiselect;
    protected PickerField.LookupAction lookupAction;

    protected final ValueChangeListener lookupSelectListener = e -> {
        if (isEditable()) {
            addValueFromLookupPickerField();
        }
    };

    public WebTokenList() {
        addButton = new WebButton();
        Messages messages = AppBeans.get(Messages.NAME);
        addButton.setCaption(messages.getMessage(TokenList.class, "actions.Add"));

        clearButton = new WebButton();
        clearButton.setCaption(messages.getMessage(TokenList.class, "actions.Clear"));

        lookupPickerField = new WebLookupPickerField();
        lookupPickerField.addValueChangeListener(lookupSelectListener);
        component = new CubaTokenList();

        setMultiSelect(false);
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;

        //noinspection unchecked
        datasource.addCollectionChangeListener(e -> {
            if (lookupPickerField != null) {
                if (isLookup()) {
                    if (getLookupScreen() != null) {
                        lookupAction.setLookupScreen(getLookupScreen());
                    } else {
                        lookupAction.setLookupScreen(null);
                    }

                    lookupAction.setLookupScreenOpenType(lookupOpenMode);
                    lookupAction.setLookupScreenParams(lookupScreenParams);
                    lookupAction.setLookupScreenDialogParams(lookupScreenDialogParams);
                }
            }
            component.refreshComponent();
            component.refreshClickListeners(itemClickListener);
        });
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);
        lookupPickerField.setFrame(frame);
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    @Override
    public WindowManager.OpenType getLookupOpenMode() {
        return lookupOpenMode;
    }

    @Override
    public void setLookupOpenMode(WindowManager.OpenType lookupOpenMode) {
        this.lookupOpenMode = lookupOpenMode;
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
    public LookupField.FilterMode getFilterMode() {
        return lookupPickerField.getFilterMode();
    }

    @Override
    public void setFilterMode(LookupField.FilterMode mode) {
        lookupPickerField.setFilterMode(mode);
    }

    @Override
    public String getOptionsCaptionProperty() {
        return lookupPickerField.getCaptionProperty();
    }

    @Override
    public void setOptionsCaptionProperty(String captionProperty) {
        lookupPickerField.setCaptionProperty(captionProperty);
    }

    @Override
    public CaptionMode getOptionsCaptionMode() {
        return lookupPickerField.getCaptionMode();
    }

    @Override
    public void setOptionsCaptionMode(CaptionMode captionMode) {
        lookupPickerField.setCaptionMode(captionMode);
    }

    @Override
    public CollectionDatasource getOptionsDatasource() {
        return lookupPickerField.getOptionsDatasource();
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        lookupPickerField.setOptionsDatasource(datasource);
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        throw new UnsupportedOperationException("TokenList does not support datasource with property");
    }

    @Override
    public MetaProperty getMetaProperty() {
        return null;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        if (datasource != null) {
            List<Object> items = new ArrayList(datasource.getItems());
            return (T) items;
        } else
            return (T) Collections.emptyList();
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addListener(ValueListener listener) {
        // todo
    }

    @Override
    public void removeListener(ValueListener listener) {
        // todo
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        LoggerFactory.getLogger(WebTokenList.class).warn("addValueChangeListener not implemented for TokenList");
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        LoggerFactory.getLogger(WebTokenList.class).warn("removeValueChangeListener not implemented for TokenList");
    }

    @Override
    public List getOptionsList() {
        return lookupPickerField.getOptionsList();
    }

    @Override
    public void setOptionsList(List optionsList) {
        lookupPickerField.setOptionsList(optionsList);
    }

    @Override
    public Map<String, Object> getOptionsMap() {
        return lookupPickerField.getOptionsMap();
    }

    @Override
    public void setOptionsMap(Map<String, Object> map) {
        lookupPickerField.setOptionsMap(map);
    }

    @Override
    public boolean isClearEnabled() {
        return clearEnabled;
    }

    @Override
    public void setClearEnabled(boolean clearEnabled) {
        if (this.clearEnabled != clearEnabled) {
            clearButton.setVisible(clearEnabled);
            this.clearEnabled = clearEnabled;
            component.refreshComponent();
        }
    }

    @Override
    public boolean isLookup() {
        return lookup;
    }

    @Override
    public void setLookup(boolean lookup) {
        if (this.lookup != lookup) {
            if (lookup) {
                lookupAction = lookupPickerField.addLookupAction();

                if (getLookupScreen() != null) {
                    lookupAction.setLookupScreen(getLookupScreen());
                }
                lookupAction.setLookupScreenOpenType(lookupOpenMode);
                lookupAction.setLookupScreenParams(lookupScreenParams);
                lookupAction.setLookupScreenDialogParams(lookupScreenDialogParams);
            } else {
                lookupPickerField.removeAction(lookupAction);
            }
        }
        this.lookup = lookup;
        component.refreshComponent();
    }

    @Override
    public String getLookupScreen() {
        return lookupScreen;
    }

    @Override
    public void setLookupScreen(String lookupScreen) {
        this.lookupScreen = lookupScreen;
        if (lookupAction != null) {
            lookupAction.setLookupScreen(lookupScreen);
        }
    }

    @Override
    public void setLookupScreenParams(Map<String, Object> params) {
        this.lookupScreenParams = params;
        if (lookupAction != null) {
            lookupAction.setLookupScreenParams(params);
        }
    }

    @Override
    public Map<String, Object> getLookupScreenParams() {
        return lookupScreenParams;
    }

    @Override
    public void setLookupScreenDialogParams(DialogParams dialogParams) {
        this.lookupScreenDialogParams = dialogParams;
        if (lookupAction != null) {
            lookupAction.setLookupScreenDialogParams(dialogParams);
        }
    }

    @Deprecated
    @Nullable
    @Override
    public DialogParams getLookupScreenDialogParams() {
        return lookupScreenDialogParams;
    }

    @Override
    public boolean isMultiSelect() {
        return multiselect;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        this.multiselect = multiselect;
        component.refreshComponent();
    }

    @Override
    public String getAddButtonCaption() {
        return addButton.getCaption();
    }

    @Override
    public void setAddButtonCaption(String caption) {
        addButton.setCaption(caption);
    }

    @Override
    public String getAddButtonIcon() {
        return addButton.getIcon();
    }

    @Override
    public void setAddButtonIcon(String icon) {
        addButton.setIcon(icon);
    }

    @Override
    public String getClearButtonCaption() {
        return clearButton.getCaption();
    }

    @Override
    public void setClearButtonCaption(String caption) {
        clearButton.setCaption(caption);
    }

    @Override
    public String getClearButtonIcon() {
        return clearButton.getIcon();
    }

    @Override
    public void setClearButtonIcon(String icon) {
        clearButton.setIcon(icon);
    }

    @Override
    public ItemChangeHandler getItemChangeHandler() {
        return itemChangeHandler;
    }

    @Override
    public void setItemChangeHandler(ItemChangeHandler handler) {
        this.itemChangeHandler = handler;
    }

    @Override
    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    @Override
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.component.refreshClickListeners(itemClickListener);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;

        component.refreshComponent();
    }

    @Override
    public boolean isInline() {
        return inline;
    }

    @Override
    public void setInline(boolean inline) {
        this.inline = inline;

        component.refreshComponent();
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;

        component.refreshComponent();
    }

    @Override
    public boolean isSimple() {
        return simple;
    }

    @Override
    public void setSimple(boolean simple) {
        this.simple = simple;
        this.addButton.setVisible(simple);
        this.component.refreshComponent();
    }

    @Override
    public void setTokenStyleGenerator(TokenStyleGenerator tokenStyleGenerator) {
        this.tokenStyleGenerator = tokenStyleGenerator;
    }

    @Override
    public TokenStyleGenerator getTokenStyleGenerator() {
        return tokenStyleGenerator;
    }

    protected String instanceCaption(Instance instance) {
        if (instance == null) {
            return "";
        }
        if (captionProperty != null) {
            if (instance.getMetaClass().getPropertyPath(captionProperty) != null) {
                Object o = instance.getValueEx(captionProperty);
                return o != null ? o.toString() : " ";
            }

            throw new IllegalArgumentException(String.format("Couldn't find property with name '%s'", captionProperty));
        } else {
            return instance.getInstanceName();
        }
    }

    public class CubaTokenList extends CustomField {

        private VerticalLayout composition;

        private Panel scrollContainer;

        private CssLayout scrollContainerlayout;

        private Component editor;

        private Map<Instance, CubaTokenListLabel> itemComponents = new HashMap<>();
        private Map<CubaTokenListLabel, Instance> componentItems = new HashMap<>();

        public CubaTokenList() {
            composition = new VerticalLayout();
            composition.setSpacing(true);
            composition.setSizeFull();

            scrollContainer = new Panel();
            scrollContainerlayout = new CssLayout();
            scrollContainerlayout.setSizeUndefined();

            scrollContainer.setContent(scrollContainerlayout);
            scrollContainer.setSizeFull();

            composition.addComponent(scrollContainer);
            composition.setExpandRatio(scrollContainer, 1);
            setPrimaryStyleName("cuba-tokenlist");
        }

        @Override
        protected Component initContent() {
            return composition;
        }

        protected void initField() {
            final HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);
            layout.setWidth("100%");

            if (!isSimple()) {
                lookupPickerField.setWidth("100%");
                Component lookupComponent = WebComponentsHelper.getComposition(lookupPickerField);
                lookupComponent.setWidth("100%");

                layout.addComponent(lookupComponent);
                layout.setExpandRatio(lookupComponent, 1);
            } else {
                lookupPickerField.setVisible(false);
            }
            addButton.setVisible(isSimple());
            addButton.setStyleName("add-btn");

            Button wrappedButton = (Button) WebComponentsHelper.unwrap(addButton);
            Collection listeners = wrappedButton.getListeners(Button.ClickEvent.class);
            for (Object listener : listeners) {
                wrappedButton.removeClickListener((Button.ClickListener) listener);
            }

            if (!isSimple()) {
                wrappedButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (isEditable()) {
                            addValueFromLookupPickerField();
                        }
                    }
                });
            } else {
                wrappedButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

                        String windowAlias;
                        if (getLookupScreen() != null) {
                            windowAlias = getLookupScreen();
                        } else if (getOptionsDatasource() != null) {
                            windowAlias = windowConfig.getBrowseScreenId(getOptionsDatasource().getMetaClass());
                        } else {
                            windowAlias = windowConfig.getBrowseScreenId(getDatasource().getMetaClass());
                        }

                        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);

                        Map<String, Object> params = new HashMap<>();
                        params.put("windowOpener", WebTokenList.this.getFrame().getId());
                        if (isMultiSelect()) {
                            WindowParams.MULTI_SELECT.set(params, true);
                            // for backward compatibility
                            params.put("multiSelect", "true");
                        }
                        if (lookupScreenParams != null) {
                            params.putAll(lookupScreenParams);
                        }

                        WindowManager wm = App.getInstance().getWindowManager();
                        if (lookupOpenMode == WindowManager.OpenType.DIALOG) {
                            if (lookupScreenDialogParams != null) {
                                wm.getDialogParams().setWidth(lookupScreenDialogParams.getWidth());
                                wm.getDialogParams().setHeight(lookupScreenDialogParams.getHeight());
                            } else {
                                ThemeConstants theme = App.getInstance().getThemeConstants();
                                int width = theme.getInt("cuba.web.WebTokenList.lookupDialog.width");
                                int height = theme.getInt("cuba.web.WebTokenList.lookupDialog.height");

                                wm.getDialogParams().setWidth(width);
                                wm.getDialogParams().setHeight(height);
                            }
                            wm.getDialogParams().setResizable(true);
                        }

                        wm.openLookup(windowInfo, new Window.Lookup.Handler() {
                            @Override
                            public void handleLookup(Collection items) {
                                if (isEditable()) {
                                    if (items == null || items.isEmpty()) return;
                                    for (final Object item : items) {
                                        if (itemChangeHandler != null) {
                                            itemChangeHandler.addItem(item);
                                        } else {
                                            datasource.addItem((Entity) item);
                                        }
                                    }
                                }
                            }
                        }, lookupOpenMode, params);
                    }
                });
            }
            layout.addComponent(wrappedButton);

            clearButton.setVisible(clearEnabled);
            clearButton.setStyleName("clear-btn");

            Button wrappedClearButton = (Button) WebComponentsHelper.unwrap(clearButton);
            wrappedClearButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    for (CubaTokenListLabel item : new ArrayList<>(itemComponents.values())) {
                        doRemove(item);
                    }
                }
            });
            if (isSimple()) {
                final HorizontalLayout clearLayout = new HorizontalLayout();
                clearLayout.addComponent(wrappedClearButton);
                layout.addComponent(clearLayout);
                layout.setExpandRatio(clearLayout, 1);
            } else {
                layout.addComponent(wrappedClearButton);
            }

            editor = layout;
        }

        public void refreshComponent() {
            if (inline) {
                addStyleName("inline");
                scrollContainerlayout.setSizeUndefined();
            } else {
                removeStyleName("inline");
                scrollContainerlayout.setWidth(100, Unit.PERCENTAGE);
            }

            if (editor != null) {
                composition.removeComponent(editor);
            }

            initField();

            if (isEditable()) {
                if (position == Position.TOP) {
                    composition.addComponentAsFirst(editor);
                } else {
                    composition.addComponent(editor);
                    editor.setWidth("100%");
                }
            }

            Layout layout = (Layout) scrollContainer.getContent();
            layout.removeAllComponents();

            if (datasource != null) {
                List<Instance> usedItems = new ArrayList<>();

                // New tokens
                for (final Object itemId : datasource.getItemIds()) {
                    final Instance item = datasource.getItem(itemId);
                    CubaTokenListLabel f = itemComponents.get(item);
                    if (f == null) {
                        f = createToken();
                        itemComponents.put(item, f);
                        componentItems.put(f, item);
                    }
                    f.setEditable(isEditable());
                    f.setText(instanceCaption(item));
                    f.setWidthUndefined();

                    setTokenStyle(f, itemId);
                    scrollContainerlayout.addComponent(f);
                    usedItems.add(item);
                }

                // Remove obsolete items
                for (Instance componentItem : new ArrayList<>(itemComponents.keySet())) {
                    if (!usedItems.contains(componentItem)) {
                        componentItems.remove(itemComponents.get(componentItem));
                        itemComponents.remove(componentItem);
                    }
                }
            }
        }

        public void refreshClickListeners(ItemClickListener listener) {
            if (datasource != null && CollectionDatasource.State.VALID.equals(datasource.getState())) {
                for (Object id : datasource.getItemIds()) {
                    Instance item = datasource.getItem(id);
                    final CubaTokenListLabel label = itemComponents.get(item);
                    if (label != null) {
                        if (listener != null) {
                            label.setClickListener(new CubaTokenListLabel.ClickListener() {
                                @Override
                                public void onClick(CubaTokenListLabel source) {
                                    doClick(label);
                                }
                            });
                        } else {
                            label.setClickListener(null);
                        }
                    }
                }
            }
        }

        protected CubaTokenListLabel createToken() {
            final CubaTokenListLabel label = new CubaTokenListLabel();
            label.setWidth("100%");
            label.addListener(new CubaTokenListLabel.RemoveTokenListener() {
                @Override
                public void removeToken(final CubaTokenListLabel source) {
                    if (isEditable()) {
                        doRemove(source);
                    }
                }
            });
            return label;
        }

        private void doRemove(CubaTokenListLabel source) {
            Instance item = componentItems.get(source);
            if (item != null) {
                itemComponents.remove(item);
                componentItems.remove(source);

                if (itemChangeHandler != null) { //todo test
                    itemChangeHandler.removeItem(item);
                } else {
                    datasource.removeItem((Entity) item);
                }
            }
        }

        private void doClick(CubaTokenListLabel source) {
            if (itemClickListener != null) {
                Instance item = componentItems.get(source);
                if (item != null)
                    itemClickListener.onClick(item);
            }
        }

        @Override
        public Class<?> getType() {
            return List.class;
        }

        protected void setTokenStyle(CubaTokenListLabel label, Object itemId) {
            if (tokenStyleGenerator != null) {
                String styleName = tokenStyleGenerator.getStyle(itemId);
                if (styleName != null && !styleName.equals("")) {
                    label.setStyleName(styleName);
                }
            }
        }

        @Override
        public void setBuffered(boolean buffered) {
        }

        @Override
        public boolean isBuffered() {
            return false;
        }

        @Override
        public void removeAllValidators() {
            getValidators().clear();
        }
    }

    protected void addValueFromLookupPickerField() {
        final Entity newItem = lookupPickerField.getValue();
        if (newItem == null) return;
        if (itemChangeHandler != null) {
            itemChangeHandler.addItem(newItem);
        } else {
            if (datasource != null)
                datasource.addItem(newItem);
        }
        lookupPickerField.setValue(null);
    }

    @Override
    protected boolean isEmpty(Object value) {
        return super.isEmpty(value) || (value instanceof Collection && ((Collection) value).isEmpty());
    }
}