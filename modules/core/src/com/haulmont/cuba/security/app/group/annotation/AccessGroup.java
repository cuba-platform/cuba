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

package com.haulmont.cuba.security.app.group.annotation;

import com.haulmont.cuba.security.app.group.AnnotatedAccessGroupDefinition;
import com.haulmont.cuba.security.group.AccessGroupDefinition;
import com.haulmont.cuba.security.role.SecurityStorageMode;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class is a access group. The access group will be available in the user interface
 * if the application is running in the corresponding mode (the app property {@code cuba.accessGroupsStorageMode}
 * equals {@code SOURCE_CODE} or {@code MIXED}).
 *
 * <p>The easiest way to determine the access group in the application source code is to extend
 * your class from {@link AnnotatedAccessGroupDefinition}
 * and mark it with this annotation. Usage example:
 *
 * <pre>
 * &#064;AccessGroup(name = "MyFirstAccessGroup", parent = ParentAccessGroup.class)
 * public class MyFirstAccessGroup extends AnnotatedAccessGroupDefinition {
 *
 *     &#064;JpqlConstraint(target = SomeEntity.class, where = "{E}.attr = true")
 *     &#064;Override
 *     public SetOfAccessConstraints accessConstraints() {
 *         return super.accessConstraints();
 *     }
 *
 *     &#064;Constraint(operations = {EntityOp.READ, EntityOp.UPDATE})
 *     &#064;JpqlConstraint(where = "{E}.active = true")
 *     public boolean userConstraints(User user) {
 *         return Boolean.TRUE.equals(user.getActive());
 *     }
 *
 *     &#064;SessionAttribute(name = "key1", value = "value1")
 *     &#064;Override
 *     public Map&lt;String, Serializable&gt; sessionAttributes() {
 *         return super.sessionAttributes();
 *     }
 * }
 * </pre>
 *
 * @see SecurityStorageMode
 * @see JpqlConstraint
 * @see Constraint
 * @see CustomConstraint
 * @see SessionAttribute
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface AccessGroup {

    /**
     * Access group name
     */
    String name();

    /**
     * Access group parent name
     */
    Class<? extends AccessGroupDefinition> parent() default AccessGroupDefinition.class;

    @AliasFor(annotation = Component.class)
    String value() default "";
}
