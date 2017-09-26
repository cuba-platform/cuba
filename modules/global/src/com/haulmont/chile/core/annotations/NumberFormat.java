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

package com.haulmont.chile.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a format {@link #pattern()} and optional {@link #decimalSeparator()} and {@link #groupingSeparator()} for
 * an entity attribute of the {@link Number} type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface NumberFormat {

	/**
	 * Pattern as described for {@link java.text.DecimalFormat}.
	 */
	String pattern();

	/**
	 * Decimal separator. If not specified, will be obtained from {@link com.haulmont.chile.core.datatypes.FormatStrings}
     * for locale-dependent formatting, or from server default locale for locale-independent formatting.
	 */
	String decimalSeparator() default "";

    /**
   	 * Grouping separator. If not specified, will be obtained from {@link com.haulmont.chile.core.datatypes.FormatStrings}
     * for locale-dependent formatting, or from server default locale for locale-independent formatting.
   	 */
	String groupingSeparator() default "";
}
