/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.restapi.swagger;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetadataObject;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.restapi.config.RestQueriesConfiguration;
import com.haulmont.restapi.config.RestServicesConfiguration;
import com.haulmont.restapi.config.RestServicesConfiguration.RestMethodInfo;
import com.haulmont.restapi.config.RestServicesConfiguration.RestMethodParamInfo;
import io.swagger.models.*;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.haulmont.cuba.core.app.serialization.EntitySerialization.ENTITY_NAME_PROP;
import static com.haulmont.cuba.core.app.serialization.EntitySerialization.INSTANCE_NAME_PROP;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component(SwaggerGenerator.NAME)
public class SwaggerGeneratorBean implements SwaggerGenerator {

    private static final Logger log = LoggerFactory.getLogger(SwaggerGeneratorBean.class);

    protected static final String ENTITY_PATH = "/entities/%s";
    protected static final String ENTITY_RUD_OPS = "/entities/%s/{entityId}";
    protected static final String ENTITY_SEARCH = "/entities/%s/search";

    protected static final String QUERY_PATH = "/queries/%s/%s";
    protected static final String QUERY_COUNT_PATH = "/queries/%s/%s/count";

    protected static final String SERVICE_PATH = "/services/%s/%s";

    protected static final String DEFINITIONS_PREFIX = "#/definitions/";
    protected static final String ENTITY_DEFINITION_PREFIX = DEFINITIONS_PREFIX + "entities_";
    protected static final String PARAMETERS_PREFIX = "#/parameters/";

    protected static final String GET_PARAM_NAME = "%s_%s_%s_%s";
    protected static final String POST_PARAM_NAME = "%s_%s_paramsObject_%s";

    protected static final String ARRAY_SIGNATURE = "[]";

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected Resources resources;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected RestQueriesConfiguration queriesConfiguration;

    @Inject
    protected RestServicesConfiguration servicesConfiguration;

    protected Swagger swagger = null;

    private volatile boolean initialized = false;

    protected Map<String, Parameter> parameters = new HashMap<>();
    protected Map<String, Model> definitions = new HashMap<>();

    @Override
    public Swagger generateSwagger() {
        checkInitialized();

        return swagger;
    }

