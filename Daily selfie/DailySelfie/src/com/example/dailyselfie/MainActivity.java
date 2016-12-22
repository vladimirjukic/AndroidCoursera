package com.example.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.PendingIntent;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dailyselfie.provider.SelfieContract;

public class MainActivity extends ListActivity implements LoaderCallbacks<Cursor> {

	public static final String IMAGE_PATH = "imagePath";
	public static final String IMAGE_NAME = "imageName";

	private static final int REQUEST_IMAGE_CAPTURE = 0;
	private static final String APP_DIR = "DailySelfie";
	private static final long TWO_MIN = 2 * 60 * 1000;

	private SelfieListAdapter adapter = null;
	private Uri selfieUri = null;
	private AlarmManager alarmManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adapter = new SelfieListAdapter(getApplicationContext(), null, 0);
		getListView().setAdapter(adapter);

		ListView selfieListView = (ListView) findViewById(android.R.id.list);
		selfieListView.setClickable(true);
		selfieListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SelfieRow selfieRow = (SelfieRow) adapter.getItem(position);
				Intent intent = new Intent(getApplicationContext(), LargeSelfieActivity.class);
				intent.putExtra(IMAGE_NAME, selfieRow.getName());
				intent.putExtra(IMAGE_PATH, selfieRow.getImagePath());
				startActivity(intent);
			}
		});

		getLoaderManager().initLoader(0, null, this);
		startNoficiationAlarm();
	}

	private void startNoficiationAlarm() {
		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(getApplicationContext(), SelfieBroadcastReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, intent, 0);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), TWO_MIN, pendingIntent);
	}

	private void cancelNoficiationAlarm() {
		Intent intent = new Intent(getApplicationContext(), SelfieBroadcastReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, intent, 0);
		if (alarmManager != null) {
			alarmManager.cancel(pendingIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_camera) {
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			selfieUri = Uri.fromFile(createFile());
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, selfieUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

		}
		else if (id == R.id.action_delete) {
			adapter.removeAllViews();

			String root = Environment.getExternalStorageDirectory().getPath();
			File bitmapStorageDir = null;
			if (root != null) {
				bitmapStorageDir = new File(root, APP_DIR);
			}

			if (bitmapStorageDir.exists()) {
				deleteFiles(bitmapStorageDir.getPath());
			}
		}
		else if (id == R.id.action_cancel_not) {
			cancelNoficiationAlarm();
			Toast.makeText(getApplicationContext(), "Notification cancelled", Toast.LENGTH_SHORT).show();
		}

		return super.onOptionsItemSelected(item);
	}

	public static void deleteFiles(String path) {

		File file = new File(path);

		if (file.exists()) {
			String deleteCmd = "rm -r " + path;
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(deleteCmd);
			}
			catch (IOException e) {
			}
		}
	}

	private File createFile() {
		String root = Environment.getExternalStorageDirectory().getPath();
		File bitmapStorageDir = null;
		if (root != null) {
			bitmapStorageDir = new File(root, APP_DIR);
		}

		if (!bitmapStorageDir.exists()) {
			bitmapStorageDir.mkdir();
		}

		return new File(bitmapStorageDir, "Image_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			SelfieRow selfieRow = new SelfieRow(selfieUri.getLastPathSegment(), selfieUri.getPath());
			adapter.add(selfieRow);
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> newLoader, Cursor newCursor) {
		adapter.swapCursor(newCursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> newLoader) {
		adapter.swapCursor(null);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), SelfieContract.CONTENT_URI, null, null,
				null, null);
		return cursorLoader;
	}
}
