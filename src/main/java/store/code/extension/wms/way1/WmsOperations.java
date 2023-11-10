package store.code.extension.wms.way1;

import java.util.List;

public interface WmsOperations {

    void submitProducts(List<?> products);

    List<Object> getProducts(Object productQuery);

    void submitStock(Object stock);

    List<Object> getStocks(Object stockQuery);

    void submitOrder(Object order);

    void changeReceiverAddress(Object receiverAddress);

    void refundOrder(Object order);

    List<Object> getOrders(Object orderQuery);

    List<Object> getRefundOrders(Object refundOrderQuery);

    List<Object> getReturnChangeOrders(Object changeOrderQuery);

}
