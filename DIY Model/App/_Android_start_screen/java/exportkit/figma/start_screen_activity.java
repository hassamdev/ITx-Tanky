
	 
	/*
	 *	This content is generated from the API File Info.
	 *	(Alt+Shift+Ctrl+I).
	 *
	 *	@desc 		
	 *	@file 		start_screen
	 *	@date 		Tuesday 21st of November 2023 10:49:46 AM
	 *	@title 		Page 1
	 *	@author 	
	 *	@keywords 	
	 *	@generator 	Export Kit v1.3.figma
	 *
	 */
	

package exportkit.figma;

import android.app.Activity;
import android.os.Bundle;


import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;

public class start_screen_activity extends Activity {

	
	private View _bg__start_screen_ek2;
	private View rectangle_1;
	private ImageView _rectangle_2;
	private View _bg__text_ek1;
	private TextView welcome_;
	private TextView see_your_water_tank_status_;
	private TextView itx_tanky;
	private ImageView rectangle_20;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);

		
		_bg__start_screen_ek2 = (View) findViewById(R.id._bg__start_screen_ek2);
		rectangle_1 = (View) findViewById(R.id.rectangle_1);
		_rectangle_2 = (ImageView) findViewById(R.id._rectangle_2);
		_bg__text_ek1 = (View) findViewById(R.id._bg__text_ek1);
		welcome_ = (TextView) findViewById(R.id.welcome_);
		see_your_water_tank_status_ = (TextView) findViewById(R.id.see_your_water_tank_status_);
		itx_tanky = (TextView) findViewById(R.id.itx_tanky);
		rectangle_20 = (ImageView) findViewById(R.id.rectangle_20);
	
		
		_rectangle_2.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				
				Intent nextScreen = new Intent(getApplicationContext(), android_large___1_activity.class);
				startActivity(nextScreen);
			
		
			}
		});
		
		
		//custom code goes here
	
	}
}
	
	