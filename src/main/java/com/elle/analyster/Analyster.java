package com.elle.analyster;

import com.elle.analyster.db.ExecuteSQLStatement;
import com.elle.analyster.domain.ModifiedData;
import com.elle.analyster.presentation.filter.CreateDocumentFilter;
import com.elle.analyster.presentation.filter.ITableFilter;
import com.elle.analyster.presentation.filter.TableRowFilterSupport;
import com.elle.analyster.service.Connection;
import com.elle.analyster.service.DeleteRecord;
import com.elle.analyster.service.TableService;
import com.elle.analyster.service.UploadRecord;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Analyster extends JFrame {

    private static final String SYMBOL_COLUMN_NAME = "Symbol";
    private static final String ASSIGNMENTS_TABLE_NAME = "Assignments";
    private static final String REPORTS_TABLE_NAME = "Reports";
    private static final String ARCHIVE_TABLE_NAME = "Assignments_Archived";
    private final TableService tableService = new TableService();
    private final Connection con = new Connection();
    private LoadTables loadTables;
    private JTable assignmentFiltered;
    private JTable reportFiltered;
    private JTable archiveAssignFiltered;
    final float[] columnWidthPercentage1 = {35, 65, 80, 70, 99, 99};
    final float[] columnWidthPercentage2 = {35, 65, 80, 100, 160, 120, 123};
    private JTableHeader header;
    private ITableFilter<?> filterTempReport;
    private ITableFilter<?> filterTempAssignment;
    private ITableFilter<?> filterTempArchive;
    private static Analyster instance;
    private static int numberAssignmentInit;
    private static int numberReportsInit;
    private static int numberArchiveAssignInit;
    @Autowired
    private UploadRecord uploadRecordService;

    public UploadRecord getUploadRecordService() {
        return uploadRecordService;
    }

    public void setUploadRecordService(UploadRecord uploadRecordService) {
        this.uploadRecordService = uploadRecordService;
    }

    public ITableFilter<?> getFilterTempArchive() {
        return filterTempArchive;
    }

    public void setFilterTempArchive(ITableFilter<?> filterTempArchive) {
        this.filterTempArchive = filterTempArchive;
    }

    public ITableFilter<?> getFilterTempReport() {
        return filterTempReport;
    }

    public void setFilterTempReport(ITableFilter<?> filterTempReport) {
        this.filterTempReport = filterTempReport;
    }

    public void setFilterTempAssignment(ITableFilter<?> filterTempAssignment) {
        this.filterTempAssignment = filterTempAssignment;
    }

    public JLabel getNumOfRecords1() {
        return numOfRecords1;
    }

    public static void setNumberArchiveAssignInit(int numberArchiveAssignInit) {
        Analyster.numberArchiveAssignInit = numberArchiveAssignInit;
    }

    public static void setNumberAssignmentInit(int numberAssignmentInit) {
        Analyster.numberAssignmentInit = numberAssignmentInit;
    }

    public static void setNumberReportsInit(int numberReportsInit) {
        Analyster.numberReportsInit = numberReportsInit;
    }

    public static String getAssignmentsTableName() {
        return ASSIGNMENTS_TABLE_NAME;
    }

    public static String getReportsTableName() {
        return REPORTS_TABLE_NAME;
    }

    public static String getArchiveTableName() {
        return ARCHIVE_TABLE_NAME;
    }

    public static Analyster getInstance() {
        if (instance == null) {
            instance = new Analyster();
        }
        return instance;
    }

    public JTable getassignmentTable() {
        return assignmentTable;
    }

    public JTable getReportTable() {
        return reportTable;
    }

    public JTable getArchiveAssignTable() {
        return archiveAssignTable;
    }
     JTable getViewerTable() {
        return viewerTable;
    }

    public JTabbedPane getjTabbedPanel1() {
        return jTabbedPanel1;
    }

    public JMenuItem getjActivateRecord() {
        return jActivateRecord;
    }

    public JMenuItem getjArchiveRecord() {
        return jArchiveRecord;
    }

    public Analyster() {
        initComponents();
        jTextArea.setVisible(false);

        // set the interface to the middle of the window
        this.setLocationRelativeTo(null);
        this.setTitle("Analyster");
        jUpload.setVisible(false);
        jDebugEnter.setVisible(false);
        jDebugCancel.setVisible(false);
        jButtonCancel.setVisible(false);
        jBatchEdit.setVisible(true);
        tableService.setAssignmentTable(assignmentTable);
        tableService.setReportTable(reportTable);
        tableService.setArchiveAssignTable(archiveAssignTable);
        tableService.setViewerTable(assignmentTable);
        jScrollPane5.setVisible(false);
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {// Allow to TAB-

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    if (jLabelEdit.getText().equals("ON ")) {
                        if (e.getComponent() instanceof JTable) {
                            JTable table = (JTable) e.getComponent();
                            int row = table.getSelectedRow();
                            int column = table.getSelectedColumn();
                            if (column == table.getRowCount() || column == 0) {
                                return false;
                            } else {
                                table.getComponentAt(row, column).requestFocus();
                                table.editCellAt(row, column);
                                JTextField selectCom = (JTextField) table.getEditorComponent();
                                selectCom.requestFocusInWindow();
                                selectCom.selectAll();
                            }
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    if (jLabelEdit.getText().equals("ON ")) {                       // Default Date input with today's date
                        JTable table = (JTable) e.getComponent().getParent();
                        int column = table.getSelectedColumn();
                        if (table.getColumnName(column).toLowerCase().contains("date")) {
                            if (e.getID() != 401) {
                                return false;
                            } else {
                                JTextField selectCom = (JTextField) e.getComponent();
                                selectCom.requestFocusInWindow();
                                selectCom.selectAll();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = new Date();
                                String today = dateFormat.format(date);
                                selectCom.setText(today);
                            }
                        }
                    }
                }
                return false;
            }
        }
        );
        instance = this;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addPanel_control = new javax.swing.JPanel();
        textForSearch = new javax.swing.JTextField();
        search = new javax.swing.JButton();
        numOfRecords1 = new javax.swing.JLabel();
        numOfRecords2 = new javax.swing.JLabel();
        jField = new javax.swing.JComboBox();
        jTimeLastUpdate = new javax.swing.JLabel();
        jButtonClearAllFilter = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jUpload = new javax.swing.JButton();
        jTabbedPanel1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        assignmentTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        archiveAssignTable = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        viewerTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jSwitchEditMode = new javax.swing.JButton();
        jLabelEdit = new javax.swing.JLabel();
        jButtonCancel = new javax.swing.JButton();
        jBatchEdit = new javax.swing.JButton();
        jBatchAdd = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();
        jDebugEnter = new javax.swing.JButton();
        jDebugCancel = new javax.swing.JButton();
        jSwitchDebugMode = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabelDebug = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemFileVersion = new javax.swing.JMenuItem();
        jMenuSelectConn = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuPrint = new javax.swing.JMenu();
        jMenuItemPrintGUI = new javax.swing.JMenuItem();
        jMenuItemPrintDisplay = new javax.swing.JMenuItem();
        jMenuItemSaveFile = new javax.swing.JMenuItem();
        jMenuItemLogOff = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuEditDB = new javax.swing.JMenuItem();
        jDeleteRecord = new javax.swing.JMenuItem();
        jArchiveRecord = new javax.swing.JMenuItem();
        jActivateRecord = new javax.swing.JMenuItem();
        jMenuFind = new javax.swing.JMenu();
        jMenuReport = new javax.swing.JMenu();
        jMenuView = new javax.swing.JMenu();
        jMenuItemViewLog = new javax.swing.JMenuItem();
        jMenuItemViewSQL = new javax.swing.JMenuItem();
        jMenuItemViewAssig = new javax.swing.JMenuItem();
        jMenuItemViewReports = new javax.swing.JMenuItem();
        jMenuItemViewAllAssig = new javax.swing.JMenuItem();
        jMenuItemViewActiveAssig = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuOther = new javax.swing.JMenu();
        jMenuItemOtherReport = new javax.swing.JMenuItem();
        jMenuItemOthersLoadData = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(894, 557));

        addPanel_control.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        textForSearch.setText("Enter Symbol name");
        textForSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textForSearchMouseClicked(evt);
            }
        });
        textForSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textForSearchKeyPressed(evt);
            }
        });

        search.setText("Search");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        numOfRecords2.setSize(new java.awt.Dimension(220, 15));

        jField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "symbol", "analyster" }));

        jTimeLastUpdate.setText("Last updated: ");

        jButtonClearAllFilter.setText("Clear All Filters");
        jButtonClearAllFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearAllFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addPanel_controlLayout = new javax.swing.GroupLayout(addPanel_control);
        addPanel_control.setLayout(addPanel_controlLayout);
        addPanel_controlLayout.setHorizontalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(jField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonClearAllFilter)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addComponent(textForSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(search))
                    .addComponent(jTimeLastUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numOfRecords1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numOfRecords2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        addPanel_controlLayout.setVerticalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textForSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(search)
                        .addComponent(jField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(numOfRecords1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numOfRecords2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTimeLastUpdate)
                            .addComponent(jButtonClearAllFilter))
                        .addGap(7, 12, Short.MAX_VALUE))))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        jUpload.setText("Upload Changes");
        jUpload.setMaximumSize(new java.awt.Dimension(95, 30));
        jUpload.setMinimumSize(new java.awt.Dimension(95, 30));
        jUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUploadActionPerformed(evt);
            }
        });

        jTabbedPanel1.setPreferredSize(new java.awt.Dimension(800, 450));
        jTabbedPanel1.setSize(getPreferredSize());
        jTabbedPanel1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPanel1StateChanged(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        assignmentTable.setAutoCreateRowSorter(true);
        assignmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "symbol", "analyst", "priority", "dateAssigned", "dateDone", "notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        assignmentTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        assignmentTable.setMinimumSize(new java.awt.Dimension(10, 240));
        assignmentTable.setName(""); // NOI18N
        jScrollPane1.setViewportView(assignmentTable);

        jTabbedPanel1.addTab("Assignments", jScrollPane1);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "", null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "symbol", "author", "analysisDate", "path", "document", "notes", "notesL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        reportTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        reportTable.setMinimumSize(new java.awt.Dimension(10, 240));
        jScrollPane4.setViewportView(reportTable);

        jTabbedPanel1.addTab("Reports", jScrollPane4);

        archiveAssignTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "symbol", "analyst", "priority", "dateAssigned", "dateDone", "notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        archiveAssignTable.setAutoscrolls(false);
        archiveAssignTable.setMinimumSize(new java.awt.Dimension(10, 240));
        jScrollPane3.setViewportView(archiveAssignTable);

        jTabbedPanel1.addTab("Assignments_Archived", jScrollPane3);

        viewerTable.setAutoCreateRowSorter(true);
        viewerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "symbol", "analyst", "priority", "dateAssigned", "dateDone", "notes"
            }
        ));
        viewerTable.setMinimumSize(new java.awt.Dimension(10, 240));
        viewerTable.setName("ViewerTable"); // NOI18N
        jScrollPane5.setViewportView(viewerTable);

        jTabbedPanel1.addTab("Viewer", jScrollPane5);

        jLabel2.setText("Edit Mode:");

        jSwitchEditMode.setText("Switch");
        jSwitchEditMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSwitchEditModeActionPerformed(evt);
            }
        });

        jLabelEdit.setText("OFF");
        jLabelEdit.setSize(new java.awt.Dimension(24, 16));

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jBatchEdit.setText("Batch Edit");
        jBatchEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBatchEditActionPerformed(evt);
            }
        });

        jBatchAdd.setText("Add Record(s)");
        jBatchAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBatchAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSwitchEditMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jUpload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBatchAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBatchEdit)
                .addGap(26, 26, 26))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jTabbedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jUpload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jSwitchEditMode)
                    .addComponent(jLabelEdit)
                    .addComponent(jButtonCancel)
                    .addComponent(jBatchEdit)
                    .addComponent(jBatchAdd))
                .addContainerGap())
        );

        jTabbedPanel1.getAccessibleContext().setAccessibleName("Reports");
        jTabbedPanel1.getAccessibleContext().setAccessibleParent(jTabbedPanel1);

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));

        jTextArea.setBackground(new java.awt.Color(0, 153, 102));
        jTextArea.setColumns(20);
        jTextArea.setLineWrap(true);
        jTextArea.setRows(5);
        jTextArea.setText("Please input an SQL statement:\\n>>");
        ((AbstractDocument) jTextArea.getDocument())
        .setDocumentFilter(new CreateDocumentFilter(33));
        jTextArea.setWrapStyleWord(true);
        jTextArea.setMaximumSize(new java.awt.Dimension(1590, 150));
        jTextArea.setMinimumSize(new java.awt.Dimension(1590, 150));
        jScrollPane2.setViewportView(jTextArea);

        jDebugEnter.setText("Enter");
        jDebugEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDebugEnterActionPerformed(evt);
            }
        });

        jDebugCancel.setText("Cancel");
        jDebugCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDebugCancelActionPerformed(evt);
            }
        });

        jSwitchDebugMode.setText("Switch");
        jSwitchDebugMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSwitchDebugModeActionPerformed(evt);
            }
        });

        jLabel1.setText("Debug Mode:");

        jLabelDebug.setText("OFF");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDebug)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSwitchDebugMode)
                .addGap(58, 58, 58)
                .addComponent(jDebugEnter)
                .addGap(59, 59, 59)
                .addComponent(jDebugCancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDebugEnter)
                    .addComponent(jDebugCancel)
                    .addComponent(jSwitchDebugMode)
                    .addComponent(jLabel1)
                    .addComponent(jLabelDebug))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenuFile.setText("File");

        jMenuItemFileVersion.setText("Version");
        jMenuItemFileVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFileVersionActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemFileVersion);

        jMenuSelectConn.setText("Select Connection");

        jMenuItem3.setText("AWS Assignments");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenuSelectConn.add(jMenuItem3);

        jMenuFile.add(jMenuSelectConn);

        jMenuPrint.setText("Print");

        jMenuItemPrintGUI.setText("Print GUI");
        jMenuPrint.add(jMenuItemPrintGUI);

        jMenuItemPrintDisplay.setText("Print Display Window");
        jMenuPrint.add(jMenuItemPrintDisplay);

        jMenuFile.add(jMenuPrint);

        jMenuItemSaveFile.setText("Save File");
        jMenuFile.add(jMenuItemSaveFile);

        jMenuItemLogOff.setText("Log out");
        jMenuItemLogOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLogOffActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemLogOff);

        menuBar.add(jMenuFile);

        jMenuEdit.setText("Edit");

        jMenuEditDB.setText("Manage databases");
        jMenuEditDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuEditDBActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuEditDB);

        jDeleteRecord.setText("Delete Record");
        jDeleteRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeleteRecordActionPerformed(evt);
            }
        });
        jMenuEdit.add(jDeleteRecord);

        jArchiveRecord.setText("Archive Record");
        jArchiveRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jArchiveRecordActionPerformed(evt);
            }
        });
        jMenuEdit.add(jArchiveRecord);

        jActivateRecord.setText("Activate Record");
        jActivateRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jActivateRecordActionPerformed(evt);
            }
        });
        jMenuEdit.add(jActivateRecord);

        menuBar.add(jMenuEdit);

        jMenuFind.setText("Find");
        menuBar.add(jMenuFind);

        jMenuReport.setText("Reports");
        menuBar.add(jMenuReport);

        jMenuView.setText("View");

        jMenuItemViewLog.setText("Log");
        jMenuItemViewLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemViewLogActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemViewLog);

        jMenuItemViewSQL.setText("SQL Command");
        jMenuView.add(jMenuItemViewSQL);

        jMenuItemViewAssig.setText("View Assignments Columns");
        jMenuView.add(jMenuItemViewAssig);

        jMenuItemViewReports.setText("View Reports Columns");
        jMenuView.add(jMenuItemViewReports);

        jMenuItemViewAllAssig.setText("View All Assignments");
        jMenuItemViewAllAssig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemViewAllAssigActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemViewAllAssig);

        jMenuItemViewActiveAssig.setText("View Active Assigments");
        jMenuItemViewActiveAssig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemViewActiveAssigActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemViewActiveAssig);

        menuBar.add(jMenuView);

        jMenuHelp.setText("Help");
        menuBar.add(jMenuHelp);

        jMenuOther.setText("Tools");

        jMenuItemOtherReport.setText("Report a bug/suggestion");
        jMenuItemOtherReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOtherReportActionPerformed(evt);
            }
        });
        jMenuOther.add(jMenuItemOtherReport);

        jMenuItemOthersLoadData.setText("Reload data");
        jMenuItemOthersLoadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOthersLoadDataActionPerformed(evt);
            }
        });
        jMenuOther.add(jMenuItemOthersLoadData);

        menuBar.add(jMenuOther);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addPanel_control, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(addPanel_control, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemFileVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFileVersionActionPerformed

        JOptionPane.showMessageDialog(this, "Creation Date: "
                + "2015-05-29" + "\n"
                + "Version: " + "0.6.2e");
    }//GEN-LAST:event_jMenuItemFileVersionActionPerformed

    private void jMenuItemViewLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewLogActionPerformed

        logwind.showLogWindow();
    }//GEN-LAST:event_jMenuItemViewLogActionPerformed

    private void textForSearchMouseClicked(MouseEvent evt) {//GEN-FIRST:event_textForSearchMouseClicked

        textForSearch.setText(null);
    }//GEN-LAST:event_textForSearchMouseClicked

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        String selectedTab = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
        filterBySearchButton(selectedTab);

    }//GEN-LAST:event_searchActionPerformed
    public void filterBySearchButton(String selectedTab) {
        int columnIndex;
        if (jField.getSelectedItem().toString().equals(SYMBOL_COLUMN_NAME)) {
            columnIndex = 1;
        } else {
            columnIndex = 2;
        }
        Object selectedField = textForSearch.getText();         //Search is case sensitive!!!!
        switch (selectedTab) {
            case ASSIGNMENTS_TABLE_NAME:
                TableRowFilterSupport.forTable(assignmentTable).actions(true).apply().apply(columnIndex, selectedField);
                GUI.columnFilterStatus(columnIndex, filterTempAssignment.getTable());
                break;
            case REPORTS_TABLE_NAME:
                TableRowFilterSupport.forTable(reportTable).actions(true).apply().apply(columnIndex, selectedField);
                GUI.columnFilterStatus(columnIndex, filterTempReport.getTable());
                break;
            default:
                TableRowFilterSupport.forTable(archiveAssignTable).actions(true).apply().apply(columnIndex, selectedField);
                GUI.columnFilterStatus(columnIndex, filterTempArchive.getTable());
                break;
        }

    }
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

        System.out.println("Connection");
        String sqlC = "select * from " + ASSIGNMENTS_TABLE_NAME;
        Connection.connection(sqlC, assignmentTable);
        System.out.println("Connection");
        String sqlD = "select * from " + REPORTS_TABLE_NAME;
        Connection.connection(sqlD, reportTable);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUploadActionPerformed
        // upload two tables separately
        
        String selectedTab = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
        switch (selectedTab) {
            case (ASSIGNMENTS_TABLE_NAME):
                updateTable(assignmentTable, modifiedDataList);
                break;
            case (REPORTS_TABLE_NAME):
                updateTable(reportTable, modifiedDataList);
                break;
            case (ARCHIVE_TABLE_NAME):
                updateTable(archiveAssignTable, modifiedDataList);
                break;
        }

        if (GUI.isIsFiltering()) {
//            loadPrevious(selectedTab);
        } else {
            switch (selectedTab) {
                case (ASSIGNMENTS_TABLE_NAME):
                    loadTables.loadAssignmentTable();
                    break;
                case (REPORTS_TABLE_NAME):
                    loadTables.loadReportTable();
                    break;
                case (ARCHIVE_TABLE_NAME):
                    loadTables.loadArchiveAssignTable();
                    break;
                case ("Viewer"):
                    loadTables.loadAssignmentTable();
            }
        }
        getModifiedDataList().clear();    // reset the arraylist to record future changes
        setLastUpdateTime();    // update time
        makeTableEditable();

    }//GEN-LAST:event_jUploadActionPerformed

    private void jMenuItemOtherReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOtherReportActionPerformed
