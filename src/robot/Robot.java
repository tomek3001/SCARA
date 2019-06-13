package robot;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.swing.*;
import java.util.List;
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
import java.util.Stack;
import java.util.Enumeration;


//test automatycznej synchronizaji

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
    Transform3D temp_transform3 = new Transform3D();
    Transform3D zmniejszenie_calosci = new Transform3D();
     
    
    Cylinder pionowy;
    
    
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
    
    
    CollisionDetector myColDet = new CollisionDetector();
    
    Stack<Integer> nagrywanie = new Stack<>();
    Stack<Integer> nagrywanie_temp = new Stack<>();
                                                //ZMIENNE I PARAMETRY
    
    Vector3f p_obserwatora = new Vector3f(0.0f,0.1f,3.0f);
    Vector3f temp_vector = new Vector3f();
    
    WspolczynnikObrotu wsp_obrotu_c = new WspolczynnikObrotu(); 
    int raport_temp = 1000;
    
    float kat1_temp;
    float kat2_temp;
    float pion;
    
    Appearance object_ap = new Appearance();
    Appearance floor_ap = new Appearance();
    
    
    String last_move;
    
    float kat1 = 0.0f;      // Kąt początkowy
    float kat2 = 0.0f;      //Dla wszystkich
    float v_obrotu = 0.14f;  //Prędkość obrotu
    float v_pionowe = 0.005f;  //Prędkość elementu ruchomego
    float w_gore = -0.005f, w_dol = 0.0f;
    float a=0f, b=0.005f;
    float object_size = 0.1f;
    float object_help_size = 0.03886f;
  
    public boolean record = false;
    public boolean collision = false;
    public boolean koniec_ruchu = false;
    public boolean isColliding = false;
    boolean hang_object = false;
    float mnoznik = 0.001f;
    JCheckBox czy_nagrywamy = new JCheckBox("Nagrywanie");
    
    
    JButton main_left = new JButton("Człon 1 ◄");
    JButton main_right = new JButton("Człon 1 ►");
    JButton child_left = new JButton("Człon 2 ◄");
    JButton child_right = new JButton("Człon 2 ►");
    JButton palka_gora = new JButton("▲");
    JButton palka_dol = new JButton("▼");
    JButton obrot = new JButton("OBRÓĆ");
    JButton cofanie = new JButton("ODTWÓRZ");
    
    javax.swing.JTextField wsp_obrotu = new javax.swing.JTextField("1");
    javax.swing.JTextField wsp_x = new javax.swing.JTextField("0.4");
    javax.swing.JTextField wsp_y = new javax.swing.JTextField("0.0");
    javax.swing.JTextField wsp_z = new javax.swing.JTextField("-0.4");
    //javax.swing.JTextField wsp_z = new javax.swing.JTextField("-0.4");
    JTextArea wsp_obrotu_info = new JTextArea(" Podaj \n współczynnik\n obrotu (1 - 30):");
    JTextArea wsp_x_info = new JTextArea(" Podaj \n współrzędną\n x:(0 - 0.8)");
    JTextArea wsp_z_info = new JTextArea(" Podaj \n współrzędną\n y (0 - 0.8):");
    JTextArea wsp_y_info = new JTextArea(" Podaj \n współrzędną\n z(od 1 do 145):");
    
    BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0),1);
    
    
    Animacja animuj = new Animacja();
        
        
    Robot(){
        //OGÓLNIE KOD WZIĘTY Z LABORKI /////////////////////////////////////////////////////////////////////////////////////////////////
          super("SCARA");
          
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        
        
        //DODANIE PANELU AKCJI-------------------------------------------------------------------------------------        
        JPanel panelOfSettings = new JPanel();
        panelOfSettings.setLayout(new GridLayout(9, 2, 0, 0));
        panelOfSettings.setBounds(10, 10, 200, 450);
        
        main_left.addActionListener(this);
        main_right.addActionListener(this);
        child_left.addActionListener(this);
        child_right.addActionListener(this);
        palka_dol.addActionListener(this);
        palka_gora.addActionListener(this);
        obrot.addActionListener(this);
        cofanie.addActionListener(this);
        czy_nagrywamy.addActionListener(new ActionListener() {                  //Przydaloby sie upiekrzyc to
              @Override
              public void actionPerformed(ActionEvent e) {
               if(czy_nagrywamy.isSelected()){
           record = true;
       }
       else if(!czy_nagrywamy.isSelected()){
           record = false;
           nagrywanie.clear();
           nagrywanie_temp.clear();
       }
              }
          });
        panelOfSettings.add(main_left);
        panelOfSettings.add(main_right);
        panelOfSettings.add(child_left);
        panelOfSettings.add(child_right);
        panelOfSettings.add(palka_gora);
        panelOfSettings.add(palka_dol);
        panelOfSettings.add(wsp_obrotu_info);
        panelOfSettings.add(wsp_obrotu);
        panelOfSettings.add(wsp_x_info);
        panelOfSettings.add(wsp_x);
        panelOfSettings.add(wsp_z_info);
        panelOfSettings.add(wsp_z);
        panelOfSettings.add(wsp_y_info);
        panelOfSettings.add(wsp_y);
        panelOfSettings.add(obrot);
        panelOfSettings.add(cofanie);
        panelOfSettings.add(czy_nagrywamy);
        
        
        wsp_obrotu_info.setEditable(false);
        wsp_obrotu_info.setBackground(new Color(1, 1, 1, 0));
        wsp_obrotu_info.setLineWrap(true);
        wsp_obrotu_info.setAlignmentY(SwingConstants.CENTER);
        
        wsp_x_info.setEditable(false);
        wsp_x_info.setBackground(new Color(1, 1, 1, 0));
        wsp_x_info.setLineWrap(true);
        wsp_x_info.setAlignmentY(SwingConstants.CENTER);
        
        wsp_z_info.setEditable(false);
        wsp_z_info.setBackground(new Color(1, 1, 1, 0));
        wsp_z_info.setLineWrap(true);
        wsp_z_info.setAlignmentY(SwingConstants.CENTER);
        
        wsp_y_info.setEditable(false);
        wsp_y_info.setBackground(new Color(1, 1, 1, 0));
        wsp_y_info.setLineWrap(true);
        wsp_y_info.setAlignmentY(SwingConstants.CENTER);
        
        
        wsp_obrotu.setMaximumSize(new Dimension(2, 2));
        wsp_obrotu.setPreferredSize(new Dimension(2, 2));
        
        wsp_obrotu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                wsp_obrotuMouseClicked(evt);
            }
        });
    
        
        wsp_x.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                wsp_xMouseClicked(evt);
            }
        });
        
        wsp_z.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                wsp_zMouseClicked(evt);
            }
        });
        
        wsp_y.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                wsp_yMouseClicked(evt);
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
        timer.scheduleAtFixedRate(new Movement(), 0 , 1);
        
        
        OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ROTATE);      // mouse functionality
        orbit.setSchedulingBounds(bounds);
        simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);
        
        
        canvas3D.addKeyListener(this);
      

        

    }
    BranchGroup utworzScene(){  
        BranchGroup wezel_scena = new BranchGroup();       
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
        Cylinder noga_robota = new Cylinder(0.0999f, 0.39f, 1, 1000, 1000, wyglad_noga_robota);
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
        //wezel_scena.addChild(blat_tg);                        ///Dsghdisfjkjsidfulfhajrifdsufhsiudfhs iudifher8rtvu28LTJFDOFIVJKDOFGVJSPOGJWESIODJ
        
        
        
        
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
        
              //Pionowy suwak
        
        
        Appearance wyglad_pionowy = new Appearance();
        wyglad_pionowy.setMaterial(mat_ramie_1);
        
        
        pionowy_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        pionowy_tg.setCapability(TransformGroup.ALLOW_COLLIDABLE_WRITE);
        
        pionowy = new Cylinder(0.01f, 0.4f, 1, 100, 100, wyglad_pionowy);
        pionowy.setCapability(TransformGroup.ALLOW_COLLIDABLE_WRITE);
        ramie_2_tg.addChild(pionowy_tg);
        pionowy_tg.addChild(pionowy);
        p_pionowy.set(new Vector3f(0.0f, 0.1f, (ramie_2.getZdimension()) ));
        pionowy_tg.setTransform(p_pionowy);
        
        
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
        
                
        //OBIEKT DO PODNOSZENIA/////////////////////////////////////////////////////////////////////////////////////////
        loader = new TextureLoader("obrazki/crate.png",this);
        image = loader.getImage();

        Texture2D skrzynia = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
        skrzynia.setImage(0, image);
        //skrzynia.setBoundaryModeS(Texture.WRAP);
        //skrzynia.setBoundaryModeT(Texture.WRAP);
        object_ap.setTexture(skrzynia);
        object_ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
                
        com.sun.j3d.utils.geometry.Box objekt = new com.sun.j3d.utils.geometry.Box(object_size,object_size,object_size,com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS|com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS_INWARD,object_ap);
        //System.out.println(objekt.getBounds());
        
        Transform3D objekt_bounds_trans = new Transform3D();
        objekt.getLocalToVworld(objekt_bounds_trans);
        Vector3d objekt_wsp = new Vector3d();
        objekt_bounds_trans.get(objekt_wsp);
        System.out.println("Współrzędne objektu: " + objekt_wsp.getX() + " " + objekt_wsp.getY() + " " +objekt_wsp.getZ());
        System.out.println("Zakres kolizji objektu: " + objekt.getCollisionBounds() + "\nZakres objektu: " + objekt.getBounds());
        BoundingBox objekt_bounds = new BoundingBox(new Point3d(- object_size, 
                                                                - object_size, 
                                                                - object_size
                                                                ), 
                                                    new Point3d(+ object_size, 
                                                                + object_size+0.001, 
                                                                + object_size
                                                                )
                                                    );
        objekt_bounds.getLower(new Point3d());
        objekt_bounds.getUpper(new Point3d());
        objekt.setBounds(objekt_bounds);
        objekt.setCollisionBounds(objekt_bounds);
        System.out.println("Zakres kolizji objektu: " + objekt.getCollisionBounds() + "\nZakres objektu: " + objekt.getBounds());
        
        
        Transform3D p_obiektu = new Transform3D();
        p_obiektu.set(new Vector3f(-0.2f,0.16f,-0.3f));
        tg_obiektu.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_obiektu.setTransform(p_obiektu);
        tg_obiektu.addChild(objekt);
        wezel_temp.addChild(tg_obiektu);
        
        //myColDet.getTheBounds(objekt_bounds);
        myColDet.getTheShape(objekt);
        myColDet.setSchedulingBounds(bounds);
        
        wezel_scena.addChild(myColDet);


     ////////////////////////////////////////////////////////////////////////////////
        
        blat.setUserData(new String("blat"));
        lightD.setUserData(new String("swiatlo1"));
        lightD2.setUserData(new String("swiatlo2"));
        myColDet.setUserData(new String("kolizja"));
        noga_robota.setUserData(new String("Noga robota"));
        objekt.setUserData(new String("box"));
        podloga.setUserData("podloga");
        ramie_1.setUserData("ramie_1");
        murek.setUserData("murek");
        skrzynia.setUserData("teksutra");
       
        
        
        
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
           animuj.Animacja(1);
           }
       }
       else if(bt == main_right){
           wsp_obrotu.setText(String.valueOf(wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText())));
           for (int i = 0; i <2*wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText()); i++)
           {obrotPrawoMain(0.5f*v_obrotu);
           animuj.Animacja(1);
           }     
       }
       else if(bt == child_right){
           wsp_obrotu.setText(String.valueOf(wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText())));
           for (int i = 0; i <2*wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText()); i++)
           {obrotPrawoSecond(0.5f*v_obrotu);
           animuj.Animacja(1);
           }
         
       }
       else if(bt == child_left){
           wsp_obrotu.setText(String.valueOf(wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText())));
           for (int i = 0; i <2*wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText()); i++)
           {obrotLewoSecond(0.5f*v_obrotu);
           animuj.Animacja(1);
           }  
       }
       else if(bt == palka_gora){
           wsp_obrotu.setText(String.valueOf(wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText())));
           for (int i = 0; i <5*wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText()); i++)
           {gora(v_pionowe*0.1f);
           animuj.Animacja(1);
           }  
       }
       else if(bt == palka_dol){
           wsp_obrotu.setText(String.valueOf(wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText())));
           for (int i = 0; i <5*wsp_obrotu_c.wsp_obrotu(wsp_obrotu.getText()); i++)
           {dol(v_pionowe*0.1f);
           animuj.Animacja(1);
           }   
       }
       else if(bt == obrot){
            if(myColDet.isColliding()){System.out.println("Nie można ruszyć - KOLIZOZZZAAAAAAAAA!!!");}
            else{
            //odwrotna kinematyka
            if(Math.sqrt( Math.pow( Double.parseDouble(wsp_x.getText()) , 2 ) + Math.pow( Double.parseDouble(wsp_z.getText()),2 ) ) <= 0.8){
            kat2_temp = (float)(Math.asin(1)-Math.acos( ( Math.pow( Double.parseDouble(wsp_x.getText()) , 2 ) + Math.pow( Double.parseDouble(wsp_z.getText()) , 2 ) - 0.32 ) / ( 2 * 0.4 * 0.4 ) ));
            kat1_temp = (float)(-Math.atan2(Double.parseDouble(wsp_z.getText()), -Double.parseDouble(wsp_x.getText())) - 
                    Math.acos( ( Math.pow( Double.parseDouble(wsp_x.getText()) , 2 ) + Math.pow( Double.parseDouble(wsp_z.getText()) , 2 )  ) / ( -2 * 0.4 * Math.sqrt( Math.pow( Double.parseDouble(wsp_x.getText()) , 2 ) + Math.pow( Double.parseDouble(wsp_z.getText()),2 ) ) ) )
                    );
            
            //normalizacja kąta
            
           if(kat1_temp>2*Math.asin(1))
               kat1_temp -= 2*(float)(2*Math.asin(1));
           if(kat1_temp<-2*Math.asin(1))
               kat1_temp += 2*(float)(2*Math.asin(1));
            
           System.out.println("\nAktualna wartość kąta 1: " + kat1 + "\nPożądana wartość kąta 1: " + kat1_temp );
           System.out.println("\nAktualna wartość kąta 2: " + kat2 + "\nPożądana wartość kąta 2: " + kat2_temp );

                                
            
            
            //Wykonanie obrotu ramienia dodatkowego
            
            if(Math.sqrt(kat2*kat2) < Math.sqrt(kat2_temp*kat2_temp)){
            {
                if(kat2<kat2_temp)
                    
            while (Math.abs(kat2_temp-kat2) > 0.05f*v_obrotu )
                    {                    
                        System.out.println("Wykonanie pętli 1");
                        obrotLewoSecond(0.05f*v_obrotu);
                        animuj.Animacja(1);
                    }
                else if(kat2>kat2_temp)
                    
            while (Math.abs(kat2_temp-kat2) > 0.05f*v_obrotu && !koniec_ruchu)
                    {                    
                        System.out.println("Wykonanie pętli 2");
                        obrotPrawoSecond(0.05f*v_obrotu);
                        animuj.Animacja(1);
                    }
                if(koniec_ruchu)
                    koniec_ruchu=!koniec_ruchu;
            }
            }
            else if(Math.sqrt(kat2*kat2) > Math.sqrt(kat2_temp*kat2_temp)){
            
            {

                if(kat2<kat2_temp)
                    while (Math.abs(kat2_temp-kat2) > 0.05f*v_obrotu )
                    {                    
                        System.out.println("Wykonanie pętli 3");
                        obrotLewoSecond(0.05f*v_obrotu);
                    }
                else if(kat2>kat2_temp)
                    while (Math.abs(kat2_temp-kat2) > 0.05f*v_obrotu )
                    {                    
                        System.out.println("Wykonanie pętli 4");
                        obrotPrawoSecond(0.05f*v_obrotu);
                        animuj.Animacja(1);
                    }
            }    
            }
            
            //Wykonanie obrotu ramienia głównego
            
            if(Math.sqrt(kat1*kat1) < Math.sqrt(kat1_temp*kat1_temp)){
            {

                if(kat1<kat1_temp)
                        while (Math.abs(kat1_temp-kat1) > 0.05f*v_obrotu)
                    {                    
                        System.out.println("Wykonanie pętli 5" + "\nAktualna wartość kąta 1: " + kat1 + "\nPożądana wartość kąta 1: " + kat1_temp );
                        obrotLewoMain(0.05f*v_obrotu);
                        animuj.Animacja(1);
                    }
                else if(kat1>kat1_temp)
                        while (Math.abs(kat1_temp-kat1) > 0.05f*v_obrotu)
                    {                    
                        
                        System.out.println("Wykonanie pętli 6" + "\nAktualna wartość kąta 1: " + kat1 + "\nPożądana wartość kąta 1: " + kat1_temp );
                        obrotPrawoMain(0.05f*v_obrotu);
                        animuj.Animacja(1);
                    }
            }
            }
            else if(Math.sqrt(kat1*kat1) > Math.sqrt(kat1_temp*kat1_temp)){
            {

                if(kat1<kat1_temp)
                        while (Math.abs(kat1_temp-kat1) > 0.05f*v_obrotu) 
                    {                   
                        System.out.println("Wykonanie pętli 7" + "\nAktualna wartość kąta 1: " + kat1 + "\nPożądana wartość kąta 1: " + kat1_temp );
                        obrotLewoMain(0.05f*v_obrotu);
                        animuj.Animacja(1);
                    }
                else if(kat1>kat1_temp)
                        while (Math.abs(kat1_temp-kat1) > 0.05f*v_obrotu)
                    {                    
                        System.out.println("Wykonanie pętli 8" + "\nAktualna wartość kąta 1: " + kat1 + "\nPożądana wartość kąta 1: " + kat1_temp );
                        obrotPrawoMain(0.05f*v_obrotu);
                        animuj.Animacja(1);
                    }
            }    
            }
            
            }
            else if ((0.8*0.8-Double.parseDouble(wsp_x.getText())) < 0 && (0.8*0.8-Double.parseDouble(wsp_z.getText())) > 0)
                System.out.println("Podaj współrzędne tak, aby pierwiastek sumy ich kwadratów nie przekraczał 0.8. Dla danego 'y' maksymalny x wynosi: " + Math.sqrt(0.8*0.8-Math.pow(Double.parseDouble(wsp_z.getText()),2)) + ".");
            else if ((0.8*0.8-Double.parseDouble(wsp_x.getText())) > 0 && (0.8*0.8-Double.parseDouble(wsp_z.getText())) < 0)
                System.out.println("Podaj współrzędne tak, aby pierwiastek sumy ich kwadratów nie przekraczał 0.8. Dla danego 'x' maksymalny y wynosi " + Math.sqrt(0.8*0.8-Math.pow(Double.parseDouble(wsp_x.getText()),2)) + ".");
            else System.out.println("Podaj współrzędne tak, aby pierwiastek sumy ich kwadratów nie przekraczał 0.8.");

            if(Double.parseDouble(wsp_y.getText())>=1 && Double.parseDouble(wsp_y.getText())<=145){              
                if(w_gore > Float.parseFloat(wsp_y.getText())/1000.0f)
                    while(w_gore > Float.parseFloat(wsp_y.getText())/1000.0f){
                        w_gore = w_gore-0.0001f;
                        p_pionowy.setTranslation(new Vector3f(0.0f, 0.1f-w_gore, 0.2f));
                        pionowy_tg.setTransform(p_pionowy);       
                        System.out.println(w_gore);
                        animuj.Animacja(1);
                    }
                else if(w_gore < Float.parseFloat(wsp_y.getText())/1000.0f)
                    while(w_gore < Float.parseFloat(wsp_y.getText())/1000.0f){
                        w_gore = w_gore+0.0001f;
                        p_pionowy.setTranslation(new Vector3f(0.0f, 0.1f-w_gore, 0.2f));
                        pionowy_tg.setTransform(p_pionowy);       
                        System.out.println(w_gore);  
                        animuj.Animacja(1);
                    }               
            }
           
           
           
            //Wykonanie obrotu ramienia dodatkowego
            
            if(Math.sqrt(kat2*kat2) < Math.sqrt(kat2_temp*kat2_temp)){
            {
                if(kat2<kat2_temp)
                    
            while (Math.abs(kat2_temp-kat2) > 0.05f*v_obrotu )
                    {                    
                        System.out.println("Wykonanie pętli 1" + "\nAktualna wartość kąta 2: " + kat2 + "\nPożądana wartość kąta 2: " + kat2_temp );
                        obrotLewoSecond(0.05f*v_obrotu);
                        animuj.Animacja(1);
    }
                else if(kat2>kat2_temp)
                    
            while (Math.abs(kat2_temp-kat2) > 0.05f*v_obrotu && !koniec_ruchu)
                    {                    
                        System.out.println("Wykonanie pętli 2" + "\nAktualna wartość kąta 2: " + kat2 + "\nPożądana wartość kąta 2: " + kat2_temp );
                        obrotPrawoSecond(0.05f*v_obrotu);
                        animuj.Animacja(1);
       }
                if(koniec_ruchu)
                    koniec_ruchu=!koniec_ruchu;
    }
            }
            else if(Math.sqrt(kat2*kat2) > Math.sqrt(kat2_temp*kat2_temp)){
            
            {

                if(kat2<kat2_temp)
                    while (Math.abs(kat2_temp-kat2) > 0.05f*v_obrotu )
                    {                    
                        System.out.println("Wykonanie pętli 3" + "\nAktualna wartość kąta 2: " + kat2 + "\nPożądana wartość kąta 2: " + kat2_temp );
                        obrotLewoSecond(0.05f*v_obrotu);
                    }
                else if(kat2>kat2_temp)
                    while (Math.abs(kat2_temp-kat2) > 0.05f*v_obrotu )
                    {                    
                        System.out.println("Wykonanie pętli 4" + "\nAktualna wartość kąta 2: " + kat2 + "\nPożądana wartość kąta 2: " + kat2_temp );
                        obrotPrawoSecond(0.05f*v_obrotu);
                        animuj.Animacja(1);
                    }
            }    
            }
            

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
            case KeyEvent.VK_LEFT   :     v_obrotu = 0.005f;  obrotLewoMain(v_obrotu);      break;   
            case KeyEvent.VK_RIGHT  :     v_obrotu = 0.005f;  obrotPrawoMain(v_obrotu);     break;
            case KeyEvent.VK_D      :     v_obrotu = 0.005f;  obrotPrawoSecond(v_obrotu);   break;
            case KeyEvent.VK_A      :     v_obrotu = 0.005f;  obrotLewoSecond(v_obrotu);    break;
            case KeyEvent.VK_DOWN   :     dol(v_pionowe);    break;
            case KeyEvent.VK_UP     :     gora(v_pionowe);      break;
            case KeyEvent.VK_SPACE  :     Pickable();        break;
            
        }
 
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void Pickable(){
        if(myColDet.inCollision && w_gore <= -0.1f && !hang_object){            
            pionowy.setCollidable(false);
            hang_object =! hang_object;
            myColDet.resetCollision();
        }
        else if(hang_object){
            hang_object = !hang_object;
        }
    }
    

    @Override
    public void keyReleased(KeyEvent e) {
        {
        
        switch(e.getKeyCode())                                                                          
        {

        }
        
        v_obrotu = 0.14f;
        
        }
    }
    
   
    public void backToTheFuture(){                  //Funkcja przenosi nas do chwili w której skończyliśmy nagrywanie       
        while(!nagrywanie_temp.isEmpty()){
            nagrywanie.push(nagrywanie_temp.lastElement());
            if( nagrywanie_temp.lastElement() == 0)
                obrotLewoMain(v_obrotu); 
            else if( nagrywanie_temp.lastElement() == 1)
                obrotPrawoMain(v_obrotu); 
            else if( nagrywanie_temp.lastElement() == 2)
                obrotLewoSecond(v_obrotu);
            else if( nagrywanie_temp.lastElement() == 3)
                obrotPrawoSecond(v_obrotu);
            else if( nagrywanie_temp.lastElement() == 4)
                gora(v_pionowe);
            else if( nagrywanie_temp.lastElement() ==5)
                dol(v_pionowe);
            nagrywanie_temp.pop();
            animuj.Animacja(10);
         }
    }
    
     public void backToThePast(){                   //Funkcja przenosi na z powrotem do punktu w którym nagrywaliśmy
         int i =0;
        // System.out.println(nagrywanie.size());
        while(!nagrywanie.isEmpty()){
            i++;
            nagrywanie_temp.push(nagrywanie.lastElement());
            if( nagrywanie.lastElement() == 0)
                obrotPrawoMain(v_obrotu); 
            else if( nagrywanie.lastElement() == 1)
                obrotLewoMain(v_obrotu); 
            else if( nagrywanie.lastElement() == 2)
                obrotPrawoSecond(v_obrotu);
            else if( nagrywanie.lastElement() == 3)
                obrotLewoSecond(v_obrotu);
            else if( nagrywanie.lastElement() == 4)
                dol(v_pionowe);
            else if( nagrywanie.lastElement() ==5)
                gora(v_pionowe);
          //  System.out.println("Cofanie: "+i+". " + nagrywanie.lastElement());
            nagrywanie.pop();
            animuj.Animacja(10);
            
         }
         
    }
  
    
    public void obrotLewoMain(float krok){
       if((myColDet.inCollision && last_move.equals("ramie_1_prawo")) || !myColDet.isColliding()){
      if(myColDet.isColliding())
      kat1 += krok;    
      kat1 += krok;    
      p_ramie_1.rotY(kat1);              
              
      matka_ramie_1_tg.setTransform(p_ramie_1); 
      last_move = "ramie_1_lewo";
       }

      }
    private void obrotPrawoMain(float krok){
       if((myColDet.inCollision && last_move.equals("ramie_1_lewo")) || !myColDet.isColliding()){
      if(myColDet.isColliding())
      kat1 -= krok;    
      kat1 -= krok;      
      p_ramie_1.rotY(kat1);
      matka_ramie_1_tg.setTransform(p_ramie_1);
      last_move = "ramie_1_prawo";
      animuj.Animacja(1);
       }

    }
    private void obrotLewoSecond(float krok){
       if((myColDet.inCollision && last_move.equals("ramie_2_prawo")) || !myColDet.isColliding()){
      if( !((kat2+krok) < -Math.PI/2 + 0.52372 && (kat2+krok) > -Math.PI/2 - 0.52372) &&!collision)
      {if(myColDet.isColliding())
      kat2 += krok;    
      kat2 += krok;    
      p_ramie_2.rotY(kat2);
      matka_ramie_2_tg.setTransform(p_ramie_2);}
      else if(!nagrywanie.isEmpty())
          nagrywanie.pop();
      
      last_move = "ramie_2_lewo";
      animuj.Animacja(1);
     }
    }
    private void obrotPrawoSecond(float krok){
       if((myColDet.inCollision && last_move.equals("ramie_2_lewo")) || !myColDet.isColliding()){
      float temp = kat2;
      if(!((kat2-krok) < -Math.PI/2 + 0.52372 && (kat2-krok) > -Math.PI/2 - 0.52372) &&!collision)
      {if(myColDet.isColliding()&& !hang_object)
      kat2 -= krok;    
      kat2 -= krok;    
      p_ramie_2.rotY(kat2);
      matka_ramie_2_tg.setTransform(p_ramie_2);}
      else if(!nagrywanie.isEmpty())
          nagrywanie.pop();
      if(temp==kat2)
          koniec_ruchu=true;
      last_move = "ramie_2_prawo";
      animuj.Animacja(1);
       }
    }
    private void gora(float krok){
        if((myColDet.inCollision && last_move.equals("pionowy_dol")) || !myColDet.isColliding()){
      if(w_gore<-0.01){
      w_gore = w_gore+krok; 
      p_pionowy.setTranslation(new Vector3f(0.0f, 0.1f+w_gore, 0.2f));
      pionowy_tg.setTransform(p_pionowy);   
      }
      else if(!nagrywanie.isEmpty())nagrywanie.pop();
        }
    }
      
    private void dol(float krok){
      if(w_gore>-0.145 && !myColDet.inCollision){
        if(!collision){
            w_gore = w_gore- krok; 
            p_pionowy.setTranslation(new Vector3f(0.0f, 0.1f+w_gore, 0.2f));
            pionowy_tg.setTransform(p_pionowy); 
      }
      else if(!nagrywanie.isEmpty())
          nagrywanie.pop();
    }
      last_move = "pionowy_dol";
    
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
            
            isColliding = myColDet.isColliding();
           
            //System.out.println(isColliding);
                    
           if(hang_object)
              tg_obiektu.setTransform(temp_transform);
           if(kat1>2*Math.asin(1))
               kat1 -= 2*(float)(2*Math.asin(1));
           if(kat1<-2*Math.asin(1))
               kat1 += 2*(float)(2*Math.asin(1));
           if(kat2>2*Math.asin(1))
               kat2 -= 2*(float)(2*Math.asin(1));
           if(kat2<-2*Math.asin(1))
               kat2 += 2*(float)(2*Math.asin(1));
           raport_temp++;
           if(raport_temp>5000){
            
            try{
            raport_temp=0;
            }
            catch(Exception e){}
           }
            
            //System.out.println("Kąt 2 :" + kat2 + "    Kąt 1: " + kat1 + " 2 asin(1)" + 4*Math.asin(1));
            pionowy_tg.getLocalToVworld(temp_transform3);
            temp_transform.get(temp_vector);
            //System.out.println("x: " + temp_vector.getX() + "y: " + temp_vector.getY() + "z: " + temp_vector.getZ());
           
        }
    }

       private void wsp_obrotuMouseClicked(java.awt.event.MouseEvent evt){
        wsp_obrotu.setText("");
        } 
       
       private void wsp_xMouseClicked(java.awt.event.MouseEvent evt){
        wsp_x.setText("");
        } 
     
       private void wsp_zMouseClicked(java.awt.event.MouseEvent evt){
        wsp_z.setText("");
        } 
       private void wsp_yMouseClicked(java.awt.event.MouseEvent evt){
        wsp_y.setText("");
        } 

         
