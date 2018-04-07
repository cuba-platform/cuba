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
import com.haulmont.cuba.core.app.NumberIdWorker
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.sys.AppContext
import com.haulmont.cuba.core.sys.NumberIdCache
import com.haulmont.cuba.core.sys.SecurityContextAwareRunnable
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory
import com.haulmont.cuba.core.sys.persistence.SequenceSupport
import com.haulmont.cuba.testmodel.number_id.NumberIdSingleTableRoot
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class NumberIdConcurrencyTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Metadata metadata
    private DataManager dataManager
    private SequenceSupport sequenceSupport

    private Logger log = LoggerFactory.getLogger(NumberIdConcurrencyTest)

    void setup() {
        metadata = cont.metadata()
        dataManager = AppBeans.get(DataManager)
        sequenceSupport = DbmsSpecificFactory.getSequenceSupport()
        cleanup()
    }

    void cleanup() {
        if (sequenceExists()) {
            def sql = sequenceSupport.deleteSequenceSql(getSequenceName('test$NumberIdSingleTableRoot'))
            def runner1 = new QueryRunner(cont.persistence().getDataSource())
            runner1.update(sql)
        }
        AppBeans.get(NumberIdWorker).reset()
        AppBeans.get(NumberIdCache).reset()

        def runner = new QueryRunner(cont.persistence().getDataSource())
        runner.update('delete from TEST_NUMBER_ID_SINGLE_TABLE_ROOT')
    }

    def "generating ids with increment 1"() {

        AppContext.setProperty('cuba.numberIdCacheSize', '1')

        when:

        generateSomeEntities(100)

        then:

        countEntities() == 100

        getNextSequenceValue() == 100

        cleanup:

        AppContext.setProperty('cuba.numberIdCacheSize', null)
    }

    def "generating ids with increment 20"() {

        AppContext.setProperty('cuba.numberIdCacheSize', '20')

        when:

        generateSomeEntities(500)

        then:

        countEntities() == 500

        cleanup:

        AppContext.setProperty('cuba.numberIdCacheSize', null)
    }

    private void generateSomeEntities(int count) {
        long start = System.currentTimeMillis()
        ExecutorService executorService = Executors.newFixedThreadPool(10)
        List<String> failed = []
        for (i in 1..count) {
            String idx = "$i"
            executorService.submit(new SecurityContextAwareRunnable({
                try {
                    NumberIdSingleTableRoot foo = metadata.create(NumberIdSingleTableRoot)
                    foo.setName('item-' + idx.padLeft(4, '0'))
                    dataManager.commit(foo)
                } catch (Exception e) {
                    log.error('Error creating entity', e)
                }
            }))
        }
        executorService.shutdown()
        boolean terminated
        try {
            terminated = executorService.awaitTermination(10, TimeUnit.SECONDS)
        } catch (InterruptedException e) {
            throw new RuntimeException(e)
        }
        if (!terminated)
            log.warn("Termination timed out")
        log.info("Completed in ${System.currentTimeMillis() - start}ms, Failed: $failed")
    }

    private String getSequenceName(String entityName) {
        return "seq_id_" + entityName.replace('$', '_')
    }

    private boolean sequenceExists() {
        def sequenceExistsSql = sequenceSupport.sequenceExistsSql(getSequenceName('test$NumberIdSingleTableRoot'))
        def runner = new QueryRunner(cont.persistence().getDataSource())
        List<Object[]> seqRows = runner.query(sequenceExistsSql, new ListArrayHandler())
        return !seqRows.isEmpty()
    }


    private long getNextSequenceValue() {
        def sql = sequenceSupport.getNextValueSql(getSequenceName('test$NumberIdSingleTableRoot'))
        def runner = new QueryRunner(cont.persistence().getDataSource())
        List<Object[]> seqRows = runner.query(sql, new ListArrayHandler())
        return seqRows[0][0] as long
    }

    private long countEntities() {
        def runner = new QueryRunner(cont.persistence().getDataSource())
        List<Object[]> seqRows = runner.query("select count(*) from TEST_NUMBER_ID_SINGLE_TABLE_ROOT", new ListArrayHandler())
        return seqRows[0][0] as long
    }
}
