package robot;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.Transform3D;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.keyboard.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;



public class Robot extends JFrame implements ActionListener, KeyListener{
    
    //WSZYSTKIE DEKLARACJE ZMIENNYCH////////////////////////////////////////////////////////////////////////

    TransformGroup wezel_temp = new TransformGroup();   
    TransformGroup podstawa_tg = new TransformGroup();
    TransformGroup matka_ramie_1_tg = new TransformGroup();                                //transformgroup obracający się i zawierający ramię
    TransformGroup ramie_1_tg = new TransformGroup();                                      //transformgroup z ramieniem przesuniętym
    TransformGroup matka_ramie_2_tg = new TransformGroup();             //transformgroup obracający się i zawierający ramię
    TransformGroup ramie_2_tg = new TransformGroup();                   //tansfromgroup z ramieniem 2 przesuniętym
    TransformGroup ramie_2_walec_1_tg = new TransformGroup();
    TransformGroup ramie_2_walec_2_tg = new TransformGroup();
    TransformGroup ramie_1_walec_1_tg = new TransformGroup();
    TransformGroup ramie_1_walec_2_tg = new TransformGroup();
    TransformGroup pionowy_tg = new TransformGroup();
            
    Transform3D p_ramie_1 = new Transform3D();
    Transform3D przesuniecie_obserwatora = new Transform3D();       //USTAWIAMY OBSERWATORA W DOMYŚLNEJ POZYCJI
    Transform3D p_podstawa = new Transform3D();
    Transform3D p_matka_ramie_2 = new Transform3D();
    Transform3D p_ramie_2 = new Transform3D();
    Transform3D p_ramie_2_walec_1 = new Transform3D();
    Transform3D p_ramie_2_walec_2 = new Transform3D();
    Transform3D p_ramie_1_walec_1 = new Transform3D();
    Transform3D p_ramie_1_walec_2 = new Transform3D();
    Transform3D p_pionowy = new Transform3D();
    
    float kat1 = 0.0f;      // Kąt początkowy
    float kat2 = 0.0f;      //Dla wszystkich
    float v_obrotu = 0.005f;  //Prędkość obrotu
    float v_pionowe = 0.05f;  //Prędkość elementu ruchomego
    float w_gore = 0.0f, w_dol = 0.0f;
    public float a=0f, b=0.005f;
    
    MouseRotate obracanie_1 = new MouseRotate();
    MouseRotate obracanie_cale = new MouseRotate();
    MouseRotate obracanie_2 = new MouseRotate();
    
    
    boolean spacja = true;
    boolean control = true;
    float mnoznik = 0.001f;
            
 
        
        
    Robot(){
        //OGÓLNIE KOD WZIĘTY Z LABORKI ////////////////////////////////////////////////////////////////////////////////////////////////
          super("SCARA");
          
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

    
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(800,600));          //ROZMIAR OKNA

        add(canvas3D);
        pack();
        setVisible(true);

        BranchGroup scena = utworzScene();
	    scena.compile();

        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
        simpleU.getViewingPlatform().setNominalViewingTransform();
        
