/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.restapi.template.MetaClassRepresentation;
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
import java.util.*;

/**
 * Author: Alexander Chevelev
 * Date: 19.04.2011
 * Time: 22:03:25
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

        Map<String, String[]> queryParams = new HashMap<String, String[]>(request.getParameterMap());
        queryParams.remove("query");
        queryParams.remove("view");
        queryParams.remove("s");
        queryParams.remove("first");
        queryParams.remove("max");

        try {
            LoadContext loadCtx = new LoadContext(metaClass);
            loadCtx.setUseSecurityConstraints(true);
            LoadContext.Query query = loadCtx.setQueryString(queryStr);
            if (firstResult != null)
                query.setFirstResult(firstResult);
            if (maxResults != null)
                query.setMaxResults(maxResults);

            for (Map.Entry<String, String[]> entry : queryParams.entrySet()) {
                query.addParameter(entry.getKey(), entry.getValue()[0]);
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

            NotDetachedCommitContext<Entity> commitContext = new NotDetachedCommitContext<Entity>();
            commitContext.setCommitInstances(commitRequest.getCommitInstances());
            commitContext.setRemoveInstances(commitRequest.getRemoveInstances());
            commitContext.setSoftDeletion(commitRequest.isSoftDeletion());
            commitContext.setNewInstanceIds(commitRequest.getNewInstanceIds());
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
                List<View> viewList = meta2views.get(metaClass);
                if (viewList == null) {
                    viewList = new ArrayList<View>();
                    meta2views.put(metaClass, viewList);
                }
                viewList.add(view);
            }

            List<MetaClassRepresentation> classes = new ArrayList<MetaClassRepresentation>();

            Collection<MetaClass> metas = MetadataHelper.getAllPersistentMetaClasses();
            for (MetaClass meta : metas) {
                MetaClassRepresentation rep = new MetaClassRepresentation(meta, meta2views.get(meta));
                classes.add(rep);
            }
            Collections.sort(classes, new Comparator<MetaClassRepresentation>() {
                public int compare(MetaClassRepresentation o1, MetaClassRepresentation o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            Map values = new HashMap();
            values.put("knownEntities", classes);

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

    private MetaClass getMetaClass(String entityName) {
        Collection<MetaClass> classes = MetadataHelper.getAllPersistentMetaClasses();
        for (MetaClass metaClass : classes) {
            if (entityName.equals(metaClass.getName()))
                return metaClass;
        }
        return null;
    }

}
