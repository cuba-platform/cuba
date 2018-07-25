package spec.cuba.core.query_conditions

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.queryconditions.Condition
import com.haulmont.cuba.core.global.queryconditions.ConditionJpqlGenerator
import com.haulmont.cuba.core.global.queryconditions.ConditionXmlLoader
import com.haulmont.cuba.core.global.queryconditions.LogicalCondition
import com.haulmont.cuba.core.global.queryconditions.PropertyCondition
import com.haulmont.cuba.testsupport.TestContainer
import org.dom4j.Element
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class QueryConditionsTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private ConditionXmlLoader xmlSerializer
    private ConditionJpqlGenerator jpqlGenerator

    void setup() {
        xmlSerializer = AppBeans.get(ConditionXmlLoader)
        jpqlGenerator = AppBeans.get(ConditionJpqlGenerator)
    }

    def "JPQL conditions"() {
        String xml = '''
            <and>
                <jpql>
                    <where>u.login like :login</where>
                </jpql>
                <jpql>
                    <join>u.userRoles ur</join>
                    <where>ur.role.name = :roleName</where>
                </jpql>
            </and>
'''
        when:

        Condition condition = xmlSerializer.fromXml(xml)

        then:

        condition instanceof LogicalCondition
        ((LogicalCondition) condition).type == LogicalCondition.Type.AND
        PropertyCondition c1 = ((LogicalCondition) condition).conditions[0]
        c1.getValue('where') == 'u.login like :login'
        c1.parameters == ['login']

        PropertyCondition c2 = ((LogicalCondition) condition).conditions[1]
        c2.getValue('join') == 'u.userRoles ur'
        c2.getValue('where') == 'ur.role.name = :roleName'
        c2.parameters == ['roleName']

        condition.getParameters().toSet() == ['login', 'roleName'].toSet()
    }

    def "some REST conditions"() {
        String xml = '''
            <and>
                <rest>
                    <param>login=${login}</param>
                </rest>
                <rest>
                    <param>role.name=${roleName}</param>
                </rest>
            </and>
'''
        xmlSerializer.addFactory('rest', { Element element ->
            if (element.name == 'rest') {
                return new SampleRestCondition(element.element('param').text)
            }
            return null
        })

        when:

        Condition condition = xmlSerializer.fromXml(xml)

        then:

        condition instanceof LogicalCondition
        ((LogicalCondition) condition).type == LogicalCondition.Type.AND
        PropertyCondition c1 = ((LogicalCondition) condition).conditions[0]
        c1.getValue('param') == 'login=${login}'
        c1.parameters == ['login']

        PropertyCondition c2 = ((LogicalCondition) condition).conditions[1]
        c2.getValue('param') == 'role.name=${roleName}'
        c2.parameters == ['roleName']

        condition.getParameters().toSet() == ['login', 'roleName'].toSet()

        cleanup:

        xmlSerializer.removeFactory('rest')
    }

    def "condition actualization"() {
        String xml = '''
            <and>
                <jpql>
                    <where>u.login like :login</where>
                </jpql>
                <jpql>
                    <join>join u.userRoles ur</join>
                    <where>ur.role.name = :roleName</where>
                </jpql>
                <or>
                    <jpql>
                        <join>, test$Foo f</join>
                        <where>f.foo = :foo</where>
                    </jpql>
                    <jpql>
                        <where>u.bar = :bar</where>
                    </jpql>
                </or>
            </and>
'''
        when:

        Condition condition = xmlSerializer.fromXml(xml)
        Condition actualized = condition.actualize(['login', 'roleName', 'foo', 'bar'].toSet())
        String query = jpqlGenerator.processQuery('select u from sec$User u', actualized)

        then:

        actualized instanceof LogicalCondition
        ((LogicalCondition) actualized).type == LogicalCondition.Type.AND
        ((LogicalCondition) actualized).conditions.size() == 3

        PropertyCondition c1 = ((LogicalCondition) actualized).conditions[0]
        c1.getValue('where') == 'u.login like :login'

        PropertyCondition c2 = ((LogicalCondition) actualized).conditions[1]
        c2.getValue('where') == 'ur.role.name = :roleName'

        LogicalCondition or = ((LogicalCondition) actualized).conditions[2]
        or.type == LogicalCondition.Type.OR
        or.conditions.size() == 2

        PropertyCondition c3 = or.conditions[0]
        c3.getValue('where') == 'f.foo = :foo'

        PropertyCondition c4 = or.conditions[1]
        c4.getValue('where') == 'u.bar = :bar'

        query == 'select u from sec$User u join u.userRoles ur, test$Foo f ' +
                'where (u.login like :login and ur.role.name = :roleName and (f.foo = :foo or u.bar = :bar))'

        when:

        actualized = condition.actualize(['login', 'roleName', 'foo'].toSet())
        query = jpqlGenerator.processQuery('select u from sec$User u', actualized)

        then:

        actualized instanceof LogicalCondition
        ((LogicalCondition) actualized).type == LogicalCondition.Type.AND
        ((LogicalCondition) actualized).conditions.size() == 3

        PropertyCondition c11 = ((LogicalCondition) actualized).conditions[0]
        c11.getValue('where') == 'u.login like :login'

        PropertyCondition c21 = ((LogicalCondition) actualized).conditions[1]
        c21.getValue('where') == 'ur.role.name = :roleName'

        PropertyCondition c31 = ((LogicalCondition) actualized).conditions[2]
        c31.getValue('where') == 'f.foo = :foo'

        query == 'select u from sec$User u join u.userRoles ur, test$Foo f ' +
                'where (u.login like :login and ur.role.name = :roleName and f.foo = :foo)'

        when:

        actualized = condition.actualize(['login', 'roleName'].toSet())
        query = jpqlGenerator.processQuery('select u from sec$User u', actualized)

        then:

        actualized instanceof LogicalCondition
        ((LogicalCondition) actualized).type == LogicalCondition.Type.AND
        ((LogicalCondition) actualized).conditions.size() == 2

        PropertyCondition c12 = ((LogicalCondition) actualized).conditions[0]
        c12.getValue('where') == 'u.login like :login'

        PropertyCondition c22 = ((LogicalCondition) actualized).conditions[1]
        c22.getValue('where') == 'ur.role.name = :roleName'

        query == 'select u from sec$User u join u.userRoles ur ' +
                'where (u.login like :login and ur.role.name = :roleName)'

        when:

        actualized = condition.actualize(['roleName'].toSet())
        query = jpqlGenerator.processQuery('select u from sec$User u', actualized)

        then:

        actualized instanceof PropertyCondition
        ((PropertyCondition) actualized).getValue('where') == 'ur.role.name = :roleName'

        query == 'select u from sec$User u join u.userRoles ur where ur.role.name = :roleName'

        when:

        actualized = condition.actualize(Collections.emptySet())
        query = jpqlGenerator.processQuery('select u from sec$User u', actualized)

        then:

        actualized == null

        query == 'select u from sec$User u'
    }
}
