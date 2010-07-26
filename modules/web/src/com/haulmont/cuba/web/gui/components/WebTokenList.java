/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 20.07.2010 15:40:53
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.TokenList;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.toolkit.ui.CustomField;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.haulmont.cuba.web.toolkit.ui.ScrollablePanel;
import com.haulmont.cuba.web.toolkit.ui.TokenListLabel;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.KeyMapper;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebTokenList extends WebAbstractComponent<WebTokenList.TokenListImpl> implements TokenList {

    private static final long serialVersionUID = -6490244006772570832L;

    private MetaClass metaClass;

    private CollectionDatasource datasource;

    private CollectionDatasource optionsDatasource;
    private String captionProperty;
    private String optionsCaptionProperty;

    private CaptionMode captionMode;

    private Position position = Position.TOP;

    private Type type = Type.LOOKUP;

    private ItemChangeHandler itemChangeHandler;

    private String caption;
    private boolean inline;

    private String lookupScreen;

    public WebTokenList() {
        component = new TokenListImpl();
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;

        metaClass = datasource.getMetaClass();

        datasource.addListener(new CollectionDatasourceListener() {
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                component.refreshComponent();
            }

            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
            }

            public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
            }

            public void valueChanged(Object source, String property, Object prevValue, Object value) {
            }
        });
    }

    public String getCaptionProperty() {
        return captionProperty;
    }

    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;

        if (optionsCaptionProperty == null && optionsDatasource != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    public String getOptionsCaptionProperty() {
        return optionsCaptionProperty;
    }

    public void setOptionsCaptionProperty(String captionProperty) {
        optionsCaptionProperty = captionProperty;

        if (optionsDatasource != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
        switch (captionMode) {
            case ITEM: {
                component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
                break;
            }
            case PROPERTY: {
                component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    public CollectionDatasource getOptionsDatasource() {
        return optionsDatasource;
    }

    public void setOptionsDatasource(CollectionDatasource optionsDatasource) {
        this.optionsDatasource = optionsDatasource;
        component.setContainerDataSource(new CollectionDsWrapper(optionsDatasource, true));

        if (optionsCaptionProperty != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(optionsCaptionProperty));
        }
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    public String getLookupScreen() {
        return lookupScreen;
    }

    public void setLookupScreen(String lookupScreen) {
        this.lookupScreen = lookupScreen;
    }

    public ItemChangeHandler getItemChangeHandler() {
        return itemChangeHandler;
    }

    public void setItemChangeHandler(ItemChangeHandler handler) {
        this.itemChangeHandler = handler;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isInline() {
        return inline;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public String getAddButtonCaption() {
        return component.getButtonCaption();
    }

    public void setAddButtonCaption(String caption) {
        component.setButtonCaption(caption);
    }

    public String getAddButtonIcon() {
        return component.getButtonIcon();
    }

    public void setAddButtonIcon(String icon) {
        component.setButtonIcon(icon);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    protected String instanceCaption(Instance instance) {
        if (instance == null) { return ""; }
        if (instance.getMetaClass().getPropertyEx(captionProperty) != null) {
            return instance.getValueEx(captionProperty).toString();
        }
        throw new IllegalArgumentException(String.format("Couldn't find property with name '%s'",
                captionProperty));
    }

    protected <T> T getValueFromKey(Object key) {
        if (key == null) return null;
        if (key instanceof Enum) { return (T) key; }

        T v;
        if (optionsDatasource != null) {
            if (Datasource.State.INVALID.equals(optionsDatasource.getState())) {
                optionsDatasource.refresh();
            }
            v = (T) optionsDatasource.getItem(key);
        } else {
            v = (T) key;
        }

        return v;
    }

    public class TokenListImpl extends CustomField
            implements com.vaadin.data.Container.Viewer, com.vaadin.data.Container.Editor {

        private VerticalLayout root;

        private Panel container;

        private Component editor;

        private com.vaadin.data.Container items;

        private Object itemCaptionPropertyId;
        private int itemCaptionMode;

        private Map<Instance, Component> itemComponents = new HashMap<Instance, Component>();
        private Map<Component, Instance> componentItems = new HashMap<Component, Instance>();
        private KeyMapper componentsMapper = new KeyMapper();

        private String buttonCaption;
        private String buttonIcon;

        public TokenListImpl() {
            root = new VerticalLayout();
            root.setSpacing(true);
            root.setSizeFull();

            container = new ScrollablePanel();
            CssLayout layout = new CssLayout();
            container.setContent(layout);
            container.setSizeFull();

            root.addComponent(container);
            root.setExpandRatio(container, 1);

            setCompositionRoot(root);

            setStyleName("token-list");
        }

        public void refreshComponent() {
            if (inline) {
                addStyleName("inline");
            }
            if (editor != null) {
                root.removeComponent(editor);
            }
            editor = createTokenEditor();
            if (editor == null) {
                throw new IllegalStateException();
            }
            if (position == Position.TOP) {
                root.addComponentAsFirst(editor);
            } else {
                root.addComponent(editor);
            }

            container.removeAllComponents();

            for (final Object itemId : datasource.getItemIds()) {
                final Instance item = (Instance) datasource.getItem(itemId);
                Component f = itemComponents.get(item);
                if (f == null) {
                    f = createTokenViewer();
                    itemComponents.put(item, f);
                    componentItems.put(f, item);
                }
                if (captionProperty != null) {
                    ((Property) f).setValue(instanceCaption(item));
                } else {
                    ((Property) f).setValue(item);
                }
                container.addComponent(f);
            }

            requestRepaint();
        }

        public void setContainerDataSource(com.vaadin.data.Container newDataSource) {
            if (newDataSource == null) {
                newDataSource = new IndexedContainer();
            }

            if (items != newDataSource) {
                items = newDataSource;
            }
        }

        public com.vaadin.data.Container getContainerDataSource() {
            return items;
        }

        public void setItemCaptionPropertyId(Object propertyId) {
            itemCaptionPropertyId = propertyId;
        }

        public Object getItemCaptionPropertyId() {
            return itemCaptionPropertyId;
        }

        public int getItemCaptionMode() {
            return itemCaptionMode;
        }

        public void setItemCaptionMode(int itemCaptionMode) {
            this.itemCaptionMode = itemCaptionMode;
        }

        protected Component createTokenViewer() {
            final TokenListLabel label = new TokenListLabel();
            String key = componentsMapper.key(label);
            label.setKey(key);
            label.addListener(new TokenListLabel.RemoveTokenListener() {
                public void removeToken(TokenListLabel source) {
                    Instance item = componentItems.get(source);
                    if (item != null) {
                        datasource.removeItem((Entity) item);

                        itemComponents.remove(item);
                        componentItems.remove(source);
                    }
                }
            });
            return label;
        }

        protected Component createTokenEditor() {
            final Field component;
            final Button.ClickListener listener;
            switch (type) {
                case PICKER:
                    final WebPickerField pickerField = new WebPickerField();
                    pickerField.setMetaClass(metaClass);
                    pickerField.setLookupScreen(lookupScreen);
                    pickerField.setWidth("100%");
                    
                    component = (Field) WebComponentsHelper.unwrap(pickerField);

                    listener = new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {
                            final Entity newItem = pickerField.getValue();
                            if (newItem == null) return;
                            if (itemChangeHandler != null) {
                                itemChangeHandler.addItem(newItem);
                            } else {
                                datasource.addItem(newItem);
                            }
                            pickerField.setValue(null);
                        }
                    };

                    break;
                case LOOKUP:
                    final WebLookupField lookupField = new WebLookupField();
                    final FilterSelect filterSelect = (FilterSelect) WebComponentsHelper.unwrap(lookupField);
                    filterSelect.setContainerDataSource(items);
                    filterSelect.setWidth("100%");
                    filterSelect.setItemCaptionMode(itemCaptionMode);
                    if (itemCaptionPropertyId != null) {
                        filterSelect.setItemCaptionPropertyId(itemCaptionPropertyId);
                    }

                    component = filterSelect;

                    listener = new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {
                            final Entity newItem = getValueFromKey(component.getValue());
                            if (newItem == null) return;
                            if (itemChangeHandler != null) {
                                itemChangeHandler.addItem(newItem);
                            } else {
                                datasource.addItem(newItem);
                            }
                            component.setValue(null);
                        }
                    };

                    break;
                default:
                    throw new IllegalArgumentException();
            }

            final HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);
            layout.setWidth("100%");

            final Button button = new Button(buttonCaption == null ? "Add" : buttonCaption);
            button.setStyleName("add-btn");
            if (buttonIcon != null) {
                button.setIcon(new ThemeResource(buttonIcon));
            }
            button.addListener(listener);

            layout.addComponent(component);
            layout.setExpandRatio(component, 1);

            layout.addComponent(button);

            component.setCaption(caption);
            
            return layout;
        }

        @Override
        public Class<?> getType() {
            return List.class;
        }

        public String getButtonCaption() {
            return buttonCaption;
        }

        public void setButtonCaption(String buttonCaption) {
            this.buttonCaption = buttonCaption;
            refreshComponent();
        }

        public String getButtonIcon() {
            return buttonIcon;
        }

        public void setButtonIcon(String buttonIcon) {
            this.buttonIcon = buttonIcon;
            refreshComponent();
        }
    }

}
