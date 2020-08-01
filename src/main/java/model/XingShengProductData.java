package model;

import java.util.List;

public class XingShengProductData {

    private List<XingShengProduct> records;
    private List<String> spuSns;

    public List<XingShengProduct> getRecords() {
        return records;
    }

    public void setRecords(List<XingShengProduct> records) {
        this.records = records;
    }

    public List<String> getSpuSns() {
        return spuSns;
    }

    public void setSpuSns(List<String> spuSns) {
        this.spuSns = spuSns;
    }
}
