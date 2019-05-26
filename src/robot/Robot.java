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

    
    public float a=0f, b=0.005f;
            
    Vector3f pierwsze_ramie = new Vector3f(1.8f, 4.0f, 0.0f);
        
        
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
        
        Transform3D przesuniecie_obserwatora = new Transform3D();       //USTAWIAMY OBSERWATORA W DOMYŚLNEJ POZYCJI
        przesuniecie_obserwatora.set(new Vector3f(0.0f,5.0f,30.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        simpleU.addBranchGraph(scena);
        
        canvas3D.addKeyListener(this);
        add(BorderLayout.CENTER, canvas3D);
        

    }

    BranchGroup utworzScene(){
        Point3d punkcik = new Point3d(0,0,0);  
        BranchGroup wezel_scena = new BranchGroup();
        TransformGroup wezel_temp = new TransformGroup();
        
        BoundingSphere bounds = new BoundingSphere(punkcik,1000000);
        
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
        
        
        
        
        //OSIE GŁÓWNE////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        //OŚ DRUGIEGO ELEMENTU ( TEŻ OBROTOWA)////////////////////////////////////////////////////////////////////////////////////////////////
//        Cylinder secondAxisX = new Cylinder(0.05f, 4.0f, wygladOsX);                //Opis drugiej osi X    
//        Transform3D  p_SecondAxisX   = new Transform3D();                           // 
//        p_SecondAxisX.set(new Vector3f(5.0f,5.0f,2.0f));                              //Przesuwamy o połowę długości secondAxisZ                                                      //Obrót o kąt 90 stopni
//        p_SecondAxisX.mul(tmp_X);                                                     //
//        TransformGroup transformacja_oX2 = new TransformGroup(p_SecondAxisX);          //
//        transformacja_oX2.addChild(secondAxisX);                                       //
//        
//        
//        Cylinder secondAxisY = new Cylinder(0.05f, 4.0f, wygladOsY);                //Opis drugiej osi Y    
//        Transform3D  p_SecondAxisY   = new Transform3D();                           // 
//        p_SecondAxisY.set(new Vector3f(5.0f,7.0f,0.0f));                              //Przesuwamy o połowę długości secondAxisZ                                                      //Obrót o kąt 90 stopni
//        p_SecondAxisY.mul(tmp_Y);                                                     //
//        TransformGroup transformacja_oY2 = new TransformGroup(p_SecondAxisY);          //
//        transformacja_oY2.addChild(secondAxisY);                                       //
//        
//        
//        Cylinder secondAxisZ = new Cylinder(0.05f, 4.0f, wygladOsZ);                //Opis drugiej osi Z    
//        Transform3D  p_SecondAxisZ   = new Transform3D();                           // 
//        p_SecondAxisZ.set(new Vector3f(7.0f,5.0f,0.0f));                              //Przesuwamy o połowę długości secondAxisZ                                                      //Obrót o kąt 90 stopni
//        p_SecondAxisZ.mul(tmp_Z);                                                     //
//        TransformGroup transformacja_oZ2 = new TransformGroup(p_SecondAxisZ);          //
//        transformacja_oZ2.addChild(secondAxisZ);                                       //
      
        
        //secondAxisAll.addChild(transformacja_oX2);                                     //Tworzenie rodzica dla całej osi drugiej
        //secondAxisAll.addChild(transformacja_oY2);
        //secondAxisAll.addChild(transformacja_oZ2);
        
        
        
        //Podstawa
        Material mat_podstawa = new Material(new Color3f(0.4f, 0.3f, 0.2f), new Color3f(0.3f, 0.2f, 0.3f), new Color3f(0.5f, 0.5f, 0.5f), new Color3f(0.3f, 0.3f, 0.3f), 128.0f);
        Appearance wyglad_podstawa = new Appearance();
        wyglad_podstawa.setMaterial(mat_podstawa);
        
        TransformGroup podstawa_tg = new TransformGroup();
        wezel_temp.addChild(podstawa_tg);
        Cylinder podstawa = new Cylinder(0.8f, 4, wyglad_podstawa);
        podstawa_tg.addChild(podstawa);
        Transform3D p_podstawa = new Transform3D();
        p_podstawa.set(new Vector3f(0.0f, 2.0f, 0.0f));
        podstawa_tg.setTransform(p_podstawa);
        
        
        //Pierwszy element ramienia
        TransformGroup matka_ramie_1_tg = new TransformGroup();                                //transformgroup obracający się i zawierający ramię
        wezel_temp.addChild(matka_ramie_1_tg);
        
        Material mat_ramie_1 = new Material(new Color3f(0.2f, 0.3f, 0.1f), new Color3f(0.7f, 0.1f, 0.0f), new Color3f(0.4f, 0.9f, 0.1f), new Color3f(0.3f, 0.9f, 0.1f), 50.0f);
        Appearance wyglad_ramie_1 = new Appearance();
        wyglad_ramie_1.setMaterial(mat_ramie_1);
        
        TransformGroup ramie_1_tg = new TransformGroup();                                      //transformgroup z ramieniem przesuniętym
        com.sun.j3d.utils.geometry.Box ramie_1 = new com.sun.j3d.utils.geometry.Box(3.0f, 0.1f, 1.0f, wyglad_ramie_1);
        Transform3D p_ramie_1 = new Transform3D();
        p_ramie_1.set(pierwsze_ramie);
        ramie_1_tg.setTransform(p_ramie_1);
        matka_ramie_1_tg.addChild(ramie_1_tg);
        ramie_1_tg.addChild(ramie_1);
        
        //Drugi element ramienia
        Material mat_ramie_2 = new Material(new Color3f(0.1f, 0.2f, 0.3f), new Color3f(0.0f, 0.7f, 0.1f), new Color3f(0.1f, 0.4f, 0.9f), new Color3f(0.1f, 0.3f, 0.f), 50.0f);
        Appearance wyglad_ramie_2 = new Appearance();     //materiał, wygląd
        wyglad_ramie_2.setMaterial(mat_ramie_2);
        
        
        TransformGroup matka_ramie_2_tg = new TransformGroup();             //transformgroup obracający się i zawierający ramię
        ramie_1_tg.addChild(matka_ramie_2_tg);
        Transform3D p_matka_ramie_2 = new Transform3D();
        p_matka_ramie_2.set(new Vector3f(3.0f, 0.6f, 0.0f));
        matka_ramie_2_tg.setTransform(p_matka_ramie_2);
        
        
        TransformGroup ramie_2_tg = new TransformGroup();                   //tansfromgroup z ramieniem 2 przesuniętym
        com.sun.j3d.utils.geometry.Box ramie_2 = new com.sun.j3d.utils.geometry.Box(1.0f, 0.5f, 3.0f, wyglad_ramie_2);
        Transform3D p_ramie_2 = new Transform3D();
        p_ramie_2.set(new Vector3f(0.0f, 0.0f, 2.5f));
        ramie_2_tg.setTransform(p_ramie_2);
        matka_ramie_2_tg.addChild(ramie_2_tg);
        ramie_2_tg.addChild(ramie_2);
        
        
        TransformGroup ramie_2_walec_1_tg = new TransformGroup();
        Transform3D p_ramie_2_walec_1 = new Transform3D();
        p_ramie_2_walec_1.set(new Vector3f(0f, 0f, -3f));
        Cylinder ramie_2_walec_1 = new Cylinder(1.0f, 1f, wyglad_ramie_2);
        ramie_2_walec_1_tg.setTransform(p_ramie_2_walec_1);
        ramie_2_tg.addChild(ramie_2_walec_1_tg);
        ramie_2_walec_1_tg.addChild(ramie_2_walec_1);
        
        Cylinder ramie_2_walec_2 = new Cylinder(1.0f, 1f, wyglad_ramie_2);
        TransformGroup ramie_2_walec_2_tg = new TransformGroup();
        Transform3D p_ramie_2_walec_2 = new Transform3D();
        p_ramie_2_walec_2.set(new Vector3f(0f, 0f, 3f));
        ramie_2_walec_2_tg.setTransform(p_ramie_2_walec_2);
        ramie_2_walec_2_tg.addChild(ramie_2_walec_2);
        ramie_2_tg.addChild(ramie_2_walec_2_tg);
        
        
        
        //Łączenie ramion
        Cylinder polaczenie_1_2 = new Cylinder(0.1f, 0.4f, wyglad_podstawa);
        matka_ramie_2_tg.addChild(polaczenie_1_2);
        
        
        //Pionowy suwak
        
        
        Appearance wyglad_pionowy = new Appearance();
        wyglad_pionowy.setMaterial(mat_ramie_1);
        
        
        TransformGroup pionowy_tg = new TransformGroup();
        Cylinder pionowy = new Cylinder(0.1f, 4f, wyglad_pionowy);
        ramie_2_tg.addChild(pionowy_tg);
        pionowy_tg.addChild(pionowy);
        Transform3D p_pionowy = new Transform3D();
        p_pionowy.set(new Vector3f(0.0f, 1.0f, (ramie_2.getZdimension()) ));
        pionowy_tg.setTransform(p_pionowy);
        
        
        
        
        //Działanie MYSZY//////////// ////////////////////////////////////////////////////////////////////////////////////
        
        
        //obracanie pierwszego ramienia
        MouseRotate obracanie_1 = new MouseRotate();                                   //OBROÓT ZA POMOCĄ MYSZY(OBA PRZCISKI)
        matka_ramie_1_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);             //obracanie górnego elementu robota
        obracanie_1.setFactor(a, 0);                                                      //mnożnik ruchu 0 - brak obrotu
        obracanie_1.setTransformGroup(matka_ramie_1_tg);
        wezel_temp.addChild(obracanie_1);
        obracanie_1.setSchedulingBounds(bounds);
        
        
        //obracanie drugiego ramienia
        MouseRotate obracanie_2 = new MouseRotate();                                   //OBROÓT ZA POMOCĄ MYSZY(OBA PRZCISKI)
        matka_ramie_2_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);             //obracanie górnego elementu robota
        obracanie_2.setFactor(b, 0);                                                      //mnożnik ruchu 0 - brak obrotu
        obracanie_2.setTransformGroup(matka_ramie_2_tg);
        wezel_temp.addChild(obracanie_2);
        obracanie_2.setSchedulingBounds(bounds);
        
        
        //obracanie całego dzieła
        MouseRotate obracanie_cale = new MouseRotate();
        wezel_temp.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        obracanie_cale.setFactor(0);                                                //mnożnik ruchu
        wezel_temp.addChild(obracanie_cale);
        obracanie_cale.setTransformGroup(wezel_temp);
        obracanie_cale.setSchedulingBounds(bounds);
        
        //Przesuwanie pionowego
        MouseTranslate suwanie_pionowe = new MouseTranslate();
        pionowy_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        suwanie_pionowe.setFactor(0, 0.001);
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
        
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
            
        {a=0.005f;
        b=0;
            System.out.println("abba");}
        else if(e.getKeyCode()== KeyEvent.VK_LEFT)
        {
            
        }
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        {a=0f;
        b=0.005f;
            System.out.println("dabob");}
        
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}



//Todamasd
