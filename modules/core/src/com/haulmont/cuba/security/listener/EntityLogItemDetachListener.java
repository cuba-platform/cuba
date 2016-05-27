/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.security.listener;

import com.google.common.collect.Ordering;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.listener.BeforeDetachEntityListener;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;
import com.haulmont.cuba.security.entity.EntityLogAttr;
import com.haulmont.cuba.security.entity.EntityLogItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("cuba_EntityLogItemDetachListener")
public class EntityLogItemDetachListener implements BeforeDetachEntityListener<EntityLogItem> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected Persistence persistence;

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

    protected void fillAttributesFromChangesField(EntityLogItem item) {
        log.trace("fillAttributesFromChangesField for " + item);
        List<EntityLogAttr> attributes = new ArrayList<>();

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

        Collections.sort(attributes, new Comparator<EntityLogAttr>() {
            @Override
            public int compare(EntityLogAttr o1, EntityLogAttr o2) {
                return Ordering.natural().compare(o1.getName(), o2.getName());
            }
        });

        item.setAttributes(new LinkedHashSet<>(attributes));
    }

    protected void fillAttributesFromTable(EntityLogItem item, EntityManager entityManager) {
        log.trace("fillAttributesFromTable for " + item);
        DbTypeConverter converter = persistence.getDbTypeConverter();
        QueryRunner queryRunner = new QueryRunner();
        try {
            Set<EntityLogAttr> attributes = queryRunner.query(
                    entityManager.getConnection(),
                    "select * from SEC_ENTITY_LOG_ATTR where ITEM_ID = ?",
                    new Object[] {converter.getSqlObject(item.getId())},
                    new AttributesResultSetHandler(item, converter)
            );

            List<EntityLogAttr> attributesList = new ArrayList<>(attributes);
            Collections.sort(attributesList, new Comparator<EntityLogAttr>() {
                @Override
                public int compare(EntityLogAttr o1, EntityLogAttr o2) {
                    return Ordering.natural().compare(o1.getName(), o2.getName());
                }
            });

            item.setAttributes(new LinkedHashSet<>(attributes));
        } catch (SQLException e) {
            log.error("Unable to load EntityLog attributes for " + item, e);
        }
    }

    protected static class AttributesResultSetHandler implements ResultSetHandler<Set<EntityLogAttr>> {

        protected EntityLogItem item;

        protected DbTypeConverter converter;

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