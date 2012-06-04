/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.SessionAttribute;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author artamonov
 * @version $Id$
 */
@Service(UserManagementService.NAME)
public class UserManagementServiceBean implements UserManagementService {

    private static final String GROUP_COPY_VIEW = "group.copy";

    private static final String MOVE_USER_TO_GROUP_VIEW = "user.moveToGroup";

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    @Override
    public Group copyAccessGroup(UUID accessGroupId) {
        checkNotNull(accessGroupId, "Null access group id");

        Group clone = null;

        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            em.setView(metadata.getViewRepository().getView(Group.class, GROUP_COPY_VIEW));

            Group accessGroup = em.find(Group.class, accessGroupId);
            if (accessGroup == null)
                throw new IllegalStateException("Unable to find specified access group with id: " + accessGroupId);

            clone = cloneGroup(accessGroup, accessGroup.getParent(), em);

            tx.commit();
        } finally {
            tx.end();
        }

        return clone;
    }

    @Override
    public Integer moveUsersToGroup(List<UUID> userIds, @Nullable UUID targetAccessGroupId) {
        checkNotNull(userIds, "Null users list");

        if (userIds.isEmpty())
            return 0;

        Transaction tx = persistence.getTransaction();

        int modifiedUsers = 0;
        try {
            EntityManager em = persistence.getEntityManager();

            Group targetAccessGroup = null;
            if (targetAccessGroupId != null) {
                targetAccessGroup = em.find(Group.class, targetAccessGroupId);
                if (targetAccessGroup == null)
                    throw new IllegalStateException("Could not found target access group with id: " + targetAccessGroupId);
            }

            em.setView(metadata.getViewRepository().getView(User.class, MOVE_USER_TO_GROUP_VIEW));

            Query query = em.createQuery("select u from sec$User u where u.id in (:userIds)");
            query.setParameter("userIds", userIds);

            List<User> users = query.getResultList();
            if (users == null || users.size() != userIds.size())
                throw new IllegalStateException("Not all users found in database");

            for (User user : users) {
                if (!ObjectUtils.equals(user.getGroup(), targetAccessGroup)) {
                    user.setGroup(targetAccessGroup);
                    em.merge(user);
                    modifiedUsers++;
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }
        return modifiedUsers;
    }

    private Group cloneGroup(Group group, Group parent, EntityManager em) {
        Group groupClone = new Group();

        groupClone.setName(group.getName());
        groupClone.setParent(parent);

        em.persist(groupClone);
        // fire hierarchy listeners
        em.flush();

        if (group.getConstraints() != null) {
            for (Constraint constraint : group.getConstraints()) {
                Constraint constraintClone = cloneConstraint(constraint, groupClone);
                em.persist(constraintClone);
            }
        }

        if (group.getSessionAttributes() != null) {
            for (SessionAttribute attribute : group.getSessionAttributes()) {
                SessionAttribute attributeClone = cloneSessionAttribute(attribute, groupClone);
                em.persist(attributeClone);
            }
        }

        Query query = em.createQuery("select g from sec$Group g where g.parent.id = :group");
        query.setParameter("group", group);

        List subGroups = query.getResultList();
        if (subGroups != null && subGroups.size() > 0) {
            for (Object subGroupObject : subGroups) {
                Group subGroup = (Group) subGroupObject;
                cloneGroup(subGroup, groupClone, em);
            }
        }

        return groupClone;
    }

    private SessionAttribute cloneSessionAttribute(SessionAttribute attribute, Group group) {
        SessionAttribute resultAttribute = new SessionAttribute();
        resultAttribute.setName(attribute.getName());
        resultAttribute.setDatatype(attribute.getDatatype());
        resultAttribute.setStringValue(attribute.getStringValue());
        resultAttribute.setGroup(group);
        return resultAttribute;
    }

    private Constraint cloneConstraint(Constraint constraint, Group group) {
        Constraint resultConstraint = new Constraint();
        resultConstraint.setEntityName(constraint.getEntityName());
        resultConstraint.setJoinClause(constraint.getJoinClause());
        resultConstraint.setWhereClause(constraint.getWhereClause());
        resultConstraint.setGroup(group);
        return resultConstraint;
    }
}