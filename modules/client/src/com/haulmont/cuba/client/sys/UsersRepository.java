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

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

@Component(UsersRepository.NAME)
public class UsersRepository {
    public static final String NAME = "cuba_UsersRepository";

    @Inject
    protected DataManager dataManager;

    private final Logger log = LoggerFactory.getLogger(UsersRepository.class);

    /**
     * Finds active user by login. Used for client authentication.
     * @param login - user login
     * @return user by login
     */
    public @Nullable User findUserByLogin(String login) {
        if (login == null) {
            throw new IllegalArgumentException("Login is null");
        }

        Optional<User> result = dataManager.load(User.class)
                .view(View.LOCAL)
                .query("select u from sec$User u where u.loginLowerCase = :login and (u.active = true or u.active is null)")
                .parameter("login", login.toLowerCase())
                .optional();

        if (!result.isPresent()) {
            log.debug("Unable to find user: {}", login);
        }

        return result.orElse(null);
    }
}
