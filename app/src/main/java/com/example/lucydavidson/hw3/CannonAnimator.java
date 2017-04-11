package com.example.lucydavidson.hw3;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import java.util.Vector;

/**
 * CannonAnimator
 *
 * This is the class for the CannonAnimator. It updates
 * the screen for every tick of time.
 *
 * @author Lucy Davidson
 * @version April 2017
 *
 */

public class CannonAnimator implements Animator  {

    //To contain all my cannonballs.
    private Vector<CannonBall> cannonBalls = new Vector<>();
    //To contain all the cannonballs that are going to be removed.
    private Vector<CannonBall> removeCannonBalls = new Vector<>();
    //To contain all the targets.
    private Vector<Target> targets = new Vector<>();
    //To contain all the targets that are going to be removed.
    private Vector<Target> removeTargets = new Vector<>();
    private Vector<Explosion> explosions = new Vector<>();
    private Vector<Explosion> removeExplosions = new Vector<>();

    /**
     External Citation
     Date: 4 April 2017
     Problem: Needed to use a vector to store the objects.
     Resource:
     https://developer.android.com/reference/java/util/Vector.html
     Solution: I looked up and used the methods in the Vector class to do this.
     */

    //This is used when creating more random targets.
    int numOfTargets = 5;

    //This is the degrees that the cannon is pointed at.
    private int degrees = 45;
    //This is true if the user clicks fire the cannon.
    private boolean fire;
    //This is a running tally of the number of targets hit.
    private int score = 0;

    //This is the style of paint for the score text.
    Paint style = new Paint();
    Paint ground = new Paint();

    @Override
    public int interval() {
        return 25;
    }

    @Override
    public int backgroundColor() {
        return 0xFFadd8e6;
    }

    @Override
    public boolean doPause() {
        return false;
    }

    @Override
    public boolean doQuit() {
        return false;
    }

    @Override
    public void tick(Canvas canvas) {

        if(explosions != null) {
            for (Explosion e : explosions) {
                if(e.getRadius() <= 2000) {
                    e.expand();
                    e.dissipate();
                    e.colorProgression();
                    e.paint(canvas);
                } else {
                    removeExplosions.add(e);
                }
            }
        }

        //If there are no targets then create them.
        if(targets.size() < 1){
            createTarget(numOfTargets);
        }

        //Loop through all the targets and paint them.
        for(Target t: targets){
            t.paint(canvas);
        }

        //Create the style for the score.
        stylize();
        //Draw the score text on the animation screen.
        canvas.drawText("SCORE: " + score, 850, 100, style);

        //If the user clicked the fire button then fire cannon.
        if(fire) {
            CannonBall cb = new CannonBall(degrees);
            //Determines the starting spot.
            //Add new cannonball to the array.
            cannonBalls.add(cb);
            //Set the fire to false so that on the next tick it
            //doesn't create another cannonball from the one button click.
            fire = false;
        }

        //If there are any cannonballs...
        if(cannonBalls != null) {
            //Loop through each cannonball.
            for(CannonBall cb: cannonBalls) {

                if(cb.getCount() >= 30) {
                    Explosion boom = new Explosion(cb.getXPos(), (int)cb.getYPos());
                    explosions.add(boom);
                    removeCannonBalls.add(cb);
                }

                //Draw it on the screen.
                cb.paint(canvas);

                //Loop through all the targets.
                for(Target t: targets){
                    //If a target and a cannonball are in the same spot
                    //then this will return true.
                    boolean check = this.checkIfHit(cb, t);
                    //If the target was hit it will enter this statement.
                    if(check) {
                        //Add the cannonball to the remove array.
                        removeCannonBalls.add(cb);
                        //Add the target to the remove array.
                        removeTargets.add(t);
                        //Draw an explosion.
                        Explosion boom = new Explosion(cb.getXPos(), (int)cb.getYPos());
                        explosions.add(boom);
                    }

                }

                //If the cannonball is off the screen then remove it from the vector.
                if(cb.getXPos() > 2050){
                    removeCannonBalls.add(cb);
                }
            }
        }

        //Remove all the cannonballs, that were placed into the remove vector,
        //from the cannonballs vector.
        if(removeCannonBalls != null) {
            for(CannonBall rcb: removeCannonBalls){
                if(cannonBalls != null) cannonBalls.remove(rcb);
            }
        }
        //Remove all the targets, that were placed into the remove vector,
        //from the targets vector.
        if(removeTargets != null) {
            for(Target rt: removeTargets){
                if(targets != null) targets.remove(rt);
            }
        }

        if(explosions != null) {
            for (Explosion re : removeExplosions) {
                if(explosions != null) explosions.remove(re);
            }
        }

        /**
         External Citation
         Date: 4 April 2017
         Problem: Program would crash when I would try and directly remove an object
         from either the targets or cannonballs vectors, the program wouldn't let
         me fire lots of cannonballs back to back without crashing.
         Resource: Adrian Low
         Solution: Told me to create another vector and add the object to be removed
         into that vector then remove all the objects in the remove vector from the
         original vector at the end, and it worked!
         */

        //Loop to move each cannonball.
        if(cannonBalls != null) {
            //Loop through each cannonball.
            for (CannonBall cb : cannonBalls) {
                //Move it to a new position.
                cb.move();
                //If the cannonball has hit the ground then change the y-velocity.
                if(checkIfGround(cb)) {
                    //If it hasn't bounced three times yet.
                    if(cb.getBounce() < 3) {
                        //Let it bounce.
                        cb.bounce();
                    } else {
                        //Otherwise if it's bounced more than three times already
                        //then let the cannonball rest on the ground.
                        cb.stop();
                    }
                }
            }
        }

        //Create the Cannon
        Cannon myCannon = new Cannon(degrees);
        myCannon.paint(canvas, degrees);

        //Create the ground.
        ground.setColor(0xFF007700);
        canvas.drawRect(0, 1180, 2050, 1220, ground);

    }

