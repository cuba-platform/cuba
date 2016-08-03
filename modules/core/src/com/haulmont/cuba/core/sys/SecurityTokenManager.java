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
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang.StringUtils.rightPad;
import static org.apache.commons.lang.StringUtils.substring;

@Component(SecurityTokenManager.NAME)
public class SecurityTokenManager {
    public static final String NAME = "cuba_SecurityTokenManager";

    @Inject
    protected ServerConfig config;

    /**
     * Encrypt filtered data and write the result to the security token
     */
    public void writeSecurityToken(BaseGenericIdEntity<?> resultEntity) {
        Multimap<String, UUID> filtered = BaseEntityInternalAccess.getFilteredData(resultEntity);
        JSONObject jsonObject = new JSONObject();
        if (filtered != null) {
            Set<Map.Entry<String, Collection<UUID>>> entries = filtered.asMap().entrySet();
            String[] filteredAttributes = new String[entries.size()];
            int i = 0;
            for (Map.Entry<String, Collection<UUID>> entry : entries) {
                jsonObject.put(entry.getKey(), entry.getValue());
                filteredAttributes[i++] = entry.getKey();
            }
            BaseEntityInternalAccess.setFilteredAttributes(resultEntity, filteredAttributes);
        }

        String json = jsonObject.toString();
        byte[] encrypted;
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        try {
            encrypted = cipher.doFinal(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while generating security token", e);
        }
        BaseEntityInternalAccess.setSecurityToken(resultEntity, encrypted);
    }

    /**
     * Decrypt security token and read filtered data
     */
    public void readSecurityToken(BaseGenericIdEntity<?> resultEntity) {
        if (BaseEntityInternalAccess.getSecurityToken(resultEntity) == null) {
            return;
        }

        BaseEntityInternalAccess.setFilteredData(resultEntity, null);
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        try {
            byte[] decrypted = cipher.doFinal(BaseEntityInternalAccess.getSecurityToken(resultEntity));
            String json = new String(decrypted, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            for (Object key : jsonObject.keySet()) {
                String elementName = String.valueOf(key);
                JSONArray jsonArray = jsonObject.getJSONArray(elementName);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String id = jsonArray.getString(i);
                    addFiltered(resultEntity, elementName, UUID.fromString(id));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while reading security token", e);
        }
    }

    protected Cipher getCipher(int mode) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            byte[] encryptionKey = rightPad(substring(config.getKeyForSecurityTokenEncryption(), 0, 16), 16).getBytes();
            SecretKeySpec sKeySpec = new SecretKeySpec(encryptionKey, "AES");
            cipher.init(mode, sKeySpec);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while initiating encryption/decryption", e);
        }
    }

    /**
     * INTERNAL.
     */
    public void addFiltered(BaseGenericIdEntity<?> entity, String property, UUID uuid) {
        initFiltered(entity);

        BaseEntityInternalAccess.getFilteredData(entity).put(property, uuid);
    }

    /**
     * INTERNAL.
     */
    public void addFiltered(BaseGenericIdEntity<?> entity, String property, Collection<UUID> uuids) {
        initFiltered(entity);
        BaseEntityInternalAccess.getFilteredData(entity).putAll(property, uuids);
    }

    /**
     * INTERNAL.
     */
    protected void initFiltered(BaseGenericIdEntity<?> entity) {
        if (BaseEntityInternalAccess.getFilteredData(entity) == null) {
            BaseEntityInternalAccess.setFilteredData(entity, ArrayListMultimap.create());
        }
    }
}