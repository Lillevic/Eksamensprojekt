//standard processing library
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

//Laver Test sådan at vi kan bruge proessing andre steder
public class Test extends PApplet {

static public Test Inst;

//Global startværdier
int gamestate = 0; //sørger for at vi starter på menu skærmen
float scroll = 0,score,speed=100;
PVector Obstaclesize = new PVector(10, 50);
float ObstacleFreq = 1.5f;
boolean grapped = false;
boolean pointed = false;
float lx,ly;

Player p;
float playersize = 10;
float PictureR = (float)1.5;

Background back;
Box2DProcessing box2d;
float f;
PImage img;
String[] list;
int[] scores = new int[5];
//Start arraylists og objects
ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
ArrayList<Particle> particles = new ArrayList<Particle>();
ArrayList<Background> Backgrounds = new ArrayList<Background>();
ArrayList<Background> HolderBackgrounds = new ArrayList<Background>();
Button start;

int wallNr = 5;
int wallint = 1;
boolean BackLayer = false;
PImage playerImg,Backwall,Backwin,Brick,LavaBrick;

ArrayList<PVector> players = new ArrayList<PVector>();

public void setup() {
  //Gør inst brugbar
  Inst = this;
  images();
  list = loadStrings("scores.txt");
  if(list.length < 1){
      String[] Zero = {"0","0","0","0","0"};
      list = Zero;
  }
  for(int i =0;i<list.length;i++){
      try {
      scores[i] = Integer.parseInt(list[i]);
        }
        catch (NumberFormatException e){
         scores[i]= 0;
        }
  }
  
  // Initialize box2d physics and create the world
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  // We are setting a custom gravity
  box2d.setGravity(0, -50);
  start = new Button(new PVector(250, 500), new PVector(200, 100), "start");
  

  //sætter nogle værdier op før det går igang.
  start = new Button(new PVector(width/2, height-500), new PVector(200, 100), "start");
  startSetup();
  SpilSetup();
}
//vælger hvilket state vi er i og kører deres kode.
public void draw() {
  clear();
  background(144,192,107);
  for(Background b:Backgrounds){
    b.display();
    if(b.done()){
        BackLayer = true;
        
        ellipse(100,100,100,100);
        //HolderBackgrounds.add(new Background(Backwall,Backgrounds.get(i).x,Backgrounds.get(i).y+width/wallNr*11/12));
        
    }
  }
  if(BackLayer){
      for(int j = 0; j<wallNr+1;j++){
        Backgrounds.remove(0);
      }
      newBackLine(height-(wallint)*width/wallNr*11/12);
      wallint++;
      BackLayer = false;
  }
  Vec2 pos = box2d.getBodyPixelCoord(p.body);
  box2d.step();
  updateObstacle();
  scroll();
  randomObs();
  
  
  
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
  for(int i = obstacles.size()-1; i>=0;i--){
    //println(obstacles.size());
    obstacles.get(i).killBody();
    obstacles.remove(i);
    
  }
  for(int i = Backgrounds.size()-1; i>=0;i--){
      Backgrounds.remove(i);
  }
  wallint = 1;
  scroll = 0;
  p.killBody();
  removePoint();
  gamestate = 0;
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
        if(wall.Grabable){
            particles.add(new Particle(lx,ly,4));
        }
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
         //grapped = true;
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
}

public void removePoint(){
  for(int i = particles.size()-1; i>-1;i--){
    particles.get(i).killBody();
    particles.remove(i);
    pointed = false;
  }
}

public void SpilSetup(){
  speed=1;
  score = 0;
  p = new Player(width/2,height/2,10,playerImg,PictureR);
  f = 10;
  particles.add(new Particle(width/2,height/2,playersize));
  Grab(particles.get(0).body,f*5);
  worldsetup();
  grapped = true;
  pointed = true;
}

public void Spil(){
  playerupdate();
  if(p.done()){
     leaderboard();
      killAll();
      startSetup();
  }
  fill(255,255,0);
  textAlign(LEFT);
  text("score:   "+PApplet.parseInt(score),20,30);
}

public void scroll(){
  while(p.y<width/8-scroll){
    scroll += speed;
    speed = speed*1.0001f;
    randomObs();
    score += speed*2;
  }
  scroll += speed;
  speed = speed * 1.0001f;
  score += speed;

}
        
public void playerupdate(){
  p.display();
  p.move();
  for(Particle pa: particles){
    pa.display();
    Grab(particles.get(0).body,f*5);
  }
  updatePoint();
}

public void updateObstacle(){
  for (int i = obstacles.size()-1; i >= 0; i--) {
    Obstacle b = obstacles.get(i);
    b.display();
    if (b.checkDeath()) {
      obstacles.remove(i);
    }
  }
  
}

public void randomObs(){
  float spawn = random(100);
  if(spawn<ObstacleFreq*speed){
    float sizeX =  random(Obstaclesize.x*2,Obstaclesize.y*2);
    float sizeY =  random(Obstaclesize.x,Obstaclesize.y);
    obstacles.add(new Obstacle(random(width),-50-scroll-sizeY,sizeX,sizeY,getRandomBoolean(80)));
  }
}

public void startSetup(){
  worldsetup();
}

public void Start(){
  start.show();
  textAlign(CENTER);
  textSize(32);
  fill(255, 120, 0);
  text("Slingknight",width/2,100);
  text("Highscores:",width/12,height/15-height/25);
  for(int i=0;i<list.length;i++){
      
      text(list[i],width/15,height/15+height/15*i);
  }
}

public boolean getRandomBoolean(float procent) {
    float rslt = random(100);
    if(rslt<=procent){
        return true;
    }
    return false;
}

public void StartButton(){ //startknappen begynder spillet.
  if(start.click()){
    killAll(); //dræber alle resterende objekter fx dem til baggrunds animationer
    SpilSetup(); //sætter spillet op
    gamestate=1; //skifter spiltilstanden fra 0 (start tilstanden) til 1 (
  }
}

