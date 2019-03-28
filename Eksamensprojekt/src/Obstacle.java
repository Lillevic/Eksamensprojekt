
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author victo
 */
class Obstacle {
    PImage Brick;
    //LavaBrick = Test.Inst.loadImage("LavaBrick.png");
    //LavaBrick.resize((int)(width/wallNr),(int)(width/wallNr));
    //Brick = Test.Inst.loadImage("GrabBrick.png");
    //Brick.resize((int)(width/wallNr),(int)(width/wallNr));
    
    // A boundary is a simple rectangle with x,y,width,and height
    float x;
    float y;
    float w;
    float h;
    boolean Grabable;
    PVector color;
    // But we also have to make a body for box2d to know about it
    Body b;
    Obstacle(float x_, float y_, float w_, float h_, boolean Grabable_) {
        
        x = x_;
        y = y_;
        w = w_;
        h = h_;
        Grabable = Grabable_;
        if(Grabable){
            Brick = Test.Inst.loadImage("GrabBrick.png");
        }else{
            Brick = Test.Inst.loadImage("LavaBrick.png");
        }
        Brick.resize((int)(w),(int)(h));
        // Define the polygon
        PolygonShape sd = new PolygonShape();
        // Figure out the box2d coordinates
        float box2dW = Test.Inst.box2d.scalarPixelsToWorld(w / 2);
        float box2dH = Test.Inst.box2d.scalarPixelsToWorld(h / 2);
        // We're just a box
        sd.setAsBox(box2dW, box2dH);
        // Create the body
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(Test.Inst.box2d.coordPixelsToWorld(x, y));
        b = Test.Inst.box2d.createBody(bd);
        // Attached the shape to the body using a Fixture
        b.createFixture(sd, 1);
    } // Define the polygon
    // Figure out the box2d coordinates
    // We're just a box
    // Create the body
    // Attached the shape to the body using a Fixture

    // Draw the boundary, if it were at an angle we'd have to do something fancier
    public void display() {
        
        
        Test.Inst.rectMode(PConstants.CENTER);
        Test.Inst.image(Brick,x-w/2, y-h/2 + Test.Inst.scroll);
    }

    public boolean checkDeath() {
        // Is it off the bottom of the screen?
        if (y > Test.Inst.height + h - Test.Inst.scroll) {
            killBody();
            return true;
        }
        return false;
    }

    public void killBody() {
        Test.Inst.box2d.destroyBody(b);
    }

    public boolean checksides(float lx, float ly) {
        if (lx < x + w / 2 && x - w / 2 < lx && ly < y + h / 2 + Test.Inst.scroll && Test.Inst.scroll + y - h / 2 < ly) {
            return true;
        }
        return false;
    }
    
}
