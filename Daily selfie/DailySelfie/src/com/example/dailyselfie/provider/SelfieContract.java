package com.example.dailyselfie.provider;

import android.net.Uri;

public class SelfieContract {
	public static final String AUTHORITY = "com.example.dailyselfie.provider";
	public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY + "/");

	public static final String SELFIES_TABLE_NAME = "selfies";

	// The URI for this table.
	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, SELFIES_TABLE_NAME);

	public static final String _ID = "_id";
	public static final String SELFIE_NAME = "selfieName";
	public static final String SELFIE_BITMAP_PATH = "selfieBitmapPath";

}
