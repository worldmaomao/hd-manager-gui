package github.com.worldmaomao.ui;

import javax.swing.*;

/**
 */
public class Gui {

    public void showLoginForm() {
        JFrame jframe = new JFrame("移动硬盘管理");
        jframe.setContentPane(new LoginForm(jframe).getRootPanel());
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
    }
}
