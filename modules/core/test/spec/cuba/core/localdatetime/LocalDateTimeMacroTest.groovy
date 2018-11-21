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

package spec.cuba.core.localdatetime

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.testmodel.localdatetime.LocalDateTimeEntity
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime

class LocalDateTimeMacroTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Persistence persistence = cont.persistence()
    private Metadata metadata = cont.metadata()
    private DataManager dataManager

    private LocalDateTimeEntity entity

    private LocalDate localDate
    private LocalDateTime localDateTime
    private OffsetDateTime offsetDateTime
    private Date nowDate

    void setup() {
        persistence.runInTransaction({ em ->
            entity = metadata.create(LocalDateTimeEntity)

            localDate = LocalDate.now()
            entity.localDate = localDate

            localDateTime = LocalDateTime.now()
            entity.localDateTime = localDateTime

            offsetDateTime = OffsetDateTime.now()
            entity.offsetDateTime = offsetDateTime

            nowDate = new Date()
            entity.nowDate = nowDate

            em.persist(entity)
        })
        dataManager = AppBeans.get(DataManager.class)
    }

    void cleanup() {
        def runner = new QueryRunner(persistence.dataSource)
        runner.update('delete from TEST_LOCAL_DATE_TIME_ENTITY')
    }

    //----------@between--------

    def "@between for LocalDate"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDate, now, now + 1, day)')
                .view(View.LOCAL).optional().orElse(null)

        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDate, now - 1, now, day)')
                .view(View.LOCAL).optional().orElse(null)

        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDate, now, now + 1, month)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDate, now - 1, now, month)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDate, now - 1, now, minute)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null
    }

    def "@between for LocalDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDateTime, now, now + 1, day)')
                .view(View.LOCAL).optional().orElse(null)

        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDateTime, now - 1, now, day)')
                .view(View.LOCAL).optional().orElse(null)

        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDateTime, now, now + 1, month)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDateTime, now - 1, now, month)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDateTime, now - 1, now, minute)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDateTime, now, now + 1, minute)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDateTime, now - 10, now - 5, second)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.localDateTime, now - 5, now + 5, second)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity
    }

    def "@between for OffsetDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.offsetDateTime, now, now + 1, day)')
                .view(View.LOCAL).optional().orElse(null)

        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.offsetDateTime, now - 1, now, day)')
                .view(View.LOCAL).optional().orElse(null)

        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.offsetDateTime, now, now + 1, month)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.offsetDateTime, now - 1, now, month)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.offsetDateTime, now - 1, now, minute)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.offsetDateTime, now, now + 1, minute)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.offsetDateTime, now - 10, now - 5, second)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @between(e.offsetDateTime, now - 5, now + 5, second)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity
    }

    //----------@dateBefore and @dateAfter--------


    def "@dateBefore for DateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateBefore(e.localDate, :param)')
                .parameter('param', LocalDate.now())
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateBefore(e.localDate, :param)')
                .parameter('param', LocalDate.now().plusDays(1))
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity
    }

    def "@dateAfter for DateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateAfter(e.localDate, :param)')
                .parameter('param', LocalDate.now())
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateAfter(e.localDate, :param)')
                .parameter('param', LocalDate.now().plusDays(1))
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null
    }

    def "@dateBefore for LocalDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateBefore(e.localDateTime, :param)')
                .parameter('param', LocalDateTime.now())
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateBefore(e.localDateTime, :param)')
                .parameter('param', LocalDateTime.now().plusDays(1))
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity
    }

    def "@dateAfter for LocalDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateAfter(e.localDateTime, :param)')
                .parameter('param', LocalDateTime.now())
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateAfter(e.localDateTime, :param)')
                .parameter('param', LocalDateTime.now().plusDays(1))
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null
    }

    def "@dateBefore for OffsetDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateBefore(e.offsetDateTime, :param)')
                .parameter('param', OffsetDateTime.now())
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateBefore(e.offsetDateTime, :param)')
                .parameter('param', OffsetDateTime.now().plusDays(1))
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity
    }

    def "@dateAfter for OffsetDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateAfter(e.offsetDateTime, :param)')
                .parameter('param', OffsetDateTime.now())
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateAfter(e.offsetDateTime, :param)')
                .parameter('param', OffsetDateTime.now().plusDays(1))
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null
    }

    def "@dateBefore with now"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateBefore(e.nowDate, now)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateBefore(e.nowDate, now+1)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity
    }

    def "@dateAfter with now"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateAfter(e.nowDate, now)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateAfter(e.nowDate, now+1)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null
    }

    //----------@dateEquals--------

    def "@dateEquals for DateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateEquals(e.localDate, :param)')
                .parameter('param', LocalDate.now())
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateEquals(e.localDate, :param)')
                .parameter('param', LocalDate.now().plusDays(1))
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null
    }


    def "@dateEquals for LocalDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateEquals(e.localDateTime, :param)')
                .parameter('param', LocalDateTime.now())
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateEquals(e.localDateTime, :param)')
                .parameter('param', LocalDateTime.now().plusDays(1))
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null
    }

    def "@dateEquals for OffsetDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateEquals(e.offsetDateTime, :param)')
                .parameter('param', OffsetDateTime.now())
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateEquals(e.offsetDateTime, :param)')
                .parameter('param', OffsetDateTime.now().plusDays(1))
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null
    }

    def "@dateEquals for now"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateEquals(e.nowDate, now)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity

        when:
        e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @dateEquals(e.nowDate, now+1)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == null
    }

    //----------@dateEquals--------

    def "@today for DateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @today(e.localDate)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity
    }

    def "@today for LocalDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @today(e.localDateTime)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity
    }

    def "@today for OffsetDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where @today(e.offsetDateTime)')
                .view(View.LOCAL).optional().orElse(null)
        then:
        e == entity
    }
}
