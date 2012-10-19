/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.shop.gui.ui.salesperson;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.shop.core.entity.PersonalData;
import com.haulmont.shop.core.entity.SalesPerson;

/**
 * @author sukhova
 * @version $Id$
 */
public class SalesPersonEditor extends AbstractEditor<SalesPerson> {

    @Override
    public void initItem(SalesPerson item) {
        if (PersistenceHelper.isNew(item)){
            item.setPersonalData(new PersonalData());
        }
    }
}
