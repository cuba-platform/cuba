/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.sys.remoting.discovery;

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaticServerSelectorTest {

    private UserSession session1;
    private UserSession session2;
    private UserSession session3;
    private UserSession session4;
    private UserSession currentSession;
    private List<String> lastSortedServers = new ArrayList<>();

    public StaticServerSelectorTest() {
        User user1 = new User();
        user1.setLogin("user1");
        session1 = new UserSession(UUID.randomUUID(), user1, Collections.emptyList(), Locale.ENGLISH, false);

        User user2 = new User();
        user2.setLogin("user2");
        session2 = new UserSession(UUID.randomUUID(), user2, Collections.emptyList(), Locale.ENGLISH, false);

        User user3 = new User();
        user3.setLogin("user3");
        session3 = new UserSession(UUID.randomUUID(), user3, Collections.emptyList(), Locale.ENGLISH, false);

        User user4 = new User();
        user4.setLogin("user4");
        session4 = new UserSession(UUID.randomUUID(), user4, Collections.emptyList(), Locale.ENGLISH, false);
    }

    private StaticServerSelector createSelector(String urlList) {
        StaticServerSelector selector = new StaticServerSelector() {
            @Nullable
            @Override
            protected SessionUrlsHolder getSessionUrlsHolder() {
                return currentSession != null ? new UserSessionUrlsHolder(currentSession) : null;
            }
        };
        selector.setBaseUrl(urlList);
//        selector.setRandomPriority("false");
        selector.setServerSorter(strings -> {
            if (lastSortedServers.isEmpty()) {
                lastSortedServers.addAll(strings);
            }
            if (lastSortedServers.size() > 1) {
                // put the last element to the beginning
                lastSortedServers.add(0, lastSortedServers.get(lastSortedServers.size() - 1));
                lastSortedServers.remove(lastSortedServers.size() - 1);
            }
            strings.clear();
            strings.addAll(lastSortedServers);
        });
        selector.setServletPath("path");
        selector.init();
        return selector;
    }


    private Object newRequest(StaticServerSelector selector, String result) {
        Object context = selector.initContext();
        String url = selector.getUrl(context);
        assertEquals(result, url);
        return context;
    }

    private void sameRequest(StaticServerSelector selector, Object context, String result) {
        String url = selector.getUrl(context);
        assertEquals(result, url);
    }

    @Test
    public void testSuccess1() throws Exception {
        StaticServerSelector selector = createSelector("server1");

        currentSession = session1;
        newRequest(selector, "server1/path");
        newRequest(selector, "server1/path");

        currentSession = session2;
        newRequest(selector, "server1/path");
        newRequest(selector, "server1/path");

        currentSession = null;
        newRequest(selector, "server1/path");
        newRequest(selector, "server1/path");
    }

    @Test
    public void testSuccess2() throws Exception {
        StaticServerSelector selector = createSelector("server1,server2");

        currentSession = session1;
        newRequest(selector, "server2/path");
        newRequest(selector, "server2/path");

        currentSession = session2;
        newRequest(selector, "server1/path");
        newRequest(selector, "server1/path");

        currentSession = session3;
        newRequest(selector, "server2/path");
        newRequest(selector, "server2/path");
    }

    @Test
    public void testSuccess3() throws Exception {
        StaticServerSelector selector = createSelector("server1,server2,server3");

        currentSession = session1;
        newRequest(selector, "server3/path");
        newRequest(selector, "server3/path");

        currentSession = session2;
        newRequest(selector, "server2/path");
        newRequest(selector, "server2/path");

        currentSession = session3;
        newRequest(selector, "server1/path");
        newRequest(selector, "server1/path");

        currentSession = session4;
        newRequest(selector, "server3/path");
        newRequest(selector, "server3/path");
    }

    @Test
    public void testFail1() throws Exception {
        StaticServerSelector selector = createSelector("server1");

        currentSession = session1;
        Object context = newRequest(selector, "server1/path");
        selector.fail(context);
        sameRequest(selector, context, null);
        sameRequest(selector, context, null);

        context = newRequest(selector, "server1/path");
        selector.fail(context);
        sameRequest(selector, context, null);

        currentSession = session2;
        context = newRequest(selector, "server1/path");
        sameRequest(selector, context, "server1/path");
    }

    @Test
    public void testFail2() throws Exception {
        StaticServerSelector selector = createSelector("server1,server2");

        currentSession = session1;
        Object context1 = newRequest(selector, "server2/path");

        currentSession = session2;
        Object context2 = newRequest(selector, "server1/path");

        selector.fail(context1);
        sameRequest(selector, context1, "server1/path");
        selector.fail(context1);
        sameRequest(selector, context1, null);

        // server1 is now available
        selector.success(context2);

        // first request fails anyway
        sameRequest(selector, context1, null);

        // next request with the same session succeeds
        currentSession = session1;
        context1 = newRequest(selector, "server1/path");
    }

    /**
     * Each new session on first request should get the full list of servers regardless of previously failed ones
     */
    @Test
    public void testFail3() throws Exception {
        StaticServerSelector selector = createSelector("server1,server2");

        currentSession = session1; //server2,server1
        Object context1 = newRequest(selector, "server2/path");

        selector.fail(context1);
        sameRequest(selector, context1, "server1/path");

        currentSession = session2; //server1,server2
        Object context2 = newRequest(selector, "server1/path");

        currentSession = session3; //server2,server1
        Object context3 = newRequest(selector, "server2/path");
    }

    @Test
    public void testNoSession() throws Exception {
        StaticServerSelector selector = createSelector("server1,server2");

        currentSession = null;
        newRequest(selector, "server2/path");
        newRequest(selector, "server1/path");
        newRequest(selector, "server2/path");
    }
}