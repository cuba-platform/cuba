/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.filestorage.amazon.util;

import java.util.Locale;

/**
 * Utilities for encoding and decoding binary data to and from different forms.
 */
public class BinaryUtils {
    /**
     * Converts byte data to a Hex-encoded string.
     *
     * @param data
     *            data to hex encode.
     *
     * @return hex-encoded string.
     */
    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte aData : data) {
            String hex = Integer.toHexString(aData);
            if (hex.length() == 1) {
                // Append leading zero.
                sb.append("0");
            } else if (hex.length() == 8) {
                // Remove ff prefix from negative numbers.
                hex = hex.substring(6);
            }
            sb.append(hex);
        }
        return sb.toString().toLowerCase(Locale.getDefault());
    }
}
