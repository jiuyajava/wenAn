
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class meiJia {
   // private static String dengLu="https://bell-mall.yunshanmeicai.com/login/wechat";
    private static String miaoSha="https://bell-mall.yunshanmeicai.com/mall/seckill";
    private static String SessionKey="";

    public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static void main(String[] args) {

        GuiOne d1 = new GuiOne();

        d1.jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SessionKey = d1.jtf1.getText();
                if(SessionKey == null || "".equals(SessionKey)){
                    return;
                }
                //获取时间
                ProductOut productOut1 = miaoSha("");
                if( productOut1.getData() == null){
                    //d1.ta.setText("");
                    d1.ta.append("=======key失效, 联系九涯============\n");
                    return;
                }

                //秒杀文案生成
                miaoShaWenAn(productOut1,d1);
                //爆品文案
                baoPinWenAn(d1);

            }

        });


    }

    private static void baoPinWenAn(GuiOne d1) {



    }

    private static void miaoShaWenAn(ProductOut productOut1,GuiOne d1) {
        List<Time> aClass = productOut1.getData().getCLASS();
        List<Product> products =new ArrayList<>();
        for (Time time: aClass ) {
            ProductOut productOut = miaoSha(time.getStart_time());
            products.addAll(productOut.getData().getList());
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


    public static ProductOut miaoSha(String start_time){
        Map params = new HashMap();
        params.put("phoneNo", "中文");
        params.put("start_time", start_time);
        Map headers = new HashMap();
        headers.put("X-Group-Token","216151");
        //headers.put("X-Bell-Token","a0bb9c8ece91d6abe067d2b4960b50f9ef768724-3");
        headers.put("X-Session-Key",SessionKey);
        String str = HttpUtils.post(miaoSha, params,headers,9000, 9000, "UTF-8");
        str = str.replaceAll("class", "CLASS");
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