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
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.report.ParameterType;
import com.haulmont.cuba.report.ReportInputParameter;

import java.util.*;

public class ParameterEditor extends AbstractEditor {
    public ParameterEditor(IFrame frame) {
        super(frame);
    }

    private ReportInputParameter parameter;
    private LookupField metaClass;
    private LookupField screen;
    private CheckBox fromBrowser;
    private HashMap<String, String> metaNamesToClassNames = new HashMap<String, String>();
    private HashMap<String, String> classNamesToMetaNames = new HashMap<String, String>();

    @Override
    public void setItem(Entity item) {
        super.setItem(item);
        parameter = (ReportInputParameter) getItem();
        boolean isEntity = ParameterType.ENTITY.equals(parameter.getType()) || ParameterType.ENTITY_LIST.equals(parameter.getType());
        metaClass.setEnabled(isEntity);
        screen.setEnabled(isEntity);
        fromBrowser.setEnabled(isEntity);
        metaClass.setValue(metaNamesToClassNames.get(parameter.getEntityMetaClass()));
        screen.setValue(parameter.getScreen());
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        LookupField type = getComponent("type");
        metaClass = getComponent("metaClass");
        screen = getComponent("screen");
        fromBrowser = getComponent("getFromBrowser");

        List lst = new ArrayList();
        Collection<MetaClass> classes = MetadataProvider.getSession().getClasses();
        for (MetaClass clazz : classes) {
            metaNamesToClassNames.put(clazz.getName(), clazz.getJavaClass().getSimpleName());
            classNamesToMetaNames.put(clazz.getJavaClass().getSimpleName(), clazz.getName());
        }
        lst.addAll(classNamesToMetaNames.keySet());
        metaClass.setOptionsList(lst);
        metaClass.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                String metaClassName = value != null ? classNamesToMetaNames.get(value.toString()) : null;
                parameter.setEntityMetaClass(metaClassName);
                if (metaClassName != null) {
                    MetaClass metaClass = MetadataProvider.getSession().getClass(metaClassName);
                    parameter.setClassName(metaClass.getJavaClass().getCanonicalName());
                }

            }
        });

        Collection<WindowInfo> windowInfoCollection = AppConfig.getInstance().getWindowConfig().getWindows();
        lst = new ArrayList();
        for (WindowInfo windowInfo : windowInfoCollection) {
            lst.add(windowInfo.getId());
        }
        screen.setOptionsList(lst);
        screen.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                parameter.setScreen(value != null ? value.toString() : null);
            }
        });

        type.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                boolean isEntity = ParameterType.ENTITY.equals(value) || ParameterType.ENTITY_LIST.equals(value);
                metaClass.setEnabled(isEntity);
                screen.setEnabled(isEntity);
                fromBrowser.setEnabled(isEntity);
            }
        });
    }
}
