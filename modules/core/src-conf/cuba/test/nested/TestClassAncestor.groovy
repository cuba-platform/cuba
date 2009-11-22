package cuba.test.nested

import com.haulmont.cuba.core.global.HsqlDbDialect

class TestClassAncestor extends HsqlDbDialect {

  def testMethod() {
    return "OK"
  }
}