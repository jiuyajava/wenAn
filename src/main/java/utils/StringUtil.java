package utils;

public class StringUtil {


    public static String  addValue(String str){
        int index = str.lastIndexOf("0");
        int temp =0;

        //以0开头的
        if (str.startsWith("0")&&index!=-1){
            temp=index+1;
        }
        Integer integer = null;
        try {
            integer = Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return null;
        }
        integer++;
        String newNum = integer.toString();

        if (newNum.length()>str.length()-temp){
            temp--;
        }
        StringBuffer sb = new StringBuffer();
        if (temp!=0){
            for (int i =0 ;i<temp ;i++){
                sb.append("0");
            }
        }
        sb.append(newNum);
        return sb.toString();
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     * @param code
     * @return
     */
    public static String autoGenericCode(String code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Integer.parseInt(code) + 1);

        return result;
    }

}
