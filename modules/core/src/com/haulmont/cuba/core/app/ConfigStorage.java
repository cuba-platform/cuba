/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 18:02:20
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.DataServiceRemote;
import com.haulmont.cuba.core.entity.Config;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.sys.ConfigWorker;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.Permission;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

public class ConfigStorage extends ManagementBean implements ConfigStorageMBean
{
    private UUID roleId;
    private UUID permissionId;

    public void create() {
    }

    public void start() {
        loadSystemProperties();
    }

    public String printProperties() {
        return printProperties(null);
    }

    public String printProperties(String prefix) {
        Transaction tx = Locator.createTransaction();
        try {
            login();
            StringBuilder sb = new StringBuilder();

            EntityManager em = PersistenceProvider.getEntityManager();
            String s = String.format("select c from core$Config c %s",
                    (prefix == null ? "" : "where c.name like ?1"));
            Query query = em.createQuery(s);
            if (prefix != null) {
                query.setParameter(1, prefix);
            }
            List<Config> list = query.getResultList();
            for (Config config : list) {
                sb.append(config.getName()).append("=").append(config.getValue()).append("<br>");
            }
            tx.commit();
            return sb.toString();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
            logout();
        }
    }

    public String getProperty(String name) {
        try {
            login();
            String value = getConfigWorker().getProperty(name);
            return value;
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    public String setProperty(String name, String value) {
        try {
            login();
            getConfigWorker().setProperty(name, value);
            return "Done";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    public String removeProperty(String name) {
        Transaction tx = Locator.createTransaction();
        try {
            login();
            EntityManager em = PersistenceProvider.getEntityManager();
            Query query = em.createQuery("delete from core$Config c where c.name = ?1");
            query.setParameter(1, name);
            query.executeUpdate();
            tx.commit();
            return "Done";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
            logout();
        }
    }

    private ConfigWorker getConfigWorker() {
        ConfigWorker configWorker = Locator.lookupLocal(ConfigWorker.JNDI_NAME);
        return configWorker;
    }

    public String loadSystemProperties() {
        String confUrl = System.getProperty("jboss.server.config.url");
        try {
            StringBuilder sb = new StringBuilder();

            String fileName = URI.create(confUrl).getPath() + "system.properties";
            File file = new File(fileName);
            if (file.exists()) {
                InputStream is = new FileInputStream(fileName);
                Properties props;
                try {
                    props = new Properties();
                    props.load(is);
                } finally {
                    is.close();
                }

                for (Map.Entry<Object, Object> entry : props.entrySet()) {
                    if ("".equals(entry.getValue())) {
                        System.getProperties().remove(entry.getKey());
                    }
                    else {
                        System.getProperties().put(entry.getKey(), entry.getValue());
                    }
                }
                sb.append("Properties from ").append(fileName).append(" loaded succesfully\n\n");
            }
            else {
                sb.append("File ").append(fileName).append(" not found\n\n");
            }

            List<String> strings = new ArrayList<String>(System.getProperties().size());
            for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
                strings.add(entry.getKey().toString() + "=" + entry.getValue().toString());
            }
            Collections.sort(strings);
            sb.append("Current system properties:\n\n");
            for (String s : strings) {
                sb.append(StringEscapeUtils.escapeHtml(s)).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String test() {
        try {
            createEntities();
            try {
                Permission p;
                Transaction tx = Locator.createTransaction();
                try {
                    EntityManager em = PersistenceProvider.getEntityManager();

                    em.setView(new View(Permission.class)
                            .addProperty("target")
                            .addProperty("role",
                                new View(Role.class)
                                    .addProperty("name")
                            )
                    );

                    p = em.find(Permission.class, permissionId);
                    tx.commitRetaining();

                    p.setTarget("newTarget");

                    em = PersistenceProvider.getEntityManager();
                    em.merge(p);

                    tx.commit();
                } finally {
                    tx.end();
                }
            } finally {
                removeEntities();
            }
            return "Done";
        } catch (Throwable t) {
            return ExceptionUtils.getStackTrace(t);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String testDataService() {
        try {
            login();
            createEntities();
            try {
                Permission p;
                DataService ds = Locator.lookupLocal(DataService.JNDI_NAME);

                DataService.LoadContext ctx = new DataServiceRemote.LoadContext(Permission.class);
                ctx.setId(permissionId);
                ctx.setView(new View(Permission.class)
                        .addProperty("target")
                        .addProperty("role",
                        new View(Role.class)
                                .addProperty("name")
                )
                );
                p = ds.load(ctx);

                p.setTarget("newTarget");

                DataServiceRemote.CommitContext commitCtx = new DataService.CommitContext(Collections.singleton(p));
                Map<Entity,Entity> map = ds.commit(commitCtx);
                return "Done";
            } finally {
                removeEntities();
                logout();
            }
        } catch (Throwable t) {
            return ExceptionUtils.getStackTrace(t);
        }
    }

    private void removeEntities() {
        Transaction tx;
        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q;
            q = em.createNativeQuery("delete from SEC_PERMISSION where ID = ?");
            q.setParameter(1, permissionId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_ROLE where ID = ?");
            q.setParameter(1, roleId.toString());
            q.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void createEntities() {
        Transaction tx;
        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Role role = new Role();
            roleId = role.getId();
            role.setName("testRole");
            em.persist(role);

            Permission permission = new Permission();
            permissionId = permission.getId();
            permission.setRole(role);
            permission.setType(0);
            permission.setTarget("testTarget");
            em.persist(permission);

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
