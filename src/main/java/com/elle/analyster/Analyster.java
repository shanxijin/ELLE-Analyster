package com.elle.analyster;

import com.elle.analyster.db.ExecuteSQLStatement;
import com.elle.analyster.domain.ModifiedData;
import com.elle.analyster.presentation.filter.CreateDocumentFilter;
import com.elle.analyster.presentation.filter.ITableFilter;
import com.elle.analyster.presentation.filter.TableRowFilterSupport;
import static com.elle.analyster.service.Connection.connection;
import com.elle.analyster.service.DeleteRecord;
import com.elle.analyster.service.TableService;
import com.elle.analyster.service.UploadRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Analyster extends JFrame implements ITableNameConstants, IColumnConstants{
    
    
    Map<String,Tab> tabs = new HashMap<>(); // stores individual tab information

    private final TableService tableService = new TableService();
    private LoadTables loadTables;
    private JTableHeader header;
    
    private static Analyster instance;
    private Logger log = LoggerFactory.getLogger(Analyster.class);
    private LogWindow logwind = new LogWindow(); 
    
    public EnterButton enterButton = new EnterButton();
    protected static boolean isFiltering = true;
    private List<ModifiedData> modifiedDataList = new ArrayList<>();    // record the locations of changed cell
    
    @Autowired
    private UploadRecord uploadRecordService;
    
    LoginWindow loginWindow;
    
    /**
     * CONSTRUCTOR
     */
    public Analyster() {
        
        instance = this; // this is used to call an instance of Analyster  

        // create tab objects -> this has to be before initcomponents();
        tabs.put(ASSIGNMENTS_TABLE_NAME, new Tab());
        tabs.put(REPORTS_TABLE_NAME, new Tab());
        tabs.put(ARCHIVE_TABLE_NAME, new Tab());
        
        // set table names -> this has to be before initcomponents();
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTableName(ASSIGNMENTS_TABLE_NAME);
        tabs.get(REPORTS_TABLE_NAME).setTableName(REPORTS_TABLE_NAME);
        tabs.get(ARCHIVE_TABLE_NAME).setTableName(ARCHIVE_TABLE_NAME);
        
        initComponents(); // generated code
        
        // set names to tables (this was in tabbedPanelChanged method)
        assignmentTable.setName(ASSIGNMENTS_TABLE_NAME);
        reportTable.setName(REPORTS_TABLE_NAME);
        archiveAssignTable.setName(ARCHIVE_TABLE_NAME);
        
        // I COMMENTED THESE OUT AND IT WORKED FINE !!
        tableService.setAssignmentTable(assignmentTable);
        tableService.setReportTable(reportTable);
        tableService.setArchiveAssignTable(archiveAssignTable);
        tableService.setViewerTable(assignmentTable);
        
        // set tables to tab objects
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTable(assignmentTable);
        tabs.get(REPORTS_TABLE_NAME).setTable(reportTable);
        tabs.get(ARCHIVE_TABLE_NAME).setTable(archiveAssignTable);
        
        // set table states to tab objects
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTableState(new TableState(assignmentTable));
        tabs.get(REPORTS_TABLE_NAME).setTableState(new TableState(reportTable));
        tabs.get(ARCHIVE_TABLE_NAME).setTableState(new TableState(archiveAssignTable));
        
        // set column width percents to tables of the tab objects
        tabs.get(ASSIGNMENTS_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_ASSIGNMENTS);
        tabs.get(REPORTS_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_REPORTS);
        tabs.get(ARCHIVE_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_ARCHIVE);
        
        setKeyboardFocusManager();

        // show and hide components
        jUpload.setVisible(false);
        jPanelSQL.setVisible(false); 
        jDebugEnter.setVisible(true);
        jDebugCancel.setVisible(true);
        jButtonCancel.setVisible(false);
        jBatchEdit.setVisible(true);
        jTextArea.setVisible(true);

        // set title of window to Analyster
        this.setTitle("Analyster");
        
        // initialize loadTables
        loadTables = new LoadTables();
        
        // create a login window instance
        loginWindow = new LoginWindow(this);
        
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
        jTimeLastUpdate = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        search = new javax.swing.JButton();
        textForSearch = new javax.swing.JTextField();
        jField = new javax.swing.JComboBox();
        jButtonClearAllFilter = new javax.swing.JButton();
        recordsLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPanel1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        assignmentTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        archiveAssignTable = new javax.swing.JTable();
        jPanelEdit = new javax.swing.JPanel();
        jBatchEdit = new javax.swing.JButton();
        jBatchAdd = new javax.swing.JButton();
        jUpload = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jSwitchEditMode = new javax.swing.JButton();
        jLabelEdit = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanelSQL = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();
        jDebugEnter = new javax.swing.JButton();
        jDebugCancel = new javax.swing.JButton();
        closeDebugPanelBtn = new javax.swing.JButton();
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
        jMenuItemViewAssig = new javax.swing.JMenuItem();
        jMenuItemViewReports = new javax.swing.JMenuItem();
        jMenuItemViewAllAssig = new javax.swing.JMenuItem();
        jMenuItemViewActiveAssig = new javax.swing.JMenuItem();
        jMenuOther = new javax.swing.JMenu();
        jMenuItemOthersLoadData = new javax.swing.JMenuItem();
        jCheckBoxMenuItemViewLog = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemViewSQL = new javax.swing.JCheckBoxMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemOtherReport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(894, 560));

        jTimeLastUpdate.setText("Last updated: ");

        search.setText("Search");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

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

        jField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "symbol", "analyster" }));

        jButtonClearAllFilter.setText("Clear All Filters");
        jButtonClearAllFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearAllFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addComponent(jButtonClearAllFilter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addGap(202, 202, 202)
                        .addComponent(textForSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(search)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textForSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search)
                    .addComponent(jField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonClearAllFilter))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        recordsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        recordsLabel.setText("jLabel1");

        javax.swing.GroupLayout addPanel_controlLayout = new javax.swing.GroupLayout(addPanel_control);
        addPanel_control.setLayout(addPanel_controlLayout);
        addPanel_controlLayout.setHorizontalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(recordsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addGap(258, 258, 258)
                        .addComponent(jTimeLastUpdate)))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        addPanel_controlLayout.setVerticalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTimeLastUpdate)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPanel1.setPreferredSize(new java.awt.Dimension(800, 584));
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
        assignmentTable.setRequestFocusEnabled(false);
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

        jPanelEdit.setPreferredSize(new java.awt.Dimension(636, 180));

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

        jUpload.setText("Upload Changes");
        jUpload.setMaximumSize(new java.awt.Dimension(95, 30));
        jUpload.setMinimumSize(new java.awt.Dimension(95, 30));
        jUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUploadActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jSwitchEditMode.setText("Switch");
        jSwitchEditMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSwitchEditModeActionPerformed(evt);
            }
        });

        jLabelEdit.setText("OFF");

        jLabel2.setText("Edit Mode:");

        javax.swing.GroupLayout jPanelEditLayout = new javax.swing.GroupLayout(jPanelEdit);
        jPanelEdit.setLayout(jPanelEditLayout);
        jPanelEditLayout.setHorizontalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
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
        jPanelEditLayout.setVerticalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jUpload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jSwitchEditMode)
                    .addComponent(jLabelEdit)
                    .addComponent(jButtonCancel)
                    .addComponent(jBatchEdit)
                    .addComponent(jBatchAdd))
                .addGap(4, 4, 4))
        );

        jScrollPane2.setBorder(null);

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

        closeDebugPanelBtn.setText("Close");
        closeDebugPanelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeDebugPanelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSQLLayout = new javax.swing.GroupLayout(jPanelSQL);
        jPanelSQL.setLayout(jPanelSQLLayout);
        jPanelSQLLayout.setHorizontalGroup(
            jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSQLLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDebugCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDebugEnter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(closeDebugPanelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE))
        );
        jPanelSQLLayout.setVerticalGroup(
            jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSQLLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanelSQLLayout.createSequentialGroup()
                        .addComponent(jDebugEnter)
                        .addGap(4, 4, 4)
                        .addComponent(jDebugCancel)
                        .addGap(4, 4, 4)
                        .addComponent(closeDebugPanelBtn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE)
            .addComponent(jPanelEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jTabbedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPanel1.getAccessibleContext().setAccessibleName("Reports");
        jTabbedPanel1.getAccessibleContext().setAccessibleParent(jTabbedPanel1);

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

        jMenuOther.setText("Tools");

        jMenuItemOthersLoadData.setText("Reload data");
        jMenuItemOthersLoadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOthersLoadDataActionPerformed(evt);
            }
        });
        jMenuOther.add(jMenuItemOthersLoadData);

        jCheckBoxMenuItemViewLog.setText("Log");
        jCheckBoxMenuItemViewLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemViewLogActionPerformed(evt);
            }
        });
        jMenuOther.add(jCheckBoxMenuItemViewLog);

        jCheckBoxMenuItemViewSQL.setText("SQL Command");
        jCheckBoxMenuItemViewSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemViewSQLActionPerformed(evt);
            }
        });
        jMenuOther.add(jCheckBoxMenuItemViewSQL);

        menuBar.add(jMenuOther);

        jMenuHelp.setText("Help");

        jMenuItemOtherReport.setText("Report a bug/suggestion");
        jMenuItemOtherReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOtherReportActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemOtherReport);

        menuBar.add(jMenuHelp);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addPanel_control, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(addPanel_control, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemFileVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFileVersionActionPerformed

        JOptionPane.showMessageDialog(this, "Creation Date: "
                + "2015-07-1" + "\n"
                + "Version: " + "0.6.5c");
    }//GEN-LAST:event_jMenuItemFileVersionActionPerformed

    private void textForSearchMouseClicked(MouseEvent evt) {//GEN-FIRST:event_textForSearchMouseClicked

        textForSearch.setText(null);
    }//GEN-LAST:event_textForSearchMouseClicked

    /**
     * This method is called when the search button is pressed
     * @param evt 
     */
    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
       filterBySearch();
    }//GEN-LAST:event_searchActionPerformed
    
    /**
     * This method is called by the searchActionPerformed method
     * and the textForSearchKeyPressed method
     */
    public void filterBySearch() {
        
        String selectedTab = getSelectedTab(); // get the selected tab
        
        int columnIndex; // the column of the table
        
        if (jField.getSelectedItem().toString().equals(SYMBOL_COLUMN_NAME)) {
            columnIndex = 1; // first column is the symbol column
        } else {
            columnIndex = 2; // the second column is the analyst column
        }
        
        String selectedField = textForSearch.getText();  // store string from text box
        
        try{
            TableRowFilterSupport.forTable(tabs.get(selectedTab).getTable()).actions(true).apply().apply(columnIndex, selectedField);
            GUI.columnFilterStatus(columnIndex, tabs.get(selectedTab).getFilter().getTable());
            // set label record information
            recordsLabel.setText(tabs.get(selectedTab).getRecordsLabel()); 
            
        }catch(NullPointerException e){
            throwUnknownTableException(selectedTab, e);
        }    
    }
    
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

        log.info("Connection");

        try{
            String sqlC = "select * from " + ASSIGNMENTS_TABLE_NAME;
            connection(sqlC, assignmentTable);
            log.info("Connection");
            String sqlD = "select * from " + REPORTS_TABLE_NAME;
            connection(sqlD, reportTable);
        } catch (SQLException ex) {
            logwind.sendMessages(ex.getMessage());
        }

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUploadActionPerformed
        // upload two tables separately
        
        String selectedTab = getSelectedTab();
        
        updateTable(tabs.get(selectedTab).getTable(), modifiedDataList);

        if (GUI.isIsFiltering()) {
//            loadPrevious(selectedTab);
        } else {         
            loadTables.loadTable(tabs.get(selectedTab).getTable());
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
            try {
                connection(enterButton.getCommand(jTextArea), assignmentTable);
            } catch (SQLException e) {
                logwind.sendMessages(e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
            }
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

    private void closeDebugPanelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeDebugPanelBtnActionPerformed

        jPanelSQL.setVisible(false);
        jCheckBoxMenuItemViewSQL.setSelected(false);
    }//GEN-LAST:event_closeDebugPanelBtnActionPerformed

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
//            ((MyTableModel) viewerTable.getModel()).setReadOnly(true);
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
            loadTables.loadTable(tabs.get(selectedTab).getTable());
        }
        makeTableEditable();

    }//GEN-LAST:event_jButtonCancelActionPerformed
    public void loadPrevious(String selectedTab) {

        // TODO check that the selectedTab is actually needed to be passed in.
        
        try{
            loadTables.loadAssignmentTableWithFilter(tabs.get(selectedTab).getFilter().getColumnIndex(), tabs.get(selectedTab).getFilter().getFilterCriteria());
            setColumnFormat(tabs.get(selectedTab).getColWidthPercent(), tabs.get(selectedTab).getTable());
            GUI.columnFilterStatus(tabs.get(selectedTab).getFilter().getColumnIndex(), tabs.get(selectedTab).getTable());
            // set label record information
            recordsLabel.setText(tabs.get(selectedTab).getRecordsLabel()); 
        }catch(NullPointerException e){
            throwUnknownTableException(selectedTab, e);
        }
    }
    
    private void changeTabbedPanelState() {
        
        String selectedTab = getSelectedTab();

        // pointers to information
        boolean isFilterActive = false;
        
        switch (selectedTab) {
            case ASSIGNMENTS_TABLE_NAME:
                jActivateRecord.setEnabled(false);
                jArchiveRecord.setEnabled(true);
                isFilterActive = GUI.filterAssignmentIsActive;
                break;
                
            case REPORTS_TABLE_NAME:
                jActivateRecord.setEnabled(false);
                jArchiveRecord.setEnabled(false);
                isFilterActive = GUI.filterReportIstActive;
                break;
                
            case ARCHIVE_TABLE_NAME:
                jActivateRecord.setEnabled(true);
                jArchiveRecord.setEnabled(false);
                isFilterActive = GUI.filterArchiveIsActive;
                break;

            default:
                selectedTab = "unknown";
                break;
        }
        
        try{
            if (isFilterActive) {
            tabs.get(selectedTab).setTable(tabs.get(selectedTab).getFilteredTable());
            } else {
                tabs.get(selectedTab).setFilter(TableRowFilterSupport.forTable(tabs.get(selectedTab).getTable()).actions(true).apply());
                tabs.get(selectedTab).setFilteredTable(tabs.get(selectedTab).getFilter().getTable());
            }
            
            // set label record information
            recordsLabel.setText(tabs.get(selectedTab).getRecordsLabel()); 
        }catch(NullPointerException e){
            throwUnknownTableException(selectedTab, e);
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

    /**
     * This method listens if the enter key was pressed in the search text box.
     * This allows the value to be entered without having to click the 
     * search button.
     * @param evt 
     */
    private void textForSearchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_textForSearchKeyPressed
        
        // if the enter key is pressed call the filterBySearch method.
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            filterBySearch();
        }
    }//GEN-LAST:event_textForSearchKeyPressed

    private void jMenuItemLogOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogOffActionPerformed
        Object[] options = {"Reconnect", "Log Out"};  // the titles of buttons

        int n = JOptionPane.showOptionDialog(this, "Would you like to reconnect?", "Log off",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title

        switch (n) {
            case 0: {               // Reconnect
                
                // hide Analyster
                this.setVisible(false);
                
                // show login window
                loginWindow.setVisible(true);

                break;
            }
            case 1:
                System.exit(0); // Quit
        }
    }//GEN-LAST:event_jMenuItemLogOffActionPerformed

    private void jDeleteRecordActionPerformed(java.awt.event.ActionEvent evt) {
        DeleteRecord deleteRecord = new DeleteRecord();
        String selectedTab = getSelectedTab();
        String sqlDelete;

       try{
            sqlDelete = deleteRecord.deleteRecordSelected(tabs.get(selectedTab).getTable());
            logwind.sendMessages(sqlDelete);
            
            // set label record information
            tabs.get(selectedTab).subtractFromTotalRowCount(1); // update total row count
            recordsLabel.setText(tabs.get(selectedTab).getRecordsLabel()); // update label
            
        }catch(NullPointerException e){
            throwUnknownTableException(selectedTab, e);
        } 
    }


    private void jMenuItemViewAllAssigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewAllAssigActionPerformed
        loadData();
    }//GEN-LAST:event_jMenuItemViewAllAssigActionPerformed

    // load only active data from analyst
    private void jMenuItemViewActiveAssigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewActiveAssigActionPerformed
        log.info("Connection");
        String sqlC = "select A.* from Assignments A left join t_analysts T\n" + "on A.analyst = T.analyst\n" + "where T.active = 1\n" + "order by A.symbol";
        try {
            connection(sqlC, assignmentTable);
        } catch (SQLException e) {
            logwind.sendMessages(e.getMessage());  
        }

        setColumnFormat(tabs.get(ASSIGNMENTS_TABLE_NAME).getColWidthPercent(), assignmentTable);
        tabs.get(ASSIGNMENTS_TABLE_NAME).getTableState().init(assignmentTable, new String[]{"Symbol", "Analyst"});
        // set label record information
        recordsLabel.setText(tabs.get(ASSIGNMENTS_TABLE_NAME).getRecordsLabel()); 
    }//GEN-LAST:event_jMenuItemViewActiveAssigActionPerformed

    private void jButtonClearAllFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearAllFilterActionPerformed

        String selectedTab = getSelectedTab();
        
        switch (selectedTab) {
            case ASSIGNMENTS_TABLE_NAME:
                GUI.filterAssignmentIsActive = false;
                break;
            case REPORTS_TABLE_NAME:
                GUI.filterReportIstActive = false;
                break;
            case ARCHIVE_TABLE_NAME:
                GUI.filterArchiveIsActive = false;
                break;
        }
        
        loadTables.loadTable(tabs.get(selectedTab).getTable());
        GUI.cleanAllColumnFilterStatus(tabs.get(selectedTab).getTable());
        // set label record information
        recordsLabel.setText(tabs.get(selectedTab).getRecordsLabel()); 
                
        modifiedDataList.clear();

    }//GEN-LAST:event_jButtonClearAllFilterActionPerformed


    private void jMenuItemOthersLoadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOthersLoadDataActionPerformed
        
        String selectedTab = getSelectedTab();
        
        loadTables.loadTable(tabs.get(selectedTab).getTable());
        // set label record information
        recordsLabel.setText(tabs.get(selectedTab).getRecordsLabel()); 
    }//GEN-LAST:event_jMenuItemOthersLoadDataActionPerformed

    private void jArchiveRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jArchiveRecordActionPerformed
        tableService.archiveRecords();

    }//GEN-LAST:event_jArchiveRecordActionPerformed

    private void jActivateRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jActivateRecordActionPerformed
        tableService.activateRecords();

    }//GEN-LAST:event_jActivateRecordActionPerformed

