/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 23.06.2010 11:46:30
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.web.gui.AbstractFieldFactory;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.CheckBox;
import com.haulmont.cuba.web.toolkit.ui.FieldGroup;
import com.haulmont.cuba.web.toolkit.ui.FieldGroupLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;

public class WebFieldGroup extends WebAbstractComponent<FieldGroup> implements com.haulmont.cuba.gui.components.FieldGroup {
    
    private static final long serialVersionUID = 768889467060419241L;

    private Map<String, Field> fields = new LinkedHashMap<String, Field>();
    private Map<Field, Integer> fieldsColumn = new HashMap<Field, Integer>();
    private Map<Integer, List<Field>> columnFields = new HashMap<Integer, List<Field>>();

    private Set<Field> readOnlyFields = new HashSet<Field>();

    private Datasource<Entity> datasource;
    protected Map<MetaClass, CollectionDatasource> optionsDatasources = new HashMap<MetaClass, CollectionDatasource>();

    private String caption;
    private String description;

    private int cols = 1;

    private List<ExpandListener> expandListeners = null;
    private List<CollapseListener> collapseListeners = null;

    private final FieldFactory fieldFactory = new FieldFactory();

    private Item itemWrapper;

    public WebFieldGroup() {
        component = new FieldGroup(fieldFactory) {
            @Override
            public void addField(Object propertyId, com.vaadin.ui.Field field) {
                Field fieldConf = WebFieldGroup.this.getField(propertyId.toString());
                if (fieldConf != null) {
                    int col = fieldsColumn.get(fieldConf);
                    List<Field> colFields = columnFields.get(col);
                    super.addField(propertyId.toString(), field, col, colFields.indexOf(fieldConf));
                } else {
                    super.addField(propertyId.toString(), field, 0);
                }
            }

            @Override
            public void addCustomField(Object propertyId, CustomFieldGenerator fieldGenerator) {
                Field fieldConf = WebFieldGroup.this.getField(propertyId.toString());
                int col = fieldsColumn.get(fieldConf);
                List<Field> colFields = columnFields.get(col);
                super.addCustomField(propertyId, fieldGenerator, col, colFields.indexOf(fieldConf));
            }
        };
        component.setLayout(new FieldGroupLayout());
        component.addListener(new ExpandCollapseListener());
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

    public void addField(Field field) {
        fields.put(field.getId(), field);
    }

    public void addField(Field field, int col) {
        if (col < 0 || col >= cols) {
            throw new IllegalStateException(String.format("Illegal column number %s, available amount of columns is %s",
                    col, cols));
        }
        fieldsColumn.put(field, col);
        List<Field> fields = columnFields.get(col);
        if (fields == null) {
            fields = new ArrayList<Field>();

            columnFields.put(col, fields);
        }
        fields.add(field);

        addField(field);
    }

    public void removeField(Field field) {
        if (fields.remove(field.getId()) != null)  {
            Integer col = fieldsColumn.get(field.getId());

            final List<Field> fields = columnFields.get(col);
            fields.remove(field);
            fieldsColumn.remove(field.getId());
        }
    }

    public float getColumnExpandRatio(int col) {
        return component.getColumnExpandRatio(col);
    }

    public void setColumnExpandRatio(int col, float ratio) {
        component.setColumnExpandRatio(col, ratio);
    }

    public void setCaptionAlignment(FieldCaptionAlignment captionAlignment) {
        FieldGroupLayout layout = component.getLayout();
        layout.setCaptionAlignment(WebComponentsHelper.convertFieldGroupCaptionAlignment(captionAlignment));
    }

    public void addCustomField(final Field field, final CustomFieldGenerator fieldGenerator) {
        if (!field.isCustom()) {
            throw new IllegalStateException(String.format("Field '%s' must by custom", field.getId()));
        }
        component.addCustomField(field.getId(), new FieldGroup.CustomFieldGenerator() {
            public com.vaadin.ui.Field generateField(Item item, Object propertyId, FieldGroup component) {
                Datasource ds;
                if (field.getDatasource() != null) {
                    ds = field.getDatasource();
                } else {
                    ds = datasource;
                }

                Component c;
                com.vaadin.ui.Field f;

                String id = (String) propertyId;
                if (ds.getMetaClass().getProperty(id) != null) {
                   c = fieldGenerator.generateField(ds, propertyId);
                   f = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(c);

                   if (f.getPropertyDataSource() == null) {
                       MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyEx(id);
                       if (field.getDatasource() != null) {
                           final ItemWrapper dsWrapper = createDatasourceWrapper(ds,
                                   Collections.<MetaPropertyPath>singleton(propertyPath));
                           f.setPropertyDataSource(dsWrapper.getItemProperty(propertyPath));
                       } else {
                           f.setPropertyDataSource(itemWrapper.getItemProperty(propertyPath));
                       }
                   }
                } else {
                    c = fieldGenerator.generateField(null, null);
                    f = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(c);
                }

                if (f.getCaption() == null) {
                    if (field.getCaption() != null) {
                        f.setCaption(field.getCaption());
                    } else if (ds.getMetaClass().getProperty(id) != null) {
                        MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyEx(id);
                        f.setCaption(MessageUtils.getPropertyCaption(propertyPath.getMetaClass(),
                                id));
                    }
                }

                if (f.getWidth() == -1f && field.getWidth() != null) {
                    f.setWidth(field.getWidth());
                }

                return f;
            }
        });
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;

        component.setCols(cols);

        Collection<MetaPropertyPath> fields = null;
        if (this.fields.isEmpty() && datasource != null) {//collects fields by entity view
            fields = MetadataHelper.getViewPropertyPaths(datasource.getView(), datasource.getMetaClass());

            for (final MetaPropertyPath propertyPath : new ArrayList<MetaPropertyPath>(fields)) {
                Class<?> propertyType = propertyPath.getMetaProperty().getJavaType();
                if (List.class.isAssignableFrom(propertyType) || Set.class.isAssignableFrom(propertyType)) {
                    fields.remove(propertyPath);
                }
            }

            component.setRows(fields.size());

        } else {
            if (datasource != null) {
                final List<String> fieldIds = new ArrayList<String>(this.fields.keySet());
                fields = new ArrayList<MetaPropertyPath>();
                for (final String id : fieldIds) {
                    final Field field = getField(id);
                    if (field.getDatasource() == null && datasource.getMetaClass().getProperty(field.getId()) != null) {
                        MetaPropertyPath propertyPath = datasource.getMetaClass().getPropertyEx(field.getId());
                        fields.add(propertyPath);
                    }
                }
            }

            component.setRows(rowsCount());
        }

        if (datasource != null) {
            itemWrapper = createDatasourceWrapper(datasource, fields);

            if (!this.fields.isEmpty()) {
                //Removes custom fields from the list. We shouldn't to create components for custom fields
                for (MetaPropertyPath propertyPath : new ArrayList<MetaPropertyPath>(fields)) {
                    final Field field = getField(propertyPath.toString());
                    if (field.isCustom()) {
                        fields.remove(propertyPath);
                    }
                }
            }

            component.setItemDataSource(itemWrapper, fields);
        } else {
            component.setItemDataSource(null, null);
        }

        for (final String id : this.fields.keySet()) {
            final Field fieldConf = getField(id);
            if (!fieldConf.isCustom()) {
                com.vaadin.ui.Field field;
                if (datasource != null && fieldConf.getDatasource() == null) {
                    field = component.getField(id);
                } else if (fieldConf.getDatasource() != null) {
                    MetaPropertyPath propertyPath = fieldConf.getDatasource().getMetaClass().getPropertyEx(fieldConf.getId());
                    final ItemWrapper dsWrapper = createDatasourceWrapper(fieldConf.getDatasource(),
                            Collections.<MetaPropertyPath>singleton(propertyPath));

                    field = fieldFactory.createField(dsWrapper, fieldConf.getId(), component);

                    if (field != null && dsWrapper.getItemProperty(fieldConf.getId()) != null) {
                        field.setPropertyDataSource(dsWrapper.getItemProperty(fieldConf.getId()));
                        component.addField(fieldConf.getId(), field);
                    }
                } else {
                    throw new IllegalStateException();
                }
                if (field != null && fieldConf.getCaption() != null) {
                    field.setCaption(fieldConf.getCaption());
                }
            }
        }
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

    public void addValidator(Field field, final com.haulmont.cuba.gui.components.Field.Validator validator) {
        final com.vaadin.ui.Field f = component.getField(field.getId());
        f.addValidator(new Validator() {
            public void validate(Object value) throws InvalidValueException {
                if ((!f.isRequired() && value == null))
                    return;
                try {
                    validator.validate(value);
                } catch (ValidationException e) {
                    throw new InvalidValueException(e.getMessage());
                }
            }

            public boolean isValid(Object value) {
                try {
                    validate(value);
                    return true;
                } catch (InvalidValueException e) {
                    return false;
                }
            }
        });
        if (f instanceof AbstractField) {
            ((AbstractField) f).setValidationVisible(true);
        }
    }

    public boolean isExpanded() {
        return component.isExpanded();
    }

    public void setExpanded(boolean expanded) {
        component.setExpanded(expanded);
    }

    public boolean isCollapsable() {
        return component.isCollapsable();
    }

    public void setCollapsable(boolean collapsable) {
        component.setCollapsable(collapsable);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        component.setCaption(caption);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        component.setDescription(description);
    }

    public boolean isEditable() {
        return !component.isReadOnly();
    }

    public void setEditable(boolean editable) {
        component.setReadOnly(!editable);
    }

    public boolean isRequired(Field field) {
        com.vaadin.ui.Field f = component.getField(field.getId());
        return f.isRequired();
    }

    public void setRequired(Field field, boolean required, String message) {
        com.vaadin.ui.Field f = component.getField(field.getId());
        f.setRequired(required);
        if (required) {
            f.setRequiredError(message);
        }
    }

    public boolean isEditable(Field field) {
        return !readOnlyFields.contains(field);
    }

    public void setEditable(Field field, boolean editable) {
        com.vaadin.ui.Field f = component.getField(field.getId());
        f.setReadOnly(!editable);
        if (editable) {
            readOnlyFields.remove(field);
        } else {
            readOnlyFields.add(field);
        }
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
                    @Override
                    public boolean isReadOnly() {
                        Field field = fields.get(propertyPath.toString());
                        return !isEditable(field);
                    }
                };
            }
        };
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

