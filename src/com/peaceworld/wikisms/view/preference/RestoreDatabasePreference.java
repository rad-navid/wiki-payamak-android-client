package com.peaceworld.wikisms.view.preference;

import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.model.dao.WikiSmsDataBase;
import com.peaceworld.wikisms.model.dao.Wikisms_dbFactory;
import com.turbomanage.storm.DatabaseHelper;

public class RestoreDatabasePreference extends DialogPreference implements
		DialogInterface.OnClickListener {

	// Keeps the font file paths and names in separate arrays
	private List<String> m_fontNames;

	// Font adaptor responsible for redrawing the item TextView with the
	// appropriate font.
	// We use BaseAdapter since we need both arrays, and the effort is quite
	// small.

	public RestoreDatabasePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		super.onPrepareDialogBuilder(builder);

		String message=getContext().getResources().getString(R.string.restore_database_setting_preference_dialog);
		builder.setMessage(message);
		// The typical interaction for list-based dialogs is to have
		// click-on-an-item dismiss the dialog
		builder.setPositiveButton("تایید", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try{
					DatabaseHelper db=Wikisms_dbFactory.getDatabaseHelper(getContext());
					db.dropAndCreate(db.getWritableDatabase());
					WikiSmsDataBase.pushDB(getContext(), true);
					
					SharedPreferences preferences = getContext().getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = preferences.edit();
					editor.clear();
					editor.commit();
					dialog.dismiss();
					
					Toast.makeText(getContext(), "با موفقیت انجام شد", Toast.LENGTH_LONG).show();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					Toast.makeText(getContext(), "انجام نشد، لطفا بعدا مجددا تلاش کنید", Toast.LENGTH_LONG).show();
				}
			}
		});
		builder.setNegativeButton("لغو", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which >= 0 && which < m_fontNames.size()) {
			String selectedFontPath = m_fontNames.get(which);
			Editor editor = getSharedPreferences().edit();
			editor.putString(getKey(), selectedFontPath);
			editor.commit();

			dialog.dismiss();
		}
	}
}