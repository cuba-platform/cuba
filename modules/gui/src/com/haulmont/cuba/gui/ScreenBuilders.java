/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.builders.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.DataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.screen.FrameOwner;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component("cuba_ScreenBuilders")
public class ScreenBuilders {
    @Inject
    protected EditorBuilderProcessor editorBuilderProcessor;
    @Inject
    protected LookupBuilderProcessor lookupBuilderProcessor;
    @Inject
    protected ScreenBuilderProcessor screenBuilderProcessor;

    /**
     * Creates a screen builder.
     * <p>
     * Example of building a screen for editing an entity:
     * <pre>{@code
     * SomeCustomerEditor screen = screenBuilders.editor(Customer.class, this)
     *         .withScreen(SomeCustomerEditor.class)
     *         .withListComponent(customersTable)
     *         .editEntity(customersTable.getSingleSelected())
     *         .build();
     * }</pre>
     * <p>
     * Example of building a screen for creating a new entity instance:
     * <pre>{@code
     * SomeCustomerEditor screen = screenBuilders.editor(Customer.class, this)
     *         .withScreen(SomeCustomerEditor.class)
     *         .withListComponent(customersTable)
     *         .newEntity()
     *         .build();
     * }</pre>
     *
     * @param entityClass edited entity class
     * @param origin      invoking screen
     * @see #editor(ListComponent)
     */
    public <E extends Entity> EditorBuilder<E> editor(Class<E> entityClass, FrameOwner origin) {
        checkNotNullArgument(entityClass);
        checkNotNullArgument(origin);

        return new EditorBuilder<>(origin, entityClass, editorBuilderProcessor::buildEditor);
    }

    /**
     * Creates a screen builder using list component.
     * <p>
     * Example of building a screen for editing a currently selected entity:
     * <pre>{@code
     * SomeCustomerEditor screen = screenBuilders.editor(customersTable)
     *          .withScreen(SomeCustomerEditor.class)
     *          .build();
     * }</pre>
     * <p>
     * Example of building a screen for creating a new entity instance:
     * <pre>{@code
     * SomeCustomerEditor screen = screenBuilders.editor(customersTable)
     *          .withScreen(SomeCustomerEditor.class)
     *          .newEntity()
     *          .build();
     * }</pre>
     *
     * @param listComponent {@link Table}, {@link DataGrid} or another component containing the list of entities
     * @see #editor(Class, FrameOwner)
     */
    public <E extends Entity> EditorBuilder<E> editor(ListComponent<E> listComponent) {
        checkNotNullArgument(listComponent);
        checkNotNullArgument(listComponent.getFrame());

        FrameOwner frameOwner = listComponent.getFrame().getFrameOwner();
        Class<E> entityClass;
        DataUnit items = listComponent.getItems();
        if (items instanceof EntityDataUnit) {
            entityClass = ((EntityDataUnit) items).getEntityMetaClass().getJavaClass();
        } else {
            throw new IllegalStateException(String.format("Component %s is not bound to data", listComponent));
        }

        EditorBuilder<E> builder = new EditorBuilder<>(frameOwner, entityClass, editorBuilderProcessor::buildEditor);
        builder.withListComponent(listComponent);
        builder.editEntity(listComponent.getSingleSelected());
        return builder;
    }

    /**
     * Creates a screen builder using {@link PickerField} component.
     * <p>
     * Example of building a screen for editing a currently set value:
     * <pre>{@code
     * SomeCustomerEditor screen = screenBuilders.editor(customerPickerField)
     *          .withScreen(SomeCustomerEditor.class)
     *          .build();
     * }</pre>
     * <p>
     * Example of building a screen for creating a new entity instance:
     * <pre>{@code
     * SomeCustomerEditor screen = screenBuilders.editor(customerPickerField)
     *          .withScreen(SomeCustomerEditor.class)
     *          .newEntity()
     *          .build();
     * }</pre>
     *
     * @param field {@link PickerField}, {@link LookupPickerField} or another picker component
     * @see #editor(Class, FrameOwner)
     */
    public <E extends Entity> EditorBuilder<E> editor(PickerField<E> field) {
        checkNotNullArgument(field);
        checkNotNullArgument(field.getFrame());

        FrameOwner frameOwner = field.getFrame().getFrameOwner();
        Class<E> entityClass;
        MetaClass metaClass = field.getMetaClass();
        if (metaClass != null) {
            entityClass = metaClass.getJavaClass();
        } else {
            throw new IllegalStateException(String.format("Component %s is not bound to meta class", field));
        }

        EditorBuilder<E> builder = new EditorBuilder<>(frameOwner, entityClass, editorBuilderProcessor::buildEditor);
        builder.withField(field);
        builder.editEntity(field.getValue());
        return builder;
    }

