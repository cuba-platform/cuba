/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.DomainDescriptionService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.portal.config.RestConfig;
import com.haulmont.cuba.security.entity.EntityOp;
import freemarker.template.TemplateException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimeType;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

/**
 * @author chevelev
 * @version $Id$
 */
@Controller
public class DataServiceController {

    private Log log = LogFactory.getLog(DataServiceController.class);

    //todo wire
    protected ConversionFactory conversionFactory = new ConversionFactory();

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected DataService dataService;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Security security;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DomainDescriptionService domainDescriptionService;

    @Inject
    protected Authentication authentication;

    @Inject
    protected RestServicePermissions restServicePermissions;

    @RequestMapping(value = "/api/find.{type}", method = RequestMethod.GET)
    public void find(@PathVariable String type,
                     @RequestParam(value = "e") String entityRef,
                     @RequestParam(value = "s") String sessionId,
                     HttpServletRequest request,
                     HttpServletResponse response) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (!authentication.begin(sessionId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
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

            response.addHeader("Access-Control-Allow-Origin", "*");
            Object objectId = loadInfo.getId();

            LoadContext loadCtx = new LoadContext(metaClass);
            loadCtx.setId(objectId);
            loadCtx.setUseSecurityConstraints(true);
            if (loadInfo.getViewName() != null) {
                loadCtx.setView(loadInfo.getViewName());
            } else {
                View view = metadata.getViewRepository().getView(metaClass, View.LOCAL);
                loadCtx.setView(new View(view, "local-with-system-props", true));
            }

            Entity entity = dataService.load(loadCtx);
            if (entity == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                Convertor convertor = conversionFactory.getConvertor(type);
                String result = convertor.process(entity, metaClass, loadCtx.getView());
                writeResponse(response, result, convertor.getMimeType());
            }
        } catch (Throwable e) {
            sendError(request, response, e);
        } finally {
            authentication.end();
        }
    }

    @RequestMapping(value = "/api/query.{type}", method = RequestMethod.GET)
    public void query(@PathVariable String type,
                      @RequestParam(value = "e") String entityName,
                      @RequestParam(value = "q") String queryStr,
                      @RequestParam(value = "view", required = false) String viewName,
                      @RequestParam(value = "first", required = false) Integer firstResult,
                      @RequestParam(value = "max", required = false) Integer maxResults,
                      @RequestParam(value = "s") String sessionId,
                      HttpServletRequest request,
                      HttpServletResponse response) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (!authentication.begin(sessionId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            response.addHeader("Access-Control-Allow-Origin", "*");
            MetaClass metaClass = getMetaClass(entityName);
            if (metaClass == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Persistent entity " + entityName + " does not exist");
                return;
            }

            if (!entityOpPermitted(metaClass, EntityOp.READ)) {
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
                query.setParameter(paramKey, parsedParam);
            }

            if (viewName == null) {
                View view = metadata.getViewRepository().getView(metaClass, View.LOCAL);
                loadCtx.setView(new View(view, "local-with-system-props", true));
            } else {
                loadCtx.setView(viewName);
            }
            List<Entity> entities = dataService.loadList(loadCtx);
            Convertor convertor = conversionFactory.getConvertor(type);
            String result = convertor.process(entities, metaClass, loadCtx.getView());
            writeResponse(response, result, convertor.getMimeType());
        } catch (Throwable e) {
            sendError(request, response, e);
        } finally {
            authentication.end();
        }
    }

    @RequestMapping(value = "/api/commit", method = RequestMethod.POST)
    public void commit(@RequestParam(value = "s") String sessionId,
                       @RequestHeader(value = "Content-Type") MimeType contentType,
                       @RequestBody String requestContent,
                       HttpServletRequest request,
                       HttpServletResponse response) throws
            IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (!authentication.begin(sessionId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            response.addHeader("Access-Control-Allow-Origin", "*");

            Convertor convertor = conversionFactory.getConvertor(contentType);

            CommitRequest commitRequest = convertor.parseCommitRequest(requestContent);
            Collection commitInstances = commitRequest.getCommitInstances();
            Set<String> newInstanceIds = commitRequest.getNewInstanceIds();

            assignUuidToNewInstances(commitInstances, newInstanceIds);

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

            NotDetachedCommitContext commitContext = new NotDetachedCommitContext();
            commitContext.setCommitInstances(commitInstances);
            commitContext.setRemoveInstances(removeInstances);
            commitContext.setSoftDeletion(commitRequest.isSoftDeletion());
            commitContext.setNewInstanceIds(newInstanceIds);
            Set<Entity> result = dataService.commit(commitContext);

            String converted = convertor.process(result);
            writeResponse(response, converted, convertor.getMimeType());
        } catch (Throwable e) {
            sendError(request, response, e);
        } finally {
            authentication.end();
        }
    }

    private void assignUuidToNewInstances(Collection commitInstances, Collection newInstanceIds) {
        for (Object id : newInstanceIds) {
            for (Object instance : commitInstances) {
                Entity entity = (Entity) instance;
                String entityFullId = EntityLoadInfo.create(entity).toString();
                if (entityFullId.equals(id) && entity.getUuid() == null) {
                    entity.setValue("uuid", UuidProvider.createUuid());
                }
            }
        }
    }

