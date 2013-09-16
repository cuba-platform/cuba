/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * {@link ConditionsTree} container for web-client tree table component.
 *
 * @author krivopustov
 * @version $Id$
 */
class ConditionsContainer implements Container.Hierarchical, Container.Sortable, Container.ItemSetChangeNotifier {

    private static final Log log = LogFactory.getLog(ConditionsContainer.class);

    public static final String NAME_PROP_ID = "name";
    public static final String OP_PROP_ID = "op";
    public static final String PARAM_PROP_ID = "param";
    public static final String HIDDEN_PROP_ID = "hidden";
    public static final String REQUIRED_PROP_ID = "required";
    public static final String CONTROL_PROP_ID = "control";

    public static final Collection<String> PROP_IDS = Collections.unmodifiableCollection(Arrays.asList(
            NAME_PROP_ID,
            OP_PROP_ID,
            PARAM_PROP_ID,
            HIDDEN_PROP_ID,
            REQUIRED_PROP_ID,
            CONTROL_PROP_ID
    ));

    private ConditionsTree conditions;

    private List<Container.ItemSetChangeListener> listeners = new ArrayList<>();

    private ItemSetChangeEvent itemSetChangeEvent = new ItemSetChangeEvent() {
        @Override
        public Container getContainer() {
            return ConditionsContainer.this;
        }
    };

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        return PROP_IDS;
    }

    @Override
    public Object nextItemId(Object itemId) {
        List<Node<AbstractCondition>> nodes = conditions.toList();
        int idx = nodes.indexOf(itemId);
        return idx == nodes.size() - 1 ? null : nodes.get(idx + 1);
    }

    @Override
    public Object prevItemId(Object itemId) {
        List<Node<AbstractCondition>> nodes = conditions.toList();
        int idx = nodes.indexOf(itemId);
        return idx <= 0 ? null : nodes.get(idx - 1);
    }

    @Override
    public Object firstItemId() {
        return conditions.getRootNodes().isEmpty() ? null : conditions.getRootNodes().get(0);
    }

    @Override
    public Object lastItemId() {
        List<Node<AbstractCondition>> nodes = conditions.toList();
        return nodes.isEmpty() ? null : nodes.get(nodes.size() - 1);
    }

    @Override
    public boolean isFirstId(Object itemId) {
        return ObjectUtils.equals(itemId, firstItemId());
    }

    @Override
    public boolean isLastId(Object itemId) {
        return ObjectUtils.equals(itemId, lastItemId());
    }

    @Override
    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        return null;
    }

    public ConditionsContainer(ConditionsTree conditions) {
        this.conditions = conditions;
    }

    @Override
    public Collection<?> getChildren(Object itemId) {
        return ((Node) itemId).getChildren();
    }

    @Override
    public Object getParent(Object itemId) {
        return ((Node) itemId).getParent();
    }

    @Override
    public Collection<?> rootItemIds() {
        return conditions.getRootNodes();
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        return ((Node<AbstractCondition>) itemId).getData().isGroup();
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean isRoot(Object itemId) {
        return ((Node) itemId).getParent() == null;
    }

    @Override
    public boolean hasChildren(Object itemId) {
        return !((Node) itemId).getChildren().isEmpty();
    }

    @Override
    public Item getItem(Object itemId) {
        return new ConditionItem(((Node<AbstractCondition>) itemId).getData());
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return PROP_IDS;
    }

    @Override
    public Collection<?> getItemIds() {
        return conditions.toList();
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        return getItem(itemId).getItemProperty(propertyId);
    }

    private CheckBox createHiddenCheckbox(final AbstractCondition condition) {
        final CheckBox checkBox = new CheckBox();
        checkBox.setImmediate(true);
        checkBox.setValue(condition.isHidden());
        checkBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean hidden = BooleanUtils.isTrue(checkBox.getValue());
                condition.setHidden(hidden);
            }
        });
        return checkBox;
    }

    private CheckBox createRequiredCheckbox(final AbstractCondition condition) {
        final CheckBox checkBox = new CheckBox();
        checkBox.setImmediate(true);
        checkBox.setValue(condition.isRequired());
        checkBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean required = BooleanUtils.isTrue(checkBox.getValue());
                condition.setRequired(required);
            }
        });
        return checkBox;
    }

    private Button createDeleteConditionBtn(final AbstractCondition condition) {
        Button delBtn = WebComponentsHelper.createButton("icons/item-remove.png");
        delBtn.setStyleName(BaseTheme.BUTTON_LINK);
        delBtn.addStyleName("icon-autosize");
        delBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                deleteCondition(condition);
            }
        });
        return delBtn;
    }

    private void deleteCondition(AbstractCondition condition) {
        Node<AbstractCondition> node = conditions.getNode(condition);
        removeItem(node);

//        updateControls();
    }

    @Override
    public Class<?> getType(Object propertyId) {
        if (propertyId.equals(NAME_PROP_ID)) {
            return NameEditor.class;

        } else if (propertyId.equals(OP_PROP_ID)) {
            return OperationEditor.Editor.class;

        } else if (propertyId.equals(PARAM_PROP_ID)) {
            return ParamEditor.class;

        } else if (propertyId.equals(HIDDEN_PROP_ID)) {
            return CheckBox.class;

        } else if (propertyId.equals(REQUIRED_PROP_ID)) {
            return CheckBox.class;

        } else if (propertyId.equals(CONTROL_PROP_ID)) {
            return Button.class;
        }
        return null;
    }

    @Override
    public int size() {
        return conditions.toList().size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return conditions.toList().contains(itemId);
    }

    private void fireItemSetChanged() {
        if (UI.getCurrent().getConnectorTracker().isWritingResponse()) {
            log.debug("Suppress containerItemSetChange listeners during painting, undefined behavior may be occured");
            return;
        }

        for (ItemSetChangeListener listener : listeners) {
            listener.containerItemSetChange(itemSetChangeEvent);
        }
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        Node<AbstractCondition> node = (Node<AbstractCondition>) itemId;
        if (node.getParent() == null)
            conditions.getRootNodes().add(node);
        fireItemSetChanged();
        return getItem(itemId);
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        return null;
    }

    @Override
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        for (Node<AbstractCondition> node : conditions.toList()) {
            if (itemId.equals(node)) {
                if (node.getParent() == null)
                    conditions.getRootNodes().remove(node);
                else
                    node.getParent().getChildren().remove(node);

                fireItemSetChanged();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        return false;
    }

    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);

    }

    @Override
    public void addListener(ItemSetChangeListener listener) {
        addItemSetChangeListener(listener);
    }

    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void removeListener(ItemSetChangeListener listener) {
        removeItemSetChangeListener(listener);
    }

    public void moveUp(Node<AbstractCondition> node) {
        List<Node<AbstractCondition>> siblings = node.getParent() == null ?
                conditions.getRootNodes() : node.getParent().getChildren();

        int idx = siblings.indexOf(node);
        if (idx > 0) {
            Node<AbstractCondition> prev = siblings.get(idx - 1);
            siblings.set(idx - 1, node);
            siblings.set(idx, prev);
            fireItemSetChanged();
        }
    }

    public void moveDown(Node<AbstractCondition> node) {
        List<Node<AbstractCondition>> siblings = node.getParent() == null ?
                conditions.getRootNodes() : node.getParent().getChildren();

        int idx = siblings.indexOf(node);
        if (idx < siblings.size() - 1) {
            Node<AbstractCondition> next = siblings.get(idx + 1);
            siblings.set(idx + 1, node);
            siblings.set(idx, next);
            fireItemSetChanged();
        }
    }

    private class ConditionItem implements Item {

        private AbstractCondition condition;

        public ConditionItem(AbstractCondition condition) {
            this.condition = condition;
        }

        @Override
        public Property getItemProperty(Object id) {
            if (id.equals(NAME_PROP_ID)) {
                return new ObjectProperty<>(new NameEditor(condition));

            } else if (id.equals(OP_PROP_ID)) {
                return new ObjectProperty<>((OperationEditor.Editor) condition.createOperationEditor().getImpl());

            } else if (id.equals(PARAM_PROP_ID)) {
                ParamEditor paramEditor = new ParamEditor(condition, false, false);
                // pack editor component to table cell
                paramEditor.setWidth(100, Sizeable.Unit.PERCENTAGE);
                paramEditor.setFieldWidth("100%");
                return new ObjectProperty<>(paramEditor);

            } else if (id.equals(HIDDEN_PROP_ID)) {
                return new ObjectProperty<>(createHiddenCheckbox(condition));

            } else if (id.equals(REQUIRED_PROP_ID)) {
                return new ObjectProperty<>(createRequiredCheckbox(condition));

            } else if (id.equals(CONTROL_PROP_ID)) {
                return new ObjectProperty<>(createDeleteConditionBtn(condition));
            }
            return null;
        }

        @Override
        public Collection<?> getItemPropertyIds() {
            return PROP_IDS;
        }

        @Override
        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            return false;
        }
    }
}