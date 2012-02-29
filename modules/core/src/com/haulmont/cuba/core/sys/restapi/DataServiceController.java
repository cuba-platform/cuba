/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.restapi;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.restapi.template.MetaClassRepresentation;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimeType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

/**
 * Author: Alexander Chevelev
 * Date: 19.04.2011
 * Time: 22:03:25
 *
 * @version $Id$
 */
@Controller
public class DataServiceController {
    private static Log log = LogFactory.getLog(DataServiceController.class);
    private DataService svc;
    //todo wire
    private ConversionFactory conversionFactory = new ConversionFactory();

    @Autowired
    public DataServiceController(DataService svc) {
        this.svc = svc;
    }

    @RequestMapping(value = "/find.{type}", method = RequestMethod.GET)
    public void find(@PathVariable String type,
                     @RequestParam(value = "e") String entityRef,
                     @RequestParam(value = "s") String sessionId,
                     HttpServletRequest request,
                     HttpServletResponse response) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Authentication authentication = Authentication.me(sessionId);
        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        EntityLoadInfo loadInfo = EntityLoadInfo.parse(entityRef);
        if (loadInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        MetaClass metaClass = loadInfo.getMetaClass();
        if (!readPermitted(metaClass)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UUID idObject = loadInfo.getId();

        try {
            LoadContext loadCtx = new LoadContext(metaClass);
            loadCtx.setId(idObject);
            loadCtx.setUseSecurityConstraints(true);
            if (loadInfo.getViewName() != null)
                loadCtx.setView(loadInfo.getViewName());

            Entity entity = svc.load(loadCtx);
            if (entity == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                Convertor convertor = conversionFactory.getConvertor(type);
                Object result = convertor.process(entity, metaClass, request.getRequestURI());
                convertor.write(response, result);
            }
        } finally {
            authentication.forget();
        }
    }

    @RequestMapping(value = "/query.{type}", method = RequestMethod.GET)
    public void query(@PathVariable String type,
                      @RequestParam(value = "e") String entityName,
                      @RequestParam(value = "q") String queryStr,
                      @RequestParam(value = "view", required = false) String view,
                      @RequestParam(value = "first", required = false) Integer firstResult,
                      @RequestParam(value = "max", required = false) Integer maxResults,
                      @RequestParam(value = "s") String sessionId,
                      HttpServletRequest request,
                      HttpServletResponse response) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Authentication authentication = Authentication.me(sessionId);
        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        MetaClass metaClass = getMetaClass(entityName);
        if (metaClass == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown entity name " + entityName);
        }

        EntityOp queryEntityOp = getQueryEntityOp(queryStr);
        if (!entityOpPermitted(metaClass, queryEntityOp)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Map<String, String[]> queryParams = new HashMap<String, String[]>(request.getParameterMap());
        queryParams.remove("e");
        queryParams.remove("q");
        queryParams.remove("view");
        queryParams.remove("first");
        queryParams.remove("s");
        queryParams.remove("max");
        try {
            LoadContext loadCtx = new LoadContext(metaClass);
            loadCtx.setUseSecurityConstraints(true);
            LoadContext.Query query = new LoadContext.Query(queryStr);
            loadCtx.setQuery(query);
            if (firstResult != null)
                query.setFirstResult(firstResult);
            if (maxResults != null)
                query.setMaxResults(maxResults);

            for (Map.Entry<String, String[]> entry : queryParams.entrySet()) {
                String paramKey = entry.getKey();
                if (paramKey.endsWith("_type"))
                    continue;

                if (entry.getValue().length != 1) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                String paramValue = entry.getValue()[0];
                Object parsedParam = parseQueryParameter(paramKey, paramValue, queryParams);
                query.addParameter(paramKey, parsedParam);
            }

            loadCtx.setView(view == null ? View.LOCAL : view);
            List<Entity> entities = svc.loadList(loadCtx);
            Convertor convertor = conversionFactory.getConvertor(type);
            Object result = convertor.process(entities, metaClass, request.getRequestURI());
            convertor.write(response, result);
        } finally {
            authentication.forget();
        }
    }

    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    public void commit(@RequestParam(value = "s") String sessionId,
                       @RequestHeader(value = "Content-Type") MimeType contentType,
                       @RequestBody String requestContent,
                       HttpServletRequest request,
                       HttpServletResponse response) throws
            IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Authentication authentication = Authentication.me(sessionId);
        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }


