package com.haulmont.chile.core.common;

public interface ValueListener {
	void propertyChanged(Object item, String property, Object prevValue, Object value);
}
