package com.haulmont.shop.gui.ui.buyer;

public class BuyerEditor extends AbstractEditor<Buyer> {

    @Inject
    private Datasource<Buyer> buyerDs;

    @Inject
    private FieldGroup personalFieldGroup;

    @Override
    public void initItem(Buyer item) {
        super.initItem(item);

        /*Добавление слушателя, который следит
        * за изменением значения полей Имя и Фамилия
        * и формирует значение поля Полное имя*/
        buyerDs.addListener(new DsListenerAdapter() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                super.valueChanged((Entity) source, property, prevValue, value);
                if ("firstName".equals(property)) {
                    if (value != null) {
                        personalFieldGroup.setFieldValue("fullName", (personalFieldGroup.getFieldValue("surName") != null ? personalFieldGroup.getFieldValue("surName") : "") + " " + value);
                    }
                }
                if ("surName".equals(property)) {
                    if (value != null) {
                        personalFieldGroup.setFieldValue("fullName", value + " " + (personalFieldGroup.getFieldValue("firstName") != null ? personalFieldGroup.getFieldValue("firstName") : ""));
                    }
                }
            }
        });
    }
}