        Convertor convertor = conversionFactory.getConvertor(contentType);
        try {
            CommitRequest commitRequest = convertor.parseCommitRequest(requestContent);
            Collection commitInstances = commitRequest.getCommitInstances();
            Collection newInstanceIds = commitRequest.getNewInstanceIds();
            //send error if the user don't have permissions to commit at least one of the entities
            if (!commitPermitted(commitInstances, newInstanceIds)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            Collection removeInstances = commitRequest.getRemoveInstances();
            //send error if the user don't have permissions to remove at least one of the entities
            if (!removePermitted(removeInstances)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            NotDetachedCommitContext<Entity> commitContext = new NotDetachedCommitContext<Entity>();
            commitContext.setCommitInstances(commitInstances);
            commitContext.setRemoveInstances(removeInstances);
            commitContext.setSoftDeletion(commitRequest.isSoftDeletion());
            commitContext.setNewInstanceIds(newInstanceIds);
            Map<Entity, Entity> result = svc.commitNotDetached(commitContext);

            Object converted = convertor.process(result, request.getRequestURI());
            convertor.write(response, converted);
        } finally {
            authentication.forget();
        }
    }

    @RequestMapping(value = "/deployViews", method = RequestMethod.POST)
    public void deployViews(@RequestParam(value = "s") String sessionId,
                            @RequestBody String requestContent,
                            HttpServletRequest request,
                            HttpServletResponse response) throws
            IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Authentication authentication = Authentication.me(sessionId);
        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        ViewRepository viewRepository = MetadataProvider.getViewRepository();
        try {
            viewRepository.deployViews(new StringReader(requestContent));
        } finally {
            authentication.forget();
        }
    }

