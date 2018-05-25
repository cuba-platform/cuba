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

package com.haulmont.cuba.core.app.importexport;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CollectionCompare {
    protected Consumer<Entity> createConsumer;
    protected Consumer<Entity> deleteConsumer;
    protected BiConsumer<Entity, Entity> updateConsumer;

    private CollectionCompare() {
    }

    public static CollectionCompare with() {
        return new CollectionCompare();
    }

    public CollectionCompare onCreate(Consumer<Entity> createConsumer) {
        this.createConsumer = createConsumer;
        return this;
    }

    public CollectionCompare onDelete(Consumer<Entity> deleteConsumer) {
        this.deleteConsumer = deleteConsumer;
        return this;
    }

    public CollectionCompare onUpdate(BiConsumer<Entity, Entity> updateConsumer) {
        this.updateConsumer = updateConsumer;
        return this;
    }

    public void compare(Collection<Entity> src, Collection<Entity> dst) {
        final Collection<Entity> srcNN = Optional.ofNullable(src)
                .orElse(Collections.emptyList());
        final Collection<Entity> dstNN = Optional.ofNullable(dst)
                .orElse(Collections.emptyList());
        for (Entity srcEntity : srcNN) {
            Optional<Entity> existingOptional = dstNN.stream()
                    .filter(e -> Objects.equals(e, srcEntity))
                    .findFirst();
            if (existingOptional.isPresent()) {
                updateConsumer.accept(srcEntity, existingOptional.get());
            } else {
                createConsumer.accept(srcEntity);
            }
        }
        dstNN.stream().filter(item -> !srcNN.contains(item)).forEach(deleteConsumer);
    }
}
