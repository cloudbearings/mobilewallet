package com.wordlypost;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.wordlypost.beans.NavDrawerItem;
import com.wordlypost.dao.CategoriesDAO;
import com.wordlypost.utils.Utils;

public class SettingsActivity extends ActionBarActivity {
	private static final String TAG = "SettingsActivity";
	LinearLayout linearMain;
	CheckBox checkBox;
	private CategoriesDAO categoriesDAO;

	@Override
	protected void onResume() {
		super.onResume();
		Utils.googleAnalaticsTracking(SettingsActivity.this,
				getString(R.string.settings_screen_name));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.settings_activity);
			linearMain = (LinearLayout) findViewById(R.id.linearMain);
			categoriesDAO = new CategoriesDAO(SettingsActivity.this);
			if (categoriesDAO.getCategoriesCount() > 0) {
				List<NavDrawerItem> items = categoriesDAO.getCategories();
				for (NavDrawerItem item : items) {
					checkBox = new CheckBox(this);
					checkBox.setId(item.getId());
					checkBox.setText(Html.fromHtml(item.getTitle()));
					if (item.getId() == getResources().getInteger(R.integer.top_news)) {
						checkBox.setEnabled(false);
					}
					if (item.getIsHomeCategory().equals(getString(R.string.Y))) {
						checkBox.setChecked(true);
					} else {
						checkBox.setChecked(false);
					}
					checkBox.setOnClickListener(addListenerOnCheckbox(checkBox));

					linearMain.addView(checkBox);
				}
			}
		} catch (Exception e) {
			Log.d(TAG, "Exception raised in onCreate()");
		}
	}

	private View.OnClickListener addListenerOnCheckbox(final CheckBox button) {
		return new View.OnClickListener() {
			public void onClick(View v) {

				if (button.isChecked()) {
					if (categoriesDAO.getHomeCategoriesCount() == 5) {
						button.setChecked(false);
						Utils.displayToad(SettingsActivity.this,
								getString(R.string.five_categories_error_msg));

					} else {
						updateHomeCategory(button.getId(), getString(R.string.Y));
					}
				} else {
					updateHomeCategory(button.getId(), getString(R.string.N));
				}
			}
		};
	}

	private void updateHomeCategory(int categoryId, String isHomeCategory) {
		long updated = categoriesDAO.updateIsHomeCategory(categoryId, isHomeCategory);
		if (updated > 0) {
			Log.i(TAG, "isHomeCategory column is updated in Categories table.");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent tabsIntent = new Intent(SettingsActivity.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
