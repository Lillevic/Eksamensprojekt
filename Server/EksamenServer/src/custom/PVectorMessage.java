package custom;

import at.mukprojects.mukcast.message.*;
import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

public class PVectorMessage implements Message {

    private static final long serialVersionUID = -4284344687323203L;
    private PVector pvector;

    public PVectorMessage(PVector pvector) {
        this.pvector=pvector;
    }

    public PVector getPVector() {
        return pvector;
    }
}
