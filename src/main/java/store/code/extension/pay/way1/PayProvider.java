package store.code.extension.pay.way1;

public interface PayProvider {

    OrderPayResult payOrder(OrderPayModel orderPayModel);

    OrderQueryResult queryOrder(OrderQueryModel orderQueryModel);

    OrderCloseResult closeOrder(OrderCloseModel orderCloseModel);

    PayNotifyResult payNotify(PayNotifyModel payNotifyModel);

}
