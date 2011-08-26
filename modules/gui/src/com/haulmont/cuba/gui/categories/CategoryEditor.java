/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.categories;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import org.apache.commons.lang.BooleanUtils;


import javax.swing.table.TableColumn;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class CategoryEditor extends AbstractEditor {
    private Category category;
    private CheckBox cb;
    private DataService dataService;

    public CategoryEditor(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        dataService = getDsContext().getDataService();
        cb = getComponent("isDefault");

    }

    public void setItem(Entity item) {
        super.setItem(item);
        category = (Category) getItem();
        generateEntityTypeField();
        initCb();
    }

    private void generateEntityTypeField(){

        boolean hasValue = (category.getEntityType() == null) ? (false) : (true);

        LookupField categoryEntityTypeField = getComponent("entityType");
        Map<String,Object> options = new HashMap<String,Object>();
        MetaClass entityType = null;
        for (MetaClass metaClass : MetadataHelper.getAllPersistentMetaClasses()) {
            if (CategorizedEntity.class.isAssignableFrom(metaClass.getJavaClass())) {
                options.put(MessageUtils.getEntityCaption(metaClass), metaClass);
                if (hasValue && metaClass.getName().equals(category.getEntityType())) {
                    entityType = metaClass;
                }
            }
        }
        categoryEntityTypeField.setOptionsMap(options);
        categoryEntityTypeField.setValue(entityType);
        categoryEntityTypeField.addListener(new ValueListener(){
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                category.setEntityType(((MetaClass)value).getName());
            }
        });
    }

    private void initCb() {
        cb.setValue(BooleanUtils.isTrue(category.getIsDefault()));
        cb.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (Boolean.TRUE.equals(value)) {
                    LoadContext categoriesContext = new LoadContext(category.getClass());
                    LoadContext.Query query = categoriesContext.setQueryString("select c from sys$Category c where c.entityType= :entityType and not c.id=:id");
                    categoriesContext.setView("category.defaultEdit");
                    query.addParameter("entityType", category.getEntityType());
                    query.addParameter("id", category.getId());
                    List<Category> categories = dataService.loadList(categoriesContext);
                    for(Category cat : categories){
                        cat.setIsDefault(false);
                    }
                    CommitContext commitContext = new CommitContext(categories);
                    dataService.commit(commitContext);
                    category.setIsDefault(true);
                }
                else if(Boolean.FALSE.equals(value)){
                    category.setIsDefault(false);
                }
            }
        });
    }

}
