/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 17.09.2010 13:10:10
 *
 * $Id$
 */
package com.haulmont.cuba.gui.presentations;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.Serializable;
import java.util.*;

public class PresentationsImpl implements Presentations, Serializable {
    private static final long serialVersionUID = -4430808716144215714L;

    private String name;
    private Map<Object, Presentation> presentations;
    private Presentation current;
    private Presentation def;

    private Set<Presentation> needToUpdate = new HashSet<Presentation>();
    private Set<Presentation> needToRemove = new HashSet<Presentation>();

    private List<PresentationsChangeListener> listeners;

    public PresentationsImpl(Component c) {
        name = ComponentsHelper.getComponentPath(c);
    }

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

    public Presentation getCurrent() {
        checkLoad();
        return current;
    }

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

    public void setSettings(Presentation p, Element e) {
        p = getPresentation(p.getId());
        if (p != null) {
            p.setXml(Dom4j.writeDocument(e.getDocument(), false));
            modify(p);
        }
    }

    public Presentation getPresentation(Object id) {
        checkLoad();
        return presentations.get(id);
    }

    public String getCaption(Object id) {
        Presentation p = getPresentation(id);
        if (p != null) {
            return p.getName();
        }
        return null;
    }

    public Collection<Object> getPresentationIds() {
        checkLoad();
        return Collections.unmodifiableCollection(presentations.keySet());
    }

    public void setDefault(Presentation p) {
        checkLoad();
        if (presentations.containsKey(p.getId())) {
            if (def != null) {
                def.setDefault(false);
            }
            p.setDefault(true);
            def = p;
        } else {
            throw new IllegalStateException(String.format("Invalid presentation: %s", p.getId()));
        }
    }

    public Presentation getDefault() {
        return def;
    }

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

    public void modify(Presentation p) {
        checkLoad();
        if (presentations.containsKey(p.getId())) {
            needToUpdate.add(p);
            if (BooleanUtils.isTrue(p.getDefault())) {
                setDefault(p);
            }
            firePresentationsSetChanged();
        } else {
            throw new IllegalStateException(String.format("Invalid presentation: %s", p.getId()));
        }
    }

    public boolean isAutoSave(Presentation p) {
        p = getPresentation(p.getId());
        return p != null && BooleanUtils.isTrue(p.getAutoSave());
    }

    public boolean isGlobal(Presentation p) {
        p = getPresentation(p.getId());
        return p != null && !PersistenceHelper.isNew(p) && p.getUser() == null;
    }

    public void commit() {
        if (!needToUpdate.isEmpty() || !needToRemove.isEmpty()) {
            DataService ds = ServiceLocator.getDataService();

            CommitContext ctx = new CommitContext(
                    Collections.unmodifiableSet(needToUpdate),
                    Collections.unmodifiableSet(needToRemove)
            );
            Map<Entity, Entity> commitResult = ds.commit(ctx);
            commited(commitResult);

            clearCommitList();
        }
    }

    public void commited(Map<Entity, Entity> map) {
        if (map.containsKey(def)) {
            setDefault((Presentation) map.get(def));
        }
        if (map.containsKey(current)) {
            current = (Presentation) map.get(current);
        }
        for (final Entity e : map.values()) {
            if (presentations.containsKey(e.getId())) {
                presentations.put(e.getId(), (Presentation) e);
            }
        }
    }

    public void addListener(PresentationsChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<PresentationsChangeListener>();
        }
        listeners.add(listener);
    }

    public void removeListener(PresentationsChangeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }
    }

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
            DataService ds = ServiceLocator.getDataService();
            LoadContext ctx = new LoadContext(Presentation.class);
            ctx.setView("app");

            User user = UserSessionClient.getUserSession().getSubstitutedUser();
            if (user == null) {
                user = UserSessionClient.getUserSession().getUser();
            }

            ctx.setQueryString("select p from sec$Presentation p " +
                    "where p.componentId = :component and (p.user is null or p.user.id = :userId)")
                    .addParameter("component", name)
                    .addParameter("userId", user.getId());

            final List<Presentation> list = ds.loadList(ctx);

            presentations = new LinkedHashMap<Object, Presentation>(list.size());
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