    protected void checkInitialized() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    log.info("Generating Swagger documentation");
                    init();
                    initialized = true;
                }
            }
        }
    }

    protected void init() {
        Pair<List<Tag>, Map<String, Path>> tagsAndPaths = generatePaths();

        swagger = new Swagger()
                .host(getHost())
                .basePath(getBasePath())
                .consumes(APPLICATION_JSON_VALUE)
                .produces(APPLICATION_JSON_VALUE)
                .info(generateInfo())
                .tags(tagsAndPaths.getFirst())
                .paths(tagsAndPaths.getSecond());

        swagger.setParameters(parameters);
        swagger.setDefinitions(definitions);
    }

    protected String getHost() {
        return globalConfig.getWebHostName() + ":" + globalConfig.getWebPort();
    }

    protected String getBasePath() {
        return "/" + globalConfig.getWebContextName() + "/rest/v2";
    }

    protected Info generateInfo() {
        return new Info()
                .version("0.1")
                .title("Project REST API")
                .description("Generated Swagger documentation");
    }

    protected Pair<List<Tag>, Map<String, Path>> generatePaths() {
        Map<String, Path> paths = new LinkedHashMap<>();

        paths.putAll(generateEntitiesPaths());
        paths.putAll(generateQueriesPaths());
        paths.putAll(generateServicesPaths());

        return new Pair<>(generateTags(), paths);
    }

    protected List<Tag> generateTags() {
        List<Tag> tags = new ArrayList<>();

        List<Tag> entityTags = metadataTools.getAllPersistentMetaClasses()
                .stream()
                .filter(mc -> !metadataTools.isSystemLevel(mc))
                .sorted(Comparator.comparing(MetadataObject::getName))
                .map(mc -> new Tag()
                        .name(mc.getName())
                        .description("Entity CRUD operations"))
                .collect(Collectors.toList());
        tags.addAll(entityTags);

        List<Tag> queryTags = queriesConfiguration.getQueries()
                .stream()
                .map(RestQueriesConfiguration.QueryInfo::getEntityName)
                .distinct()
                .sorted(String::compareTo)
                .map(queryEntity -> new Tag()
                        .name(queryEntity + " Queries")
                        .description("Predefined queries execution"))
                .collect(Collectors.toList());
        tags.addAll(queryTags);

        List<Tag> serviceTags = servicesConfiguration.getServiceInfos()
                .stream()
                .sorted(Comparator.comparing(RestServicesConfiguration.RestServiceInfo::getName))
                .map(serviceInfo -> new Tag()
                        .name(serviceInfo.getName())
                        .description("Middleware services execution"))
                .collect(Collectors.toList());
        tags.addAll(serviceTags);

        return tags;
    }

    /*
     * Entities
     */

    protected Map<String, Path> generateEntitiesPaths() {
        Map<String, Path> paths = new LinkedHashMap<>();

        for (MetaClass metaClass : metadataTools.getAllPersistentMetaClasses()) {
            if (metadataTools.isSystemLevel(metaClass)) {
                continue;
            }
            paths.putAll(generateEntityPaths(metaClass));
        }

        return paths;
    }

    protected Map<String, Path> generateEntityPaths(MetaClass entityClass) {
        ModelImpl entityModel = generateEntityModel(entityClass);

        Map<String, Path> entityPaths = new LinkedHashMap<>();

        entityPaths.putAll(generateEntityCRUDPaths(entityModel));
        entityPaths.putAll(generateEntityFilterPaths(entityModel));

        return entityPaths;
    }

    protected Map<String, Path> generateEntityCRUDPaths(ModelImpl entityModel) {
        Map<String, Path> crudPaths = new LinkedHashMap<>();

        crudPaths.putAll(generateEntityPath(entityModel));
        crudPaths.putAll(generateEntityRUDPaths(entityModel));

        return crudPaths;
    }

    protected Map<String, Path> generateEntityPath(ModelImpl entityModel) {
        return Collections.singletonMap(
                String.format(ENTITY_PATH, entityModel.getName()),
                new Path()
                        .get(generateEntityBrowseOperation(entityModel))
                        .post(generateEntityCreateOperation(entityModel)));
    }

    protected Map<String, Path> generateEntityRUDPaths(ModelImpl entityModel) {
        return Collections.singletonMap(
                String.format(ENTITY_RUD_OPS, entityModel.getName()),
                new Path()
                        .get(generateEntityReadOperation(entityModel))
                        .put(generateEntityUpdateOperation(entityModel))
                        .delete(generateEntityDeleteOperation(entityModel)));
    }

    protected Map<String, Path> generateEntityFilterPaths(ModelImpl entityModel) {
        return Collections.singletonMap(
                String.format(ENTITY_SEARCH, entityModel.getName()),
                new Path()
                        .get(generateEntitySearchOperation(entityModel, RequestMethod.GET))
                        .post(generateEntitySearchOperation(entityModel, RequestMethod.POST)));
    }

    protected Operation generateEntityCreateOperation(ModelImpl entityModel) {
        Operation operation = new Operation()
                .tag(entityModel.getName())
                .produces(APPLICATION_JSON_VALUE)
                .summary("Creates new entity: " + entityModel.getName())
                .description("The method expects a JSON with entity object in the request body. " +
                        "The entity object may contain references to other entities.")
                .response(201, new Response()
                        .description("Entity created. The created entity is returned in the response body.")
                        .schema(new RefProperty(ENTITY_DEFINITION_PREFIX + entityModel.getName())))
                .response(400, getErrorResponse("Bad request. For example, the entity may have a reference to the non-existing entity."))
                .response(403, getErrorResponse("Forbidden. The user doesn't have permissions to create the entity."))
                .response(404, getErrorResponse("Not found. MetaClass for the entity with the given name not found."));

        BodyParameter entityParam = new BodyParameter()
                .name("entityJson")
                .description("JSON object with the entity")
                .schema(new RefModel(ENTITY_DEFINITION_PREFIX + entityModel.getName()));
        entityParam.setRequired(true);

        operation.parameter(entityParam);

        return operation;
    }

    protected Operation generateEntityBrowseOperation(ModelImpl entityModel) {
        Operation operation = new Operation()
                .tag(entityModel.getName())
                .produces(APPLICATION_JSON_VALUE)
                .summary("Gets a list of entities: " + entityModel.getName())
                .description("Gets a list of entities")
                .response(200, new Response()
                        .description("Success. The list of entities is returned in the response body.")
                        .schema(new ArrayProperty(new RefProperty(ENTITY_DEFINITION_PREFIX + entityModel.getName()))))
                .response(403, getErrorResponse("Forbidden. The user doesn't have permissions to read the entity."))
                .response(404, getErrorResponse("Not found. MetaClass for the entity with the given name not found."));

        operation.setParameters(generateEntityOptionalParams(false));

        return operation;
    }

    protected Operation generateEntityReadOperation(ModelImpl entityModel) {
        Operation operation = new Operation()
                .tag(entityModel.getName())
                .produces(APPLICATION_JSON_VALUE)
                .summary("Gets a single entity by identifier: " + entityModel.getName())
                .description("Gets a single entity by identifier")
                .parameter(new PathParameter()
                        .name("entityId")
                        .description("Entity identifier")
                        .required(true)
                        .property(new StringProperty()))
                .response(200, new Response()
                        .description("Success. The entity is returned in the response body.")
                        .schema(new RefProperty(ENTITY_DEFINITION_PREFIX + entityModel.getName())))
                .response(403, getErrorResponse("Forbidden. The user doesn't have permissions to read the entity."))
                .response(404, getErrorResponse("Not found. MetaClass for the entity with the given name not found."));

        operation.getParameters().addAll(generateEntityOptionalParams(true));

        return operation;
    }

    protected Operation generateEntityUpdateOperation(ModelImpl entityModel) {
        BodyParameter entityParam = new BodyParameter()
                .name("entityJson")
                .description("JSON object with the entity")
                .schema(new RefModel(ENTITY_DEFINITION_PREFIX + entityModel.getName()));
        entityParam.setRequired(true);

        PathParameter entityIdParam = new PathParameter()
                .name("entityId")
                .description("Entity identifier")
                .required(true)
                .property(new StringProperty().description("Entity identifier"));

        return new Operation()
                .tag(entityModel.getName())
                .produces(APPLICATION_JSON_VALUE)
                .summary("Updates the entity: " + entityModel.getName())
                .description("Updates the entity. Only fields that are passed in the JSON object " +
                        "(the request body) are updated.")
                .parameter(entityIdParam)
                .parameter(entityParam)
                .response(200, new Response()
                        .description("Success. The updated entity is returned in the response body.")
                        .schema(new RefProperty(ENTITY_DEFINITION_PREFIX + entityModel.getName())))
                .response(403, getErrorResponse("Forbidden. The user doesn't have permissions to update the entity."))
                .response(404, getErrorResponse("Not found. MetaClass for the entity with the given name not found."));
    }

    protected Operation generateEntityDeleteOperation(ModelImpl entityModel) {
        return new Operation()
                .tag(entityModel.getName())
                .produces(APPLICATION_JSON_VALUE)
                .summary("Deletes the entity: " + entityModel.getName())
                .parameter(new PathParameter()
                        .name("entityId")
                        .description("Entity identifier")
                        .required(true)
                        .property(new StringProperty()))
                .response(200, new Response().description("Success. Entity was deleted."))
                .response(403, getErrorResponse("Forbidden. The user doesn't have permissions to delete the entity"))
                .response(404, getErrorResponse("Not found. MetaClass for the entity with the given name not found."));
    }

    protected Operation generateEntitySearchOperation(ModelImpl entityModel, RequestMethod method) {
        Operation operation = new Operation()
                .tag(entityModel.getName())
                .produces(APPLICATION_JSON_VALUE)
                .summary("Find entities by filter conditions: " + entityModel.getName())
                .description("Finds entities by filter conditions. The filter is defined by JSON object " +
                        "that is passed as in URL parameter.")
                .response(200, new Response()
                        .description("Success. Entities that conforms filter conditions are returned in the response body.")
                        .schema(new ArrayProperty(new RefProperty(ENTITY_DEFINITION_PREFIX + entityModel.getName()))))
                .response(400, getErrorResponse("Bad request. For example, the condition value cannot be parsed."))
                .response(403, getErrorResponse("Forbidden. The user doesn't have permissions to read the entity."))
                .response(404, getErrorResponse("Not found. MetaClass for the entity with the given name not found."));

        if (RequestMethod.GET == method) {
            QueryParameter parameter = new QueryParameter()
                    .name("filter")
                    .required(true)
                    .property(new StringProperty().description("JSON with filter definition"));
            operation.parameter(parameter);
        } else {
            BodyParameter parameter = new BodyParameter()
                    .name("filter")
                    .schema(new ModelImpl()
                            .property("JSON with filter definition", new StringProperty()));
            parameter.setRequired(true);
            operation.parameter(parameter);
        }

        operation.getParameters().addAll(generateEntityOptionalParams(false));

        return operation;
    }

    protected List<Parameter> generateEntityOptionalParams(boolean singleEntityOperation) {
        List<Parameter> singleEntityParams = Arrays.asList(
                new QueryParameter()
                        .name("dynamicAttributes")
                        .description("Specifies whether entity dynamic attributes should be returned.")
                        .property(new BooleanProperty()),
                new QueryParameter()
                        .name("returnNulls")
                        .description("Specifies whether null fields will be written to the result JSON.")
                        .property(new BooleanProperty()),
                new QueryParameter()
                        .name("view")
                        .description("Name of the view which is used for loading the entity.")
                        .property(new StringProperty())
        );

        if (singleEntityOperation) {
            return singleEntityParams;
        }

        List<Parameter> multipleEntityParams = new ArrayList<>(Arrays.asList(
                new QueryParameter()
                        .name("returnCount")
                        .description("Specifies whether the total count of entities should be returned in the " +
                                "'X-Total-Count' header.")
                        .property(new BooleanProperty()),
                new QueryParameter()
                        .name("offset")
                        .description("Position of the first result to retrieve.")
                        .property(new StringProperty()),
                new QueryParameter()
                        .name("limit")
                        .description("Number of extracted entities.")
                        .property(new StringProperty()),
                new QueryParameter()
                        .name("sort")
                        .description("Name of the field to be sorted by. If the name is preceding by the '+' " +
                                "character, then the sort order is ascending, if by the '-' character then " +
                                "descending. If there is no special character before the property name, then " +
                                "ascending sort will be used.")
                        .property(new StringProperty())
        ));
        multipleEntityParams.addAll(singleEntityParams);

        return multipleEntityParams;
    }

    protected ModelImpl generateEntityModel(MetaClass entityClass) {
        Map<String, Property> properties = new LinkedHashMap<>();

        properties.put(ENTITY_NAME_PROP, new StringProperty()
                ._default(entityClass.getName()));
        properties.put(INSTANCE_NAME_PROP, getNamePatternProperty(entityClass));

        for (MetaProperty metaProperty : entityClass.getProperties()) {
            String fieldName = metaProperty.getName();
            Class<?> propertyType = metaProperty.getJavaType();
            String propertyTypeName = propertyType.getName();

            if (Collection.class.isAssignableFrom(propertyType)) {
                String collectionItemsType = metaProperty.getRange().asClass().getJavaClass().getName();
                Property itemsProperty = getPropertyFromJavaType(collectionItemsType);

                Property collectionProperty = new ArrayProperty(itemsProperty);
                properties.put(fieldName, collectionProperty);
            } else if (Map.class.isAssignableFrom(propertyType)) {
                properties.put(fieldName, new ObjectProperty(Collections.emptyMap()));
            } else {
                properties.put(fieldName, getPropertyFromJavaType(propertyTypeName));
            }
        }

        ModelImpl model = new ModelImpl()
                .name(entityClass.getName());
        model.setProperties(properties);

        definitions.put("entities_" + model.getName(), model);

        return model;
    }

    protected Property getNamePatternProperty(MetaClass entityClass) {
        Property namePatternProperty = new StringProperty();
        Class<?> javaClass = entityClass.getJavaClass();
        NamePattern namePatternAnnotation = javaClass.getAnnotation(NamePattern.class);
        if (namePatternAnnotation != null) {
            namePatternProperty.setDefault(namePatternAnnotation.value());
        }
        return namePatternProperty;
    }

    /*
     * Services
     */

    protected Map<String, Path> generateServicesPaths() {
        Map<String, Path> paths = new LinkedHashMap<>();

        for (RestServicesConfiguration.RestServiceInfo serviceInfo : servicesConfiguration.getServiceInfos()) {
            String serviceName = serviceInfo.getName();

            for (RestMethodInfo methodInfo : serviceInfo.getMethods()) {
                String methodName = methodInfo.getName();
                paths.put(
                        String.format(SERVICE_PATH, serviceName, methodName),
                        generateServiceMethodPath(serviceName, methodInfo));
            }
        }

        return paths;
    }

    protected Path generateServiceMethodPath(String service, RestMethodInfo methodInfo) {
        return new Path()
                .get(generateServiceMethodOp(service, methodInfo, RequestMethod.GET))
                .post(generateServiceMethodOp(service, methodInfo, RequestMethod.POST));
    }

    protected Operation generateServiceMethodOp(String service, RestMethodInfo methodInfo, RequestMethod requestMethod) {
        Operation operation = new Operation()
                .tag(service)
                .produces(APPLICATION_JSON_VALUE)
                .summary(service + "#" + methodInfo.getName())
                .description("Executes the service method. This request expects query parameters with the names defined " +
                        "in services configuration on the middleware.")
                .response(200, new Response()
                        .description("Returns the result of the method execution. It can be of simple datatype " +
                                "as well as JSON that represents an entity or entities collection.")
                        .schema(new StringProperty()))
                .response(204, new Response().description("No content. This status is returned when the service " +
                        "method was executed successfully but returns null or is of void type."))
                .response(403, getErrorResponse("Forbidden. The user doesn't have permissions to invoke the service method."));

        operation.setParameters(generateServiceMethodParams(service, methodInfo, requestMethod));

        return operation;
    }

    protected List<Parameter> generateServiceMethodParams(String service, RestMethodInfo methodInfo, RequestMethod requestMethod) {
        if (RequestMethod.GET == requestMethod) {
            return methodInfo.getParams()
                    .stream()
                    .map(p -> generateServiceGetOpParam(service, methodInfo.getName(), p, requestMethod))
                    .collect(Collectors.toList());
        } else {
            String paramName = String.format(POST_PARAM_NAME, service, methodInfo.getName(), requestMethod.name());

            parameters.put(paramName, generateServicePostOpParam(methodInfo.getParams()));

            return Collections.singletonList(new RefParameter(PARAMETERS_PREFIX + paramName));
        }
    }

    protected Parameter generateServiceGetOpParam(String service, String method, RestMethodParamInfo param, RequestMethod requestMethod) {
        String paramName = String.format(GET_PARAM_NAME, service, method, param.getName(), requestMethod.name());

        parameters.put(paramName, generateGetOperationParam(new Pair<>(param.getName(), param.getType())));

        return new RefParameter(PARAMETERS_PREFIX + paramName);
    }

    protected Parameter generateServicePostOpParam(List<RestMethodParamInfo> params) {
        Map<String, Property> modelProps = params.stream()
                .collect(Collectors.toMap(
                        RestMethodParamInfo::getName,
                        p -> getPropertyFromJavaType(p.getType())));

        ModelImpl parameterModel = new ModelImpl();
        parameterModel.setProperties(modelProps);

        BodyParameter parameter = new BodyParameter()
                .name("paramsObject")
                .schema(parameterModel);
        parameter.setRequired(true);

        return parameter;
    }

    /*
     * Queries
     */

    protected Map<String, Path> generateQueriesPaths() {
        Map<String, Path> paths = new LinkedHashMap<>();

        for (RestQueriesConfiguration.QueryInfo queryInfo : queriesConfiguration.getQueries()) {
            String entity = queryInfo.getEntityName();
            String queryName = queryInfo.getName();

            paths.put(
                    String.format(QUERY_PATH, entity, queryName),
                    generateQueryPath(queryInfo));

            paths.put(
                    String.format(QUERY_COUNT_PATH, entity, queryName),
                    generateQueryCountPath(queryInfo));
        }

        return paths;
    }

    protected Path generateQueryPath(RestQueriesConfiguration.QueryInfo query) {
        return new Path()
                .get(generateQueryOperation(query, RequestMethod.GET))
                .post(generateQueryOperation(query, RequestMethod.POST));
    }

    protected Path generateQueryCountPath(RestQueriesConfiguration.QueryInfo query) {
        return new Path()
                .get(generateQueryCountOperation(query, RequestMethod.GET))
                .post(generateQueryCountOperation(query, RequestMethod.POST));
    }

    protected Operation generateQueryOperation(RestQueriesConfiguration.QueryInfo query, RequestMethod method) {
        Operation operation = new Operation()
                .tag(query.getEntityName() + " Queries")
                .produces(APPLICATION_JSON_VALUE)
                .summary(query.getName())
                .description("Executes a predefined query. Query parameters must be passed in the request body as JSON map.")
                .response(200, new Response()
                        .description("Success")
                        .schema(new ArrayProperty(new RefProperty(ENTITY_DEFINITION_PREFIX + query.getEntityName()))))
                .response(403, getErrorResponse("Forbidden. A user doesn't have permissions to read the entity."))
                .response(404, getErrorResponse("Not found. MetaClass for the entity with the given name not found."));

        operation.setParameters(generateQueryOpParams(query, method, true));

        return operation;
    }

    protected Operation generateQueryCountOperation(RestQueriesConfiguration.QueryInfo query, RequestMethod method) {
        Operation operation = new Operation()
                .tag(query.getEntityName() + " Queries")
                .produces(APPLICATION_JSON_VALUE)
                .summary("Return a number of entities in query result")
                .description("Returns a number of entities that matches the query. You can use the all keyword for " +
                        "the queryNameParam to get the number of all available entities.")
                .response(200, new Response()
                        .description("Success. Entities count is returned")
                        .schema(new IntegerProperty().description("Entities count")))
                .response(403, getErrorResponse("Forbidden. The user doesn't have permissions to read the entity."))
                .response(404, getErrorResponse("MetaClass not found or query with the given name not found"));

        operation.setParameters(generateQueryOpParams(query, method, false));

        return operation;
    }

    protected List<Parameter> generateQueryOpParams(RestQueriesConfiguration.QueryInfo query, RequestMethod method,
                                                    boolean generateOptionalParams) {
        List<Parameter> optionalParams = generateOptionalParams ?
                generateOptionalQueryParams() : Collections.emptyList();

        if (RequestMethod.GET == method) {
            List<Parameter> queryParams = query.getParams()
                    .stream()
                    .map(p -> generateQueryGetOpParam(query, p))
                    .collect(Collectors.toList());

            queryParams.addAll(optionalParams);

            return queryParams;
        } else {
            String paramName = String.format(POST_PARAM_NAME, query.getEntityName(), query.getName(), method.name());

            parameters.put(paramName, generateQueryPostOpParam(query.getParams()));

            List<Parameter> queryParams = new ArrayList<>();
            queryParams.add(new RefParameter(PARAMETERS_PREFIX + paramName));
            queryParams.addAll(optionalParams);

            return queryParams;
        }
    }

    protected List<Parameter> generateOptionalQueryParams() {
        return Arrays.asList(
                new QueryParameter()
                        .name("dynamicAttributes")
                        .description("Specifies whether entity dynamic attributes should be returned.")
                        .property(new BooleanProperty()),
                new QueryParameter()
                        .name("returnCount")
                        .description("Specifies whether the total count of entities should be returned in the " +
                                "'X-Total-Count' header.")
                        .property(new BooleanProperty()),
                new QueryParameter()
                        .name("returnNulls")
                        .description("Specifies whether null fields will be written to the result JSON.")
                        .property(new BooleanProperty()),
                new QueryParameter()
                        .name("view")
                        .description("Name of the view which is used for loading the entity. Specify this parameter " +
                                "if you want to extract entities with the view other than it is defined in the REST " +
                                "queries configuration file.")
                        .property(new StringProperty()),
                new QueryParameter()
                        .name("offset")
                        .description("Position of the first result to retrieve.")
                        .property(new StringProperty()),
                new QueryParameter()
                        .name("limit")
                        .description("Number of extracted entities.")
                        .property(new StringProperty())
        );
    }

    protected Parameter generateQueryGetOpParam(RestQueriesConfiguration.QueryInfo query, RestQueriesConfiguration.QueryParamInfo param) {
        String paramName = String.format(GET_PARAM_NAME, query.getEntityName(), query.getName(), param.getName(),
                RequestMethod.GET.name());

        parameters.put(paramName, generateGetOperationParam(new Pair<>(param.getName(), param.getType())));

        return new RefParameter(PARAMETERS_PREFIX + paramName);
    }

    protected Parameter generateQueryPostOpParam(List<RestQueriesConfiguration.QueryParamInfo> params) {
        Map<String, Property> modelProps = params.stream()
                .collect(Collectors.toMap(
                        RestQueriesConfiguration.QueryParamInfo::getName,
                        p -> getPropertyFromJavaType(p.getType())));

        ModelImpl parameterModel = new ModelImpl();
        parameterModel.setProperties(modelProps);

        BodyParameter parameter = new BodyParameter()
                .name("paramsObject");
        parameter.setRequired(true);

        return parameter.schema(parameterModel);
    }

    /*
     * Common
     */

    protected Parameter generateGetOperationParam(Pair<String, String> param) {
        boolean paramIsArray = param.getSecond() != null && param.getSecond().contains(ARRAY_SIGNATURE);

        return new QueryParameter()
                .name(param.getFirst())
                .required(true)
                .type(paramIsArray ? "array" : "string")
                .items(paramIsArray ? new StringProperty() : null);
    }

    protected Property getPropertyFromJavaType(String type) {
        if (type == null) {
            return new StringProperty();
        }

        if (type.contains(ARRAY_SIGNATURE)) {
            String itemsType = type.replace(ARRAY_SIGNATURE, "");
            return new ArrayProperty(getPropertyFromJavaType(itemsType));
        }

        Property primitiveProperty = getPrimitiveProperty(type);
        if (primitiveProperty != null) {
            return primitiveProperty;
        }

        Property entityProperty = getObjectProperty(type);
        if (entityProperty != null) {
            return entityProperty;
        }

        return new StringProperty().description(type);
    }

    protected Property getObjectProperty(String classFqn) {
        Class<?> clazz;
        try {
            clazz = ReflectionHelper.loadClass(classFqn);
        } catch (ClassNotFoundException e) {
            return null;
        }

        MetaClass metaClass = metadata.getClass(clazz);
        if (metaClass != null) {
            return new ObjectProperty()
                    .description(metaClass.getName());
        }

        if (Enum.class.isAssignableFrom(clazz)) {
            return new StringProperty().description(classFqn);
        }

        return null;
    }

    protected Property getPrimitiveProperty(String type) {
        String primitiveType = type;
        if (type.contains(".")) {
            primitiveType = primitiveType.substring(primitiveType.lastIndexOf(".") + 1).toLowerCase();
        }

        switch (primitiveType) {
            case "boolean":
                return new BooleanProperty().example(true);
            case "float":
            case "double":
                return new DoubleProperty().example("3.14");
            case "byte":
            case "short":
            case "int":
            case "integer":
                return new IntegerProperty().example(42);
            case "long":
                return new LongProperty().example(Long.MAX_VALUE >> 4);
            case "date":
                return new DateProperty().example("2005-14-10T13:17:42.16Z");
            case "uuid":
                UUIDProperty uuidProp = new UUIDProperty();
                uuidProp.setExample("19474a3b-99b5-482e-9e77-852be9adf817");
                return uuidProp;
            case "string":
                return new StringProperty().example("String");
            default:
                return null;
        }
    }

    protected Response getErrorResponse(String msg) {
        return new Response()
                .description(msg)
                .schema(getErrorSchema());
    }

    protected Property getErrorSchema() {
        return new ObjectProperty()
                .property("error", new StringProperty().description("Error message"))
                .property("details", new StringProperty().description("Detailed error description"));
    }
}
