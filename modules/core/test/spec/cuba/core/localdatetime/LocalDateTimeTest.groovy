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
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset

class LocalDateTimeTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Persistence persistence = cont.persistence()
    private Metadata metadata = cont.metadata()
    private DataManager dataManager

    private LocalDateTimeEntity entity

    private LocalDate localDate, minLocalDate
    private LocalTime localTime, minLocalTime
    private LocalDateTime localDateTime, minLocalDateTime
    private OffsetDateTime offsetDateTime, minOffsetDateTime
    private OffsetTime offsetTime, minOffsetTime


    void setup() {
        persistence.runInTransaction({ em ->
            entity = metadata.create(LocalDateTimeEntity)

            localDate = LocalDate.of(2018, 05, 06)
            minLocalDate = LocalDate.of(2018, 05, 05)
            entity.localDate = localDate

            localTime = LocalTime.of(10, 20)
            minLocalTime = LocalTime.of(10, 10)
            entity.localTime = localTime

            localDateTime = LocalDateTime.of(LocalDate.of(2018, 04, 20),
                    LocalTime.of(9, 30, 15, 256000000))
            minLocalDateTime = LocalDateTime.of(LocalDate.of(2018, 04, 19),
                    LocalTime.of(9, 30, 10, 256000000))
            entity.localDateTime = localDateTime

            def offsetLocalDateTime = LocalDateTime.of(LocalDate.of(2018, 04, 20),
                    LocalTime.of(10, 30, 15, 256000000))
            def minOffsetLocalDateTime = LocalDateTime.of(LocalDate.of(2018, 04, 20),
                    LocalTime.of(10, 20, 15, 256000000))
            offsetDateTime = OffsetDateTime.of(offsetLocalDateTime, ZoneOffset.of('Z'))
            minOffsetDateTime = OffsetDateTime.of(minOffsetLocalDateTime, ZoneOffset.of('Z'))
            entity.offsetDateTime = offsetDateTime

            offsetTime = OffsetTime.of(LocalTime.of(10, 20), ZoneOffset.of('Z'))
            minOffsetTime = OffsetTime.of(LocalTime.of(10, 10), ZoneOffset.of('Z'))
            entity.offsetTime = offsetTime

            em.persist(entity)
        })
        dataManager = AppBeans.get(DataManager.class)
    }

    void cleanup() {
        def runner = new QueryRunner(persistence.dataSource)
        runner.update('delete from TEST_LOCAL_DATE_TIME_ENTITY')
    }

    def "load/store LocalDate"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity).id(entity.id).view(View.LOCAL).one()

        then:
        e.localDate != null
        e.localDate == localDate
    }

    def "load/store LocalTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity).id(entity.id).view(View.LOCAL).one()

        then:
        e.localTime != null
        e.localTime == localTime
    }

    def "load/store LocalDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity).id(entity.id).view(View.LOCAL).one()

        then:
        e.localDateTime != null
        e.localDateTime == localDateTime
    }

    def "load/store OffsetDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity).id(entity.id).view(View.LOCAL).one()

        then:
        e.offsetDateTime != null
        e.offsetDateTime.isEqual(offsetDateTime)
    }

    @Ignore
    def "load/store OffsetTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity).id(entity.id).view(View.LOCAL).one()

        then:
        e.offsetTime != null
        e.offsetTime.isEqual(offsetTime)
    }

    def "find by LocalDate"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.localDate = :dt')
                .parameter('dt', localDate)
                .view(View.LOCAL).one()

        then:
        e == entity
    }

    def "find by LocalDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.localDateTime = :dt')
                .parameter('dt', localDateTime)
                .view(View.LOCAL).one()

        then:
        e == entity
    }

    def "find by LocalTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.localTime = :dt')
                .parameter('dt', localTime)
                .view(View.LOCAL).one()

        then:
        e == entity
    }

    def "find by OffsetDateTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.offsetDateTime = :dt')
                .parameter('dt', offsetDateTime)
                .view(View.LOCAL).one()

        then:
        e == entity
        e.offsetDateTime != null
        e.offsetDateTime.isEqual(offsetDateTime)
    }

    @Ignore
    def "find by OffsetTime"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.offsetTime = :dt')
                .parameter('dt', offsetTime)
                .view(View.LOCAL).one()

        then:
        e == entity
        e.offsetTime != null
        e.offsetTime.isEqual(offsetTime)
    }

    def "find by LocalDate greater than"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.localDate > :dt and e.id = :id')
                .parameter('dt', minLocalDate)
                .parameter('id', entity.id)
                .view(View.LOCAL).one()

        then:
        e == entity
    }

    def "find by LocalTime greater than"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.localTime > :dt and e.id = :id')
                .parameter('dt', minLocalTime)
                .parameter('id', entity.id)
                .view(View.LOCAL).one()

        then:
        e == entity
    }

    def "find by LocalDateTime greater than"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.localDateTime > :dt and e.id = :id')
                .parameter('dt', minLocalDateTime)
                .parameter('id', entity.id)
                .view(View.LOCAL).one()

        then:
        e == entity
    }

    def "find by OffsetDateTime greater than"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.offsetDateTime > :dt and e.id = :id')
                .parameter('dt', minOffsetDateTime)
                .parameter('id', entity.id)
                .view(View.LOCAL).one()

        then:
        e == entity
        e.offsetDateTime != null
        e.offsetDateTime.isEqual(offsetDateTime)
    }

    @Ignore
    def "find by OffsetTime greater than"() {
        when:
        def e = dataManager.load(LocalDateTimeEntity)
                .query('select e from test_LocalDateTimeEntity e where e.offsetTime > :dt and e.id = :id')
                .parameter('dt', minOffsetTime)
                .parameter('id', entity.id)
                .view(View.LOCAL).one()

        then:
        e == entity
        e.offsetTime != null
        e.offsetTime.isEqual(offsetTime)
    }
}
