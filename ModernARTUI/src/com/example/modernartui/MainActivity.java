package com.example.modernartui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	private SeekBar seekBar = null;
	LinearLayout layoutColor1 = null;
	LinearLayout layoutColor2 = null;
	LinearLayout layoutColor3 = null;
	LinearLayout layoutColor4 = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		layoutColor1 = (LinearLayout) findViewById(R.id.layout_color1);
		layoutColor1.setBackgroundColor(Color.rgb(153, 0, 0));
		layoutColor2 = (LinearLayout) findViewById(R.id.layout_color2);
		layoutColor2.setBackgroundColor(Color.rgb(51, 0, 102));
		layoutColor3 = (LinearLayout) findViewById(R.id.layout_color3);
		layoutColor3.setBackgroundColor(Color.rgb(204, 0, 102));
		layoutColor4 = (LinearLayout) findViewById(R.id.layout_color4);
		layoutColor4.setBackgroundColor(Color.rgb(0, 0, 153));

		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setMax(255);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				layoutColor1.setBackgroundColor(Color.rgb(50 + progress, 100 + progress, 200 + progress));
				layoutColor2.setBackgroundColor(Color.rgb(200 + progress, 100 + progress, 50 + progress));
				layoutColor3.setBackgroundColor(Color.rgb(100 + progress, 255 + progress, 100 + progress));
				layoutColor4.setBackgroundColor(Color.rgb(255 + progress, 100 + progress, 255 + progress));
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_info) {
			AlertDialog.Builder moreInfoAlert = new AlertDialog.Builder(MainActivity.this);
			moreInfoAlert
					.setMessage("Inspired by the works of artists such as Piet Mondrian and Ben Nicholson.\n\nClick below to learn more!");
			moreInfoAlert.setCancelable(true);
			moreInfoAlert.setPositiveButton("Visit MOMA", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.moma.org")));
				}
			});
			moreInfoAlert.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			moreInfoAlert.show();
		}
		return true;
	}
}
