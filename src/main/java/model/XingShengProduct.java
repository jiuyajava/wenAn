package model;

import java.util.List;

public class XingShengProduct {

    private String prName;
    private String spuSn;
    private String saleAmt;
    private String limitQty;
    private String saleQty;

    public String getSpuSn() {
        return spuSn;
    }

    public void setSpuSn(String spuSn) {
        this.spuSn = spuSn;
    }

    public String getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(String saleQty) {
        this.saleQty = saleQty;
    }

    public String getPrName() {
        return prName;
    }

    public void setPrName(String prName) {
        this.prName = prName;
    }

    public String getSaleAmt() {
        return saleAmt;
    }

    public void setSaleAmt(String saleAmt) {
        this.saleAmt = saleAmt;
    }

    public String getLimitQty() {
        return limitQty;
    }

    public void setLimitQty(String limitQty) {
        this.limitQty = limitQty;
    }
}
