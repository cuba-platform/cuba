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

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.restapi.exception.RestAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 */
@RestController()
public class PermissionsController {

    @Inject
    protected UserSessionSource userSessionSource;

    @RequestMapping(value = "/api/permissions", method = RequestMethod.GET)
    public Collection<PermissionInfo> getPermissions() {
        Collection<PermissionInfo> result = new ArrayList<>();
        for (PermissionType permissionType : PermissionType.values()) {
            Map<String, Integer> permissionsMap = userSessionSource.getUserSession().getPermissionsByType(permissionType);
            for (Map.Entry<String, Integer> entry : permissionsMap.entrySet()) {
                String target = entry.getKey();
                Integer value = entry.getValue();
                PermissionInfo permissionInfo = new PermissionInfo(permissionType.name(),
                        target,
                        getPermissionValueStr(permissionType, value),
                        value);
                result.add(permissionInfo);
            }
        }
        return result;
    }

    protected String getPermissionValueStr(PermissionType permissionType, int value) {
        switch (permissionType) {
            case SCREEN:
            case SPECIFIC:
            case ENTITY_OP:
                return value == 1 ? "ALLOW" : "DENY";
            case ENTITY_ATTR:
                switch (value) {
                    case 0: return "DENY";
                    case 1: return "VIEW";
                    case 2: return "MODIFY";
                }
            case UI:
                switch (value) {
                    case 0: return "HIDE";
                    case 1: return "READ_ONLY";
                    case 2: return "SHOW";
                }
        }
        throw new RestAPIException("Cannot evaluate permission value", "", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected class PermissionInfo {
        protected String type;
        protected String target;
        protected String value;
        protected int intValue;

        public PermissionInfo(String type, String target, String value, int intValue) {
            this.type = type;
            this.target = target;
            this.value = value;
            this.intValue = intValue;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }
    }
}
