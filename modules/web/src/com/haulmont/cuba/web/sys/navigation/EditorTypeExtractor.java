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

package com.haulmont.cuba.web.sys.navigation;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.screen.EditorScreen;
import org.springframework.core.ResolvableType;

import javax.annotation.Nullable;
import java.lang.reflect.Modifier;
import java.util.Optional;

public final class EditorTypeExtractor {

    private EditorTypeExtractor() {
    }

    @Nullable
    public static Class<? extends Entity> extractEntityClass(WindowInfo windowInfo) {
        return Optional.of(windowInfo)
                .map(WindowInfo::getControllerClass)
                .map(ResolvableType::forClass)
                .map(rt -> rt.as(EditorScreen.class))
                .map(rt -> rt.getGeneric(0))
                .map(ResolvableType::resolve)
                .flatMap(EditorTypeExtractor::asEntityClass)
                .orElse(null);
    }

    private static Optional<Class<? extends Entity>> asEntityClass(Class<?> cls) {
        if (!Entity.class.isAssignableFrom(cls)) {
            return Optional.empty();
        }
        int modifiers = cls.getModifiers();
        if (Modifier.isAbstract(modifiers)) {
            return Optional.empty();
        }
        if (Modifier.isInterface(modifiers)) {
            return Optional.empty();
        }
        return Optional.of((Class<? extends Entity>)cls);
    }

}
