package model;

import java.util.List;

public class XingShengProductSpuSnsOut {

    private List<saleQty> data;
    private String success;

    public List<saleQty> getData() {
        return data;
    }

    public void setData(List<saleQty> data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
