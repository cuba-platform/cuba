/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.12.2008 18:44:52
 *
 * $Id$
 */
package com.haulmont.cuba.security;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordDigest
{
    public static void main(String[] args) {
        String s = DigestUtils.md5Hex("admin");
        System.out.println(s);
    }
}
