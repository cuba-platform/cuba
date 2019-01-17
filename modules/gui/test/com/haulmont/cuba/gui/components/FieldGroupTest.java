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

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.AbstractComponentTestCase.TestValueBinder;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.gui.components.data.value.ValueBinder;
import com.haulmont.cuba.gui.components.factories.DefaultComponentGenerationStrategy;
import com.haulmont.cuba.gui.components.factories.FieldGroupFieldFactoryImpl;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.security.entity.User;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@Ignore
public class FieldGroupTest extends CubaClientTestCase {

    @Mocked
    protected BackgroundWorker backgroundWorker;
    @Mocked
    protected ApplicationContext applicationContext;
    @Mocked
    protected AutowireCapableBeanFactory beanFactory;
    @Mocked
    protected BeanLocator beanLocator;
    @Mocked
    protected UserSessionSource userSessionSource;

    protected UiComponents uiComponents;

    protected TestFieldGroupFieldFactoryImpl fieldFactory;

    protected ValueBinder valueBinder;
    protected OptionsBinder optionsBinder;

    @Before
    public void setUp() {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        fieldFactory = new TestFieldGroupFieldFactoryImpl();

        this.valueBinder = new TestValueBinder(beanLocator, messageTools, metadata.getTools(), beanValidation, security);
        this.optionsBinder = new OptionsBinder();

        new Expectations() {
            {
                AppBeans.get(BackgroundWorker.NAME); result = backgroundWorker; minTimes = 0;
                AppBeans.get(BackgroundWorker.class); result = backgroundWorker; minTimes = 0;
                AppBeans.get(BackgroundWorker.NAME, BackgroundWorker.class); result = backgroundWorker; minTimes = 0;

                AppBeans.get(FieldGroupFieldFactory.NAME); result = fieldFactory; minTimes = 0;
                AppBeans.get(FieldGroupFieldFactory.class); result = fieldFactory; minTimes = 0;
                AppBeans.get(FieldGroupFieldFactory.NAME, FieldGroupFieldFactory.class); result = fieldFactory; minTimes = 0;

                applicationContext.getAutowireCapableBeanFactory(); result = beanFactory; minTimes = 0;

                beanFactory.autowireBean(any); result = new Delegate() {
                @SuppressWarnings("unused")
                void autowireBean(java.lang.Object o) throws org.springframework.beans.BeansException {
                    autowireUiComponent((Component) o);
                }
            }; minTimes = 0;

                userSessionSource.getLocale(); result = Locale.ENGLISH; minTimes = 0;

                beanLocator.get(MetadataTools.NAME); result = metadata.getTools(); minTimes = 0;

                beanLocator.get(ValueBinder.NAME); result = valueBinder; minTimes = 0;
                beanLocator.get(ValueBinder.class); result = valueBinder; minTimes = 0;
                beanLocator.get(ValueBinder.NAME, ValueBinder.class); result = valueBinder; minTimes = 0;

                beanLocator.get(OptionsBinder.NAME); result = optionsBinder; minTimes = 0;
                beanLocator.get(OptionsBinder.NAME, OptionsBinder.class); result = optionsBinder; minTimes = 0;

                beanLocator.get(Configuration.NAME); result = configuration; minTimes = 0;
                beanLocator.get(FormatStringsRegistry.NAME); result = formatStringsRegistry; minTimes = 0;
            }
        };

        initExpectations();

        messages.init();

        DefaultComponentGenerationStrategy strategy = new DefaultComponentGenerationStrategy(messages, null);

        UiComponentsGenerator uiComponentsGenerator = new UiComponentsGenerator(){
            @Override
            protected List<ComponentGenerationStrategy> getComponentGenerationStrategies() {
                return Collections.singletonList(strategy);
            }
        };

        uiComponents = createComponentsFactory();
        strategy.setUiComponents(uiComponents);
        fieldFactory.setUiComponentsGenerator(uiComponentsGenerator);
    }

    protected void autowireUiComponent(Component o) {

    }

    protected void initExpectations() {
    }

    protected UiComponents createComponentsFactory() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void newFieldGroup() {
        Component component = uiComponents.create(FieldGroup.NAME);
        assertTrue(component instanceof FieldGroup);
    }

