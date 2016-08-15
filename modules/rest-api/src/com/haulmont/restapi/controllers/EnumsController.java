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

import com.haulmont.restapi.data.EnumInfo;
import com.haulmont.restapi.service.EnumsControllerManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller that is used for getting an information about enums
 */
@RestController
@RequestMapping(path = "/api/metadata/enums", produces = "application/json; charset=UTF-8")
public class EnumsController {

    @Inject
    protected EnumsControllerManager enumsControllerManager;

    @GetMapping
    public List<EnumInfo> getAllEnumInfos() {
        return enumsControllerManager.getAllEnumInfos();
    }

    @GetMapping("/{enumClassName:.+}")
    public EnumInfo getEnumInfo(@PathVariable String enumClassName) {
        return enumsControllerManager.getEnumInfo(enumClassName);
    }

}
