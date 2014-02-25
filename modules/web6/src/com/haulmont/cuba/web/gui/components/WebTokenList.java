/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.ui.CustomField;
import com.haulmont.cuba.web.toolkit.ui.ScrollablePanel;
import com.haulmont.cuba.web.toolkit.ui.TokenListLabel;
import com.vaadin.terminal.KeyMapper;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.*;

import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebTokenList extends WebAbstractField<WebTokenList.TokenListImpl> implements TokenList {

    private CollectionDatasource datasource;

    private String captionProperty;

    private CaptionMode captionMode;

    private Position position = Position.TOP;

    private ItemChangeHandler itemChangeHandler;

    private ItemClickListener itemClickListener;

    private boolean inline;

    private WebButton addButton;

    private WebLookupPickerField lookupPickerField;

    private String lookupScreen;
    private WindowManager.OpenType lookupOpenMode = WindowManager.OpenType.THIS_TAB;
    private Map<String, Object> lookupScreenParams = null;

    private TokenStyleGenerator tokenStyleGenerator;

    private boolean lookup = false;

    private boolean editable = true;

    private boolean simple = false;

    private boolean multiselect;
    private PickerField.LookupAction lookupAction;

    public WebTokenList() {
        addButton = new WebButton();
        addButton.setCaption(AppBeans.get(Messages.class).getMessage(TokenList.class, "actions.Add"));

        lookupPickerField = new WebLookupPickerField();
        component = new TokenListImpl();

        setMultiSelect(false);
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        datasource.addListener(new CollectionDsListenerAdapter<Entity>() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                if (lookupPickerField != null) {
                    if (isLookup()) {
                        if (getLookupScreen() != null)
                            lookupAction.setLookupScreen(getLookupScreen());
                        else
                            lookupAction.setLookupScreen(null);

                        lookupAction.setLookupScreenOpenType(lookupOpenMode);
                        lookupAction.setLookupScreenParams(lookupScreenParams);
                    }
                }
                component.refreshComponent();
                component.refreshClickListeners(itemClickListener);
            }
        });
    }

    @Override
    public void setFrame(IFrame frame) {
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
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        if (datasource != null) {
            List<Object> items = new ArrayList<>();
            for (final Object itemId : datasource.getItemIds()) {
                items.add(datasource.getItem(itemId));
            }
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
    public void setValueChangingListener(ValueChangingListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeValueChangingListener() {
        throw new UnsupportedOperationException();
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
    public boolean isLookup() {
        return lookup;
    }

    @Override
    public void setLookup(boolean lookup) {
        if (this.lookup != lookup) {
            if (lookup)
                lookupAction = lookupPickerField.addLookupAction();
            else
                lookupPickerField.removeAction(lookupAction);
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
    }

    @Override
    public void setLookupScreenParams(Map<String, Object> params) {
        this.lookupScreenParams = params;
    }

    @Override
    public Map<String, Object> getLookupScreenParams() {
        return lookupScreenParams;
    }

    @Override
    public boolean isMultiSelect() {
        return multiselect;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        this.multiselect = multiselect;
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
    }

    @Override
    public boolean isInline() {
        return inline;
    }

    @Override
    public void setInline(boolean inline) {
        this.inline = inline;
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
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
        this.component.editor = null;
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
        if (instance == null)
            return "";
        if (captionProperty != null) {
            if (instance.getMetaClass().getPropertyPath(captionProperty) != null) {
                Object o = instance.getValueEx(captionProperty);
                return o != null ? o.toString() : " ";
            }
            throw new IllegalArgumentException(String.format("Couldn't find property with name '%s'",
                    captionProperty));
        } else
            return instance.getInstanceName();
    }

    public class TokenListImpl extends CustomField {

        private VerticalLayout root;

        private Panel scrollContainer;

        private Component editor;

        private Map<Instance, TokenListLabel> itemComponents = new HashMap<>();
        private Map<TokenListLabel, Instance> componentItems = new HashMap<>();
        private KeyMapper componentsMapper = new KeyMapper();

        public TokenListImpl() {
            root = new VerticalLayout();
            root.setSpacing(true);
            root.setSizeFull();

            scrollContainer = new ScrollablePanel();
            CssLayout layout = new CssLayout();
            scrollContainer.setContent(layout);
            scrollContainer.setSizeFull();

            root.addComponent(scrollContainer);
            root.setExpandRatio(scrollContainer, 1);

            setCompositionRoot(root);

            setStyleName("token-list");
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

            addButton.setStyleName("add-btn");

            Button wrappedButton = (Button) WebComponentsHelper.unwrap(addButton);
            if (!isSimple()) {
                wrappedButton.addListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (isEditable()) {
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
                    }
                });
            } else {
                wrappedButton.addListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {

                        String windowAlias;
                        if (getLookupScreen() != null) {
                            windowAlias = getLookupScreen();
                        } else if (getOptionsDatasource() != null) {
                            windowAlias = getOptionsDatasource().getMetaClass().getName() + ".browse";
                        } else {
                            windowAlias = getDatasource().getMetaClass().getName() + ".browse";
                        }

                        WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo(windowAlias);

                        Map<String, Object> params = new HashMap<>();
                        params.put("windowOpener", WebTokenList.this.getFrame().getId());
                        if (isMultiSelect()) {
                            WindowParams.MULTI_SELECT.set(params, true);
                            // for backward compatibility
                            params.put("multiSelect", "true");
                        }
                        if (lookupScreenParams != null)
                            params.putAll(lookupScreenParams);

                        WindowManager wm = App.getInstance().getWindowManager();
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

            editor = layout;
        }

        public void refreshComponent() {
            if (inline) {
                addStyleName("inline");
            }

            if (editor != null) {
                root.removeComponent(editor);
            }

            if (editor == null) {
                initField();
            }

            if (isEditable()) {
                if (position == Position.TOP) {
                    root.addComponentAsFirst(editor);
                } else {
                    root.addComponent(editor);
                    editor.setWidth("100%");
                }
            }

            scrollContainer.removeAllComponents();

            if (datasource != null) {
                List<Instance> usedItems = new ArrayList<>();

                // New tokens
                for (final Object itemId : datasource.getItemIds()) {
                    final Instance item = datasource.getItem(itemId);
                    TokenListLabel f = itemComponents.get(item);
                    if (f == null) {
                        f = createToken();
                        itemComponents.put(item, f);
                        componentItems.put(f, item);
                    }
                    f.setEditable(isEditable());
                    f.setValue(instanceCaption(item));
                    f.setWidth("100%");
                    setTokenStyle(f, itemId);
                    scrollContainer.addComponent(f);
                    usedItems.add(item);
                }

                // Remove obsolete items
                for (Instance componentItem : new ArrayList<Instance>(itemComponents.keySet())) {
                    if (!usedItems.contains(componentItem)) {
                        componentItems.remove(itemComponents.get(componentItem));
                        itemComponents.remove(componentItem);
                    }
                }
            }

            root.requestRepaint();
        }
        
        public void refreshClickListeners(ItemClickListener listener) {
            if (datasource != null && CollectionDatasource.State.VALID.equals(datasource.getState())) {
                for (Object id : datasource.getItemIds()) {
                    Instance item = datasource.getItem(id);
                    final TokenListLabel label = itemComponents.get(item);
                    if (label != null) {
                        if (listener != null)
                            label.setClickListener(new TokenListLabel.ClickListener() {
                                @Override
                                public void onClick(TokenListLabel source) {
                                    doClick(label);
                                }
                            });
                        else
                            label.setClickListener(null);
                        label.requestRepaint();
                    }
                }
            }
        }

        protected TokenListLabel createToken() {
            final TokenListLabel label = new TokenListLabel();
            String key = componentsMapper.key(label);
            label.setKey(key);
            label.setWidth("100%");
            label.addListener(new TokenListLabel.RemoveTokenListener() {
                @Override
                public void removeToken(final TokenListLabel source) {
                    if (isEditable()) {
                        doRemove(source);
                    }
                }
            });
            return label;
        }

        private void doRemove(TokenListLabel source) {
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

        private void doClick(TokenListLabel source) {
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

        protected void setTokenStyle(TokenListLabel label, Object itemId) {
            if (tokenStyleGenerator != null) {
                String styleName = tokenStyleGenerator.getStyle(itemId);
                if (styleName != null && !styleName.equals("")) {
                    label.setStyleName(styleName);
                }
            }
        }
    }
}