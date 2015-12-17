/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.MetaClassImpl;
import com.haulmont.chile.core.model.impl.MetaPropertyImpl;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import de.javakaffee.kryoserializers.*;
import de.javakaffee.kryoserializers.cglib.CGLibProxySerializer;
import de.javakaffee.kryoserializers.guava.ImmutableListSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableMapSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableMultimapSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableSetSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateTimeSerializer;
import org.eclipse.persistence.indirection.IndirectCollection;
import org.eclipse.persistence.indirection.IndirectContainer;
import org.eclipse.persistence.indirection.ValueHolderInterface;
import org.eclipse.persistence.internal.indirection.jdk8.IndirectList;
import org.eclipse.persistence.internal.indirection.jdk8.IndirectMap;
import org.eclipse.persistence.internal.indirection.jdk8.IndirectSet;
import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class KryoSerialization implements Serialization {
    protected final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            return newKryoInstance();
        }
    };

    protected Kryo newKryoInstance() {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));

        //To work properly must itself be loaded by the application classloader (i.e. by classloader capable of loading
        //all the other application classes). For web application it means placing this class inside webapp folder.
        kryo.setClassLoader(KryoSerialization.class.getClassLoader());

        kryo.register(MetaClassImpl.class, new Serializer<MetaClassImpl>() {
            @Override
            public void write(Kryo kryo, Output output, MetaClassImpl object) {
                output.writeString(object.getName());
            }

            @Override
            public MetaClassImpl read(Kryo kryo, Input input, Class type) {
                Metadata metadata = AppBeans.get(Metadata.NAME);
                return (MetaClassImpl) metadata.getSession().getClassNN(input.readString());
            }
        });

        kryo.register(MetaPropertyImpl.class, new Serializer<MetaPropertyImpl>() {
            @Override
            public void write(Kryo kryo, Output output, MetaPropertyImpl object) {
                output.writeString(object.getDomain().getName());
                output.writeString(object.getName());
            }

            @Override
            public MetaPropertyImpl read(Kryo kryo, Input input, Class type) {
                Metadata metadata = AppBeans.get(Metadata.NAME);
                MetaClass metaClass = metadata.getSession().getClassNN(input.readString());
                return (MetaPropertyImpl) metaClass.getPropertyNN(input.readString());
            }
        });

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

        return kryo;
    }

    public void serialize(Object object, OutputStream os) {
        try (Output output = new Output(os)) {
            if (object instanceof BaseGenericIdEntity && ((BaseGenericIdEntity) object).__managed()) {
                ((BaseGenericIdEntity) object).__detached(true);
            }
            kryos.get().writeClassAndObject(output, object);
        }
    }

    public Object deserialize(InputStream is) {
        try (Input input = new Input(is)) {
            return kryos.get().readClassAndObject(input);
        }
    }

    public byte[] serialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serialize(object, bos);
        return bos.toByteArray();
    }

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
}
