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
import javax.vecmath.Point3d;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.image.TextureLoader;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;




public class Robot extends JFrame implements ActionListener, KeyListener{
    
    
    
   
    //WSZYSTKIE DEKLARACJE ZMIENNYCH////////////////////////////////////////////////////////////////////////
     private Timer timer = new Timer();
    
    
     
    Transform3D p_ramie_1 = new Transform3D();
    Transform3D przesuniecie_obserwatora = new Transform3D();       //USTAWIAMY OBSERWATORA W DOMYŚLNEJ POZYCJI
    Transform3D p_noga_robota = new Transform3D();
    Transform3D p_matka_ramie_2 = new Transform3D();
    Transform3D p_matka_ramie_2_mysz = new Transform3D();
    Transform3D p_ramie_2 = new Transform3D();
    Transform3D p_ramie_2_walec_1 = new Transform3D();
    Transform3D p_ramie_2_walec_2 = new Transform3D();
    Transform3D p_ramie_1_walec_1 = new Transform3D();
    Transform3D p_ramie_1_walec_2 = new Transform3D();
    Transform3D p_pionowy = new Transform3D();
    Transform3D temp_transform = new Transform3D();
    Transform3D temp_transform2 = new Transform3D();
    Transform3D zmniejszenie_calosci = new Transform3D();
     
    
    TransformGroup wezel_temp = new TransformGroup(zmniejszenie_calosci);   
    TransformGroup noga_robota_tg = new TransformGroup();
    TransformGroup matka_ramie_1_tg = new TransformGroup();                                //transformgroup obracający się i zawierający ramię
    TransformGroup matka_ramie_1_tg_mysz = new TransformGroup();
    TransformGroup ramie_1_tg = new TransformGroup();                                      //transformgroup z ramieniem przesuniętym
    TransformGroup matka_ramie_2_tg = new TransformGroup();             //transformgroup obracający się i zawierający ramię
    TransformGroup matka_ramie_2_tg_mysz = new TransformGroup(); 
    TransformGroup ramie_2_tg = new TransformGroup();                   //tansfromgroup z ramieniem 2 przesuniętym
    TransformGroup ramie_2_walec_1_tg = new TransformGroup();
    TransformGroup ramie_2_walec_2_tg = new TransformGroup();
    TransformGroup ramie_1_walec_1_tg = new TransformGroup();
    TransformGroup ramie_1_walec_2_tg = new TransformGroup();
    TransformGroup pionowy_tg = new TransformGroup();
    TransformGroup stolik_tg = new TransformGroup();
    TransformGroup blat_tg = new TransformGroup();
    TransformGroup tg_obiektu = new TransformGroup();
    
                                                //ZMIENNE I PARAMETRY
    
    Vector3f p_obserwatora = new Vector3f(0.0f,0.1f,3.0f);
    
    
    
    WspolczynnikObrotu wsp_obrotu_c = new WspolczynnikObrotu(); 
    
    Appearance object_ap = new Appearance();
    Appearance floor_ap = new Appearance();
    
    float kat1 = 0.0f;      // Kąt początkowy
    float kat2 = 0.0f;      //Dla wszystkich
    float v_obrotu = 0.005f;  //Prędkość obrotu
    float v_pionowe = 0.005f;  //Prędkość elementu ruchomego
    float w_gore = -0.005f, w_dol = 0.0f;
    float a=0f, b=0.005f;
    float object_size = 0.1f;
  
    
    boolean hang_object = false;
    float mnoznik = 0.001f;
            
    
    JButton main_left = new JButton("<<<<<");
    JButton main_right = new JButton(">>>>>");
    JButton child_left = new JButton("<<<<<");
    JButton child_right = new JButton(">>>>>");
    
    
    javax.swing.JTextField wsp_obrotu = new javax.swing.JTextField("1");
    JTextArea wsp_obrotu_info = new JTextArea(" Podaj \n współczynnik\n obrotu (1 - 30):");
    
