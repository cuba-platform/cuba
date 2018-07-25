package com.haulmont.cuba.query_conditions;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.queryconditions.LogicalCondition;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.haulmont.cuba.core.global.queryconditions.JpqlCondition.where;
import static com.haulmont.cuba.core.global.queryconditions.LogicalCondition.and;
import static com.haulmont.cuba.core.global.queryconditions.LogicalCondition.or;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueryConditionsUsageTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private DataManager dataManager;

    @Before
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);
    }

    @Test
    public void test() {
        LogicalCondition condition = and()
                .add(where("u.login like :login"))
                .add(where("u.userRoles ur", "ur.role.name = :roleName"))
                .add(or()
                        .add(where("u.foo = :foo"))
                        .add(where("u.bar = :bar"))
                );

        LoadContext.Query query = LoadContext.createQuery("select u from sec$User u")
                .setCondition(condition)
                .setParameter("login", "admin");
        LoadContext<User> loadContext = LoadContext.create(User.class).setQuery(query);
        List<User> users = dataManager.loadList(loadContext);
        assertEquals(1, users.size());

        Optional<User> userOpt = dataManager.load(User.class)
                .query("select u from sec$User u")
                .condition(condition)
                .parameter("login", "admin")
                .optional();
        assertTrue(userOpt.isPresent());
    }
}
