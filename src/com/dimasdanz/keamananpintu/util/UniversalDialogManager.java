package com.dimasdanz.keamananpintu.util;

import com.dimasdanz.keamananpintu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UniversalDialogManager extends DialogFragment{
	DialogManagerListener mListener;
	
	public interface DialogManagerListener{
		public void onDialogPositiveClick(DialogFragment dialog, String data);
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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_input, null);
		final TextView textHeader = (TextView) view.findViewById(R.id.txtHeaderDialog);
		final EditText textInput = (EditText) view.findViewById(R.id.txtInput);
		int title = R.string.string_hostname;
		switch (getArguments().getInt("type", 0)) {
		case 1:
			textHeader.setText(R.string.hint_dialog_attempt_input);
			textInput.setInputType(InputType.TYPE_CLASS_NUMBER);
			textInput.setHint(R.string.string_attempt);
			title = R.string.string_attempt;
			break;
		case 2:
			textHeader.setText(R.string.hint_dialog_hostname_input);
			break;
		default:
			break;
		}
		textInput.setText(getArguments().getString("data", null));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 4);
		builder.setIcon(android.R.drawable.ic_dialog_info);
    	builder.setTitle(title);
		builder.setView(view);
		builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		mListener.onDialogPositiveClick(UniversalDialogManager.this, textInput.getText().toString());
        	}
        });
		builder.setNeutralButton(R.string.close, null);
		return builder.create();
	}
}