    @RequestMapping(value = "/printDomain", method = RequestMethod.GET)
    public void printDomain(@RequestParam(value = "s") String sessionId,
                            HttpServletResponse response) throws
            IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, TemplateException {

        Authentication authentication = Authentication.me(sessionId);
        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setLocale(UserSessionProvider.getLocale());
        PrintWriter writer = response.getWriter();

        try {
            ViewRepository viewRepository = MetadataProvider.getViewRepository();
            List<View> views = viewRepository.getAll();
            Map<MetaClass, List<View>> meta2views = new HashMap<MetaClass, List<View>>();
            for (View view : views) {
                MetaClass metaClass = MetadataProvider.getSession().getClass(view.getEntityClass());
                if (!readPermitted(metaClass))
                    continue;

                List<View> viewList = meta2views.get(metaClass);
                if (viewList == null) {
                    viewList = new ArrayList<View>();
                    meta2views.put(metaClass, viewList);
                }
                viewList.add(view);
            }

            List<MetaClassRepresentation> classes = new ArrayList<MetaClassRepresentation>();

            Set<MetaClass> metas = new HashSet<MetaClass>(MetadataHelper.getAllPersistentMetaClasses());
            metas.addAll(MetadataHelper.getAllEmbeddableMetaClasses());
            for (MetaClass meta : metas) {
                if (!readPermitted(meta))
                    continue;
                MetaClassRepresentation rep = new MetaClassRepresentation(meta, meta2views.get(meta));
                classes.add(rep);
            }
            Collections.sort(classes, new Comparator<MetaClassRepresentation>() {
                public int compare(MetaClassRepresentation o1, MetaClassRepresentation o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            Map<String, Object> values = new HashMap<String, Object>();
            values.put("knownEntities", classes);

            int typesNumber = Datatypes.getNames().size();
            String[] availableTypes = Datatypes.getNames().toArray(new String[typesNumber]);
            Arrays.sort(availableTypes);

            values.put("availableTypes", availableTypes);
            Configuration cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
            cfg.setOutputEncoding("UTF-8");
            cfg.setClassForTemplateLoading(DataServiceController.class, "template");
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template template = cfg.getTemplate("domain.ftl");
            template.process(values, writer);
        } finally {
            authentication.forget();
        }
    }

    private Object parseQueryParameter(String paramKey, String paramValue, Map<String, String[]> queryParams) {
        String[] typeName = queryParams.get(paramKey + "_type");
        //if the type is specified
        if (typeName != null && typeName.length == 1) {
            return parseByTypename(paramValue, typeName[0]);
        }
        //if the type is not specified
        else if (typeName == null) {
            return tryParse(paramValue);
        }
        //if several types have been declared
        else {
            throw new IllegalStateException("Too many parameters in request");
        }
    }

    /**
     * Tries to parse a string value into some of the available Datatypes
     * when no Datatype was specified.
     *
     * @param value value to parse
     * @return parsed value
     */
    private Object tryParse(String value) {
        try {
            return parseByDatatypeName(value, UUIDDatatype.NAME);
        } catch (Exception ignored) {
        }
        try {
            return parseByDatatypeName(value, DateTimeDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatypeName(value,  TimeDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatypeName(value, DateDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatypeName(value, BigDecimalDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatypeName(value, DoubleDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                return parseByDatatypeName(value, BooleanDatatype.NAME);
            }
        } catch (ParseException ignored) {
        }
        //return string value if couldn't parse into specific type
        return value;
    }

    private Object parseByDatatypeName(String value, String name) throws ParseException {
        Datatype datatype = Datatypes.get(name);
        return datatype.parse(value);
    }

    /**
     * Parses string value into specific type
     * @param value value to parse
     * @param typeName Datatype name
     * @return parsed object
     */
    private Object parseByTypename(String value, String typeName) {
        Datatype datatype = Datatypes.get(typeName);
        try {
            return datatype.parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse specified parameter");
        }
    }

    private MetaClass getMetaClass(String entityName) {
        Collection<MetaClass> classes = MetadataHelper.getAllPersistentMetaClasses();
        for (MetaClass metaClass : classes) {
            if (entityName.equals(metaClass.getName()))
                return metaClass;
        }
        return null;
    }

    /**
     * Checks if the user have permissions to commit (create or update)
     * all of the entities.
     *
     * @param commitInstances entities to commit
     * @param newInstanceIds  ids of the new entities
     * @return true - if the user can commit all of the requested entities, false -
     *         if he don't have permissions to commit at least one of the entities.
     */
    private boolean commitPermitted(Collection commitInstances, Collection newInstanceIds) {
        for (Object commitInstance : commitInstances) {
            Entity entity = (Entity) commitInstance;
            String fullId = entity.getMetaClass().getName() + "-" + entity.getId();
            if (newInstanceIds.contains(fullId)) {
                if (!createPermitted(entity.getMetaClass()))
                    return false;
            } else if (!updatePermitted(entity.getMetaClass())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the user have permissions to remove all of the requested entities.
     *
     * @param removeInstances entities to remove
     * @return true - if the user can remove all of the requested entities, false -
     *         if he don't have permissions to remove at least one of the entities.
     */
    private boolean removePermitted(Collection removeInstances) {
        for (Object removeInstance : removeInstances) {
            Entity next = (Entity) removeInstance;
            if (!removePermitted(next.getMetaClass()))
                return false;
        }
        return true;
    }

    private boolean readPermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.READ);
    }

    private boolean createPermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.CREATE);
    }

    private boolean updatePermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.UPDATE);
    }

    private boolean removePermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.DELETE);
    }

    private boolean entityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        UserSession session = UserSessionProvider.getUserSession();
        return session.isEntityOpPermitted(metaClass, entityOp);
    }

    /**
     * Returns EntityOp query requests to
     *
     * @param query JPQL query
     * @return EntityOp.READ or EntityOp.UPDATE or EntityOp.DELETE or null
     */
    private static EntityOp getQueryEntityOp(String query) {
        if (query == null)
            return null;

        query = query.trim().toLowerCase();
        if (query.isEmpty())
            return null;

        int firstSpaceIndex = query.indexOf(' ');
        int endIndex = firstSpaceIndex != -1 ? firstSpaceIndex : query.length();
        String op = query.substring(0, endIndex);
        if ("select".equals(op))
            return EntityOp.READ;
        else if ("update".equals(op))
            return EntityOp.UPDATE;
        else if ("delete".equals(op))
            return EntityOp.DELETE;
        return null;
    }
}
