import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.HttpUtils;

import java.awt.event.ActionEvent;
import java.util.*;
import java.util.stream.Collectors;

public class View1Action implements java.awt.event.ActionListener {
    private static String miaoSha="https://bell-mall.yunshanmeicai.com/mall/seckill";
    private static String SessionKey="265cd49b2980a356afbe01cbf31a09ee";
    private static String bellToken="d5169ea86e7b7495e5b60bc45a29d9e6626af75d-3";

    public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    /*
     * 这一部分是因为我把界面和动作分开成两个类来写了才需要进行对象的传递如果动作和界面在一个对象中写的话就不需要传入对象了
     */
    GuiOne d1=new GuiOne();//先定义一个View对象在切换界面的时候会使用到
    public View1Action(GuiOne v)//将外界的View对象传入这个类里
    {
        this.d1=v;
    }
    @Override
    public void actionPerformed(ActionEvent event)
    {

        String token  = d1.jtf1.getText();
        String key = d1.jtf2.getText();
        if(key!=null && !"".equals(key) && token!=null && !"".equals(token)){
            SessionKey = key;
            bellToken = token;
        }
        if(SessionKey == null || "".equals(SessionKey)){
            return;
        }
        //获取时间
        Boolean flag = true;
        int i=0;
        ProductOut productOut1 = new ProductOut();
        while (flag){
            productOut1 = miaoSha("",d1);
            if(productOut1!=null && productOut1.getData() != null){
                flag = false;
            }
            i++;
            if(i>10){
                d1.ta.append("=======key失效, 联系九涯============\n");
                return;
            }
        }

        //秒杀文案生成
        miaoShaWenAn(productOut1,d1);
        //爆品文案
        //baoPinWenAn(d1);

    }

    private static void miaoShaWenAn(ProductOut productOut1,GuiOne d1) {
        List<Time> aClass = productOut1.getData().getCLASS();
        List<Product> products =new ArrayList<>();
        for (Time time: aClass ) {
            ProductOut productOut = new ProductOut();
            for (int i = 0; i < 5; i++) {
                productOut = miaoSha(time.getStart_time(),d1);
                if(productOut.getData() != null){
                    break;
                }
            }
            //System.out.println(time.getName() +" : "+ productOut.getData());
            if(productOut.getData() != null && productOut.getData().getList() != null){
                products.addAll(productOut.getData().getList());
            }
            //products.addAll(productOut.getData().getList());
        }

        Map<String, List<Product>> collect = products.stream().collect(Collectors.groupingBy(Product::getStart_time));


        //按时间重排序
        Map map = paiXu(collect);

        String assemble = assemble(map, aClass,d1);
        //System.out.println(path);
        String path = "C://wenAn/";

        FileWriters.save(assemble,path);
        //d1.ta.setText("");
        d1.ta.append("=======成功============\n");
        //System.out.println("=======成功============");

    }


    private static Map paiXu(Map<String, List<Product>> collect) {
        List<Map.Entry<String,List<Product>>> entrys=new ArrayList<>(collect.entrySet());
        //2：调用Collections.sort(list,comparator)方法把Entry-list排序
        Collections.sort(entrys, new MyComparator());
        //3：遍历排好序的Entry-list，可得到按顺序输出的结果
        TreeMap<String, List<Product>> listTreeMap = new TreeMap<>();
        for(Map.Entry<String,List<Product>> entry:entrys){
            listTreeMap.put(entry.getKey(),entry.getValue());
        }
        return listTreeMap;
    }


    //组装数据
    private static String assemble(Map<String, List<Product>> collect, List<Time> aClass,GuiOne d1) {
        String selectedItem =(String) d1.jcb1.getSelectedItem();
        TemplateEnum template = TemplateEnum.getName(selectedItem);

        String tou = template.getStart() ;
        String wei = template.getEnding();
        String centered = template.getCentered();

        String wenAn ="";
        for(Map.Entry<String,  List<Product>> entry : collect.entrySet()) {

            String key = entry.getKey();
            String time = getTime(key, aClass);
            String touT = String.format(tou, time);
            List<Product> list = entry.getValue();
            int size = list.size() > 8 ? 8 : list.size();
            String text ="";
            for (int i = 0; i < size; i++) {
                text += list.get(i).getProduct_name() + "\n" +
                        String.format(centered,list.get(i).getProduct_price());

            }

            wenAn += touT + text + wei + "\n ===================================== \n ";
        }
        return wenAn;
    }

    //获取, 时间戳对应的时间
    public static String getTime(String key ,List<Time> aClass){
        for (Time time: aClass ) {
            if(time.getStart_time().equals(key)){
                return time.getName();
            }
        }
        return "全天";
    }


    public static ProductOut miaoSha(String start_time,GuiOne d1 ){
        Map params = new HashMap();
        //params.put("phoneNo", "中文");
        //params.put("version", "1.0.5");
        params.put("start_time", start_time);
        Map headers = new HashMap();
        headers.put("X-Bell-Token",bellToken);
        headers.put("X-Group-Token","216151");
        headers.put("X-Session-Key",SessionKey);
        headers.put("content-type", "application/json");
        String str = HttpUtils.post(miaoSha, params,headers,5000, 5000, "UTF-8");
        if(str == null){
            return null;
        }
        str = str.replaceAll("class", "CLASS");
//        if(str.contains("登录过期 请重新登录1028")){
//            d1.ta.append("=======key失效, 联系九涯============\n");
//        }
        ProductOut productOut = GSON.fromJson(str, ProductOut.class);
        return productOut;
    }


    static class MyComparator implements Comparator<Map.Entry>{
        @Override
        public int compare(Map.Entry o1, Map.Entry o2) {
            return ((String)o1.getKey()).compareTo((String)o2.getKey());
        }
    }


}

