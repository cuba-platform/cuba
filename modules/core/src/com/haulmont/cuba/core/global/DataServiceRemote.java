/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 11:13:20
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;

import javax.ejb.Remote;
import java.io.Serializable;
import java.util.*;

@Remote
public interface DataServiceRemote
{
    String JNDI_NAME = "cuba/core/DataService";

    DbDialect getDbDialect();

    Map<Entity, Entity> commit(CommitContext<Entity> context);

    <A extends Entity> A load(LoadContext context);
    <A extends Entity> List<A> loadList(LoadContext context);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public class CommitContext<Entity> implements Serializable {
        private static final long serialVersionUID = 2510011302544968537L;
        
        protected Collection<Entity> commitInstances = new HashSet<Entity>();
        protected Collection<Entity> removeInstances = new HashSet<Entity>();

        protected Map<Entity, View> views = new HashMap<Entity, View>();

        public CommitContext() {
        }

        public CommitContext(Collection<Entity> commitInstances) {
            this.commitInstances.addAll(commitInstances);
        }

        public CommitContext(Collection<Entity> commitInstances, Collection<Entity> removeInstances) {
            this.commitInstances.addAll(commitInstances);
            this.removeInstances.addAll(removeInstances);
        }

        public Collection<Entity> getCommitInstances() {
            return commitInstances;
        }

        public void setCommitInstances(Collection<Entity> commitInstances) {
            this.commitInstances = commitInstances;
        }

        public Collection<Entity> getRemoveInstances() {
            return removeInstances;
        }

        public void setRemoveInstances(Collection<Entity> removeInstances) {
            this.removeInstances = removeInstances;
        }

        public Map<Entity, View> getViews() {
            return views;
        }
    }

    public class LoadContext implements Serializable {

        private static final long serialVersionUID = -8808320502197308698L;

        protected String metaClass;
        protected Query query;
        protected View view;
        protected Object id;
        protected Collection<Object> ids;

        public LoadContext(MetaClass metaClass) {
            this.metaClass = metaClass.getName();
        }

        public LoadContext(Class metaClass) {
            this.metaClass = MetadataProvider.getSession().getClass(metaClass).getName();
        }

        public String getMetaClass() {
            return metaClass;
        }

        public DataServiceRemote.Query getQuery() {
            return query;
        }

        public void setQuery(Query query) {
            this.query = query;
        }

        public View getView() {
            return view;
        }

        public LoadContext setView(View view) {
            this.view = view;
            return this;
        }

        public Object getId() {
            return id;
        }

        public LoadContext setId(Object id) {
            this.id = id;
            return this;
        }

        public Collection<Object> getIds() {
            return ids;
        }

        public void setIds(Collection<Object> ids) {
            this.ids = ids;
        }

        public Query setQueryString(String queryString) {
            final Query query = new Query(queryString);
            setQuery(query);
            return query;
        }
    }

    public class Query implements Serializable {

        private static final long serialVersionUID = 3819951144050635838L;

        private Map<String, Object> parameters = new HashMap<String, Object>();
        private String queryString;
        private int firstResult;
        private int maxResults;

        public Query(String queryString) {
            this.queryString = queryString;
        }

        public Query addParameter(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        public String getQueryString() {
            return queryString;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
        }

        public Query setFirstResult(int firstResult) {
            this.firstResult = firstResult;
            return this;
        }

        public Query setMaxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public int getFirstResult() {
            return firstResult;
        }

        public int getMaxResults() {
            return maxResults;
        }
    }
}
