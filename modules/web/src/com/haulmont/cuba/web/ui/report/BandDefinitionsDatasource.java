/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 13:18:51
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui.report;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;
import com.haulmont.cuba.report.BandDefinition;
import com.haulmont.cuba.report.Report;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BandDefinitionsDatasource extends AbstractTreeDatasource<BandDefinition, UUID> {

    private DataService dataService;

    public BandDefinitionsDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
        dataService = getDataService();
    }

    @Override
    protected Tree<BandDefinition> loadTree(Map<String, Object> params) {
        Tree<BandDefinition> tree = new Tree<BandDefinition>();
        Datasource ds = getDsContext().get("reportDs");

        Report report = (Report) ds.getItem();
        BandDefinition rootBandDefinition = report.getRootBandDefinition();
        if (rootBandDefinition != null) {
            Node<BandDefinition> rootNode = createNode(rootBandDefinition);
            tree.setRootNodes(Collections.<Node<BandDefinition>>singletonList(rootNode));
        }

        return tree;
    }

    private Node<BandDefinition> createNode(BandDefinition bandDefinition) {
        if (!PersistenceHelper.isNew(bandDefinition)) {
            bandDefinition = dataService.reload(bandDefinition, "report.edit");
        }
        Node<BandDefinition> node = new Node<BandDefinition>();
        node.setData(bandDefinition);

        List<BandDefinition> children = bandDefinition.getChildrenBandDefinitions();
        if (children != null)
            for (BandDefinition definition : children) {
                Node<BandDefinition> _node = createNode(definition);
                node.addChild(_node);
            }
        return node;
    }
}
