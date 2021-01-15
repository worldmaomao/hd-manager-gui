package github.com.worldmaomao.ui;

import github.com.worldmaomao.config.ConfigLoader;
import github.com.worldmaomao.constant.GlobalVariables;
import github.com.worldmaomao.disk.FileScanner;
import github.com.worldmaomao.servcie.DiskItemService;
import github.com.worldmaomao.servcie.DiskService;
import github.com.worldmaomao.servcie.impl.DiskItemServiceImpl;
import github.com.worldmaomao.servcie.impl.DiskServiceImpl;
import github.com.worldmaomao.ui.tableModel.DiskItemSearchTableModel;
import github.com.worldmaomao.ui.tableModel.LocalFileScanTableModel;
import github.com.worldmaomao.ui.tableModel.NoEditableTableModel;
import github.com.worldmaomao.vo.*;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public class TabForm {
    private static final String[] diskColumns = new String[]{"Id", "硬盘名称", "描述"};
    private static final String[] diskItemColumns = new String[]{"选中", "Id", "文件名", "硬盘", "入库时间"};
    private static final String[] fileScanColumns = new String[]{"选中", "文件名", "文件路径", "文件类型"};


    private JFrame frame;

    private JTabbedPane tabbedPane1;
    private JTextField keywordTextFieldTab1;
    private JComboBox diskComboxTab1;
    private JButton 查询Button;
    private JTable diskItemSearchTableTab1;
    private JButton 添加Button;
    private JTable diskTableTab2;
    private JComboBox localDiskComboBoxTab3;
    private JTextField scanDeepTextFieldTab3;
    private JTextField scanPathTextFieldTab3;
    private JButton 扫描Button;
    private JTable fileScanTableTab3;
    private JButton 入库Button;
    private JPanel rootPanel;
    private JButton 上一页Button;
    private JButton 下一页Button;
    private JButton 跳转Button;
    private JTextField pageSizeTab1;
    private JTextField pageTab1;
    private JTextField totalCountTab1;
    private JButton 删除Button;
    private JCheckBox 是否包含目录CheckBox;
    private JCheckBox 过滤点开头的文体局CheckBox;
    private JButton 删除所选Button;
    private JButton 删除Button1;
    private JButton 硬盘查询Button1;


    public TabForm() {
        frame = new JFrame(GlobalVariables.Title);
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);

        // 设置硬盘下拉框
        setDiskCombox();

        查询Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchDiskItem(1);
            }
        });

        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane1.getSelectedIndex() == 0) {
                    setDiskCombox();
                }
                if (tabbedPane1.getSelectedIndex() == 1) {
                    setDiskTable();
                }
                if (tabbedPane1.getSelectedIndex() == 2) {
                    // 给本地磁盘下拉框赋值
                    localDiskComboBoxTab3.setModel(new DefaultComboBoxModel(GlobalVariables.LocalDiskCache.getDiskNames().toArray()));
                    setLocalDiskSelected();
                }
            }
        });

        localDiskComboBoxTab3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setLocalDiskSelected();
            }
        });

        扫描Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String scanDeep = scanDeepTextFieldTab3.getText();
                String scanPath = scanPathTextFieldTab3.getText();

                try {
                    FileScanner fileScanner = new FileScanner(scanPath, NumberUtils.toInt(scanDeep, 1), 是否包含目录CheckBox.isSelected(), 过滤点开头的文体局CheckBox.isSelected());
                    扫描Button.setEnabled(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<LocalFileVo> list = fileScanner.scan();
                            setFileScanResult(list);
                            扫描Button.setEnabled(true);
                        }
                    }).start();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

                }
            }
        });


        删除Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalFileScanTableModel tableModel = (LocalFileScanTableModel) fileScanTableTab3.getModel();
                Vector dataVector = tableModel.getDataVector();
                List<Integer> rowIds = new ArrayList<>();
                for (int i = 0; i < dataVector.size(); i++) {
                    Vector row = (Vector) dataVector.get(i);
                    if ((Boolean) row.get(0)) {
                        rowIds.add(fileScanTableTab3.convertRowIndexToModel(i));
                    }
                }
                if (rowIds.isEmpty()) {
                    return;
                }
                Integer[] delRowIds = new Integer[]{};
                delRowIds = rowIds.toArray(delRowIds);

                // 排序后从后往前删
                Arrays.sort(delRowIds);
                for (int i = delRowIds.length - 1; i >= 0; i--) {
                    tableModel.removeRow(delRowIds[i]);
                }
                fileScanTableTab3.updateUI();
            }
        });
        入库Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileScanTableTab3.getModel().getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "请先扫描文件", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ImportDiskItemDialog dialog = new ImportDiskItemDialog(fileScanTableTab3);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

            }
        });
        删除所选Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (diskItemSearchTableTab1.getModel().getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "没有文件可以删除", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DiskItemSearchTableModel tableModel = (DiskItemSearchTableModel) diskItemSearchTableTab1.getModel();
                Vector dataVector = tableModel.getDataVector();
                List<Integer> rowIds = new ArrayList<>();
                List<String> diskItemIdList = new ArrayList<>();
                Map<String, Integer> idToRowIdMap = new HashMap<>();
                for (int i = 0; i < dataVector.size(); i++) {
                    Vector row = (Vector) dataVector.get(i);
                    if ((Boolean) row.get(0)) {
                        String diskItemId = ((String) row.get(1));
                        int rowId = diskItemSearchTableTab1.convertRowIndexToModel(i);
                        rowIds.add(rowId);
                        diskItemIdList.add(diskItemId);
                        idToRowIdMap.put(diskItemId, rowId);
                    }
                }
                if (rowIds.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请选择需要删除的文件", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    DiskItemServiceImpl diskItemService = new DiskItemServiceImpl(ConfigLoader.loadConfig());
                    List<DeleteDiskItemResultVo> failedList = diskItemService.batchDelete(diskItemIdList);
                    for (DeleteDiskItemResultVo vo : failedList) {
                        idToRowIdMap.remove(vo.getDiskItemId());
                    }
                    Integer[] delRowIds = new Integer[]{};
                    delRowIds = idToRowIdMap.values().toArray(delRowIds);
                    // 排序后从后往前删
                    Arrays.sort(delRowIds);
                    for (int i = delRowIds.length - 1; i >= 0; i--) {
                        tableModel.removeRow(delRowIds[i]);
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            diskItemSearchTableTab1.updateUI();
                        }
                    });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        硬盘查询Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalVariables.loadDiskData();
                setDiskTable();
            }
        });

        删除Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowId = diskTableTab2.getSelectedRow();
                if (rowId < 0) {
                    return;
                }
                NoEditableTableModel tableModel = (NoEditableTableModel) diskTableTab2.getModel();
                Vector dataVector = tableModel.getDataVector();
                Vector row = (Vector) dataVector.get(rowId);
                String diskId = (String) row.get(0);
                try {
                    DiskService diskService = new DiskServiceImpl(ConfigLoader.loadConfig());
                    ServiceResultVo resultVo = diskService.delete(diskId);
                    if (resultVo.isSuccess()) {
                        diskTableTab2.remove(diskTableTab2.convertRowIndexToModel(rowId));
                    } else {
                        JOptionPane.showMessageDialog(null, resultVo.getError(), "删除失败", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        添加Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateDiskDialog dialog = new CreateDiskDialog(TabForm.this);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        上一页Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int page = NumberUtils.toInt(pageTab1.getText(), 1);
                page = (page - 1);
                if (page < 1) {
                    page = 1;
                }
                searchDiskItem(page);
            }
        });
        下一页Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int page = NumberUtils.toInt(pageTab1.getText(), 1);
                if (page < 1) {
                    page = 1;
                }
                page = (page + 1);
                searchDiskItem(page);
            }
        });
        跳转Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int page = NumberUtils.toInt(pageTab1.getText(), 1);
                if (page < 1) {
                    page = 1;
                }
                searchDiskItem(page);
            }
        });
    }

    private void setDiskCombox() {
        List<String> diskNameList = GlobalVariables.DiskCache.getDiskNames();
        diskNameList.add(0, "所有");
        diskComboxTab1.setModel(new DefaultComboBoxModel(diskNameList.toArray()));
    }

    public void setDiskTable() {
        NoEditableTableModel tableModel = new NoEditableTableModel();
        List<DiskVo> list = GlobalVariables.DiskCache.getDiskList();
        Object[][] rows = new Object[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            DiskVo vo = list.get(i);
            Object[] row = new Object[3];
            row[0] = vo.getId();
            row[1] = vo.getName();
            row[2] = vo.getDescription();
            rows[i] = row;
        }
        tableModel.setDataVector(rows, diskColumns);
        diskTableTab2.setModel(tableModel);
        TableColumnModel tableColumnModel = diskTableTab2.getColumnModel();
        diskTableTab2.removeColumn(tableColumnModel.getColumn(0));
    }

    private void setLocalDiskSelected() {
        String localDiskName = (String) localDiskComboBoxTab3.getSelectedItem();
        LocalDiskVo localDiskVo = GlobalVariables.LocalDiskCache.getDiskByName(localDiskName);
        if (localDiskVo != null) {
            scanPathTextFieldTab3.setText(localDiskVo.getRootPath());
        }
    }

    private void setFileScanResult(List<LocalFileVo> list) {
        Object[][] rows = new Object[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            LocalFileVo vo = list.get(i);
            Object[] row = new Object[4];
            row[0] = Boolean.FALSE;
            row[1] = vo.getFileName();
            row[2] = vo.getFilePath();
            row[3] = vo.getFileType().equalsIgnoreCase("file") ? "文件" : "目录";
            rows[i] = row;
        }

        LocalFileScanTableModel tableModel = new LocalFileScanTableModel();
        tableModel.setDataVector(rows, fileScanColumns);
        fileScanTableTab3.setModel(tableModel);
        fileScanTableTab3.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    private void searchDiskItem(int page) {
        String diskName = (String) diskComboxTab1.getSelectedItem();
        DiskVo diskVo = GlobalVariables.DiskCache.getDiskByName(diskName);
        String diskId = diskVo == null ? "" : diskVo.getId();
        String fileName = keywordTextFieldTab1.getText();
        String pageSize = pageSizeTab1.getText();
        QueryDiskItemRequestVo requestVo = new QueryDiskItemRequestVo();
        requestVo.setDiskId(diskId);
        requestVo.setFileName(fileName);
        requestVo.setPage(page);
        requestVo.setPageSize(NumberUtils.toInt(pageSize, 20));
        DiskItemService diskItemService = new DiskItemServiceImpl(ConfigLoader.loadConfig());
        try {
            QueryDiskItemResponseVo responseVo = diskItemService.query(requestVo);
            setDiskItemQueryResult(responseVo);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
    }

    private void setDiskItemQueryResult(QueryDiskItemResponseVo responseVo) {
        totalCountTab1.setText(String.valueOf(responseVo.getTotalCount()));
        pageSizeTab1.setText(String.valueOf(responseVo.getPageSize()));
        pageTab1.setText(String.valueOf(responseVo.getPage()));

        DateFormat df = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        Object[][] rows = new Object[responseVo.getList().size()][];
        for (int i = 0; i < responseVo.getList().size(); i++) {
            DiskItemVo vo = responseVo.getList().get(i);
            Object[] row = new Object[5];
            row[0] = Boolean.FALSE;
            row[1] = vo.getId();
            row[2] = vo.getFileName();
            row[3] = vo.getDiskName();
            Date date = new Date(vo.getCreated() * 1000);
            row[4] = df.format(date);
            rows[i] = row;
        }
        DiskItemSearchTableModel tableModel = new DiskItemSearchTableModel();
        tableModel.setDataVector(rows, diskItemColumns);
        diskItemSearchTableTab1.setModel(tableModel);

        // 设置列宽
        diskItemSearchTableTab1.getColumnModel().getColumn(0).setPreferredWidth(10);
        diskItemSearchTableTab1.getColumnModel().getColumn(1).setPreferredWidth(0);
        diskItemSearchTableTab1.getColumnModel().getColumn(2).setPreferredWidth(800);
        diskItemSearchTableTab1.getColumnModel().getColumn(3).setPreferredWidth(20);
        diskItemSearchTableTab1.getColumnModel().getColumn(4).setPreferredWidth(80);

        // 隐藏id列
        TableColumn idColumn = diskItemSearchTableTab1.getColumnModel().getColumn(1);
        diskItemSearchTableTab1.removeColumn(idColumn);
    }


}
