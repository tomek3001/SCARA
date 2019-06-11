package robot;

import javax.swing.JOptionPane;


public class WspolczynnikObrotu {
    public float wsp_obrotu(String text){
    
    float obrot = 1;
        try {
        obrot = Float.parseFloat(text);    
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.setName("Błąd");
                       
        }
    if (obrot>30) obrot = 30;
    else if (obrot<0) obrot = 0.1f;
    return obrot;
    
    }
    
}
