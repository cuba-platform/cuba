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

import com.haulmont.bali.util.Preconditions;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Crockford Base32 encoding implementation that is used to serialize UUID values.
 * <p>
 *
 * <a href="https://www.crockford.com/wrmg/base32.html">Crockford Base32 encoding</a>
 *
 * @see UrlIdSerializer
 */
public final class CrockfordUuidEncoder {

    protected static final int INVALID_CHAR = -1;
    protected static final int BASE = 32;

    protected static final String CROCKFORD_CHARSET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ";

    protected static final char[] encodeTable = new char[BASE];
    protected static final int[] decodeTable = new int['z' + 1];

    protected static final String STRING_UUID_SPLIT_REGEX = "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})";
    protected static final Pattern STRING_UUID_SPLIT_PATTERN = Pattern.compile(STRING_UUID_SPLIT_REGEX);

    static {
        buildEncodeTable();
        buildDecodeTable();
    }

    private CrockfordUuidEncoder() {
    }

    /**
     * Performs Base32 encoding for the given {@code uuid}.
     *
     * @param uuid UUID
     * @return Base32 encoded string
     */
    public static String encode(UUID uuid) {
        Preconditions.checkNotNullArgument(uuid, "Unable to encode null UUID value");

        String noHyphensUuid = uuid.toString()
                .replaceAll("-", "");
        BigInteger number = new BigInteger(noHyphensUuid, 16);

        String encoded = number.toString(BASE)
                .toUpperCase();

        char[] translated = new char[encoded.length()];

        for (int i = 0; i < encoded.length(); i++) {
            int charDigit = Character.digit(encoded.charAt(i), BASE);
            translated[i] = encodeTable[charDigit];
        }

        encoded = new String(translated)
                .toLowerCase();

        return encoded;
    }

    /**
     * Performs Base32 decoding for the given {@code encoded} string.
     *
     * @param encoded encoded string
     * @return decoded UUID
     */
    public static UUID decode(String encoded) {
        Preconditions.checkNotNullArgument(encoded, "Unable to decode null string");

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

        String stringUuid = correct(decoded.toString(16));

        return parseUuid(stringUuid);
    }

    protected static UUID parseUuid(String stringUuid) {
        Matcher matcher = STRING_UUID_SPLIT_PATTERN.matcher(stringUuid);

        if (!matcher.matches()) {
            throw new RuntimeException(
                    String.format("An error occurred while deserializing UUID id: '%s'", stringUuid));
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= matcher.groupCount(); i++) {
            sb.append(matcher.group(i));
            if (i < matcher.groupCount()) {
                sb.append('-');
            }
        }

        return UUID.fromString(sb.toString());
    }

    // have to do this correction due to loosing leading zeros while converting to BigInt
    protected static String correct(String uuid) {
        if (uuid.length() == 32) {
            return uuid;
        }

        StringBuilder corrected = new StringBuilder(32);

        for (int i = 0; i < 32 - uuid.length(); i++) {
            corrected.append('0');
        }
        corrected.append(uuid);

        return corrected.toString();
    }

    protected static void buildEncodeTable() {
        for (int i = 0; i < BASE; i++) {
            CrockfordUuidEncoder.encodeTable[i] = CrockfordUuidEncoder.CROCKFORD_CHARSET.charAt(i);
        }
    }

    protected static void buildDecodeTable() {
        int[] table = CrockfordUuidEncoder.decodeTable;

        Arrays.fill(table, INVALID_CHAR);

        String lc = CrockfordUuidEncoder.CROCKFORD_CHARSET.toLowerCase();
        String uc = CrockfordUuidEncoder.CROCKFORD_CHARSET.toUpperCase();

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
