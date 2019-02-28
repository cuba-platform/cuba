/*
 * Copyright (c) 2008-2019 Haulmont.
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

package spec.cuba.web.menu.commandtargets;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.sys.BeanLocatorAware;

import java.util.Map;
import java.util.function.Consumer;

public class TestMenuItemConsumer implements Consumer<Map<String, Object>>, BeanLocatorAware {

    public static final ThreadLocal<Boolean> launched = new ThreadLocal<>();
    public static final ThreadLocal<Boolean> beanLocatorSet = new ThreadLocal<>();

    @Override
    public void accept(Map<String, Object> stringObjectMap) {
        launched.set(true);
    }

    @Override
    public void setBeanLocator(BeanLocator beanLocator) {
        beanLocatorSet.set(true);
    }
}
