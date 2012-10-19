/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.shop.core.app;

import com.haulmont.shop.core.entity.Sale;

import java.math.BigDecimal;

/**
 * @author sukhova
 * @version $Id$
 */
public interface SaleService {
    String NAME = "shop_SaleService";

    public BigDecimal getPrice(Sale sale);
}
