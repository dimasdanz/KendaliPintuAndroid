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
