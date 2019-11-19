



public enum  TemplateEnum {
    NaoZhong("闹钟","⏰%s点预告⏰\n","更多好物详见商城 ～","❗️%s元\n\n"),
    AiXin("爱心","❤%s点预告❤\n","更多超值好物详见商城 ～","\uD83D\uDCE2%s元\n\n"),

    ;


    private String name; //类型
    private String Start;//开头
    private String Ending;//结尾
    private String Centered;//中间

    TemplateEnum(String name, String start, String ending, String centered) {
        this.name = name;
        Start = start;
        Ending = ending;
        Centered = centered;
    }

    // 普通方法
    public static TemplateEnum getName(String name) {
        for (TemplateEnum c : TemplateEnum.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getEnding() {
        return Ending;
    }

    public void setEnding(String ending) {
        Ending = ending;
    }

    public String getCentered() {
        return Centered;
    }

    public void setCentered(String centered) {
        Centered = centered;
    }


//    public static void main(String[] args) {
//
//        TemplateEnum name = TemplateEnum.getName("闹钟");
//        System.out.println();
//    }

}