    @Override
    public void onTouch(MotionEvent event) {
    }

    //Check if any of the cannonballs hit any of the targets.
    public boolean checkIfHit(CannonBall cb, Target t){
        //Find the difference between the cb position and the target position.
        int yDiff = (int)(Math.abs((cb.getYPos() - t.getYPos())));
        int xDiff = (int)(Math.abs((cb.getXPos() - t.getXPos())));
        //Conditions for the cannonball to hit the target.
        boolean ySame = (yDiff <= (t.getRadius() + cb.getRadius()));
        boolean xSame = (xDiff <= (t.getRadius() + cb.getRadius()));
        //If conditions are true then remove the target and the cannonball,
        //make a new one, and add one to the score tally.
        if(ySame && xSame){
            //Add one to the score to indicate a hit!
            score++;
            //Return true to indicate that a target was hit.
            return true;
        }
        //Returns false if a target was not hit.
        return false;
    }

    //Creates a new batch of targets.
    public void createTarget(int num){
        //Loops through and creates the number of targets specified.
        for(int i = 0; i < num; i++){
            Target t = new Target();
            //Adds the targets to the target array.
            targets.add(t);
        }
    }

    public boolean checkIfGround(CannonBall cb){
        //Ground dimensions (0, 1180, 2050, 1220)
        if(cb.getYPos() >= (1180 - (cb.getRadius()*2))) {
            return true;
        }
        return false;
    }

    //Creates the paint for the score element
    public void stylize(){
        style.setColor(0xFFFFFF00);
        style.setTextSize(75f);
        style.setFakeBoldText(true);

        /**
         External Citation
         Date: 3 April 2017
         Problem: I wanted to make the score text somewhat unique.
         Resource:
         https://developer.android.com/reference/android/graphics/Paint.html
         Solution: I looked up and used the methods in the paint class to do this.
         */
    }

    //Setter when the degrees on the cannon is changed.
    public void setDegrees(int angle){degrees = angle;}
    //Setter when the user clicks fire the cannon.
    public void setFire(boolean initFire){fire = initFire;}

    /**
     External Citation
     Date: 3 April 2017
     Problem: Wasn't sure how to fully implement Animator.
     Resource: TestAnimator by Andrew Nuxoll.
     Solution: Referenced the resource when creating this class.
     */
}
