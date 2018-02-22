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
 *
 */
package com.haulmont.cuba.core.sys;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UuidProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.haulmont.cuba.core.entity.BaseEntityInternalAccess.*;
import static org.apache.commons.lang.StringUtils.rightPad;
import static org.apache.commons.lang.StringUtils.substring;

@Component(SecurityTokenManager.NAME)
public class SecurityTokenManager {
    public static final String NAME = "cuba_SecurityTokenManager";

    @Inject
    protected ServerConfig config;
    @Inject
    protected Metadata metadata;

    protected static final String READ_ONLY_ATTRIBUTES_KEY = "__readonlyAttributes";
    protected static final String REQUIRED_ATTRIBUTES_KEY = "__requiredAttributes";
    protected static final String HIDDEN_ATTRIBUTES_KEY = "__hiddenAttributes";
    protected static final Set SYSTEM_ATTRIBUTE_KEYS = Sets.newHashSet(READ_ONLY_ATTRIBUTES_KEY,
            REQUIRED_ATTRIBUTES_KEY, HIDDEN_ATTRIBUTES_KEY);
    /**
     * Encrypt filtered data and write the result to the security token
     */
    public void writeSecurityToken(Entity entity) {
        SecurityState securityState = getOrCreateSecurityState(entity);
        if (securityState != null) {
            JSONObject jsonObject = new JSONObject();
            Multimap<String, Object> filtered = getFilteredData(securityState);
            if (filtered != null) {
                Set<Map.Entry<String, Collection<Object>>> entries = filtered.asMap().entrySet();
                String[] filteredAttributes = new String[entries.size()];
                int i = 0;
                for (Map.Entry<String, Collection<Object>> entry : entries) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                    filteredAttributes[i++] = entry.getKey();
                }
                setFilteredAttributes(securityState, filteredAttributes);
            }
            if (!securityState.getReadonlyAttributes().isEmpty()) {
                jsonObject.put(READ_ONLY_ATTRIBUTES_KEY, securityState.getReadonlyAttributes());
            }
            if (!securityState.getHiddenAttributes().isEmpty()) {
                jsonObject.put(HIDDEN_ATTRIBUTES_KEY, securityState.getHiddenAttributes());
            }
            if (!securityState.getRequiredAttributes().isEmpty()) {
                jsonObject.put(REQUIRED_ATTRIBUTES_KEY, securityState.getRequiredAttributes());
            }

            String json = jsonObject.toString();
            byte[] encrypted;
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            try {
                encrypted = cipher.doFinal(json.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while generating security token", e);
            }
            setSecurityToken(securityState, encrypted);
        }
    }

    /**
     * Decrypt security token and read filtered data
     */
    public void readSecurityToken(Entity entity) {
        SecurityState securityState = getSecurityState(entity);
        if (getSecurityToken(entity) == null) {
            return;
        }
        Multimap<String, Object> filteredData = ArrayListMultimap.create();
        BaseEntityInternalAccess.setFilteredData(securityState, filteredData);
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        try {
            byte[] decrypted = cipher.doFinal(getSecurityToken(securityState));
            String json = new String(decrypted, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            for (Object key : jsonObject.keySet()) {
                if (!SYSTEM_ATTRIBUTE_KEYS.contains(key)) {
                    String elementName = String.valueOf(key);
                    JSONArray jsonArray = jsonObject.getJSONArray(elementName);
                    MetaProperty metaProperty = entity.getMetaClass().getPropertyNN(elementName);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Object id = jsonArray.get(i);
                        filteredData.put(elementName, convertId(id, metaProperty));
                    }
                }
            }
            if (jsonObject.has(READ_ONLY_ATTRIBUTES_KEY)) {
                BaseEntityInternalAccess.setReadonlyAttributes(securityState, parseJsonArrayAsStrings(
                        jsonObject.getJSONArray(READ_ONLY_ATTRIBUTES_KEY)));
            }
            if (jsonObject.has(HIDDEN_ATTRIBUTES_KEY)) {
                BaseEntityInternalAccess.setHiddenAttributes(securityState, parseJsonArrayAsStrings(
                        jsonObject.getJSONArray(HIDDEN_ATTRIBUTES_KEY)));
            }
            if (jsonObject.has(REQUIRED_ATTRIBUTES_KEY)) {
                BaseEntityInternalAccess.setRequiredAttributes(securityState, parseJsonArrayAsStrings(
                        jsonObject.getJSONArray(REQUIRED_ATTRIBUTES_KEY)));
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while reading security token", e);
        }
    }

    protected Cipher getCipher(int mode) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            byte[] encryptionKey = rightPad(substring(config.getKeyForSecurityTokenEncryption(), 0, 16), 16)
                    .getBytes(StandardCharsets.UTF_8);

            SecretKeySpec sKeySpec = new SecretKeySpec(encryptionKey, "AES");
            cipher.init(mode, sKeySpec);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while initiating encryption/decryption", e);
        }
    }

    protected String[] parseJsonArrayAsStrings(JSONArray array) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            result.add(array.getString(i));
        }
        return result.toArray(new String[result.size()]);
    }

    protected Object convertId(Object value, MetaProperty metaProperty) {
        MetaClass metaClass = metaProperty.getRange().asClass();
        if (HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
            return UuidProvider.fromString((String) value);
        }
        MetaProperty primaryKey = metadata.getTools().getPrimaryKeyProperty(metaClass);

        if (primaryKey != null) {
            Class type = primaryKey.getJavaType();
            if (UUID.class.equals(type)) {
                return UuidProvider.fromString((String) value);
            } else if (Long.class.equals(type) || IdProxy.class.equals(type)) {
                return ((Integer) value).longValue();
            } else if (Integer.class.equals(type)) {
                return value;
            } else if (String.class.equals(type)) {
                return value;
            } else {
                throw new IllegalStateException(
                        String.format("Unsupported primary key type: %s for %s", type.getSimpleName(), metaClass.getName()));
            }
        } else {
            throw new IllegalStateException(
                    String.format("Primary key not found for %s", metaClass.getName()));
        }
    }

    /**
     * INTERNAL.
     */
    public void addFiltered(BaseGenericIdEntity<?> entity, String property, Object id) {
        SecurityState securityState = getOrCreateSecurityState(entity);
        if (getFilteredData(securityState) == null) {
            setFilteredData(securityState, ArrayListMultimap.create());
        }
        getFilteredData(securityState).put(property, id);
    }

    /**
     * INTERNAL.
     */
    public void addFiltered(BaseGenericIdEntity<?> entity, String property, Collection ids) {
        SecurityState securityState = getOrCreateSecurityState(entity);
        if (getFilteredData(securityState) == null) {
            setFilteredData(securityState, ArrayListMultimap.create());
        }
        getFilteredData(securityState).putAll(property, ids);
    }
}