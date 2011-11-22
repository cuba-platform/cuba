/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigLayoutHelper;
import com.haulmont.cuba.desktop.sys.vcl.CollapsiblePanel;
import com.haulmont.cuba.desktop.sys.vcl.ToolTipButton;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.swing.*;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopFieldGroup extends DesktopAbstractComponent<JPanel> implements FieldGroup, AutoExpanding {

    private MigLayout layout;
    private Datasource datasource;
    private int rows;
    private int cols = 1;
    private boolean editable = true;
    private boolean enabled = true;
    private boolean borderVisible = false;

    private Map<String, Field> fields = new LinkedHashMap<String, Field>();
    private Map<Field, Integer> fieldsColumn = new HashMap<Field, Integer>();
    private Map<Field, Component> fieldComponents = new HashMap<Field, Component>();
    private Map<Field, JLabel> fieldLabels = new HashMap<Field, JLabel>();
    private Map<Field, ToolTipButton> fieldTooltips = new HashMap<Field, ToolTipButton>();
    private Map<Integer, List<Field>> columnFields = new HashMap<Integer, List<Field>>();
    private Map<Field, CustomFieldGenerator> generators = new HashMap<Field, CustomFieldGenerator>();
    private AbstractFieldFactory fieldFactory = new FieldFactory();

    private Set<Field> readOnlyFields = new HashSet<Field>();
    private Set<Field> disabledFields = new HashSet<Field>();

    private CollapsiblePanel collapsiblePanel;

    private List<ExpandListener> expandListeners = null;
    private List<CollapseListener> collapseListeners = null;

    private Security security = AppContext.getBean(Security.NAME);

    public DesktopFieldGroup() {
        LC lc = new LC();
        lc.hideMode(3); // Invisible components will not participate in the layout at all and it will for instance not take up a grid cell.
        lc.insets("0 0 0 0");
        if (LayoutAdapter.isDebug())
            lc.debug(1000);

        layout = new MigLayout(lc);
        impl = new JPanel(layout);

        collapsiblePanel = new CollapsiblePanel(super.getComposition());
        collapsiblePanel.setBorderVisible(false);
        collapsiblePanel.addCollapseListener(new CollapsiblePanel.CollapseListener() {
            @Override
            public void collapsed() {
                fireCollapseListeners();
            }

            @Override
            public void expanded() {
                fireExpandListeners();
            }
        });
    }

    @Override
    public boolean isExpanded() {
        return collapsiblePanel.isExpanded();
    }

    @Override
    public void setExpanded(boolean expanded) {
        collapsiblePanel.setExpanded(expanded);
    }

    @Override
    public boolean isCollapsible() {
        return collapsiblePanel.isCollapsable();
    }

    @Override
    public void setCollapsible(boolean collapsable) {
        collapsiblePanel.setCollapsible(collapsable);
    }

    public void addListener(ExpandListener listener) {
        if (expandListeners == null) {
            expandListeners = new ArrayList<ExpandListener>();
        }
        expandListeners.add(listener);
    }

    public void removeListener(ExpandListener listener) {
        if (expandListeners != null) {
            expandListeners.remove(listener);
            if (expandListeners.isEmpty()) {
                expandListeners = null;
            }
        }
    }

    private void fireExpandListeners() {
        if (expandListeners != null) {
            for (final ExpandListener expandListener : expandListeners) {
                expandListener.onExpand(this);
            }
        }
    }

    public void addListener(CollapseListener listener) {
        if (collapseListeners == null) {
            collapseListeners = new ArrayList<CollapseListener>();
        }
        collapseListeners.add(listener);
    }

    public void removeListener(CollapseListener listener) {
        if (collapseListeners != null) {
            collapseListeners.remove(listener);
            if (collapseListeners.isEmpty()) {
                collapseListeners = null;
            }
        }
    }

    private void fireCollapseListeners() {
        if (collapseListeners != null) {
            for (final CollapseListener collapseListener : collapseListeners) {
                collapseListener.onCollapse(this);
            }
        }
    }

    public List<Field> getFields() {
        return new ArrayList<Field>(fields.values());
    }

    public Field getField(String id) {
        for (final Map.Entry<String, Field> entry : fields.entrySet()) {
            if (entry.getKey().equals(id)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void fillColumnFields(int col, Field field) {
        List<Field> fields = columnFields.get(col);
        if (fields == null) {
            fields = new ArrayList<Field>();

            columnFields.put(col, fields);
        }
        fields.add(field);
    }

    public void addField(Field field) {
        if (cols == 0) {
            cols = 1;
        }
        addField(field, 0);
    }

    public void addField(Field field, int col) {
        if (col < 0 || col >= cols) {
            throw new IllegalStateException(String.format("Illegal column number %s, available amount of columns is %s",
                    col, cols));
        }
        fields.put(field.getId(), field);
        fieldsColumn.put(field, col);
        fillColumnFields(col, field);
    }

    public void removeField(Field field) {
        if (fields.remove(field.getId()) != null) {
            Integer col = fieldsColumn.get(field.getId());

            final List<Field> fields = columnFields.get(col);
            fields.remove(field);
            fieldsColumn.remove(field.getId());
        }
    }

    @Override
    public void requestFocus() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (!columnFields.isEmpty()) {
                    List<Field> fields = columnFields.get(0);
                    if (!fields.isEmpty()) {
                        Field f = fields.get(0);
                        Component c = fieldComponents.get(f);
                        if (c != null) {
                            c.requestFocus();
                        }
                    }
                }
            }
        });
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
        if (this.fields.isEmpty() && datasource != null) {
            //collect fields by entity view
            Collection<MetaPropertyPath> fieldsMetaProps = MetadataHelper.getViewPropertyPaths(datasource.getView(), datasource.getMetaClass());
            for (MetaPropertyPath mpp : fieldsMetaProps) {
                MetaProperty mp = mpp.getMetaProperty();
                if (!mp.getRange().getCardinality().isMany() && !MetadataHelper.isSystem(mp)) {
                    Field field = new Field(mpp.toString());
                    addField(field);
                }
            }
        }
        rows = rowsCount();

        createFields();
    }

    public boolean isRequired(Field field) {
        Component component = fieldComponents.get(field);
        return component instanceof com.haulmont.cuba.gui.components.Field && ((com.haulmont.cuba.gui.components.Field) component).isRequired();
    }

    public void setRequired(Field field, boolean required, String message) {
        Component component = fieldComponents.get(field);
        if (component instanceof com.haulmont.cuba.gui.components.Field) {
            ((com.haulmont.cuba.gui.components.Field) component).setRequired(required);
            ((com.haulmont.cuba.gui.components.Field) component).setRequiredMessage(message);
        }
    }

    public boolean isRequired(String fieldId) {
        Field field = fields.get(fieldId);
        return field != null && isRequired(field);
    }

    public void setRequired(String fieldId, boolean required, String message) {
        Field field = fields.get(fieldId);
        if (field != null)
            setRequired(field, required, message);
    }

    public void addValidator(Field field, com.haulmont.cuba.gui.components.Field.Validator validator) {
        Component component = fieldComponents.get(field);
        if (component instanceof com.haulmont.cuba.gui.components.Field) {
            ((com.haulmont.cuba.gui.components.Field) component).addValidator(validator);
        }
    }

    public void addValidator(String fieldId, com.haulmont.cuba.gui.components.Field.Validator validator) {
        Field field = fields.get(fieldId);
        if (fieldId != null)
            addValidator(field, validator);
    }

    public boolean isEditable(Field field) {
        return !readOnlyFields.contains(field);
    }

    public void setEditable(Field field, boolean editable) {
        doSetEditable(field, editable);

        if (editable) {
            readOnlyFields.remove(field);
        } else {
            readOnlyFields.add(field);
        }
    }

    private void doSetEditable(Field field, boolean editable) {
        Component component = fieldComponents.get(field);
        if (component instanceof Editable) {
            ((Editable) component).setEditable(editable);
        }
        if (fieldLabels.containsKey(field)) {
            fieldLabels.get(field).setEnabled(editable);
        }
    }

    public boolean isEditable(String fieldId) {
        Field field = fields.get(fieldId);
        return field != null && isEditable(field);
    }

    public void setEditable(String fieldId, boolean editable) {
        Field field = fields.get(fieldId);
        if (field != null)
            setEditable(field, editable);
    }

    public boolean isEnabled(Field field) {
        Component component = fieldComponents.get(field);
        return component != null && component.isEnabled();
    }

    public void setEnabled(Field field, boolean enabled) {
        doSetEnabled(field, enabled);

        if (enabled)
            disabledFields.remove(field);
        else
            disabledFields.add(field);
    }

    private void doSetEnabled(Field field, boolean enabled) {
        Component component = fieldComponents.get(field);
        if (component != null)
            component.setEnabled(enabled);
        if (fieldLabels.containsKey(field)) {
            fieldLabels.get(field).setEnabled(enabled);
        }
    }

    public boolean isEnabled(String fieldId) {
        Field field = fields.get(fieldId);
        return field != null && isEnabled(field);
    }

    public void setEnabled(String fieldId, boolean enabled) {
        Field field = fields.get(fieldId);
        if (field != null)
            setEnabled(field, enabled);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        for (FieldGroup.Field field : fields.values()) {
            doSetEnabled(field, enabled && !disabledFields.contains(field));
        }
    }

    public boolean isVisible(Field field) {
        Component component = fieldComponents.get(field);
        return component != null && component.isVisible() && isVisible();
    }

    public void setVisible(Field field, boolean visible) {
        Component component = fieldComponents.get(field);
        if (component != null) {
            component.setVisible(visible);
        }
        if (fieldLabels.containsKey(field)) {
            fieldLabels.get(field).setVisible(visible);
        }
    }

    public boolean isVisible(String fieldId) {
        Field field = fields.get(fieldId);
        return field != null && isVisible(field);
    }

    public void setVisible(String fieldId, boolean visible) {
        Field field = fields.get(fieldId);
        if (field != null)
            setVisible(field, visible);
    }

    @Override
    public boolean isBorderVisible() {
        return borderVisible;
    }

    @Override
    public void setBorderVisible(boolean borderVisible) {
        this.borderVisible = borderVisible;
        collapsiblePanel.setBorderVisible(borderVisible);
    }

    public Object getFieldValue(Field field) {
        Component component = fieldComponents.get(field);
        if (component instanceof HasValue) {
            return ((HasValue) component).getValue();
        }
        return null;
    }

    public void setFieldValue(Field field, Object value) {
        Component component = fieldComponents.get(field);
        if (component instanceof HasValue) {
            ((HasValue) component).setValue(value);
        }
    }

    public Object getFieldValue(String fieldId) {
        Field field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        return getFieldValue(field);
    }

    public void setFieldValue(String fieldId, Object value) {
        Field field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        setFieldValue(field, value);
    }

    @Override
    public void setFieldCaption(String fieldId, String caption) {
        Field field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));

        JLabel label = fieldLabels.get(field);
        if (label == null)
            throw new IllegalStateException(String.format("Label for field '%s' not found", fieldId));
        label.setText(caption);
    }

    public void setCaptionAlignment(FieldCaptionAlignment captionAlignment) {
    }

    private int rowsCount() {
        int rowsCount = 0;
        for (final List<Field> fields : columnFields.values()) {
            rowsCount = Math.max(rowsCount, fields.size());
        }
        return rowsCount;
    }

    public int getColumns() {
        return cols;
    }

    public void setColumns(int cols) {
        this.cols = cols;
    }

    public float getColumnExpandRatio(int col) {
        return 0;
    }

    public void setColumnExpandRatio(int col, float ratio) {
    }

    public void addCustomField(String fieldId, CustomFieldGenerator fieldGenerator) {
        Field field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        addCustomField(field, fieldGenerator);
    }

    public void addCustomField(Field field, CustomFieldGenerator fieldGenerator) {
        if (!field.isCustom()) {
            throw new IllegalStateException(String.format("Field '%s' must be custom", field.getId()));
        }
        generators.put(field, fieldGenerator);
        // immediately create field, even before postInit()
        createFieldComponent(field);
    }

    public void postInit() {
    }

    private void createFields() {
        impl.removeAll();
        for (Field field : fields.values()) {
            if (field.isCustom()) {
                continue; // custom field is generated in another method
            }

            createFieldComponent(field);
        }
    }

    private void createFieldComponent(Field field) {
        int col = fieldsColumn.get(field);
        int row = columnFields.get(col).indexOf(field);

        Datasource ds;
        if (field.getDatasource() != null) {
            ds = field.getDatasource();
        } else {
            ds = datasource;
        }

        String caption = null;
        String description = null;

        CustomFieldGenerator generator = generators.get(field);
        if (generator == null)
            generator = createDefaultGenerator(field);

        Component component = generator.generateField(ds, field.getId());
        applyPermissions(component);

        if (component instanceof com.haulmont.cuba.gui.components.Field) { // do not create caption for buttons etc.
            caption = field.getCaption();
            if (caption == null) {
                MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyPath(field.getId());
                if (propertyPath != null)
                    caption = MessageUtils.getPropertyCaption(propertyPath.getMetaClass(), field.getId());
            }

            if (StringUtils.isNotEmpty(((HasCaption) component).getCaption())) {
                caption = ((HasCaption) component).getCaption();     // custom field has manually set caption
            }

            description = field.getDescription();
            if (StringUtils.isNotEmpty(((HasCaption) component).getDescription())) {
                description = ((HasCaption) component).getDescription();  // custom field has manually set description
            } else if (StringUtils.isNotEmpty(description)) {
                ((HasCaption) component).setDescription(description);
            }
        }

        JLabel label = new JLabel(caption);
        label.setVisible(component.isVisible());
        impl.add(label, new CC().cell(col * 3, row, 1, 1));
        fieldLabels.put(field, label);
        if (description != null && !(component instanceof CheckBox)) {
            field.setDescription(description);
            ToolTipButton tooltipBtn = new ToolTipButton();
            tooltipBtn.setVisible(component.isVisible());
            tooltipBtn.setToolTipText(description);
            DesktopToolTipManager.getInstance().registerTooltip(tooltipBtn);
            impl.add(tooltipBtn, new CC().cell(col * 3 + 2, row, 1, 1).alignY("top"));
            fieldTooltips.put(field, tooltipBtn);
        }
        fieldComponents.put(field, component);
        assignTypicalAttributes(component);
        JComponent jComponent = DesktopComponentsHelper.getComposition(component);
        CC cell = new CC().cell(col * 3 + 1, row, 1, 1);

        if (field.getWidth() != null && component.getWidth() == 0 && component.getWidthUnits() == 0) {
            component.setWidth(field.getWidth());
        }
        MigLayoutHelper.applyWidth(cell, (int) component.getWidth(), component.getWidthUnits(), false);
        MigLayoutHelper.applyHeight(cell, (int) component.getHeight(), component.getHeightUnits(), false);
        jComponent.putClientProperty(getSwingPropertyId(), field.getId());
        impl.add(jComponent, cell);
    }

    private void applyPermissions(Component c) {
        if (c instanceof DatasourceComponent) {
            DatasourceComponent dsComponent = (DatasourceComponent) c;
            MetaProperty metaProperty = dsComponent.getMetaProperty();

            if (metaProperty != null) {
                dsComponent.setEditable(security.isEntityAttrModificationPermitted(getDatasource().getMetaClass(), metaProperty)
                        && dsComponent.isEditable());
            }
        }
    }

    private void assignTypicalAttributes(Component c) {
        if (c instanceof BelongToFrame) {
            BelongToFrame belongToFrame = (BelongToFrame) c;
            if (belongToFrame.getFrame() == null) {
                belongToFrame.setFrame(getFrame());
            }
        }
    }

    private CustomFieldGenerator createDefaultGenerator(final Field field) {
        return new CustomFieldGenerator() {
            public Component generateField(Datasource datasource, Object propertyId) {
                Component component = fieldFactory.createField(datasource, (String) propertyId, field.getXmlDescriptor());
                return component;
            }
        };
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;

        for (FieldGroup.Field field : fields.values()) {
            doSetEditable(field, editable && !readOnlyFields.contains(field));
        }
    }

    public String getCaption() {
        return collapsiblePanel.getCaption();
    }

    public void setCaption(String caption) {
        collapsiblePanel.setCaption(caption);
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    public void applySettings(Element element) {
        Element fieldGroupElement = element.element("fieldGroup");
        if (fieldGroupElement != null) {
            String expanded = fieldGroupElement.attributeValue("expanded");
            if (expanded != null) {
                setExpanded(BooleanUtils.toBoolean(expanded));
            }
        }
    }

    public boolean saveSettings(Element element) {
        Element fieldGroupElement = element.element("fieldGroup");
        if (fieldGroupElement != null) {
            element.remove(fieldGroupElement);
        }
        fieldGroupElement = element.addElement("fieldGroup");
        fieldGroupElement.addAttribute("expanded", BooleanUtils.toStringTrueFalse(isExpanded()));
        return true;
    }

    public Collection<Component> getComponents() {
        return fieldComponents.values();
    }

    @Override
    public JComponent getComposition() {
        return collapsiblePanel;
    }

    @Override
    public boolean expandsWidth() {
        return true;
    }

    @Override
    public boolean expandsHeight() {
        return false;
    }

    @Override
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    @Override
    public void validate() throws ValidationException {
        if (!isVisible() || !isEditable() || !isEnabled())
            return;

        Map<Field, Exception> problems = new HashMap<Field, Exception>();

        for (Map.Entry<Field, Component> componentEntry : fieldComponents.entrySet()) {
            Field field = componentEntry.getKey();
            Component component = componentEntry.getValue();

            if (!isEditable(field) || !isEnabled(field) || !isVisible(field))
                continue;

            // If has valid state
            if ((component instanceof Validatable) &&
                    (component instanceof Editable)) {
                // If editable
                if (component.isVisible() &&
                        component.isEnabled() &&
                        ((Editable) component).isEditable()) {

                    try {
                        ((Validatable) component).validate();
                    } catch (ValidationException ex) {
                        problems.put(field, ex);
                    }
                }
            }
        }

        if (!problems.isEmpty()) {
            StringBuilder msgBuilder = new StringBuilder(
                    MessageProvider.getMessage(DesktopWindow.class, "validationFail") + "<br>");
            for (Iterator<Field> iterator = problems.keySet().iterator(); iterator.hasNext(); ) {
                Field field = iterator.next();
                Exception ex = problems.get(field);
                msgBuilder.append(ex.getMessage());
                if (iterator.hasNext())
                    msgBuilder.append("<br>");
            }

            FieldsValidationException validationException = new FieldsValidationException(msgBuilder.toString());
            validationException.setProblemFields(problems);
            throw validationException;
        }
    }

    protected class FieldFactory extends AbstractFieldFactory {

        @Override
        protected CollectionDatasource getOptionsDatasource(Datasource datasource, String property) {
            final Field field = fields.get(property);

            DsContext dsContext;
            if (datasource == null) {
                if (field.getDatasource() == null) {
                    throw new IllegalStateException("FieldGroup datasource is null");
                }
                dsContext = field.getDatasource().getDsContext();
            } else {
                dsContext = datasource.getDsContext();
            }
            Element descriptor = field.getXmlDescriptor();
            String optDsName = descriptor == null ? null : descriptor.attributeValue("optionsDatasource");

            if (!StringUtils.isBlank(optDsName)) {
                CollectionDatasource optDs = dsContext.get(optDsName);
                if (optDs == null) {
                    throw new IllegalStateException("Options datasource not found: " + optDsName);
                }
                return optDs;
            } else {
                return null;
            }
        }
    }
}
