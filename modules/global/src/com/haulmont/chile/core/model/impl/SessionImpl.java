package com.haulmont.chile.core.model.impl;

import java.util.*;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.Session;

public class SessionImpl implements Session {

	private final Map<String, MetaModel> models = new HashMap<String, MetaModel>();
	
	public MetaModel getModel(String name) {
		return models.get(name);
	}

	public Collection<MetaModel> getModels() {
		return models.values();
	}

	public MetaClass getClass(String name) {
        for (MetaModel model : models.values()) {
            final MetaClass metaClass = model.getClass(name);
            if (metaClass != null) return metaClass;
        }

        return null;
	}

	public MetaClass getClass(Class<?> clazz) {
        for (MetaModel model : models.values()) {
            final MetaClass metaClass = model.getClass(clazz);
            if (metaClass != null) return metaClass;
        }

        return null;
	}

	public Collection<MetaClass> getClasses() {
        final List<MetaClass> classes = new ArrayList<MetaClass>();
        for (MetaModel model : models.values()) {
            classes.addAll(model.getClasses());
        }

        return classes;
	}

    public void addModel(MetaModelImpl model) {
        models.put(model.getName(), model);
    }
}
