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



public class Robot extends JFrame{

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

        Transform3D przesuniecie_obserwatora = new Transform3D();       //USTAWIAMY OBSERWATORA W DOMYŚLNEJ POZYCJI
        przesuniecie_obserwatora.set(new Vector3f(0.0f,5.0f,30.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        simpleU.addBranchGraph(scena);

    }

    BranchGroup utworzScene(){
        Point3d punkcik = new Point3d(0,0,0);  
        BranchGroup wezel_scena = new BranchGroup();
        TransformGroup wezel_temp = new TransformGroup();
        
        BoundingSphere bounds = new BoundingSphere(punkcik,10000);
        
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
        float axisPosition [] = new float[]{2.0f,2.0f,2.0f};              // X = 0, Y = 1, Z = 2 Y nie używamy
        TransformGroup mainAxisAll = new TransformGroup();
        
        Transform3D tmp_X = new Transform3D();
        Transform3D tmp_Y = new Transform3D();                                      //Nie używamy tego ale nie uwuwam bo może się przydać
        Transform3D tmp_Z = new Transform3D();
        
        
        Appearance  wygladOsX = new Appearance();
        wygladOsX.setColoringAttributes(new ColoringAttributes(1.0f,0.0f,0.0f,ColoringAttributes.NICEST));
        Cylinder mainAxisX = new Cylinder(0.05f, 4.0f, wygladOsX);                  //Opis głownej osi X                                                 
        Transform3D  p_mainAxisX   = new Transform3D();                             //  
        p_mainAxisX.set(new Vector3f(0.0f,0.0f,2.0f));                             //Przesuwamy o połowę długości mainAxisX   
        tmp_X.rotX(Math.PI/2);                                                      //Obrót o kąt 90 stopni
        p_mainAxisX.mul(tmp_X);                                                     //
        TransformGroup transformacja_oX = new TransformGroup(p_mainAxisX);          //
        transformacja_oX.addChild(mainAxisX);                                       //
        //wezel_temp.addChild(transformacja_oX);                                      //
        
        
        Appearance  wygladOsY = new Appearance();
        wygladOsY.setColoringAttributes(new ColoringAttributes(0.0f,1.0f,0.0f,ColoringAttributes.NICEST));
        Cylinder mainAxisY = new Cylinder(0.05f, 4.0f, wygladOsY);                   //Opis głownej osi Y                                                 
        Transform3D  p_mainAxisY   = new Transform3D();                             // 
        p_mainAxisY.set(new Vector3f(0.0f,2.0f,0.0f));                              //Przesuwamy o połowę długości mainAxisY
        TransformGroup transformacja_oY = new TransformGroup(p_mainAxisY);          //
        transformacja_oY.addChild(mainAxisY);                                       //
        //wezel_temp.addChild(transformacja_oY);                                     //

        
       
        Appearance  wygladOsZ = new Appearance();
        wygladOsZ.setColoringAttributes(new ColoringAttributes(0.0f,0.0f,1.0f,ColoringAttributes.NICEST));
        Cylinder mainAxisZ = new Cylinder(0.05f, 4.0f, wygladOsZ);                  //Opis głownej osi Z                                                 
        Transform3D  p_mainAxisZ   = new Transform3D();                             // 
        p_mainAxisZ.set(new Vector3f(2.0f,0.0f,0.0f));                              //Przesuwamy o połowę długości mainAxisZ
        tmp_Z.rotZ(Math.PI/2);                                                      //Obrót o kąt 90 stopni
        p_mainAxisZ.mul(tmp_Z);                                                     //
        TransformGroup transformacja_oZ = new TransformGroup(p_mainAxisZ);          //
        transformacja_oZ.addChild(mainAxisZ);                                       //
        //wezel_temp.addChild(transformacja_oZ);                                      //
        
        mainAxisAll.addChild(transformacja_oX);                                     //Tworzenie rodzica dla całej osi głównej
        mainAxisAll.addChild(transformacja_oY);
        mainAxisAll.addChild(transformacja_oZ);
        wezel_temp.addChild(mainAxisAll);
        
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
        
        
        
        
        //Pierwszy element ramienia
        TransformGroup matka_ramie_1_tg = new TransformGroup();
        wezel_temp.addChild(matka_ramie_1_tg);
        
        Material mat_ramie_1 = new Material(new Color3f(0.2f, 0.3f, 0.1f), new Color3f(0.7f, 0.1f, 0.0f), new Color3f(0.4f, 0.9f, 0.1f), new Color3f(0.3f, 0.9f, 0.1f), 50.0f);
        Appearance wyglad_ramie_1 = new Appearance();
        wyglad_ramie_1.setMaterial(mat_ramie_1);
        
        TransformGroup ramie_1_tg = new TransformGroup();
        com.sun.j3d.utils.geometry.Box ramie_1 = new com.sun.j3d.utils.geometry.Box(3.0f, 0.1f, 1.0f, wyglad_ramie_1);
        Transform3D p_ramie_1 = new Transform3D();
        p_ramie_1.set(new Vector3f(1.8f, 4.0f, 0.0f));
        ramie_1_tg.setTransform(p_ramie_1);
        matka_ramie_1_tg.addChild(ramie_1_tg);
        ramie_1_tg.addChild(ramie_1);
        
        
        
        //Drugi element ramienia
        Material mat_ramie_2 = new Material(new Color3f(0.1f, 0.2f, 0.3f), new Color3f(0.0f, 0.7f, 0.1f), new Color3f(0.1f, 0.4f, 0.9f), new Color3f(0.1f, 0.3f, 0.f), 50.0f);
        Appearance wyglad_ramie_2 = new Appearance();
        wyglad_ramie_2.setMaterial(mat_ramie_2);
        
        
        TransformGroup matka_ramie_2_tg = new TransformGroup();
        ramie_1_tg.addChild(matka_ramie_2_tg);
        Transform3D p_matka_ramie_2 = new Transform3D();
        p_matka_ramie_2.set(new Vector3f(2.0f, 0.2f, 0.0f));
        matka_ramie_2_tg.setTransform(p_matka_ramie_2);
        
        
        TransformGroup ramie_2_tg = new TransformGroup();
        com.sun.j3d.utils.geometry.Box ramie_2 = new com.sun.j3d.utils.geometry.Box(1.0f, 0.1f, 3.0f, wyglad_ramie_2);
        Transform3D p_ramie_2 = new Transform3D();
        p_ramie_2.set(new Vector3f(0.0f, 0.0f, 2.0f));
        ramie_2_tg.setTransform(p_ramie_2);
        matka_ramie_2_tg.addChild(ramie_2_tg);
        ramie_2_tg.addChild(ramie_2);
        
        
        //Pionowy suwak
        
        
        Appearance wyglad_pionowy = new Appearance();
        wyglad_pionowy.setMaterial(mat_ramie_1);
        
        
        TransformGroup pionowy_tg = new TransformGroup();
        Cylinder pionowy = new Cylinder(0.1f, 4f, wyglad_pionowy);
        // matka_pionowy_tg.addChild(pionowy_tg);
        ramie_2_tg.addChild(pionowy_tg);
        pionowy_tg.addChild(pionowy);
        Transform3D p_pionowy = new Transform3D();
        p_pionowy.set(new Vector3f(0.0f, 1.0f, (ramie_2.getZdimension())-(ramie_2.getZdimension())/4 ));
        pionowy_tg.setTransform(p_pionowy);
        
        
        
        
        //Działanie MYSZY////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        //obracanie pierwszego ramienia
        MouseRotate obracanie_1 = new MouseRotate();                                   //OBROÓT ZA POMOCĄ MYSZY(OBA PRZCISKI)
        matka_ramie_2_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);             //obracanie górnego elementu robota
        obracanie_1.setFactor(0.05, 0);                                                      //mnożnik ruchu 0 - brak obrotu
        obracanie_1.setTransformGroup(matka_ramie_2_tg);
        wezel_temp.addChild(obracanie_1);
        obracanie_1.setSchedulingBounds(bounds);
        
        
        //obracanie całego dzieła
        MouseRotate obracanie_cale = new MouseRotate();
        wezel_temp.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        obracanie_cale.setFactor(0.001);                                                //mnożnik ruchu
        wezel_temp.addChild(obracanie_cale);
        obracanie_cale.setTransformGroup(wezel_temp);
        obracanie_cale.setSchedulingBounds(bounds);
        
        //Przesuwanie pionowego
        
        
        
        
        
         
        
        
        
        
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
      new Robot();

   }
    
}



