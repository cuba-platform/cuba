/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.listener;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.listener.BeforeDetachEntityListener;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;
import com.haulmont.cuba.security.entity.EntityLogAttr;
import com.haulmont.cuba.security.entity.EntityLogItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityLogItemDetachListener implements BeforeDetachEntityListener<EntityLogItem> {

    private Log log = LogFactory.getLog(getClass());

    protected DbTypeConverter converter = AppBeans.get(Persistence.class).getDbTypeConverter();

    @Override
    public void onBeforeDetach(EntityLogItem item, EntityManager entityManager) {
        if (item.getAttributes() != null)
            return;

        if (StringUtils.isBlank(item.getChanges())) {
            fillAttributesFromTable(item, entityManager);
        } else {
            fillAttributesFromChangesField(item);
        }
    }

    private void fillAttributesFromChangesField(EntityLogItem item) {
        log.trace("fillAttributesFromChangesField for " + item);
        Set<EntityLogAttr> attributes = new HashSet<>();

        StringReader reader = new StringReader(item.getChanges());
        Properties properties = new Properties();
        try {
            properties.load(reader);
            Enumeration<?> names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                if (name.endsWith(EntityLogAttr.VALUE_ID_SUFFIX) || name.endsWith(EntityLogAttr.MP_SUFFIX))
                    continue;

                EntityLogAttr attr = new EntityLogAttr();
                attr.setLogItem(item);
                attr.setName(name);
                attr.setValue(properties.getProperty(name));

                String id = properties.getProperty(name + EntityLogAttr.VALUE_ID_SUFFIX);
                if (id != null)
                    attr.setValueId(UuidProvider.fromString(id));

                String mp = properties.getProperty(name + EntityLogAttr.MP_SUFFIX);
                if (mp != null)
                    attr.setMessagesPack(mp);

                attributes.add(attr);
            }
        } catch (Exception e) {
            log.error("Unable to fill EntityLog attributes for " + item, e);
        }

        item.setAttributes(attributes);
    }

    private void fillAttributesFromTable(EntityLogItem item, EntityManager entityManager) {
        log.trace("fillAttributesFromTable for " + item);
        QueryRunner queryRunner = new QueryRunner();
        try {
            Set<EntityLogAttr> attributes = queryRunner.query(
                    entityManager.getConnection(),
                    "select * from SEC_ENTITY_LOG_ATTR where ITEM_ID = ?",
                    new Object[] {converter.getSqlObject(item.getId())},
                    new AttributesResultSetHandler(item, converter)
            );
            item.setAttributes(attributes);
        } catch (SQLException e) {
            log.error("Unable to load EntityLog attributes for " + item, e);
        }
    }

    private static class AttributesResultSetHandler implements ResultSetHandler<Set<EntityLogAttr>> {

        private EntityLogItem item;

        private DbTypeConverter converter;

        public AttributesResultSetHandler(EntityLogItem item, DbTypeConverter converter) {
            this.item = item;
            this.converter = converter;
        }

        @Override
        public Set<EntityLogAttr> handle(ResultSet rs) throws SQLException {
            HashSet<EntityLogAttr> attributes = new HashSet<>();
            while (rs.next()) {
                EntityLogAttr attr = new EntityLogAttr();
                attr.setId((UUID) converter.getJavaObject(rs, rs.findColumn("ID")));
                attr.setLogItem(item);
                attr.setName(rs.getString("NAME"));
                attr.setValue(rs.getString("VALUE"));
                attr.setValueId((UUID) converter.getJavaObject(rs, rs.findColumn("VALUE_ID")));
                attr.setMessagesPack(rs.getString("MESSAGES_PACK"));

                attributes.add(attr);
            }
            return attributes;
        }
    }
}
