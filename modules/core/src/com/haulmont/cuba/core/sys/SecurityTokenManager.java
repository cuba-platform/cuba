/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

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

@Component
public class SecurityTokenManager {
    @Inject
    private Configuration configuration;

    /**
     * Encrypt filtered data and write the result to the security token
     */
    public void writeSecurityToken(BaseGenericIdEntity<?> resultEntity) {
        Multimap<String, UUID> filtered = resultEntity.__getFilteredData();
        JSONObject jsonObject = new JSONObject();
        if (filtered != null) {
            Set<Map.Entry<String, Collection<UUID>>> entries = filtered.asMap().entrySet();
            String[] filteredAttributes = new String[entries.size()];
            int i = 0;
            for (Map.Entry<String, Collection<UUID>> entry : entries) {
                jsonObject.put(entry.getKey(), entry.getValue());
                filteredAttributes[i++] = entry.getKey();
            }
            resultEntity.__setFilteredAttributes(filteredAttributes);
        }

        String json = jsonObject.toString();
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encrypted = cipher.doFinal(json.getBytes("UTF-8"));
            resultEntity.__setSecurityToken(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while generating security token", e);
        }
    }

    /**
     * Decrypt security token and read filtered data
     */
    public void readSecurityToken(BaseGenericIdEntity<?> resultEntity) {
        if (resultEntity.__getSecurityToken() == null) {
            return;
        }

        resultEntity.__setFilteredData(null);
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            byte[] decrypted = cipher.doFinal(resultEntity.__getSecurityToken());
            String json = new String(decrypted, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            for (Object key : jsonObject.keySet()) {
                String elementName = String.valueOf(key);
                JSONArray jsonArray = jsonObject.getJSONArray(elementName);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String id = jsonArray.getString(i);
                    resultEntity.__addFiltered(elementName, UUID.fromString(id));
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
}