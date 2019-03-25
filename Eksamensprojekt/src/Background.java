
import processing.core.PImage;
import processing.core.PVector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author erikv
 */
public class Background {
    PImage img;
    int x,y;
    Background(PImage img_, int x_, int y_) {
        img=img_;
        x=x_;
        y=y_;
    }
    public void update(){
        display();
        done();
    }
    public void display(){
                Test.Inst.image(img, x, y + Test.Inst.scroll);
        
    }
    
    public boolean done(){
        if(y+Test.Inst.scroll>Test.Inst.height){
            return true;
        }else{
            return false;
        }
    }
}
