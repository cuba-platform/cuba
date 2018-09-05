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

package spec.cuba.core.metadata

import com.haulmont.bali.db.ListArrayHandler
import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory
import com.haulmont.cuba.core.sys.persistence.SequenceSupport
import com.haulmont.cuba.testmodel.number_id.NumberIdJoinedChild
import com.haulmont.cuba.testmodel.number_id.NumberIdJoinedRoot
import com.haulmont.cuba.testmodel.number_id.NumberIdSeqNameFirst
import com.haulmont.cuba.testmodel.number_id.NumberIdSeqNameSecond
import com.haulmont.cuba.testmodel.number_id.NumberIdSingleTableChild
import com.haulmont.cuba.testmodel.number_id.NumberIdSingleTableGrandChild
import com.haulmont.cuba.testmodel.number_id.NumberIdSingleTableRoot
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class NumberIdGenerationTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Metadata metadata
    private SequenceSupport sequenceSupport

    void setup() {
        metadata = cont.metadata()
        sequenceSupport = DbmsSpecificFactory.getSequenceSupport()
    }

    def "joined inheritance strategy"() {

        when:

        def root1 = metadata.create(NumberIdJoinedRoot)
        def root2 = metadata.create(NumberIdJoinedRoot)

        then:

        sequenceExistsByEntityName(metadata.getClassNN(NumberIdJoinedRoot).getName())
        root2.id == root1.id + 1

        when: "creating child entities"

        def child1 = metadata.create(NumberIdJoinedChild)
        def child2 = metadata.create(NumberIdJoinedChild)

        then: "the same sequence as for root is used"

        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdJoinedChild).getName())
        child1.id == root2.id + 1
        child2.id == child1.id + 1

    }

    def "single table inheritance strategy"() {

        when:

        def root1 = metadata.create(NumberIdSingleTableRoot)
        def root2 = metadata.create(NumberIdSingleTableRoot)

        then:

        sequenceExistsByEntityName(metadata.getClassNN(NumberIdSingleTableRoot).getName())
        root2.id == root1.id + 1

        when: "creating child entities"

        def child1 = metadata.create(NumberIdSingleTableChild)
        def child2 = metadata.create(NumberIdSingleTableChild)

        then: "the same sequence as for root is used"

        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdSingleTableChild).getName())
        child1.id == root2.id + 1
        child2.id == child1.id + 1

        when: "creating grand children entities"

        def grandChild1 = metadata.create(NumberIdSingleTableGrandChild)
        def grandChild2 = metadata.create(NumberIdSingleTableGrandChild)

        then: "the same sequence as for root is used"

        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdSingleTableChild).getName())
        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdSingleTableGrandChild).getName())
        grandChild1.id == child2.id + 1
        grandChild2.id == grandChild1.id + 1
    }

    def "sequence name annotation"() {

        when:

        def first = metadata.create(NumberIdSeqNameFirst)
        def second = metadata.create(NumberIdSeqNameSecond)

        then:

        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdSeqNameFirst).getName())
        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdSeqNameSecond).getName())
        sequenceExistsByName('seq_number_id_name')
        first.id + 1  == second.id
        getCurrentSequenceValue('seq_number_id_name') == second.id

        when:

        first = metadata.create(NumberIdSeqNameFirst)

        then:

        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdSeqNameFirst).getName())
        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdSeqNameSecond).getName())
        sequenceExistsByName('seq_number_id_name')
        getCurrentSequenceValue('seq_number_id_name') == first.id

        when:

        second = metadata.create(NumberIdSeqNameSecond)

        then:

        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdSeqNameFirst).getName())
        !sequenceExistsByEntityName(metadata.getClassNN(NumberIdSeqNameSecond).getName())
        sequenceExistsByName('seq_number_id_name')
        getCurrentSequenceValue('seq_number_id_name') == second.id
        first.id + 1  == second.id
    }

    private boolean sequenceExistsByEntityName(String entityName) {
        return sequenceExistsByName(getSequenceName(entityName))
    }

    private boolean sequenceExistsByName(String sequenceName) {
        def sequenceExistsSql = sequenceSupport.sequenceExistsSql(sequenceName)
        def runner = new QueryRunner(cont.persistence().getDataSource())
        List<Object[]> seqRows = runner.query(sequenceExistsSql, new ListArrayHandler())
        return !seqRows.isEmpty()
    }

    protected String getSequenceName(String entityName) {
        return "seq_id_" + entityName.replace('$', '_');
    }

    private long getCurrentSequenceValue(String sequenceName) {
        def sql = "select NEXT_VALUE from INFORMATION_SCHEMA.SYSTEM_SEQUENCES where SEQUENCE_NAME = '" + sequenceName.toUpperCase() + "'"
        def runner = new QueryRunner(cont.persistence().getDataSource())
        List<Object[]> seqRows = runner.query(sql, new ListArrayHandler())
        return (seqRows[0][0] as long) - 1
    }
}
