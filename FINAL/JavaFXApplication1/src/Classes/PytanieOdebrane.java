/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.sql.Blob;

/**
 *
 * @author super
 */
public class PytanieOdebrane {
    public int id;
    public String pytanie;
    public Blob obraz;
    public int punkty;

    public PytanieOdebrane(int id, String pytanie, Blob obraz, int punkty) {
        this.id = id;
        this.pytanie = pytanie;
        this.obraz = obraz;
        this.punkty=punkty;
    }
    
}
