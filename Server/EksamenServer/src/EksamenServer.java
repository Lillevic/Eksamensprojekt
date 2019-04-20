import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

//box2d for tyngdekraft og grappling
import shiffman.box2d.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.joints.*; 
import org.jbox2d.collision.shapes.Shape; 
import org.jbox2d.dynamics.contacts.*; 

//java imports
import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 
import java.util.Random;

import at.mukprojects.mukcast.message.*;
//import at.mukprojects.mukcast.client.*;
import at.mukprojects.mukcast.server.*;

import custom.PVectorMessage;

public class EksamenServer extends PApplet {

static public EksamenServer Inst;

MuKCastServer server;

PImage img1, img2;
ArrayList<PVector> players = new ArrayList<PVector>();

public void setup() {

  textAlign(CENTER, CENTER);

  img1 = loadImage("vstreamsphere01.jpg");
  img2 = loadImage("vstreamsphere02.jpg");

  img1.resize(800, 450);
  img2.resize(800, 450);

  server = new MuKCastServer(this);
  try {
  	server.startServer();
  } 
  catch(IOException e) {
    e.printStackTrace();
  }

  image(img1, 0, 110, width/2, 225);
  image(img2, width/2, 110, width/2, 225);

  textSize(48);
  text("Server: " + server.getServerInfo(), width/2, height/2);
}

public void draw() {

  surface.setTitle("Server - FPS: " + frameRate);

  try {
    for (PVector player : players) {
        server.broadcastMessage(new PVectorMessage(new PVector(player.x,player.y)));
    }
    for (int i = players.size() - 1; i >= 0; i--) {
        players.remove(i);
    }
  } 
  catch(IOException e) {
    e.printStackTrace();
  }
}

public void handleMessage(MuKCastServer server, Message message) {
  PVector position = ((PVectorMessage)message).getPVector();
  println(position);
  println("i recieved a message");
  players.add(((PVectorMessage) message).getPVector());
}

public void settings() {  size(1000, 1000, P2D); }
  
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "EksamenServer" };
    if (passedArgs != null) {
        PApplet.main(appletArgs);
    }
  }
}