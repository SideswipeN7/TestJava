package Frame;

import Classes.DbConnection;
import Classes.Egzamin;
import Classes.Pytanie;
import Classes.EgzaminTabelModel;
import Classes.Exam;
import Classes.Odpowiedzi;
import Classes.ImageLoader;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author super
 */
public class Admin extends javax.swing.JFrame {

    DbConnection con;
    ResultSet rs;
    private String pass;
    int id;
    int[] idp = new int[4];
    ImageIcon addedImage = null;
    ImageIcon loadedImage = null;
    int idpyt = 0;
    boolean changed = false;
    List<Egzamin> u;
    List<Pytanie> pytania;
    ImageLoader il;
    List<Odpowiedzi> odp;
    private boolean pokaz = true;
    private int idExam;

    public Admin() {
        initComponents();
        this.con = new DbConnection();
        this.setLocationRelativeTo(null);
        il = new ImageLoader();
        setAll();
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public Admin(DbConnection conection) {
        initComponents();
        this.con = conection;
        this.setLocationRelativeTo(null);
        setAll();
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void setAll() {
        this.pnEdit.setVisible(false);
        this.lbErrWE.setVisible(false);
        setcbNewSet();
        getQuestNum();
        cbNewSet.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                getQuestNum();
            }
        });

        cbUsersSet();
        setCbSudo();
        userData();
        cbUsers.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                userData();
            }
        });

        cbSetSet();
        cbQuestSet();
        cbSet.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                cbQuestSet();
            }
        });

        dataSet();
        cbQuest.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //il.clearImage(lbReadImage);
                clearEdit();
                dataSet();

            }
        });
        showExams();

        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel selectionModel = jTable1.getSelectionModel();

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

                showQuest();
                showAnswered();
                showTable2();
            }
        });

