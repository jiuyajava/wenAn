public class JDProduct {

    private String skuName;
    private String price;
    private String skuPurchaseNumber;//已售
    private String storeCount;//未售


    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSkuPurchaseNumber() {
        return skuPurchaseNumber;
    }

    public void setSkuPurchaseNumber(String skuPurchaseNumber) {
        this.skuPurchaseNumber = skuPurchaseNumber;
    }

    public String getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(String storeCount) {
        this.storeCount = storeCount;
    }
}
