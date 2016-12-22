package com.example.dailyselfie.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class SelfieContentProvider extends ContentProvider {

	private DatabaseHelper mDbHelper;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Selfies";

	private static final String CREATE_LOCATION_TABLE = " CREATE TABLE " + SelfieContract.SELFIES_TABLE_NAME + " ("
			+ SelfieContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SelfieContract.SELFIE_NAME
			+ " TEXT NOT NULL, " + SelfieContract.SELFIE_BITMAP_PATH + " TEXT NOT NULL);";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_LOCATION_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + SelfieContract.SELFIES_TABLE_NAME);
			onCreate(db);
		}
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new DatabaseHelper(getContext());
		return (mDbHelper != null);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(SelfieContract.SELFIES_TABLE_NAME);

		Cursor cursor = qb.query(mDbHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null,
				sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = mDbHelper.getWritableDatabase().insert(SelfieContract.SELFIES_TABLE_NAME, "", values);
		if (rowID > 0) {
			Uri fullUri = ContentUris.withAppendedId(SelfieContract.CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(fullUri, null);
			return fullUri;
		}
		throw new SQLException("Failed to add record into" + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsDeleted = mDbHelper.getWritableDatabase().delete(SelfieContract.SELFIES_TABLE_NAME, null, null);
		getContext().getContentResolver().notifyChange(SelfieContract.CONTENT_URI, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
