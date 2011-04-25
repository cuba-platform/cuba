/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.impl.StringDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.data.Datasource;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.dom4j.Element;

import javax.swing.*;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopFieldGroup extends DesktopAbstractComponent<JPanel> implements FieldGroup {

    private MigLayout layout;
    private Datasource datasource;
    private int rows;
    private int cols;
    private String caption;

    private Map<String, Field> fields = new LinkedHashMap<String, Field>();
    private Map<Field, Integer> fieldsColumn = new HashMap<Field, Integer>();
    private Map<Integer, List<Field>> columnFields = new HashMap<Integer, List<Field>>();
    private Map<Field, CustomFieldGenerator> generators = new HashMap<Field, CustomFieldGenerator>();
    private FieldFactory fieldFactory = new FieldFactory();

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
        return false;
    }

    public void setRequired(Field field, boolean required, String message) {
    }

    public boolean isRequired(String fieldId) {
        return false;
    }

    public void setRequired(String fieldId, boolean required, String message) {
    }

    public void addValidator(Field field, com.haulmont.cuba.gui.components.Field.Validator validator) {
    }

    public void addValidator(String fieldId, com.haulmont.cuba.gui.components.Field.Validator validator) {
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
        return false;
    }

    public void setEditable(Field field, boolean editable) {
    }

    public boolean isEditable(String fieldId) {
        return false;
    }

    public void setEditable(String fieldId, boolean editable) {
    }

    public boolean isEnabled(Field field) {
        return false;
    }

    public void setEnabled(Field field, boolean enabled) {
    }

    public boolean isEnabled(String fieldId) {
        return false;
    }

    public void setEnabled(String fieldId, boolean enabled) {
    }

    public boolean isVisible(Field field) {
        return false;
    }

    public void setVisible(Field field, boolean visible) {
    }

    public boolean isVisible(String fieldId) {
        return false;
    }

    public void setVisible(String fieldId, boolean visible) {
    }

    public Object getFieldValue(Field field) {
        return null;
    }

    public void setFieldValue(Field field, Object value) {
    }

    public Object getFieldValue(String fieldId) {
        return null;
    }

    public void setFieldValue(String fieldId, Object value) {
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
                caption = MessageUtils.getPropertyCaption(ds.getMetaClass(), field.getId());
            }
            JLabel label = new JLabel(caption);
            impl.add(label, new CC().cell(col*2, row, 1, 1));

            CustomFieldGenerator generator = generators.get(field);
            if (generator == null)
                generator = createDefaultGenerator(field);

            Component component = generator.generateField(ds, field.getId());
            JComponent jComponent = DesktopComponentsHelper.unwrap(component);

            impl.add(jComponent, new CC().cell(col*2+1, row, 1, 1));
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
}
