package com.haulmont.chile.core.datatypes;

import java.util.List;

public interface Enumeration<T extends Enum> extends Datatype<T>{
    List<Enum> getValues();
}
