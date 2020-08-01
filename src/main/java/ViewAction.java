import java.awt.event.ActionEvent;

public class ViewAction implements java.awt.event.ActionListener {
    /*
     * 这一部分是因为我把界面和动作分开成两个类来写了才需要进行对象的传递如果动作和界面在一个对象中写的话就不需要传入对象了
     */
    GuiOne v=new GuiOne();//先定义一个View对象在切换界面的时候会使用到
    public ViewAction(GuiOne v)//将外界的View对象传入这个类里
    {
        this.v=v;
    }
    @Override
    public void actionPerformed(ActionEvent event)
    {
        Object object=event.getSource();//创建事件源对象
        String selectedItem = (String)v.jcb1.getSelectedItem();
        switch(selectedItem){
            //如果按下视图1
            case "美菜文案":
                //这里之所以先创建新视图再关闭旧视图是因为反过来的话我们会发现延迟看上去会有一些难受
                new GuiOne().View1();//new一个View类并调用里面的view函数
                v.setVisible(false);//关闭穿进来的那个类的视图
                break;
            //如果按下视图2
            case "京东区区购数据":
                new GuiOne().View2();
                v.setVisible(false);
            case "兴盛优选":
                new GuiOne().View3();
                v.setVisible(false);
        }
    }

}

