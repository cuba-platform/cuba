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

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component(DatatypeRegistry.NAME)
public class DatatypeRegistryImpl implements DatatypeRegistry {

    private Logger log = LoggerFactory.getLogger(DatatypeRegistryImpl.class);

    protected Map<Class<?>, Datatype> datatypeByClass = new HashMap<>();
    protected Map<String, Datatype> datatypeByName = new HashMap<>();

    @Override
    public Datatype get(String name) {
        Datatype datatype = datatypeByName.get(name);
        if (datatype == null)
            throw new IllegalArgumentException("Datatype " + name + " is not found");
        return datatype;
    }

    @Nullable
    @Override
    public <T> Datatype<T> get(Class<T> clazz) {
        Datatype datatype = datatypeByClass.get(clazz);
        if (datatype == null) {
            // if no exact type found, try to find matching super-type
            for (Map.Entry<Class<?>, Datatype> entry : datatypeByClass.entrySet()) {
                if (entry.getKey().isAssignableFrom(clazz)) {
                    datatype = entry.getValue();
                    break;
                }
            }
        }
        //noinspection unchecked
        return datatype;
    }

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance
     * @throws IllegalArgumentException if no datatype suitable for the given type found
     */
    @Override
    public <T> Datatype<T> getNN(Class<T> clazz) {
        Datatype<T> datatype = get(clazz);
        if (datatype == null)
            throw new IllegalArgumentException("A datatype for " + clazz + " is not found");
        return datatype;
    }

    @Override
    public Set<String> getNames() {
        return Collections.unmodifiableSet(datatypeByName.keySet());
    }

    @Override
    public void register(Datatype datatype, boolean defaultForJavaClass) {
        Preconditions.checkNotNullArgument(datatype, "datatype is null");
        log.trace("Register datatype: {}, defaultForJavaClass: {}", datatype.getClass(), defaultForJavaClass);

        if (defaultForJavaClass) {
            datatypeByClass.put(datatype.getJavaClass(), datatype);
        }
        datatypeByName.put(datatype.getName(), datatype);
    }
}