    /**
     * Creates a screen builder.
     * <p>
     * Example of building a lookup screen for adding instance to data container:
     * <pre>{@code
     * SomeCustomerListScreen screen = screenBuilders.lookup(Customer.class, this)
     *         .withScreen(SomeCustomerListScreen.class)
     *         .withOpenMode(OpenMode.DIALOG)
     *         .withContainer(customersDc)
     *         .build();
     * }</pre>
     * <p>
     * Example of building a lookup screen with custom select handler:
     * <pre>{@code
     * SomeCustomerListScreen screen = screenBuilders.lookup(Customer.class, this)
     *         .withScreen(SomeCustomerListScreen.class)
     *         .withOpenMode(OpenMode.DIALOG)
     *         .withSelectHandler(customers -> {
     *             // customers contains selected values
     *         })
     *         .build();
     * }</pre>
     *
     * @param entityClass entity class
     * @param origin      invoking screen
     * @param <E>         type of entity
     */
    public <E extends Entity> LookupBuilder<E> lookup(Class<E> entityClass, FrameOwner origin) {
        checkNotNullArgument(entityClass);
        checkNotNullArgument(origin);

        return new LookupBuilder<>(origin, entityClass, lookupBuilderProcessor::buildLookup);
    }

    /**
     * Creates a screen builder using list component.
     * <p>
     * Example of building a lookup screen for adding row to table / tree component:
     * <pre>{@code
     * SomeCustomerListScreen screen = screenBuilders.lookup(customersTable)
     *         .withScreen(SomeCustomerListScreen.class)
     *         .build();
     * }</pre>
     *
     * @param listComponent {@link Table}, {@link DataGrid} or another component containing the list of entities
     * @param <E>           type of entity
     * @see #lookup(Class, FrameOwner)
     */
    public <E extends Entity> LookupBuilder<E> lookup(ListComponent<E> listComponent) {
        checkNotNullArgument(listComponent);
        checkNotNullArgument(listComponent.getFrame());

        FrameOwner frameOwner = listComponent.getFrame().getFrameOwner();
        Class<E> entityClass;
        DataUnit items = listComponent.getItems();
        if (items instanceof EntityDataUnit) {
            entityClass = ((EntityDataUnit) items).getEntityMetaClass().getJavaClass();
        } else {
            throw new IllegalStateException(String.format("Component %s is not bound to data", listComponent));
        }

        LookupBuilder<E> builder = new LookupBuilder<>(frameOwner, entityClass, lookupBuilderProcessor::buildLookup);
        builder.withListComponent(listComponent);
        return builder;
    }

    /**
     * Creates a screen builder using {@link PickerField} component.
     * <p>
     * Example of building a lookup screen for setting value to PickerField:
     * <pre>{@code
     * SomeCustomerListScreen screen = screenBuilders.lookup(customerPickerField)
     *         .withScreen(SomeCustomerListScreen.class)
     *         .build();
     * }</pre>
     *
     * @param field {@link PickerField}, {@link LookupPickerField} or another picker component
     * @param <E>   type of entity
     * @see #lookup(Class, FrameOwner)
     */
    public <E extends Entity> LookupBuilder<E> lookup(PickerField<E> field) {
        checkNotNullArgument(field);
        checkNotNullArgument(field.getFrame());

        FrameOwner frameOwner = field.getFrame().getFrameOwner();
        Class<E> entityClass;
        MetaClass metaClass = field.getMetaClass();
        if (metaClass != null) {
            entityClass = metaClass.getJavaClass();
        } else {
            throw new IllegalStateException(String.format("Component %s is not bound to meta class", field));
        }

        LookupBuilder<E> builder = new LookupBuilder<>(frameOwner, entityClass, lookupBuilderProcessor::buildLookup);
        builder.withField(field);
        return builder;
    }

    /**
     * Creates a screen builder.
     * <p>
     * Example of building a screen:
     * <pre>{@code
     * SomeScreen screen = screenBuilders.screen(this)
     *         .withScreen(SomeScreen.class)
     *         .build();
     * }</pre>
     *
     * @param origin invoking screen
     */
    public ScreenBuilder screen(FrameOwner origin) {
        checkNotNullArgument(origin);

        return new ScreenBuilder(origin, screenBuilderProcessor::buildScreen);
    }
}