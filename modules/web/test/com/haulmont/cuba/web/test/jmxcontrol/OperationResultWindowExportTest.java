/*
 * Copyright (c) 2008-2026 Haulmont.
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

package com.haulmont.cuba.web.test.jmxcontrol;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.web.app.ui.jmxcontrol.inspect.operation.OperationResultWindow;
import com.haulmont.cuba.web.container.CubaTestContainer;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.annotation.Nullable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationResultWindowExportTest {

    @RegisterExtension
    public TestUiEnvironment environment = new TestUiEnvironment(CubaTestContainer.Common.INSTANCE);

    @Test
    public void exportFileNameUsesUnderscoresInTimestamp() {
        RecordingExportDisplay exportDisplay = new RecordingExportDisplay();
        OperationResultWindow window = new TestOperationResultWindow(exportDisplay, dateAt(12, 34, 56));

        window.exportToFile();

        assertEquals("jmx.TestBean-doIt-12_34_56.log", exportDisplay.resourceName);
    }

    @Test
    public void exportFileNameIsAcceptedByFilenameUtils() {
        // commons-io 2.7+ rejects ':' (NTFS ADS separator) in file names when running on Windows
        RecordingExportDisplay exportDisplay = new RecordingExportDisplay();
        OperationResultWindow window = new TestOperationResultWindow(exportDisplay, dateAt(12, 34, 56));

        window.exportToFile();

        assertEquals("log", FilenameUtils.getExtension(exportDisplay.resourceName));
    }

    protected static Date dateAt(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2026, Calendar.SEPTEMBER, 22, hour, minute, second);
        return calendar.getTime();
    }

    protected static class TestOperationResultWindow extends OperationResultWindow {
        public TestOperationResultWindow(ExportDisplay exportDisplay, Date timestamp) {
            this.exportDisplay = exportDisplay;
            this.timeSource = new FixedTimeSource(timestamp);
            this.beanName = "TestBean";
            this.methodName = "doIt";
            this.result = "42";
        }
    }

    protected static class FixedTimeSource implements TimeSource {
        private final Date timestamp;

        public FixedTimeSource(Date timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public Date currentTimestamp() {
            return timestamp;
        }

        @Override
        public long currentTimeMillis() {
            return timestamp.getTime();
        }

        @Override
        public ZonedDateTime now() {
            return timestamp.toInstant().atZone(ZoneId.systemDefault());
        }
    }

    protected static class RecordingExportDisplay implements ExportDisplay {
        protected String resourceName;

        @Override
        public void show(ExportDataProvider dataProvider, String resourceName, @Nullable ExportFormat format) {
            this.resourceName = resourceName;
        }

        @Override
        public void show(ExportDataProvider dataProvider, String resourceName) {
            this.resourceName = resourceName;
        }

        @Override
        public void show(FileDescriptor fileDescriptor, @Nullable ExportFormat format) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void show(FileDescriptor fileDescriptor) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isShowNewWindow() {
            return false;
        }

        @Override
        public void setShowNewWindow(boolean showNewWindow) {
        }

        @Override
        public void setFrame(@Nullable Frame frame) {
        }
    }
}
