import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import utils.ExcelExportUtils;
import utils.HttpUtils;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class View2Action implements java.awt.event.ActionListener {
    private static String JDququGouMiaoSha="http://api.m.jd.com/api?functionId=ChunXiaoGroupListService&appid=chunxiao&&body=";

    private static String JDququGouSort="http://api.m.jd.com/api?functionId=ChunXiaoGroupListService&appid=chunxiao&body=";
    private static String path = "C://wenAn/";


    public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static List<JDSort> list = new ArrayList();
    static{
        JDSort jdSort = new JDSort();
        jdSort.setCategoryId("");
        jdSort.setCategoryName("热卖爆款");
        list.add(jdSort);
        JDSort jdSort2 = new JDSort();
        jdSort2.setCategoryId("A002");
        jdSort2.setCategoryName("蔬菜豆制品");
        list.add(jdSort2);
        JDSort jdSort3 = new JDSort();
        jdSort3.setCategoryId("A001");
        jdSort3.setCategoryName("新鲜水果");
        list.add(jdSort3);
        JDSort jdSort4 = new JDSort();
        jdSort4.setCategoryId("A006");
        jdSort4.setCategoryName("冷冻冷藏");
        list.add(jdSort4);
        JDSort jdSort5 = new JDSort();
        jdSort5.setCategoryId("A005");
        jdSort5.setCategoryName("米面粮油");
        list.add(jdSort5);
        JDSort jdSort6 = new JDSort();
        jdSort6.setCategoryId("A003");
        jdSort6.setCategoryName("水产海鲜");
        list.add(jdSort6);
        JDSort jdSort7 = new JDSort();
        jdSort7.setCategoryId("A004");
        jdSort7.setCategoryName("肉禽蛋");
        list.add(jdSort7);
        JDSort jdSort8 = new JDSort();
        jdSort8.setCategoryId("A007");
        jdSort8.setCategoryName("休闲零食");
        list.add(jdSort8);
        JDSort jdSort9 = new JDSort();
        jdSort9.setCategoryId("A008");
        jdSort9.setCategoryName("酒水乳饮");
        list.add(jdSort9);
        JDSort jdSort0 = new JDSort();
        jdSort0.setCategoryId("A009");
        jdSort0.setCategoryName("日用百货");
        list.add(jdSort0);
    }

    /*
     * 这一部分是因为我把界面和动作分开成两个类来写了才需要进行对象的传递如果动作和界面在一个对象中写的话就不需要传入对象了
     */
    GuiOne d1=new GuiOne();//先定义一个View对象在切换界面的时候会使用到
    public View2Action(GuiOne v)//将外界的View对象传入这个类里
    {
        this.d1=v;
    }
    @Override
    public void actionPerformed(ActionEvent event)
    {

        //秒杀
        JDProductOut jdProductOut = miaoSha();

        String outFileString = outFile();
        LinkedHashMap headMap = headMap();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        FileOutputStream outFile = null;
        try {
            outFile = new FileOutputStream(outFileString);
            if(jdProductOut.getData().getGoodsList()!=null){
                List<JDProduct> goodsList = jdProductOut.getData().getGoodsList();
                ExcelExportUtils.getWorkbook(headMap,goodsList,workbook,"秒杀");
            }
            //分类
            for (JDSort jDSort: list) {
                JDProductOut sort = sort(jDSort);
                if(sort.getData().getGoodsList()!=null){
                    List<JDProduct> goodsList = sort.getData().getGoodsList();
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
        headMap.put("skuName","商品");
        headMap.put("price","价钱");
        headMap.put("skuPurchaseNumber","已售");
        headMap.put("storeCount","未售");
       return headMap;
    }


    private JDProductOut sort(JDSort jDSort) {
        Map params = new HashMap();
        Map headers = new HashMap();
        headers.put("content-type", "application/json");
        headers.put("referer", "https://servicewechat.com/wxc82d1114e718c544/55/page-frame.html");
        String body="{\"funName\":\"listCustomerFresh\",\"param\":{\"pageNo\":1,\"pageSize\":100,\"unionId\":9000001790,\"categoryId\":\"%s\",\"isMarketingLabel\":\"\"}}";
        body = String.format(body, jDSort.getCategoryId());
        //热卖参数不一样
        if(jDSort.getCategoryName().equals("热卖爆款")){
            body="{\"funName\":\"listCustomerFresh\",\"param\":{\"pageNo\":1,\"pageSize\":10,\"unionId\":9000001790,\"categoryId\":\"\",\"isMarketingLabel\":true}}";
        }
        try {
            body = URLEncoder.encode(body,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str = HttpUtils.get(JDququGouSort+body, params,headers,5000, 5000, "UTF-8");
        if(str == null){
            return null;
        }

        JDProductOut productOut = GSON.fromJson(str, JDProductOut.class);
        productOut.setName(jDSort.getCategoryName());
        return productOut;
    }


    public static JDProductOut miaoSha(){
        Map params = new HashMap();
        //params.put("phoneNo", "中文");
        //params.put("version", "1.0.5");
        Map headers = new HashMap();
        headers.put("content-type", "application/json");
        headers.put("referer", "https://servicewechat.com/wxc82d1114e718c544/55/page-frame.html");
        String body= "{\"funName\":\"listCustomerFresh\",\"param\":{\"pageNo\":1,\"pageSize\":100,\"skuType\":1,\"unionId\":\"9000001790\"}}";
        try {
            body = URLEncoder.encode(body,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //JDququGouMiaoSha = JDququGouMiaoSha+body;
        String str = HttpUtils.get(JDququGouMiaoSha+body, params,headers,5000, 5000, "UTF-8");
        if(str == null){
            return null;
        }

        JDProductOut productOut = GSON.fromJson(str, JDProductOut.class);
        return productOut;
    }




}

