/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.gui.facets;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.ActionsAwareDialogFacet.DialogAction;
import com.haulmont.cuba.gui.components.InputDialogFacet;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.FacetProvider;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.web.gui.components.WebInputDialogFacet;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.haulmont.cuba.gui.icons.Icons.ICON_NAME_REGEX;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component(InputDialogFacetProvider.NAME)
public class InputDialogFacetProvider implements FacetProvider<InputDialogFacet> {

    public static final String NAME = "cuba_InputDialogFacetProvider";

    protected static final Pattern PARAM_TYPE_REGEX = Pattern.compile("^(\\w+)Parameter$");

    @Inject
    protected MessageTools messageTools;
    @Inject
    protected Metadata metadata;
    @Inject
    protected DatatypeRegistry datatypeRegistry;
    @Inject
    protected Icons icons;
    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    @Override
    public Class<InputDialogFacet> getFacetClass() {
        return InputDialogFacet.class;
    }

    @Override
    public InputDialogFacet create() {
        return new WebInputDialogFacet();
    }

    @Override
    public String getFacetTag() {
        return "inputDialog";
    }

    @Override
    public void loadFromXml(InputDialogFacet facet, Element element,
                            ComponentLoader.ComponentContext context) {
        loadId(facet, element);
        loadCaption(facet, element, context);

        loadWidth(facet, element);
        loadHeight(facet, element);

        loadTarget(facet, element, context);

        loadInputParameters(facet, element, context);
        loadDialogActions(facet, element, context);
    }

    protected void loadId(InputDialogFacet facet, Element element) {
        String id = element.attributeValue("id");
        if (isNotEmpty(id)) {
            facet.setId(id);
        }
    }

    protected void loadCaption(InputDialogFacet facet, Element element,
                               ComponentLoader.ComponentContext context) {
        String caption = element.attributeValue("caption");
        if (isNotEmpty(caption)) {
            facet.setCaption(loadResourceString(context, caption));
        }
    }

    protected void loadWidth(InputDialogFacet facet, Element element) {
        String width = element.attributeValue("width");
        if (isNotEmpty(width)) {
            facet.setWidth(width);
        }
    }

    protected void loadHeight(InputDialogFacet facet, Element element) {
        String height = element.attributeValue("height");
        if (isNotEmpty(height)) {
            facet.setHeight(height);
        }
    }

    protected void loadTarget(InputDialogFacet facet, Element element,
                              ComponentLoader.ComponentContext context) {
        String actionTarget = element.attributeValue("onAction");
        String buttonTarget = element.attributeValue("onButton");

        if (isNotEmpty(actionTarget)
                && isNotEmpty(buttonTarget)) {
            throw new GuiDevelopmentException(
                    "InputDialog facet should have either action or button target",
                    context);
        }

        if (isNotEmpty(actionTarget)) {
            facet.setActionTarget(actionTarget);
        } else if (isNotEmpty(buttonTarget)) {
            facet.setButtonTarget(buttonTarget);
        }
    }

    protected void loadDialogActions(InputDialogFacet facet, Element element,
                                     ComponentLoader.ComponentContext context) {
        loadDefaultActions(facet, element);

        Element actions = element.element("actions");
        if (actions != null) {
            if (facet.getDialogActions() == null) {
                loadActions(facet, element, context);
            } else {
                throw new GuiDevelopmentException(
                        "Predefined and custom actions cannot be used for InputDialog at the same time",
                        context);
            }
        }
    }

    protected void loadActions(InputDialogFacet facet, Element element,
                               ComponentLoader.ComponentContext context) {
        Element actionsEl = element.element("actions");
        if (actionsEl == null) {
            return;
        }

        List<Element> actionElements = actionsEl.elements("action");

        List<DialogAction<InputDialogFacet>> actions = new ArrayList<>(actionElements.size());
        for (Element actionElement : actionElements) {
            actions.add(loadAction(actionElement, context));
        }

        facet.setActions(actions);
    }

