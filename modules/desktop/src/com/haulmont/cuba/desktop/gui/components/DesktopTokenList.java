/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigBoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigLayoutHelper;
import com.haulmont.cuba.desktop.sys.vcl.ExtFlowLayout;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DesktopTokenList extends DesktopAbstractField<JPanel> implements TokenList {

    protected TokenStyleGenerator tokenStyleGenerator;

    protected CollectionDatasource datasource;

    protected String captionProperty;

    protected CaptionMode captionMode;

    protected Position position = Position.TOP;

    protected ItemChangeHandler itemChangeHandler;

    protected ItemClickListener itemClickListener;

    protected boolean inline;

    protected TokenListImpl rootPanel;

    protected DesktopButton addButton;

    protected DesktopButton clearButton;

    protected DesktopLookupPickerField lookupPickerField;

    protected String lookupScreen;

    protected WindowManager.OpenType lookupOpenMode = WindowManager.OpenType.THIS_TAB;

    protected Map<String, Object> lookupScreenParams;

    protected DialogParams lookupScreenDialogParams;

    protected boolean lookup = false;

    protected boolean clearEnabled = true;

    protected boolean editable = true;

    protected boolean simple = false;

    protected AfterLookupCloseHandler afterLookupCloseHandler;

    protected AfterLookupSelectionHandler afterLookupSelectionHandler;

    protected boolean multiselect;

    protected PickerField.LookupAction lookupAction;

    protected CollectionDatasource.CollectionChangeListener collectionChangeListener;

    protected final ValueChangeListener lookupSelectListener = e -> {
        if (isEditable()) {
            addValueFromLookupPickerField();
        }
    };

    public DesktopTokenList() {
        rootPanel = new TokenListImpl();

        impl = rootPanel.getImpl();
        addButton = new DesktopButton();

        Messages messages = AppBeans.get(Messages.NAME);
        addButton.setCaption(messages.getMessage(TokenList.class, "actions.Add"));

        clearButton = new DesktopButton();
        clearButton.setCaption(messages.getMessage(TokenList.class, "actions.Clear"));

        lookupPickerField = new DesktopLookupPickerField();
        lookupPickerField.addValueChangeListener(lookupSelectListener);

        setMultiSelect(false);
        setWidth(Component.AUTO_SIZE);
        setHeight(Component.AUTO_SIZE);
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
    public MetaPropertyPath getMetaPropertyPath() {
        return null;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        throw new UnsupportedOperationException("TokenList does not support datasource with property");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;

        collectionChangeListener = e -> {
            if (lookupPickerField != null) {
                if (isLookup()) {
                    if (getLookupScreen() != null)
                        lookupAction.setLookupScreen(getLookupScreen());
                    else
                        lookupAction.setLookupScreen(null);

                    lookupAction.setLookupScreenOpenType(lookupOpenMode);
                    lookupAction.setLookupScreenParams(lookupScreenParams);
                    lookupAction.setLookupScreenDialogParams(lookupScreenDialogParams);
                }
            }
            rootPanel.refreshComponent();
            rootPanel.refreshClickListeners(itemClickListener);
        };
        datasource.addCollectionChangeListener(new WeakCollectionChangeListener(datasource, collectionChangeListener));
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
    public void setRefreshOptionsOnLookupClose(boolean refresh) {
        lookupPickerField.setRefreshOptionsOnLookupClose(refresh);
    }

    @Override
    public boolean isRefreshOptionsOnLookupClose() {
        return lookupPickerField.isRefreshOptionsOnLookupClose();
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
    public Map<String, ?> getOptionsMap() {
        return lookupPickerField.getOptionsMap();
    }

    @Override
    public void setOptionsMap(Map<String, ?> map) {
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
            rootPanel.refreshComponent();
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

            lookupAction.setAfterLookupCloseHandler((window, actionId) -> {
                if (afterLookupCloseHandler != null) {
                    afterLookupCloseHandler.onClose(window, actionId);
                }
            });

            lookupAction.setAfterLookupSelectionHandler(items -> {
                if (afterLookupSelectionHandler != null) {
                    afterLookupSelectionHandler.onSelect(items);
                }
            });
        }
        this.lookup = lookup;

        rootPanel.refreshComponent();
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
    public void setLookupScreenDialogParams(DialogParams dialogparams) {
        this.lookupScreenDialogParams = dialogparams;
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
    }

    @Override
    public boolean isSimple() {
        return simple;
    }

    @Override
    public void setSimple(boolean simple) {
        this.simple = simple;
        this.addButton.setVisible(simple);
        this.rootPanel.editor = null;
        this.rootPanel.refreshComponent();
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
        this.rootPanel.editor = null;
        this.rootPanel.refreshComponent();
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
        this.rootPanel.editor = null;
        this.rootPanel.refreshComponent();
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
        return this.itemClickListener;
    }

    @Override
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.rootPanel.refreshClickListeners(itemClickListener);
    }

    @Override
    public AfterLookupCloseHandler getAfterLookupCloseHandler() {
        return afterLookupCloseHandler;
    }

    @Override
    public void setAfterLookupCloseHandler(AfterLookupCloseHandler afterLookupCloseHandler) {
        this.afterLookupCloseHandler = afterLookupCloseHandler;
    }

    @Override
    public AfterLookupSelectionHandler getAfterLookupSelectionHandler() {
        return afterLookupSelectionHandler;
    }

    @Override
    public void setAfterLookupSelectionHandler(AfterLookupSelectionHandler afterLookupSelectionHandler) {
        this.afterLookupSelectionHandler = afterLookupSelectionHandler;
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
        return rootPanel.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        rootPanel.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return getImpl().getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        getImpl().setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        if (datasource != null) {
            List<Object> items = new ArrayList<>(datasource.getItems());
            return (T) items;
        } else {
            return (T) Collections.emptyList();
        }
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
        LoggerFactory.getLogger(DesktopTokenList.class).warn("addValueChangeListener not implemented for TokenList");
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        LoggerFactory.getLogger(DesktopTokenList.class).warn("removeValueChangeListener not implemented for TokenList");
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;

        rootPanel.refreshComponent();
    }

    @Override
    protected void updateEnabled() {
        super.updateEnabled();

        if (lookupPickerField != null) {
            lookupPickerField.setParentEnabled(isEnabledWithParent());
        }
        if (addButton != null) {
            addButton.setParentEnabled(isEnabledWithParent());
        }

        if (clearButton != null) {
            clearButton.setParentEnabled(isEnabledWithParent());
        }

        if (rootPanel != null) {
            rootPanel.refreshComponent();
        }
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
        private DesktopButton openButton;

        private boolean editable;
        private boolean enabled;

        private List<RemoveTokenListener> listeners;
        private ClickListener clickListener;

        public TokenListLabel() {
            label = new DesktopLabel();
            label.setHeight("24px");
            composition = new DesktopHBox();

            composition.setSpacing(true);
            composition.add(label);
            removeButton = new DesktopButton();
            removeButton.getImpl().setPreferredSize(new Dimension(0, 24));
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

            JButton button = (JButton) removeButton.getComponent();
            button.setBorder(new EmptyBorder(0, 3, 0, 3));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            openButton = new DesktopButton();
            openButton.getImpl().setPreferredSize(new Dimension(0, 24));
            openButton.setAction(new AbstractAction("actions.Open") {
                @Override
                public void actionPerform(Component component) {
                    fireClickListener();
                }

                @Override
                public String getCaption() {
                    return "";
                }
            });
            openButton.setIcon("/components/pickerfield/images/open-btn.png");
            composition.add(openButton);
            openButton.setVisible(false);

            button = (JButton) openButton.getComponent();
            button.setBorder(new EmptyBorder(0, 3, 0, 3));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            impl = (JLabel) label.getComponent();
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

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            removeButton.setEnabled(enabled);
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        public void addListener(RemoveTokenListener listener) {
            if (listeners == null) {
                listeners = new ArrayList<>();
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

        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
            openButton.setVisible(clickListener != null);
        }

        private void fireRemoveListeners() {
            if (listeners != null) {
                for (final RemoveTokenListener listener : listeners) {
                    listener.removeToken(this);
                }
            }
        }

        private void fireClickListener() {
            if (clickListener != null)
                clickListener.onClick(this);
        }
    }

    public interface RemoveTokenListener {
        void removeToken(TokenListLabel source);
    }

    public interface ClickListener {
        void onClick(TokenListLabel source);
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
                    addValueFromLookupPickerField();
                }
            }
        }

        private void openLookupWindow() {
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
            params.put("windowOpener", DesktopTokenList.this.getFrame().getId());
            if (isMultiSelect()) {
                WindowParams.MULTI_SELECT.set(params, true);
                // for backward compatibility
                params.put("multiSelect", "true");
            }
            if (lookupScreenParams != null) {
                params.putAll(lookupScreenParams);
            }

            WindowManager wm = DesktopComponentsHelper.getTopLevelFrame(DesktopTokenList.this).getWindowManager();
            if (lookupOpenMode == WindowManager.OpenType.DIALOG) {
                DialogParams dialogParams = wm.getDialogParams();

                dialogParams.setResizable(true);
                if (lookupScreenDialogParams != null) {
                    dialogParams.setWidth(lookupScreenDialogParams.getWidth());
                    dialogParams.setHeight(lookupScreenDialogParams.getHeight());
                } else {
                    dialogParams.setWidth(800);
                    dialogParams.setHeight(600);
                }
            }

            Window.Lookup lookupWindow = wm.openLookup(windowInfo, new Window.Lookup.Handler() {
                @Override
                public void handleLookup(Collection items) {
                    if (lookupPickerField.isRefreshOptionsOnLookupClose()) {
                        lookupPickerField.getOptionsDatasource().refresh();
                    }

                    if (isEditable()) {
                        if (items == null || items.isEmpty()) return;
                        for (final Object item : items) {
                            if (itemChangeHandler != null) {
                                itemChangeHandler.addItem(item);
                            } else {
                                datasource.addItem((Entity) item);
                            }
                        }

                        if (afterLookupSelectionHandler != null) {
                            afterLookupSelectionHandler.onSelect(items);
                        }
                    }
                }
            }, lookupOpenMode, params);

            if (afterLookupCloseHandler != null) {
                lookupWindow.addCloseListener(actionId ->
                        afterLookupCloseHandler.onClose(lookupWindow, actionId)
                );
            }
        }
    }

    @Override
    public void setHeight(String height) {
        float oldHeight = getHeight();

        super.setHeight(height);

        rootPanel.setHeight(height);

        if ((getHeight() > 0 && oldHeight < 0)
                || (getHeight() < 0 && oldHeight > 0)) {
            rootPanel.refreshComponent();
        }
    }

    @Override
    public void setWidth(String width) {
        float oldWidth = getWidth();

        super.setWidth(width);

        rootPanel.setWidth(width);

        if ((getWidth() > 0 && oldWidth < 0)
                || (getWidth() < 0 && oldWidth > 0)) {
            rootPanel.refreshComponent();
        }
    }

    @Override
    public void setParent(Component parent) {
        super.setParent(parent);

        rootPanel.setParent(parent);
    }

    @Override
    public void setContainer(DesktopContainer container) {
        super.setContainer(container);

        rootPanel.setContainer(container);
    }

    public class TokenListImpl extends DesktopVBox {
        private DesktopScrollBoxLayout scrollContainer;
        private JPanel tokensContainer;

        private DesktopHBox editor;

        private String caption;

        private Map<Instance, TokenListLabel> itemComponents = new HashMap<>();
        private Map<TokenListLabel, Instance> componentItems = new HashMap<>();

        private TokenListImpl() {
            setSpacing(true);
            setExpanded(true);

            scrollContainer = new DesktopScrollBoxLayout();
            tokensContainer = new JPanel();
        }

        protected void initField() {
            DesktopHBox hBox = new DesktopHBox();
            hBox.setSpacing(true);
            hBox.setWidth("100%");

            if (!isSimple()) {
                if (lookupPickerField.getParent() instanceof Container) {
                    ((Container) lookupPickerField.getParent()).remove(lookupPickerField);
                }
                lookupPickerField.setWidth("100%");
                hBox.add(lookupPickerField);
                hBox.expand(lookupPickerField);
            }
            addButton.setAction(new AddAction());

            lookupPickerField.setVisible(!isSimple());
            addButton.setVisible(isSimple());

            addButton.setStyleName("add-btn");
            if (addButton.getParent() instanceof Container) {
                ((Container) addButton.getParent()).remove(addButton);
            }
            hBox.add(addButton);

            clearButton.setAction(new AbstractAction("actions.Clear") {
                @Override
                public String getCaption() {
                    return clearButton.getCaption();
                }

                @Override
                public void actionPerform(Component component) {
                    for (TokenListLabel item : new ArrayList<>(itemComponents.values())) {
                        doRemove(item);
                    }
                }
            });
            clearButton.setVisible(clearEnabled);
            clearButton.setStyleName("clear-btn");
            if (clearButton.getParent() instanceof Container) {
                ((Container) clearButton.getParent()).remove(clearButton);
            }
            if (isSimple()) {
                DesktopHBox clearBox = new DesktopHBox();
                clearBox.add(clearButton);
                hBox.add(clearBox);
                hBox.expand(clearBox);
            } else {
                hBox.add(clearButton);
            }

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
            if (editor != null) {
                remove(editor);
            }

            if (editor == null) {
                initField();
            }

            if (isEditable()) {
                if (position == Position.TOP) {
                    add(editor, 0);
                } else {
                    add(editor);
                }
            }

            if (inline) {
                tokensContainer.setLayout(new ExtFlowLayout());
            } else {
                BoxLayoutAdapter tcLayoutAdapter = MigBoxLayoutAdapter.create(tokensContainer);
                tcLayoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);
            }

            CC tokensContainerCC = new CC();
            if ((getHeight() < 0 || getWidth() < 0) && !inline) {
                MigLayoutHelper.applyWidth(tokensContainerCC, -1, UNITS_PIXELS, false);
            } else {
                MigLayoutHelper.applyWidth(tokensContainerCC, 100, UNITS_PERCENTAGE, false);
            }

            MigLayoutHelper.applyHeight(tokensContainerCC, -1, UNITS_PIXELS, false);

            tokensContainer.removeAll();

            remove(scrollContainer);
            scrollContainer.removeAll();
            impl.remove(tokensContainer);

            if (getHeight() < 0 || getWidth() < 0) {
                impl.add(tokensContainer, tokensContainerCC);
            } else {
                DesktopVBox scrollWrap = new DesktopVBox();
                scrollWrap.setHeight(AUTO_SIZE);
                scrollWrap.getImpl().add(tokensContainer, tokensContainerCC);

                scrollContainer.setWidth("100%");
                scrollContainer.setHeight("100%");

                if (inline) {
                    scrollContainer.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.VERTICAL);
                } else {
                    scrollContainer.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.BOTH);
                }

                scrollContainer.remove(scrollWrap);
                scrollContainer.add(scrollWrap);

                add(scrollContainer);
            }

            if (datasource != null) {
                List<Instance> usedItems = new ArrayList<>();

                // New tokens
                for (Object itemId : datasource.getItemIds()) {
                    Instance item = datasource.getItem(itemId);
                    TokenListLabel f = itemComponents.get(item);
                    if (f == null) {
                        f = createToken();
                        itemComponents.put(item, f);
                        componentItems.put(f, item);
                    }

                    f.setEnabled(DesktopTokenList.this.isEnabledWithParent());
                    f.setEditable(isEditable());
                    f.setValue(instanceCaption(item));

                    setTokenStyle(f, itemId);

                    if (tokensContainer.getLayout() instanceof MigLayout) {
                        CC tokenCC = new CC();
                        MigLayoutHelper.applyWidth(tokenCC, -1, UNITS_PIXELS, false);
                        MigLayoutHelper.applyHeight(tokenCC, -1, UNITS_PIXELS, false);

                        tokensContainer.add(f.getComposition(), tokenCC);
                    } else {
                        tokensContainer.add(f.getComposition());
                    }

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

            tokensContainer.revalidate();
            tokensContainer.repaint();

            requestRepaint();
            requestContainerUpdate();
        }

        public void refreshClickListeners(ItemClickListener listener) {
            if (datasource != null && CollectionDatasource.State.VALID.equals(datasource.getState())) {
                for (Object id : datasource.getItemIds()) {
                    Instance item = datasource.getItem(id);
                    final TokenListLabel label = itemComponents.get(item);
                    if (label != null) {
                        if (listener == null) {
                            label.setClickListener(null);
                        } else {
                            label.setClickListener(new ClickListener() {
                                @Override
                                public void onClick(TokenListLabel source) {
                                    doClick(label);
                                }
                            });
                        }
                    }
                }
            }
        }

        protected TokenListLabel createToken() {
            TokenListLabel label = new TokenListLabel();
            label.setWidth(Component.AUTO_SIZE);
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

        private void doClick(TokenListLabel source) {
            if (itemClickListener != null) {
                Instance item = componentItems.get(source);
                if (item != null)
                    itemClickListener.onClick(item);
            }
        }
    }

    protected void addValueFromLookupPickerField() {
        final Entity newItem = lookupPickerField.getValue();
        if (newItem == null) return;
        if (itemChangeHandler != null) {
            itemChangeHandler.addItem(newItem);
        } else {
            if (datasource != null && !datasource.getItems().contains(newItem)) {
                datasource.addItem(newItem);
            }
        }
        lookupPickerField.setValue(null);

        if (lookupPickerField.isRefreshOptionsOnLookupClose()) {
            for (Object obj : getDatasource().getItems()) {
                Entity entity = (Entity) obj;
                if (getOptionsDatasource().containsItem(entity.getId())) {
                    datasource.updateItem(getOptionsDatasource().getItem(entity.getId()));
                }
            }
        }
    }

    @Override
    protected boolean isEmpty(Object value) {
        return super.isEmpty(value) || (value instanceof Collection && ((Collection) value).isEmpty());
    }
}