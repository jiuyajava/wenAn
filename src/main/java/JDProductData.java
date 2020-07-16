import java.util.List;

public class JDProductData {

    private String apiEnum;
    private List<JDProduct> goodsList;

    public String getApiEnum() {
        return apiEnum;
    }

    public void setApiEnum(String apiEnum) {
        this.apiEnum = apiEnum;
    }

    public List<JDProduct> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<JDProduct> goodsList) {
        this.goodsList = goodsList;
    }
}