//        new ReportWin();// Create Report
    }//GEN-LAST:event_jMenuItemOtherReportActionPerformed

    private void jDebugEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDebugEnterActionPerformed
        if (enterButton.isCreateTable(jTextArea)) {
            Connection.connection(enterButton.getCommand(jTextArea), assignmentTable);
        } else {
            ExecuteSQLStatement.updateDatabase(GUI.con,
                    enterButton.getCommand(jTextArea));
        }
    }//GEN-LAST:event_jDebugEnterActionPerformed

    private void jDebugCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDebugCancelActionPerformed
        ((AbstractDocument) jTextArea.getDocument())
                .setDocumentFilter(new CreateDocumentFilter(0));
        jTextArea.setText("Please input an SQL statement:\n>>");
        ((AbstractDocument) jTextArea.getDocument())
                .setDocumentFilter(new CreateDocumentFilter(33));
    }//GEN-LAST:event_jDebugCancelActionPerformed

    private void jSwitchDebugModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSwitchDebugModeActionPerformed

        if (jLabelDebug.getText().equals("OFF")) {
            jTextArea.setVisible(true);
            jLabelDebug.setText("ON ");
            jDebugEnter.setVisible(true);
            jDebugCancel.setVisible(true);
        } else {
            jTextArea.setVisible(false);
            jLabelDebug.setText("OFF");
            jDebugEnter.setVisible(false);
            jDebugCancel.setVisible(false);
        }
    }//GEN-LAST:event_jSwitchDebugModeActionPerformed

    private void jSwitchEditModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSwitchEditModeActionPerformed

        makeTableEditable();

    }//GEN-LAST:event_jSwitchEditModeActionPerformed
    //Make the table Editable or Read Only

    public void makeTableEditable() {
        if (jLabelEdit.getText().equals("OFF")) {
            jLabelEdit.setText("ON ");
            jSwitchEditMode.setVisible(false);
            jUpload.setVisible(true);
            jButtonCancel.setVisible(true);
            jBatchEdit.setVisible(true);
            isFiltering = false;
            makeEditable(assignmentTable.getModel());
            makeEditable(reportTable.getModel());
            makeEditable(archiveAssignTable.getModel());

        } else {
            jLabelEdit.setText("OFF");
            jSwitchEditMode.setVisible(true);
            jUpload.setVisible(false);
            jButtonCancel.setVisible(false);
            jBatchEdit.setVisible(true);
            isFiltering = true;
            ((MyTableModel) assignmentTable.getModel()).setReadOnly(true);
            ((MyTableModel) reportTable.getModel()).setReadOnly(true);
            ((MyTableModel) archiveAssignTable.getModel()).setReadOnly(true);
            ((MyTableModel) viewerTable.getModel()).setReadOnly(true);
        }
    }

    private void makeEditable(TableModel tableModel) {
        if (tableModel instanceof MyTableModel) {
            ((MyTableModel) tableModel).setReadOnly(false);
             tableModel.removeTableModelListener(assignmentTable); 
             tableModel.removeTableModelListener(reportTable); 
             
        }
    }
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        String selectedTab = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
        if (GUI.isIsFiltering()) {
            loadPrevious(selectedTab);
        } else {
            switch (selectedTab) {
                case (ASSIGNMENTS_TABLE_NAME):
                    loadTables.loadAssignmentTable();
                    break;
                case (REPORTS_TABLE_NAME):
                    loadTables.loadReportTable();
                    break;
                case (ARCHIVE_TABLE_NAME):
                    loadTables.loadArchiveAssignTable();
                    break;
            }
        }
        makeTableEditable();

    }//GEN-LAST:event_jButtonCancelActionPerformed
    public void loadPrevious(String selectedTab) {

        if (selectedTab.equals(ASSIGNMENTS_TABLE_NAME)) {
            loadTables.loadAssignmentTableWithFilter(filterTempAssignment.getColumnIndex(), filterTempAssignment.getFilterCriteria());
            setColumnFormat(columnWidthPercentage1, assignmentTable);
            GUI.columnFilterStatus(filterTempAssignment.getColumnIndex(), assignmentTable);
        } else if (selectedTab.equals(REPORTS_TABLE_NAME)) {
            loadTables.loadReportTableWithFilter(filterTempReport.getColumnIndex(), filterTempReport.getFilterCriteria());
            setColumnFormat(columnWidthPercentage2, reportTable);
            GUI.columnFilterStatus(filterTempReport.getColumnIndex(), reportTable);

        } else {
            loadTables.loadArchiveTableWithFilter(filterTempArchive.getColumnIndex(), filterTempArchive.getFilterCriteria());
            setColumnFormat(columnWidthPercentage1, archiveAssignTable);
            GUI.columnFilterStatus(filterTempArchive.getColumnIndex(), archiveAssignTable);
        }
    }
    private void changeTabbedPanelState(String selectedTab) {
        //To remember previous filter or create a filter if the table is not filtered.//
        assignmentTable.setName(ASSIGNMENTS_TABLE_NAME);
        reportTable.setName(REPORTS_TABLE_NAME);
        archiveAssignTable.setName(ARCHIVE_TABLE_NAME);

        switch (selectedTab) {
            case ASSIGNMENTS_TABLE_NAME:
                jActivateRecord.setEnabled(false);
                jArchiveRecord.setEnabled(true);
                if (GUI.filterAssignmentIsActive == true) {
                    assignmentTable = assignmentFiltered;
                } else {
                    filterTempAssignment = TableRowFilterSupport.forTable(assignmentTable).actions(true)
                            .apply();
                    assignmentFiltered = filterTempAssignment.getTable();
                }
                numOfRecords1.setText("Number of records in Assignments: " + numberAssignmentInit);
                numOfRecords2.setText("Number of records shown: " + assignmentFiltered.getRowCount());
                break;
            case REPORTS_TABLE_NAME:
                jActivateRecord.setEnabled(false);
                jArchiveRecord.setEnabled(false);
                if (GUI.filterReportIstActive == false) {
                    filterTempReport = TableRowFilterSupport.forTable(reportTable).actions(true)
                            .apply();
                    reportFiltered = filterTempReport.getTable();
                } else {
                    reportTable = reportFiltered;
                }
                numOfRecords1.setText("Number of records in Reports: " + numberReportsInit);
                numOfRecords2.setText("Number of records shown: " + reportFiltered.getRowCount());
                break;
            case ARCHIVE_TABLE_NAME:
                jActivateRecord.setEnabled(true);
                jArchiveRecord.setEnabled(false);
                if (GUI.filterArchiveIsActive == false) {
                    filterTempArchive = TableRowFilterSupport.forTable(archiveAssignTable).actions(true)
                            .apply();
                    archiveAssignFiltered = filterTempArchive.getTable();
                } else {
                    archiveAssignTable = archiveAssignFiltered;
                }
                numOfRecords1.setText("Number of records in Archive: " + numberArchiveAssignInit);
                numOfRecords2.setText("Number of records shown: " + archiveAssignFiltered.getRowCount());
                break;
            case "Viewer":
                jActivateRecord.setEnabled(false);
                jArchiveRecord.setEnabled(true);
                viewerTable = assignmentTable;
                viewerTable.setVisible(true);
                numOfRecords1.setText("Number of records in Assignments: " + numberAssignmentInit);
                numOfRecords2.setText("Number of records shown: " + assignmentFiltered.getRowCount());
                break;
        }

    }

    private void jBatchEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBatchEditActionPerformed
        TableEditor tableEditor = new TableEditor(jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex()), this);
        tableEditor.setVisible(true);

    }//GEN-LAST:event_jBatchEditActionPerformed

    private void jMenuEditDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuEditDBActionPerformed
        new EditDatabaseList().setVisible(true);
    }//GEN-LAST:event_jMenuEditDBActionPerformed

    private void jBatchAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBatchAddActionPerformed
        new AddRecords(this, logwind).setVisible(true);
    }//GEN-LAST:event_jBatchAddActionPerformed

    private void textForSearchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_textForSearchKeyPressed
        String selectedTab = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            filterBySearchButton(selectedTab);
        }
    }//GEN-LAST:event_textForSearchKeyPressed

    private void jMenuItemLogOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogOffActionPerformed
        Object[] options = {"Reconnect", "Log Out"};  // the titles of buttons

        int n = JOptionPane.showOptionDialog(null, "Would you like to reconnect?", "Log off",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title

        switch (n) {
            case 0: {               // Reconnect
                new LoginWindow(this).setVisible(true);
                break;
            }
            case 1:
                System.exit(1); // Quit
        }
    }//GEN-LAST:event_jMenuItemLogOffActionPerformed

    private void jDeleteRecordActionPerformed(java.awt.event.ActionEvent evt) {
        DeleteRecord deleteRecord = new DeleteRecord();
        JTable table = null;
        String tableName = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());

        if (null != tableName) {
            switch (tableName) {
                case ASSIGNMENTS_TABLE_NAME:
                    table = assignmentTable;
                    break;
                case REPORTS_TABLE_NAME:
                    table = reportTable;
                    break;
                default:
                    table = archiveAssignTable;
                    break;
            }
        }
        deleteRecord.deleteRecordSelected(table);
    }


    private void jMenuItemViewAllAssigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewAllAssigActionPerformed
        loadData();
    }//GEN-LAST:event_jMenuItemViewAllAssigActionPerformed

    private void jMenuItemViewActiveAssigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewActiveAssigActionPerformed
        loadActiveData();
    }//GEN-LAST:event_jMenuItemViewActiveAssigActionPerformed

    private void jButtonClearAllFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearAllFilterActionPerformed

        clearAllFilters();

    }//GEN-LAST:event_jButtonClearAllFilterActionPerformed

    private void clearAllFilters() {
        switch (jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex())) {
            case ASSIGNMENTS_TABLE_NAME:

                GUI.filterAssignmentIsActive = false;
                loadTables.loadAssignmentTable();
                GUI.cleanAllColumnFilterStatus(assignmentTable);
                break;
            case REPORTS_TABLE_NAME:
                GUI.filterReportIstActive = false;
                loadTables.loadReportTable();
                numOfRecords1.setText("Number of records in Report: " + reportTable.getRowCount());
                numOfRecords2.setText("Number of records shown: " + reportTable.getRowCount());
                filterTempReport = TableRowFilterSupport.forTable(reportTable).actions(true).apply();
                filterTempReport.getTable();
                GUI.cleanAllColumnFilterStatus(reportTable);
                break;
            case ARCHIVE_TABLE_NAME:
                GUI.filterArchiveIsActive = false;
                loadTables.loadArchiveAssignTable();
                filterTempArchive = TableRowFilterSupport.forTable(archiveAssignTable).actions(true).apply();//
                filterTempArchive.getTable();
                numOfRecords1.setText("Number of records in Assignments Archive: " + archiveAssignTable.getRowCount());
                numOfRecords2.setText("Number of records shown: " + archiveAssignTable.getRowCount());
                GUI.cleanAllColumnFilterStatus(archiveAssignTable);
                break;
        }
        modifiedDataList.clear();

    }

    private void jMenuItemOthersLoadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOthersLoadDataActionPerformed
        String titleAt = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
        if (titleAt.equals(ASSIGNMENTS_TABLE_NAME)) {
            loadTables.loadAssignmentTable();
        } else if (titleAt.equals(REPORTS_TABLE_NAME)) {
            loadTables.loadReportTable();
            numOfRecords1.setText("Number of records in Report: " + reportTable.getRowCount());
            numOfRecords2.setText("Number of records shown: " + reportTable.getRowCount());
            filterTempReport = TableRowFilterSupport.forTable(reportTable).actions(true).apply();
            filterTempReport.getTable();
        } else {
            loadTables.loadArchiveAssignTable();
            filterTempArchive = TableRowFilterSupport.forTable(archiveAssignTable).actions(true).apply();//
            filterTempArchive.getTable();
            numOfRecords1.setText("Number of records in Assignments Archive: " + archiveAssignTable.getRowCount());
            numOfRecords2.setText("Number of records shown: " + archiveAssignTable.getRowCount());
        }
    }//GEN-LAST:event_jMenuItemOthersLoadDataActionPerformed

    private void jArchiveRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jArchiveRecordActionPerformed
        tableService.archiveRecords();

    }//GEN-LAST:event_jArchiveRecordActionPerformed

    private void jActivateRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jActivateRecordActionPerformed
        tableService.activateRecords();

    }//GEN-LAST:event_jActivateRecordActionPerformed

