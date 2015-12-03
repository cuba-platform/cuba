/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.TrackEditScreenHistory;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * User access group.
 *
 * @author krivopustov
 * @version $Id$
 */
@Entity(name = "sec$Group")
@Table(name = "SEC_GROUP")
@Listeners("cuba_GroupEntityListener")
@NamePattern("%s|name")
@TrackEditScreenHistory
public class Group extends StandardEntity {

    private static final long serialVersionUID = -4581386806900761785L;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Group parent;

    @OneToMany(mappedBy = "group")
    @OrderBy("level")
    private List<GroupHierarchy> hierarchyList;

    @OneToMany(mappedBy = "group")
    @Composition()
    @OnDelete(DeletePolicy.CASCADE)
    private Set<Constraint> constraints;

    @OneToMany(mappedBy = "group")
    @Composition()
    @OnDelete(DeletePolicy.CASCADE)
    private Set<SessionAttribute> sessionAttributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    public List<GroupHierarchy> getHierarchyList() {
        return hierarchyList;
    }

    public void setHierarchyList(List<GroupHierarchy> hierarchyList) {
        this.hierarchyList = hierarchyList;
    }

    public Set<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(Set<Constraint> constraints) {
        this.constraints = constraints;
    }

    public Set<SessionAttribute> getSessionAttributes() {
        return sessionAttributes;
    }

    public void setSessionAttributes(Set<SessionAttribute> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                '}';
    }
}
