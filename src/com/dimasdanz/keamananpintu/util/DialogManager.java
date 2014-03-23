package com.dimasdanz.keamananpintu.util;

import com.dimasdanz.keamananpintu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;

public class DialogManager extends DialogFragment {
	DialogManagerListener mListener;
	
	public interface DialogManagerListener {
        public void onDialogPositiveClick(DialogFragment dialog, String value);
        public void onDialogNegativeClick(DialogFragment dialog, int value);
    }
	
	public static DialogManager newInstance(int dmNum) {
		DialogManager f = new DialogManager();
        Bundle args = new Bundle();
        args.putInt("dmNum", dmNum);
        f.setArguments(args);
        return f;
    }
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogManagerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogManagerListener");
        }
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int dmNum = getArguments().getInt("dmNum");		
		
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (dmNum) {
		case 0:
			builder.setIcon(android.R.drawable.ic_dialog_info);
        	builder.setTitle(R.string.title_dialog_server_error);
        	builder.setMessage(R.string.message_dialog_connection_error);
			break;
		case 1:
			final EditText editText = new EditText(getActivity());
			editText.setText(null);
			builder.setIcon(android.R.drawable.ic_dialog_info);
        	builder.setTitle(R.string.string_hostname);
			builder.setView(editText);
			builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int id) {
            		mListener.onDialogPositiveClick(DialogManager.this, editText.getText().toString());
            	}
            });
			break;
		case 2:
			final EditText numberText = new EditText(getActivity());
			numberText.setText(null);
			numberText.setInputType(InputType.TYPE_CLASS_NUMBER);
			builder.setIcon(android.R.drawable.ic_dialog_info);
        	builder.setTitle(R.string.string_attempt);
			builder.setView(numberText);
			builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int id) {
            		mListener.onDialogPositiveClick(DialogManager.this, numberText.getText().toString());
            	}
            });
			break;
		default:
			break;
		}  		
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		mListener.onDialogNegativeClick(DialogManager.this, dmNum);
        	}
        });
        return builder.create();
    }
}
