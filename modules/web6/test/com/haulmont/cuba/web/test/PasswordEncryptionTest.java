/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.test;

import junit.framework.TestCase;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author artamonov
 * @version $Id$
 */
public class PasswordEncryptionTest extends TestCase {
    private static final String PASSWORD_KEY = "25tuThUw";

    public void testEncryptDecrypt() {
        String password = "password";
        String encryptedPassword = encryptPassword(password);
        String decryptPassword = decryptPassword(encryptedPassword);

        assertEquals(password, decryptPassword);
    }

    protected String encryptPassword(String password) {
        SecretKeySpec key = new SecretKeySpec(PASSWORD_KEY.getBytes(), "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(PASSWORD_KEY.getBytes());
        String result;
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            result = new String(Hex.encodeHex(cipher.doFinal(password.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    protected String decryptPassword(String password) {
        SecretKeySpec key = new SecretKeySpec(PASSWORD_KEY.getBytes(), "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(PASSWORD_KEY.getBytes());
        String result;
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            result = new String(cipher.doFinal(Hex.decodeHex(password.toCharArray())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}