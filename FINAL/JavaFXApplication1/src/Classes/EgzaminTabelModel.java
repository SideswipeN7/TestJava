/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author super
 */
public class EgzaminTabelModel extends AbstractTableModel {

    private List<Egzamin> egz = new ArrayList();
    private String[] columnNames = {"Użytkownik", "Zestaw", "Data"};

    public EgzaminTabelModel(List<Egzamin> list) {
        this.egz = list;
    }

   public String getSelectedEgzamin(int index){
       Object o =egz.indexOf(index);
       Egzamin e =(Egzamin)o;
       return e.userName+" "+e.nazwaZestawu;
   }
   
    @Override
    public int getRowCount() {
        return egz.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //return (Object) egz.get(rowIndex);
        Egzamin si = egz.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return si.userName;
            case 1:
                return si.nazwaZestawu;
            case 2:
                return si.data;
            case 3:
                return si.idEgzaminu;
        }
        return null;

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Integer.class;
        }
        return null;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

}