//Filter is generated everytime that table is selected.
    private void jTabbedPanel1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPanel1StateChanged

        String selectedTab = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
        TableState ts = getTableState();
        changeTabbedPanelState(selectedTab);
        String[] field = ts.getSearchFields();
        if (field == null) {
            jField.setModel(new DefaultComboBoxModel(new String[]{"Symbol", "Analyst"}));
        } else {
            jField.setModel(new DefaultComboBoxModel(field));
        }

        modifiedDataList.clear();    // when selected table changed, clear former edit history
    }//GEN-LAST:event_jTabbedPanel1StateChanged

    private void jTableChanged(TableModelEvent e) {

        int row = e.getFirstRow();
        int col = e.getColumn();
        int id = 0;
        Object value = null;
        String tableName = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
        switch (tableName) {
            case ASSIGNMENTS_TABLE_NAME:
                id = (Integer) assignmentTable.getModel().getValueAt(row, 0);
                value = assignmentTable.getModel().getValueAt(row, col);
                break;
            case REPORTS_TABLE_NAME:
                id = (Integer) reportTable.getModel().getValueAt(row, 0);
                value = reportTable.getModel().getValueAt(row, col);
                break;
            case ARCHIVE_TABLE_NAME:
                id = (Integer) archiveAssignTable.getModel().getValueAt(row, 0);
                value = archiveAssignTable.getModel().getValueAt(row, col);
                break;
        }

        ModifiedData modifiedData = new ModifiedData();
        modifiedData.setColumnIndex(col);
        modifiedData.setId(id);
        modifiedData.setTableName(tableName);
        modifiedData.setValueModified(value);
        modifiedDataList.add(modifiedData);
    }

    public void loadData() {
        loadTables = new LoadTables();
        loadTables.loadTables();
         filterTempAssignment = TableRowFilterSupport.forTable(assignmentTable).actions(true)
                            .apply();
                    assignmentFiltered = filterTempAssignment.getTable(); 
    }

    public void setTerminalsFunction(final JTable table) { //set all listenner for JTable.
        header = table.getTableHeader();
        if (header != null) {
            header.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        if (isFiltering) {
                            clearFilterDoubleClick(e, table);
                        }
                    } else if (e.getClickCount() == 1) {

                    }
                }
            });
        }
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_F2) {
                    makeTableEditable();
                } else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
