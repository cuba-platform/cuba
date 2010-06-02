package com.haulmont.cuba.gui.components;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

public interface ValueProvider extends Serializable {

    @Nullable
    Map<String, Object> getValues();

    @Nullable
    Map<String, Object> getParameters();
}
