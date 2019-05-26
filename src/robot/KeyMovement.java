/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.Enumeration;;
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


public class KeyMovement extends Behavior{
 private WakeupOnAWTEvent   wakeupOne = null;
 private WakeupCriterion[]  wakeupArray = new WakeupCriterion[1];
 private WakeupCondition    wakeupCondition = null;
 
 
 TransformGroup             m_TransformGroup = null;
 
 public KeyMovement( TransformGroup tg )
 {
  m_TransformGroup = tg;
  
  wakeupOne = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
  wakeupArray[0] = wakeupOne;
  wakeupCondition = new WakeupOr(wakeupArray);
 }
 
 //Override Behavior's initialize method to set up wakeup criteria
 public void initialize()
 {
  //Establish initial wakeup criteria
  wakeupOn(wakeupCondition);
 }
 
 //Override Behavior's stimulus method to handle the event.
 public void processStimulus(Enumeration criteria)
 {
  WakeupOnAWTEvent ev;
  WakeupCriterion genericEvt;
  AWTEvent[] events;
  
  while (criteria.hasMoreElements())
  {
   genericEvt = (WakeupCriterion) criteria.nextElement();
   
   if (genericEvt instanceof WakeupOnAWTEvent)
   {
    ev = (WakeupOnAWTEvent) genericEvt;
    events = ev.getAWTEvent();
    processAWTEvent(events);
   }
  }
  
  //Set wakeup criteria for next time
  wakeupOn(wakeupCondition);
 }
 
 //Process a keyboard event
 private void processAWTEvent(AWTEvent[] events)
 {
  for( int n = 0; n < events.length; n++)
  {
   if( events[n] instanceof KeyEvent)
   {
    KeyEvent eventKey = (KeyEvent) events[n];
    
    if( eventKey.getID() == KeyEvent.KEY_PRESSED )
    {
     int keyCode = eventKey.getKeyCode();
     int keyChar = eventKey.getKeyChar();
     
     switch (keyCode)
     {
      case KeyEvent.VK_SPACE:
          System.out.println("W LEWO");
      break;
      
      case KeyEvent.VK_RIGHT:
          System.out.println("W PRAWO");
      break;
     }
    }
   }
  }
 }
}