    @Test
    public void initFields() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);

        FieldGroup.FieldConfig fc = fieldGroup.createField("name");
        fc.setProperty("name");
        fc.setDatasource(createTestDs());

        fieldGroup.addField(fc);
        fieldGroup.bind();
    }

    @Test
    public void initFieldsWithProperties() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);

        Datasource<User> testDs = createTestDs();

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fcName.setDatasource(testDs);
        fieldGroup.addField(fcName);

        FieldGroup.FieldConfig fcLogin = fieldGroup.createField("login");
        fcLogin.setProperty("login");
        fcLogin.setEnabled(false);
        fcLogin.setVisible(false);
        fcLogin.setDatasource(testDs);
        fieldGroup.addField(fcLogin);

        fieldGroup.bind();

        assertNotNull(fcLogin.getComponent());
        assertNotNull(fcName.getComponent());

        assertEquals(fcName, fieldGroup.getField("name"));
        assertEquals(fcLogin, fieldGroup.getField("login"));
    }

    @Test
    public void initWithCustomFields() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fcName.setComponent(uiComponents.create(TextField.NAME));
        fieldGroup.addField(fcName);

        FieldGroup.FieldConfig fcLogin = fieldGroup.createField("login");
        fcLogin.setProperty("login");
        fcLogin.setEnabled(false);
        fcLogin.setVisible(false);
        fcLogin.setComponent(uiComponents.create(TextField.NAME));
        fieldGroup.addField(fcLogin);

        assertNotNull(fcLogin.getComponent());
        assertNotNull(fcName.getComponent());

        assertEquals(2, fieldGroup.getOwnComponents().size());
        assertTrue(fieldGroup.getOwnComponents().contains(fcName.getComponent()));
        assertTrue(fieldGroup.getOwnComponents().contains(fcLogin.getComponent()));
    }

    @Test
    public void add() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fcName.setComponent(uiComponents.create(TextField.NAME));
        fieldGroup.addField(fcName);

        FieldGroup.FieldConfig fcLogin = fieldGroup.createField("login");
        fcLogin.setProperty("login");
        fcLogin.setEnabled(false);
        fcLogin.setVisible(false);
        fcLogin.setComponent(uiComponents.create(TextField.NAME));
        fieldGroup.addField(fcLogin);

        assertNotNull(fcLogin.getComponent());
        assertNotNull(fcName.getComponent());

        assertNotNull(getComposition(fcName));
        assertNotNull(getComposition(fcLogin));

        assertEquals(2, fieldGroup.getOwnComponents().size());
        assertTrue(fieldGroup.getOwnComponents().contains(fcName.getComponent()));
        assertTrue(fieldGroup.getOwnComponents().contains(fcLogin.getComponent()));

        assertEquals(2, getGridRows(fieldGroup));
        assertEquals(1, getGridColumns(fieldGroup));

        assertEquals(getComposition(fcName), getGridCellComposition(fieldGroup,0, 0));
        assertEquals(getComposition(fcLogin), getGridCellComposition(fieldGroup,0, 1));
    }

    @Test
    public void addWithColumn() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);
        fieldGroup.setColumns(2);

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fcName.setComponent(uiComponents.create(TextField.NAME));
        fieldGroup.addField(fcName);

        FieldGroup.FieldConfig fcLogin = fieldGroup.createField("login");
        fcLogin.setProperty("login");
        fcLogin.setEnabled(false);
        fcLogin.setVisible(false);
        fcLogin.setComponent(uiComponents.create(TextField.NAME));
        fieldGroup.addField(fcLogin, 1);

        assertNotNull(fcLogin.getComponent());
        assertNotNull(fcName.getComponent());

        assertNotNull(getComposition(fcName));
        assertNotNull(getComposition(fcLogin));

        assertEquals(2, fieldGroup.getOwnComponents().size());
        assertTrue(fieldGroup.getOwnComponents().contains(fcName.getComponent()));
        assertTrue(fieldGroup.getOwnComponents().contains(fcLogin.getComponent()));

        assertEquals(1, getGridRows(fieldGroup));
        assertEquals(2, getGridColumns(fieldGroup));

        assertEquals(getComposition(fcName), getGridCellComposition(fieldGroup,0, 0));
        assertEquals(getComposition(fcLogin), getGridCellComposition(fieldGroup,1, 0));
    }

    @Test
    public void addWithColumnAndRow() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fcName.setComponent(uiComponents.create(TextField.NAME));
        fieldGroup.addField(fcName);

        FieldGroup.FieldConfig fcLogin = fieldGroup.createField("login");
        fcLogin.setProperty("login");
        fcLogin.setEnabled(false);
        fcLogin.setVisible(false);
        fcLogin.setComponent(uiComponents.create(TextField.NAME));
        fieldGroup.addField(fcLogin, 0, 0);

        assertNotNull(fcLogin.getComponent());
        assertNotNull(fcName.getComponent());

        assertNotNull(getComposition(fcName));
        assertNotNull(getComposition(fcLogin));

        assertEquals(2, fieldGroup.getOwnComponents().size());
        assertTrue(fieldGroup.getOwnComponents().contains(fcName.getComponent()));
        assertTrue(fieldGroup.getOwnComponents().contains(fcLogin.getComponent()));

        assertEquals(2, getGridRows(fieldGroup));
        assertEquals(1, getGridColumns(fieldGroup));

        assertEquals(getComposition(fcName), getGridCellComposition(fieldGroup,0, 1));
        assertEquals(getComposition(fcLogin), getGridCellComposition(fieldGroup,0, 0));
    }

    @Test
    public void removeField() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);

        Datasource<User> testDs = createTestDs();

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fcName.setDatasource(testDs);
        fieldGroup.addField(fcName);

        FieldGroup.FieldConfig fcLogin = fieldGroup.createField("login");
        fcLogin.setProperty("login");
        fcLogin.setEnabled(false);
        fcLogin.setVisible(false);
        fcLogin.setDatasource(testDs);
        fieldGroup.addField(fcLogin);

        fieldGroup.removeField("login");

        assertEquals(fcName, fieldGroup.getField("name"));
        assertEquals(null, fieldGroup.getField("login"));

        fieldGroup.bind();

        assertEquals(1, getGridRows(fieldGroup));
        assertEquals(1, getGridColumns(fieldGroup));

        assertEquals(getComposition(fcName), getGridCellComposition(fieldGroup,0, 0));
    }

    @Test
    public void removeBoundField() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);

        Datasource<User> testDs = createTestDs();

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fcName.setDatasource(testDs);
        fieldGroup.addField(fcName);

        FieldGroup.FieldConfig fcLogin = fieldGroup.createField("login");
        fcLogin.setProperty("login");
        fcLogin.setEnabled(false);
        fcLogin.setVisible(false);
        fcLogin.setDatasource(testDs);
        fieldGroup.addField(fcLogin);

        fieldGroup.bind();

        fieldGroup.removeField("login");

        assertEquals(fcName, fieldGroup.getField("name"));
        assertEquals(null, fieldGroup.getField("login"));

        assertEquals(1, getGridRows(fieldGroup));
        assertEquals(1, getGridColumns(fieldGroup));

        assertEquals(getComposition(fcName), getGridCellComposition(fieldGroup,0, 0));
    }

    @Test
    public void addWithSet() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);
        fieldGroup.setColumns(2);

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fieldGroup.addField(fcName);

        FieldGroup.FieldConfig fcLogin = fieldGroup.createField("login");
        fcLogin.setProperty("login");
        fcLogin.setEnabled(false);
        fcLogin.setVisible(false);
        fieldGroup.addField(fcLogin, 1);

        fcName.setComponent(uiComponents.create(TextField.NAME));
        fcLogin.setComponent(uiComponents.create(TextField.NAME));

        assertNotNull(fcLogin.getComponent());
        assertNotNull(fcName.getComponent());

        assertNotNull(getComposition(fcName));
        assertNotNull(getComposition(fcLogin));

        assertEquals(2, fieldGroup.getOwnComponents().size());
        assertTrue(fieldGroup.getOwnComponents().contains(fcName.getComponent()));
        assertTrue(fieldGroup.getOwnComponents().contains(fcLogin.getComponent()));

        assertEquals(1, getGridRows(fieldGroup));
        assertEquals(2, getGridColumns(fieldGroup));

        assertEquals(getComposition(fcName), getGridCellComposition(fieldGroup, 0, 0));
        assertEquals(getComposition(fcLogin), getGridCellComposition(fieldGroup, 1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addExistingField() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);
        fieldGroup.setColumns(2);

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fieldGroup.addField(fcName);

        fieldGroup.addField(fcName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addIncorrectColumn() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);
        fieldGroup.setColumns(2);

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fieldGroup.addField(fcName);

        fieldGroup.addField(fcName, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addIncorrectRow() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);
        fieldGroup.setColumns(2);

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fieldGroup.addField(fcName);

        fieldGroup.addField(fcName, 0, 3);
    }

    @Test(expected = IllegalStateException.class)
    public void changeBoundComponent() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);
        fieldGroup.setColumns(2);

        FieldGroup.FieldConfig fcName = fieldGroup.createField("name");
        fcName.setProperty("name");
        fcName.setEditable(false);
        fieldGroup.addField(fcName);

        fieldGroup.bind();

        fcName.setComponent(uiComponents.create(TextArea.NAME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNonDefinedField() {
        FieldGroup fieldGroup = uiComponents.create(FieldGroup.class);
        fieldGroup.setColumns(2);

        fieldGroup.removeField("none");
    }

    protected Object getComposition(FieldGroup.FieldConfig fc) {
        Method getCompositionMethod = MethodUtils.getAccessibleMethod(fc.getClass(), "getComponent");
        Object composition;
        try {
            composition = getCompositionMethod.invoke(fc);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Invoke error", e);
        }
        return ((Component) composition).unwrap(Object.class);
    }

    protected int getGridRows(FieldGroup fieldGroup) {
        throw new UnsupportedOperationException();
    }

    protected int getGridColumns(FieldGroup fieldGroup) {
        throw new UnsupportedOperationException();
    }

    protected Object getGridCellComposition(FieldGroup fieldGroup, int col, int row) {
        throw new UnsupportedOperationException();
    }

    protected Datasource<User> createTestDs() {
        //noinspection unchecked
        Datasource<User> testDs = new DsBuilder()
                .setId("testDs")
                .setJavaClass(User.class)
                .setView(viewRepository.getView(User.class, View.LOCAL))
                .buildDatasource();

        testDs.setItem(metadata.create(User.class));
        ((DatasourceImpl) testDs).valid();

        return testDs;
    }

    @SuppressWarnings("ReassignmentInjectVariable")
    protected static class TestFieldGroupFieldFactoryImpl extends FieldGroupFieldFactoryImpl {
        public void setUiComponentsGenerator(UiComponentsGenerator generator) {
            this.uiComponentsGenerator = generator;
        }
    }
}