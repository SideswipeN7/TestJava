package Frame;

import Classes.DbConnection;
import Classes.Odpowiedz;
import Classes.PytanieOdebrane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author super
 */
public class User extends javax.swing.JFrame {

    DbConnection con;
    int pytanie = 1;    // zlicza ilość wystąpień pytania  
    private ResultSet rs_zestaw;
    public int[] old_questions = new int[100];
    int q = 0;
    int punkty_za_pytanie = 0;
    int mozliwe_punkty = 0;
    double wynik = 0;
    Odpowiedz[] odpowiedzi;
    int idEgzaminu;
    int idUser = 2;
    int pozostało;
    int id_pytania;
    int max_pytan = 0;
    boolean wykonaj;
    int min = 2;

    private void setPanels() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        int x = (int) (width / 2);
        int y = (int) (height / 2);
//        PanelStartowy.setLayout(null);
//        PanelStartowy.setLocation(x, y);
//        PanelStartowy.setSize(500, 120);
//PanelStartowy.setLayout(new BoxLayout(PanelStartowy, BoxLayout.Y_AXIS));
        PanelStartowy.setAlignmentY(JFrame.CENTER_ALIGNMENT);
        PanelStartowy.setAlignmentX(JFrame.CENTER_ALIGNMENT);
        //PanelStartowy.setVisible(true);

    }

    public User() {
        initComponents();
        con = new DbConnection();
        this.PanelEgzaminu.setVisible(false);
        this.PanelPytania.setVisible(false);
        this.PanelStartowy.setVisible(true);
        this.btn_Zakoncz.setVisible(false);
        this.PanelKoncowy.setVisible(false);
        lbBlad.setVisible(false);
        setcbNewSet();
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        cb_Kategoria.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setTxLpytan();
            }
        });
        cb_Kategoria.setSelectedIndex(1);
        setPanels();
    }

    public User(DbConnection conection, int id) {
        initComponents();
        this.con = conection;
        this.idUser = id;
        this.PanelEgzaminu.setVisible(false);
        this.PanelPytania.setVisible(false);
        this.PanelStartowy.setVisible(true);
        this.btn_Zakoncz.setVisible(false);
        this.PanelKoncowy.setVisible(false);
        lbBlad.setVisible(false);
        this.setLocationRelativeTo(null);
        setcbNewSet();
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        cb_Kategoria.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setTxLpytan();
            }
        });
        cb_Kategoria.setSelectedIndex(0);
        setTxLpytan();
    }

    private void setTxLpytan() {
        String kategoria = cb_Kategoria.getSelectedItem().toString();
        max_pytan = con.getMaxPytan(kategoria);
        txf_Lpytan.setText(max_pytan + "");
        if (max_pytan == 0) {
            wykonaj = false;
        }
        if (max_pytan > 0) {
            wykonaj = true;
        }
    }

    private void resetPoints() {
        punkty_za_pytanie = 0;
        mozliwe_punkty = 0;
        wynik = 0;

    }

    private void setcbNewSet() {
        cb_Kategoria.removeAllItems();
        rs_zestaw = con.getData("Nazwa", "zestaw");
        try {
            while (rs_zestaw.next()) {

                String kat = rs_zestaw.getString("Nazwa");
                cb_Kategoria.addItem(kat);

            }
        } catch (SQLException ex) {
            System.out.println("Bład pobrania zestawów]\n" + ex);
        }
    }

    private void CheckAnswers() {
        int wartosc_odpowiedzi = 0;
        int ilosc_poprawnych_odpowiedzi = 0;

        if (this.cb_1.isSelected() && odpowiedzi[0].Prawda == true) {
            wartosc_odpowiedzi++;
            con.setOdpowiedz(odpowiedzi[0].idOdpowiedzi, idEgzaminu, id_pytania);
        }
        if (this.cb_2.isSelected() && odpowiedzi[1].Prawda == true) {
            wartosc_odpowiedzi++;
            con.setOdpowiedz(odpowiedzi[1].idOdpowiedzi, idEgzaminu, id_pytania);
        }
        if (this.cb_3.isSelected() && odpowiedzi[2].Prawda == true) {
            wartosc_odpowiedzi++;
            con.setOdpowiedz(odpowiedzi[2].idOdpowiedzi, idEgzaminu, id_pytania);
        }
        if (this.cb_4.isSelected() && odpowiedzi[3].Prawda == true) {
            wartosc_odpowiedzi++;
            con.setOdpowiedz(odpowiedzi[3].idOdpowiedzi, idEgzaminu, id_pytania);
        }
        if (this.cb_1.isSelected() && odpowiedzi[0].Prawda == false) {
            wartosc_odpowiedzi = 0;
            con.setOdpowiedz(odpowiedzi[0].idOdpowiedzi, idEgzaminu, id_pytania);
        }
        if (this.cb_2.isSelected() && odpowiedzi[1].Prawda == false) {
            wartosc_odpowiedzi = 0;
            con.setOdpowiedz(odpowiedzi[1].idOdpowiedzi, idEgzaminu, id_pytania);
        }
        if (this.cb_3.isSelected() && odpowiedzi[2].Prawda == false) {
            wartosc_odpowiedzi = 0;
            con.setOdpowiedz(odpowiedzi[2].idOdpowiedzi, idEgzaminu, id_pytania);
        }
        if (this.cb_4.isSelected() && odpowiedzi[3].Prawda == false) {
            wartosc_odpowiedzi = 0;
            con.setOdpowiedz(odpowiedzi[3].idOdpowiedzi, idEgzaminu, id_pytania);
        }

        for (int i = 0; i < 4; i++) {
            if (odpowiedzi[i].Prawda == true) {
                ilosc_poprawnych_odpowiedzi++;
            }
        }
        if (wartosc_odpowiedzi > 0) {
            wynik += (((double) wartosc_odpowiedzi / (double) ilosc_poprawnych_odpowiedzi) * (double) punkty_za_pytanie);
        }
    }

    private void SetNewQuestion_Answers() {
        lbl_Pytanie.setText(null);
        String kategoria = cb_Kategoria.getSelectedItem().toString();
        odpowiedzi = new Odpowiedz[4];
        PytanieOdebrane p = con.getRandomQuestion(kategoria);
        id_pytania = p.id;
        if (p.obraz != null) {
            con.getImage(id_pytania, this.lbl_Zdjecie);
        }
        punkty_za_pytanie = p.punkty;
        lbl_Pytanie.setText(p.pytanie);
        mozliwe_punkty += punkty_za_pytanie;
        odpowiedzi = con.getAnswers(id_pytania);
        this.cb_1.setText(odpowiedzi[0].Odpowiedz);
        this.cb_2.setText(odpowiedzi[1].Odpowiedz);
        this.cb_3.setText(odpowiedzi[2].Odpowiedz);
        this.cb_4.setText(odpowiedzi[3].Odpowiedz);
        this.lbl_wynik.setText("" + wynik);

    }

    /**
     * Creates new form User
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PanelPytania = new javax.swing.JPanel();
        cb_1 = new javax.swing.JCheckBox();
        cb_2 = new javax.swing.JCheckBox();
        cb_3 = new javax.swing.JCheckBox();
        cb_4 = new javax.swing.JCheckBox();
        lbl_Pytanie = new java.awt.Label();
        lbl_Zdjecie = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btn_Zakoncz = new javax.swing.JButton();
        lbBlad = new javax.swing.JLabel();
        PanelKoncowy = new javax.swing.JPanel();
        lbl_WynikEgzaminu = new javax.swing.JLabel();
        PanelStartowy = new javax.swing.JPanel();
        label1 = new java.awt.Label();
        cb_Kategoria = new javax.swing.JComboBox<>();
        label2 = new java.awt.Label();
        txf_Lpytan = new java.awt.TextField();
        btn_RozpocznijEgzamin = new javax.swing.JButton();
        PanelEgzaminu = new javax.swing.JPanel();
        lbl_Pozostalo = new javax.swing.JLabel();
        lbl_Punkty = new java.awt.Label();
        lbl_wynik = new javax.swing.JLabel();
        lbl_Pozostalo_txt = new java.awt.Label();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jmState = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(760, 1024));

        cb_1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        cb_1.setText("Odpowiedz1");
        cb_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cb_1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        cb_2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        cb_2.setText("Odpowiedz2");
        cb_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cb_2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        cb_3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        cb_3.setText("Odpowiedz3");
        cb_3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cb_3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        cb_4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        cb_4.setText("Odpowiedz4");
        cb_4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cb_4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lbl_Pytanie.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_Pytanie.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lbl_Pytanie.setText("W tym obszarze wyświetlone zostanie pytanie.");

        jButton1.setText("Dalej");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btn_Zakoncz.setText("Zakończ egzamin");
        btn_Zakoncz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ZakonczActionPerformed(evt);
            }
        });

        lbBlad.setText("jLabel1");

        lbl_WynikEgzaminu.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lbl_WynikEgzaminu.setText("Wynik");

        javax.swing.GroupLayout PanelKoncowyLayout = new javax.swing.GroupLayout(PanelKoncowy);
        PanelKoncowy.setLayout(PanelKoncowyLayout);
        PanelKoncowyLayout.setHorizontalGroup(
            PanelKoncowyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelKoncowyLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(lbl_WynikEgzaminu, javax.swing.GroupLayout.PREFERRED_SIZE, 851, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        PanelKoncowyLayout.setVerticalGroup(
            PanelKoncowyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelKoncowyLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(lbl_WynikEgzaminu, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelPytaniaLayout = new javax.swing.GroupLayout(PanelPytania);
        PanelPytania.setLayout(PanelPytaniaLayout);
        PanelPytaniaLayout.setHorizontalGroup(
            PanelPytaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPytaniaLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lbl_Zdjecie, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PanelPytaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPytaniaLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btn_Zakoncz))
                    .addComponent(lbl_Pytanie, javax.swing.GroupLayout.PREFERRED_SIZE, 1679, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelPytaniaLayout.createSequentialGroup()
                        .addGroup(PanelPytaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cb_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cb_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cb_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cb_2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanelKoncowy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PanelPytaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanelPytaniaLayout.createSequentialGroup()
                    .addGap(820, 820, 820)
                    .addComponent(lbBlad, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(820, Short.MAX_VALUE)))
        );
        PanelPytaniaLayout.setVerticalGroup(
            PanelPytaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPytaniaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelPytaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Zdjecie, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelPytaniaLayout.createSequentialGroup()
                        .addComponent(lbl_Pytanie, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addGroup(PanelPytaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PanelPytaniaLayout.createSequentialGroup()
                                .addComponent(cb_1)
                                .addGap(50, 50, 50)
                                .addComponent(cb_2)
                                .addGap(50, 50, 50)
                                .addComponent(cb_3)
                                .addGap(50, 50, 50)
                                .addComponent(cb_4))
                            .addComponent(PanelKoncowy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(60, 60, 60)
                .addGroup(PanelPytaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btn_Zakoncz))
                .addContainerGap(170, Short.MAX_VALUE))
            .addGroup(PanelPytaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanelPytaniaLayout.createSequentialGroup()
                    .addGap(340, 340, 340)
                    .addComponent(lbBlad)
                    .addContainerGap(341, Short.MAX_VALUE)))
        );

        label1.setText("Wybierz kategorię pytania");

        cb_Kategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        label2.setText("Liczba pytań:");

        txf_Lpytan.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txf_Lpytan.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txf_Lpytan.setText("10");

        btn_RozpocznijEgzamin.setText("Rozpocznij Egzamin");
        btn_RozpocznijEgzamin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RozpocznijEgzaminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelStartowyLayout = new javax.swing.GroupLayout(PanelStartowy);
        PanelStartowy.setLayout(PanelStartowyLayout);
        PanelStartowyLayout.setHorizontalGroup(
            PanelStartowyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelStartowyLayout.createSequentialGroup()
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_Kategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txf_Lpytan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_RozpocznijEgzamin)
                .addGap(0, 11, Short.MAX_VALUE))
        );
        PanelStartowyLayout.setVerticalGroup(
            PanelStartowyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelStartowyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelStartowyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelStartowyLayout.createSequentialGroup()
                        .addComponent(cb_Kategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelStartowyLayout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(64, 64, 64))
                    .addGroup(PanelStartowyLayout.createSequentialGroup()
                        .addGroup(PanelStartowyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_RozpocznijEgzamin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txf_Lpytan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        lbl_Pozostalo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lbl_Pozostalo.setText("10");

        lbl_Punkty.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_Punkty.setText("Zdobyte punkty:");

        lbl_wynik.setText("0");

        lbl_Pozostalo_txt.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_Pozostalo_txt.setText("Pozostało pytań:");

        javax.swing.GroupLayout PanelEgzaminuLayout = new javax.swing.GroupLayout(PanelEgzaminu);
        PanelEgzaminu.setLayout(PanelEgzaminuLayout);
        PanelEgzaminuLayout.setHorizontalGroup(
            PanelEgzaminuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelEgzaminuLayout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addComponent(lbl_Pozostalo_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Pozostalo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Punkty, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_wynik, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        PanelEgzaminuLayout.setVerticalGroup(
            PanelEgzaminuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelEgzaminuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelEgzaminuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_wynik, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_Punkty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_Pozostalo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_Pozostalo_txt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                .addGap(52, 52, 52))
        );

        lbl_Punkty.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(PanelEgzaminu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PanelPytania, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(PanelStartowy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(PanelStartowy, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(PanelEgzaminu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelPytania, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(322, Short.MAX_VALUE))
        );

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

        jmState.setText("Tryb użytkownika");
        jmState.setEnabled(false);
        jmState.setFocusable(false);
        jMenuBar1.add(jmState);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, 2062, 2062, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean tryParseInt(String x) {
        try {
            int a = Integer.parseInt(x);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    private void btn_RozpocznijEgzaminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RozpocznijEgzaminActionPerformed
        lbBlad.setVisible(false);
        //  setTxLpytan();
        test();
        if (wykonaj && tryParseInt(txf_Lpytan.getText())) {
            int idz = 0;
            resetPoints();
            this.pozostało = Integer.parseInt(this.txf_Lpytan.getText()) - 1;
            this.PanelPytania.setVisible(true);
            this.PanelEgzaminu.setVisible(true);
            this.PanelStartowy.setVisible(false);
            try {
                rs_zestaw = con.getData("Nazwa, ZestawID", "zestaw");
                while (rs_zestaw.next()) {
                    if (rs_zestaw.getString("Nazwa").equals(this.cb_Kategoria.getSelectedItem().toString())) {
                        idz = rs_zestaw.getInt("ZestawID");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Bład rozpoczecia testu\n" + ex);
            }
            this.idEgzaminu = con.StartExam(idUser, idz);
            SetNewQuestion_Answers();
            pytanie++;
            this.lbl_Pozostalo.setText(pozostało + "");
        } else {
            if (!tryParseInt(txf_Lpytan.getText())) {
                lbBlad.setText("Zły rozmiar liczby");
                lbBlad.setVisible(true);
            }

        }
    }//GEN-LAST:event_btn_RozpocznijEgzaminActionPerformed

    private void btn_ZakonczActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ZakonczActionPerformed
        this.PanelEgzaminu.setVisible(false);
        this.lbl_Pytanie.setVisible(false);
        this.cb_1.setVisible(false);
        this.cb_2.setVisible(false);
        this.cb_3.setVisible(false);
        this.cb_4.setVisible(false);
        this.lbl_Zdjecie.setVisible(false);
        this.btn_Zakoncz.setVisible(false);
        this.PanelKoncowy.setVisible(true);
        CheckAnswers();
        double procent = ((double) this.wynik / (double) this.mozliwe_punkty) * 100;
        String p = String.format("%1$.2f ", procent);
        this.lbl_WynikEgzaminu.setText("Wynik egzaminu to: " + p + "%" + " Zdobyłeś(aś) " + this.wynik + " na " + this.mozliwe_punkty + " możliwych punktów");
    }//GEN-LAST:event_btn_ZakonczActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        txf_Lpytan.hide();
        CheckAnswers();
        this.lbl_Zdjecie.setIcon(null);
        this.cb_1.setSelected(false);
        this.cb_2.setSelected(false);
        this.cb_3.setSelected(false);
        this.cb_4.setSelected(false);
        int l = Integer.parseInt(txf_Lpytan.getText());
        this.pozostało--;
        this.lbl_Pozostalo.setText(pozostało + "");
        if (pytanie <= l) {
            SetNewQuestion_Answers();
            pytanie++;
            if (pytanie == l + 1) {
                jButton1.setVisible(false);
                this.btn_Zakoncz.setVisible(true);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Logowanie l = new Logowanie();
        l.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JOptionPane.showMessageDialog(null, "Aplikacja stworzona przez: Paweł Wanot oraz Michał Stanecki", JOptionPane.ICON_PROPERTY, JOptionPane.OK_OPTION); // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void test() {

        if (tryParseInt(txf_Lpytan.getText())) {
            int ile = Integer.parseInt(txf_Lpytan.getText());
            if (ile > max_pytan) {
                lbBlad.setText("Zestaw nie zawiera tylu pytań");
                lbBlad.setVisible(true);
                wykonaj = false;
            }
            if (ile < min) {
                lbBlad.setText("Podano zbyt małą liczbę pytań by rozpocząć egzamin");
                lbBlad.setVisible(true);
                wykonaj = false;
            }
        }

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
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new User().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelEgzaminu;
    private javax.swing.JPanel PanelKoncowy;
    private javax.swing.JPanel PanelPytania;
    private javax.swing.JPanel PanelStartowy;
    private javax.swing.JButton btn_RozpocznijEgzamin;
    private javax.swing.JButton btn_Zakoncz;
    private javax.swing.JCheckBox cb_1;
    private javax.swing.JCheckBox cb_2;
    private javax.swing.JCheckBox cb_3;
    private javax.swing.JCheckBox cb_4;
    private javax.swing.JComboBox<String> cb_Kategoria;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenu jmState;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private javax.swing.JLabel lbBlad;
    private javax.swing.JLabel lbl_Pozostalo;
    private java.awt.Label lbl_Pozostalo_txt;
    private java.awt.Label lbl_Punkty;
    private java.awt.Label lbl_Pytanie;
    private javax.swing.JLabel lbl_WynikEgzaminu;
    private javax.swing.JLabel lbl_Zdjecie;
    private javax.swing.JLabel lbl_wynik;
    private java.awt.TextField txf_Lpytan;
    // End of variables declaration//GEN-END:variables

}
