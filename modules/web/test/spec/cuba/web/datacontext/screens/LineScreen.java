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

package spec.cuba.web.datacontext.screens;

import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataComponents;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.web.testmodel.sales.OrderLine;
import com.haulmont.cuba.web.testmodel.sales.OrderLineParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@UiController
@UiDescriptor("line-screen.xml")
@EditedEntityContainer("lineDc")
public class LineScreen extends StandardEditor<OrderLine> {

    private static final Logger log = LoggerFactory.getLogger(LineScreen.class);

    @Inject
    private TextField<Integer> qtyField;

    @Inject
    private DataComponents dataComponents;

    @Inject
    public Table<OrderLineParam> paramsTable;

    @Inject
    private DataContext dataContext;
    @Inject
    private InstanceContainer<OrderLine> lineDc;
    @Inject
    public CollectionPropertyContainer<OrderLineParam> paramsDc;

    @Subscribe
    protected void onInit(InitEvent event) {
        log.debug("onInit: dataContext={}", getScreenData().getDataContext());
    }

    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {
        OrderLine mergedOrderLine = dataContext.merge(getEditedEntity());
        lineDc.setItem(mergedOrderLine);
    }

    public void changeCommitAndClose(int quantity) {
        qtyField.setValue(quantity);
        closeWithCommit();
    }
}
