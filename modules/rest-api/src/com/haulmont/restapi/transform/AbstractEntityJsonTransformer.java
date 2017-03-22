/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.restapi.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.haulmont.restapi.config.RestJsonTransformations;
import com.haulmont.restapi.exception.RestAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

/**
 * Class containing a basic functionality of standard JSON transformer. It doest the following transformations:
 * <ul>
 *     <li>change entity name</li>
 *     <li>change attribute name</li>
 *     <li>remove attribute</li>
 * </ul>
 */
public abstract class AbstractEntityJsonTransformer implements EntityJsonTransformer {

    protected static final Logger log = LoggerFactory.getLogger(AbstractEntityJsonTransformer.class);

    protected String fromEntityName;
    protected String toEntityName;
    protected Map<String, String> attributesToRename = new HashMap<>();
    protected String version;
    protected JsonTransformationDirection direction;
    protected Set<String> attributesToRemove = new HashSet<>();

    @Inject
    protected RestJsonTransformations jsonTransformations;

    public AbstractEntityJsonTransformer(String fromEntityName,
                                         String toEntityName,
                                         String version,
                                         JsonTransformationDirection direction) {
        this.fromEntityName = fromEntityName;
        this.toEntityName = toEntityName;
        this.version = version;
        this.direction = direction;
    }

    @Override
    public String getTransformedEntityName() {
        return toEntityName;
    }

    /**
     * Method checks whether the passed JSON is an array of entities and if so it executes the {@link #transformEntityJson(ObjectNode, ObjectMapper)}
     * method for each array element. Method executes the {@link #transformEntityJson(ObjectNode, ObjectMapper)} method otherwise.
     * @param json JSON containing an entity or a list of entities
     * @return transformed JSON
     */
    @Override
    public String transformJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            if (rootNode.isArray()) {
                Iterator<JsonNode> iterator = rootNode.elements();
                while (iterator.hasNext()) {
                    ObjectNode entityJsonNode = (ObjectNode) iterator.next();
                    transformEntityJson(entityJsonNode, objectMapper);
                }
            } else if (rootNode.isObject()) {
                transformEntityJson((ObjectNode) rootNode, objectMapper);
            }
            return objectMapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            throw new RestAPIException("JSON transformation failed", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    protected void transformEntityJson(ObjectNode rootObjectNode, ObjectMapper objectMapper) throws IOException {
        replaceEntityName(rootObjectNode);
        renameAttributes(rootObjectNode);
        removeAttributes(rootObjectNode);
        transformNestedToOneReferences(rootObjectNode, objectMapper);
        transformNestedToManyReferences(rootObjectNode, objectMapper);
        doCustomTransformations(rootObjectNode, objectMapper);
    }

    protected void removeAttributes(ObjectNode rootObjectNode) {
        attributesToRemove.forEach(rootObjectNode::remove);
    }

    protected void transformNestedToOneReferences(ObjectNode rootObjectNode, ObjectMapper objectMapper) throws IOException {
        Iterator<Map.Entry<String, JsonNode>> iterator = rootObjectNode.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String attributeName = entry.getKey();
            JsonNode nestedJsonNode = entry.getValue();
            if (nestedJsonNode.isObject()) {
                JsonNode childEntityNameNode = nestedJsonNode.get("_entityName");
                if (childEntityNameNode != null) {
                    String childEntityNameValue = childEntityNameNode.asText();
                    EntityJsonTransformer childEntityTransformer = jsonTransformations.getTransformer(childEntityNameValue, this.version, this.direction);
                    if (childEntityTransformer != null) {
                        String transformedChildEntityJson = childEntityTransformer.transformJson(objectMapper.writeValueAsString(nestedJsonNode));
                        JsonNode transformedChildJsonNode = objectMapper.readTree(transformedChildEntityJson);
                        rootObjectNode.set(attributeName, transformedChildJsonNode);
                    }
                }
            }
        }
    }

    protected void transformNestedToManyReferences(ObjectNode rootObjectNode, ObjectMapper objectMapper) throws IOException {
        Iterator<Map.Entry<String, JsonNode>> iterator = rootObjectNode.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String attributeName = entry.getKey();
            JsonNode nestedJsonNode = entry.getValue();
            if (nestedJsonNode.isArray()) {
                if (nestedJsonNode.size() > 0) {
                    JsonNode firstArrayElement = nestedJsonNode.get(0);
                    JsonNode nestedEntityNameNode = firstArrayElement.get("_entityName");
                    if (nestedEntityNameNode != null) {
                        String nestedEntityNameValue = nestedEntityNameNode.asText();
                        EntityJsonTransformer nestedEntityTransformer = jsonTransformations.getTransformer(nestedEntityNameValue, this.version, this.direction);
                        if (nestedEntityTransformer != null) {
                            String transformedChildEntityJson = nestedEntityTransformer.transformJson(objectMapper.writeValueAsString(nestedJsonNode));
                            JsonNode transformedChildJsonNode = objectMapper.readTree(transformedChildEntityJson);
                            rootObjectNode.set(attributeName, transformedChildJsonNode);
                        }
                    }
                }
            }
        }
    }

    protected void renameAttributes(ObjectNode rootObjectNode) {
        //rename attributes
        for (Map.Entry<String, String> entry : attributesToRename.entrySet()) {
            String prevAttrName = entry.getKey();
            String newAttrName = entry.getValue();
            JsonNode attrNode = rootObjectNode.get(prevAttrName);
            if (attrNode != null) {
                rootObjectNode.set(newAttrName, attrNode);
                rootObjectNode.remove(prevAttrName);
            }
        }
    }

    protected void replaceEntityName(ObjectNode rootObjectNode) {
        JsonNode entityName = rootObjectNode.get("_entityName");
        if (entityName != null) {
            String entityNameValue = entityName.asText();
            if (fromEntityName.equals(entityNameValue)) {
                rootObjectNode.put("_entityName", toEntityName);
            }
        }
    }

    /**
     * Override this method in the subclass to perform custom transformations
     */
    protected void doCustomTransformations(ObjectNode rootObjectNode, ObjectMapper objectMapper) {
    }

    public String getFromEntityName() {
        return fromEntityName;
    }

    public void setFromEntityName(String fromEntityName) {
        this.fromEntityName = fromEntityName;
    }

    public String getToEntityName() {
        return toEntityName;
    }

    public void setToEntityName(String toEntityName) {
        this.toEntityName = toEntityName;
    }

    public Map<String, String> getAttributesToRename() {
        return attributesToRename;
    }

    public void setAttributesToRename(Map<String, String> attributesToRename) {
        this.attributesToRename = attributesToRename;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public JsonTransformationDirection getDirection() {
        return direction;
    }

    public void setDirection(JsonTransformationDirection direction) {
        this.direction = direction;
    }

    public Set<String> getAttributesToRemove() {
        return attributesToRemove;
    }

    public void setAttributesToRemove(Set<String> attributesToRemove) {
        this.attributesToRemove = attributesToRemove;
    }

}
