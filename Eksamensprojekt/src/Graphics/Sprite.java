/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

/**
 *
 * @author victo
 */
public class Sprite {

    private final int SIZE;
    private int x,y;
    public int[] pixels;
    public Sprite(int size, int x, int y, SpriteSheet sheet){
        SIZE = size;
        this.x =x*size;
        this.y =y*size;
        this.sheet = sheet;
    }
    private void load(){
        
    }
    
}
