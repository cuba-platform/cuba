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

package spec.cuba.web.screens.injection;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Sort;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.screen.Install;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Target;
import com.haulmont.cuba.security.entity.User;

import java.util.Date;

public class ScreenBindInstall extends Screen {

    @Install(subject = "formatter", to = "label1")
    private String format(Date date) {
        return "formatted-date";
    }

    @Install(to = "usersTable", subject = "styleProvider")
    private String getStyleName(User user, String columnId) {
        return "awesome-style";
    }

    @Install(to = "usersTable.genColumn", type = Table.ColumnGenerator.class)
    protected Component tableColumnGenerator(User user) {
        return null;
    }

    @Install(type = Table.StyleProvider.class, to = "groupTable")
    public String getGroupStyle(User user, String columnId) {
        return "ok-style";
    }

    @Install(subject = "iconProvider", to = "tree")
    public String getIcon(User user) {
        return "ok.png";
    }

    // private is also supported
    @Install(subject = "validator", to = "textField1")
    private void validateText(String text) throws ValidationException {
        if (text == null || text.length() < 10) {
            throw new ValidationException("Incorrect length");
        }
    }

    @Install(to = "dataGrid", subject = "cellDescriptionProvider")
    protected String dataGridCellDescriptionProvider(User user, String columnId) {
        return "OK";
    }

    @Install(to = "dataGrid.name", subject = "editFieldGenerator")
    protected Field dataGridColumnEditorFieldProvider(Datasource datasource, String property) {
        return null;
    }

    @Install(to = "dataGrid.name", subject = "styleProvider")
    protected String dataGridCellStylenameProvider(Entity entity) {
        return "custom-stylename";
    }

    @Install(to = "dataGrid.name", subject = "descriptionProvider")
    protected String dataGridColumnCellDescriptionProvider(Entity entity) {
        return "test description";
    }

    @Install(to = "entitiesDc", target = Target.DATA_CONTAINER, subject = "sorter")
    protected void entitiesDcSorter(Sort sort) {}

}
