
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import processing.core.PApplet;
import processing.core.PImage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author victo
 */
class Player {
    
    //Player body
    Body body;
    float r;
    int col;
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;
    float x;
    float y;
    PImage img;
    float PictureR;

    Player(float x_, float y_, float r_, PImage img_, float PictureR_) {
        r = r_;
        col = Test.Inst.color(183, 183, 0);
        // This function puts the Player in the Box2d world
        x = x_;
        y = y_;
        img=img_;
        PictureR = PictureR_;
        makeBody(x_, y_, r);
    } // This function puts the Player in the Box2d world

    // This function removes the Player from the box2d world
    public void killBody() {
        Test.Inst.box2d.destroyBody(body);
    }

    // Is the Player ready for deletion?
    public boolean done() {
        // Let's find the screen position of the Player
        Vec2 pos = Test.Inst.box2d.getBodyPixelCoord(body);
        // Is it off the bottom of the screen?
        if (pos.y + Test.Inst.scroll > Test.Inst.height + 200) {
            return true;
        }
        return false;
    }

    //
    public void display() {
        // We look at each body and get its screen position
        Vec2 pos = Test.Inst.box2d.getBodyPixelCoord(body);
        // Get its angle of rotation
        x = pos.x;
        y = pos.y;
        Test.Inst.pushMatrix();
        Test.Inst.stroke(0);
        Test.Inst.strokeWeight(1);
        Test.Inst.fill(col);
        Test.Inst.ellipse(x, y + Test.Inst.scroll, r * 2, r * 2);
        Test.Inst.image(img,x-r*PictureR, y-r*PictureR + Test.Inst.scroll);
        // Let's add a line so we can see the rotation
        Test.Inst.line(0, 0, r, 0);
        Test.Inst.popMatrix();
    }

    // Here's our function that adds the Player to the Box2D world
    public void makeBody(float x, float y, float r) {
        // Define a body
        BodyDef bd = new BodyDef();
        // Set its position
        bd.position = Test.Inst.box2d.coordPixelsToWorld(x, y);
        bd.type = BodyType.DYNAMIC;
        body = Test.Inst.box2d.createBody(bd);
        // Make the body's shape a circle
        CircleShape cs = new CircleShape();
        cs.m_radius = Test.Inst.box2d.scalarPixelsToWorld(r);
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        // Parameters that affect physics
        fd.density = (float)1.5;
        fd.friction = 0.01f;
        fd.restitution = 0.3f;
        // Attach fixture to body
        body.createFixture(fd);
    }

    public void dir() {
        switch (Test.Inst.key) {
            case 'a':
                left = true;
                break;
            case 'A':
                left = true;
                break;
            case 'd':
                right = true;
                break;
            case 'D':
                right = true;
                break;
            case 'w':
                up = true;
                break;
            case 'W':
                up = true;
                break;
            case 's':
                down = true;
                break;
            case 'S':
                down = true;
                break;
        }
    }

    public void nodir() {
        switch (Test.Inst.key) {
            case 'a':
                left = false;
                break;
            case 'A':
                left = false;
                break;
            case 'd':
                right = false;
                break;
            case 'D':
                right = false;
                break;
            case 'w':
                up = false;
                break;
            case 'W':
                up = false;
                break;
            case 's':
                down = false;
                break;
            case 'S':
                down = false;
                break;
        }
    }

    public void move() {
        if (left == true) {
            applyForce(new Vec2(-100, 0));
        }
        if (right == true) {
            applyForce(new Vec2(100, 0));
        }
        if (up == true) {
            Test.Inst.f = PApplet.max(0, Test.Inst.f - 0.2f);
        }
        if (down == true) {
            Test.Inst.f = PApplet.max(0, Test.Inst.f + 0.2f);
        }
    }

    public void applyForce(Vec2 force) {
        Vec2 pos = body.getWorldCenter();
        body.applyForce(force, pos);
    }
    
}
