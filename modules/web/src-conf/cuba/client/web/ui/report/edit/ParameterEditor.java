/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 26.05.2010 16:07:12
 *
 * $Id$
 */
package cuba.client.web.ui.report.edit;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.report.ParameterType;
import com.haulmont.cuba.report.ReportInputParameter;

import java.util.*;

public class ParameterEditor extends AbstractEditor {
    public ParameterEditor(IFrame frame) {
        super(frame);
    }

    private ReportInputParameter parameter;
    private LookupField screen;
    private CheckBox fromBrowser;
    private HashMap<String, String> metaNamesToClassNames = new HashMap<String, String>();
    private HashMap<String, String> classNamesToMetaNames = new HashMap<String, String>();

    @Override
    public void setItem(Entity item) {
        super.setItem(item);
        parameter = (ReportInputParameter) getItem();
        boolean isEntity = ParameterType.ENTITY.equals(parameter.getType());
        screen.setEnabled(isEntity);
        fromBrowser.setEnabled(isEntity);
        screen.setValue(metaNamesToClassNames.get(parameter.getScreen()));
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        LookupField type = getComponent("type");
        screen = getComponent("screen");
        fromBrowser = getComponent("getFromBrowser");

        List lst = new ArrayList();
        Collection<MetaClass> classes = MetadataProvider.getSession().getClasses();
        for (MetaClass clazz : classes) {
            metaNamesToClassNames.put(clazz.getName(), clazz.getJavaClass().getSimpleName());
            classNamesToMetaNames.put(clazz.getJavaClass().getSimpleName(), clazz.getName());
        }
        lst.addAll(classNamesToMetaNames.keySet());

        screen.setOptionsList(lst);

        screen.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                String metaClassName = value != null ? classNamesToMetaNames.get(value.toString()) : null;
                parameter.setScreen(metaClassName);
                if (metaClassName != null) {
                    MetaClass metaClass = MetadataProvider.getSession().getClass(metaClassName);
                    parameter.setClassName(metaClass.getJavaClass().getCanonicalName());
                }

            }
        });

        type.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                boolean isEntity = ParameterType.ENTITY.equals(value) || ParameterType.ENTITY_LIST.equals(value);
                screen.setEnabled(isEntity);
                fromBrowser.setEnabled(isEntity);
            }
        });
    }
}
