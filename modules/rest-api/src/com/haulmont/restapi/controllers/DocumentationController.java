/*
 * Copyright (c) 2008-2016 Haulmont.
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

package com.haulmont.restapi.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.restapi.exception.RestAPIException;
import com.haulmont.restapi.swagger.SwaggerGenerator;
import io.swagger.models.Swagger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;

import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.WRITE_DOC_START_MARKER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController("cuba_DocumentationController")
@RequestMapping("/v2/docs")
public class DocumentationController {

    @Inject
    protected Resources resources;

    @Inject
    protected SwaggerGenerator swaggerGenerator;

    @RequestMapping(value = "/swagger.yaml", method = RequestMethod.GET, produces = "application/yaml")
    public String getSwaggerYaml() {
        return resources.getResourceAsString("classpath:rest-api-swagger.yaml");
    }

    @RequestMapping(value = "/swagger.json", method = RequestMethod.GET, produces = "application/json")
    public String getSwaggerJson() {
        String yaml = getSwaggerYaml();
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj;
        try {
            obj = yamlReader.readValue(yaml, Object.class);
            ObjectMapper jsonWriter = new ObjectMapper();
            return jsonWriter.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RestAPIException("Internal server error", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/swaggerDetailed.yaml", method = RequestMethod.GET, produces = "application/yaml")
    public String getProjectSwaggerYaml() {
        ObjectMapper jsonWriter = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        YAMLMapper yamlMapper = new YAMLMapper()
                .disable(WRITE_DOC_START_MARKER);

        try {
            Swagger swagger = swaggerGenerator.generateSwagger();

            JsonNode jsonNode = jsonWriter.readTree(
                    jsonWriter.writeValueAsBytes(swagger));

            return yamlMapper.writeValueAsString(jsonNode);
        } catch (IOException e) {
            throw new RestAPIException("An error occurred while generating Swagger documentation", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/swaggerDetailed.json", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getProjectSwaggerJson() {
        ObjectMapper jsonWriter = new ObjectMapper();
        jsonWriter.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            Swagger swagger = swaggerGenerator.generateSwagger();

            return jsonWriter.writeValueAsString(swagger);
        } catch (JsonProcessingException e) {
            throw new RestAPIException("An error occurred while generating Swagger documentation", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}