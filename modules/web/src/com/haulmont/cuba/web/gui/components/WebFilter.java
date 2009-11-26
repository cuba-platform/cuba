/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.10.2009 14:03:58
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.filter.QueryFilter;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import com.haulmont.cuba.web.gui.components.filter.*;
import com.haulmont.cuba.security.entity.User;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.CheckBox;
import org.dom4j.Element;
import org.dom4j.Attribute;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import static org.apache.commons.lang.BooleanUtils.isTrue;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebFilter
        extends WebAbstractComponent<VerticalLayout> implements Filter
{
    private static final String MESSAGES_PACK = "com.haulmont.cuba.web.gui.components.filter";

    private CollectionDatasource datasource;
    private QueryFilter dsQueryFilter;
    private FilterEntity filterEntity;
    private List<Condition> conditions = Collections.EMPTY_LIST;

    private AbstractLayout paramsLayout;
    private AbstractOrderedLayout editLayout;
    private AbstractSelect select;
    private Button createBtn;
    private Button editBtn;
    private Button deleteBtn;
    private Button applyBtn;
    private CheckBox defaultCb;

    private boolean changingFilter;
    private boolean editing;

    public WebFilter() {
        component = new VerticalLayout();
        component.setMargin(true);

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);

        select = new Select();
        select.setWidth("300px");
        select.setNullSelectionAllowed(false);
        select.setImmediate(true);
        select.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (changingFilter)
                    return;

                filterEntity = (FilterEntity) select.getValue();
                parseFilterXml();

                updateButtons();
                component.removeComponent(paramsLayout);
                createParamsLayout();
                component.addComponent(paramsLayout);
            }
        });
        topLayout.addComponent(select);

        applyBtn = WebComponentsHelper.createButton("icons/search.png");
        applyBtn.setCaption(MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "actions.Apply"));
        applyBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                apply();
            }
        });
        topLayout.addComponent(applyBtn);

        createBtn = WebComponentsHelper.createButton("icons/create.png");
        createBtn.setCaption(MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "actions.Create"));
        createBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                createFilterEntity();
                parseFilterXml();
                switchToEdit();
            }
        });
        topLayout.addComponent(createBtn);

        editBtn = WebComponentsHelper.createButton("icons/edit.png");
        editBtn.setCaption(MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "actions.Edit"));
        editBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                switchToEdit();
            }
        });
        topLayout.addComponent(editBtn);

        deleteBtn = WebComponentsHelper.createButton("icons/remove.png");
        deleteBtn.setCaption(MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "actions.Remove"));
        deleteBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getFrame().showOptionDialog(
                        MessageProvider.getMessage(MESSAGES_PACK, "deleteDlg.title"),
                        MessageProvider.getMessage(MESSAGES_PACK, "deleteDlg.msg"),
                        IFrame.MessageType.CONFIRMATION,
                        new Action[] {
                                new DialogAction(DialogAction.Type.YES) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        deleteFilterEntity();
                                        select.removeItem(select.getValue());
                                        if (!select.getItemIds().isEmpty()) {
                                            select.select(select.getItemIds().iterator().next());
                                        } else {
                                            select.select(null);
                                        }
                                    }
                                },
                                new DialogAction(DialogAction.Type.NO)
                        }
                );
            }
        });
        topLayout.addComponent(deleteBtn);

        defaultCb = new CheckBox();
        defaultCb.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "defaultCb"));
        defaultCb.setImmediate(true);

        defaultCb.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                if (filterEntity != null) {
                    filterEntity.setIsDefault(isTrue((Boolean) defaultCb.getValue()));

                    Collection<FilterEntity> filters = select.getItemIds();
                    for (FilterEntity filter : filters) {
                        if (!filter.equals(filterEntity))
                            filter.setIsDefault(false);
                    }
                }

            }
        });
        topLayout.addComponent(defaultCb);

        component.addComponent(topLayout);

        createParamsLayout();
        component.addComponent(paramsLayout);

        updateButtons();
    }

    private void apply() {
        Element element = Dom4j.readDocument(filterEntity.getXml()).getRootElement();
        QueryFilter queryFilter = new QueryFilter(element, datasource.getMetaClass().getName());

        if (dsQueryFilter != null) {
            queryFilter = new QueryFilter(dsQueryFilter, queryFilter);
        }

        datasource.setQueryFilter(queryFilter);
        datasource.refresh();
    }

    private void createFilterEntity() {
        filterEntity = new FilterEntity();

        filterEntity.setComponentId(getComponentPath());
        filterEntity.setName(MessageProvider.getMessage(MESSAGES_PACK, "newFilterName"));
        filterEntity.setUser(UserSessionClient.getUserSession().getUser());
//        filterEntity.setXml(
//                "<filter><and>\n" +
//                        "<c type=\"PROPERTY\" name=\"login\">u.login like :component$usersFilter.login\n" +
//                            "<param name=\"component$users-filter.login\" class=\"java.lang.String\">aaa</param>\n" +
//                        "</c>\n" +
//                        "<c type=\"CUSTOM\" name=\"group\" join=\"left join u.group.hierarchyList h\">\n" +
//                            "(h.parent.id = :component$usersFilter.group or h.group.id = :component$usersFilter.group)" +
//                            " or (u.group.id = :component$usersFilter.group and h.parent is null)\n" +
//                            "<param name=\"component$users-filter.group\" class=\"com.haulmont.cuba.security.entity.Group\"></param>\n" +
//                        "</c>\n" +
//                "</and></filter>"
//        );
    }

    private String getComponentPath() {
        StringBuilder sb = new StringBuilder(getId());
        IFrame frame = getFrame();
        while (frame != null) {
            sb.insert(0, ".");
            String s = frame.getId();
            if (s.contains("."))
                s = "[" + s + "]";
            sb.insert(0, s);
            if (frame instanceof Window)
                break;
            frame = frame.getFrame();
        }
        return sb.toString();
    }

    private void createParamsLayout() {
        if (conditions.isEmpty()) {
            paramsLayout = new HorizontalLayout();
            return;
        }

        int columns = 3;
        int rows = conditions.size() / columns;
        if (conditions.size() % columns != 0)
            rows++;
        com.vaadin.ui.GridLayout grid = new com.vaadin.ui.GridLayout(columns, rows);
        grid.setMargin(true, false, false, false);

        for (int i = 0; i < conditions.size(); i++) {
            Condition condition = conditions.get(i);
            HorizontalLayout paramLayout = new HorizontalLayout();
            paramLayout.setSpacing(true);
            paramLayout.setMargin(false, true, true, false);
            if (condition.getParam().getJavaClass() != null) {
                Label label = new Label(condition.getLocCaption());
                paramLayout.addComponent(label);

                ParamEditor paramEditor = new ParamEditor(condition, true);
                paramLayout.addComponent(paramEditor);
            }
            grid.addComponent(paramLayout, i % columns, i / columns);
            grid.setComponentAlignment(paramLayout, com.vaadin.ui.Alignment.MIDDLE_RIGHT);
        }

        paramsLayout = grid;
    }

    public void editorCommitted() {
        changingFilter = true;
        try {
            parseFilterXml();

            select.addItem(filterEntity);
            select.setItemCaption(filterEntity, InstanceUtils.getInstanceName((Instance) filterEntity));
            select.setValue(filterEntity);

            switchToUse();
            saveFilterEntity();
        } finally {
            changingFilter = false;
        }
    }

    public void editorCancelled() {
        if (filterEntity.getXml() == null)
            filterEntity = null;
        
        switchToUse();
    }

    private void loadFilterEntities() {
        DataService ds = ServiceLocator.getDataService();
        LoadContext ctx = new LoadContext(FilterEntity.class);
        ctx.setView("app");

        User user = UserSessionClient.getUserSession().getSubstitutedUser();
        if (user == null)
            user = UserSessionClient.getUserSession().getUser();

        ctx.setQueryString("select f from sec$Filter f " +
                "where f.componentId = :component and (f.user is null or f.user.id = :userId)")
                .addParameter("component", getComponentPath())
                .addParameter("userId", user.getId());

        List<FilterEntity> filters = ds.loadList(ctx);
        for (FilterEntity filter : filters) {
            select.addItem(filter);
            select.setItemCaption(filter, filter.getName());
        }
    }

    private void saveFilterEntity() {
        DataService ds = ServiceLocator.getDataService();
        CommitContext ctx = new CommitContext(Collections.singletonList(filterEntity));
        ds.commit(ctx);
    }

    private void deleteFilterEntity() {
        DataService ds = ServiceLocator.getDataService();
        CommitContext ctx = new CommitContext();
        ctx.setRemoveInstances(Collections.singletonList(filterEntity));
        ds.commit(ctx);
        filterEntity = null;
    }

    private void createEditLayout() {
        editLayout = new VerticalLayout();
        editLayout.setSpacing(true);

        final FilterEditor editor = new FilterEditor(this, filterEntity, getXmlDescriptor());
        editLayout.addComponent(editor.getLayout());
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        this.dsQueryFilter = datasource.getQueryFilter();
    }

    private void switchToUse() {
        editing = false;
        updateButtons();
        component.removeComponent(editLayout);
        createParamsLayout();
        component.addComponent(paramsLayout);
    }

    private void switchToEdit() {
        editing = true;
        updateButtons();
        component.removeComponent(paramsLayout);
        createEditLayout();
        component.addComponent(editLayout);
    }

    private void updateButtons() {
        select.setEnabled(!editing);
        createBtn.setEnabled(!editing);
        applyBtn.setEnabled(filterEntity != null && !editing);
        editBtn.setEnabled(filterEntity != null && !editing && checkGlobalFilterPermission());
        deleteBtn.setEnabled(filterEntity != null && !editing && checkGlobalFilterPermission());
        defaultCb.setEnabled(filterEntity != null && !editing);

        if (filterEntity != null && !editing)
            defaultCb.setValue(isTrue(filterEntity.getIsDefault()));
    }

    private boolean checkGlobalFilterPermission() {
        if (filterEntity == null || filterEntity.getUser() != null)
            return true;
        else
            return UserSessionClient.getUserSession().isSpecificPermitted("cuba.gui.filter.global");
    }

    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    public <T extends Component> T getOwnComponent(String id) {
        for (Condition condition : conditions) {
            if (condition.getParam() != null) {
                String paramName = condition.getParam().getName();
                String componentName = paramName.substring(paramName.indexOf('.') + 1);
                if (id.equals(componentName)) {
                    ParamWrapper wrapper = new ParamWrapper(condition);
                    return (T) wrapper;
                }
            }
        }
        return null;
    }

    public <T extends Component> T getComponent(String id) {
        String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1)
            return (T)getOwnComponent(id);
        else
            throw new UnsupportedOperationException("Filter contains only one level of subcomponents");
    }

    public Collection<Component> getOwnComponents() {
        return Collections.EMPTY_LIST;
    }

    public Collection<Component> getComponents() {
        return getOwnComponents();
    }

    private void parseFilterXml() {
        if (filterEntity == null) {
            conditions = new ArrayList<Condition>();
        } else {
            FilterParser parser =
                    new FilterParser(filterEntity.getXml(), getFrame().getMessagesPack(), getId());
            conditions = parser.fromXml().getConditions();
        }
    }

    public void applySettings(Element element) {
        loadFilterEntities();

        Element e = element.element("defaultFilter");
        if (e != null) {
            String defIdStr =  e.attributeValue("id");
            if (!StringUtils.isBlank(defIdStr)) {
                UUID defaultId = null;
                try {
                    defaultId = UUID.fromString(defIdStr);
                } catch (IllegalArgumentException ex) {
                    //
                }
                if (defaultId != null) {
                    Collection<FilterEntity> filters = select.getItemIds();
                    for (FilterEntity filter : filters) {
                        if (defaultId.equals(filter.getId())) {
                            filter.setIsDefault(true);
                            select.setValue(filter);
                            updateButtons();
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean saveSettings(Element element) {
        Element e = element.element("defaultFilter");
        if (e == null)
            e = element.addElement("defaultFilter");

        UUID defaultId = null;
        Collection<FilterEntity> filters = select.getItemIds();
        for (FilterEntity filter : filters) {
            if (isTrue(filter.getIsDefault())) {
                defaultId = filter.getId();
                break;
            }
        }
        String newDef = defaultId != null ? defaultId.toString() : null;
        Attribute attr = e.attribute("id");
        String oldDef = attr != null ? attr.getValue() : null;
        if (!ObjectUtils.equals(oldDef, newDef)) {
            if (newDef == null && attr != null) {
                e.remove(attr);
            } else {
                if (attr == null)
                    e.addAttribute("id", newDef);
                else
                    attr.setValue(newDef);
            }
            return true;
        }
        return false;
    }

    public static final Pattern LIKE_PATTERN = Pattern.compile("\\slike\\s+" + ParametersHelper.QUERY_PARAMETERS_RE);

    private static class ParamWrapper implements HasValue {

        private final Condition condition;

        private ParamWrapper(Condition condition) {
            this.condition = condition;
        }

        public <T> T getValue() {
            Object value = condition.getParam().getValue();
            if (value instanceof String
                    && !StringUtils.isEmpty((String) value)
                    && !((String) value).contains("%")
                    && !((String) value).startsWith(ParametersHelper.CASE_INSENSITIVE_MARKER))
            {
                // try to wrap value for case-insensitive "like" search
                if (condition instanceof PropertyCondition) {
                    PropertyCondition.Op op = ((PropertyCondition) condition).getOperator();
                    if (PropertyCondition.Op.CONTAINS.equals(op) || op.equals(PropertyCondition.Op.DOES_NOT_CONTAIN)) {
                        value = wrapValueForLike(value);
                    }
                } else if (condition instanceof CustomCondition) {
                    String where = ((CustomCondition) condition).getWhere();
                    Matcher matcher = LIKE_PATTERN.matcher(where);
                    if (matcher.find()) {
                        value = wrapValueForLike(value);
                    }
                }
            } else if (value instanceof EnumClass) {
                value = ((EnumClass) value).getId();
            }
            return (T) value;
        }

        private String wrapValueForLike(Object value) {
            return ParametersHelper.CASE_INSENSITIVE_MARKER + "%" + value + "%";
        }

        public void setValue(Object value) {
        }

        public void addListener(ValueListener listener) {
        }

        public void removeListener(ValueListener listener) {
        }

        public boolean isEditable() {
            return false;
        }

        public void setEditable(boolean editable) {
        }

        public String getId() {
            return condition.getName();
        }

        public void setId(String id) {
        }

        public String getDebugId() {
            return null;
        }

        public void setDebugId(String id) {
        }

        public boolean isEnabled() {
            return false;
        }

        public void setEnabled(boolean enabled) {
        }

        public boolean isVisible() {
            return false;
        }

        public void setVisible(boolean visible) {
        }

        public void requestFocus() {
        }

        public float getHeight() {
            return 0;
        }

        public int getHeightUnits() {
            return 0;
        }

        public void setHeight(String height) {
        }

        public float getWidth() {
            return 0;
        }

        public int getWidthUnits() {
            return 0;
        }

        public void setWidth(String width) {
        }

        public Alignment getAlignment() {
            return Alignment.TOP_LEFT;
        }

        public void setAlignment(Alignment alignment) {
        }

        public String getStyleName() {
            return null;
        }

        public void setStyleName(String name) {
        }

        public <A extends IFrame> A getFrame() {
            return null;
        }

        public void setFrame(IFrame frame) {
        }
    }
}
