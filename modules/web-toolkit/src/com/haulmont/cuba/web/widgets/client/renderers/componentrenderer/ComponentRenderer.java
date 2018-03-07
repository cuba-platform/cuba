/*
 * Licensed under the Apache License,Version2.0(the"License");you may not
 * use this file except in compliance with the License.You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.haulmont.cuba.web.widgets.client.renderers.componentrenderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.renderers.WidgetRenderer;
import com.vaadin.client.widget.grid.RendererCellReference;

/**
 * A renderer for vaadin components.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends WidgetRenderer<ComponentConnector, SimplePanel> {

    @Override
    public SimplePanel createWidget() {
        final SimplePanel panel = GWT.create(SimplePanel.class);
        panel.getElement().addClassName("cr-component-cell");
        panel.sinkEvents(com.google.gwt.user.client.Event.ONCLICK);
        return panel;
    }

    @Override
    public void render(RendererCellReference rendererCellReference, final ComponentConnector componentConnector,
                       final SimplePanel panel) {
        if (componentConnector != null) {

            // render chart widgets deferred so measurements are correct. Do not render
            // normal component widgets deferred as it causes some flicker when rerendering the grid
            if (componentConnector.getClass().getName().equals("com.vaadin.addon.charts.shared.ChartConnector")) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        panel.setWidget(componentConnector.getWidget());
                    }
                });
            } else {
                panel.setWidget(componentConnector.getWidget());
            }
        } else {
            panel.clear();
        }
    }
}
