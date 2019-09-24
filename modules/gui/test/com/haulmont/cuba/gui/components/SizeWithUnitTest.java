/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SizeWithUnitTest extends Assertions {

    @Test
    public void testPixelSize() {
        String[] sizes = {
                "999999px",
                "1000px",
                "500px",
                "1px",
                "0px"
        };

        for (String sizeString : sizes) {
            SizeWithUnit size = SizeWithUnit.parseStringSize(sizeString);
            float expected = Float.parseFloat(sizeString.substring(0, sizeString.length() - 2));
            assertEquals(expected, size.getSize(), 0f);
            assertEquals(SizeUnit.PIXELS, size.getUnit());
        }
    }

    @Test
    public void testPercentageSize() {
        String[] sizes = {
                "999999%",
                "1000%",
                "500%",
                "1%",
                "0%"
        };

        for (String sizeString : sizes) {
            SizeWithUnit size = SizeWithUnit.parseStringSize(sizeString);
            float expected = Float.parseFloat(sizeString.substring(0, sizeString.length() - 1));
            assertEquals(expected, size.getSize(), 0f);
            assertEquals(SizeUnit.PERCENTAGE, size.getUnit());
        }
    }

    @Test
    public void testUnitless() {
        String[] sizes = {
                "999999",
                "1000",
                "500",
                "1",
                "0"
        };

        for (String sizeString : sizes) {
            SizeWithUnit size = SizeWithUnit.parseStringSize(sizeString);
            float expected = Float.parseFloat(sizeString);
            assertEquals(expected, size.getSize(), 0f);
            assertEquals(SizeUnit.PIXELS, size.getUnit());
        }
    }

    @Test
    public void testDefaultUnit() {
        String[] sizes = {
                "999999",
                "1000",
                "500",
                "1",
                "0"
        };

        for (String sizeString : sizes) {
            SizeWithUnit size = SizeWithUnit.parseStringSize(sizeString, SizeUnit.PERCENTAGE);
            float expected = Float.parseFloat(sizeString);
            assertEquals(expected, size.getSize(), 0f);
            assertEquals(SizeUnit.PERCENTAGE, size.getUnit());
        }
    }

    @Test
    public void testAutoSize() {
        String[] sizes = {
                "-1px",
                "AUTO",
                "-1",
                "",
                null
        };

        for (String sizeString : sizes) {
            SizeWithUnit size = SizeWithUnit.parseStringSize(sizeString);
            assertEquals(-1.0f, size.getSize(), 0f);
        }
    }

    @Test
    public void testInvalidSize() {
        String[] sizes = {
                "600em",
                "600rem",
                "600ex",
                "600in",
                "600cm",
                "600mm",
                "600pt",
                "600pc",
                "ten"
        };

        for (String sizeString : sizes) {
            try {
                SizeWithUnit.parseStringSize(sizeString);
                fail("Expected an IllegalArgumentException to be thrown");
            } catch (IllegalArgumentException ex) {
                assertEquals("Invalid size argument: \"" + sizeString
                        + "\" (should match " + SizeWithUnit.SIZE_PATTERN.pattern() + ")", ex.getMessage());
            }
        }
    }
}