//Filter is generated everytime that table is selected.
    private void jTabbedPanel1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPanel1StateChanged

        TableState ts = getTableState();
        changeTabbedPanelState();
        String[] field = ts.getSearchFields();
        if (field == null) {
            jField.setModel(new DefaultComboBoxModel(new String[]{"Symbol", "Analyst"}));
        } else {
            jField.setModel(new DefaultComboBoxModel(field));
        }

        modifiedDataList.clear();    // when selected table changed, clear former edit history
    }//GEN-LAST:event_jTabbedPanel1StateChanged

    private void jCheckBoxMenuItemViewLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemViewLogActionPerformed

        if(jCheckBoxMenuItemViewLog.isSelected()){
            
            logwind.setVisible(true); // show log window
            
            // remove check if window is closed from the window
            logwind.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e){
                        jCheckBoxMenuItemViewLog.setSelected(false);
                    }
                });
        }else{
            // hide log window
            logwind.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxMenuItemViewLogActionPerformed

    private void jCheckBoxMenuItemViewSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemViewSQLActionPerformed
 
        /**
         * ************* Strange behavior *************************
         * The jPanelSQL.getHeight() is the height before 
         * the jCheckBoxMenuItemViewSQLActionPerformed method was called.
         * 
         * The jPanelSQL.setVisible() does not change the size 
         * of the sql panel after it is executed.
         * 
         * The jPanel size will only change after 
         * the jCheckBoxMenuItemViewSQLActionPerformed is finished.
         * 
         * That is why the the actual integer is used rather than  getHeight().
         * 
         * Example:
         * jPanelSQL.setVisible(true);
         * jPanelSQL.getHeight(); // this returns 0
         */
        
        if(jCheckBoxMenuItemViewSQL.isSelected()){
            
            // show sql panel
            jPanelSQL.setVisible(true);
            this.setSize(this.getWidth(), 560 + 112); 
            
        }else{
            
            // hide sql panel
            jPanelSQL.setVisible(false);
            this.setSize(this.getWidth(), 560);
        }
    }//GEN-LAST:event_jCheckBoxMenuItemViewSQLActionPerformed

    private void jTableChanged(TableModelEvent e) {

        int row = e.getFirstRow();
        int col = e.getColumn();
        int id;
        Object value;
        
        String selectedTab = getSelectedTab();
        
        id = (Integer) tabs.get(selectedTab).getTable().getModel().getValueAt(row, 0);
        value = tabs.get(selectedTab).getTable().getModel().getValueAt(row, col);

        ModifiedData modifiedData = new ModifiedData();
        modifiedData.setColumnIndex(col);
        modifiedData.setId(id);
        modifiedData.setTableName(selectedTab);
        modifiedData.setValueModified(value);
        modifiedDataList.add(modifiedData);
    }

    public void loadData() {
        loadTables.loadTables(tabs);
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
            // apply filter
            tabs.get(table.getName()).getFilter().apply(columnIndex[0], selectedField);
            GUI.columnFilterStatus(columnIndex[0], tabs.get(table.getName()).getFilter().getTable());
            // set label record information
            recordsLabel.setText(tabs.get(table.getName()).getRecordsLabel()); 
        }
    }

    /**
     * This is triggered when the the column header is double clicked
     */
    private void clearFilterDoubleClick(MouseEvent e, JTable table) {
        
        int columnIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
        
        tabs.get(table.getName()).getFilter().apply(columnIndex, tabs.get(table.getName()).getFilter().getDistinctColumnItems(columnIndex));
        GUI.cleanColumnFilterStatus(columnIndex, tabs.get(table.getName()).getFilter().getTable());// clean green background
        // set label record information
        recordsLabel.setText(tabs.get(table.getName()).getRecordsLabel()); 
    }

    public String sqlQuery(String tableName) { //Creat Query to select * from DB.
        log.info("Connection");
        String SqlQuery = "SELECT * FROM " + tableName + " ORDER BY symbol ASC";
        return SqlQuery;
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
        setColumnFormat(tabs.get(ASSIGNMENTS_TABLE_NAME).getColWidthPercent(), assignmentTable);
        setColumnFormat(tabs.get(REPORTS_TABLE_NAME).getColWidthPercent(), reportTable);
        setColumnFormat(tabs.get(ARCHIVE_TABLE_NAME).getColWidthPercent(), archiveAssignTable);
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

    /**
     * This method handles unknown table exceptions
     * It takes the value of the selected tab that caused the exception
     * It also takes the exception to print the stack trace
     * @param selectedTab
     */
    public void throwUnknownTableException(String selectedTab, Exception e) {
        try {
            String errorMessage = "ERROR: unknown table: " + selectedTab;
            throw new NoSuchFieldException(errorMessage);
        } catch (NoSuchFieldException ex) {
            ex.setStackTrace(e.getStackTrace());
            ex.printStackTrace();
            // post to log.txt
            getLogwind().sendMessages(ex.getMessage());
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
        String selectedTab = getSelectedTab();
        return tabs.get(selectedTab).getTable();
    }

    public TableState getTableState() {                 // get TableState by selected Tab
        String selectedTab = getSelectedTab();
        return tabs.get(selectedTab).getTableState();
    }

    public TableState getTableState(JTable table) {     // get TableState by jTable object
        return tabs.get(table.getName()).getTableState();
    }

    public void setLastUpdateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        jTimeLastUpdate.setText("Last updated: " + time);
    }
    
    private void setKeyboardFocusManager() {
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
    }
    
    public UploadRecord getUploadRecordService() {
        return uploadRecordService;
    }

    public void setUploadRecordService(UploadRecord uploadRecordService) {
        this.uploadRecordService = uploadRecordService;
    }

    public ITableFilter<?> getFilterTempArchive() {
        return tabs.get(ARCHIVE_TABLE_NAME).getFilter();
    }

    public void setFilterTempArchive(ITableFilter<?> filterTempArchive) {
        tabs.get(ARCHIVE_TABLE_NAME).setFilter(filterTempArchive);
    }

    public ITableFilter<?> getFilterTempReport() {
        return tabs.get(REPORTS_TABLE_NAME).getFilter();
    }

    public void setFilterTempReport(ITableFilter<?> filterTempReport) {
        tabs.get(REPORTS_TABLE_NAME).setFilter(filterTempReport);
    }

    public void setFilterTempAssignment(ITableFilter<?> filterTempAssignment) {
        tabs.get(ASSIGNMENTS_TABLE_NAME).setFilter(filterTempAssignment);
    }

    public void setNumberArchiveAssignInit(int numberArchiveAssignInit) {
        tabs.get(ARCHIVE_TABLE_NAME).setTotalRecords(numberArchiveAssignInit);
    }

    public void setNumberAssignmentInit(int numberAssignmentInit) {
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTotalRecords(numberAssignmentInit);
    }

    public void setNumberReportsInit(int numberReportsInit) {
        tabs.get(REPORTS_TABLE_NAME).setTotalRecords(numberReportsInit);
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
//     JTable getViewerTable() {
//        return viewerTable;
//    }

    public JTabbedPane getjTabbedPanel1() {
        return jTabbedPanel1;
    }

    public JMenuItem getjActivateRecord() {
        return jActivateRecord;
    }

    public JMenuItem getjArchiveRecord() {
        return jArchiveRecord;
    }

    public JLabel getRecordsLabel() {
        return recordsLabel;
    }

    public LogWindow getLogwind() {
        return logwind;
    }

    public List<ModifiedData> getModifiedDataList() {
        return modifiedDataList;
    }

    public TableState getAssignments() {
        return tabs.get(ASSIGNMENTS_TABLE_NAME).getTableState();
    }

    public TableState getReports() {
        return tabs.get(REPORTS_TABLE_NAME).getTableState();
    }

    public TableState getArchiveAssign() {
        return tabs.get(ARCHIVE_TABLE_NAME).getTableState();
    }

    public ITableFilter<?> getFilterTempAssignment() {
        return tabs.get(ASSIGNMENTS_TABLE_NAME).getFilter();
    }
    
        public Map<String, Tab> getTabs() {
        return tabs;
    }

    public void setTabs(Map<String, Tab> tabs) {
        this.tabs = tabs;
    }
    
    private String getSelectedTab() {
        return jTabbedPanel1.getTitleAt(jTabbedPanel1.getSelectedIndex());
    }

    public LoadTables getLoadTables() {
        return loadTables;
    }
    
    
    
    // @formatter:off
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addPanel_control;
    private javax.swing.JTable archiveAssignTable;
    private javax.swing.JTable assignmentTable;
    private javax.swing.JButton closeDebugPanelBtn;
    private javax.swing.JMenuItem jActivateRecord;
    private javax.swing.JMenuItem jArchiveRecord;
    private javax.swing.JButton jBatchAdd;
    private javax.swing.JButton jBatchEdit;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonClearAllFilter;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemViewLog;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemViewSQL;
    private javax.swing.JButton jDebugCancel;
    private javax.swing.JButton jDebugEnter;
    private javax.swing.JMenuItem jDeleteRecord;
    private javax.swing.JComboBox jField;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JMenuItem jMenuItemViewReports;
    private javax.swing.JMenu jMenuOther;
    private javax.swing.JMenu jMenuPrint;
    private javax.swing.JMenu jMenuReport;
    private javax.swing.JMenu jMenuSelectConn;
    private javax.swing.JMenu jMenuView;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelEdit;
    private javax.swing.JPanel jPanelSQL;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton jSwitchEditMode;
    private javax.swing.JTabbedPane jTabbedPanel1;
    private javax.swing.JTextArea jTextArea;
    private javax.swing.JLabel jTimeLastUpdate;
    private javax.swing.JButton jUpload;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel recordsLabel;
    private javax.swing.JTable reportTable;
    private javax.swing.JButton search;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField textForSearch;
    // End of variables declaration//GEN-END:variables
    // @formatter:on

 
    /**
     *  CLASS 
     */
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
}
