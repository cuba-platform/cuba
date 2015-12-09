/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;

/**
 * @author gaslov
 * @version $Id$
 */
public interface ConditionDescriptorsTreeBuilderAPI {

    String NAME = "cuba_ConditionDescriptorsTreeBuilder";

    Tree<AbstractConditionDescriptor> build();
}
