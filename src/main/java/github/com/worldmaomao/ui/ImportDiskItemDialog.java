package github.com.worldmaomao.ui;

import github.com.worldmaomao.config.ConfigLoader;
import github.com.worldmaomao.constant.GlobalVariables;
import github.com.worldmaomao.servcie.DiskItemService;
import github.com.worldmaomao.servcie.impl.DiskItemServiceImpl;
import github.com.worldmaomao.ui.tableModel.LocalFileScanTableModel;
import github.com.worldmaomao.ui.tableModel.NoEditableTableModel;
import github.com.worldmaomao.vo.DiskVo;
import github.com.worldmaomao.vo.ImportDiskItemResultVo;
import github.com.worldmaomao.vo.ImportDiskItemVo;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class ImportDiskItemDialog extends JDialog {
    private static final String[] ImportResultColumns = new String[]{"文件名", "入库结果"};

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private JTable table1;
    private JLabel importSummaryLabel;
    private JTable fileScanTable;

    public ImportDiskItemDialog(JTable fileScanTable) {
        this.fileScanTable = fileScanTable;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        comboBox1.setModel(new DefaultComboBoxModel(GlobalVariables.DiskCache.getDiskNames().toArray()));

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
        // add your code here
        // 二次确认
        int response = JOptionPane.showConfirmDialog(this, "确认要把这些文件导入到硬盘" + comboBox1.getSelectedItem() + "吗？", "导入确认", JOptionPane.YES_NO_OPTION);
        if (response != JOptionPane.YES_OPTION) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                buttonOK.setEnabled(false);
            }
        });
        // 组装导入参数
        String diskName = (String) comboBox1.getSelectedItem();
        DiskVo diskVo = GlobalVariables.DiskCache.getDiskByName(diskName);
        LocalFileScanTableModel tableModel = (LocalFileScanTableModel) fileScanTable.getModel();
        Vector dataVendor = tableModel.getDataVector();
        List<ImportDiskItemVo> diskItemVoList = new ArrayList<>();
        for (int i = 0; i < dataVendor.size(); i++) {
            Vector object = (Vector) dataVendor.get(i);
            ImportDiskItemVo vo = new ImportDiskItemVo();
            vo.setDiskId(diskVo.getId());
            vo.setFileName((String) object.get(1));
            vo.setFileType(object.get(3).equals("文件") ? "file" : "dir");
            vo.setDescription("");
            vo.setPicPath("");
            diskItemVoList.add(vo);
        }
        try {
            if (!diskItemVoList.isEmpty()) {
                DiskItemService diskItemService = new DiskItemServiceImpl(ConfigLoader.loadConfig());
                List<ImportDiskItemResultVo> resultList = diskItemService.batchImport(diskItemVoList);
                setResultTable(diskItemVoList, resultList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    buttonOK.setEnabled(true);
                }
            });
        }

    }

    private void setResultTable(List<ImportDiskItemVo> diskItemVoList, List<ImportDiskItemResultVo> resultList) {
        importSummaryLabel.setText(String.format("预计导入：%d, 导入成功：%d,导入失败：%d", diskItemVoList.size(), (diskItemVoList.size() - resultList.size()), resultList.size()));
        Map<String, String> importResultMap = resultList.stream().collect(Collectors.toMap(ImportDiskItemResultVo::getDiskItemName, ImportDiskItemResultVo::getError, (key1, key2) -> key2));
        Object[][] rows = new Object[diskItemVoList.size()][];
        for (int i = 0; i < diskItemVoList.size(); i++) {
            ImportDiskItemVo vo = diskItemVoList.get(i);
            Object[] row = new Object[2];
            row[0] = vo.getFileName();
            if (importResultMap.containsKey(vo.getFileName())) {
                row[1] = importResultMap.get(vo.getFileName());
            } else {
                row[1] = "成功";
            }
            rows[i] = row;
        }
        NoEditableTableModel tableModel = new NoEditableTableModel();
        tableModel.setDataVector(rows, ImportResultColumns);
        table1.setModel(tableModel);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ImportDiskItemDialog dialog = new ImportDiskItemDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
