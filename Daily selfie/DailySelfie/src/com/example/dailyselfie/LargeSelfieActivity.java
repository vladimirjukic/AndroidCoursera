package com.example.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class LargeSelfieActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.large_selfie_image);

		ImageView largeSelfieImage = (ImageView) findViewById(R.id.large_selfie);
		largeSelfieImage.setScaleType(ScaleType.FIT_XY);

		Intent intent = getIntent();
		String imageName = intent.getStringExtra(MainActivity.IMAGE_NAME);
		String imagePath = intent.getStringExtra(MainActivity.IMAGE_PATH);

		this.setTitle(imageName);
		SelfieListAdapter.setImage(largeSelfieImage, imagePath, 8);
	}

}