//       
//       
//       
//       
//       
//       

 class CollisionDetector extends Behavior {
  boolean inCollision = false;
  /** The separate criteria used to wake up this beahvior. */
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;
 
  /** The shape that is watched for collision. */
  protected com.sun.j3d.utils.geometry.Box collidingBox;
 
  /**
   * @param theShape
   *            Shape3D that is to be watched for collisions.
   * @param theBounds
   *            Bounds that define the active region for this behaviour
   */
  
  
com.sun.j3d.utils.geometry.Box theShape;
BoundingBox theBounds;  
  
  public CollisionDetector(com.sun.j3d.utils.geometry.Box theShape, BoundingBox theBounds) {
      inCollision = false;
      collidingBox = theShape;
      collidingBox.setCollisionBounds(theBounds);
  }  
  
   public void getTheShape(com.sun.j3d.utils.geometry.Box theShape){
      collidingBox = theShape;
 } 
 
 public void getTheBounds(BoundingBox theBounds){
      collidingBox.setCollisionBounds(theBounds);
 }
  public CollisionDetector() {
      inCollision = false;
  }
 
    public void resetCollision(){
        inCollision = false;
    }
         
  /**
   * This creates an entry, exit and movement collision criteria. These are
   * then OR'ed together, and the wake up condition set to the result.
   */
  public void initialize() {
    wEnter = new WakeupOnCollisionEntry(collidingBox);
    wExit = new WakeupOnCollisionExit(collidingBox);
    wakeupOn(wEnter);
    
      System.out.println(collidingBox.getBounds());
      System.out.println(collidingBox.getCollisionBounds());


  }
 
  /**
   * Where the work is done in this class. A message is printed out using the
   * userData of the object collided with. The wake up condition is then set
   * to the OR'ed criterion again.
   */
  public void processStimulus(Enumeration criteria) {

    inCollision = !inCollision;

    if (inCollision) {
        //System.out.println("Weszlo");
        wakeupOn(wExit);  
      }
    else {
        //System.out.println("Wyszlo");
        wakeupOn(wEnter); 
    }
    
  }
  
  private boolean isColliding() {
     return inCollision;
 }


 }
}
     
