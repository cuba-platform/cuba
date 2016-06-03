/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.sys.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.util.ObjectMap;
import com.esotericsoftware.kryo.util.Util;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.MetaClassImpl;
import com.haulmont.chile.core.model.impl.MetaPropertyImpl;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import de.javakaffee.kryoserializers.*;
import de.javakaffee.kryoserializers.cglib.CGLibProxySerializer;
import de.javakaffee.kryoserializers.guava.ImmutableListSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableMapSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableMultimapSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableSetSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateTimeSerializer;
import org.apache.commons.lang.ClassUtils;
import org.eclipse.persistence.indirection.IndirectCollection;
import org.eclipse.persistence.indirection.IndirectContainer;
import org.eclipse.persistence.indirection.ValueHolderInterface;
import org.eclipse.persistence.internal.indirection.jdk8.IndirectList;
import org.eclipse.persistence.internal.indirection.jdk8.IndirectMap;
import org.eclipse.persistence.internal.indirection.jdk8.IndirectSet;
import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;

/**
 * The serialization implementation using Kryo serialization
 */
public class KryoSerialization implements Serialization {

    protected static final Logger log = LoggerFactory.getLogger(KryoSerialization.class);

    protected final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return newKryoInstance();
        }
    };

    public KryoSerialization() {
    }

    protected Kryo newKryoInstance() {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new CubaInstantiatorStrategy());

        //To work properly must itself be loaded by the application classloader (i.e. by classloader capable of loading
        //all the other application classes). For web application it means placing this class inside webapp folder.
        kryo.setClassLoader(KryoSerialization.class.getClassLoader());

        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
        kryo.register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
        kryo.register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
        kryo.register(Collections.singletonList("").getClass(), new CollectionsSingletonListSerializer());
        kryo.register(Collections.singleton("").getClass(), new CollectionsSingletonSetSerializer());
        kryo.register(Collections.singletonMap("", "").getClass(), new CollectionsSingletonMapSerializer());
        kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
        kryo.register(InvocationHandler.class, new JdkProxySerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);

        kryo.register(CGLibProxySerializer.CGLibProxyMarker.class, new CGLibProxySerializer());
        kryo.register(DateTime.class, new JodaDateTimeSerializer());
        kryo.register(LocalDate.class, new JodaLocalDateSerializer());
        kryo.register(LocalDateTime.class, new JodaLocalDateTimeSerializer());
        ImmutableListSerializer.registerSerializers(kryo);
        ImmutableSetSerializer.registerSerializers(kryo);
        ImmutableMapSerializer.registerSerializers(kryo);
        ImmutableMultimapSerializer.registerSerializers(kryo);
        kryo.register(IndirectList.class, new IndirectContainerSerializer());
        kryo.register(IndirectMap.class, new IndirectContainerSerializer());
        kryo.register(IndirectSet.class, new IndirectContainerSerializer());

        kryo.register(org.eclipse.persistence.indirection.IndirectList.class, new IndirectContainerSerializer());
        kryo.register(org.eclipse.persistence.indirection.IndirectMap.class, new IndirectContainerSerializer());
        kryo.register(org.eclipse.persistence.indirection.IndirectSet.class, new IndirectContainerSerializer());

        //classes with custom serialization methods
        kryo.register(HashMultimap.class, new CubaJavaSerializer());
        kryo.register(ArrayListMultimap.class, new CubaJavaSerializer());
        kryo.register(MetaClassImpl.class, new CubaJavaSerializer());
        kryo.register(MetaPropertyImpl.class, new CubaJavaSerializer());
