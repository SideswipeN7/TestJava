/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import javax.swing.ImageIcon;

/**
 *
 * @author super
 */
public class Pytanie {

     public int idPytania;
    public ImageIcon img;
    public String pytanie;

    public Pytanie(int idPytania, ImageIcon img, String pytanie) {
        this.idPytania = idPytania;
        this.img = img;
        this.pytanie = pytanie;
    }
    
        public Pytanie(int idPytania, String pytanie) {
        this.idPytania = idPytania;
        this.pytanie = pytanie;
    }

    public String toString() {
        return pytanie;
    }
}
