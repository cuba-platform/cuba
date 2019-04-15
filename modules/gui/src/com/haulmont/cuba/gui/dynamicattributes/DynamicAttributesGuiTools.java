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

package com.haulmont.cuba.gui.dynamicattributes;

import com.google.common.base.Strings;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Categorized;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.commonlookup.CommonLookupController;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.sys.ScreensHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component(DynamicAttributesGuiTools.NAME)
public class DynamicAttributesGuiTools {
    public static final String NAME = "cuba_DynamicAttributesGuiTools";

    @Inject
    protected DynamicAttributes dynamicAttributes;

    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected Metadata metadata;

    @Inject
    protected ReferenceToEntitySupport referenceToEntitySupport;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected ScreensHelper screensHelper;

    /**
     * Enforce the datasource to change modified status if dynamic attribute is changed
     */
    @SuppressWarnings("unchecked")
    public void listenDynamicAttributesChanges(final Datasource datasource) {
        if (datasource != null && datasource.getLoadDynamicAttributes()) {
            datasource.addItemPropertyChangeListener(e -> {
                if (DynamicAttributesUtils.isDynamicAttribute(e.getProperty())) {
                    ((DatasourceImplementation) datasource).modified(e.getItem());
                }
            });
        }
    }

    /**
     * Get attributes which should be added automatically to the screen and component.
     * Based on visibility settings from category attribute editor.
     */
    public Set<CategoryAttribute> getAttributesToShowOnTheScreen(MetaClass metaClass, String screen, @Nullable String component) {
        Collection<CategoryAttribute> attributesForMetaClass =
                dynamicAttributes.getAttributesForMetaClass(metaClass);
        Set<CategoryAttribute> categoryAttributes = new LinkedHashSet<>();

        for (CategoryAttribute attribute : attributesForMetaClass) {
            if (attributeShouldBeShownOnTheScreen(screen, component, attribute)) {
                categoryAttributes.add(attribute);
            }
        }

        return categoryAttributes;
    }

