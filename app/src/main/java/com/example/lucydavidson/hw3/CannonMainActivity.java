package com.example.lucydavidson.hw3;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * CannonMainActivity
 * 
 * This is the activity for the cannon animation. It creates a AnimationCanvas
 * containing a particular Animator object
 * 
 * @author Andrew Nuxoll
 * @author Lucy Davidson
 * @version April 2017
 *
 * Features implemented from Part B:
 * 1. Allow the user to have an arbitrary number of cannonballs in the air at once. (10 pts).
 * 2. Instead of stopping when it hits the ground, the cannonball rolls along and/or
 * 	  bounces in a believable manner. (10 pts).
 * 3. Have the targets create an animated explosion (or similar effect) when the
 *    cannonball hits them. (5 pts).
 * 
 */

public class CannonMainActivity extends Activity
		implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

	//Set up the variables for the layout.
	private SeekBar angleSeek;
	private TextView angleText;
	private Button fireCannonButton;
	private Animator cannonAnim;

	//Set the initial angle of the cannon to 45.
	private int angle = 45;
	/**
	 * creates an AnimationCanvas containing a CannonAnimator.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cannon_main);

		// Create an animation canvas and place it in the main layout
		cannonAnim = new CannonAnimator();
		AnimationCanvas myCanvas = new AnimationCanvas(this, cannonAnim);
		LinearLayout mainLayout = (LinearLayout) this.findViewById(R.id.topLevelLayout);
		mainLayout.addView(myCanvas);

		//Initialize the fire boolean so that it won't fire until the user
		//pressed the fire cannon button.
		cannonAnim.setFire(false);

		//Connect the variables to the views.
		angleSeek = (SeekBar)findViewById(R.id.angle_seekbar);
		fireCannonButton = (Button)findViewById(R.id.fire_cannon_button);
		angleText = (TextView)findViewById(R.id.angle_text);

		//Set the listeners to the slider and the button.
		angleSeek.setOnSeekBarChangeListener(this);
		fireCannonButton.setOnClickListener(this);

	}

	@Override
	//When the seek bar is moved.
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		//Update where the cannon should be pointed.
		angle = progress;
		//Set the cannon to where it should be pointed.
		cannonAnim.setDegrees(angle);
		//Set the text display so it displays the current angle.
		angleText.setText("ANGLE: " + angle);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onClick(View v) {
		//The user wants to fire the cannon.
		cannonAnim.setFire(true);
	}
}
