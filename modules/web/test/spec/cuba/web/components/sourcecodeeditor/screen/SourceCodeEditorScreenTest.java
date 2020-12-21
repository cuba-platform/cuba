/*
 * Copyright (c) 2008-2020 Haulmont.
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

package spec.cuba.web.components.sourcecodeeditor.screen;

import com.haulmont.cuba.gui.components.SourceCodeEditor;
import com.haulmont.cuba.gui.components.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.components.autocomplete.Suggestion;
import com.haulmont.cuba.gui.screen.Install;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UiController
@UiDescriptor("sourcecodeeditor-screen-test.xml")
public class SourceCodeEditorScreenTest extends Screen {

    @Inject
    public SourceCodeEditor sourceCodeEditor;

    public int startPosition = -1;
    public int endPosition = -1;

    @Install(to = "sourceCodeEditor", subject = "suggester")
    private List<Suggestion> sourceCodeEditorSuggester(AutoCompleteSupport source, String text, int cursorPosition) {
        List<String> options = Arrays.asList("___");
        List<Suggestion> suggestions = new ArrayList<>();
        for (String item : options) {
            suggestions.add(new Suggestion(source, item, item, null, startPosition, endPosition));
        }
        return suggestions;
    }
}
