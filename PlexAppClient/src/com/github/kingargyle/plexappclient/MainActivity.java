/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.kingargyle.plexappclient;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.kingargyle.plexapp.model.impl.*;

public class MainActivity extends Activity {

	private Gallery mainGallery;
	private View mainView;
	private MainMenuTextView preSelected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plex_app_main);
		
		mainView = findViewById(R.id.mainLayout);


		// Yes I know this is bad, really need to make network activity happen
		// in AsyncTask.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setupGallery();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_plex_app_main, menu);
		return true;
	}

	private void setupGallery() {

		mainGallery = (Gallery) findViewById(R.id.mainGalleryMenu);
		mainGallery.setAdapter(new MainMenuTextViewAdapter(this, mainView));
		mainGallery
				.setOnItemSelectedListener(new GalleryOnItemSelectedListener());
	}

	private class GalleryOnItemSelectedListener implements
			OnItemSelectedListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(
		 * android.widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			Bitmap bitmap = null;
			switch (position) {
			case 0: {
				bitmap = MainActivity
						.getBitmapFromURL("http://192.168.0.108:32400/:/resources/movie-fanart.jpg");
				break;
			}
			case 1: {
				bitmap = MainActivity
						.getBitmapFromURL("http://192.168.0.108:32400/:/resources/show-fanart.jpg");
				break;
			}
			default: {
				bitmap = MainActivity
						.getBitmapFromURL("http://192.168.0.108:32400/:/plugins/com.plexapp.plugins.pandora/resources/art-default.jpg");
			}
			}
			
			
			if (bitmap != null) {
				BitmapDrawable dbg = new BitmapDrawable(bitmap);
				mainView.setBackgroundDrawable(dbg);
				mainView.refreshDrawableState();
			}
			
			if (v instanceof MainMenuTextView) {
				MainMenuTextView tv = (MainMenuTextView) v;
				tv.setTextSize(tv.getTextSize() + 20);
				tv.setTypeface(null, Typeface.BOLD);
				if (preSelected != null) {
					preSelected.setTextSize(30);
					preSelected.refreshDrawableState();
					preSelected.setTypeface(null, Typeface.NORMAL);
				}
				preSelected = tv;
			}
				
			
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected
		 * (android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	public static Bitmap getBitmapFromURL(String src) {
		try {

			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap mybitmap = BitmapFactory.decodeStream(input);

			return mybitmap;

		} catch (Exception ex) {

			return null;
		}

	}

}
