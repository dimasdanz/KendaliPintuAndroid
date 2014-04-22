package com.dimasdanz.kendalipintu.usermodel;

import java.util.ArrayList;

import com.dimasdanz.kendalipintu.R;

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
import android.widget.Toast;

public class UserDialogManager extends DialogFragment{
	UserDialogManagerListener mListener;
	
	public interface UserDialogManagerListener {
        public void onDialogPositiveClick(DialogFragment dialog, ArrayList<String> data);
        public void onDialogNegativeClick(DialogFragment dialog, ArrayList<String> data);
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
            mListener = (UserDialogManagerListener) activity;
        } catch (ClassCastException e) {
        	e.toString();
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final ArrayList<String> userData = new ArrayList<String>();
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
			@Override
        	public void onClick(DialogInterface dialog, int id) {
        		if(txtUsername.getText().toString().length() > 0 || txtUserPass.getText().length() > 0){
        			userData.add(txtUserID.getText().toString());
            		userData.add(txtUsername.getText().toString());
            		userData.add(txtUserPass.getText().toString());
            		mListener.onDialogPositiveClick(UserDialogManager.this, userData);
        		}else{
        			//TODO Change string, use a better listener
        			Toast.makeText(getActivity(), "Please fill all field", Toast.LENGTH_SHORT).show();
        		}
        	}
        });
		builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
			@Override
        	public void onClick(DialogInterface dialog, int id) {
				if(txtUserID.getText().toString().length() > 0){
					userData.add(txtUserID.getText().toString());
	        		mListener.onDialogNegativeClick(UserDialogManager.this, userData);
				}else{
					//TODO Change string, use a better listener
					Toast.makeText(getActivity(), "Please fill all field", Toast.LENGTH_SHORT).show();
				}        		
        	}
        });
		builder.setNeutralButton(R.string.close, null);
		return builder.create();
	}
}
