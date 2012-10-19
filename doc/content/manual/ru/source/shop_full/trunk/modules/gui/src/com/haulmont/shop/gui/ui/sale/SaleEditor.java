/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.shop.gui.ui.sale;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.shop.core.app.SaleService;
import com.haulmont.shop.core.entity.Sale;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author sukhova
 * @version $Id$
 */
public class SaleEditor extends AbstractEditor<Sale> {

    @Inject
    private Datasource<Sale> saleDs;

    @Inject
    private FieldGroup fieldGroup;

    @Inject
    private SaleService shop_SaleService;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }


    public void calcPrice() {
        Sale sale = saleDs.getItem();
        if ((sale.getBuyer() == null) || (sale.getProduct() == null) || (sale.getOrderDate() == null) || (sale.getQuantity() == null)) {
            showNotification(getMessage("notificationMessage"),
                    NotificationType.HUMANIZED);
        } else {
            BigDecimal price = null;
            try {
                price = shop_SaleService.getPrice(sale);
                sale.setPrice(price);
            } catch (Exception e) {
            }
        }
    }
}
