package github.com.worldmaomao.ui;

import github.com.worldmaomao.config.Config;
import github.com.worldmaomao.config.ConfigLoader;
import github.com.worldmaomao.constant.GlobalVariables;
import github.com.worldmaomao.servcie.LoginService;
import github.com.worldmaomao.servcie.impl.LoginServiceImpl;
import github.com.worldmaomao.vo.LoginVo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 */
public class LoginForm {
    private JFrame frame;
    private JTextField 用户名TextField;
    private JButton 登录Button;
    private JButton 重置Button;
    private JCheckBox 保持登录CheckBox;
    private JPanel rootPanel;
    private JPasswordField passwordField1;
    private JLabel processLabel;
    private JTextField addressUrlTextFieldTab1;


    public LoginForm(JFrame frame) {
        this.frame = frame;

        Config config = ConfigLoader.loadConfig();

        addressUrlTextFieldTab1.setText(config.getServerUrl());
        用户名TextField.setText(config.getUsername());

        重置Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginForm.this.用户名TextField.setText("");
                LoginForm.this.passwordField1.setText("");
            }
        });
        登录Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }).start();
            }
        });
        passwordField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            login();
                        }
                    }).start();
                }
            }
        });
    }


    private void login() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    登录Button.setEnabled(false);
                    processLabel.setText("登录中......");
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        String username = LoginForm.this.用户名TextField.getText();
        String password = new String(LoginForm.this.passwordField1.getPassword());
        Boolean isKeepLogin = LoginForm.this.保持登录CheckBox.isSelected();
        String serverUrl = LoginForm.this.addressUrlTextFieldTab1.getText();

        Config config = ConfigLoader.loadConfig();
        config.setServerUrl(serverUrl);
        LoginVo loginVo = LoginVo.builder().username(username).password(password).build();
        LoginService loginService = LoginServiceImpl.builder().config(config).build();
        try {
            // 登录
            String token = loginService.login(loginVo);
            GlobalVariables.JWT = token;

            // 保存配置信息
            config.setUsername(username);
            // 保存token
            if (isKeepLogin) {
                config.setToken(token);
            }
            ConfigLoader.storeConfig(config);


            // 加载服务器硬盘数据
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    processLabel.setText("加载服务器硬盘数据......");
                }
            });
            GlobalVariables.loadDiskData();
            // 扫描本地磁盘列表
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    processLabel.setText("扫描本地磁盘列表......");
                }
            });
            GlobalVariables.loadLocalDisk();
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    LoginForm.this.frame.dispose();
                    new TabForm();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "网络错误", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex1) {
            ex1.printStackTrace();
            JOptionPane.showMessageDialog(null, ex1.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        } finally {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    登录Button.setEnabled(true);
                    processLabel.setText("");
                }
            });

        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
