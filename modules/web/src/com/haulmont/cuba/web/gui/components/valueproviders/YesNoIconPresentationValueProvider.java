package com.haulmont.cuba.web.gui.components.valueproviders;

import com.vaadin.data.ValueProvider;
import org.apache.commons.lang3.BooleanUtils;

public class YesNoIconPresentationValueProvider implements ValueProvider<Boolean, String> {

    protected static final String BASE_STYLE = "boolean-value";

    private final String trueString;

    private final String falseString;

    public YesNoIconPresentationValueProvider() {
        this(Boolean.TRUE.toString(), Boolean.FALSE.toString());
    }

    public YesNoIconPresentationValueProvider(String trueString, String falseString) {
        this.trueString = trueString;
        this.falseString = falseString;
    }

    @Override
    public String apply(Boolean value) {
        if (BooleanUtils.isTrue(value)) {
            return getHtmlString(getTrueString());
        } else {
            return getHtmlString(getFalseString());
        }
    }

    public String getTrueString() {
        return trueString;
    }

    public String getFalseString() {
        return falseString;
    }

    protected String getHtmlString(String value) {
        return "<div class=\"" + BASE_STYLE + " " + BASE_STYLE + "-" + value + "\"/>";
    }
}
