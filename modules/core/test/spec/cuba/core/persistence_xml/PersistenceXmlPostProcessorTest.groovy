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

package spec.cuba.core.persistence_xml

import com.haulmont.cuba.core.global.Stores
import com.haulmont.cuba.core.sys.AppContext
import com.haulmont.cuba.core.sys.persistence.PersistenceConfigProcessor
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class PersistenceXmlPostProcessorTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private String workDir
    private PersistenceConfigProcessor persistenceConfigProcessor

    void setup() {
        workDir = System.getProperty('user.dir') + '/test-home/PersistenceXmlPostProcessorTest'
        new File(workDir).deleteDir()

        persistenceConfigProcessor = new PersistenceConfigProcessor()
        this.persistenceConfigProcessor.setStorageName(Stores.MAIN)
        this.persistenceConfigProcessor.setSourceFiles(['base-persistence.xml', 'cuba-persistence.xml'])
        this.persistenceConfigProcessor.setOutputFile(workDir + '/persistence.xml')
    }

    void cleanup() {
    }

    def "orm.xml is not created by default"() {

        when:

        persistenceConfigProcessor.create()

        then:

        new File("$workDir/persistence.xml").exists()
        ! new File("$workDir/orm.xml").exists()
    }

    def "orm.xml is created and modified by post-processor"() {
        AppContext.setProperty('cuba.ormXmlPostProcessor', 'spec.cuba.core.persistence_xml.TestOrmXmlPostProcessor')

        when:

        persistenceConfigProcessor.create()

        then:

        new File("$workDir/persistence.xml").exists()
        def ormXmlFile = new File("$workDir/orm.xml")
        ormXmlFile.exists()

        def xml = new XmlSlurper().parse(ormXmlFile)
        xml.test.@attr == 'val'

        cleanup:

        AppContext.setProperty('cuba.ormXmlPostProcessor', null)
    }

    def "persistence.xml is modified by post-processor"() {
        AppContext.setProperty('cuba.persistenceXmlPostProcessor', 'spec.cuba.core.persistence_xml.TestPersistenceXmlPostProcessor')

        when:

        persistenceConfigProcessor.create()

        then:

        def persistenceXml = new File("$workDir/persistence.xml")
        persistenceXml.exists()

        def xml = new XmlSlurper().parse(persistenceXml)
        xml.'persistence-unit'.'properties'.'property'.find { it -> it.@name == 'some-prop' && it.@value == 'some-val'}

        cleanup:

        AppContext.setProperty('cuba.persistenceXmlPostProcessor', null)
    }
}
