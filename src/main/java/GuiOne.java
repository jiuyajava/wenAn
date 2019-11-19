
import java.awt.*;


import javax.swing.*;


public class GuiOne extends JFrame{

    // 定义组件
    JPanel jp1, jp2, jp3;
    JLabel jlb1, jlb2;
    JButton jb1, jb2;
    JTextField jtf1;
    JPasswordField jpf1;
    JTextArea ta;
    JComboBox jcb1;


    // 构造函数
    public GuiOne() {

        jp1 = new JPanel();
        jp3 = new JPanel();

        String [] ct= {"闹钟"};	//创建
        jcb1=new JComboBox(ct);		//添加到下拉框中

        jlb1 = new JLabel("key");

        jb1 = new JButton("提交");

        jtf1 = new JTextField(22);

        jpf1 = new JPasswordField(10);// 设置布局管理(上面忘记：extends JFrame，这里出错了)
        this.setLayout(new GridLayout(3, 1));

        //文本域
        ta = new JTextArea();
        ta.setLineWrap(true);
        ta.setBounds(10, 30, 100, 80);


        // 加入各个组件
        jp1.add(jlb1);
        jp1.add(jtf1);
        jp1.add(jcb1);

        jp3.add(jb1);



        // 加入到JFrame
        this.add(jp1);
        this.add(jp3);
        this.add(ta);




        this.setSize(500, 300);
        this.setTitle("登录");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