        przesuniecie_obserwatora.set(new Vector3f(0.0f,5.0f,30.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        simpleU.addBranchGraph(scena);
        
        canvas3D.addKeyListener(this);
        add(BorderLayout.CENTER, canvas3D);
        

    }
    BranchGroup utworzScene(){
        Point3d punkcik = new Point3d(0,0,0);  
        BranchGroup wezel_scena = new BranchGroup();
        
        BoundingSphere bounds = new BoundingSphere(punkcik,1000);
        
      //ŚWIATŁO////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      
      //AmbientLight lightA = new AmbientLight();
      //lightA.setInfluencingBounds(bounds);
      //wezel_temp.addChild(lightA);

      DirectionalLight lightD = new DirectionalLight();
      lightD.setInfluencingBounds(bounds);
      lightD.setDirection(new Vector3f(0.0f, 0.0f, -2.0f));
      lightD.setColor(new Color3f(1.0f, 1.0f, 1.0f));
      wezel_temp.addChild(lightD);
      
      DirectionalLight lightD2 = new DirectionalLight();
      lightD2.setInfluencingBounds(bounds);
      lightD2.setDirection(new Vector3f(0.0f,0.0f, -20.0f));
      lightD2.setColor(new Color3f(1.0f, 0.0f, 1.0f));
      wezel_scena.addChild(lightD2);
        
        
        //Podstawa
        Material mat_podstawa = new Material(new Color3f(0.75f, 0.75f, 0.75f), new Color3f(0.3f, 0.2f, 0.3f), new Color3f(0.5f, 0.5f, 0.5f), new Color3f(0.3f, 0.3f, 0.3f), 128.0f);
        Appearance wyglad_podstawa = new Appearance();
        wyglad_podstawa.setMaterial(mat_podstawa);
        
        wezel_temp.addChild(podstawa_tg);
        Cylinder podstawa = new Cylinder(0.8f, 4, 1, 1000, 1000, wyglad_podstawa);
        podstawa_tg.addChild(podstawa);
        p_podstawa.set(new Vector3f(0.0f, 2.0f, 0.0f));
        podstawa_tg.setTransform(p_podstawa);
        
        
        //Pierwszy element ramienia
        wezel_temp.addChild(matka_ramie_1_tg);
        
        Material mat_ramie_1 = new Material(new Color3f(0.2f, 0.3f, 0.1f), new Color3f(0.57f, 0.57f, 0.57f), new Color3f(0.4f, 0.9f, 0.1f), new Color3f(0.3f, 0.9f, 0.1f), 50.0f);
        Appearance wyglad_ramie_1 = new Appearance();
        wyglad_ramie_1.setMaterial(mat_ramie_1);
        
        Material mat_ramie_2 = new Material(new Color3f(0.1f, 0.2f, 0.3f), new Color3f(0.0f, 0.7f, 0.1f), new Color3f(0.1f, 0.4f, 0.9f), new Color3f(0.1f, 0.3f, 0.f), 50.0f);
        Appearance wyglad_ramie_2 = new Appearance();     //materiał, wygląd
        wyglad_ramie_2.setMaterial(mat_ramie_2);
        
        com.sun.j3d.utils.geometry.Box ramie_1 = new com.sun.j3d.utils.geometry.Box(2.0f, 0.3f, 1.0f, wyglad_ramie_1);
        p_ramie_1.set(new Vector3f(2f, 4.0f, 0.0f));
        ramie_1_tg.setTransform(p_ramie_1);
        matka_ramie_1_tg.addChild(ramie_1_tg);
        ramie_1_tg.addChild(ramie_1);
        
        
        Cylinder ramie_1_walec_1 = new Cylinder(1.0f, 0.6f, 1, 1000, 1, wyglad_ramie_1);
        p_ramie_1_walec_1.set(new Vector3f(0f, 2f, 0f));
        ramie_1_walec_1_tg.setTransform(p_ramie_1_walec_1);
        ramie_1_walec_1_tg.addChild(ramie_1_walec_1);
        podstawa_tg.addChild(ramie_1_walec_1_tg);
        
        
        Cylinder ramie_1_walec_2 = new Cylinder(1.0f, 0.6f, 1, 1000, 1, wyglad_ramie_1);
        p_ramie_1_walec_2.set(new Vector3f(2f, 0f, 0f));
        ramie_1_walec_2_tg.setTransform(p_ramie_1_walec_2);
        ramie_1_walec_2_tg.addChild(ramie_1_walec_2);
        ramie_1_tg.addChild(ramie_1_walec_2_tg);
        
        
        
        //Drugi element ramienia

        
        
        ramie_1_tg.addChild(matka_ramie_2_tg);
        p_matka_ramie_2.set(new Vector3f(2f, 0.6f, 0.0f));
        matka_ramie_2_tg.setTransform(p_matka_ramie_2);
        
        
        com.sun.j3d.utils.geometry.Box ramie_2 = new com.sun.j3d.utils.geometry.Box(1.0f, 0.3f, 2.0f, wyglad_ramie_2);
        p_ramie_2.set(new Vector3f(0.0f, 0.0f, 2f));
        ramie_2_tg.setTransform(p_ramie_2);
        matka_ramie_2_tg.addChild(ramie_2_tg);
        ramie_2_tg.addChild(ramie_2);
        
        
        p_ramie_2_walec_1.set(new Vector3f(0f, 0f, -2f));
        Cylinder ramie_2_walec_1 = new Cylinder(1.0f, 0.6f, 1, 1000, 1, wyglad_ramie_2);
        ramie_2_walec_1_tg.setTransform(p_ramie_2_walec_1);
        ramie_2_tg.addChild(ramie_2_walec_1_tg);
        ramie_2_walec_1_tg.addChild(ramie_2_walec_1);
        
        Cylinder ramie_2_walec_2 = new Cylinder(1.0f, 0.6f, 1, 1000, 1, wyglad_ramie_2);
        p_ramie_2_walec_2.set(new Vector3f(0f, 0f, 2f));
        ramie_2_walec_2_tg.setTransform(p_ramie_2_walec_2);
        ramie_2_walec_2_tg.addChild(ramie_2_walec_2);
        ramie_2_tg.addChild(ramie_2_walec_2_tg);
        
        
        
        //Łączenie ramion
        Cylinder polaczenie_1_2 = new Cylinder(0.1f, 0.4f, wyglad_podstawa);
        matka_ramie_2_tg.addChild(polaczenie_1_2);
        
        
        //Pionowy suwak
        
        
        Appearance wyglad_pionowy = new Appearance();
        wyglad_pionowy.setMaterial(mat_ramie_1);
        
        
        Cylinder pionowy = new Cylinder(0.1f, 4f, 1, 100, 100, wyglad_pionowy);
        ramie_2_tg.addChild(pionowy_tg);
        pionowy_tg.addChild(pionowy);
        p_pionowy.set(new Vector3f(0.0f, 1.0f, (ramie_2.getZdimension()) ));
        pionowy_tg.setTransform(p_pionowy);
        System.out.println(ramie_2.getZdimension());
        
        
        
        
        //Działanie MYSZY//////////// ////////////////////////////////////////////////////////////////////////////////////
        
        
        //obracanie pierwszego ramienia
                                           //OBROÓT ZA POMOCĄ MYSZY(OBA PRZCISKI)
        matka_ramie_1_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);             //obracanie górnego elementu robota
        obracanie_1.setFactor(0, 0);                                                      //mnożnik ruchu 0 - brak obrotu
        obracanie_1.setTransformGroup(matka_ramie_1_tg);
        wezel_temp.addChild(obracanie_1);
        obracanie_1.setSchedulingBounds(bounds);
        
        
        //obracanie drugiego ramienia                                 //OBROÓT ZA POMOCĄ MYSZY(OBA PRZCISKI)
        matka_ramie_2_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);             //obracanie górnego elementu robota
        obracanie_2.setFactor(0, 0);                                                      //mnożnik ruchu 0 - brak obrotu
        obracanie_2.setTransformGroup(matka_ramie_2_tg);
        wezel_temp.addChild(obracanie_2);
        obracanie_2.setSchedulingBounds(bounds);
        
        
        //obracanie całego dzieła
        wezel_temp.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        obracanie_cale.setFactor(mnoznik);                                                //mnożnik ruchu
        wezel_temp.addChild(obracanie_cale);
        obracanie_cale.setTransformGroup(wezel_temp);
        obracanie_cale.setSchedulingBounds(bounds);
        
