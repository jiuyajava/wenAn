import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.XingShengProduct;
import model.XingShengProductOut;
import model.XingShengProductSpuSnsOut;
import model.saleQty;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import utils.ExcelExportUtils;
import utils.HttpUtils;

import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class View3Action implements java.awt.event.ActionListener {
//    private static String JDququGouMiaoSha="http://api.m.jd.com/api?functionId=ChunXiaoGroupListService&appid=chunxiao&&body=";

    private static String XingShengququGouSort="https://mall.xsyxsc.com/user/window/getProducts/v3";
    //已售是单独拉的
    private static String XingShengSpuSns="https://mall.xsyxsc.com/user/product/getProductsMarketingData/v3";
    private static String path = "C://wenAn/";
    private static String apisign = "b83906b3d0cfabf828a6b0613ac7a98f";


    public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static List<JDSort> list = new ArrayList();
    static{
        //        "windowId": 454,
        //        "windowName": "今日新品",
        JDSort jdSort = new JDSort();
        jdSort.setCategoryId("454");
        jdSort.setCategoryName("今日新品");
        list.add(jdSort);
        JDSort jdSort2 = new JDSort();
        jdSort2.setCategoryId("73");
        jdSort2.setCategoryName("水果");
        list.add(jdSort2);
        JDSort jdSort3 = new JDSort();
        jdSort3.setCategoryId("74");
        jdSort3.setCategoryName("蔬菜");
        list.add(jdSort3);
        JDSort jdSort4 = new JDSort();
        jdSort4.setCategoryId("76");
        jdSort4.setCategoryName("肉蛋水产");
        list.add(jdSort4);
        JDSort jdSort5 = new JDSort();
        jdSort5.setCategoryId("77");
        jdSort5.setCategoryName("佐餐食材");
        list.add(jdSort5);
        JDSort jdSort6 = new JDSort();
        jdSort6.setCategoryId("78");
        jdSort6.setCategoryName("粮油调味");
        list.add(jdSort6);
        JDSort jdSort7 = new JDSort();
        jdSort7.setCategoryId("79");
        jdSort7.setCategoryName("餐包烘焙");
        list.add(jdSort7);
        JDSort jdSort8 = new JDSort();
        jdSort8.setCategoryId("80");
        jdSort8.setCategoryName("酒水乳饮");
        list.add(jdSort8);
        JDSort jdSort9 = new JDSort();
        jdSort9.setCategoryId("81");
        jdSort9.setCategoryName("休闲零食");
        list.add(jdSort9);
        JDSort jdSort0 = new JDSort();
        jdSort0.setCategoryId("82");
        jdSort0.setCategoryName("家居百货");
        list.add(jdSort0);
        JDSort jdSort11 = new JDSort();
        jdSort11.setCategoryId("83");
        jdSort11.setCategoryName("个护母婴");
        list.add(jdSort11);
    }

    /*
     * 这一部分是因为我把界面和动作分开成两个类来写了才需要进行对象的传递如果动作和界面在一个对象中写的话就不需要传入对象了
     */
    GuiOne d1=new GuiOne();//先定义一个View对象在切换界面的时候会使用到
    public View3Action(GuiOne v)//将外界的View对象传入这个类里
    {
        this.d1=v;
    }
    @Override
    public void actionPerformed(ActionEvent event)
    {
        String token  = d1.jtf1.getText();
        if( token!=null && !"".equals(token)){
            apisign = token;
        }
        String outFileString = outFile();
        LinkedHashMap headMap = headMap();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        FileOutputStream outFile = null;
        try {
            outFile = new FileOutputStream(outFileString);
            //分类
            for (JDSort jDSort: list) {
                XingShengProductOut sort = sort(jDSort);
                if(sort.getData().getRecords()!=null){
                    //已售
                    String categoryName = jDSort.getCategoryName();
                    if(categoryName.equals("家居百货")){
                        System.out.println();
                    }
                    List<String> spuSns = sort.getData().getSpuSns();
                    if(sort.getData().getSpuSns().size()>30){
                        spuSns = spuSns.subList(0, 30);
                    }
                    XingShengProductSpuSnsOut xingShengProductSpuSnsOut = saleQtySort(spuSns);
                    List<XingShengProduct> goodsList = sort.getData().getRecords();
                    goodsList.stream().forEach(t->{
                        List<saleQty> data = xingShengProductSpuSnsOut.getData();
                        if(data!=null){
                            for (saleQty saleQtyT: data) {
                                if(saleQtyT.getSpuSn().equals(t.getSpuSn())){
                                    t.setSaleQty(saleQtyT.getSaleQty());
                                }
                            }
                        }
                    });
                    ExcelExportUtils.getWorkbook(headMap,goodsList,workbook,jDSort.getCategoryName());
                }
            }
            workbook.write(outFile);
            outFile.flush();
            d1.ta.append("成功 : "+outFileString+"\n");
        } catch (Exception e) {
            d1.ta.append("失败 : "+outFileString+"\n");
            e.printStackTrace();
        }finally {
            try {
                if (outFile != null) {
                    outFile.close();
                }
            } catch (IOException e) {
                //log.error(e.getMessage(), e);
            }
        }

    }


    private String outFile() {
        try {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
            String str = sdf.format(d);
            String relFilePath = path + str +".xlsx";
            return relFilePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private LinkedHashMap headMap() {
        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        headMap.put("prName","商品");
        headMap.put("saleAmt","价钱");
        headMap.put("saleQty","已售");
        headMap.put("limitQty","限量");
       return headMap;
    }


    private XingShengProductOut sort(JDSort jDSort) {
        Map params = new HashMap();
        params.put("windowId", jDSort.getCategoryId());
        Map headers = new HashMap();
        headers.put("content-type", "application/json");
        long Time = System.currentTimeMillis();
        headers.put("api-timestamp", Time+"");
        headers.put("api-sign", apisign);
        headers.put("accept", "application/json, text/plain, */*");
        headers.put("api-version", "V3");

        String str = HttpUtils.xingshengPost(XingShengququGouSort, params,headers,5000, 5000, "UTF-8");
        if(str == null){
            return null;
        }
        XingShengProductOut productOut = GSON.fromJson(str, XingShengProductOut.class);
        productOut.setName(jDSort.getCategoryName());
        return productOut;
    }

    private XingShengProductSpuSnsOut saleQtySort(List<String> list) {
        Map params = new HashMap();
        params.put("spuSns", list);
        Map headers = new HashMap();
        headers.put("content-type", "application/x-www-form-urlencoded");
        long Time = System.currentTimeMillis();
        headers.put("api-timestamp", Time+"");
        headers.put("api-sign", apisign);
        headers.put("accept", "application/json, text/plain, */*");
        headers.put("api-version", "V3");

        String str = HttpUtils.xingshengPost(XingShengSpuSns, params,headers,5000, 5000, "UTF-8");

        if(str == null){
            return null;
        }
        XingShengProductSpuSnsOut productOut = GSON.fromJson(str, XingShengProductSpuSnsOut.class);
        //productOut.setName(jDSort.getCategoryName());
        return productOut;
    }





}

