/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;

import javax.swing.BoxLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopTokenList extends DesktopAbstractField<DesktopTokenList.TokenListImpl> implements TokenList {

    private TokenStyleGenerator tokenStyleGenerator;

    private CollectionDatasource datasource;

    private String captionProperty;

    private CaptionMode captionMode;

    private Position position = Position.TOP;

    private ItemChangeHandler itemChangeHandler;

    private boolean inline;

    private DesktopButton addButton;

    private DesktopLookupPickerField lookupPickerField;

    private String lookupScreen;

    private WindowManager.OpenType lookupOpenMode = WindowManager.OpenType.THIS_TAB;

    private Map<String, Object> lookupScreenParams = null;

    private boolean lookup;

    private boolean editable;

    private boolean simple;

    private boolean multiselect;

    private PickerField.LookupAction lookupAction;

    public DesktopTokenList() {
        impl = new TokenListImpl();

        addButton = new DesktopButton();
        addButton.setCaption(MessageProvider.getMessage(TokenList.class, "actions.Add"));

        lookupPickerField = new DesktopLookupPickerField();

        setMultiSelect(false);
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
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return null;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
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
                impl.refreshComponent();
            }
        });
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
        impl.refreshComponent();
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
        lookupPickerField.setMultiSelect(multiselect);
    }

    @Override
    public boolean isSimple() {
        return simple;
    }

    @Override
    public void setSimple(boolean simple) {
        this.simple = simple;
        this.impl.editor = null;
        this.impl.refreshComponent();
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
    public WindowManager.OpenType getLookupOpenMode() {
        return lookupOpenMode;
    }

    @Override
    public void setLookupOpenMode(WindowManager.OpenType lookupOpenMode) {
        this.lookupOpenMode = lookupOpenMode;
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
    public void setTokenStyleGenerator(TokenStyleGenerator tokenStyleGenerator) {
        this.tokenStyleGenerator = tokenStyleGenerator;
    }

    @Override
    public TokenStyleGenerator getTokenStyleGenerator() {
        return tokenStyleGenerator;
    }

    @Override
    public String getCaption() {
        return impl.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        impl.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        if (datasource != null) {
            List<Object> items = new ArrayList<Object>();
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
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
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
            throw new IllegalArgumentException(String.format("Couldn't find property with name '%s'",
                    captionProperty));
        } else
            return instance.getInstanceName();
    }

    public class TokenListLabel extends DesktopAbstractComponent<JLabel> {

        private DesktopLabel label;
        private DesktopHBox composition;
        private DesktopButton removeButton;

        private boolean editable;

        private List<RemoveTokenListener> listeners;

        public TokenListLabel() {
            label = new DesktopLabel();
            composition = new DesktopHBox();
            composition.add(label);
            composition.expand(label);
            removeButton = new DesktopButton();
            removeButton.setAction(new AbstractAction("actions.Remove") {

                @Override
                public String getCaption() {
                    return "";
                }

                @Override
                public void actionPerform(Component component) {
                    fireRemoveListeners();
                }
            });

            composition.add(removeButton);
            removeButton.setVisible(isEditable());
            removeButton.setIcon("icons/remove.png");

            JButton button = removeButton.getComponent();
            button.setBorder(new EmptyBorder(0,3,0,3));
            button.setFocusPainted(false);
            button.setBorderPainted(false);

            impl = label.getComponent();
        }

        @Override
        public JComponent getComposition() {
            return composition.getComposition();
        }

        public void setValue(String value) {
            label.setValue(value);
        }

        public Object getValue() {
            return label.getValue();
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
            removeButton.setVisible(editable);
        }

        public boolean isEditable() {
            return editable;
        }

        public void addListener(RemoveTokenListener listener) {
            if (listeners == null) {
                listeners = new ArrayList<RemoveTokenListener>();
            }
            listeners.add(listener);
        }

        public void removeListener(RemoveTokenListener listener) {
            if (listeners != null) {
                listeners.remove(listener);
                if (listeners.isEmpty()) {
                    listeners = null;
                }
            }
        }

        private void fireRemoveListeners() {
            if (listeners != null) {
                for (final RemoveTokenListener listener : listeners) {
                    listener.removeToken(this);
                }
            }
        }
    }

    public interface RemoveTokenListener {
        void removeToken(TokenListLabel source);
    }

    public class AddAction extends AbstractAction {

        public AddAction() {
            super("actions.Add");
        }

        @Override
        public String getCaption() {
            return addButton.getCaption();
        }

        @Override
        public void actionPerform(Component component) {
            if (isSimple()) {
                openLookupWindow();
            } else {
                if (isEditable()) {
                    getValueFromField();
                }
            }
        }

        private void getValueFromField() {
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

        private void openLookupWindow() {
            String windowAlias;
            if (getLookupScreen() != null) {
                windowAlias = getLookupScreen();
            } else if (getOptionsDatasource() != null) {
                windowAlias = getOptionsDatasource().getMetaClass().getName() + ".browse";
            } else {
                windowAlias = getDatasource().getMetaClass().getName() + ".browse";
            }

            WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
            WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("windowOpener", DesktopTokenList.this.<IFrame>getFrame().getId());
            if (isMultiSelect()) {
                params.put("multiSelect", "true");
            }
            if (lookupScreenParams != null)
                params.putAll(lookupScreenParams);

            WindowManager wm = DesktopComponentsHelper.getTopLevelFrame(DesktopTokenList.this).getWindowManager();
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
    }

    public class TokenListImpl extends JPanel {

        private DesktopVBox root;

        private DesktopScrollBoxLayout scrollContainer;
        private DesktopVBox tokensContainer;

        private DesktopHBox editor;

        private String caption;

        private Map<Instance, TokenListLabel> itemComponents = new HashMap<Instance, TokenListLabel>();
        private Map<TokenListLabel, Instance> componentItems = new HashMap<TokenListLabel, Instance>();

        private TokenListImpl() {
            BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(layout);

            root = new DesktopVBox();
            root.setSpacing(true);
            root.setExpanded(true);

            scrollContainer = new DesktopScrollBoxLayout();
            scrollContainer.setWidth("100%");
            scrollContainer.setHeight("100%");

            tokensContainer = new DesktopVBox();
            tokensContainer.setWidth("100%");
            scrollContainer.add(tokensContainer);

            root.add(scrollContainer);

            this.add(root.getComposition());
        }

        protected void initField() {
            DesktopHBox hBox = new DesktopHBox();
            hBox.setSpacing(true);
            hBox.setWidth("100%");

            if (isSimple()) {
                lookupPickerField.setWidth("100%");
                hBox.add(lookupPickerField);
                hBox.expand(lookupPickerField);
            }
            addButton.setAction(new AddAction());

            lookupPickerField.setVisible(!isSimple());

            addButton.setStyleName("add-btn");
            hBox.add(addButton);

            editor = hBox;
        }

        protected void setTokenStyle(TokenListLabel label, Object itemId) {
            if (tokenStyleGenerator != null) {
                String styleName = tokenStyleGenerator.getStyle(itemId);
                if (styleName != null && !styleName.equals("")) {
                    label.setStyleName(styleName);
                }
            }
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public void refreshComponent() {
            // todo inline mode

            if (editor != null) {
                root.remove(editor);
            }

            if (editor == null) {
                initField();
            }

            if (isEditable()) {
                java.awt.Container rootComposition = root.getComposition();
                if (position == Position.TOP) {
                    rootComposition.add(editor.getComposition(), 0);
                } else {
                    rootComposition.add(editor.getComposition());
                }
            }

            for (Component ownComponent : new ArrayList<Component>(tokensContainer.getOwnComponents()))
                tokensContainer.remove(ownComponent);

            if (datasource != null) {
                List<Instance> usedItems = new ArrayList<Instance>();

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
                    f.setHeight("24px");
                    f.setWidth("100%");
                    setTokenStyle(f, itemId);
                    tokensContainer.add(f);
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
        }

        protected TokenListLabel createToken() {
            final TokenListLabel label = new TokenListLabel();
            label.setWidth("100%");
            label.addListener(new RemoveTokenListener() {
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
    }
}
