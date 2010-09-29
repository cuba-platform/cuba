/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 22.09.2010 18:39:00
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.presentations;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.presentations.PresentationsChangeListener;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.toolkit.gwt.client.presentations.TablePresentationsPopup;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebPopupButton;
import com.haulmont.cuba.web.toolkit.ui.MenuBar;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@ClientWidget(TablePresentationsPopup.class)
public class TablePresentations extends CustomComponent {
    private MenuBar menuBar;
    private WebPopupButton button;

    private Table table;
    
    private static final long serialVersionUID = -8633565024508836913L;

    public TablePresentations(Table component) {
        this.table = component;
        initLayout();
        setWidth("100%");
        setStyleName("table-presentations");

        setParent(WebComponentsHelper.unwrap(component));

        table.getPresentations().addListener(new PresentationsChangeListener() {
            public void currentPresentationChanged(Presentations presentations, Object oldPresentationId) {
                table.getPresentations().commit();
                build();
            }

            public void presentationsSetChanged(Presentations presentations) {
                build();
            }
        });

        build();
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        if (variables.containsKey("repaint")) {
            requestRepaint();
        }
    }

    private void initLayout() {
        VerticalLayout root = new VerticalLayout();
        setCompositionRoot(root);
        root.setSpacing(true);

        Label label = new Label(getMessage("PresentationsPopup.title"));
        label.setStyleName("title");
        label.setWidth("-1px");
        root.addComponent(label);

        menuBar = new MenuBar();
        menuBar.setStyleName("list");
        menuBar.setWidth("100%");
        menuBar.setVertical(true);
        root.addComponent(menuBar);

        button = new WebPopupButton();
        button.setCaption(getMessage("PresentationsPopup.actions"));
        root.addComponent(button.<Component>getComponent());
        root.setComponentAlignment(button.<Component>getComponent(), Alignment.MIDDLE_CENTER);

        root.setExpandRatio(menuBar, 1);
    }

    public void build() {
        button.setPopupVisible(false);
        buildPresentationsList();
        buildActions();
    }

    private void buildPresentationsList() {
        menuBar.removeItems();

        final Presentations p = table.getPresentations();

        for (final Object presId : p.getPresentationIds()) {
            final MenuBar.MenuItem item = menuBar.addItem(
                    buildItemCaption(p.getCaption(presId)),
                    new com.vaadin.ui.MenuBar.Command() {
                        public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                            table.applyPresentation(presId);
                        }
                    }
            );
            final Presentation current = p.getCurrent();
            if (current != null && presId.equals(current.getId())) {
                item.setStyleName("current");
            }
        }
    }

    private void buildActions() {
        final Collection<Action> actions = new ArrayList(button.getActions());
        for (final Action action : actions) {
            button.removeAction(action);
        }

        final Presentations p = table.getPresentations();
        final Presentation current = p.getCurrent();

        button.addAction(new AbstractAction(getMessage("PresentationsPopup.saveAs")) {
            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                Presentation presentation = new Presentation();
                presentation.setComponentId(ComponentsHelper.getComponentPath(table));

                openEditor(presentation);
            }
        });
        final boolean allowGlobalPresentations = UserSessionClient.getUserSession()
                .isSpecificPermitted("cuba.gui.presentations.global");
        if (current != null && (!p.isGlobal(current) || allowGlobalPresentations)) {
            button.addAction(new AbstractAction(getMessage("PresentationsPopup.save")) {
                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                    Element e = p.getSettings(current);
                    table.saveSettings(e);
                    p.setSettings(current, e);
                    p.commit();
                }
            });
            button.addAction(new AbstractAction(getMessage("PresentationsPopup.edit")) {
                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                    openEditor(current);
                }
            });
            button.addAction(new AbstractAction(getMessage("PresentationsPopup.delete")) {
                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                    p.remove(current);
                    p.commit();
                }
            });
        }
    }

    private void openEditor(Presentation presentation) {
        PresentationEditor window = new PresentationEditor(presentation, table);
        App.getInstance().getAppWindow().addWindow(window);
        window.center();
    }

    private static String buildItemCaption(String caption) {
        if (caption == null) return "";
        return caption;
    }

    private String getMessage(String key) {
        return MessageProvider.getMessage(getClass(), key);
    }
}
