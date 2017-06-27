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
public class Odpowiedzi {

    public int idPyt;
    public int idOdp;
    public String odp;
    public boolean pop;
    public boolean userOdp;

    public Odpowiedzi(int idPyt, int idOdp, String odp, boolean pop, boolean userOdp) {
        this.idPyt = idPyt;
        this.idOdp = idOdp;
        this.odp = odp;
        this.pop = pop;
        this.userOdp = userOdp;
    }

    public Odpowiedzi(int idPyt, int idOdp, String odp, boolean pop) {
        this.idPyt = idPyt;
        this.idOdp = idOdp;
        this.odp = odp;
        this.pop = pop;
        this.userOdp = false;
    }

    public void setUserOdp(boolean userOdp) {
        this.userOdp = userOdp;
    }

}
