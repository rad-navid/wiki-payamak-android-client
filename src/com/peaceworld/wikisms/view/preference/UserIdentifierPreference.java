package com.peaceworld.wikisms.view.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.peaceworld.wikisms.R;

public class UserIdentifierPreference extends DialogPreference implements
		DialogInterface.OnClickListener {

	// Keeps the font file paths and names in separate arrays

	// Font adaptor responsible for redrawing the item TextView with the
	// appropriate font.
	// We use BaseAdapter since we need both arrays, and the effort is quite
	// small.

	public UserIdentifierPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		super.onPrepareDialogBuilder(builder);

		String message=getContext().getResources().getString(R.string.user_identifier_preference_dialog);
		builder.setMessage(message);
		// The typical interaction for list-based dialogs is to have
		// click-on-an-item dismiss the dialog
		builder.setPositiveButton("",null);
		builder.setNegativeButton("",null);
		builder.setNeutralButton("Ok", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}

}