
import java.awt.*;


import javax.swing.*;


public class GuiOne extends JFrame{

    // 定义组件
    JPanel jp1, jp2, jp3;
    JLabel jlb1, jlb2;
    JButton jb1, jb2;
    JTextField jtf1,jtf2;
    JPasswordField jpf1;
    JTextArea ta;
    JComboBox jcb1;
    JScrollPane scroll;

    // 构造函数
    public void View() {
        this.setTitle("我爱你");
        jp1 = new JPanel();
        ViewAction exwpAction=new ViewAction(this);
        String [] ct= {"请选择","美菜文案","京东区区购数据","兴盛优选"};	//创建
        jcb1=new JComboBox(ct);		//添加到下拉框中
        jp1.add(jcb1);
        jcb1.addActionListener(exwpAction);
        // 加入到JFrame
        this.add(jp1);
        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * 美家文案生成
     */
    public void View1() {
        this.setTitle("文案生成");
        View1Action exwpAction=new View1Action(this);
        jp1 = new JPanel();
        jp3 = new JPanel();

        String [] ct= {"闹钟","爱心"};	//创建
        jcb1=new JComboBox(ct);		//添加到下拉框中

        jlb1 = new JLabel("X-Bell-Token");
        jlb2 = new JLabel("X-Session-Key");

        jb1 = new JButton("提交");
        jb1.addActionListener(exwpAction);
        jtf1 = new JTextField(22);
        jtf2 = new JTextField(22);

        jpf1 = new JPasswordField(10);// 设置布局管理(上面忘记：extends JFrame，这里出错了)
        this.setLayout(new GridLayout(3, 1));

        //文本域
        ta = new JTextArea();
        //ta.setLineWrap(true);
        ta.setBounds(10, 30, 100, 150);
        scroll = new JScrollPane(ta);

        // 加入各个组件
        jp1.add(jlb1);
        jp3.add(jlb2);
        jp1.add(jtf1);
        jp3.add(jtf2);

        jp1.add(jcb1);

        jp3.add(jb1);



        // 加入到JFrame
        this.add(jp1);
        this.add(jp3);
        this.add(scroll);
        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * 京东区区购
     */
    public void View2() {
        this.setTitle("京东区区购");
        View2Action exwpAction=new View2Action(this);
        jp1 = new JPanel();
        jp3 = new JPanel();

        String [] ct= {"闹钟","爱心"};	//创建
        //jcb1=new JComboBox(ct);		//添加到下拉框中

        //jlb1 = new JLabel("X-Bell-Token");
        //jlb2 = new JLabel("X-Session-Key");

        jb1 = new JButton("提交");
        jb1.addActionListener(exwpAction);
        //jtf1 = new JTextField(22);
        //jtf2 = new JTextField(22);

        jpf1 = new JPasswordField(10);// 设置布局管理(上面忘记：extends JFrame，这里出错了)
        this.setLayout(new GridLayout(3, 1));

        //文本域
        ta = new JTextArea();
        //ta.setLineWrap(true);
        ta.setBounds(10, 30, 100, 150);
        scroll = new JScrollPane(ta);

        // 加入各个组件
        //jp1.add(jlb1);
        //jp3.add(jlb2);
        //jp1.add(jtf1);
        //jp3.add(jtf2);

        //jp1.add(jcb1);

        jp3.add(jb1);



        // 加入到JFrame
        this.add(jp1);
        this.add(jp3);
        this.add(scroll);

        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    /**
     * 兴盛优选生成
     */
    public void View3() {
        this.setTitle("兴盛优选生成");
        View3Action exwpAction=new View3Action(this);
            jp1 = new JPanel();
            jp3 = new JPanel();

            String [] ct= {"闹钟","爱心"};	//创建
            //jcb1=new JComboBox(ct);		//添加到下拉框中

            jlb1 = new JLabel("api-sign");
            //jlb2 = new JLabel("X-Session-Key");

            jb1 = new JButton("提交");
            jb1.addActionListener(exwpAction);
            jtf1 = new JTextField(22);
            //jtf2 = new JTextField(22);

            jpf1 = new JPasswordField(10);// 设置布局管理(上面忘记：extends JFrame，这里出错了)
            this.setLayout(new GridLayout(3, 1));

            //文本域
            ta = new JTextArea();
            //ta.setLineWrap(true);
            ta.setBounds(10, 30, 100, 150);
            scroll = new JScrollPane(ta);

            // 加入各个组件
            jp1.add(jlb1);
            //jp3.add(jlb2);
            jp1.add(jtf1);
            //jp3.add(jtf2);

            //jp1.add(jcb1);

            jp3.add(jb1);



            // 加入到JFrame
            this.add(jp1);
            this.add(jp3);
            this.add(scroll);

            this.setSize(500, 300);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);
        }

}
