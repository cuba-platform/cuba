/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.shop.gui.ui.buyer;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.shop.core.entity.Buyer;
import com.haulmont.shop.core.entity.PersonalData;

/**
 * @author sukhova
 * @version $Id$
 */
public class BuyerEditor  extends AbstractEditor<Buyer> {

    @Override
    public void initItem(Buyer item) {
        if (PersistenceHelper.isNew(item)){
            item.setPersonalData(new PersonalData());
        }
    }
}
