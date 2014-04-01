package com.dimasdanz.keamananpintu.usermodel;

import java.util.ArrayList;

import com.dimasdanz.keamananpintu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UserDialogManager extends DialogFragment{
	DialogManagerListener mListener;
	
	public interface DialogManagerListener {
        public void onDialogPositiveClick(DialogFragment dialog, ArrayList<String> data);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	public static UserDialogManager newInstance(ArrayList<String> data) {
		UserDialogManager f = new UserDialogManager();
        Bundle args = new Bundle();
        if(data != null){
        	args.putStringArrayList("data", data);
        }
        f.setArguments(args);
        return f;
    }
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogManagerListener) activity;
        } catch (ClassCastException e) {
        	e.toString();
        }
    }
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_user_form, null);
        final TextView txtUserID = (TextView)view.findViewById(R.id.txtUserID);
        final EditText txtUsername = (EditText)view.findViewById(R.id.txtUserName);
        final EditText txtUserPass = (EditText)view.findViewById(R.id.txtUserPass);
        int btnText = R.string.action_add_account;
        if(!getArguments().isEmpty()){
        	btnText = R.string.change;
        	txtUserID.setText(getArguments().getStringArrayList("data").get(0));
        	txtUsername.setText(getArguments().getStringArrayList("data").get(1));
        	txtUserPass.setText(getArguments().getStringArrayList("data").get(2));
        }
        
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 4);
		builder.setIcon(R.drawable.ic_action_group);
    	builder.setTitle(R.string.dialog_title_userform);
		builder.setView(view);
		builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		mListener.onDialogPositiveClick(UserDialogManager.this, null);
        	}
        });
		builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		mListener.onDialogNegativeClick(UserDialogManager.this);
        	}
        });
		builder.setNeutralButton(R.string.close, null);
		return builder.create();
	}
}
