/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author super
 */
public class Egzamin {
    
    String userName;
    int userId;
    String nazwaZestawu;
    int zestawId;
    String data;
    int idEgzaminu;

    public Egzamin(String userName, int userId, String nazwaZestawu, int zestawId, String data, int idEgzaminu) {
        this.userName = userName;
        this.userId = userId;
        this.nazwaZestawu = nazwaZestawu;
        this.zestawId = zestawId;
        this.data = data;
        this.idEgzaminu = idEgzaminu;
    }

    
    public  String toString(){
        return userName+" "+nazwaZestawu+" "+data;
    }
}
