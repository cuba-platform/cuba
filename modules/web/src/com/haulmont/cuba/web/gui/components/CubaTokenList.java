/*
 * Copyright (c) 2008-2019 Haulmont.
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

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.TokenList;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.web.widgets.CubaScrollBoxLayout;
import com.haulmont.cuba.web.widgets.CubaTokenListLabel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;

public class CubaTokenList<T> extends CustomField<T> {

    protected static final String TOKENLIST_STYLENAME = "c-tokenlist";
    protected static final String TOKENLIST_SCROLLBOX_STYLENAME = "c-tokenlist-scrollbox";

    protected static final String ADD_BTN_STYLENAME = "add-btn";
    protected static final String CLEAR_BTN_STYLENAME = "clear-btn";
    protected static final String INLINE_STYLENAME = "inline";
    protected static final String READONLY_STYLENAME = "readonly";

    protected WebTokenList owner;

    protected VerticalLayout composition;
    protected CubaScrollBoxLayout tokenContainer;
    protected HorizontalLayout editor;

    protected Map<Entity, CubaTokenListLabel> itemComponents = new HashMap<>();
    protected Map<CubaTokenListLabel, Entity> componentItems = new HashMap<>();

    protected Subscription addButtonSub;

    public CubaTokenList(WebTokenList owner) {
        this.owner = owner;

        composition = new VerticalLayout();
        composition.setWidthUndefined();
        composition.setSpacing(false);
        composition.setMargin(false);

        tokenContainer = new CubaScrollBoxLayout();
        tokenContainer.setStyleName(TOKENLIST_SCROLLBOX_STYLENAME);
        tokenContainer.setWidthUndefined();
        tokenContainer.setMargin(new MarginInfo(true, false, false, false));

        composition.addComponent(tokenContainer);
        setPrimaryStyleName(TOKENLIST_STYLENAME);

        // do not trigger overridden method
        super.setWidth(-1, Unit.PIXELS);
    }

    @Override
    public T getValue() {
        return null;
    }

    @Override
    protected void doSetValue(T value) {
    }

    @Override
    public boolean isEmpty() {
        return owner.getValueSource() != null
                ? owner.getValueSourceValue().isEmpty()
                : super.isEmpty();
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        if (height > 0) {
            composition.setHeight("100%");
            composition.setExpandRatio(tokenContainer, 1);
            tokenContainer.setHeight("100%");
        } else {
            composition.setHeightUndefined();
            composition.setExpandRatio(tokenContainer, 0);
            tokenContainer.setHeightUndefined();
        }
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        // workaround for custom field call from constructor
        if (composition != null && tokenContainer != null) {
            if (width > 0) {
                composition.setWidth("100%");
                editor.setWidth("100%");

                if (!owner.isSimple()) {
                    owner.lookupPickerField.setWidthFull();
                    editor.setExpandRatio(WebComponentsHelper.getComposition(owner.lookupPickerField), 1);
                }
            } else {
                composition.setWidthUndefined();
                editor.setWidthUndefined();

                if (!owner.isSimple()) {
                    owner.lookupPickerField.setWidthAuto();
                    editor.setExpandRatio(WebComponentsHelper.getComposition(owner.lookupPickerField), 0);
                }
            }
        }
    }

    @Override
    protected Component initContent() {
        return composition;
    }

    protected void initField() {
        if (editor == null) {
            editor = new HorizontalLayout();
            editor.setSpacing(true);
            editor.setWidthUndefined();
        }
        editor.removeAllComponents();

        if (!owner.isSimple()) {
            owner.lookupPickerField.setWidthAuto();
            editor.addComponent(WebComponentsHelper.getComposition(owner.lookupPickerField));
        }
        owner.lookupPickerField.setVisible(!owner.isSimple());

        owner.addButton.setVisible(owner.isSimple());
        owner.addButton.setStyleName(ADD_BTN_STYLENAME);

        if (addButtonSub != null) {
            addButtonSub.remove();
        }

        if (!owner.isSimple()) {
            addButtonSub = owner.addButton.addClickListener(e -> {
                if (owner.isEditable()) {
                    owner.addValueFromLookupPickerField();
                }
                owner.addButton.focus();
            });
        } else {
            addButtonSub = owner.addButton.addClickListener(e ->
                    owner.openLookup(() -> owner.addButton.focus()));
        }
        editor.addComponent(owner.addButton.unwrap(com.vaadin.ui.Button.class));

        owner.clearButton.setVisible(owner.clearEnabled);
        owner.clearButton.setStyleName(CLEAR_BTN_STYLENAME);
        owner.clearButton.addClickListener(e -> {
            for (CubaTokenListLabel item : new ArrayList<>(itemComponents.values())) {
                doRemove(item);
            }
            owner.clearButton.focus();
        });

        com.vaadin.ui.Button vClearButton = owner.clearButton.unwrap(com.vaadin.ui.Button.class);
        if (owner.isSimple()) {
            HorizontalLayout clearLayout = new HorizontalLayout();
            clearLayout.addComponent(vClearButton);
            editor.addComponent(clearLayout);
            editor.setExpandRatio(clearLayout, 1);
        } else {
            editor.addComponent(vClearButton);
        }
    }

    @SuppressWarnings("unchecked")
    public void refreshComponent() {
        if (owner.inline) {
            addStyleName(INLINE_STYLENAME);
        } else {
            removeStyleName(INLINE_STYLENAME);
        }

        if (owner.editable) {
            removeStyleName(READONLY_STYLENAME);
        } else {
            addStyleName(READONLY_STYLENAME);
        }

        if (editor != null) {
            composition.removeComponent(editor);
        }

        initField();

        if (owner.isEditable()) {
            if (owner.position == TokenList.Position.TOP) {
                composition.addComponentAsFirst(editor);
            } else {
                composition.addComponent(editor);
            }
        }

        tokenContainer.removeAllComponents();

        //noinspection unchecked
        ValueSource<Collection<Entity>> valueSource = owner.getValueSource();

        if (valueSource != null && CollectionUtils.isNotEmpty(valueSource.getValue())) {
            List<Entity> usedItems = new ArrayList<>();

            // New tokens
            for (Entity entity : valueSource.getValue()) {
                CubaTokenListLabel f = itemComponents.get(entity);
                if (f == null) {
                    f = createToken();
                    itemComponents.put(entity, f);
                    componentItems.put(f, entity);
                }
                f.setEditable(owner.isEditable());
                f.setText(owner.getInstanceCaption(entity));
                f.setWidthUndefined();

                setTokenStyle(f, entity.getId());
                tokenContainer.addComponent(f);
                usedItems.add(entity);
            }

            // Remove obsolete items
            for (Entity componentItem : new ArrayList<>(itemComponents.keySet())) {
                if (!usedItems.contains(componentItem)) {
                    componentItems.remove(itemComponents.get(componentItem));
                    itemComponents.remove(componentItem);
                }
            }
        }

        if (getHeight() < 0) {
            tokenContainer.setVisible(!isEmpty());
        } else {
            tokenContainer.setVisible(true);
        }

        updateEditorMargins();

        updateSizes();
    }

    protected void updateEditorMargins() {
        if (tokenContainer.isVisible()) {
            if (owner.position == TokenList.Position.TOP) {
                editor.setMargin(new MarginInfo(false, false, true, false));
            } else {
                editor.setMargin(new MarginInfo(true, false, false, false));
            }
        } else {
            editor.setMargin(false);
        }
    }

    protected void updateSizes() {
        if (getHeight() > 0) {
            composition.setHeight("100%");
            composition.setExpandRatio(tokenContainer, 1);
            tokenContainer.setHeight("100%");
        } else {
            composition.setHeightUndefined();
            composition.setExpandRatio(tokenContainer, 0);
            tokenContainer.setHeightUndefined();
        }

        if (getWidth() > 0) {
            composition.setWidth("100%");
            editor.setWidth("100%");

            if (!owner.isSimple()) {
                owner.lookupPickerField.setWidthFull();
                editor.setExpandRatio(WebComponentsHelper.getComposition(owner.lookupPickerField), 1);
            }
        } else {
            composition.setWidthUndefined();
            editor.setWidthUndefined();

            if (!owner.isSimple()) {
                owner.lookupPickerField.setWidthAuto();
                editor.setExpandRatio(WebComponentsHelper.getComposition(owner.lookupPickerField), 0);
            }
        }
    }

    public void refreshClickListeners(TokenList.ItemClickListener listener) {
        //noinspection unchecked
        ValueSource<Collection<Entity>> valueSource = owner.getValueSource();
        if (valueSource != null
                && CollectionUtils.isNotEmpty(valueSource.getValue())
                && BindingState.ACTIVE == valueSource.getState()) {

            for (Entity entity : valueSource.getValue()) {
                CubaTokenListLabel label = itemComponents.get(entity);
                if (label != null) {
                    if (listener != null) {
                        label.setClickListener(source ->
                                doClick(label));
                    } else {
                        label.setClickListener(null);
                    }
                }
            }
        }
    }

    protected CubaTokenListLabel createToken() {
        CubaTokenListLabel label = new CubaTokenListLabel();
        label.setWidth("100%");
        label.addListener((CubaTokenListLabel.RemoveTokenListener) source -> {
            if (owner.isEditable()) {
                doRemove(source);
            }
        });
        return label;
    }

    @SuppressWarnings("unchecked")
    protected void doRemove(CubaTokenListLabel source) {
        Instance item = componentItems.get(source);
        if (item != null) {
            itemComponents.remove(item);
            componentItems.remove(source);

            if (owner.itemChangeHandler != null) {
                owner.itemChangeHandler.removeItem(item);
            } else {
                ValueSource<Collection<? extends Entity>> valueSource = owner.getValueSource();
                if (valueSource != null) {
                    Collection<Entity> value = owner.getValueSourceValue();

                    value.remove(item);

                    valueSource.setValue(value);
                }
            }
        }
    }

    protected void doClick(CubaTokenListLabel source) {
        if (owner.itemClickListener != null) {
            Instance item = componentItems.get(source);
            if (item != null) {
                owner.itemClickListener.onClick(item);
            }
        }
    }

    protected void setTokenStyle(CubaTokenListLabel label, Object itemId) {
        if (owner.tokenStyleGenerator != null) {
            //noinspection unchecked
            String styleName = ((Function<Object, String>) owner.getTokenStyleGenerator()).apply(itemId);
            if (styleName != null && !styleName.isEmpty()) {
                label.setStyleName(styleName);
            }
        }
    }
}