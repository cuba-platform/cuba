package com.haulmont.cuba.gui.components;

import javax.annotation.Nullable;
import java.util.Map;

public interface ValueProvider {

    @Nullable
    Map<String, Object> getValues();

    @Nullable
    Map<String, Object> getParameters();
}
