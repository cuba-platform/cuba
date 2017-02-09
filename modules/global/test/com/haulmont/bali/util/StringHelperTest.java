package com.haulmont.bali.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dmitry Chigileychik
 * @since 08.02.17.
 */
public class StringHelperTest {

    @Test
    public void testEmptyStringNormalize() {
        String result = StringHelper.removeExtraSpaces("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testStringNormalize() {
        String result = StringHelper.removeExtraSpaces(" aaa  bbb   ccc ddd ");
        Assert.assertEquals("aaa bbb ccc ddd",result);
    }

    @Test
    public void compareWithApacheNormalize() {
        String apacheResult = StringUtils.normalizeSpace(" aaa  bbb   ccc ddd ");
        String haulmontResult = StringHelper.removeExtraSpaces(" aaa  bbb   ccc ddd ");
        Assert.assertEquals(apacheResult, haulmontResult);
    }

    @Test
    public void testApacheNormalizeEmptyString() {
        String result = StringUtils.normalizeSpace("");
        Assert.assertEquals("", result);
    }
}
