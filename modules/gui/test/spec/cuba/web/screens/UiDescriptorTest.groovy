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

package spec.cuba.web.screens

import com.haulmont.cuba.gui.screen.Screen
import com.haulmont.cuba.gui.screen.UiDescriptor
import spock.lang.Specification

import static com.haulmont.cuba.gui.sys.UiDescriptorUtils.getInferredTemplate

class UiDescriptorTest extends Specification {

    def "@UiDescriptor is inherited by subclasses"() {
        when:

        def annotation = BaseScreen.getAnnotation(UiDescriptor)

        then:

        annotation != null
        getInferredTemplate(annotation, BaseScreen) == 'base.xml'

        when:

        def demoAnnotation = DemoScreen.getAnnotation(UiDescriptor)

        then:

        demoAnnotation != null
        getInferredTemplate(demoAnnotation, DemoScreen) == 'base.xml'

        when:

        def pathAnnotation = PathScreen.getAnnotation(UiDescriptor)

        then:

        pathAnnotation != null
        getInferredTemplate(pathAnnotation, PathScreen) == 'path.xml'

        when:

        def extAnnotation = ExtScreen.getAnnotation(UiDescriptor)

        then:

        extAnnotation != null
        getInferredTemplate(extAnnotation, ExtScreen) == 'ext.xml'
    }

    @UiDescriptor("base.xml")
    static class BaseScreen extends Screen {
    }

    static class DemoScreen extends BaseScreen {
    }

    @UiDescriptor(path = "path.xml")
    static class PathScreen extends BaseScreen {
    }

    @UiDescriptor("ext.xml")
    static class ExtScreen extends DemoScreen {
    }
}