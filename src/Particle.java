
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author victo
 */
class Particle {
    
    // We need to keep track of a Body and a radius
    Body body;
    float r;
    float x;
    float y;
    int col;

    Particle(float x_, float y_, float r_) {
        r = r_;
        x = x_;
        y = y_ - Test.Inst.scroll;
        // Define a body
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        // Set its position
        bd.position = Test.Inst.box2d.coordPixelsToWorld(x, y);
        body = Test.Inst.box2d.world.createBody(bd);
        // Make the body's shape a circle
        // Make the body's shape a circle
        CircleShape cs = new CircleShape();
        cs.m_radius = Test.Inst.box2d.scalarPixelsToWorld(r);
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        // Parameters that affect physics
        fd.density = 1;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;
        body.createFixture(fd);
        col = Test.Inst.color(175);
    } // Define a body
    // Set its position
    // Make the body's shape a circle
    // Make the body's shape a circle
    // Parameters that affect physics

    // This function removes the particle from the box2d world
    public void killBody() {
        Test.Inst.box2d.destroyBody(body);
    }

    // Is the particle ready for deletion?
    public boolean done() {
        // Let's find the screen position of the particle
        Vec2 pos = Test.Inst.box2d.getBodyPixelCoord(body);
        // Is it off the bottom of the screen?
        if (pos.y > Test.Inst.height + r * 2) {
            killBody();
            return true;
        }
        return false;
    }

    //
    public void display() {
        // Get its angle of rotation
        float a = body.getAngle();
        Test.Inst.pushMatrix();
        Test.Inst.rotate(a);
        Test.Inst.fill(col);
        Test.Inst.stroke(0);
        Test.Inst.strokeWeight(1);
        Test.Inst.ellipse(x, y + Test.Inst.scroll, r * 2, r * 2);
        // Let's add a line so we can see the rotation
        Test.Inst.line(x, y + Test.Inst.scroll, Test.Inst.p.x, Test.Inst.p.y + Test.Inst.scroll);
        Test.Inst.popMatrix();
    }
    
}
