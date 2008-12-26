/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.12.2008 14:39:33
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity(name = "sec$Group")
@Table(name = "SEC_GROUP")
@Listeners({"com.haulmont.cuba.security.listener.GroupEntityListener"})
public class Group extends StandardEntity
{
    private static final long serialVersionUID = -4581386806900761785L;

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Group parent;

    @OneToMany(mappedBy = "group")
    @OrderBy("level")
    private List<GroupHierarchy> hierarchyList;

    @OneToMany(mappedBy = "group")
    private Set<Constraint> constraints;

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

    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                '}';
    }
}
