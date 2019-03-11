
import processing.core.PConstants;
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
class Button {
    
    PVector pos;
    PVector size;
    String text;

    Button(PVector pos_, PVector size_, String text_) {
        pos = pos_;
        size = size_;
        text = text_;
    }

    public boolean click() {
        if (Test.Inst.mouseX < pos.x + size.x / 2 && pos.x - size.x / 2 < Test.Inst.mouseX && Test.Inst.mouseY < pos.y + size.y / 2 && pos.y - size.y / 2 < Test.Inst.mouseY) {
            return true;
        }
        return false;
    }

    public void show() {
        Test.Inst.rectMode(PConstants.CENTER);
        Test.Inst.fill(200, 200, 200);
        Test.Inst.rect(pos.x, pos.y, size.x, size.y);
        Test.Inst.textAlign(PConstants.CENTER);
        Test.Inst.textSize(32);
        Test.Inst.fill(0, 102, 153);
        Test.Inst.text(text, pos.x, pos.y);
    }
    
}
