import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.joints.*; 
import org.jbox2d.collision.shapes.Shape; 
import org.jbox2d.dynamics.contacts.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Test extends PApplet {
static public Test Inst;
int gamestate = 0;
float scroll,score,speed=100;
PVector Obstaclesize = new PVector(10, 50);

float ObstacleFreq = 1.5f;
boolean grapped = false;
boolean pointed = false;
float lx,ly;









Player p;
Box2DProcessing box2d;
float f;

ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
ArrayList<Particle> particles = new ArrayList<Particle>();
Button start = new Button(new PVector(250, 500), new PVector(200, 100), "start");

public void setup() {
  

  // Initialize box2d physics and create the world
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  // We are setting a custom gravity
  box2d.setGravity(0, -50);

  //SpilSetup();
  startSetup();
  
  SpilSetup();
  //Startsetup();
}

public void draw() {
  switch(gamestate) {
  case 0:
    Start();
    break;
  case 1:
    Spil();
    break;
  }
}

public void killAll(){
  println("kiling " + obstacles.size());
  for(int i = obstacles.size()-1; i>0;i--){
    //println(obstacles.size());
    obstacles.get(i).killBody();
    obstacles.remove(i);
    
  }
  p.killBody();
  removePoint();
  gamestate = 0;
  startSetup();
}

public void mousePressed() {
  switch(gamestate) {
  case 0:
    StartButton();
    break;
  case 1:
    if(mouseButton == LEFT){
      grapple();    
    }else{
      removePoint();
    }
  break;
  }
  
}

public void keyPressed() {
  switch(gamestate) {
    case 0:
  
      break;
    case 1:
      p.dir();
      break;
  }
}
public void keyReleased() {
  switch(gamestate) {
    case 0:
  
      break;
    case 1:
      p.nodir();
      break;
  }
}
public void grapple(){
  if(grapped){
    removePoint();
  }
  float px = mouseX-p.x;
  float py = mouseY-p.y-scroll;
  PVector hi = new PVector(px,py);
  float dist = hi.mag();
  hi = hi.div(dist);
  
  
  f = 0;
  int num = 0;
  grapped = false;
  pointed = false;
  while(abs(num) < 1000){
    float lx = p.x+f*hi.x*5;
    float ly = p.y+scroll+f*hi.y*5;
    ellipse(lx,ly,5,5);
    for(Obstacle wall: obstacles){
      if(wall.checksides(lx,ly)){
        particles.add(new Particle(lx,ly,4));
        num = 1000;
        grapped = true;
      }
    }
    f++;
    num++;
    
  }
}




public void Grab(Body i,float dist){
         DistanceJointDef djd = new DistanceJointDef();
         // Connection between previous particle and this one
         djd.bodyA = p.body;
         djd.bodyB = i;
         // Equilibrium length
         djd.length = box2d.scalarPixelsToWorld(dist);
         // These properties affect how springy the joint is 
         djd.frequencyHz = 30;
         djd.dampingRatio = 0;
         
         // Make the joint.  Note we aren't storing a reference to the joint ourselves anywhere!
         // We might need to someday, but for now it's ok
         DistanceJoint dj = (DistanceJoint) box2d.world.createJoint(djd);
         grapped = true;
}

public void updatePoint(){
  if (particles.size()>0){pointed = true;}
  if(pointed){
    lx=particles.get(0).x;
    ly=particles.get(0).y;
    removePoint();
    particles.add(new Particle(lx,ly+scroll,4));
    if(f*5+0.2f<dist(p.x,p.y,lx,ly)){
      Grab(particles.get(0).body,f*5);
    }
  }
  println(f*5);
  println(dist(p.x,p.y,lx,ly));
}

public void removePoint(){
  for(int i = particles.size()-1; i>-1;i--){
    particles.get(i).killBody();
    particles.remove(i);
    pointed = false;
  }
}

  
  
public void SpilSetup(){
  scroll = 0;
  speed=1;
  score = 0;
  for(int i = 0; i<height; i++){
    float spawn = random(100);
    if(spawn<ObstacleFreq/2){
      obstacles.add(new Obstacle(random(width),i,random(Obstaclesize.x*2,Obstaclesize.y*2),random(Obstaclesize.x,Obstaclesize.y)));
    }
  }
  p = new Player(width/2,height/2,10);
  f = 10;
  particles.add(new Particle(width/2,height/2,4));
  Grab(particles.get(0).body,f*5);
  grapped = true;
  pointed = true;
}



public void startSetup(){
  clear();
  background(144,192,107);
  start.show();
  textAlign(CENTER);
  textSize(32);
  fill(0, 102, 153);
  text("SlingClimb",width/2,100);
}
public void Spil(){
  clear();
  background(144,192,107);
  Vec2 pos = box2d.getBodyPixelCoord(p.body);
  println("WHY:  "+pos.y);
  box2d.step();
  scroll();
  
  update();
  randomObs();
  p.done();
  fill(0);
  textAlign(LEFT);
  text("score:   "+PApplet.parseInt(score),20,30);
}

public void scroll(){
  while(p.y<400-scroll){
    scroll += speed;
    speed = speed*1.0001f;
    randomObs();
    score += speed*2;
  }
  scroll += speed;
  speed = speed * 1.0001f;
  score += speed;
}

public void update(){
  for (int i = obstacles.size()-1; i >= 0; i--) {
    Obstacle b = obstacles.get(i);
    b.display();
    if (b.checkDeath()) {
      obstacles.remove(i);
    }
  }
  p.display();
  p.move();
  for(Particle pa: particles){
    pa.display();
    Grab(particles.get(0).body,f*5);
  }
  updatePoint();
}

public void randomObs(){
  float spawn = random(100);
  if(spawn<ObstacleFreq*speed){
    float sizeX =  random(Obstaclesize.x*2,Obstaclesize.y*2);
    float sizeY =  random(Obstaclesize.x,Obstaclesize.y);
    obstacles.add(new Obstacle(random(width),-50-scroll-sizeY,sizeX,sizeY));
  }
}
public void Start(){
  
}

public void StartButton(){
  if(start.click()){
    SpilSetup();
    gamestate=1;
  }
}
  public void settings() {  size(2000, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Test" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
