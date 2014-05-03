import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import oscP5.*; 
import netP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class GMComms extends PApplet {





ControlP5 cp5;
OscP5 oscP5;
NetAddress myRemoteLocation;

String textValue = "";
boolean alert = false;

public void setup() {
  size(1000,130);
  
  oscP5 = new OscP5(this,12000);
  
  myRemoteLocation = new NetAddress("10.0.0.49",12000);
  
  PFont font = createFont("arial",20);
  
  cp5 = new ControlP5(this);
  
                 
  cp5.addTextfield("textValue")
     .setPosition(20,20)
     .setSize(960,40)
     .setFont(createFont("arial",20))
     .setAutoClear(true)
     .setColorBackground(color(0,0,0))
     .captionLabel().setVisible(false)
     ;
     
  textFont(font);
}

public void draw() {
  if(alert){
    background(255,0,0);
  }
  else{
    background(0);
  }
  fill(255);
  text(textValue, 20,100);
}

public void controlEvent(ControlEvent theEvent) {
  if(theEvent.isAssignableFrom(Textfield.class)) {
    OscMessage myMessage = new OscMessage("/GMComms/message");
    myMessage.add(textValue);
    oscP5.send(myMessage, myRemoteLocation);
    println("sent osc message");
  }
}

public void mouseClicked() {
  alert = false;
}

public void keyPressed() {
  alert = false;
}


public void oscEvent(OscMessage theOscMessage) {
  /* check if theOscMessage has the address pattern we are looking for. */
  
  if(theOscMessage.checkAddrPattern("/GMComms/message")==true) {
    /* check if the typetag is the right one. */
    /* parse theOscMessage and extract the values from the osc message arguments. */
    textValue = theOscMessage.get(0).stringValue();
    alert = true;
    print("Text value:");
    println(textValue); 
  } 
  println("### received an osc message. with address pattern "+theOscMessage.addrPattern());
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "GMComms" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
