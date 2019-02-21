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

package spec.cuba.web.screens.inspection;

import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.Screen;

public class ScreenWithProperties extends Screen {

    private int intProperty;
    private String stringProperty;
    private Table tableProperty;

    public void setIntProperty(int intProperty) {
        this.intProperty = intProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public void setTableProperty(Table tableProperty) {
        this.tableProperty = tableProperty;
    }

    private void setIntPropertyPrivate(int intProperty) {
        this.intProperty = intProperty;
    }

    protected void setStringPropertyProtected(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public void setTwoProperties(int intProperty, String stringProperty) {
        this.intProperty = intProperty;
        this.stringProperty = stringProperty;
    }

    public void setThreeProperties(int intProperty, String stringProperty, Table tableProperty) {
        this.intProperty = intProperty;
        this.stringProperty = stringProperty;
        this.tableProperty = tableProperty;
    }
}
