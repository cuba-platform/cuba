/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.sys.navigation;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * It is a Crockford Base32 encoding implementation that is used to serialize UUID ids.
 * <p>
 *
 * <a href="https://www.crockford.com/wrmg/base32.html">Crockford Base32 encoding</a>
 * <p>
 * <p>
 * UUID serializing example:
 * <pre>{@code
 *    UUID id = UUID.randomUUID();
 *
 *    String stringUuid = id.toString()
 *            .replaceAll("-", "");
 *
 *    BigInteger biUuid = new BigInteger(stringUuid, 16);
 *
 *    String encoded = CrockfordEncoder.encode(biUuid)
 *                         .toLowerCase();
 * }</pre>
 * <p>
 * <p>
 * Encoded UUID deserializing example:
 * <pre>{@code
 *    String encoded;
 *
 *    BigInteger biUuid = CrockfordEncoder.decode(encoded);
 *
 *    // String representation of UUID without hyphens
 *    String stringUuid = biUuid.toString(16);
 * }</pre>
 *
 * @see UrlIdSerializer
 */
public final class CrockfordEncoder {

    protected static final int INVALID_CHAR = -1;
    protected static final int BASE = 32;

    protected static final String CROCKFORD_CHARSET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ";

    protected static final char[] encodeTable = new char[BASE];
    protected static final int[] decodeTable = new int['z' + 1];

    static {
        buildEncodeTable();
        buildDecodeTable();
    }

    private CrockfordEncoder() {
    }

    /**
     * Performs Base32 encoding for the given {@code number}.
     *
     * @param number number
     * @return Base32 encoded string
     */
    public static String encode(BigInteger number) {
        return __encode(number);
    }

    /**
     * Performs Base32 decoding for the given {@code encodedString}.
     *
     * @param encodedString encoded string
     * @return decoded number
     */
    public static BigInteger decode(String encodedString) {
        return __decode(encodedString);
    }

    protected static String __encode(BigInteger number) {
        String formatted = number.toString(BASE)
                .toUpperCase();
        char[] translated = new char[formatted.length()];

        for (int i = 0; i < formatted.length(); i++) {
            int charDigit = Character.digit(formatted.charAt(i), BASE);
            translated[i] = encodeTable[charDigit];
        }

        formatted = new String(translated);

        return formatted;
    }

    protected static BigInteger __decode(String encoded) {
        BigInteger base = new BigInteger(Integer.toString(BASE));
        BigInteger decoded = BigInteger.ZERO;

        for (int i = 0; i < encoded.length(); i++) {
            char c = encoded.charAt(i);

            if (c >= decodeTable.length
                    || decodeTable[c] == INVALID_CHAR) {
                throw new NumberFormatException(
                        String.format("Invalid character '%s' at position: %s", c, i));
            }

            BigInteger num = new BigInteger(Integer.toString(decodeTable[c]));

            decoded = i == 0 ? num
                    : base.multiply(decoded)
                            .add(num);
        }

        return decoded;
    }

    protected static void buildEncodeTable() {
        for (int i = 0; i < BASE; i++) {
            CrockfordEncoder.encodeTable[i] = CrockfordEncoder.CROCKFORD_CHARSET.charAt(i);
        }
    }

    protected static void buildDecodeTable() {
        int[] table = CrockfordEncoder.decodeTable;

        Arrays.fill(table, INVALID_CHAR);

        String lc = CrockfordEncoder.CROCKFORD_CHARSET.toLowerCase();
        String uc = CrockfordEncoder.CROCKFORD_CHARSET.toUpperCase();

        for (int i = 0; i < BASE; i++) {
            char l = lc.charAt(i);
            char u = uc.charAt(i);

            if (table[l] == INVALID_CHAR && table[u] == INVALID_CHAR) {
                table[l] = i;
                table[u] = i;
            }
        }
    }
}
