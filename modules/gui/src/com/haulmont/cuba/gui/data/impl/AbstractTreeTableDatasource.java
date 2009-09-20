package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.TreeTableDatasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.bali.datastruct.Node;

import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Nikolay Gorodnov
 * Date: 03.06.2009
 */
public abstract class AbstractTreeTableDatasource<T extends Entity<K>, K>
        extends AbstractTreeDatasource<T, K>
        implements TreeTableDatasource<T, K>
{
    private Log log = LogFactory.getLog(AbstractTreeTableDatasource.class);

    private static class TreeTableNodeComparator implements Comparator<Node> {
        private final EntityComparator entityComparator;

        private TreeTableNodeComparator(MetaPropertyPath propertyPath, boolean asc) {
            entityComparator = new EntityComparator(propertyPath, asc);
        }

        public int compare(Node n1, Node n2) {
            Entity e1 = (Entity) n1.getData();
            Entity e2 = (Entity) n2.getData();
            return entityComparator.compare(e1, e2);
        }
    }

    protected AbstractTreeTableDatasource(
            DsContext context,
            DataService dataservice,
            String id,
            MetaClass metaClass,
            String viewName
    ) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    protected void doSort() {
        if (tree == null) {
            log.warn("AbstractTreeTableDatasource.doSort: Tree is null, exiting");
            return;
        }

        final MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
        final boolean asc = Order.ASC.equals(sortInfos[0].getOrder());

        for (Node<T> rootNode : tree.getRootNodes()) {
            sortNodeContent(rootNode, propertyPath, asc);
        }

        data.clear();
        for (Node<T> node : tree.toList()) {
            final T entity = node.getData();
            final K id = entity.getId();

            data.put(id, entity);
        }
    }

    private void sortNodeContent(Node<T> node, MetaPropertyPath propertyPath, boolean asc) {
        Collections.sort(node.getChildren(), new TreeTableNodeComparator(propertyPath, asc));
        for (Node<T> n : node.getChildren()) {
            sortNodeContent(n, propertyPath, asc);
        }
    }
}