        //Przesuwanie pionowego
        MouseTranslate suwanie_pionowe = new MouseTranslate();
        pionowy_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        suwanie_pionowe.setFactor(0, mnoznik*3);
        pionowy_tg.addChild(suwanie_pionowe);
        suwanie_pionowe.setTransformGroup(pionowy_tg);
        BoundingSphere bounds2 = new BoundingSphere(punkcik, 0.00001);
        ramie_2_tg.setBounds(bounds2);
        suwanie_pionowe.setSchedulingBounds(bounds2);
        
        
        
         
        
        
        
        
        MouseWheelZoom zoom = new MouseWheelZoom();                                 //PRZYBLIŻANIE I ODDALANIE
        zoom.setTransformGroup(wezel_temp);
        zoom.setSchedulingBounds(bounds);
        wezel_temp.addChild(zoom);
        
        //Działanie KLAWIATURY////////////////////////////////////////////////////////////////////////////////////////////////

    
        
        //KOŃEC////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        wezel_scena.addChild(wezel_temp);
        return wezel_scena;

    }

     public static void main(String args[]){

         
      //new Robot();
        Robot bb = new Robot();
        bb.addKeyListener(bb);

   }

    @Override
    public void actionPerformed(ActionEvent e) {
     
        
        
        
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        switch(e.getKeyCode())                                                                          
        {
            case KeyEvent.VK_LEFT   :     obrotLewoMain(v_obrotu);      if(v_obrotu < 0.14f)v_obrotu = v_obrotu*1.1f;   break;   
            case KeyEvent.VK_RIGHT  :     obrotPrawoMain(v_obrotu);     if(v_obrotu < 0.14f)v_obrotu = v_obrotu*1.1f;   break;
            case KeyEvent.VK_D      :     obrotPrawoSecond(v_obrotu);   if(v_obrotu < 0.14f)v_obrotu = v_obrotu*1.1f;   break;
            case KeyEvent.VK_A      :     obrotLewoSecond(v_obrotu);    if(v_obrotu < 0.14f)v_obrotu = v_obrotu*1.1f;   break;
            case KeyEvent.VK_DOWN   :     dol(v_pionowe);       break;
            case KeyEvent.VK_UP     :     gora(v_pionowe);      break;
            case KeyEvent.VK_SPACE  :     if(spacja&control){obrotRamie1(); spacja = false;};                                  break;
            case KeyEvent.VK_CONTROL  :   if(control&spacja){obrotRamie2(); control = false;};                                  break;
        }
 
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        {
        
        switch(e.getKeyCode())                                                                          
        {
            case KeyEvent.VK_SPACE  :     if(!spacja&control){obrotRamie1_rev(); spacja = true;}; System.out.println("Narko"); 
            case KeyEvent.VK_CONTROL  :   if(!control&spacja){obrotRamie2_rev(); control = true;}; System.out.println("Narko");                                  break;
        }
        
        v_obrotu = 0.005f;
        
        }//  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void obrotRamie1(){
        obracanie_1.setFactor(mnoznik, 0);
        obracanie_cale.setFactor(0);
        System.out.println("Halko");
    }
    
    private void obrotRamie1_rev(){
        obracanie_1.setFactor(0);
        obracanie_cale.setFactor(mnoznik);
        System.out.println("Halko");
    }
    
    
        private void obrotRamie2(){
        obracanie_2.setFactor(mnoznik, 0);
        obracanie_cale.setFactor(0);
        System.out.println("Halko");
    }
    
        private void obrotRamie2_rev(){
        obracanie_2.setFactor(0);
        obracanie_cale.setFactor(mnoznik);
        System.out.println("Halko");
    }
    
    
    
    private void obrotLewoMain(float krok){
      kat1 += krok;    
      p_ramie_1.rotY(kat1);
      //p_ramie_1.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
      matka_ramie_1_tg.setTransform(p_ramie_1); 
    }
    private void obrotPrawoMain(float krok){
      kat1 -= krok;    
      p_ramie_1.rotY(kat1);
      //p_ramie_1.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
      matka_ramie_1_tg.setTransform(p_ramie_1); 
    }
    private void obrotLewoSecond(float krok){
      kat2 += krok;    
      p_ramie_2.rotY(kat2);
      p_ramie_2.setTranslation(new Vector3f(2f, 0.6f, 0.0f));
      matka_ramie_2_tg.setTransform(p_ramie_2); 
    }
    private void obrotPrawoSecond(float krok){
      kat2 -= krok;    
      p_ramie_2.rotY(kat2);
      p_ramie_2.setTranslation(new Vector3f(2f, 0.6f, 0.0f));
      matka_ramie_2_tg.setTransform(p_ramie_2);
    }
    private void gora(float krok){   
      if(w_gore<0.6){
      w_gore = w_gore+krok; 
      p_pionowy.setTranslation(new Vector3f(0.0f, 1.0f+w_gore, 2f));
      pionowy_tg.setTransform(p_pionowy);   
      }
    }
      
    private void dol(float krok){
      if(w_gore>-2.5){
      w_gore = w_gore- krok; 
      p_pionowy.setTranslation(new Vector3f(0.0f, 1.0f+w_gore, 2f));
      pionowy_tg.setTransform(p_pionowy); 
      }
    }
    
    
}



//Todamasd
