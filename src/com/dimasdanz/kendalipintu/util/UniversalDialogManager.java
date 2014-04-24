/*******************************************************************************
 * Copyright (c) 2014 Dimas Rullyan Danu
 * 
 * Kendali Pintu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Kendali Pintu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Kendali Pintu. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.dimasdanz.kendalipintu.util;

import com.dimasdanz.kendalipintu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class UniversalDialogManager extends DialogFragment{
	DialogManagerListener mListener;
	
	public interface DialogManagerListener{
		public void onDialogPositiveClick(DialogFragment dialog);
	}
	
	public static UniversalDialogManager newInstance(int type, String data){
		UniversalDialogManager f = new UniversalDialogManager();
		Bundle args = new Bundle();
		args.putInt("type", type);
		args.putString("data", data);
		f.setArguments(args);
		return f;
	}
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogManagerListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 4);
		builder.setIcon(android.R.drawable.ic_dialog_info);
    	builder.setTitle(R.string.string_confirmation);
		builder.setMessage(R.string.string_are_you_sure);
		builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		mListener.onDialogPositiveClick(UniversalDialogManager.this);
        	}
        });
		builder.setNeutralButton(R.string.close, null);
		return builder.create();
	}
}
