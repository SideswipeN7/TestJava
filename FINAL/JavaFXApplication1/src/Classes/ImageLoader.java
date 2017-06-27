/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.awt.Image;
import javax.swing.JLabel;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

/**
 *
 * @author super
 */
public class ImageLoader {

    private ImageIcon il;
    private String fileName;

    public ImageLoader() {
    }

    public void ladujObraz(JLabel l) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
         fileName = f.getAbsolutePath();
        ImageIcon icon = new ImageIcon(fileName);
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(l.getWidth(), l.getHeight(), java.awt.Image.SCALE_SMOOTH);
        il = new ImageIcon(resizedImage);
        l.setIcon(il);
    }

    public ImageIcon getImageIcon() {
        return il;
    }
    
    public void clearImage(JLabel l){
        il=null;
        l.setIcon(il);
    }
    public String getPath(){
        return fileName;
    }

}
