

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.out;

/**
 * 在项目指定目录下建一个Java文件，并写入内容
 * @author jiuya
 */
public class FileWriters {

    /**
     * 写入txt文件
     *
     * @param result
     * @param filePath
     * @return
     */
    public static boolean save(String result, String filePath) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        String str = sdf.format(d);
        String relFilePath = filePath + str +".txt";
        System.out.println("文案所在目录 :" + relFilePath);
        StringBuffer content = new StringBuffer();
        boolean flag = false;
        BufferedWriter out = null;
        try {
                File pathFile = new File(filePath);
                if(!pathFile.exists()){
                    pathFile.mkdirs();
                }
                File file = new File(relFilePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
//                //标题头
//                out.write("curr_time,link_id,travel_time,speed,reliabilitycode,link_len,adcode,time_stamp,state,public_rec_time,ds");
//                out.newLine();

                out.write(result);
                out.newLine();

                flag = true;
            } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();

    } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return flag;
        }
    }
}