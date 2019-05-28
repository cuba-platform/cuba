/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.core.bean_validation

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.RemoteException
import com.haulmont.cuba.core.sys.MethodValidationTestService
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class MethodValidationTest extends Specification {
    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private MethodValidationTestService service

    void setup() {
        service = AppBeans.get(MethodValidationTestService)
    }

    def "invalid parameter test"(){
        when:
        service.validateParam("1")

        then:
        thrown(RemoteException)
    }

    def "invalid result test"(){
        when:
        service.validateResult("1")

        then:
        thrown(RemoteException)
    }

    def "valid parameter test"(){
        when:
        service.validateParam("12345")

        then: "ok"
    }

    def "valid result test"(){
        when:
        service.validateResult("12345")

        then: "ok"
    }
}
