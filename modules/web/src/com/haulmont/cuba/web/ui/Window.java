/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 19:02:39
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.BasicService;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.components.ComponentsHelper;
import com.itmill.toolkit.ui.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;

public class Window implements com.haulmont.cuba.gui.components.Window, Component.Wrapper, Component.HasXmlDescriptor
{
    private String id;

    private Map<String, Component> componentByIds = new HashMap<String, Component>();
    private ResourceBundle resourceBundle;

    protected com.itmill.toolkit.ui.Component component;
    private Element element;

    private DsContext dsContext;
    private String caption;

    public Window() {
        component = createLayout();
    }

    protected com.itmill.toolkit.ui.Component createLayout() {
        ExpandLayout layout = new ExpandLayout(ExpandLayout.ORIENTATION_VERTICAL);
        layout.setMargin(true);
        layout.setSpacing(true);

        return layout;
    }

    protected ComponentContainer getContainer() {
        return (ComponentContainer) component;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public <T extends com.haulmont.cuba.gui.components.Window> T openWindow(String descriptor, WindowManager.OpenType openType, Map<String, Object> params) {
        return App.getInstance().getScreenManager().<T>openWindow(descriptor, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openWindow(Class aclass, WindowManager.OpenType openType, Map<String, Object> params) {
        return App.getInstance().getScreenManager().<T>openWindow(aclass, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openWindow(String descriptor, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openWindow(descriptor, openType);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openWindow(Class aclass, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openWindow(aclass, openType);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String descriptor, Object item, WindowManager.OpenType openType, Map<String, Object> params) {
        return App.getInstance().getScreenManager().<T>openEditor(descriptor, item, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(Class aclass, Object item, WindowManager.OpenType openType, Map<String, Object> params) {
        return App.getInstance().getScreenManager().<T>openEditor(aclass, item, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String descriptor, Object item, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openEditor(descriptor, item, openType);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(Class aclass, Object item, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openEditor(aclass, item, openType);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openLookup(String descriptor, com.haulmont.cuba.gui.components.Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        return App.getInstance().getScreenManager().<T>openLookup(descriptor, handler, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openLookup(Class aclass, com.haulmont.cuba.gui.components.Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        return App.getInstance().getScreenManager().<T>openLookup(aclass, handler, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openLookup(String descriptor, com.haulmont.cuba.gui.components.Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openLookup(descriptor, handler, openType);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openLookup(Class aclass, com.haulmont.cuba.gui.components.Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openLookup(aclass, handler, openType);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public DsContext getDsContext() {
        return dsContext;
    }

    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    public Element getXmlDescriptor() {
        return element;
    }

    public void setXmlDescriptor(Element element) {
        this.element = element;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void add(Component component) {
        getContainer().addComponent(ComponentsHelper.unwrap(component));
        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }
    }

    public void remove(Component component) {
        getContainer().removeComponent(ComponentsHelper.unwrap(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
    }

    public boolean onClose() {
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void requestFocus() {
    }

    public int getHeight() {
        return component.getHeight();
    }

    public int getHeightUnits() {
        return component.getHeightUnits();
    }

    public void setHeight(String height) {
        component.setHeight(height);
    }

    public int getWidth() {
        return component.getWidth();
    }

    public int getWidthUnits() {
        return component.getWidthUnits();
    }

    public void setWidth(String width) {
        component.setWidth(width);
    }

    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.<T>getComponent(this, id);
    }

    public int getVerticalAlignment() {
        return Layout.AlignmentHandler.ALIGNMENT_VERTICAL_CENTER;
    }

    public void setVerticalAlignment(int verticalAlIlignment) {}

    public int getHorizontalAlignment() {
        return Layout.AlignmentHandler.ALIGNMENT_HORIZONTAL_CENTER;
    }

    public void setHorizontalAlignment(int horizontalAlIlignment) {}

    public void expand(Component component, String height, String width) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T> T getComponent() {
        return (T) component;
    }

    public boolean close() {
        App.getInstance().getScreenManager().closeScreen();
        return true;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public static class Editor extends Window implements com.haulmont.cuba.gui.components.Window.Editor {
        protected Object item;
        private Form form;

        public Object getItem() {
            return item;
        }

        @Override
        protected com.itmill.toolkit.ui.Component createLayout() {
            ExpandLayout layout = new ExpandLayout();

            form = new Form();

            Layout okbar = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);
            okbar.setHeight("25px");

            okbar.addComponent(new Button("OK", this, "commit"));
            okbar.addComponent(new Button("Cancel", this, "close"));

            layout.addComponent(form);
            layout.addComponent(okbar);

            layout.expand(form);

            return layout;
        }

        @Override
        protected ComponentContainer getContainer() {
            return form.getLayout();
        }

        public void setItem(Object item) {
            this.item = item;

            final Element element = getXmlDescriptor();

            final String datasourceName = element.attributeValue("datasource");
            if (!StringUtils.isEmpty(datasourceName)) {
                final DsContext context = getDsContext();
                if (context != null) {
                    final Datasource ds = context.get(datasourceName);
                    if (ds != null) {
                        ds.setItem(item);
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
        }

        protected MetaClass getMetaClass(Object item) {
            final MetaClass metaClass;
            if (item instanceof Datasource) {
                metaClass = ((Datasource) item).getMetaClass();
            } else {
                metaClass = ((Instance) item).getMetaClass();
            }
            return metaClass;
        }

        protected Instance getInstance(Object item) {
            if (item instanceof Datasource) {
                return (Instance) ((Datasource) item).getItem();
            } else {
                return (Instance) item;
            }
        }

        public void commit() {
            form.commit();
            if (item instanceof Datasource) {
                final Datasource ds = (Datasource) item;
                ds.commit();
            } else {
                BasicService service = Locator.lookupLocal(BasicService.JNDI_NAME);
                service.update((BaseEntity) item);
            }
            close();
        }
    }

    public static class Lookup extends Window implements com.haulmont.cuba.gui.components.Window.Lookup {
        private Handler handler;

        private Component lookupComponent;
        private ExpandLayout contaiter;

        public com.haulmont.cuba.gui.components.Component getLookupComponent() {
            return lookupComponent;
        }

        public void setLookupComponent(Component lookupComponent) {
            this.lookupComponent = lookupComponent;
        }

        public Handler getLookupHandler() {
            return handler;
        }

        public void setLookupHandler(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected ComponentContainer getContainer() {
            return contaiter;
        }

        @Override
        protected com.itmill.toolkit.ui.Component createLayout() {
            final ExpandLayout form = new ExpandLayout();

            contaiter = new ExpandLayout();

            OrderedLayout okbar = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);
            okbar.setHeight("25px");

            final Button selectButton = new Button("Select");
            selectButton.addListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    final com.haulmont.cuba.gui.components.Component lookupComponent = getLookupComponent();

                    if (lookupComponent instanceof com.haulmont.cuba.gui.components.Table ) {
                        final Set selected = ((com.haulmont.cuba.gui.components.Table) lookupComponent).getSelected();
                        handler.handleLookup(selected);
                    } else if (lookupComponent instanceof com.haulmont.cuba.gui.components.Tree) {
                        final Object selected = ((com.haulmont.cuba.gui.components.Tree) lookupComponent).getSelected();
                        handler.handleLookup(Collections.singleton(selected));
                    } else if (lookupComponent instanceof LookupField) {
                        final Object value = ((LookupField) lookupComponent).getValue();
                        handler.handleLookup(Collections.singleton(value));
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }
            });

            final Button cancelButton = new Button("Cancel", this, "close");

            okbar.addComponent(selectButton);
            okbar.addComponent(cancelButton);

            form.addComponent(contaiter);
            form.addComponent(okbar);

            form.expand(contaiter);

            return form;
        }
    }
}