//
        if(jTable1.getModel().getRowCount()>0){
        jTable1.setRowSelectionInterval(0, 0);
        }
        cbOdp.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                showAnswered();

            }
        });

        txQuest.setLineWrap(true);
        txNewQuest.setLineWrap(true);
    }

    private void showTable2() {
        Object o4 = jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 3);
        int exam = Integer.parseInt((String) o4.toString());
        int ilosc = cbOdp.getItemCount();
        int i = 0;
        int h = 0;
        List<String> listah = new ArrayList<String>();
        do {
            Object o = cbOdp.getItemAt(h);
            Pytanie p = (Pytanie) o;
            odp = con.GetOdpowiedzi(p.idPytania, exam);
            listah.add("Pytanie " + (h + 1));
            listah.add(p.pytanie);
            listah.add("1)" + odp.get(i).odp);
            listah.add("2)" + odp.get(i + 1).odp);
            listah.add("3)" + odp.get(i + 2).odp);
            listah.add("4)" + odp.get(i + 3).odp);
            listah.add("");
            listah.add("Odpowiedzi poprawne:");
            if (odp.get(i).pop) {
                listah.add("1)" + odp.get(i).odp);
            }
            if (odp.get(i + 1).pop) {
                listah.add("2)" + odp.get(i + 1).odp);
            }
            if (odp.get(i + 2).pop) {
                listah.add("3)" + odp.get(i + 2).odp);
            }
            if (odp.get(i + 3).pop) {
                listah.add("4)" + odp.get(i + 3).odp);
            }
            listah.add("");
            listah.add("Odpowiedzi zaznaczone:");
            if (odp.get(i).userOdp) {
                listah.add("1)" + odp.get(i).odp);
            }
            if (odp.get(i + 1).userOdp) {
                listah.add("2)" + odp.get(i + 1).odp);
            }
            if (odp.get(i + 2).userOdp) {
                listah.add("3)" + odp.get(i + 2).odp);
            }
            if (odp.get(i + 3).userOdp) {
                listah.add("4)" + odp.get(i + 3).odp);
            }
            listah.add("");
            listah.add("");
            h++;
        } while (h < ilosc);
        jList1.removeAll();
        DefaultListModel<String> model = new DefaultListModel<>();

        for (String s : listah) {
            model.addElement(s);
        }

        jList1.setModel(model);

    }

    private void cbUsersSet() {
        cbUsers.removeAllItems();
        rs = con.getData("Login", "uzytkownicy");
        try {
            while (rs.next()) {
                String user = rs.getString("Login");
                cbUsers.addItem(user);
            }
        } catch (SQLException ex) {
            System.out.println("Bład Wyświetlania Użytkownika\n" + ex);
        }
    }

    private void setCbSudo() {
        this.cbSudo.removeAllItems();
        this.cbSudo.addItem("Tak");
        this.cbSudo.addItem("Nie");
    }

    private void userData() {
        boolean sudo = false;
        String x = (String) cbUsers.getSelectedItem();
        try {
            rs = con.getData("*", "uzytkownicy WHERE Login='" + x + "'");
            while (rs.next()) {
                pass = rs.getString("Haslo");
                this.lbUser.setText(x);
                this.txUser.setText(x);
                //this.txPass.setText(pass);
                sudo = rs.getBoolean("Sudo");
                this.id = rs.getInt("IdUzytkownika");
            }
            if (sudo) {
                this.lbSudo.setText("Tak");
                this.cbSudo.setSelectedItem("Tak");
            } else {
                this.lbSudo.setText("Nie");
                this.cbSudo.setSelectedItem("Nie");
            }

        } catch (Exception ex) {
            System.out.println("Błąd Wyświetlania Użytkowników\n" + ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ViewEdit = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txNewQuest = new javax.swing.JTextArea();
        txNewSet = new javax.swing.JTextField();
        txNewAns1 = new javax.swing.JTextField();
        txNewAns2 = new javax.swing.JTextField();
        txNewAns3 = new javax.swing.JTextField();
        txNewAns4 = new javax.swing.JTextField();
        chNewAns1 = new javax.swing.JCheckBox();
        chNewAns2 = new javax.swing.JCheckBox();
        chNewAns3 = new javax.swing.JCheckBox();
        chNewAns4 = new javax.swing.JCheckBox();
        btNewAns = new javax.swing.JButton();
        lbQuestNum = new javax.swing.JLabel();
        btSetAdd = new javax.swing.JButton();
        cbNewSet = new javax.swing.JComboBox<>();
        txWeight = new javax.swing.JTextField();
        lbNum = new javax.swing.JLabel();
        lbErrWE = new javax.swing.JLabel();
        lbImage = new javax.swing.JLabel();
        btAddImage = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        cbSet = new javax.swing.JComboBox<>();
        cbQuest = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txQuest = new javax.swing.JTextArea();
        txAns1 = new javax.swing.JTextField();
        txAns2 = new javax.swing.JTextField();
        txAns3 = new javax.swing.JTextField();
        txAns4 = new javax.swing.JTextField();
        chAns1 = new javax.swing.JCheckBox();
        chAns2 = new javax.swing.JCheckBox();
        chAns3 = new javax.swing.JCheckBox();
        chAns4 = new javax.swing.JCheckBox();
        btUpdate = new javax.swing.JButton();
        txValue = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lbReadImage = new javax.swing.JLabel();
        btChange = new javax.swing.JButton();
        btReset = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        cbUsers = new javax.swing.JComboBox<>();
        rdEdit = new javax.swing.JRadioButton();
        rdView1 = new javax.swing.JRadioButton();
        pnView = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbUser = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbPass = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbSudo = new javax.swing.JLabel();
        pnEdit = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabe5 = new javax.swing.JLabel();
        txUser = new javax.swing.JTextField();
        txPass = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbSudo = new javax.swing.JComboBox<>();
        btSave = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lbPytanie = new javax.swing.JLabel();
        lbOdp1 = new javax.swing.JLabel();
        rdPop1 = new javax.swing.JRadioButton();
        chOdp1 = new javax.swing.JCheckBox();
        chOdp2 = new javax.swing.JCheckBox();
        rdPop2 = new javax.swing.JRadioButton();
        lbOdp2 = new javax.swing.JLabel();
        chOdp3 = new javax.swing.JCheckBox();
        rdPop3 = new javax.swing.JRadioButton();
        lbOdp3 = new javax.swing.JLabel();
        chOdp4 = new javax.swing.JCheckBox();
        rdPop4 = new javax.swing.JRadioButton();
        lbOdp4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cbOdp = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btRefresh = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        lbZdjecie = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jmState = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1920, 1080));

        jPanel1.setMaximumSize(new java.awt.Dimension(1920, 1080));

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(1920, 1080));

        jPanel3.setMaximumSize(new java.awt.Dimension(1920, 1080));

        txNewQuest.setColumns(20);
        txNewQuest.setRows(5);
        txNewQuest.setText("Pytanie");
        txNewQuest.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txNewQuestFocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(txNewQuest);

        txNewSet.setText("Nazwa zestawu");
        txNewSet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txNewSetFocusGained(evt);
            }
        });

        txNewAns1.setText("Odpowiedź 2");
        txNewAns1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txNewAns1FocusGained(evt);
            }
        });

        txNewAns2.setText("Odpowiedź 1");
        txNewAns2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txNewAns2FocusGained(evt);
            }
        });

        txNewAns3.setText("Odpowiedź 4");
        txNewAns3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txNewAns3FocusGained(evt);
            }
        });

        txNewAns4.setText("Odpowiedź 3");
        txNewAns4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txNewAns4FocusGained(evt);
            }
        });

        chNewAns1.setText("Odpowiedź poprawna");

        chNewAns2.setText("Odpowiedź poprawna");

        chNewAns3.setText("Odpowiedź poprawna");

        chNewAns4.setText("Odpowiedź poprawna");

        btNewAns.setText("Dodaj zadanie");
        btNewAns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNewAnsActionPerformed(evt);
            }
        });

        lbQuestNum.setText("Zadanie nr ");

        btSetAdd.setText("Dodaj Zestaw");
        btSetAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSetAddActionPerformed(evt);
            }
        });

        cbNewSet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txWeight.setText("Wartość pytania");
        txWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txWeightFocusGained(evt);
            }
        });

        lbNum.setText("1");

        lbErrWE.setText("Błędna wartość punktów");

        lbImage.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.black, null));

        btAddImage.setText("Załaduj Obraz");
        btAddImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(txNewSet, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btSetAdd))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbNewSet, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txNewAns3, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txNewAns1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txNewAns4, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lbQuestNum)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbNum, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txNewAns2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chNewAns2)
                                    .addComponent(chNewAns1)
                                    .addComponent(chNewAns3)
                                    .addComponent(chNewAns4)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lbErrWE, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txWeight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(142, 142, 142)
                                .addComponent(btNewAns)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(222, 222, 222)
                                .addComponent(btAddImage)))))
                .addContainerGap(4619, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txNewSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSetAdd))
                .addGap(18, 18, 18)
                .addComponent(cbNewSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbQuestNum)
                    .addComponent(lbNum)
                    .addComponent(lbErrWE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chNewAns1)
                            .addComponent(txNewAns2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txNewAns1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chNewAns2))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txNewAns4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chNewAns4))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txNewAns3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chNewAns3))
                        .addGap(43, 43, 43)
                        .addComponent(btNewAns))
                    .addComponent(txWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btAddImage)))
                .addContainerGap(901, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tryb Dodawania Testów", jPanel3);

        jPanel2.setMaximumSize(new java.awt.Dimension(1920, 1080));

        cbSet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbQuest.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txQuest.setColumns(20);
        txQuest.setRows(5);
        jScrollPane1.setViewportView(txQuest);

        txAns1.setText("jTextField1");

        txAns2.setText("jTextField1");

        txAns3.setText("jTextField1");

        txAns4.setText("jTextField1");

        chAns1.setText("Odpowiedź poprawna");

        chAns2.setText("Odpowiedź poprawna");

        chAns3.setText("Odpowiedź poprawna");

        chAns4.setText("Odpowiedź poprawna");

        btUpdate.setText("Zapisz");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        txValue.setText("Wartość Pytania");
        txValue.setMinimumSize(new java.awt.Dimension(14, 110));

        jLabel6.setText("Wartość Pytania");

        jLabel7.setText("Zestaw");

        jLabel8.setText("Pytanie");

        lbReadImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btChange.setText("Zmień obraz");
        btChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btChangeActionPerformed(evt);
            }
        });

        btReset.setText("Przywróć oryginalny");
        btReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(btChange)
                        .addGap(29, 29, 29)
                        .addComponent(btReset))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lbReadImage, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(lbReadImage, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btChange)
                    .addComponent(btReset))
                .addGap(62, 62, 62))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txAns4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txAns3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txAns2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txAns1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSet, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(82, 82, 82)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cbQuest, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chAns1)
                            .addComponent(chAns2)
                            .addComponent(chAns4)
                            .addComponent(chAns3)
                            .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txValue, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(34, 34, 34)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(4404, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbQuest, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txValue, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txAns1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chAns1))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txAns2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chAns2))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txAns3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chAns3))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txAns4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chAns4))))
                        .addGap(46, 46, 46)
                        .addComponent(btUpdate)))
                .addContainerGap(904, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tryb Edycji testów", jPanel2);

        jPanel4.setMaximumSize(new java.awt.Dimension(1920, 1080));

        cbUsers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ViewEdit.add(rdEdit);
        rdEdit.setText("Zmiana danych");
        rdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEditActionPerformed(evt);
            }
        });

        ViewEdit.add(rdView1);
        rdView1.setSelected(true);
        rdView1.setText("Podgląd Danych");
        rdView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdView1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Nazwa Użytkownika");

        lbUser.setText("jLabel2");

        jLabel2.setText("Hasło Użytkownika");

        lbPass.setText("************************");

        jLabel3.setText("Administrator");

        lbSudo.setText("Nie");

        javax.swing.GroupLayout pnViewLayout = new javax.swing.GroupLayout(pnView);
        pnView.setLayout(pnViewLayout);
        pnViewLayout.setHorizontalGroup(
            pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnViewLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbSudo)
                    .addGroup(pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lbUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbPass, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(293, Short.MAX_VALUE))
        );
        pnViewLayout.setVerticalGroup(
            pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnViewLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lbUser))
                .addGap(18, 18, 18)
                .addGroup(pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lbPass))
                .addGap(22, 22, 22)
                .addGroup(pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lbSudo))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel4.setText("Nazwa Użytkownika");

        jLabe5.setText("Hasło Użytkownika");

        txUser.setText("jTextField1");

        txPass.setText(" ");

        jLabel5.setText("Administrator");

        cbSudo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btSave.setText("Zapisz zmiany");
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });

        btDelete.setText("Usuń");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnEditLayout = new javax.swing.GroupLayout(pnEdit);
        pnEdit.setLayout(pnEditLayout);
        pnEditLayout.setHorizontalGroup(
            pnEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnEditLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabe5, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(pnEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbSudo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnEditLayout.createSequentialGroup()
                        .addComponent(btSave)
                        .addGap(18, 18, 18)
                        .addComponent(btDelete)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txUser)
                    .addComponent(txPass))
                .addGap(226, 226, 226))
        );
        pnEditLayout.setVerticalGroup(
            pnEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnEditLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(pnEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabe5)
                    .addComponent(txPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(pnEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbSudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btSave)
                    .addComponent(btDelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setText("Użytkownik");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(cbUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(68, 68, 68)
                                        .addComponent(rdView1)
                                        .addGap(18, 18, 18)
                                        .addComponent(rdEdit))
                                    .addComponent(jLabel9)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(pnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(4992, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdEdit)
                    .addComponent(rdView1))
                .addGap(33, 33, 33)
                .addComponent(pnView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(955, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tryb Zarządzania Użytkownikami", jPanel4);

        jPanel6.setMaximumSize(new java.awt.Dimension(1920, 1080));

        jPanel8.setMaximumSize(new java.awt.Dimension(1920, 1080));
        jPanel8.setName(""); // NOI18N

        lbPytanie.setText("PYTANIE?");

        lbOdp1.setText("jLabel10");

        rdPop1.setEnabled(false);

        chOdp1.setEnabled(false);

        chOdp2.setEnabled(false);

        rdPop2.setEnabled(false);

        lbOdp2.setText("jLabel10");

        chOdp3.setEnabled(false);

        rdPop3.setEnabled(false);

        lbOdp3.setText("jLabel10");

        chOdp4.setEnabled(false);

        rdPop4.setEnabled(false);

        lbOdp4.setText("jLabel10");

        jLabel10.setText("Pytanie");

        cbOdp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel11.setText("Poprawne");

        jLabel12.setText("Zaznaczone");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbOdp, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(lbPytanie, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 26, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(lbOdp4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdPop4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(lbOdp2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdPop2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(lbOdp3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdPop3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(lbOdp1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdPop1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel11)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(chOdp1)
                                    .addComponent(chOdp2)
                                    .addComponent(chOdp3)
                                    .addComponent(chOdp4)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)))
                        .addContainerGap(440, Short.MAX_VALUE))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbOdp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(lbPytanie, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chOdp1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rdPop1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbOdp1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(lbOdp2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(lbOdp3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(rdPop2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(chOdp2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(rdPop3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(chOdp3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbOdp4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chOdp4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)))
                    .addComponent(rdPop4))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jTable1);

        btRefresh.setText("Odśwież Egzaminy");
        btRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRefreshActionPerformed(evt);
            }
        });

        jButton1.setText("Zapisz Egzamin");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList1);

        lbZdjecie.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane4))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btRefresh)
                        .addGap(49, 49, 49)
                        .addComponent(jButton1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(lbZdjecie, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(4215, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btRefresh)
                            .addComponent(jButton1))
                        .addGap(11, 11, 11)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbZdjecie, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(573, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Egzaminy", jPanel6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Tryb");

        jMenuBar1.setMaximumSize(new java.awt.Dimension(255, 1080));
        jMenuBar1.setName("Tryb administratora"); // NOI18N

        jMenu1.setText("Użytkownik");

        jMenuItem1.setText("Wyloguj");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Pomoc");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem2.setText("O programie");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        jmState.setText("Tryb Administratora");
        jmState.setEnabled(false);
        jmState.setFocusable(false);
        jMenuBar1.add(jmState);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        try {
            con.deleteData("uzytkownicy", "IdUzytkownika=" + id);
            clearUsers();
        } catch (Exception ex) {
            System.out.println("Bład Usuwania Użytkownika\n" + ex);
        }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed
        try {
            String user = this.txUser.getText();
            String newPass = this.txPass.getText();

            if (this.cbSudo.getSelectedItem() == "Tak") {
                con.updateData("Login='" + user + "', Haslo='" + newPass + "', sudo=1", "uzytkownicy", "idUzytkownika=" + id);
            } else {
                con.updateData("Login='" + user + "', Haslo='" + newPass + "', sudo=0", "uzytkownicy", "IdUzytkownika=" + id);
            }
        } catch (Exception ex) {
            System.out.println("Bład Edycji Użytkownika\n" + ex);
        }
    }//GEN-LAST:event_btSaveActionPerformed

    private void rdView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdView1ActionPerformed
        this.pnView.setVisible(true);
        this.pnEdit.setVisible(false);
    }//GEN-LAST:event_rdView1ActionPerformed

    private void rdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEditActionPerformed
        this.pnView.setVisible(false);
        this.pnEdit.setVisible(true);
    }//GEN-LAST:event_rdEditActionPerformed

    private void btResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResetActionPerformed
        loadedImage = null;
        il.clearImage(lbReadImage);
        con.getImage(idpyt, lbReadImage);
        changed = false;
    }//GEN-LAST:event_btResetActionPerformed

    private void btChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btChangeActionPerformed
        il.ladujObraz(lbReadImage);
        loadedImage = il.getImageIcon();
        changed = true;
    }//GEN-LAST:event_btChangeActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if (tryParse(txValue.getText())) {
            String zestaw = (String) cbSet.getSelectedItem();
            int pyt = Integer.parseInt((String) cbQuest.getSelectedItem());
            String pytanie = txQuest.getText();
            int wartosc = Integer.parseInt(txValue.getText());

            String odp1 = txAns1.getText();
            String odp2 = txAns2.getText();
            String odp3 = txAns3.getText();
            String odp4 = txAns4.getText();

            Boolean prawda1 = chAns1.isSelected();
            Boolean prawda2 = chAns2.isSelected();
            Boolean prawda3 = chAns3.isSelected();
            Boolean prawda4 = chAns4.isSelected();

            con.updateData("p.Pytanie='" + pytanie + "', p.Punkty=" + wartosc, "pytania p JOIN zestaw z ON p.ZestawID=z.ZestawID JOIN odpowiedzi o ON o.idPytania=p.NrPytania", "z.Nazwa='" + zestaw + "' AND p.NrPytania=" + pyt);
            con.updateData("o.Odpowiedz='" + odp1 + "', o.Prawda=" + prawda1, "pytania p JOIN zestaw z ON p.ZestawID=z.ZestawID JOIN odpowiedzi o ON o.idPytania=p.NrPytania", "z.Nazwa='" + zestaw + "' AND p.NrPytania=" + pyt + " AND o.idOdpowiedzi=" + idp[0]);
            con.updateData("o.Odpowiedz='" + odp4 + "', o.Prawda=" + prawda2, "pytania p JOIN zestaw z ON p.ZestawID=z.ZestawID JOIN odpowiedzi o ON o.idPytania=p.NrPytania", "z.Nazwa='" + zestaw + "' AND p.NrPytania=" + pyt + " AND o.idOdpowiedzi=" + idp[1]);
            con.updateData("o.Odpowiedz='" + odp3 + "', o.Prawda=" + prawda3, "pytania p JOIN zestaw z ON p.ZestawID=z.ZestawID JOIN odpowiedzi o ON o.idPytania=p.NrPytania", "z.Nazwa='" + zestaw + "' AND p.NrPytania=" + pyt + " AND o.idOdpowiedzi=" + idp[2]);
            con.updateData("o.Odpowiedz='" + odp2 + "', o.Prawda=" + prawda4, "pytania p JOIN zestaw z ON p.ZestawID=z.ZestawID JOIN odpowiedzi o ON o.idPytania=p.NrPytania", "z.Nazwa='" + zestaw + "' AND p.NrPytania=" + pyt + " AND o.idOdpowiedzi=" + idp[3]);
            if (changed) {
                try {

                    con.addImage(idpyt, il.getPath());
                } catch (SQLException ex) {
                    System.out.println("Bład modyfikacji Obrazu\n" + ex);
                }
            }
        }
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btAddImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddImageActionPerformed
        il.ladujObraz(lbImage);
        addedImage = il.getImageIcon();
    }//GEN-LAST:event_btAddImageActionPerformed

    private void txWeightFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txWeightFocusGained
        txWeight.setText("");
        lbErrWE.setVisible(false);
    }//GEN-LAST:event_txWeightFocusGained

    private void btSetAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSetAddActionPerformed
        String nazwa = txNewSet.getText();
        nazwa = "'" + nazwa + "'";
        System.out.println(nazwa);
        con.setData("Nazwa", nazwa, "zestaw");
        setcbNewSet();
    }//GEN-LAST:event_btSetAddActionPerformed

    private void btNewAnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNewAnsActionPerformed
        String pkty = this.txWeight.getText();
        if (tryParse(pkty)) {
            try {
                String pytanie = this.txNewQuest.getText();

                String odp1 = this.txNewAns2.getText();
                String odp2 = this.txNewAns1.getText();
                String odp3 = this.txNewAns4.getText();
                String odp4 = this.txNewAns3.getText();

                boolean od1 = this.chNewAns1.isSelected();
                boolean od2 = this.chNewAns2.isSelected();
                boolean od3 = this.chNewAns4.isSelected();
                boolean od4 = this.chNewAns3.isSelected();

                int idZestawu = getKatId((String) this.cbNewSet.getSelectedItem());
                int pyt = Integer.parseInt(this.lbNum.getText().trim());
                int pkt = Integer.parseInt(pkty);

                con.setData("NrPytania, Pytanie, ZestawID, Punkty", "" + pyt + ", '" + pytanie + "'," + idZestawu + "," + pkt, "pytania");
                con.setData("Odpowiedz, Prawda, idPytania", "'" + odp1 + "', " + od1 + "," + pyt, "odpowiedzi");
                con.setData("Odpowiedz, Prawda, idPytania", "'" + odp2 + "', " + od2 + "," + pyt, "odpowiedzi");
                con.setData("Odpowiedz, Prawda, idPytania", "'" + odp3 + "', " + od3 + "," + pyt, "odpowiedzi");
                con.setData("Odpowiedz, Prawda, idPytania", "'" + odp4 + "', " + od4 + "," + pyt, "odpowiedzi");
                if (addedImage != null) {
                    rs = con.getData("idPytania", "Pytania WHERE NrPytania=" + pyt + " AND ZestawID=" + idZestawu);
                    while (rs.next()) {
                        idpyt = rs.getInt("idPytania");
                    }
                    con.addImage(idpyt, il.getPath());
                }
                this.clearNew();
                cbQuestSet();
            } catch (Exception ex) {
                System.out.println("Błąd dodania zadania\n" + ex);
            }
        } else {
            this.lbErrWE.setVisible(true);
        }
    }//GEN-LAST:event_btNewAnsActionPerformed

    private void txNewAns4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txNewAns4FocusGained
        txNewAns4.setText("");
    }//GEN-LAST:event_txNewAns4FocusGained

    private void txNewAns3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txNewAns3FocusGained
        txNewAns3.setText("");
    }//GEN-LAST:event_txNewAns3FocusGained

    private void txNewAns2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txNewAns2FocusGained
        txNewAns2.setText("");
    }//GEN-LAST:event_txNewAns2FocusGained

    private void txNewAns1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txNewAns1FocusGained
        txNewAns1.setText("");
    }//GEN-LAST:event_txNewAns1FocusGained

    private void txNewSetFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txNewSetFocusGained
        txNewSet.setText("");
    }//GEN-LAST:event_txNewSetFocusGained

    private void txNewQuestFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txNewQuestFocusGained
        txNewQuest.setText("");
    }//GEN-LAST:event_txNewQuestFocusGained
    private void showExams() {
        u = con.getExams();
        EgzaminTabelModel dtm = new EgzaminTabelModel(u);
        jTable1.setModel(dtm);
    }

    private void showAnswered() {
        ClearOdp();
        Object o = cbOdp.getModel().getElementAt(cbOdp.getSelectedIndex());
        try {
            Pytanie p = (Pytanie) o;
            if (p.img != null) {
                con.getImage(p.idPytania, lbZdjecie);
            }
            odp = con.GetOdpowiedzi(p.idPytania, idExam);

            lbPytanie.setText(p.pytanie);
            lbOdp1.setText((odp.get(0).odp));
            if (odp.get(0).pop) {
                rdPop1.setSelected(true);
            }
            if (odp.get(0).userOdp) {
                chOdp1.setSelected(true);
            }
            lbOdp2.setText((odp.get(1).odp));
            if (odp.get(1).pop) {
                rdPop2.setSelected(true);
            }
            if (odp.get(1).userOdp) {
                chOdp2.setSelected(true);
            }

            lbOdp3.setText((odp.get(2).odp));
            if (odp.get(2).pop) {
                rdPop3.setSelected(true);
            }
            if (odp.get(2).userOdp) {
                chOdp3.setSelected(true);
            }

            lbOdp4.setText((odp.get(3).odp));
            if (odp.get(3).pop) {
                rdPop4.setSelected(true);
            }
            if (odp.get(3).userOdp) {
                chOdp4.setSelected(true);
            }
        } catch (Exception ex) {
            System.out.println("Bład pokazania odpowiedzi\n" + ex);
        }
    }

    private void ClearOdp() {
        lbZdjecie.setIcon(null);

        rdPop1.setSelected(false);
        chOdp1.setSelected(false);

        rdPop2.setSelected(false);
        chOdp2.setSelected(false);

        rdPop3.setSelected(false);
        chOdp3.setSelected(false);

        rdPop4.setSelected(false);
        chOdp4.setSelected(false);
    }


    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        showExams();
    }//GEN-LAST:event_btRefreshActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Logowanie l = new Logowanie();
        l.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JOptionPane.showMessageDialog(null, "Aplikacja stworzona przez: Paweł Wanot oraz Michał Stanecki", JOptionPane.ICON_PROPERTY, JOptionPane.OK_OPTION); // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            Object o1 = jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 0);
            Object o2 = jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1);
            Object o3 = jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 2);
            Object o4 = jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 3);
            int exam = Integer.parseInt((String) o4.toString());
            String fileName = (String) o1.toString() + (String) o2.toString()+exam ;
            
            //Object o = jTable1
            //String egzamin= jTable1.getModel().ge
            String userHomeFolder = System.getProperty("user.home") + "/Desktop";
            File textFile = new File(userHomeFolder, fileName + ".txt");
            PrintWriter out = new PrintWriter(new FileWriter(textFile));
            int ilosc = cbOdp.getItemCount();
            int i = 0;
            int h = 0;
            out.println("Odpowiadał:  " + (String) o1.toString());
            out.println("Dnia:  " + (String) o3.toString());
            out.println("Z:  " + (String) o2.toString());
            out.println("");
            out.println("");
            do {
                Object o = cbOdp.getItemAt(h);
                Pytanie p = (Pytanie) o;
                odp = con.GetOdpowiedzi(p.idPytania, exam);
                out.println("Pytanie " + (h + 1) + "\n");
                out.println(p.pytanie + "\n");
                out.println("1)" + odp.get(i).odp + "\n");
                out.println("2)" + odp.get(i + 1).odp + "\n");
                out.println("3)" + odp.get(i + 2).odp + "\n");
                out.println("4)" + odp.get(i + 3).odp + "\n");
                out.println("Odpowiedzi poprawne:" + "\n");
                if (odp.get(i).pop) {
                    out.println("1)" + odp.get(i).odp + "\n");
                }
                if (odp.get(i + 1).pop) {
                    out.println("2)" + odp.get(i + 1).odp + "\n");
                }
                if (odp.get(i + 2).pop) {
                    out.println("3)" + odp.get(i + 2).odp + "\n");
                }
                if (odp.get(i + 3).pop) {
                    out.println("4)" + odp.get(i + 3).odp + "\n");
                }
                out.println("Odpowiedzi zaznaczone:" + "\n");
                if (odp.get(i).userOdp) {
                    out.println("1)" + odp.get(i).odp + "\n");
                }
                if (odp.get(i + 1).userOdp) {
                    out.println("2)" + odp.get(i + 1).odp + "\n");
                }
                if (odp.get(i + 2).userOdp) {
                    out.println("3)" + odp.get(i + 2).odp + "\n");
                }
                if (odp.get(i + 3).userOdp) {
                    out.println("4)" + odp.get(i + 3).odp + "\n");
                }
                out.println("\n");

                h++;
            } while (h < ilosc);
            out.close();

        } catch (IOException ex) {
            System.out.println("Bład zapisu:"+ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void showQuest() {
        Object o = jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 3);
        idExam = (int) o;
        pytania = con.GetPytania(idExam, lbZdjecie);
        DefaultComboBoxModel dlm = new DefaultComboBoxModel();
        for (Pytanie pyt : pytania) {
            dlm.addElement(pyt);
        }
        cbOdp.setModel(dlm);

    }

    private void clearNew() throws SQLException {
        this.txNewQuest.setText("Pytanie");

        this.txNewAns1.setText("Odpowiedź 2");
        this.txNewAns2.setText("Odpowiedź 1");
        this.txNewAns3.setText("Odpowiedź 4");
        this.txNewAns4.setText("Odpowiedź 3");
        this.chNewAns1.setSelected(false);
        this.chNewAns2.setSelected(false);
        this.chNewAns3.setSelected(false);
        this.chNewAns4.setSelected(false);
        getQuestNum();
        this.txWeight.setText("Wartość pytania");
        il.clearImage(lbImage);
        addedImage = null;

    }

    private boolean tryParse(String x) {
        try {
            Integer.parseInt(x);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearUsers() {
        userData();
        this.cbUsers.setSelectedIndex(1);
    }

    public int getQuestNum() {
        String a = (this.cbNewSet.getSelectedItem().toString());
        int liczba = Integer.parseInt(con.question(a));
        if (liczba > 0) {
            getQuestNum(liczba);
            return liczba;
        }
        lbNum.setText(" 1");
        return 1;
    }

    private void getQuestNum(int id) {
        try {
            rs = con.getData("Max(NrPytania)", "pytania WHERE ZestawID= " + id);
            int x = 0;
            while (rs.next()) {
                x = rs.getInt("Max(NrPytania)");
                x++;
                lbNum.setText(" " + x);
            }
        } catch (SQLException ex) {
            System.out.println("Błąd Pobrania Numeru Pytania\n" + ex);
        }

    }

    private int getKatId(String nazwa) {
        rs = con.getData("ZestawID", "zestaw WHERE Nazwa ='" + nazwa + "'");
        try {
            while (rs.next()) {
                return rs.getInt("ZestawID");
            }
        } catch (SQLException ex) {
            System.out.println("Bład Pobrania Numeru Zestawu\n" + ex);
        }
        return 0;
    }

    private void setcbNewSet() {
        cbNewSet.removeAllItems();
        rs = con.getData("Nazwa", "zestaw");
        try {
            while (rs.next()) {
                String kat = rs.getString("Nazwa");
                cbNewSet.addItem(kat);
            }
        } catch (SQLException ex) {
            System.out.println("Bład Wyświetlania Nazwy Zestawu\n" + ex);
        }
    }

    private void cbSetSet() {
        cbSet.removeAllItems();
        rs = con.getData("Nazwa", "zestaw");
        try {
            while (rs.next()) {
                String kat = rs.getString("Nazwa");
                cbSet.addItem(kat);
            }
        } catch (SQLException ex) {
            System.out.println("Bład Wyświetlenia Nazwy Zestawu - Edycja\n" + ex);
        }
    }

    private void cbQuestSet() {

        cbQuest.removeAllItems();
        String zestaw = (String) cbSet.getSelectedItem();
        zestaw = zestaw.trim();
        List<String> pytania = con.GetPytNum(zestaw);
        for (String s : pytania) {
            cbQuest.addItem(s);
        }
    }

    private void dataSet() {
        if (cbQuest.getItemCount() > 0) {
            String zestaw = (String) cbSet.getSelectedItem();
            int pytanie = Integer.parseInt((String) cbQuest.getSelectedItem());
            String[] odp = new String[4];
            Boolean[] prawda = new Boolean[4];
            Blob imageBlob = null;

            rs = con.getData("distinct p.Pytanie, p.Punkty, p.idPytania, p.Zdjecie", "pytania p JOIN zestaw z ON p.ZestawID=z.ZestawID JOIN odpowiedzi o ON o.idPytania=p.NrPytania WHERE z.Nazwa='" + zestaw + "' AND p.NrPytania=" + pytanie);
            try {
                while (rs.next()) {
                    String pyt = rs.getString("p.Pytanie");
                    String value = rs.getString("p.Punkty");
                    imageBlob = rs.getBlob("p.Zdjecie");
                    idpyt = rs.getInt("p.idPytania");
                    txQuest.setText(pyt);
                    txValue.setText(value);
                    if (imageBlob != null) {
                        con.getImage(idpyt, lbReadImage);
                    }
                }

                rs = con.getData("o.Odpowiedz, o.Prawda, o.idOdpowiedzi", "pytania p JOIN zestaw z ON p.ZestawID=z.ZestawID JOIN odpowiedzi o ON o.idPytania=p.NrPytania WHERE z.Nazwa='" + zestaw + "' AND p.NrPytania=" + pytanie);
                try {
                    int i = 0;
                    while (rs.next()) {

                        odp[i] = rs.getString("o.Odpowiedz");
                        prawda[i] = rs.getBoolean("o.Prawda");
                        idp[i] = rs.getInt("o.idOdpowiedzi");
                        i++;
                    }
                    txAns1.setText(odp[0]);
                    txAns2.setText(odp[1]);
                    txAns3.setText(odp[2]);
                    txAns4.setText(odp[3]);

                    chAns1.setSelected(prawda[0]);
                    chAns2.setSelected(prawda[1]);
                    chAns3.setSelected(prawda[2]);
                    chAns4.setSelected(prawda[3]);

                } catch (SQLException ex) {
                    System.out.println("Bład Aktualizacji Odpowiedzi\n" + ex);
                }
            } catch (SQLException ex) {
                System.out.println("Bład Aktualizacji Pytania\n" + ex);
            }
        } else {
            clearEdit();
        }
    }

    private void clearEdit() {
        txQuest.setText("");
        txAns1.setText("");
        txAns2.setText("");
        txAns3.setText("");
        txAns4.setText("");
        chAns1.setSelected(false);
        chAns2.setSelected(false);
        chAns3.setSelected(false);
        chAns4.setSelected(false);
        txValue.setText("");
        loadedImage = null;
        lbReadImage.setIcon(loadedImage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup ViewEdit;
    private javax.swing.JButton btAddImage;
    private javax.swing.JButton btChange;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btNewAns;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btReset;
    private javax.swing.JButton btSave;
    private javax.swing.JButton btSetAdd;
    private javax.swing.JButton btUpdate;
    private javax.swing.JComboBox<String> cbNewSet;
    private javax.swing.JComboBox<String> cbOdp;
    private javax.swing.JComboBox<String> cbQuest;
    private javax.swing.JComboBox<String> cbSet;
    private javax.swing.JComboBox<String> cbSudo;
    private javax.swing.JComboBox<String> cbUsers;
    private javax.swing.JCheckBox chAns1;
    private javax.swing.JCheckBox chAns2;
    private javax.swing.JCheckBox chAns3;
    private javax.swing.JCheckBox chAns4;
    private javax.swing.JCheckBox chNewAns1;
    private javax.swing.JCheckBox chNewAns2;
    private javax.swing.JCheckBox chNewAns3;
    private javax.swing.JCheckBox chNewAns4;
    private javax.swing.JCheckBox chOdp1;
    private javax.swing.JCheckBox chOdp2;
    private javax.swing.JCheckBox chOdp3;
    private javax.swing.JCheckBox chOdp4;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabe5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JMenu jmState;
    private javax.swing.JLabel lbErrWE;
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbNum;
    private javax.swing.JLabel lbOdp1;
    private javax.swing.JLabel lbOdp2;
    private javax.swing.JLabel lbOdp3;
    private javax.swing.JLabel lbOdp4;
    private javax.swing.JLabel lbPass;
    private javax.swing.JLabel lbPytanie;
    private javax.swing.JLabel lbQuestNum;
    private javax.swing.JLabel lbReadImage;
    private javax.swing.JLabel lbSudo;
    private javax.swing.JLabel lbUser;
    private javax.swing.JLabel lbZdjecie;
    private javax.swing.JPanel pnEdit;
    private javax.swing.JPanel pnView;
    private javax.swing.JRadioButton rdEdit;
    private javax.swing.JRadioButton rdPop1;
    private javax.swing.JRadioButton rdPop2;
    private javax.swing.JRadioButton rdPop3;
    private javax.swing.JRadioButton rdPop4;
    private javax.swing.JRadioButton rdView1;
    private javax.swing.JTextField txAns1;
    private javax.swing.JTextField txAns2;
    private javax.swing.JTextField txAns3;
    private javax.swing.JTextField txAns4;
    private javax.swing.JTextField txNewAns1;
    private javax.swing.JTextField txNewAns2;
    private javax.swing.JTextField txNewAns3;
    private javax.swing.JTextField txNewAns4;
    private javax.swing.JTextArea txNewQuest;
    private javax.swing.JTextField txNewSet;
    private javax.swing.JTextField txPass;
    private javax.swing.JTextArea txQuest;
    private javax.swing.JTextField txUser;
    private javax.swing.JTextField txValue;
    private javax.swing.JTextField txWeight;
    // End of variables declaration//GEN-END:variables

}
