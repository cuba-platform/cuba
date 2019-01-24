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

package com.haulmont.cuba.portal.sys.thymeleaf;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Nonnull;

@Component(ThymeleafViewsCacheInvalidation.NAME)
public class ThymeleafViewsCacheInvalidationBean implements ThymeleafViewsCacheInvalidation, ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void clearViewsCache() {
        ThymeleafViewResolver viewResolver = applicationContext.getBean(ThymeleafViewResolver.class);
        viewResolver.clearCache();
        viewResolver.getTemplateEngine().getConfiguration().getTemplateManager().clearCaches();
    }
}