    protected DialogAction<InputDialogFacet> loadAction(Element element,
                                                                         ComponentLoader.ComponentContext context) {
        String id = element.attributeValue("id");
        String caption = loadResourceString(context, element.attributeValue("caption"));
        String description = loadResourceString(context, element.attributeValue("description"));
        String icon = getIconPath(context, element.attributeValue("icon"));
        boolean primary = Boolean.parseBoolean(element.attributeValue("primary"));

        return new DialogAction<>(id, caption, description, icon, primary);
    }

    protected void loadDefaultActions(InputDialogFacet facet, Element element) {
        String actions = element.attributeValue("defaultActions");
        if (isNotEmpty(actions)) {
            facet.setDialogActions(DialogActions.valueOf(actions));
        }
    }

    protected void loadInputParameters(InputDialogFacet facet, Element element,
                                       ComponentLoader.ComponentContext context) {
        List<InputParameter> inputParameters = new ArrayList<>();
        Set<String> paramIds = new HashSet<>();

        Element paramsEl = element.element("parameters");
        if (paramsEl == null) {
            return;
        }

        for (Element paramEl : paramsEl.elements()) {
            String paramId = paramEl.attributeValue("id");

            if (!paramIds.contains(paramId)) {
                inputParameters.add(loadInputParameter(paramEl, context));
                paramIds.add(paramId);
            } else {
                throw new GuiDevelopmentException("InputDialog parameters should have unique ids", context);
            }
        }

        if (inputParameters.isEmpty()) {
            throw new GuiDevelopmentException("InputDialog Facet cannot be used without parameters", context);
        }

        facet.setParameters(inputParameters.toArray(new InputParameter[]{}));
    }

    protected InputParameter loadInputParameter(Element paramEl,
                                                ComponentLoader.ComponentContext context) {
        String paramName = paramEl.getName();
        if ("entityParameter".equals(paramName)) {
            return loadEntityParameter(paramEl, context);
        } else if ("enumParameter".equals(paramName)) {
            return loadEnumParameter(paramEl, context);
        } else if (PARAM_TYPE_REGEX.matcher(paramName).matches()) {
            return loadPrimitiveParameter(paramEl, context);
        } else {
            throw new GuiDevelopmentException(
                    String.format("Unsupported type '%s' of InputDialog parameter '%s'",
                            paramName, paramEl.attributeValue("id")),
                    context);
        }
    }

    protected InputParameter loadPrimitiveParameter(Element paramEl,
                                                    ComponentLoader.ComponentContext context) {
        String paramId = paramEl.attributeValue("id");
        String paramName = paramEl.getName();

        InputParameter inputParameter;

        if ("bigDecimalParameter".equals(paramName)) {
            // Handle BigDecimal explicitly because its datatype id doesn't match with pattern "typeParameter"
            inputParameter = InputParameter.bigDecimalParameter(paramId)
                    .withCaption(loadParamCaption(paramEl, context))
                    .withRequired(loadParamRequired(paramEl))
                    .withDefaultValue(
                            loadDefaultValue(paramEl, datatypeRegistry.get(BigDecimal.class), context));
        } else {
            Datatype datatype = loadDatatype(paramEl, context);

            inputParameter = InputParameter.parameter(paramId)
                    .withCaption(loadParamCaption(paramEl, context))
                    .withRequired(loadParamRequired(paramEl))
                    .withDatatype(datatype)
                    .withDefaultValue(
                            loadDefaultValue(paramEl, datatype, context));
        }

        return inputParameter;
    }

    @SuppressWarnings("unchecked")
    protected InputParameter loadEntityParameter(Element paramEl,
                                                 ComponentLoader.ComponentContext context) {
        String paramId = paramEl.attributeValue("id");
        String classFqn = paramEl.attributeValue("entityClass");

        InputParameter parameter;

        Class clazz = loadParamClass(paramEl, classFqn, context);
        MetaClass entityClass = metadata.getClass(clazz);

        if (entityClass != null) {
            parameter = InputParameter.entityParameter(paramId, clazz)
                    .withCaption(loadParamCaption(paramEl, context))
                    .withRequired(loadParamRequired(paramEl));
        } else {
            throw new GuiDevelopmentException(
                    String.format(
                            "Unable to create InputDialog parameter '%s'. Class '%s' is not entity class",
                            paramId, classFqn),
                    context);
        }

        return parameter;
    }

