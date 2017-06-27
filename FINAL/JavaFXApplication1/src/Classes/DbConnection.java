/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author super
 */
public class DbConnection {

    private Connection con;
    private java.sql.Statement st;
    private ResultSet rs;
    private ResultSet rsUser;
    private ResultSet rsOdp;
    public String status;
    public int[] old_questions = new int[100];
    int q = 0;

    public DbConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://192.168.1.4:3306/quizz?autoReconnect=true&useSSL=false";
            String user = "quizz";
            String pass = "P@ssw0rd";
            
//            String url = "jdbc:mysql://mysql3.gear.host:3306/quizz?autoReconnect=true&useSSL=false";
//            String user = "quizz";
//            String pass = "Bh2KnM3HM!?f";
            
            con = DriverManager.getConnection(url, user, pass);
            status = "Połączono";
            st = con.createStatement();
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
            status = "Brak Połączenia";
        }
    }

    public List<Egzamin> getExams() {
        List<Egzamin> lb = new ArrayList<Egzamin>();
        rs = getData("e.Data, e.idEgzaminu, z.ZestawID, z.Nazwa, u.idUzytkownika, u.Login", "egzamin e join zestaw z on z.ZestawID=e.idZestawu join uzytkownicy u on e.idUzytkownika=u.idUzytkownika");
        try {
            while (rs.next()) {
                lb.add(new Egzamin(rs.getString("u.Login"), rs.getInt("u.idUzytkownika"), rs.getString("z.Nazwa"), rs.getInt("z.ZestawID"), rs.getString("e.Data"), rs.getInt("e.idEgzaminu")));

            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lb;
    }

    public List<Uzytkownik> tryLogin() {
        List<Uzytkownik> u = new ArrayList<Uzytkownik>();
        try {
            String query = "SELECT * FROM uzytkownicy";
            rs = st.executeQuery(query);
            while (rs.next()) {
                u.add(new Uzytkownik(rs.getString("Login"), rs.getString("Haslo"), rs.getInt("idUzytkownika")));
            }

        } catch (Exception ex) {
            System.out.println("Błąd Logowania - Connection\n" + ex);

        }
        return u;
    }

    public boolean trySudo(String x, String y) {
        try {
            String query = "SELECT * FROM uzytkownicy";
            rs = st.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("Login");
                String age = rs.getString("Haslo");
                boolean sudo = rs.getBoolean("Sudo");
                if (x.equals(name) && y.equals(age) && sudo == true) {
                    return true;
                }
            }
        } catch (Exception ex) {
            System.out.println("Błąd Sudo - Connection\n" + ex);
        }
        return false;
    }

    public ResultSet getData(String szukane, String tablica) {
        try {
            String query = "SELECT " + szukane + " FROM " + tablica;
            rs = st.executeQuery(query);
            return rs;
        } catch (Exception ex) {
            System.out.println("Błąd Składni SQL - SELECT\n" + ex);
        }
        return null;
    }

    public String question(String x) {

        try {
            String querry = "SELECT ZestawID FROM zestaw WHERE Nazwa= '" + x + " ' ";
            rs = st.executeQuery(querry);
            String u = null;
            while (rs.next()) {
                u = rs.getString("ZestawID");
            }
            return u;
        } catch (SQLException ex) {
            System.out.println("Błąd Składni SQL - SELECT ZestawID\n" + ex);
        }
        return "0";
    }

    public void setData(String kolmny, String wartosci, String tablica) {
        try {
            String query = "INSERT INTO " + tablica + " (" + kolmny + ") VALUES (" + wartosci + ")";
            System.out.println(query);
            st.executeUpdate(query);
        } catch (Exception ex) {
            System.out.println("Błąd Składni SQL - INSERT INTO\n" + ex);
        }
    }

    public void updateData(String wartosci, String tablica, String gdzie) {
        try {
            String query = "Update " + tablica + " SET  " + wartosci + "  WHERE " + gdzie;
            System.out.println(query);
            st.executeUpdate(query);
        } catch (Exception ex) {
            System.out.println("Błąd Składni SQL - UPDATE\n" + ex);
        }
    }

    public void deleteData(String tablica, String gdzie) {
        try {
            String query = "DELETE FROM " + tablica + " WHERE " + gdzie;
            System.out.println(query);
            st.executeUpdate(query);
        } catch (Exception ex) {
            System.out.println("Błąd Składni SQL - DELETE\n" + ex);
        }
    }

    public PytanieOdebrane getRandomQuestion(String kategoria) {

        Random losowa = new Random();
        int nr_pytania;
        int liczba_pytan = 0;
        try {
            String pytania = "SELECT COUNT(*) AS total FROM PYTANIA "
                    + "JOIN ZESTAW ON ZESTAW.ZestawID = PYTANIA.ZestawID "
                    + "WHERE ZESTAW.Nazwa='" + kategoria + "'"; // zlicza liczbę pytań w konkretnej kategorii
            rs = st.executeQuery(pytania);

            try {
                while (rs.next()) {
                    liczba_pytan = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            nr_pytania = losowa.nextInt(liczba_pytan)+1;    // losuje nr pytania
           

            for (int i = 0; i <= q; i++) {   // dla każdego starego pytania sprawdź czy jego numer się nie powtórzył
                if (nr_pytania == old_questions[i]) // jeśli tak
                {
                    return getRandomQuestion(kategoria); // jeszcze raz wywołaj mi tą funkcję (w tym losowanie nowego numeru)

                }
            }
            String query = "SELECT * FROM pytania "
                    + // 
                    "WHERE PYTANIA.NrPytania=" + nr_pytania;
            PytanieOdebrane po = null;
            rs = st.executeQuery(query);            
            while(rs.next()){
                po= new PytanieOdebrane(rs.getInt("idPytania"), rs.getString("Pytanie"), rs.getBlob("Zdjecie"),rs.getInt("Punkty"));
            }
            old_questions[q] = nr_pytania;
            q++;
            return po;
        } catch (Exception ex) {
            System.out.println("Błąd: " + ex);
        }
        return null;
    }

    public void addImage(int id, String path) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE pytania SET Zdjecie = ? WHERE idPytania= ?");
            File blob = new File(path);
            InputStream is = new FileInputStream(blob);

            ps.setBinaryStream(1, is, (int) blob.length());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Bład Dodawania obrazu\n" + ex);
        }
    }

    public void getImage(int idPytania, JLabel label) {
        try {
            Statement ste = con.createStatement();
            rs = ste.executeQuery("SELECT * FROM pytania WHERE idPytania=" + idPytania);
            if (rs.next()) {
                byte[] img = rs.getBytes("Zdjecie");
                ImageIcon image = new ImageIcon(img);
                Image im = image.getImage();
                Image myImg = im.getScaledInstance(label.getWidth(), label.getHeight(), java.awt.Image.SCALE_SMOOTH);
                ImageIcon newImage = new ImageIcon(myImg);
                label.setIcon(newImage);
            }
        } catch (Exception ex) {
            System.out.println("Błąd Pobrania obrazu\n" + ex);
        }
    }

    public List<Pytanie> GetPytania(int i, JLabel label) {
        List<Pytanie> lp = new ArrayList<Pytanie>();
        try {
            rs = getData(" Distinct p.idPytania, p.Pytanie, p.zdjecie", "odpowiedziuzytkownika o join pytania p on o.idPytania=p.idPytania WHERE o.idEgzaminu=" + i);
            while (rs.next()) {
                if (rs.getBlob("p.zdjecie") != null) {
                    byte[] img = rs.getBytes("p.zdjecie");
                    ImageIcon image = new ImageIcon(img);
                    Image im = image.getImage();
                    Image myImg = im.getScaledInstance(label.getWidth(), label.getHeight(), java.awt.Image.SCALE_SMOOTH);
                    ImageIcon newImage = new ImageIcon(myImg);
                    lp.add(new Pytanie(rs.getInt("p.idPytania"), newImage, rs.getString("p.Pytanie")));
                } else {
                    lp.add(new Pytanie(rs.getInt("p.idPytania"), rs.getString("p.Pytanie")));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Bład pobierz pytania\n"+ex);
        }
        return lp;
    }

    public List<Odpowiedzi> GetOdpowiedzi(int idPytania, int idEgzaminu) {
        List<Odpowiedzi> odp = new ArrayList<Odpowiedzi>();
        int blad = 263;
        try {
            rsOdp = getData("i.idOdpowiedzi, i.Odpowiedz, i.Odpowiedz, i.Prawda, i.idPytania", "odpowiedzi i WHERE i.idPytania=" + idPytania);
            while (rsOdp.next()) {
                blad++;
                odp.add(new Odpowiedzi(rsOdp.getInt("i.idPytania"), rsOdp.getInt("i.idOdpowiedzi"), rsOdp.getString("i.Odpowiedz"), rsOdp.getBoolean("Prawda")));
            }
            rsUser = getData("i.idOdpowiedzi", "odpowiedziuzytkownika i WHERE i.idPytania=" + idPytania + " AND i.idEgzaminu=" + idEgzaminu);
            while (rsUser.next()) {
                int x = rsUser.getInt("i.idOdpowiedzi");
                for (Odpowiedzi o : odp) {
                    if (o.idOdp == x) {
                        o.setUserOdp(true);
                    }
                }

            }
        } catch (SQLException ex) {
            System.out.println("Błąd " + ex);
        }
        return odp;
    }

    public int StartExam(int idUzytkownika, int idZestawu) {
        int ide = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        System.out.println(cal.getTime());
// Output "Wed Sep 26 14:23:28 EST 2012"

        String formatted = format1.format(cal.getTime());
        System.out.println(formatted);
// Output "2012-09-26"
        try (
                PreparedStatement statement = con.prepareStatement("INSERT INTO Egzamin (idUzytkownika, idZestawu, Data) VALUES(" + idUzytkownika + "," + idZestawu + ",'" + formatted + "');",
                        Statement.RETURN_GENERATED_KEYS);) {

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ide = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Coś się... popsuło");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Bład Start Egzaminu\n"+ex);
        }
        return ide;
    }

    public int[] getTab() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int[] tab = new int[4];
        for (int i = 0; i < 4; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        for (int i = 0; i < 4; i++) {
            tab[i] = (list.get(i));
        }
        return tab;
    }

    public List<String> GetPytNum(String zestaw) {
        List<String> g = new ArrayList<String>();
        rs = getData("NrPytania", "pytania p JOIN zestaw z ON p.ZestawID=z.ZestawID WHERE z.Nazwa='" + zestaw + "'");
        try {
            while (rs.next()) {
                g.add(rs.getString("NrPytania"));

            }

        } catch (SQLException ex) {
            System.out.println("Bład numeru pytania\n"+ex);
        }
        return g;
    }

    public void setOdpowiedz(int id_Odpowiedzi, int id_Egzaminu, int idPytania) {
        setData("idOdpowiedzi, idEgzaminu, idPytania", id_Odpowiedzi + "," + id_Egzaminu + "," + idPytania, "odpowiedziuzytkownika");
    }

    public Odpowiedz[] getAnswers(int idPytania) {
        Odpowiedz[] answer = new Odpowiedz[4];
        Odpowiedz[] randomanswers = new Odpowiedz[4];
        int[] randoms = new int[4];
        int x = 0;
        randoms = getTab();
        try {
            String query = "SELECT * FROM Odpowiedzi WHERE Odpowiedzi.idPytania = " + idPytania;
            rs = st.executeQuery(query);
            while (rs.next()) {
                answer[x] = new Odpowiedz(rs.getString("Odpowiedz"), rs.getInt("idOdpowiedzi"), rs.getBoolean("Prawda"));
                x++;
            }
        } catch (SQLException ex) {
            System.out.println("Bład Odpowiedzi\n"+ex);
        }
        for (int i = 0; i < 4; i++) {
            randomanswers[i] = answer[randoms[i]];
        }
        return randomanswers;
    }

    public int getMaxPytan(String kategoria) {
       
        try {
             rs=getData("COUNT(*)", "pytania p join zestaw z on p.ZestawID=z.ZestawID WHERE z.Nazwa='"+kategoria+"'");
            while(rs.next()){
                return rs.getInt("COUNT(*)");
            }
        } catch (SQLException ex) {
            System.out.println("Bład pobrania ilości pytań dla kategorii\n"+ex);
            return 0;
        }
        
     return 0;   
    }

}
