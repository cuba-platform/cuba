/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.core.sys.persistence.mapping;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.entity.SoftDelete;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.springframework.stereotype.Component;

import javax.persistence.OneToOne;
import java.lang.reflect.Field;

/**
 * Generates expression to support soft delete feature in CUBA.
 */
@Component("cuba_SoftDeleteJoinExpressionProvider")
public class SoftDeleteJoinExpressionProvider extends AbstractJoinExpressionProvider {

    @Override
    protected Expression processOneToManyMapping(OneToManyMapping mapping) {
        if (SoftDelete.class.isAssignableFrom(mapping.getReferenceClass())) {
            return new ExpressionBuilder().get("deleteTs").isNull();
        }
        return null;
    }

    @Override
    protected Expression processOneToOneMapping(OneToOneMapping mapping) {
        ClassDescriptor descriptor = mapping.getDescriptor();
        Field referenceField = FieldUtils.getAllFieldsList(descriptor.getJavaClass())
                .stream().filter(f -> f.getName().equals(mapping.getAttributeName()))
                .findFirst().orElse(null);
        if (SoftDelete.class.isAssignableFrom(mapping.getReferenceClass()) && referenceField != null) {
            OneToOne oneToOne = referenceField.getAnnotation(OneToOne.class);
            if (oneToOne != null && !Strings.isNullOrEmpty(oneToOne.mappedBy())) {
                return new ExpressionBuilder().get("deleteTs").isNull();
            }
        }
        return null;
    }

    @Override
    protected Expression processManyToOneMapping(ManyToOneMapping mapping) {
        return null;
    }

    @Override
    protected Expression processManyToManyMapping(ManyToManyMapping mapping) {
        return null;
    }
}
