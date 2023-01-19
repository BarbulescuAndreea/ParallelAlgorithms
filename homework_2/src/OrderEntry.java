public class OrderEntry {
    private final String orderID;
    private final int productCount;

    public OrderEntry(String orderID, int productCount) {
        this.orderID = orderID;
        this.productCount = productCount;
    }

    public String getOrderID() {
        return orderID;
    }

    public int getProductCount() {
        return productCount;
    }
}
