package robot;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;

     
 class CollisionDetector extends Behavior {
  boolean inCollision = false;
  /** The separate criteria used to wake up this beahvior. */
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;
 
  /*Kształt którego kolizję chcemy wykryć. */
  protected com.sun.j3d.utils.geometry.Box collidingBox;
 
  
  
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
         
  public void initialize() {
    wEnter = new WakeupOnCollisionEntry(collidingBox);
    wExit = new WakeupOnCollisionExit(collidingBox);
    wakeupOn(wEnter);
    
     // System.out.println(collidingBox.getBounds());
     // System.out.println(collidingBox.getCollisionBounds());


  }
 
  public void processStimulus(Enumeration criteria) {

    inCollision = !inCollision;

    if (inCollision) {
        wakeupOn(wExit);  
      }
    else {
        wakeupOn(wEnter); 
    }
    
  }
  
  protected boolean isColliding() {
     return inCollision;
 }


 }