    /**
     * Method checks whether any class in the view hierarchy contains dynamic attributes that must be displayed on
     * the current screen
     */
    public boolean screenContainsDynamicAttributes(View mainDatasourceView, String screenId) {
        DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.class);
        Set<Class> classesWithDynamicAttributes = collectEntityClassesWithDynamicAttributes(mainDatasourceView);
        for (Class classWithDynamicAttributes : classesWithDynamicAttributes) {
            MetaClass metaClass = metadata.getClassNN(classWithDynamicAttributes);
            if (!dynamicAttributesGuiTools.getAttributesToShowOnTheScreen(metaClass, screenId, null)
                    .isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected Set<Class> collectEntityClassesWithDynamicAttributes(@Nullable View view) {
        if (view == null) {
            return Collections.emptySet();
        }

        DynamicAttributes dynamicAttributes = AppBeans.get(DynamicAttributes.class);
        Metadata metadata = AppBeans.get(Metadata.class);
        return collectEntityClasses(view, new HashSet<>()).stream()
                .filter(BaseGenericIdEntity.class::isAssignableFrom)
                .filter(aClass -> !dynamicAttributes.getAttributesForMetaClass(metadata.getClassNN(aClass)).isEmpty())
                .collect(Collectors.toSet());
    }

    protected Set<Class> collectEntityClasses(View view, Set<View> visited) {
        if (visited.contains(view)) {
            return Collections.emptySet();
        } else {
            visited.add(view);
        }

        HashSet<Class> classes = new HashSet<>();
        classes.add(view.getEntityClass());
        for (ViewProperty viewProperty : view.getProperties()) {
            if (viewProperty.getView() != null) {
                classes.addAll(collectEntityClasses(viewProperty.getView(), visited));
            }
        }
        return classes;
    }


    public void initDefaultAttributeValues(BaseGenericIdEntity item, MetaClass metaClass) {
        Preconditions.checkNotNullArgument(metaClass, "metaClass is null");
        Collection<CategoryAttribute> attributes =
                dynamicAttributes.getAttributesForMetaClass(metaClass);
        if (item.getDynamicAttributes() == null) {
            item.setDynamicAttributes(new HashMap<>());
        }
        ZonedDateTime currentTimestamp = AppBeans.get(TimeSource.NAME, TimeSource.class).now();
        boolean entityIsCategorized = item instanceof Categorized && ((Categorized) item).getCategory() != null;

        for (CategoryAttribute categoryAttribute : attributes) {
            setDefaultAttributeValue(item, categoryAttribute, entityIsCategorized, currentTimestamp);
        }
    }

    protected void setDefaultAttributeValue(BaseGenericIdEntity item, CategoryAttribute categoryAttribute,
                                   boolean entityIsCategorized, ZonedDateTime currentTimestamp) {
        String code = DynamicAttributesUtils.encodeAttributeCode(categoryAttribute.getCode());
        if (entityIsCategorized && !categoryAttribute.getCategory().equals(((Categorized) item).getCategory())) {
            item.setValue(code, null);//cleanup attributes from not dedicated category
            return;
        }

        if (item.getValue(code) != null) {
            return;//skip not null attributes
        }

        if (categoryAttribute.getDefaultValue() != null) {
            if (BooleanUtils.isTrue(categoryAttribute.getIsEntity())) {
                MetaClass entityMetaClass = metadata.getClassNN(categoryAttribute.getJavaClassForEntity());
                LoadContext<Entity> lc = new LoadContext<>(entityMetaClass).setView(View.MINIMAL);
                String pkName = referenceToEntitySupport.getPrimaryKeyForLoadingEntity(entityMetaClass);
                lc.setQueryString(format("select e from %s e where e.%s = :entityId", entityMetaClass.getName(), pkName))
                        .setParameter("entityId", categoryAttribute.getDefaultValue());
                Entity defaultEntity = dataManager.load(lc);
                item.setValue(code, defaultEntity);
            } else if (Boolean.TRUE.equals(categoryAttribute.getIsCollection())) {
                List<Object> list = new ArrayList<>();
                list.add(categoryAttribute.getDefaultValue());
                item.setValue(code, list);
            } else {
                item.setValue(code, categoryAttribute.getDefaultValue());
            }
        } else if (Boolean.TRUE.equals(categoryAttribute.getDefaultDateIsCurrent())) {
            if (PropertyType.DATE_WITHOUT_TIME.equals(categoryAttribute.getDataType())) {
                item.setValue(code, currentTimestamp.toLocalDate());
            } else {
                item.setValue(code, Date.from(currentTimestamp.toInstant()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void listenCategoryChanges(Datasource ds) {
        ds.addItemPropertyChangeListener(e -> {
            if ("category".equals(e.getProperty())) {
                initDefaultAttributeValues((BaseGenericIdEntity) e.getItem(), e.getItem().getMetaClass());
            }
        });
    }

    protected boolean attributeShouldBeShownOnTheScreen(String screen, String component, CategoryAttribute attribute) {
        Set<String> targetScreensSet = attribute.targetScreensSet();
        return targetScreensSet.contains(screen) || targetScreensSet.contains(screen + "#" + component);
    }

    /**
     * Initializes the pickerField for selecting the dynamic attribute value. If the CategoryAttribute has "where" or
     * "join" clauses then the data in lookup screens will be filtered with these clauses
     *
     * @param pickerField       PickerField component whose lookup action must be initialized
     * @param categoryAttribute CategoryAttribute that is represented by the pickerField
     */
    public void initEntityPickerField(PickerField pickerField, CategoryAttribute categoryAttribute) {
        Class javaClass = categoryAttribute.getJavaClassForEntity();
        if (javaClass == null) {
            throw new IllegalArgumentException("Entity type is not specified in category attribute");
        }
        MetaClass metaClass = metadata.getClassNN(javaClass);
        PickerField.LookupAction lookupAction = (PickerField.LookupAction) pickerField.getAction(PickerField.LookupAction.NAME);
        if (!Strings.isNullOrEmpty(categoryAttribute.getJoinClause())
                || !Strings.isNullOrEmpty(categoryAttribute.getWhereClause())) {
            lookupAction = createLookupAction(pickerField, categoryAttribute.getJoinClause(), categoryAttribute.getWhereClause());
            pickerField.addAction(lookupAction);
        }

        if (lookupAction == null) {
            lookupAction = pickerField.addLookupAction();
        }

        String screen = categoryAttribute.getScreen();
        if (StringUtils.isNotBlank(screen)) {
            lookupAction.setLookupScreen(screen);
        } else {
            screen = windowConfig.getBrowseScreenId(metaClass);
            Map<String, String> screensMap = screensHelper.getAvailableBrowserScreens(javaClass);
            if (windowConfig.findWindowInfo(screen) != null && screensMap.containsValue(screen)) {
                lookupAction.setLookupScreen(screen);
                lookupAction.setLookupScreenOpenType(OpenType.THIS_TAB);
            } else {
                lookupAction.setLookupScreen(CommonLookupController.SCREEN_ID);
                lookupAction.setLookupScreenParams(ParamsMap.of(CommonLookupController.CLASS_PARAMETER, metaClass));
                lookupAction.setLookupScreenOpenType(OpenType.DIALOG);
            }
        }
    }

    /**
     * Creates the collection datasource that is used for selecting the dynamic attribute value. If the
     * CategoryAttribute has "where" or "join" clauses then only items that satisfy these clauses will be presented in
     * the options datasource
     */
    public CollectionDatasource createOptionsDatasourceForLookup(MetaClass metaClass, String joinClause, String whereClause) {
        CollectionDatasource optionsDatasource = DsBuilder.create()
                .setMetaClass(metaClass)
                .setViewName(View.MINIMAL)
                .buildCollectionDatasource();

        String query = "select e from " + metaClass.getName() + " e";

        if (!Strings.isNullOrEmpty(joinClause)) {
            query += " " + joinClause;
        }
        if (!Strings.isNullOrEmpty(whereClause)) {
            query += " where " + whereClause.replaceAll("\\{E\\}", "e");
        }

        optionsDatasource.setQuery(query);
        optionsDatasource.refresh();
        return optionsDatasource;
    }

    /**
     * Creates the lookup action that will open the lookup screen with the dynamic filter applied. This filter contains
     * a condition with join and where clauses
     */
    public PickerField.LookupAction createLookupAction(PickerField pickerField,
                                                       String joinClause,
                                                       String whereClause) {
        FilteringLookupAction filteringLookupAction = new FilteringLookupAction(pickerField, joinClause, whereClause);
        Map<String, Object> params = new HashMap<>();
        WindowParams.DISABLE_RESUME_SUSPENDED.set(params, true);
        WindowParams.DISABLE_AUTO_REFRESH.set(params, true);
        filteringLookupAction.setLookupScreenParams(params);
        return filteringLookupAction;
    }

    /**
     * Reload dynamic attributes on the entity
     */
    @SuppressWarnings("unchecked")
    public void reloadDynamicAttributes(BaseGenericIdEntity entity) {
        MetaClass metaClass = entity.getMetaClass();
        View view = new View(metaClass.getJavaClass(), false)
                .addProperty(metadata.getTools().getPrimaryKeyName(metaClass));
        LoadContext loadContext = new LoadContext(metaClass)
                .setView(view)
                .setLoadDynamicAttributes(true)
                .setId(entity.getId());
        BaseGenericIdEntity reloadedEntity = (BaseGenericIdEntity) dataManager.load(loadContext);
        if (reloadedEntity != null) {
            entity.setDynamicAttributes(reloadedEntity.getDynamicAttributes());
        }
    }
}