package github.com.worldmaomao.ui;

import github.com.worldmaomao.constant.GlobalVariables;

import javax.swing.*;

/**
 */
public class Gui {

    public void showLoginForm() {
        JFrame jframe = new JFrame(GlobalVariables.Title);
        jframe.setContentPane(new LoginForm(jframe).getRootPanel());
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
    }
}
