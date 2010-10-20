/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 20.10.2010 16:12:23
 *
 * $Id$
 */
package cuba.client.web.ui.report.edit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.components.*;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.vaadin.ui.AbstractSelect;

import java.util.Map;

public class ValueFormatEditor extends BasicEditor {
    private static String[] defaultFormats = new String[]{
            "#,##0",
            "##,##0",
            "#,##0.###",
            "#,##0.##",
            "dd/MM/yyyy HH:mm"
    };

    FilterSelect fSelect = null;

    public ValueFormatEditor(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params){
        super.init(params);
        final FieldGroup fields = getComponent("formatFields");
        // Add default format strings to combobox
        FieldGroup.Field f = fields.getField("formatString");
        fields.addCustomField(f, new FieldGroup.CustomFieldGenerator() {
            public Component generateField(Datasource datasource, Object propertyId) {
                final WebLookupField lookupField = new WebLookupField();
                fSelect = (FilterSelect) WebComponentsHelper.unwrap(lookupField);
                final FilterSelect select = fSelect;
                for (String format : defaultFormats) {
                    select.addItem(format);
                    select.setItemCaption(format,format);
                }
                                
                select.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_EXPLICIT);
                select.setNewItemsAllowed(true);
                select.setNewItemHandler(new AbstractSelect.NewItemHandler() {
                    public void addNewItem(String newItemCaption) {
                        select.addItem(newItemCaption);
                        select.setItemCaption(newItemCaption,newItemCaption);
                        select.select(newItemCaption);
                    }
                });
                return lookupField;
            }
        });  
    }

    @Override
    public void setItem(Entity item){
        super.setItem(item);
        Object value = fSelect.getValue();
        fSelect.addItem(value);
        fSelect.setItemCaption(value,value.toString());
        fSelect.select(value);
    }
}
