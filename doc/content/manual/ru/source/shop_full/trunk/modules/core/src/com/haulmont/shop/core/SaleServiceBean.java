/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.shop.core;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.shop.core.app.SaleService;
import com.haulmont.shop.core.entity.Discount;
import com.haulmont.shop.core.entity.Sale;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sukhova
 * @version $Id$
 */
@Service(SaleService.NAME)
public class SaleServiceBean implements SaleService {

    @Inject
    private Persistence persistence;


    @Override
    public BigDecimal getPrice(Sale sale) {
        BigDecimal price = null;
        Transaction tx = persistence.createTransaction();

        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("select d from shop$Discount d where d.product.id = ?1 and d.buyer.id = ?2" +
                    " and d.fromDate<= ?3 and d.tillDate>= ?4 and d.minQuantity<=?5");
            query.setParameter(1, sale.getProduct());
            query.setParameter(2, sale.getBuyer());
            query.setParameter(3, sale.getOrderDate());
            query.setParameter(4, sale.getOrderDate());
            query.setParameter(5, sale.getQuantity());
            List<Discount> discounts = query.getResultList();
            if (discounts != null && !discounts.isEmpty()) {
                Discount discount = (Discount) query.getSingleResult();
                price = discount.getPrice().multiply(new BigDecimal(new Integer(sale.getQuantity())));
            } else {
                price = sale.getProduct().getPrice().multiply(new BigDecimal(new Integer(sale.getQuantity())));
            }
            tx.commit();
        } finally {
            tx.end();
        }

        return price;
    }
}
