/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.11.2008 13:23:09
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.sys.ManagedPersistenceProvider;
import com.haulmont.cuba.core.sys.EntityManagerFactoryImpl;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.global.HsqlDbDialect;
import com.haulmont.cuba.core.global.PostgresDbDialect;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.HSQLDictionary;
import org.apache.openjpa.jdbc.sql.PostgresDictionary;

import java.lang.annotation.Annotation;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Entry point to middleware persistence functionality.
 * <p>
 * Main purpose is to create references to {@link com.haulmont.cuba.core.EntityManager}.
 * Also has some helper methods.
 */
public abstract class PersistenceProvider
{
    private static PersistenceProvider instance;

    public static final String PERSISTENCE_XML = "cuba.PersistenceXml";
    public static final String PERSISTENCE_UNIT = "cuba.PersistenceUnit";

    protected static final String DEFAULT_PERSISTENCE_XML = "META-INF/cuba-persistence.xml";
    protected static final String DEFAULT_PERSISTENCE_UNIT = "cuba";

    private DbDialect dbDialect;

    private static PersistenceProvider getInstance() {
        if (instance == null) {
            instance = new ManagedPersistenceProvider(Locator.getJndiContext());
        }
        return instance;
    }

    /**
     * Path to persistence.xml which is currently in use
     */
    public static String getPersistenceXmlPath() {
        String xmlPath = System.getProperty(PERSISTENCE_XML);
        if (StringUtils.isBlank(xmlPath))
            xmlPath = DEFAULT_PERSISTENCE_XML;
        return xmlPath;
    }

    /**
     * Persistence unit name which is currently in use
     */
    public static String getPersistenceUnitName() {
        String unitName = System.getProperty(PERSISTENCE_UNIT);
        if (StringUtils.isBlank(unitName))
            unitName = DEFAULT_PERSISTENCE_UNIT;
        return unitName;
    }

    /**
     * The DB dialect instance
     */
    public static DbDialect getDbDialect() {
        return getInstance().__getDbDialect();
    }

    /**
     * Current {@link com.haulmont.cuba.core.EntityManagerFactory} instance
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return getInstance().__getEntityManagerFactory();
    }

    /**
     * Returns a reference to EntityManager<p>
     * Inside JTA transaction returns existing or creates new transaction-bound EntityManager,
     * which will be closed on transaction commit/rollback.<br>
     * Outside a transaction always creates new EntityManager which must be closed explicitly after use.
     */
    public static EntityManager getEntityManager() {
        return getInstance().__getEntityManager();
    }

    /**
     * Returns the set of dirty fields (fields changed since last load from DB)
     * @param entity entity instance
     */
    public static Set<String> getDirtyFields(BaseEntity entity) {
        if (!(entity instanceof PersistenceCapable))
            return Collections.emptySet();

        OpenJPAStateManager stateManager = (OpenJPAStateManager) ((PersistenceCapable) entity).pcGetStateManager();
        if (stateManager == null)
            return Collections.emptySet();

        Set<String> set = new HashSet<String>();
        BitSet dirtySet = stateManager.getDirty();
        for (int i = 0; i < dirtySet.size()-1; i++) {
            if (dirtySet.get(i)) {
                FieldMetaData field = stateManager.getMetaData().getField(i);
                set.add(field.getName());
            }
        }
        return set;
    }

    /**
     * Global soft deletion sign. If true, each new {@link com.haulmont.cuba.core.EntityManager}
     * will be created with the same SoftDeletion sign value.
     */
    public static boolean isSoftDeletion() {
        return getInstance().__isSoftDeletion();
    }

    /**
     * Global soft deletion sign. If true, each new {@link com.haulmont.cuba.core.EntityManager}
     * will be created with the same SoftDeletion sign value.
     */
    public static void setSoftDeletion(boolean value) {
        getInstance().__setSoftDeletion(value);
    }

    protected DbDialect __getDbDialect() {
        if (dbDialect == null) {
            OpenJPAEntityManagerFactory factory = ((EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory()).getDelegate();
            OpenJPAConfiguration configuration = ((OpenJPAEntityManagerFactorySPI) factory).getConfiguration();
            if (configuration instanceof JDBCConfiguration) {
                DBDictionary dictionary = ((JDBCConfiguration) configuration).getDBDictionaryInstance();
                if (dictionary instanceof HSQLDictionary) {
                    dbDialect = new HsqlDbDialect();
                } else if (dictionary instanceof PostgresDictionary) {
                    dbDialect = new PostgresDbDialect();
                } else {
                    throw new UnsupportedOperationException("Unsupported DBDictionary class: " + dictionary.getClass());
                }
            } else {
                throw new UnsupportedOperationException("Unsupported OpenJPAConfiguration class: " + configuration.getClass());
            }
        }
        return dbDialect;
    }

    protected abstract EntityManagerFactory __getEntityManagerFactory();

    protected abstract EntityManager __getEntityManager();

    protected abstract boolean __isSoftDeletion();

    protected abstract void __setSoftDeletion(boolean value);
}
