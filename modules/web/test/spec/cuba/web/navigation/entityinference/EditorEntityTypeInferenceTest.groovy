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

package spec.cuba.web.navigation.entityinference

import com.haulmont.cuba.gui.config.WindowInfo
import com.haulmont.cuba.gui.screen.FrameOwner
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.web.sys.navigation.EditorTypeExtractor
import org.dom4j.tree.BaseElement
import spec.cuba.web.navigation.entityinference.testscreens.notype.byclass.ExtBaseAbstEditorNT
import spec.cuba.web.navigation.entityinference.testscreens.notype.byclass.ExtBaseStdEditorNT
import spec.cuba.web.navigation.entityinference.testscreens.notype.byclass.ScreenExtAbstEditorNT
import spec.cuba.web.navigation.entityinference.testscreens.notype.byclass.ScreenExtStdEditorNT
import spec.cuba.web.navigation.entityinference.testscreens.notype.byinterface.ExtBaseEditorScreenNT
import spec.cuba.web.navigation.entityinference.testscreens.notype.byinterface.ScreenImplEditorScreenNT
import spec.cuba.web.navigation.entityinference.testscreens.withtype.byclass.ExtBaseAbstEditor
import spec.cuba.web.navigation.entityinference.testscreens.withtype.byclass.ExtBaseStdEditor
import spec.cuba.web.navigation.entityinference.testscreens.withtype.byclass.ScreenExtAbstEditor
import spec.cuba.web.navigation.entityinference.testscreens.withtype.byclass.ScreenExtStdEditor
import spec.cuba.web.navigation.entityinference.testscreens.withtype.byinterface.ExtBaseEditorScreen
import spec.cuba.web.navigation.entityinference.testscreens.withtype.byinterface.ScreenImplEditorScreen
import spock.lang.Specification

class EditorEntityTypeInferenceTest extends Specification {

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen implements EditorScreen<User>'() {
        def windowInfo = getWindowInfoFor(ScreenImplEditorScreen.class)

        when: 'entity type is specified in EditorScreen generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type can be extracted'
        User.class.isAssignableFrom(entityClass)
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends AbstractEditor<User>'() {
        def windowInfo = getWindowInfoFor(ScreenExtAbstEditor.class)

        when: 'entity type is specified in AbstractEditor generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type can be extracted'
        User.class.isAssignableFrom(entityClass)
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends StandardEditor<User>'() {
        def windowInfo = getWindowInfoFor(ScreenExtStdEditor.class)

        when: 'entity type is specified in StandardEditor generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type can be extracted'
        User.class.isAssignableFrom(entityClass)
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends BaseEditorScreen implements EditorScreen<User>'() {
        def windowInfo = getWindowInfoFor(ExtBaseEditorScreen.class)

        when: 'entity type is specified in parent in EditorScreen generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type can be extracted'
        User.class.isAssignableFrom(entityClass)
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends BaseAbstEditor extends AbstractEditor<User>'() {
        def windowInfo = getWindowInfoFor(ExtBaseAbstEditor.class)

        when: 'entity type is specified in parent in AbstractEditor generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type can be extracted'
        User.class.isAssignableFrom(entityClass)
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends BaseStdEditor extends StandardEditor<User>'() {
        def windowInfo = getWindowInfoFor(ExtBaseStdEditor.class)

        when: 'entity type is specified in parent in StandardEditor generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type can be extracted'
        User.class.isAssignableFrom(entityClass)
    }

    // No type declared

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen implements EditorScreen'() {
        def windowInfo = getWindowInfoFor(ScreenImplEditorScreenNT.class)

        when: 'entity type is not specified in EditorScreen generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type cannot be be extracted'
        entityClass == null
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends AbstractEditor'() {
        def windowInfo = getWindowInfoFor(ScreenExtAbstEditorNT.class)

        when: 'entity type is not specified in AbstractEditor generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type cannot be extracted'
        entityClass == null
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends StandardEditor'() {
        def windowInfo = getWindowInfoFor(ScreenExtStdEditorNT.class)

        when: 'entity type is not specified in StandardEditor generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type cannot be extracted'
        entityClass == null
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends BaseEditorScreen implements EditorScreen'() {
        def windowInfo = getWindowInfoFor(ExtBaseEditorScreenNT.class)

        when: 'entity type is not specified in parent in EditorScreen generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type cannot be extracted'
        entityClass == null
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends BaseAbstEditor extends AbstractEditor'() {
        def windowInfo = getWindowInfoFor(ExtBaseAbstEditorNT.class)

        when: 'entity type is not specified in parent in AbstractEditor generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type cannot be extracted'
        entityClass == null
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Screen extends BaseStdEditor extends StandardEditor'() {
        def windowInfo = getWindowInfoFor(ExtBaseStdEditorNT.class)

        when: 'entity type is not specified in parent in StandardEditor generic'
        def entityClass = EditorTypeExtractor.extractEntityClass(windowInfo)

        then: 'type cannot be extracted'
        entityClass == null
    }

    // Util

    protected getWindowInfoFor(Class<? extends FrameOwner> controllerClass) {
        def wi = new WindowInfo('paramParentEditor', null, new BaseElement('window'))

        wi = Spy(wi)
        wi.getControllerClass() >> controllerClass

        wi
    }
}