    @RequestMapping(value = "/api/deployViews", method = RequestMethod.POST)
    public void deployViews(@RequestParam(value = "s") String sessionId,
                            @RequestBody String requestContent,
                            HttpServletRequest request,
                            HttpServletResponse response) throws
            IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (!authentication.begin(sessionId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            response.addHeader("Access-Control-Allow-Origin", "*");
            ViewRepository viewRepository = metadata.getViewRepository();
            ((AbstractViewRepository) viewRepository).deployViews(new StringReader(requestContent));
        } catch (Throwable e) {
            sendError(request, response, e);
        } finally {
            authentication.end();
        }
    }

    @RequestMapping(value = "/api/printDomain", method = RequestMethod.GET)
    public void printDomain(@RequestParam(value = "s") String sessionId,
                            HttpServletRequest request,
                            HttpServletResponse response) throws
            IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, TemplateException {

        if (!authentication.begin(sessionId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setLocale(userSessionSource.getLocale());

            String domainDescription = domainDescriptionService.getDomainDescription();
            response.getWriter().write(domainDescription);

        } catch (Throwable e) {
            sendError(request, response, e);
        } finally {
            authentication.end();
        }
    }

    @RequestMapping(value = "/api/service.{type}", method = RequestMethod.GET)
    public void serviceByGet(@PathVariable(value = "type") String type,
                             @RequestParam(value = "s") String sessionId,
                             @RequestParam(value = "service") String serviceName,
                             @RequestParam(value = "method") String methodName,
                             @RequestParam(value = "view", required = false) String view,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        if (!authentication.begin(sessionId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (!restServicePermissions.isPermitted(serviceName, methodName)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            response.addHeader("Access-Control-Allow-Origin", "*");

            Map<String, String[]> parameterMap = request.getParameterMap();
            List<String> paramValuesString = new ArrayList<>();
            List<Class> paramTypes = new ArrayList<>();

            int idx = 0;
            while (true) {
                String[] _values = parameterMap.get("param" + idx);
                if (_values == null) break;
                if (_values.length > 1) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Multiple values for param" + idx);
                    return;
                }
                paramValuesString.add(_values[0]);

                String[] _types = parameterMap.get("param" + idx + "_type");
                if (_types != null) {
                    if (_types.length > 1) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Multiple values for param" + idx + "_type");
                        return;
                    }
                    paramTypes.add(idx, ClassUtils.forName(_types[0], null));
                } else if (!paramTypes.isEmpty()) {
                    //types should be defined for all parameters or for none of them
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter type for param" + idx +" is not defined");
                    return;
                }
                idx++;
            }

            Convertor convertor = conversionFactory.getConvertor(type);
            ServiceRequest serviceRequest = new ServiceRequest(serviceName, methodName, convertor);
            serviceRequest.setParamTypes(paramTypes);
            serviceRequest.setParamValuesString(paramValuesString);

            Object result = serviceRequest.invokeMethod();

            String converted = convertor.processServiceMethodResult(result, view);
            writeResponse(response, converted, convertor.getMimeType());
        } catch (Throwable e) {
            sendError(request, response, e);
        } finally {
            authentication.end();
        }
    }

    @RequestMapping(value = "/api/service", method = RequestMethod.POST)
    public void serviceByPost(@RequestParam(value = "s") String sessionId,
                             @RequestHeader(value = "Content-Type") MimeType contentType,
                             @RequestBody String requestContent,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException, JSONException {

        if (!authentication.begin(sessionId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            response.addHeader("Access-Control-Allow-Origin", "*");

            Convertor convertor = conversionFactory.getConvertor(contentType);
            ServiceRequest serviceRequest = convertor.parseServiceRequest(requestContent);

            if (!restServicePermissions.isPermitted(serviceRequest.getServiceName(), serviceRequest.getMethodName())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            Object result = serviceRequest.invokeMethod();
            String converted = convertor.processServiceMethodResult(result, serviceRequest.getViewName());
            writeResponse(response, converted, convertor.getMimeType());
        } catch (RestServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            log.error("Error processing request: " + request.getRequestURI() + "?" + request.getQueryString(), e);
        } catch (Throwable e) {
            sendError(request, response, e);
        } finally {
            authentication.end();
        }
    }

    private void  writeResponse(HttpServletResponse response, String result, MimeType mimeType) throws IOException {
        response.setContentType(mimeType.toString());
        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, Throwable e) throws IOException {
        log.error("Error processing request: " + request.getRequestURI() + "?" + request.getQueryString(), e);

        Configuration configuration = AppBeans.get(Configuration.class);
        boolean isProductionMode = configuration.getConfig(RestConfig.class).getProductionMode();

        String msg;
        if (isProductionMode) {
            msg = "Internal server error";
        } else {
            Throwable t = ExceptionUtils.getRootCause(e);
            msg = t != null ? ExceptionUtils.getStackTrace(t) : ExceptionUtils.getStackTrace(e);
        }
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
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
            throw new IllegalArgumentException(String.format("Cannot parse specified parameter of type '%s'", typeName), e);
        }
    }

    private MetaClass getMetaClass(String entityName) {
        Collection<MetaClass> classes = metadataTools.getAllPersistentMetaClasses();
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
        return security.isEntityOpPermitted(metaClass, entityOp);
    }
}