    @SuppressWarnings("unchecked")
    protected InputParameter loadEnumParameter(Element paramEl,
                                               ComponentLoader.ComponentContext context) {
        String paramId = paramEl.attributeValue("id");
        String classFqn = paramEl.attributeValue("enumClass");

        InputParameter parameter;

        Class clazz = loadParamClass(paramEl, classFqn, context);

        if (EnumClass.class.isAssignableFrom(clazz)) {
            parameter = InputParameter.enumParameter(paramId, clazz)
                    .withCaption(loadParamCaption(paramEl, context))
                    .withRequired(loadParamRequired(paramEl));
        } else {
            throw new GuiDevelopmentException(
                    String.format(
                            "Unable to create InputDialog parameter '%s'. Class '%s' is not enum class",
                            paramId, classFqn),
                    context);
        }

        return parameter;
    }

    protected Datatype loadDatatype(Element element,
                                    ComponentLoader.ComponentContext context) {
        String paramName = element.getName();

        Matcher matcher = PARAM_TYPE_REGEX.matcher(paramName);
        if (matcher.matches()) {
            String typeName = matcher.group(1);
            return datatypeRegistry.get(typeName);
        } else {
            throw new GuiDevelopmentException(
                    String.format("Unsupported InputDialog parameter type: '%s'", paramName),
                    context);
        }
    }

    @Nullable
    protected String loadParamCaption(Element paramEl,
                                      ComponentLoader.ComponentContext context) {
        String caption = paramEl.attributeValue("caption");
        if (isNotEmpty(caption)) {
            return loadResourceString(context, caption);
        }
        return null;
    }

    protected boolean loadParamRequired(Element paramEl) {
        String required = paramEl.attributeValue("required");
        if (isNotEmpty(required)) {
            return Boolean.parseBoolean(required);
        }
        return false;
    }

    @Nullable
    protected Object loadDefaultValue(Element paramEl, Datatype datatype,
                                      ComponentLoader.ComponentContext context) {
        String defaultValue = paramEl.attributeValue("defaultValue");
        if (isNotEmpty(defaultValue)) {
            try {
                return datatype.parse(defaultValue);
            } catch (ParseException e) {
                throw new GuiDevelopmentException(
                        String.format("Unable to parse default value '%s' as '%s' for InputDialog parameter '%s'",
                                defaultValue, datatype, paramEl.attributeValue("id")),
                        context);
            }
        }
        return null;
    }

    protected Class loadParamClass(Element paramEl, String classFqn,
                                   ComponentLoader.ComponentContext context) {
        try {
            return ReflectionHelper.loadClass(classFqn);
        } catch (ClassNotFoundException e) {
            throw new GuiDevelopmentException(
                    String.format(
                            "Unable to create InputDialog parameter '%s'. Class '%s' not found",
                            paramEl.attributeValue("id"), classFqn),
                    context);
        }
    }

    protected String loadResourceString(ComponentLoader.ComponentContext context, String caption) {
        if (isEmpty(caption)) {
            return caption;
        }

        Class screenClass = context.getFrame()
                .getFrameOwner()
                .getClass();

        return messageTools.loadString(screenClass.getPackage().getName(), caption);
    }

    protected String getIconPath(ComponentLoader.ComponentContext context, String icon) {
        if (icon == null || icon.isEmpty()) {
            return null;
        }

        String iconPath = null;

        if (ICON_NAME_REGEX.matcher(icon).matches()) {
            iconPath = icons.get(icon);
        }

        if (isEmpty(iconPath)) {
            String themeValue = loadThemeString(icon);
            iconPath = loadResourceString(context, themeValue);
        }

        return iconPath;
    }

    protected String loadThemeString(String value) {
        if (value != null && value.startsWith(ThemeConstants.PREFIX)) {
            value = themeConstantsManager.getConstants()
                    .get(value.substring(ThemeConstants.PREFIX.length()));
        }
        return value;
    }
}
