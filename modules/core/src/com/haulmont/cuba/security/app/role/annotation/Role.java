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

package com.haulmont.cuba.security.app.role.annotation;

import com.haulmont.cuba.security.app.role.AnnotatedRoleDefinition;
import com.haulmont.cuba.security.entity.SecurityScope;
import com.haulmont.cuba.security.role.SecurityStorageMode;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class is a "Role". Such a role will be available in the user interface
 * if the application is running in the corresponding mode (the app property {@code cuba.rolesStorageMode}
 * equals {@code SOURCE_CODE} or {@code MIXED}).
 *
 * <p>The easiest way to determine the role in the application source code is to extend
 * your class from {@link AnnotatedRoleDefinition AnnotatedRoleDefinition}
 * and mark it with this annotation. Usage example:
 *
 * <pre>
 *     &#064;Role(name = "My first role", isDefault = true)
 * public class MyFirstRole extends AnnotatedRoleDefinition {
 *
 *     &#064;EntityAccess(entityClass = SomeEntity.class,
 *             operations = {EntityOp.DELETE, EntityOp.UPDATE})
 *     &#064;Override
 *     public EntityPermissionsContainer entityPermissions() {
 *         return super.entityPermissions();
 *     }
 *
 *     &#064;EntityAttributeAccess(entityClass = SomeEntity.class, view = {"someAttribute", "attr2"},
 *             modify = {"attr3", "attr4"})
 *     &#064;Override
 *     public EntityAttributePermissionsContainer entityAttributePermissions() {
 *         return super.entityAttributePermissions();
 *     }
 *
 *     &#064;SpecificAccess(permissions = {"my.specific.permission1", "my.specific.permission2"})
 *     &#064;Override
 *     public SpecificPermissionsContainer specificPermissions() {
 *         return super.specificPermissions();
 *     }
 *
 *     &#064;ScreenAccess(screenIds = {"myapp_SomeEntity.edit", "myapp_OtherEntity.browse"})
 *     &#064;Override
 *     public ScreenPermissionsContainer screenPermissions() {
 *         return super.screenPermissions();
 *     }
 *
 *     &#064;ScreenComponentAccess(screenId = "myapp_SomeEntity.browse", deny = {"someGroupBox"})
 *     &#064;Override
 *     public ScreenComponentPermissionsContainer screenComponentPermissions() {
 *         return super.screenComponentPermissions();
 *     }
 * }
 * </pre>
 *
 * @see SecurityStorageMode
 * @see EntityAccess
 * @see EntityAttributeAccess
 * @see ScreenAccess
 * @see ScreenComponentAccess
 * @see SpecificAccess
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Role {

    /**
     * Role name.
     */
    String name();

    /**
     * Description of the role.
     */
    String description() default "";

    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * Determines if the role is default.
     */
    boolean isDefault() default false;

    /**
     * Determines if the role is super.
     */
    boolean isSuper() default false;

    /**
     * Determines security scope for the role.
     */
    String securityScope() default SecurityScope.DEFAULT_SCOPE_NAME;
}