    protected void fireExpandListeners() {
        if (expandListeners != null) {
            for (final ExpandListener listener : expandListeners) {
                listener.onExpand(this);
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

    protected void fireCollapseListeners() {
        if (collapseListeners != null) {
            for (final CollapseListener listener : collapseListeners) {
                listener.onCollapse(this);
            }
        }
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
        fieldGroupElement.addAttribute("expanded", String.valueOf(isExpanded()));
        return true;
    }

    protected class ExpandCollapseListener implements FieldGroup.ExpandCollapseListener {
        private static final long serialVersionUID = 4917475472402160597L;

        public void onExpand(FieldGroup component) {
            fireExpandListeners();
        }

        public void onCollapse(FieldGroup component) {
            fireCollapseListeners();
        }
    }

    protected class FieldFactory extends AbstractFieldFactory {
        @Override
        protected Datasource getDatasource() {
            return datasource;
        }

        @Override
        protected void initCommon(com.vaadin.ui.Field field, MetaPropertyPath propertyPath) {
            if (field instanceof TextField) {
                ((TextField) field).setNullRepresentation("");
            } else if (field instanceof DateField && getFormatter(propertyPath) != null) {
                String format = getFormat(propertyPath);
                if (format != null) {
                    ((DateField) field).setDateFormat(format);
                }
            } else if (field instanceof CheckBox) {
                ((CheckBox) field).setLayoutCaption(true); 
            }

            Field fieldConf = getField(propertyPath.toString());
            if (fieldConf != null && fieldConf.getWidth() != null) {
                field.setWidth(fieldConf.getWidth());
            }
        }

        @Override
        protected CollectionDatasource getOptionsDatasource(MetaClass metaClass, MetaPropertyPath propertyPath) {
            final Field field = fields.get(propertyPath.toString());

            Datasource ds = datasource;

            DsContext dsContext;
            if (ds == null) {
                ds = field.getDatasource();
                if (ds == null) {
                    throw new IllegalStateException("FieldGroup datasource is null");
                }
                dsContext = ds.getDsContext();
            } else {
                dsContext = ds.getDsContext();
            }

            String optDsName = field != null
                    ? field.getXmlDescriptor().attributeValue("optionsDatasource")
                    : null;

            if (!StringUtils.isBlank(optDsName)) {
                CollectionDatasource optDs = dsContext.get(optDsName);
                if (optDs == null) {
                    throw new IllegalStateException("Options datasource not found: " + optDsName);
                }
                return optDs;
            } else {
/*
                CollectionDatasource optDs = optionsDatasources.get(metaClass);
                if (optDs != null) return optDs;

                final DataService dataservice = ds.getDataService();

                final String id = metaClass.getName();
                final String viewName = null; //metaClass.getName() + ".lookup";

                optDs = new CollectionDatasourceImpl(dsContext, dataservice, id, metaClass, viewName);
                optDs.refresh();

                optionsDatasources.put(metaClass, optDs);

                return optDs;
*/
                return null;
            }
        }

        @Override
        protected void initValidators(com.vaadin.ui.Field field, MetaPropertyPath propertyPath, boolean validationVisible) {
            //do nothing
        }

        @Override
        protected Collection<com.haulmont.cuba.gui.components.Field.Validator> getValidators(MetaPropertyPath propertyPath) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected boolean required(MetaPropertyPath propertyPath) {
            return false;
        }

        @Override
        protected String requiredMessage(MetaPropertyPath propertyPath) {
            return null;
        }

        @Override
        protected Formatter getFormatter(MetaPropertyPath propertyPath) {
            Field field = fields.get(propertyPath);
            if (field != null) {
                return field.getFormatter();
            } else {
                return null;
            }
        }

        @Override
        protected String getFormat(MetaPropertyPath propertyPath) {
            Field field = fields.get(propertyPath);
            if (field != null) {
                Element formatterElement = field.getXmlDescriptor().element("formatter");
                return formatterElement.attributeValue("format");
            }
            return null;
        }
    }
}