  public void settings() {  size(2000, 1000, P2D); }
  
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Test" };
    if (passedArgs != null) {
        PApplet.main(appletArgs);
    }
  }
  
  public void worldsetup(){
    for(int i = 0; i<height; i++){  //spawner et tilfædigt antal blokke,(mellem 1 og 100), i tilfældige positioner når spillet begynder da dette sørger for at der altid er en måde at komme vidrere når man starter.
      float spawn = random(100);
      if(spawn<ObstacleFreq/2){
        obstacles.add(new Obstacle(random(width),i,random(Obstaclesize.x*2,Obstaclesize.y*2),random(Obstaclesize.x,Obstaclesize.y),getRandomBoolean(90)));
      }
    }
    //if(true){
    for(wallint = 1;wallint<wallNr*2;wallint++){
        newBackLine(height-(wallint)*width/wallNr*11/12);
    }
    
  }
  
  public void images(){
        playerImg = Test.Inst.loadImage("Helmet.png");
        playerImg.resize((int)(playersize*2*PictureR),(int)(playersize*2*PictureR));
        Backwall = Test.Inst.loadImage("Brick.png");
        Backwall.resize((int)(width/wallNr),(int)(width/wallNr));
        Backwin = Test.Inst.loadImage("Window.png");
        Backwin.resize((int)(width/wallNr),(int)(width/wallNr));
        
        
  }
  
  public void newBackLine(int y){
      for(int i = 0; i<wallNr+1;i++){
          int BackX = i*width/wallNr*11/12;
          if(random(100)<=10){
              Backgrounds.add(new Background(Backwin,BackX,y));
          }else{
              Backgrounds.add(new Background(Backwall,BackX,y));
          }
          
          
      }
  }
  
  public void leaderboard(){
      int scor = (int) score;
      scores = append(scores,scor);
      scores = sort(scores);
      scores = reverse(scores);
      scores = shorten(scores);
      for(int i =0;i<scores.length;i++){
          list[i]= ""+PApplet.parseInt(scores[i]);
      }
      saveStrings("scores.txt",list); //gemmer listen til et tekst dokument
    }
}


