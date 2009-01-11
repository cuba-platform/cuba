/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:51:01
 * $Id$
 */
package com.haulmont.cuba.gui.xml.data;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.gui.data.impl.DatasourceFactoryImpl;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.Profile;
import org.dom4j.Element;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

public class DatasourcesLoaderTest extends CubaTestCase {
    protected Element loadXml(String resource) {
        final InputStream stream = getClass().getResourceAsStream(resource);

        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        return doc.getRootElement();
    }

    public void testDatasource1() {
        final DatasourceFactoryImpl factory = new DatasourceFactoryImpl();
        
        final DsContextLoader loader = new DsContextLoader(factory);
        final DsContext dsContext = loader.loadDatasources(loadXml("/com/haulmont/cuba/gui/xml/data/server.xml"));
        System.out.println("datasources = " + dsContext);
    }

    public void testDatasource2() {
        final DatasourceFactoryImpl factory = new DatasourceFactoryImpl();

        final DsContextLoader loader = new DsContextLoader(factory);
        final DsContext dsContext = loader.loadDatasources(loadXml("/com/haulmont/cuba/gui/xml/data/security.xml"));

        final Set<Profile> profiles = new HashSet<Profile>();
        profiles.add(new Profile());
        profiles.add(new Profile());

        final User user = new User();
        user.setProfiles(profiles);

        dsContext.setContext(new Context() {
            public <T> T getValue(String property) {
                if ("user".equals(property)) {
                    return (T) user;
                }
                return null;
            }

            public void setValue(String property, Object value) {}

            public void addValueListener(ValueListener listener) {}

            public void removeValueListener(ValueListener listener) {}
        });
        System.out.println("datasources = " + dsContext);
        System.out.println("((CollectionDatasource)datasources.get(\"profiles\")).getItemIds() = " + ((CollectionDatasource) dsContext.get("profiles")).getItemIds());
    }

    public void testDatasource3() {
        final DatasourceFactoryImpl factory = new DatasourceFactoryImpl();

        final DsContextLoader loader = new DsContextLoader(factory);
        final DsContext dsContext = loader.loadDatasources(loadXml("/com/haulmont/cuba/gui/xml/data/security2.xml"));

        final Set<Profile> profiles = new HashSet<Profile>();
        profiles.add(new Profile());
        profiles.add(new Profile());

        final User user = new User();
        user.setProfiles(profiles);

        dsContext.setContext(new Context() {
            public <T> T getValue(String property) {
                if ("user".equals(property)) {
                    return (T) user;
                }
                return null;
            }

            public void setValue(String property, Object value) {}

            public void addValueListener(ValueListener listener) {}

            public void removeValueListener(ValueListener listener) {}
        });

        final CollectionDatasource datasource = dsContext.get("profiles");

        datasource.refresh();
        final Collection itemIds = datasource.getItemIds();

        System.out.println("datasources = " + dsContext);
    }

}
