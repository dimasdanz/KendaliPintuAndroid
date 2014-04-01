package com.dimasdanz.keamananpintu.util;

import java.util.ArrayList;

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

public class DialogManager extends DialogFragment {
	DialogManagerListener mListener;
	
	public interface DialogManagerListener {
        public void onDialogPositiveClick(DialogFragment dialog, ArrayList<String> al);
        public void onDialogNegativeClick(DialogFragment dialog, int value);
    }
	
	public static DialogManager newInstance(int dmNum, ArrayList<String> data) {
		DialogManager f = new DialogManager();
        Bundle args = new Bundle();
        args.putInt("dmNum", dmNum);
        args.putStringArrayList("data", data);
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
	
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int dmNum = getArguments().getInt("dmNum");
		final ArrayList<String> data = getArguments().getStringArrayList("data");
		
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 4);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        switch (dmNum) {
		case 0:
			builder.setIcon(android.R.drawable.ic_dialog_info);
        	builder.setTitle(R.string.title_dialog_server_error);
        	builder.setMessage(R.string.message_dialog_connection_error);
			break;
		case 1:
			final View view1 = inflater.inflate(R.layout.dialog_input, null);
			final EditText inputHostname = (EditText) view1.findViewById(R.id.txtInput);
			final TextView textHeader1 = (TextView) view1.findViewById(R.id.txtHeaderDialog);
			textHeader1.setText(R.string.hint_dialog_hostname_input);
			if(!data.isEmpty()){
				inputHostname.setText(data.get(0));
				inputHostname.setSelection(data.get(0).length());
			}
			builder.setIcon(android.R.drawable.ic_dialog_info);
        	builder.setTitle(R.string.string_hostname);
			builder.setView(view1);
			builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int id) {
            		ArrayList<String> al = new ArrayList<String>();
            		al.add(inputHostname.getText().toString());
            		mListener.onDialogPositiveClick(DialogManager.this, al);
            	}
            });
			break;
		case 2:
			final View view2 = inflater.inflate(R.layout.dialog_input, null);
			final EditText inputNumber = (EditText) view2.findViewById(R.id.txtInput);
			final TextView textHeader2 = (TextView) view2.findViewById(R.id.txtHeaderDialog);
			inputNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
			inputNumber.setHint(null);
			if(!data.isEmpty()){
				inputNumber.setText(data.get(0));
				inputNumber.setSelection(data.get(0).length());
			}
			textHeader2.setText(R.string.hint_dialog_attempt_input);
			builder.setIcon(android.R.drawable.ic_dialog_info);
        	builder.setTitle(R.string.string_attempt);
			builder.setView(view2);
			builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int id) {
            		ArrayList<String> al = new ArrayList<String>();
            		al.add(inputNumber.getText().toString());
            		mListener.onDialogPositiveClick(DialogManager.this, al);
            	}
            });
			break;
		case 3:
			final View view3 = inflater.inflate(R.layout.dialog_user_form, null);
			final TextView userid = (TextView) view3.findViewById(R.id.txtUserID);
			final EditText username = (EditText) view3.findViewById(R.id.txtUserName);
			final EditText password = (EditText) view3.findViewById(R.id.txtUserPass);
			final boolean edit;
			password.setInputType(InputType.TYPE_CLASS_NUMBER);
			if(!data.isEmpty()){
				edit = true;
				userid.setText(data.get(0));
				username.setText(data.get(1));
				username.setSelection(data.get(1).length());
				password.setText(data.get(2));
				password.setSelection(data.get(2).length());
			}else{
				edit = false;
			}
			builder.setIcon(android.R.drawable.ic_dialog_info);
        	builder.setTitle(R.string.string_attempt);
			builder.setView(view3);
			builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int id) {
            		ArrayList<String> al = new ArrayList<String>();
            		al.add(userid.getText().toString());
            		al.add(username.getText().toString());
            		al.add(password.getText().toString());
            		al.add(Boolean.toString(edit));
            		mListener.onDialogPositiveClick(DialogManager.this, al);
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
