package com.example.lucydavidson.hw3;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by lucydavidson on 4/9/17.
 */

public class Explosion {

    private float xPos;
    private float yPos;
    private int radius;
    private int color;
    private int time;

    private Path explosion = new Path();
    private Paint style;

    //Constructor
    public Explosion(int initXPos, int initYPos){


        xPos = (float)initXPos;
        yPos = (float)initYPos;
        radius = 10;
        color = 0xFFFFFFDD;
        time = 0;

        style = new Paint();
        style.setColor(color);

    }

    public void paint(Canvas canvas){
        /*
        explosion.lineTo((xPos + 0), (yPos + 40));
        explosion.lineTo((xPos + 15), (yPos + 60));
        explosion.lineTo((xPos + 20), (yPos + 20));
        explosion.lineTo((xPos + 60), (yPos + 15));
        explosion.lineTo((xPos + 40), (yPos + 0));
        explosion.lineTo((xPos + 60), (yPos - 15));
        explosion.lineTo((xPos + 20), (yPos - 20));
        explosion.lineTo((xPos + 15), (yPos - 60));
        explosion.lineTo((xPos + 0), (yPos - 40));
        explosion.lineTo((xPos - 15), (yPos - 60));
        explosion.lineTo((xPos - 20), (yPos - 20));
        explosion.lineTo((xPos - 60), (yPos - 15));
        explosion.lineTo((xPos - 40), (yPos + 0));
        explosion.lineTo((xPos - 60), (yPos + 15));
        explosion.lineTo((xPos - 20), (yPos + 20));
        explosion.lineTo((xPos - 15), (yPos + 60));
        explosion.lineTo((xPos + 0), (yPos + 40));
        explosion.close();

        canvas.drawPath(explosion, style);
        */
        canvas.drawCircle(xPos, yPos, radius, style);
    }

    //Causes a rapid growth of the explosion shape.
    public void expand(){
        //Increase the radius of the circle.
        radius = radius + (int)(0.8*radius);
    }

    //The explosion slowly fades over time.
    public void dissipate() {
        //Get the transparency.
        int alpha = style.getAlpha();
        //Reduce the transparency.
        alpha = alpha - 20;
        if(alpha > 0) {
            //Set to new transparency
            style.setAlpha(alpha);
        }

    }

    public void colorProgression() {
        if(radius < 500) style.setColor(Color.WHITE);
        if(radius >= 500 && radius < 1200) style.setColor(Color.YELLOW);
        if(radius >= 1200) style.setColor(Color.RED);
    }

    public int getRadius(){return radius;}
}
