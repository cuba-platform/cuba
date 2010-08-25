/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.03.2009 14:44:01
 * $Id$
 */

package com.haulmont.cuba.web.app.ui.security.role.edit;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.*;

import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.web.gui.components.*;
import com.vaadin.ui.*;

public class PermissionsLookup extends AbstractLookup {

    protected Tree entityPermissionsTree;
    protected String type;
    @SuppressWarnings({"unchecked"})
    protected LinkedList<PermissionConfig.Target> targets;

    public PermissionsLookup(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        entityPermissionsTree = getComponent("permissions-tree");

        @SuppressWarnings({"unchecked"})
        CollectionDatasource<PermissionConfig.Target, String> entityPermissionsDs =
                entityPermissionsTree.getDatasource();

        entityPermissionsDs.refresh();
        entityPermissionsTree.expandTree();

        targets = new LinkedList<PermissionConfig.Target>();

        addListener(new CloseListener(){
            public void windowClosed(String actionId) {
                if("select".equals(actionId))
                    getLookupHandler().handleLookup(targets);
            }
        });

        Button checkAll = getComponent("checkAll");
        if(checkAll != null)
            checkAll.setAction(new AbstractAction("checkAll"){
                public void actionPerform(Component component) {
                    HierarchicalDatasource datasource = (HierarchicalDatasource)entityPermissionsTree.getDatasource();
                    if(datasource != null){
                        for (String uuid : (Collection<String>) datasource.getItemIds() ){
                            PermissionConfig.Target target = (PermissionConfig.Target)datasource.getItem(uuid);
                            if (!targets.contains(target)&&target.getValue() != null)
                                targets.add(target);
                        }
                        entityPermissionsTree.refresh();
                    }
                }

                @Override
                public String getCaption() {
                    return "";
                }
            });

        Button removeCheckAll = getComponent("removeCheckAll");
        if(removeCheckAll != null)
            removeCheckAll.setAction(new AbstractAction("removeCheckAll"){
                public void actionPerform(Component component) {
                    targets.clear();
                    entityPermissionsTree.refresh();
                }
    
                @Override
                public String getCaption() {
                    return "";
                }
            });

        if(entityPermissionsTree instanceof WidgetsTree){
            WebComponentsHelper.unwrap(entityPermissionsTree).addStyleName("empty");
            ((WidgetsTree)entityPermissionsTree).setWidgetBuilder(new WebWidgetsTree.WidgetBuilder() {
                public Component build(HierarchicalDatasource datasource, Object itemId, boolean leaf){
                    final PermissionConfig.Target target = (PermissionConfig.Target)datasource.getItem(itemId);
                    WebHBoxLayout hLayout= new WebHBoxLayout();
                    WebLabel labelCaption = new WebLabel();
                    labelCaption.setValue(target.getCaption());
                    WebCheckBox checkBox = null;
                    if(target.getValue() != null){
                        checkBox= new WebCheckBox();
                        final com.vaadin.ui.CheckBox vCheckBox = (com.vaadin.ui.CheckBox) WebComponentsHelper.unwrap(checkBox);
                        if(targets.contains(target)){
                            checkBox.setValue(true);
                        }

                        vCheckBox.addListener(new com.vaadin.ui.CheckBox.ClickListener() {
                            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                                if((Boolean)vCheckBox.getValue()){
                                    targets.add(target);
                                }else{
                                    targets.remove(target);
                                }
                            }
                        });
                    }
                    hLayout.add(labelCaption);
                    if(checkBox != null)
                        hLayout.add(checkBox);
                    hLayout.setSpacing(true);
                    return hLayout ;
                }
            });
        }

        Label permissionsType = getComponent("permissionsType");
        type = (String)params.get("param$PermissionValue");
        if(PermissionValue.ALLOW.name().equals(type)){
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class,"PermissionValue.ALLOW"));
        }else if(PermissionValue.DENY.name().equals(type)){
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class,"PermissionValue.DENY"));
        }else if (PropertyPermissionValue.MODIFY.name().equals(type)){
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class,"PropertyPermissionValue.MODIFY"));
        }else if(PropertyPermissionValue.VIEW.name().equals(type)){
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class,"PropertyPermissionValue.VIEW"));
        }else if("FORBID".equals(type)){
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class,"PropertyPermissionValue.DENY"));
        } else{
            permissionsType.setVisible(false);
        }
    }
}