//                    GUI.isChangesUploaded=true;
                }
            }
        }
        );
        table.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            filterByDoubleClick(table);
                        } else if (e.getClickCount() == 1) {
                            if (jLabelEdit.getText().equals("ON ")) {
                                selectAllText(e);
                            }
                        }
                    }

                    private void selectAllText(MouseEvent e) {// Select all text inside jTextField

                        JTable table = (JTable) e.getComponent();
                        int row = table.getSelectedRow();
                        int column = table.getSelectedColumn();
                        if (column != 0) {
                            table.getComponentAt(row, column).requestFocus();
                            table.editCellAt(row, column);
                            JTextField selectCom = (JTextField) table.getEditorComponent();
                            if (selectCom != null) {
                                selectCom.requestFocusInWindow();
                                selectCom.selectAll();
                            }
                        }

                    }
                }
        );

    }

    public void filterByDoubleClick(JTable table) {

        int[] columnIndex = table.getColumnModel().getSelectedColumns();
        int rowIndex = table.getSelectedRow();
        if (rowIndex != -1) {
            Object selectedField = table.getValueAt(rowIndex, columnIndex[0]);
            if (table.getName().equals(ASSIGNMENTS_TABLE_NAME)) {
                filterTempAssignment = TableRowFilterSupport.forTable(assignmentTable).actions(true).apply();
                filterTempAssignment.apply(columnIndex[0], selectedField);
                GUI.columnFilterStatus(columnIndex[0], filterTempAssignment.getTable());
                numOfRecords1.setText("Number of records in Assignments: " + numberAssignmentInit);
            } else if (table.getName().equals(REPORTS_TABLE_NAME)) {
                filterTempReport = TableRowFilterSupport.forTable(reportTable).actions(true) // D, Delete if you want to have multiple filters (for example: Symbol + analyster at the same time)
                        .apply();
                filterTempReport.apply(columnIndex[0], selectedField);
                GUI.columnFilterStatus(columnIndex[0], filterTempReport.getTable());
                numOfRecords1.setText("Number of records in Reports: " + numberReportsInit);
            } else {
                filterTempArchive = TableRowFilterSupport.forTable(archiveAssignTable).actions(true) // D, Delete if you want to have multiple filters (for example: Symbol + analyster at the same time)
                        .apply();
                filterTempArchive.apply(columnIndex[0], selectedField);
                GUI.columnFilterStatus(columnIndex[0], filterTempArchive.getTable());
                numOfRecords1.setText("Number of records in Archive: " + numberArchiveAssignInit);

            }
        }
    }

    private void clearFilterDoubleClick(MouseEvent e, JTable table) {
        int columnIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
        if (table.getName().equals(ASSIGNMENTS_TABLE_NAME)) {
            filterTempAssignment.apply(columnIndex, filterTempAssignment.getDistinctColumnItems(columnIndex));
            GUI.cleanColumnFilterStatus(columnIndex, filterTempAssignment.getTable());// clean green background
            numOfRecords1.setText("Number of records in Assignments: " + numberAssignmentInit);
        } else if (table.getName().equals(REPORTS_TABLE_NAME)) {
            filterTempReport.apply(columnIndex, filterTempReport.getDistinctColumnItems(columnIndex));
            GUI.cleanColumnFilterStatus(columnIndex, filterTempReport.getTable());// clean green background
            numOfRecords1.setText("Number of records in Reports: " + numberReportsInit);
        } else {
            filterTempArchive.apply(columnIndex, filterTempArchive.getDistinctColumnItems(columnIndex));
            GUI.cleanColumnFilterStatus(columnIndex, filterTempArchive.getTable());// clean green background
            numOfRecords1.setText("Number of records in Archive: " + numberArchiveAssignInit);

        }
    }

    public String sqlQuery(String tableName) { //Creat Query to select * from DB.
        System.out.println("Connection");
        String SqlQuery = "SELECT * FROM " + tableName + " ORDER BY symbol ASC";
        return SqlQuery;
    }

    public void loadActiveData() {// load only active data from analyst
        System.out.println("Connection");
        String sqlC = "select A.* from Assignments A left join t_analysts T\n" + "on A.analyst = T.analyst\n" + "where T.active = 1\n" + "order by A.symbol";
        Connection.connection(sqlC, assignmentTable);
        setColumnFormat(columnWidthPercentage1, assignmentTable);
        assignments.init(assignmentTable, new String[]{"Symbol", "Analyst"});
        numOfRecords1.setText("Number of records in Assignments:" + assignments.getRowsNumber());

    }

    public void tableReload(final JTable table, Vector data, Vector columnNames) {
        MyTableModel model = new MyTableModel(data, columnNames, isFiltering);
        TableRowSorter sorter = new TableRowSorter<>(model);

        model.addTableModelListener(new TableModelListener() {  // add table model listener every time the table model reloaded
            @Override
            public void tableChanged(TableModelEvent e) {
                jTableChanged(e);
            }
        });

        table.setModel(model);
        table.setRowSorter(sorter);
        setColumnFormat(columnWidthPercentage1, assignmentTable);
        setColumnFormat(columnWidthPercentage2, reportTable);
        setColumnFormat(columnWidthPercentage1, archiveAssignTable);
    }

    void setColumnFormat(float[] width, JTable table) {
        // Center column content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //LEFT column content
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        //Center column header
        int widthFixedColumns = 0;
        header = table.getTableHeader();
        if (!(header.getDefaultRenderer() instanceof AlignmentTableHeaderCellRenderer)) {
            header.setDefaultRenderer(new AlignmentTableHeaderCellRenderer(header.getDefaultRenderer()));
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        switch (table.getName()) {

            case REPORTS_TABLE_NAME: {
                int i;
                for (i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i >= width.length - 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    widthFixedColumns += pWidth;
                }
                Double tw = jPanel5.getSize().getWidth();
                int twi = tw.intValue();
                table.getColumnModel().getColumn(width.length).setPreferredWidth(twi - (widthFixedColumns + 25));
                table.setMinimumSize(new Dimension(908, 300));
                table.setPreferredScrollableViewportSize(new Dimension(908, 300));
                break;
            }
            default:
                for (int i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    widthFixedColumns += pWidth;
                }
                Double tw = jPanel5.getSize().getWidth();
                int twi = tw.intValue();
                table.getColumnModel().getColumn(width.length).setPreferredWidth(twi - (widthFixedColumns + 25));
                table.setMinimumSize(new Dimension(908, 300));
                table.setPreferredScrollableViewportSize(new Dimension(908, 300));
                break;

        }
    }

   

    class AlignmentTableHeaderCellRenderer implements TableCellRenderer {

        private final TableCellRenderer wrappedRenderer;
        private final JLabel label;

        public AlignmentTableHeaderCellRenderer(TableCellRenderer wrappedRenderer) {
            if (!(wrappedRenderer instanceof JLabel)) {
                throw new IllegalArgumentException("The supplied renderer must inherit from JLabel");
            }
            this.wrappedRenderer = wrappedRenderer;
            this.label = (JLabel) wrappedRenderer;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            wrappedRenderer.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            if (table.getName().equals(REPORTS_TABLE_NAME)) {

                if (column < table.getColumnCount() - 4) {
                    label.setHorizontalAlignment(JLabel.CENTER);
                    return label;
                } else {
                    label.setHorizontalAlignment(JLabel.LEFT);
                    return label;
                }
            }

            label.setHorizontalAlignment(column == table.getColumnCount() - 1 ? JLabel.LEFT : JLabel.CENTER);
            return label;

        }

    }

    // Keep the float in Table Editor by separating editing part out here
    public void batchEdit(TableEditor editor) {
        
        int row[], id, col = 1, i, j, num;
        JTable table = getSelectedTable();   // current Table
        String newString, columnName;
        table.setAutoCreateRowSorter(false);
        List<ModifiedData> modifiedDataBatchEdit = new ArrayList<>();
        newString = editor.newString;
        row = table.getSelectedRows();
        num = table.getSelectedRowCount();
        columnName = editor.category;
        for (i = 0; i < table.getColumnCount(); i++) {
            if (columnName.equals(table.getColumnName(i))) {
                col = i;
                break;
            }
        }
            for (i = 0; i <= num - 1; i++) {
                int row2 = table.convertRowIndexToModel(row[i]);
                id = (Integer)table.getModel().getValueAt(row2,0);
                ModifiedData modifiedData = new ModifiedData();
                modifiedData.setColumnIndex(col);
                modifiedData.setTableName(table.getName());
                modifiedData.setId(id);
                modifiedData.setValueModified(newString);
                modifiedDataBatchEdit.add(modifiedData);
            }
            updateTable(table, modifiedDataBatchEdit);

    }

    private void updateTable(JTable table, List<ModifiedData> modifiedDataList) {
        table.getModel().addTableModelListener(table);
        try {
            String uploadQuery = uploadRecordService.uploadRecord(table, modifiedDataList);
            loadPrevious(table.getName());

            JOptionPane.showMessageDialog(null, "Edits uploaded!");
            logwind.sendMessages(uploadQuery);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Upload failed!");
            logwind.sendMessages(e.getMessage());
            logwind.sendMessages(e.getSQLState() + "\n");
        }
    }

    public JTable getSelectedTable() {  //get JTable by  selected Tab
        String selectedTab = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
        switch (selectedTab) {
            case ASSIGNMENTS_TABLE_NAME:
                return assignmentTable;
            case REPORTS_TABLE_NAME:
                return reportTable;
            default:
                return archiveAssignTable;
        }
    }

    public TableState getTableState() {                 // get TableState by selected Tab
        String selectedTab = jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
        //  A switch works with the byte, short, char, and int primitive data types.
        switch (selectedTab) {
            case ASSIGNMENTS_TABLE_NAME:
                return assignments;
            case REPORTS_TABLE_NAME:
                return reports;
            case ARCHIVE_TABLE_NAME:
                return archiveAssign;
            case "Viewer":
                return viewer;
            default:
                return null;
        }
    }

    public TableState getTableState(JTable table) {     // get TableState by jTable object
        if (table == assignmentTable) {
            return assignments;
        } else if (table == reportTable) {
            return reports;
        } else if (table == archiveAssignTable) {
            return archiveAssign;
        } else if (table == viewerTable) {
            return viewer;
        } else {
            JOptionPane.showMessageDialog(null, "TableState not found!");
            return null;
        }
    }

    public void setLastUpdateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        jTimeLastUpdate.setText("Last updated: " + time);
    }

    public JLabel getNumOfRecords2() {
        return numOfRecords2;
    }

    public LogWindow getLogwind() {
        return logwind;
    }

    public List<ModifiedData> getModifiedDataList() {
        return modifiedDataList;
    }

    public TableState getAssignments() {
        return assignments;
    }

    public TableState getViewer() {
        return viewer;
    }

    public TableState getReports() {
        return reports;
    }

    public TableState getArchiveAssign() {
        return archiveAssign;
    }

    public ITableFilter<?> getFilterTempAssignment() {
        return filterTempAssignment;
    }
    public TableState viewer = new TableState();
    public TableState assignments = new TableState();
    public TableState reports = new TableState();
    public TableState archiveAssign = new TableState();

    public EnterButton enterButton = new EnterButton();
    public LogWindow logwind = new LogWindow();

    protected static boolean isFiltering = true;
    private List<ModifiedData> modifiedDataList = new ArrayList<>();    // record the locations of changed cell
    // @formatter:off
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addPanel_control;
    private javax.swing.JTable archiveAssignTable;
    private javax.swing.JTable assignmentTable;
    private javax.swing.JMenuItem jActivateRecord;
    private javax.swing.JMenuItem jArchiveRecord;
    private javax.swing.JButton jBatchAdd;
    private javax.swing.JButton jBatchEdit;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonClearAllFilter;
    private javax.swing.JButton jDebugCancel;
    private javax.swing.JButton jDebugEnter;
    private javax.swing.JMenuItem jDeleteRecord;
    private javax.swing.JComboBox jField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelDebug;
    private javax.swing.JLabel jLabelEdit;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenuItem jMenuEditDB;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuFind;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItemFileVersion;
    private javax.swing.JMenuItem jMenuItemLogOff;
    private javax.swing.JMenuItem jMenuItemOtherReport;
    private javax.swing.JMenuItem jMenuItemOthersLoadData;
    private javax.swing.JMenuItem jMenuItemPrintDisplay;
    private javax.swing.JMenuItem jMenuItemPrintGUI;
    private javax.swing.JMenuItem jMenuItemSaveFile;
    private javax.swing.JMenuItem jMenuItemViewActiveAssig;
    private javax.swing.JMenuItem jMenuItemViewAllAssig;
    private javax.swing.JMenuItem jMenuItemViewAssig;
    private javax.swing.JMenuItem jMenuItemViewLog;
    private javax.swing.JMenuItem jMenuItemViewReports;
    private javax.swing.JMenuItem jMenuItemViewSQL;
    private javax.swing.JMenu jMenuOther;
    private javax.swing.JMenu jMenuPrint;
    private javax.swing.JMenu jMenuReport;
    private javax.swing.JMenu jMenuSelectConn;
    private javax.swing.JMenu jMenuView;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton jSwitchDebugMode;
    private javax.swing.JButton jSwitchEditMode;
    private javax.swing.JTabbedPane jTabbedPanel1;
    private javax.swing.JTextArea jTextArea;
    private javax.swing.JLabel jTimeLastUpdate;
    private javax.swing.JButton jUpload;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel numOfRecords1;
    private javax.swing.JLabel numOfRecords2;
    private javax.swing.JTable reportTable;
    private javax.swing.JButton search;
    private javax.swing.JTextField textForSearch;
    private javax.swing.JTable viewerTable;
    // End of variables declaration//GEN-END:variables
    // @formatter:on
}
