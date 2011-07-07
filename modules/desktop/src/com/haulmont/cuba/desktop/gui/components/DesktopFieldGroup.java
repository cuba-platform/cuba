/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigLayoutHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
    private int cols;
    private String caption;

    private Map<String, Field> fields = new LinkedHashMap<String, Field>();
    private Map<Field, Integer> fieldsColumn = new HashMap<Field, Integer>();
    private Map<Field, Component> fieldComponents = new HashMap<Field, Component>();
    private Map<Field, JLabel> fieldLabels = new HashMap<Field, JLabel>();
    private Map<Integer, List<Field>> columnFields = new HashMap<Integer, List<Field>>();
    private Map<Field, CustomFieldGenerator> generators = new HashMap<Field, CustomFieldGenerator>();
    private AbstractFieldFactory fieldFactory = new FieldFactory();

    public DesktopFieldGroup() {
        LC lc = new LC();
        lc.insets("panel");
        if (LayoutAdapter.isDebug())
            lc.debug(1000);

        layout = new MigLayout(lc);
        impl = new JPanel(layout);
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
        fields.put(field.getId(), field);
        fieldsColumn.put(field, 0);
        fillColumnFields(0, field);
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

    public boolean isCollapsable() {
        return false;
    }

    public void setCollapsable(boolean collapsable) {
    }

    public boolean isExpanded() {
        return false;
    }

    public void setExpanded(boolean expanded) {
    }

    public boolean isEditable(Field field) {
        Component component = fieldComponents.get(field);
        return component instanceof Editable && ((Editable) component).isEditable();
    }

    public void setEditable(Field field, boolean editable) {
        Component component = fieldComponents.get(field);
        if (component instanceof Editable) {
            ((Editable) component).setEditable(editable);
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
        Component component = fieldComponents.get(field);
        if (component != null)
            component.setEnabled(enabled);
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

    public boolean isVisible(Field field) {
        Component component = fieldComponents.get(field);
        return component != null && component.isVisible();
    }

    public void setVisible(Field field, boolean visible) {
        Component component = fieldComponents.get(field);
        if (component != null)
            component.setVisible(visible);
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
    }

    public void addListener(ExpandListener listener) {
    }

    public void removeListener(ExpandListener listener) {
    }

    public void addListener(CollapseListener listener) {
    }

    public void removeListener(CollapseListener listener) {
    }

    public void postInit() {
        int row = -1;
        int col = -1;
        for (Field field : fields.values()) {
            if (fieldsColumn.get(field) != col) {
                row = 0;
            } else {
                row++;
            }
            col = fieldsColumn.get(field);

            Datasource ds;
            if (field.getDatasource() != null) {
                ds = field.getDatasource();
            } else {
                ds = datasource;
            }

            String caption = field.getCaption();
            if (caption == null) {
                MetaProperty metaProperty = ds.getMetaClass().getProperty(field.getId());
                if (metaProperty != null)
                    caption = MessageUtils.getPropertyCaption(metaProperty);
            }
            JLabel label = new JLabel(caption);
            impl.add(label, new CC().cell(col*2, row, 1, 1));
            fieldLabels.put(field, label);

            CustomFieldGenerator generator = generators.get(field);
            if (generator == null)
                generator = createDefaultGenerator(field);

            Component component = generator.generateField(ds, field.getId());
            fieldComponents.put(field, component);
            assignTypicalAttributes(component);
            JComponent jComponent = DesktopComponentsHelper.unwrap(component);

            CC cell = new CC().cell(col * 2 + 1, row, 1, 1);

            if (field.getWidth() != null) {
                component.setWidth(field.getWidth());
                MigLayoutHelper.applyWidth(cell, (int) component.getWidth(), component.getWidthUnits(), false);
            }

            impl.add(jComponent, cell);
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

    private CustomFieldGenerator createDefaultGenerator(Field field) {
        return new CustomFieldGenerator() {
            public Component generateField(Datasource datasource, Object propertyId) {
                Component component = fieldFactory.createField(datasource, (String) propertyId);
                return component;
            }
        };
    }

    public boolean isEditable() {
        return true;
    }

    public void setEditable(boolean editable) {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        TitledBorder titledBorder = BorderFactory.createTitledBorder(caption);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitlePosition(TitledBorder.TOP);
        titledBorder.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(java.awt.Color.gray),
                        BorderFactory.createEmptyBorder(0,5,5,5)
                )
        );
        titledBorder.setTitleFont(UIManager.getLookAndFeelDefaults().getFont("Panel.font"));
        impl.setBorder(titledBorder);
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    public void applySettings(Element element) {
    }

    public boolean saveSettings(Element element) {
        return false;
    }

    public Collection<Component> getComponents() {
        return fieldComponents.values();
    }

    @Override
    public boolean expandsWidth() {
        return true;
    }

    @Override
    public boolean expandsHeight() {
        return false;
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
