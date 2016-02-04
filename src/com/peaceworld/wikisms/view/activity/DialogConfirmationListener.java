package com.peaceworld.wikisms.view.activity;

import com.peaceworld.wikisms.view.newui.dialog.SimpleOneInputDialog.TYPE;

public interface DialogConfirmationListener {
	
	public void actionPerformed(boolean confirmed, TYPE actionType);

}
