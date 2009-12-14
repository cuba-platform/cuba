/*
* Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
* Haulmont Technology proprietary and confidential.
* Use is subject to license terms.

* Author: Konstantin Krivopustov
* Created: 14.12.2009 15:37:05
*
* $Id$
*/
import com.haulmont.cuba.core.PersistenceProvider
import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.Query

EntityManager em = PersistenceProvider.getEntityManager()
Query q = em.createQuery("select count(u.id) from sec\$User u where u.email like ?1 and u.group.id = ?2")
q.setParameter(1, "%@haulmont.com%")
q.setParameter(2, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"))

return q.getSingleResult()