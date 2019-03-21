
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
    PImage Back;
    float y, y2;
    int nr = 3;
    Background(int Pwidth) {
        y=Pwidth;
        Test.Inst.save("noget.txt");
        Back = Test.Inst.loadImage("Brick.png");
        Back.resize((int)(Pwidth/nr),(int)(Pwidth/nr));
    }
    public void update(){
        display();
        done();
    }
    public void display(){
        for(int qx = 0; qx<nr; qx++){
            for(int qy = 0; qy<nr*nr; qy++){
                Test.Inst.image(Back, qx*(int)(Test.Inst.width/nr), y + Test.Inst.scroll-(qy*(int)(Test.Inst.width/nr))+Test.Inst.width);
            }
        }
        
    }
    
    public void done(){
        if(y+Test.Inst.scroll>Test.Inst.width){
            y-=Test.Inst.width;
        }
    }
}
