package github.com.worldmaomao.ui;

import github.com.worldmaomao.config.ConfigLoader;
import github.com.worldmaomao.constant.GlobalVariables;
import github.com.worldmaomao.servcie.DiskService;
import github.com.worldmaomao.servcie.impl.DiskServiceImpl;
import github.com.worldmaomao.vo.DiskCreateVo;
import github.com.worldmaomao.vo.ServiceResultVo;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class CreateDiskDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField diskNameTextField;
    private JTextField descriptionTextField;

    private TabForm tabForm;

    public CreateDiskDialog(TabForm tabForm) {
        this.tabForm = tabForm;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String diskName = diskNameTextField.getText();
        String description = descriptionTextField.getText();
        DiskCreateVo vo = new DiskCreateVo();
        vo.setName(diskName);
        vo.setDescription(description);
        try {
            DiskService diskService = new DiskServiceImpl(ConfigLoader.loadConfig());
            ServiceResultVo resultVo = diskService.create(vo);
            if (resultVo.isSuccess()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GlobalVariables.loadDiskData();
                        CreateDiskDialog.this.tabForm.setDiskTable();
                    }
                }).start();
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, resultVo.getError(), "创建失败", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
