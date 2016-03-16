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
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.global.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
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
    private Configuration configuration;

    /**
     * Encrypt filtered data and write the result to the security token
     */
    public void writeSecurityToken(BaseGenericIdEntity<?> resultEntity) {
        Multimap<String, UUID> filtered = resultEntity.__filteredData();
        JSONObject jsonObject = new JSONObject();
        if (filtered != null) {
            Set<Map.Entry<String, Collection<UUID>>> entries = filtered.asMap().entrySet();
            String[] filteredAttributes = new String[entries.size()];
            int i = 0;
            for (Map.Entry<String, Collection<UUID>> entry : entries) {
                jsonObject.put(entry.getKey(), entry.getValue());
                filteredAttributes[i++] = entry.getKey();
            }
            resultEntity.__filteredAttributes(filteredAttributes);
        }

        String json = jsonObject.toString();
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encrypted = cipher.doFinal(json.getBytes("UTF-8"));
            resultEntity.__securityToken(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while generating security token", e);
        }
    }

    /**
     * Decrypt security token and read filtered data
     */
    public void readSecurityToken(BaseGenericIdEntity<?> resultEntity) {
        if (resultEntity.__securityToken() == null) {
            return;
        }

        resultEntity.__filteredData(null);
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            byte[] decrypted = cipher.doFinal(resultEntity.__securityToken());
            String json = new String(decrypted, "UTF-8");
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
            ServerConfig config = configuration.getConfig(ServerConfig.class);
            Cipher cipher = Cipher.getInstance("AES");
            byte[] encryptionKey = rightPad(substring(config.getKeyForSecurityTokenEncryption(), 0, 15), 16).getBytes();
            SecretKeySpec sKeySpec = new SecretKeySpec(encryptionKey, "AES");
            cipher.init(mode, sKeySpec);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while initiating encryption/decription", e);
        }
    }

    /**
     * INTERNAL.
     */
    public void addFiltered(BaseGenericIdEntity<?> entity, String property, UUID uuid) {
        initFiltered(entity);
        entity.__filteredData().put(property, uuid);
    }

    /**
     * INTERNAL.
     */
    public void addFiltered(BaseGenericIdEntity<?> entity, String property, Collection<UUID> uuids) {
        initFiltered(entity);
        entity.__filteredData().putAll(property, uuids);
    }

    /**
     * INTERNAL.
     */
    protected void initFiltered(BaseGenericIdEntity<?> entity) {
        if (entity.__filteredData() == null) {
            entity.__filteredData(ArrayListMultimap.create());
        }
    }
}