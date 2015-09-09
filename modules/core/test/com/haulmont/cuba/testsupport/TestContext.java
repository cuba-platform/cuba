/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.testsupport;

import javax.naming.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestContext implements Context {

    private Map<String, Object> store = new ConcurrentHashMap<>();

    private static Context instance = new TestContext();

    public static Context getInstance() {
        return instance;
    }

    private TestContext() {
    }

    public Object lookup(Name name) throws NamingException {
        return lookup(name.toString());
    }

    public Object lookup(String name) throws NamingException {
        Object obj = store.get(name);
        if (obj != null)
            return obj;
        else
            throw new NamingException("Not bound: " + name);
    }

    public void bind(Name name, Object obj) throws NamingException {
        bind(name.toString(), obj);
    }

    public void bind(String name, Object obj) throws NamingException {
        store.put(name, obj);
    }

    public void rebind(Name name, Object obj) throws NamingException {
        rebind(name.toString(), obj);
    }

    public void rebind(String name, Object obj) throws NamingException {
        store.put(name, obj);
    }

    public void unbind(Name name) throws NamingException {
        unbind(name.toString());
    }

    public void unbind(String name) throws NamingException {
        store.remove(name);
    }

    public void rename(Name oldName, Name newName) throws NamingException {
    }

    public void rename(String oldName, String newName) throws NamingException {
    }

    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return null;
    }

    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        return null;
    }

    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return null;
    }

    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        return null;
    }

    public void destroySubcontext(Name name) throws NamingException {
    }

    public void destroySubcontext(String name) throws NamingException {
    }

    public Context createSubcontext(Name name) throws NamingException {
        return null;
    }

    public Context createSubcontext(String name) throws NamingException {
        return null;
    }

    public Object lookupLink(Name name) throws NamingException {
        return null;
    }

    public Object lookupLink(String name) throws NamingException {
        return null;
    }

    public NameParser getNameParser(Name name) throws NamingException {
        return null;
    }

    public NameParser getNameParser(String name) throws NamingException {
        return null;
    }

    public Name composeName(Name name, Name prefix) throws NamingException {
        return null;
    }

    public String composeName(String name, String prefix) throws NamingException {
        return null;
    }

    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        return null;
    }

    public Object removeFromEnvironment(String propName) throws NamingException {
        return null;
    }

    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return null;
    }

    public void close() throws NamingException {
    }

    public String getNameInNamespace() throws NamingException {
        return null;
    }
}