    Animacja animuj = new Animacja();
        
        
    Robot(){
        //OGÓLNIE KOD WZIĘTY Z LABORKI ////////////////////////////////////////////////////////////////////////////////////////////////
          super("SCARA");
          
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        
        
        //DODANIE PANELU AKCJI-------------------------------------------------------------------------------------        
        JPanel panelOfSettings = new JPanel();
        panelOfSettings.setLayout(new GridLayout(3, 2, 0, 0));
        panelOfSettings.setBounds(0, 0, 200, 170);
        
        main_left.addActionListener(this);
        main_right.addActionListener(this);
        child_left.addActionListener(this);
        child_right.addActionListener(this);
        
        panelOfSettings.add(main_left);
        panelOfSettings.add(main_right);
        panelOfSettings.add(child_left);
        panelOfSettings.add(child_right);
        panelOfSettings.add(wsp_obrotu_info);
        panelOfSettings.add(wsp_obrotu);
        wsp_obrotu_info.setEditable(false);
        wsp_obrotu_info.setBackground(new Color(1, 1, 1, 0));
        wsp_obrotu_info.setLineWrap(true);
        wsp_obrotu_info.setAlignmentY(SwingConstants.CENTER);
        
        wsp_obrotu.setMaximumSize(new Dimension(2, 2));
        wsp_obrotu.setPreferredSize(new Dimension(2, 2));
        
        wsp_obrotu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                wsp_obrotuMouseClicked(evt);
            }
        });
    
        
        
        
        

          add(panelOfSettings);
         //------------------------------------------------------------------------------------------------------
        canvas3D.setPreferredSize(new Dimension(900,700));          //ROZMIAR OKNA
        
        add(canvas3D);
        pack();
        setVisible(true);

        BranchGroup scena = utworzScene();
	    scena.compile();

        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
        simpleU.getViewingPlatform().setNominalViewingTransform();
        
        przesuniecie_obserwatora.set(p_obserwatora);

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        simpleU.addBranchGraph(scena);
        timer.scheduleAtFixedRate(new Movement(), 5, 5);
        
        
        OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ROTATE);      // mouse functionality
        orbit.setSchedulingBounds(new BoundingSphere());
        simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);
        
        
        canvas3D.addKeyListener(this);
        add(BorderLayout.CENTER, canvas3D);
      

        

    }
    BranchGroup utworzScene(){
        Point3d punkcik = new Point3d(0,0,0);  
        BranchGroup wezel_scena = new BranchGroup();
        
        BoundingSphere bounds = new BoundingSphere(punkcik,1000);
        wezel_scena.setBounds(bounds);
      //ŚWIATŁO////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      
      //AmbientLight lightA = new AmbientLight();
      //lightA.setInfluencingBounds(bounds);
      //wezel_temp.addChild(lightA);

      DirectionalLight lightD = new DirectionalLight();
      lightD.setInfluencingBounds(bounds);
      lightD.setDirection(new Vector3f(-5.70f, 0.3f, -2.0f));
      lightD.setColor(new Color3f(1.0f, 1.0f, 1.0f));
      wezel_scena.addChild(lightD);
      
      DirectionalLight lightD2 = new DirectionalLight();
      lightD2.setInfluencingBounds(bounds);
      lightD2.setDirection(new Vector3f(0.0f,0.0f, -20.0f));
      lightD2.setColor(new Color3f(1.0f, 0.0f, 1.0f));
      wezel_scena.addChild(lightD2);
        
        
      
      
      
              //PODŁOGA
              
              
              
        TextureLoader loader = new TextureLoader("obrazki/steel.jpg",this);
        ImageComponent2D image = loader.getImage();

        Texture2D floor = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
        floor.setImage(0, image);
        floor.setBoundaryModeS(Texture.WRAP);
        floor.setBoundaryModeT(Texture.WRAP);
        floor_ap.setTexture(floor);
        floor_ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        
        com.sun.j3d.utils.geometry.Box podloga = new com.sun.j3d.utils.geometry.Box(2.0f,0.001f,2.0f,com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS,floor_ap);
        Transform3D p_floor = new Transform3D();
        p_floor.set(new Vector3f(0.0f,-0.3f,0.0f));
        
        TransformGroup tg_floor = new TransformGroup(p_floor);
        tg_floor.addChild(podloga);
        wezel_temp.addChild(tg_floor);
        
      
      
        //Podstawa
        Material mat_noga_robota = new Material(new Color3f(0.75f, 0.75f, 0.75f), new Color3f(0.3f, 0.2f, 0.3f), new Color3f(0.5f, 0.5f, 0.5f), new Color3f(0.3f, 0.3f, 0.3f), 128.0f);
        Appearance wyglad_noga_robota = new Appearance();
        wyglad_noga_robota.setMaterial(mat_noga_robota);
        
        wezel_temp.addChild(noga_robota_tg);
        Cylinder noga_robota = new Cylinder(0.1f, 0.39f, 1, 1000, 1000, wyglad_noga_robota);
        noga_robota_tg.addChild(noga_robota);
        p_noga_robota.set(new Vector3f(0.0f, 0.2f, 0.0f));
        noga_robota_tg.setTransform(p_noga_robota);
        
        //Stół
        
        wezel_temp.addChild(stolik_tg);
        
        
        
        
         loader = new TextureLoader("obrazki/drewno.jpg",this);
         image = loader.getImage();

        Texture2D murek = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
        murek.setImage(0, image);
        murek.setBoundaryModeS(Texture.WRAP);
        murek.setBoundaryModeT(Texture.WRAP);
        Appearance wyglad_blat = new Appearance();
        wyglad_blat.setTexture(murek);
        
        
        
        
        
        float blat_x, blat_y, blat_z;
        blat_x = 2.0f;
        blat_y = 0.03f;
        blat_z = 0.7f;
           com.sun.j3d.utils.geometry.Box blat = new com.sun.j3d.utils.geometry.Box(blat_x, blat_y, blat_z, com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS|com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wyglad_blat);
        
        //blat_x, blat_y, blat_z, wyglad_noga_robota
        
        Transform3D p_blat = new Transform3D();
        p_blat.set(new Vector3f(0.0f, blat_y, 0.01f));
        TransformGroup blat_tg = new TransformGroup(p_blat);
        blat_tg.addChild(blat);
        stolik_tg.addChild(blat_tg);
        
        
        
        
        com.sun.j3d.utils.geometry.Box noga_1 = new com.sun.j3d.utils.geometry.Box(blat_x*0.1f, 5*blat_y, blat_z*0.1f,  com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS|com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wyglad_blat);
        Transform3D p_noga_1 = new Transform3D();
        p_noga_1.set(new Vector3f(blat_x*0.8f, -5f*blat_y, blat_z*0.8f));
        TransformGroup noga_1_tg = new TransformGroup(p_noga_1);
        noga_1_tg.addChild(noga_1);
        stolik_tg.addChild(noga_1_tg);
        
        
        com.sun.j3d.utils.geometry.Box noga_2 = new com.sun.j3d.utils.geometry.Box(blat_x*0.1f, 5*blat_y, blat_z*0.1f,  com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS|com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wyglad_blat);
        Transform3D p_noga_2 = new Transform3D();
        p_noga_2.set(new Vector3f(blat_x*0.8f, -5f*blat_y, -blat_z*0.8f));
        TransformGroup noga_2_tg = new TransformGroup(p_noga_2);
        noga_2_tg.addChild(noga_2);
        stolik_tg.addChild(noga_2_tg);
        
        
        com.sun.j3d.utils.geometry.Box noga_3 = new com.sun.j3d.utils.geometry.Box(blat_x*0.1f, 5*blat_y, blat_z*0.1f,  com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS|com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wyglad_blat);
        Transform3D p_noga_3 = new Transform3D();
        p_noga_3.set(new Vector3f(-blat_x*0.8f, -5f*blat_y, blat_z*0.8f));
        TransformGroup noga_3_tg = new TransformGroup(p_noga_3);
        noga_3_tg.addChild(noga_3);
        stolik_tg.addChild(noga_3_tg);
        
        
        com.sun.j3d.utils.geometry.Box noga_4 = new com.sun.j3d.utils.geometry.Box(blat_x*0.1f, 5*blat_y, blat_z*0.1f,  com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS|com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wyglad_blat);
        Transform3D p_noga_4 = new Transform3D();
        p_noga_4.set(new Vector3f(-blat_x*0.8f, -5f*blat_y, -blat_z*0.8f));
        TransformGroup noga_4_tg = new TransformGroup(p_noga_4);
        noga_4_tg.addChild(noga_4);
        stolik_tg.addChild(noga_4_tg);
        
        
        
        
        //Pierwszy element ramienia
        wezel_temp.addChild(matka_ramie_1_tg_mysz);
        
        matka_ramie_1_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
        
        Material mat_ramie_1 = new Material(new Color3f(0.2f, 0.3f, 0.1f), new Color3f(0.57f, 0.57f, 0.57f), new Color3f(0.4f, 0.9f, 0.1f), new Color3f(0.3f, 0.9f, 0.1f), 50.0f);
        Appearance wyglad_ramie_1 = new Appearance();
        wyglad_ramie_1.setMaterial(mat_ramie_1);
        
        Material mat_ramie_2 = new Material(new Color3f(0.1f, 0.2f, 0.3f), new Color3f(0.0f, 0.7f, 0.1f), new Color3f(0.1f, 0.4f, 0.9f), new Color3f(0.1f, 0.3f, 0.f), 50.0f);
        Appearance wyglad_ramie_2 = new Appearance();     //materiał, wygląd
        wyglad_ramie_2.setMaterial(mat_ramie_2);
        
        com.sun.j3d.utils.geometry.Box ramie_1 = new com.sun.j3d.utils.geometry.Box(0.2f, 0.03f, 0.1f, wyglad_ramie_1);
        p_ramie_1.set(new Vector3f(0.2f, 0.4f, 0.0f));
        ramie_1_tg.setTransform(p_ramie_1);
        matka_ramie_1_tg_mysz.addChild(matka_ramie_1_tg);
        matka_ramie_1_tg.addChild(ramie_1_tg);
        ramie_1_tg.addChild(ramie_1);
        
        
        Cylinder ramie_1_walec_1 = new Cylinder(0.1f, 0.06f, 1, 1000, 1, wyglad_ramie_1);
        p_ramie_1_walec_1.set(new Vector3f(0f, 0.2f, 0f));
        ramie_1_walec_1_tg.setTransform(p_ramie_1_walec_1);
        ramie_1_walec_1_tg.addChild(ramie_1_walec_1);
        noga_robota_tg.addChild(ramie_1_walec_1_tg);
        
        
        Cylinder ramie_1_walec_2 = new Cylinder(0.1f, 0.06f, 1, 1000, 1, wyglad_ramie_1);
        p_ramie_1_walec_2.set(new Vector3f(0.2f, 0f, 0f));
        ramie_1_walec_2_tg.setTransform(p_ramie_1_walec_2);
        ramie_1_walec_2_tg.addChild(ramie_1_walec_2);
        ramie_1_tg.addChild(ramie_1_walec_2_tg);
        
        
        
        //Drugi element ramienia

        matka_ramie_2_tg_mysz.addChild(matka_ramie_2_tg);
        
        p_matka_ramie_2_mysz.set(new Vector3f(0.2f, 0.06f, 0.0f));
        matka_ramie_2_tg_mysz.setTransform(p_matka_ramie_2_mysz);
        
        
        ramie_1_tg.addChild(matka_ramie_2_tg_mysz);
        //p_matka_ramie_2.set(new Vector3f(2f, 0.6f, 0.0f));
        //matka_ramie_2_tg.setTransform(p_matka_ramie_2);
        
        matka_ramie_2_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        
        com.sun.j3d.utils.geometry.Box ramie_2 = new com.sun.j3d.utils.geometry.Box(0.1f, 0.03f, 0.2f, wyglad_ramie_2);
        p_ramie_2.set(new Vector3f(0.0f, 0.0f, 0.2f));
        ramie_2_tg.setTransform(p_ramie_2);
        matka_ramie_2_tg.addChild(ramie_2_tg);
        ramie_2_tg.addChild(ramie_2);
        
        
        p_ramie_2_walec_1.set(new Vector3f(0f, 0f, -0.2f));
        Cylinder ramie_2_walec_1 = new Cylinder(0.1f, 0.06f, 1, 1000, 1, wyglad_ramie_2);
        ramie_2_walec_1_tg.setTransform(p_ramie_2_walec_1);
        ramie_2_tg.addChild(ramie_2_walec_1_tg);
        ramie_2_walec_1_tg.addChild(ramie_2_walec_1);
        
        Cylinder ramie_2_walec_2 = new Cylinder(0.1f, 0.06f, 1, 1000, 1, wyglad_ramie_2);
        p_ramie_2_walec_2.set(new Vector3f(0f, 0f, 0.2f));
        ramie_2_walec_2_tg.setTransform(p_ramie_2_walec_2);
        ramie_2_walec_2_tg.addChild(ramie_2_walec_2);
        ramie_2_tg.addChild(ramie_2_walec_2_tg);
        
        
        
        //Łączenie ramion
        Cylinder polaczenie_1_2_c = new Cylinder(0.023f, 0.07f, wyglad_noga_robota);
        Sphere polaczenie_1_2_s = new Sphere(0.023f, 1, 100, wyglad_noga_robota);
        Transform3D p_polaczenie_1_2_s = new Transform3D();
        p_polaczenie_1_2_s.set(new Vector3f(0.0f, 0.035f, 0.0f));
        TransformGroup polaczenie_1_2_s_tg = new TransformGroup(p_polaczenie_1_2_s);
        polaczenie_1_2_s_tg.addChild(polaczenie_1_2_s);
        matka_ramie_2_tg.addChild(polaczenie_1_2_s_tg);
        matka_ramie_2_tg.addChild(polaczenie_1_2_c);
        
        
        //Pionowy suwak
        
        
        Appearance wyglad_pionowy = new Appearance();
        wyglad_pionowy.setMaterial(mat_ramie_1);
        
        
        pionowy_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        Cylinder pionowy = new Cylinder(0.01f, 0.4f, 1, 100, 100, wyglad_pionowy);
        ramie_2_tg.addChild(pionowy_tg);
        pionowy_tg.addChild(pionowy);
        p_pionowy.set(new Vector3f(0.0f, 0.1f, (ramie_2.getZdimension()) ));
        pionowy_tg.setTransform(p_pionowy);
        
        
     
        
        
        //OBIEKT DO PODNOSZENIA/////////////////////////////////////////////////////////////////////////////////////////
        loader = new TextureLoader("obrazki/crate.png",this);
        image = loader.getImage();

        Texture2D skrzynia = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
        skrzynia.setImage(0, image);
        skrzynia.setBoundaryModeS(Texture.WRAP);
        skrzynia.setBoundaryModeT(Texture.WRAP);
        object_ap.setTexture(skrzynia);
        object_ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
                
        com.sun.j3d.utils.geometry.Box objekt = new com.sun.j3d.utils.geometry.Box(object_size,object_size,object_size,com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS,object_ap);
        
        
        Transform3D p_obiektu = new Transform3D();
        p_obiektu.set(new Vector3f(0.3f,0.15f,0.3f));
        tg_obiektu.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_obiektu.setTransform(p_obiektu);
        tg_obiektu.addChild(objekt);
        wezel_temp.addChild(tg_obiektu);

        
        
        
        
        
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
      JButton bt = (JButton)e.getSource();
       if(bt == main_left){ 
           wsp_obrotu.setText(String.valueOf(wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText())));
           for (int i = 0; i <2*wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText()); i++)
           {obrotLewoMain(v_obrotu*0.5f);
           animuj.Animacja();
           }
       }
       else if(bt == main_right){
           wsp_obrotu.setText(String.valueOf(wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText())));
           for (int i = 0; i <2*wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText()); i++)
           {obrotPrawoMain(0.5f*v_obrotu);
           animuj.Animacja();
           }     
       }
       else if(bt == child_right){
           wsp_obrotu.setText(String.valueOf(wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText())));
           for (int i = 0; i <2*wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText()); i++)
           {obrotPrawoSecond(0.5f*v_obrotu);
           animuj.Animacja();
           }
         
       }
       else if(bt == child_left){
           wsp_obrotu.setText(String.valueOf(wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText())));
           for (int i = 0; i <2*wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText()); i++)
           {obrotLewoSecond(0.5f*v_obrotu);
           animuj.Animacja();
           }  
       }
       
         
   
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
            case KeyEvent.VK_SPACE  :     hang_object =! hang_object;        break;
            case KeyEvent.VK_CONTROL  :          break;
            
        }
 
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        {
        
        switch(e.getKeyCode())                                                                          
        {

        }
        
        v_obrotu = 0.005f;
        
        }
    }
    
   
    
   
    
    public void obrotLewoMain(float krok){
      kat1 += krok;    
      p_ramie_1.rotY(kat1);
              
      matka_ramie_1_tg.setTransform(p_ramie_1); 
    }
    private void obrotPrawoMain(float krok){
      kat1 -= krok;    
      p_ramie_1.rotY(kat1);
      //p_ramie_1.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
      matka_ramie_1_tg.setTransform(p_ramie_1); 
    }
    private void obrotLewoSecond(float krok){
      if(kat2 < 4.2f)
      kat2 += krok;    
      p_ramie_2.rotY(kat2);
      matka_ramie_2_tg.setTransform(p_ramie_2); 
    }
    private void obrotPrawoSecond(float krok){
        if(kat2 > -0.87f)
      kat2 -= krok;    
      p_ramie_2.rotY(kat2);
      matka_ramie_2_tg.setTransform(p_ramie_2);
    }
    private void gora(float krok){   
      if(w_gore<-0.01){
      w_gore = w_gore+krok; 
      p_pionowy.setTranslation(new Vector3f(0.0f, 0.1f+w_gore, 0.2f));
      pionowy_tg.setTransform(p_pionowy);   
      }
    }
      
    private void dol(float krok){
      if(w_gore>-0.145){
        if(!Collision()){
            w_gore = w_gore- krok; 
            p_pionowy.setTranslation(new Vector3f(0.0f, 0.1f+w_gore, 0.2f));
            pionowy_tg.setTransform(p_pionowy); 
        }
      }
      
    }
    

    private class Movement extends TimerTask{
        @Override
        public void run() {
            temp_transform.set(new Vector3f(0.0f,0.0f,0.0f));
            temp_transform2.rotY(kat1);
            temp_transform2.mul(temp_transform);
            matka_ramie_1_tg.setTransform(temp_transform2);
            
            
            temp_transform.set(new Vector3f(0.0f,0.0f,0.0f));
            temp_transform2.rotY(kat2);
            temp_transform2.mul(temp_transform);
            matka_ramie_2_tg.setTransform(temp_transform2);
            
            
            temp_transform.set(p_pionowy);
            temp_transform2.rotY(0.0f);
            temp_transform2.mul(temp_transform);
            pionowy_tg.setTransform(temp_transform2);
            
            
            pionowy_tg.getLocalToVworld(temp_transform);
            Transform3D positionFix = new Transform3D();            
            positionFix.set(new Vector3f(0.0f,w_gore-0.18f,0.2f));
            temp_transform.mul(positionFix);
            
                    
           if(hang_object)
              tg_obiektu.setTransform(temp_transform);
            
        }
    }

       private void wsp_obrotuMouseClicked(java.awt.event.MouseEvent evt){
        wsp_obrotu.setText("");
        } 
      private boolean Collision(){
          Vector3f tempV1 = new Vector3f();
          Vector3f tempV2 = new Vector3f();
          Transform3D temp1 = new Transform3D();
          Transform3D temp2 = new Transform3D();
          Vector3f tempV3 = new Vector3f();
          Transform3D temp3 = new Transform3D();
          
          tg_obiektu.getTransform(temp1);
          temp1.get(tempV1);
          System.out.println("OBIEKT:: "+tempV1.x);
          
          pionowy_tg.getTransform(temp2);
          temp2.get(tempV2);
          System.out.println("SUWAK::  "+(tempV2.x));
          
          
          
          
          if((tempV2.y+0.155f)<tempV1.y)
              return true;
          else
              return false;
          
          
          
      }
}



//Todama