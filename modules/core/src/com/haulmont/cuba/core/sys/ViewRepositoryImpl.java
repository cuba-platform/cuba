/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.ViewRepository;

import javax.annotation.ManagedBean;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(ViewRepository.NAME)
public class ViewRepositoryImpl extends AbstractViewRepository implements ViewRepository {
}
