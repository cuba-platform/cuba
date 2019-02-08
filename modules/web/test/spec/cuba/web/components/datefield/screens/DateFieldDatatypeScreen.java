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

package spec.cuba.web.components.datefield.screens;

import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

@UiController
@UiDescriptor("datefield-datatype-screen.xml")
public class DateFieldDatatypeScreen extends Screen {
    @Inject
    private DateField<java.sql.Date> dateField;
    @Inject
    private DateField<Date> dateTimeField;
    @Inject
    private DateField<LocalDate> localDateField;
    @Inject
    private DateField<LocalDateTime> localDateTimeField;
    @Inject
    private DateField<OffsetDateTime> offsetDateTimeField;

    @Subscribe
    protected void onInit(InitEvent event) {
        Date now = new Date();
        dateField.setValue(new java.sql.Date(now.getTime()));
        dateTimeField.setValue(now);
        localDateField.setValue(LocalDate.now());
        localDateTimeField.setValue(LocalDateTime.now());
        offsetDateTimeField.setValue(OffsetDateTime.now());
    }
}