//        kryo.register(LoadContext.class, new CubaJavaSerializer());

        return kryo;
    }

    @Override
    @SuppressWarnings("finally")
    public void serialize(Object object, OutputStream os) {
        try (Output output = new CubaOutput(os)) {
            if (object instanceof BaseGenericIdEntity
                    && BaseEntityInternalAccess.isManaged((BaseGenericIdEntity) object)) {
                BaseEntityInternalAccess.setDetached((BaseGenericIdEntity) object, true);
            }
            kryos.get().writeClassAndObject(output, object);
        }
    }

    @Override
    public Object deserialize(InputStream is) {
        try (Input input = new Input(is)) {
            return kryos.get().readClassAndObject(input);
        }
    }

    @Override
    public byte[] serialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serialize(object, bos);
        return bos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        return deserialize(new ByteArrayInputStream(bytes));
    }

    public static class IndirectContainerSerializer extends CollectionSerializer {
        @Override
        public void write(Kryo kryo, Output output, Collection collection) {
            boolean isNotInstantiated = collection instanceof IndirectContainer
                    && !((IndirectContainer) collection).isInstantiated();
            output.writeBoolean(isNotInstantiated);
            if (!isNotInstantiated) {
                super.write(kryo, output, collection);
            }
        }

        @Override
        public Collection read(Kryo kryo, Input input, Class<Collection> type) {
            boolean isNotInstantiated = input.readBoolean();
            if (!isNotInstantiated) {
                return super.read(kryo, input, type);
            } else {
                IndirectCollection indirectCollection = (IndirectCollection) kryo.newInstance((Class) type);
                indirectCollection.setValueHolder(new ValueHolderInterface() {
                    @Override
                    public Object clone() {
                        return throwException();
                    }

                    @Override
                    public Object getValue() {
                        return throwException();
                    }

                    @Override
                    public boolean isInstantiated() {
                        return false;
                    }

                    @Override
                    public void setValue(Object value) {
                        throwException();
                    }

                    protected Object throwException() {
                        throw new IllegalStateException(
                                ExceptionLocalization.buildMessage("cannot_get_unfetched_attribute", new Object[]{"", ""}));
                    }
                });
                return (Collection) indirectCollection;
            }
        }
    }

    public static class CubaJavaSerializer extends JavaSerializer {
        @Override
        public Object read(Kryo kryo, Input input, Class type) {
            try {
                ObjectMap graphContext = kryo.getGraphContext();
                ObjectInputStream objectStream = (ObjectInputStream) graphContext.get(this);
                if (objectStream == null) {
                    objectStream = new ObjectInputStream(input) {
                        @Override
                        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                            return ClassUtils.getClass(KryoSerialization.class.getClassLoader(), desc.getName());
                        }
                    };
                    graphContext.put(this, objectStream);
                }
                return objectStream.readObject();
            } catch (Exception ex) {
                throw new KryoException("Error during Java deserialization.", ex);
            }
        }
    }

    /**
     * Strategy first tries to find and use a no-arg constructor and if it fails to do so, it should try to use
     * {@link StdInstantiatorStrategy} as a fallback, because this one does not invoke any constructor at all.
     * Strategy is a copy {@link Kryo.DefaultInstantiatorStrategy}, but if instantiation fails, use fallback to create object
     */
    public static class CubaInstantiatorStrategy implements org.objenesis.strategy.InstantiatorStrategy {
        private final InstantiatorStrategy fallbackStrategy = new StdInstantiatorStrategy();

        @Override
        public ObjectInstantiator newInstantiatorOf(Class type) {
            // Use ReflectASM if the class is not a non-static member class.
            Class enclosingType = type.getEnclosingClass();
            boolean isNonStaticMemberClass = enclosingType != null && type.isMemberClass()
                    && !Modifier.isStatic(type.getModifiers());
            if (!isNonStaticMemberClass) {
                try {
                    final ConstructorAccess access = ConstructorAccess.get(type);
                    return () -> {
                        try {
                            return access.newInstance();
                        } catch (Exception ex) {
                            if (log.isTraceEnabled()) {
                                log.trace("Unable instantiate class {}", Util.className(type), ex);
                            }
                            return fallbackStrategy.newInstantiatorOf(type).newInstance();
                        }
                    };
                } catch (Exception ignored) {
                }
            }
            // Reflection.
            try {
                Constructor ctor;
                try {
                    ctor = type.getConstructor((Class[]) null);
                } catch (Exception ex) {
                    ctor = type.getDeclaredConstructor((Class[]) null);
                    ctor.setAccessible(true);
                }
                final Constructor constructor = ctor;
                return () -> {
                    try {
                        return constructor.newInstance();
                    } catch (Exception ex) {
                        if (log.isTraceEnabled()) {
                            log.trace("Unable instantiate class {}", Util.className(type), ex);
                        }
                        return fallbackStrategy.newInstantiatorOf(type).newInstance();
                    }
                };
            } catch (Exception ignored) {
            }
            return fallbackStrategy.newInstantiatorOf(type);
        }
    }

    public static class CubaOutput extends Output {

        public CubaOutput(OutputStream outputStream) {
            super(outputStream);
        }

        @Override
        public void close() {
            //Prevent close stream. Stream closed only by:
            //com.haulmont.cuba.core.sys.remoting.HttpServiceExporter,
            //com.haulmont.cuba.core.sys.remoting.ClusteredHttpInvokerRequestExecutor()
            //Only flush buffer to output stream
            flush();
        }
    }
}