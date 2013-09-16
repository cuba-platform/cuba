/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.presentations;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class PresentationsImpl implements Presentations {

    private String name;
    private Map<Object, Presentation> presentations;
    private Presentation current;
    private Presentation def;

    private Set<Presentation> needToUpdate = new HashSet<>();
    private Set<Presentation> needToRemove = new HashSet<>();

    private List<PresentationsChangeListener> listeners;

    public PresentationsImpl(Component c) {
        name = ComponentsHelper.getComponentPath(c);
    }

    @Override
    public void add(Presentation p) {
        checkLoad();
        presentations.put(p.getId(), p);
        if (PersistenceHelper.isNew(p)) {
            needToUpdate.add(p);

            if (BooleanUtils.isTrue(p.getDefault())) {
                def = p;
            }
        }
        firePresentationsSetChanged();
    }

    @Override
    public Presentation getCurrent() {
        checkLoad();
        return current;
    }

    @Override
    public void setCurrent(Presentation p) {
        checkLoad();
        if (presentations.containsKey(p.getId())) {
            Object old = current;
            current = p;
            fireCurrentPresentationChanged(old);
        } else {
            throw new IllegalStateException(String.format("Invalid presentation: %s", p.getId()));
        }
    }

    @Override
    public Element getSettings(Presentation p) {
        p = getPresentation(p.getId());
        if (p != null) {
            Document doc;
            if (!StringUtils.isEmpty(p.getXml())) {
                doc = Dom4j.readDocument(p.getXml());
            } else {
                doc = DocumentHelper.createDocument();
                doc.setRootElement(doc.addElement("presentation"));
            }
            return doc.getRootElement();
        } else {
            return null;
        }
    }

    @Override
    public void setSettings(Presentation p, Element e) {
        p = getPresentation(p.getId());
        if (p != null) {
            p.setXml(Dom4j.writeDocument(e.getDocument(), false));
            modify(p);
        }
    }

    @Override
    public Presentation getPresentation(Object id) {
        checkLoad();
        return presentations.get(id);
    }

    @Override
    public String getCaption(Object id) {
        Presentation p = getPresentation(id);
        if (p != null) {
            return p.getName();
        }
        return null;
    }

    @Override
    public Collection<Object> getPresentationIds() {
        checkLoad();
        return Collections.unmodifiableCollection(presentations.keySet());
    }

    @Override
    public void setDefault(Presentation p) {
        checkLoad();
        if (p == null) {
            def = null;
        } else if (presentations.containsKey(p.getId())) {
            if (def != null) {
                def.setDefault(false);
            }
            p.setDefault(true);
            def = p;
        } else {
            throw new IllegalStateException(String.format("Invalid presentation: %s", p.getId()));
        }
    }

    @Override
    public Presentation getDefault() {
        return def;
    }

    @Override
    public void remove(Presentation p) {
        checkLoad();
        if (presentations.remove(p.getId()) != null) {
            if (PersistenceHelper.isNew(p)) {
                needToUpdate.remove(p);
            } else {
                needToUpdate.remove(p);
                needToRemove.add(p);
            }

            if (p.equals(def)) {
                def = null;
            }

            if (p.equals(current)) {
                current = null;
            }

            firePresentationsSetChanged();
        }
    }

    @Override
    public void modify(Presentation p) {
        checkLoad();
        if (presentations.containsKey(p.getId())) {
            needToUpdate.add(p);
            if (BooleanUtils.isTrue(p.getDefault())) {
                setDefault(p);
            } else if (def != null && def.getId().equals(p.getId())) {
                setDefault(null);
            }
        } else {
            throw new IllegalStateException(String.format("Invalid presentation: %s", p.getId()));
        }
    }

    @Override
    public boolean isAutoSave(Presentation p) {
        p = getPresentation(p.getId());
        return p != null && BooleanUtils.isTrue(p.getAutoSave());
    }

    @Override
    public boolean isGlobal(Presentation p) {
        p = getPresentation(p.getId());
        return p != null && !PersistenceHelper.isNew(p) && p.getUser() == null;
    }

    @Override
    public void commit() {
        if (!needToUpdate.isEmpty() || !needToRemove.isEmpty()) {
            DataService ds = AppBeans.get(DataService.class);

            CommitContext ctx = new CommitContext(
                    Collections.unmodifiableSet(needToUpdate),
                    Collections.unmodifiableSet(needToRemove)
            );
            Set<Entity> commitResult = ds.commit(ctx);
            commited(commitResult);

            clearCommitList();

            firePresentationsSetChanged();
        }
    }

    public void commited(Set<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.equals(def))
                setDefault((Presentation) entity);
            else if (entity.equals(current))
                current = (Presentation) entity;

            if (presentations.containsKey(entity.getId())) {
                presentations.put(entity.getId(), (Presentation) entity);
            }
        }
    }

    @Override
    public void addListener(PresentationsChangeListener listener) {
        if (listeners == null) {
            listeners = new LinkedList<>();
        }
        listeners.add(listener);
    }

    @Override
    public void removeListener(PresentationsChangeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }
    }

    @Override
    public Presentation getPresentationByName(String name) {
        for (Presentation p : presentations.values()) {
            if (name.equalsIgnoreCase(p.getName())) {
                return p;
            }
        }
        return null;
    }

    protected void fireCurrentPresentationChanged(Object oldPresentationId) {
        if (listeners != null) {
            for (final PresentationsChangeListener listener : listeners) {
                listener.currentPresentationChanged(this, oldPresentationId);
            }
        }
    }

    protected void firePresentationsSetChanged() {
        if (listeners != null) {
            for (final PresentationsChangeListener listener : listeners) {
                listener.presentationsSetChanged(this);
            }
        }
    }

    private void checkLoad() {
        if (presentations == null) {
            DataService ds = AppBeans.get(DataService.class);
            LoadContext ctx = new LoadContext(Presentation.class);
            ctx.setView("app");

            UserSession session = AppBeans.get(UserSessionSource.class).getUserSession();
            User user = session.getCurrentOrSubstitutedUser();

            ctx.setQueryString("select p from sec$Presentation p " +
                    "where p.componentId = :component and (p.user is null or p.user.id = :userId)")
                    .setParameter("component", name)
                    .setParameter("userId", user.getId());

            final List<Presentation> list = ds.loadList(ctx);

            presentations = new LinkedHashMap<>(list.size());
            for (final Presentation p : list) {
                presentations.put(p.getId(), p);
            }
        }
    }

    private void clearCommitList() {
        needToUpdate.clear();
        needToRemove.clear();
    }
}