package com.example.dailyselfie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.example.dailyselfie.provider.SelfieContract;

public class SelfieListAdapter extends CursorAdapter {

	private final List<SelfieRow> items = new ArrayList<SelfieRow>();
	private final Context context;
	private static LayoutInflater layoutInflater = null;

	public SelfieListAdapter(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	static class ViewHolder {
		ImageView selfie;
		TextView name;
	}

	public void add(SelfieRow item) {

		items.add(item);
		ContentValues values = new ContentValues();

		values.put(SelfieContract.SELFIE_NAME, item.getName());
		values.put(SelfieContract.SELFIE_BITMAP_PATH, item.getImagePath());

		context.getContentResolver().insert(SelfieContract.CONTENT_URI, values);
	}

	@Override
	public Cursor swapCursor(Cursor newCursor) {

		if (newCursor != null) {
			items.clear();

			if (newCursor.moveToFirst()) {
				do {
					items.add(getSelfieRowFromCursor(newCursor));
				} while (newCursor.moveToNext());
			}
		}
		return super.swapCursor(newCursor);
	}

	private SelfieRow getSelfieRowFromCursor(Cursor cursor) {

		String selfieName = cursor.getString(cursor.getColumnIndex(SelfieContract.SELFIE_NAME));
		String selfieBitmapPath = cursor.getString(cursor.getColumnIndex(SelfieContract.SELFIE_BITMAP_PATH));

		return new SelfieRow(selfieName, selfieBitmapPath);

	}

	public void clear() {
		items.clear();
	}

	public void removeAllViews() {
		clear();

		context.getContentResolver().delete(SelfieContract.CONTENT_URI, null, null);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int pos) {
		return items.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getCursor() == null) {
			throw new IllegalStateException("this should only be called when the cursor is valid");
		}

		if (getCursor().moveToPosition(position)) {
			View newView;
			if (convertView == null) {
				newView = newView(context, getCursor(), parent);
			}
			else {
				newView = convertView;
			}
			bindView(newView, context, getCursor());
			return newView;
		}
		else {
			SelfieRow item = items.get(position);
			View newView;
			ViewHolder holder = new ViewHolder();

			newView = layoutInflater.inflate(R.layout.selfie_item, parent, false);
			holder.name = (TextView) newView.findViewById(R.id.selfie_name);
			holder.name.setText(item.getName());
			holder.selfie = (ImageView) newView.findViewById(R.id.selfie_image);
			holder.selfie.setScaleType(ScaleType.FIT_CENTER);
			setImage(holder.selfie, item.getImagePath(), 16);

			newView.setTag(holder);

			return newView;
		}
	}

	public static void setImage(ImageView imageView, String imagePath, int sampleSize) {
		try {
			InputStream stream = new FileInputStream(imagePath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = sampleSize;
			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
			bitmap = rotateImage(bitmap, imagePath);
			imageView.setImageBitmap(bitmap);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Bitmap rotateImage(Bitmap bitmap, String imagePath) {
		// Rotate Image if Needed
		try {
			// Determine Orientation
			ExifInterface exif = new ExifInterface(imagePath);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

			// Determine Rotation
			int rotation = 0;
			if (orientation == 6)
				rotation = 90;
			else if (orientation == 3)
				rotation = 180;
			else if (orientation == 8)
				rotation = 270;

			// Rotate Image if Necessary
			if (rotation != 0) {
				// Create Matrix
				Matrix matrix = new Matrix();
				matrix.postRotate(rotation);

				// Rotate Bitmap
				Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

				// Pretend none of this ever happened!
				bitmap.recycle();
				bitmap = rotated;
				rotated = null;
			}
		}
		catch (Exception e) {
		}

		return bitmap;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View newView;
		ViewHolder holder = new ViewHolder();

		newView = layoutInflater.inflate(R.layout.selfie_item, parent, false);
		holder.name = (TextView) newView.findViewById(R.id.selfie_name);
		holder.selfie = (ImageView) newView.findViewById(R.id.selfie_image);

		newView.setTag(holder);

		return newView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		ViewHolder holder = (ViewHolder) view.getTag();
		holder.selfie.setScaleType(ScaleType.FIT_CENTER);
		setImage(holder.selfie, cursor.getString(cursor.getColumnIndex(SelfieContract.SELFIE_BITMAP_PATH)), 16);
		holder.name.setText(cursor.getString(cursor.getColumnIndex(SelfieContract.SELFIE_NAME)));
	}
}
