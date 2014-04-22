package com.dimasdanz.kendalipintu.usermodel;

import java.util.List;

import com.dimasdanz.kendalipintu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<UserModel>{
	private List<UserModel> userList;
	private Context context;
	private int layoutId;

	public UserAdapter(Context context, List<UserModel> itemList, int layoutId) {
		super(context, layoutId, itemList);
		this.userList = itemList;
		this.context = context;
		this.layoutId = layoutId;
	}
	
	@Override
	public int getCount() {
		return userList.size() ;
	}

	@Override
	public UserModel getItem(int position) {		
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return userList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = convertView;
		
		final UserModel um = userList.get(position);
		
		if (result == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = inflater.inflate(layoutId, parent, false);
		}
		
		TextView userID = (TextView) result.findViewById(R.id.lblUserID);
		TextView userName = (TextView) result.findViewById(R.id.lblUserItem);
		TextView userPass = (TextView) result.findViewById(R.id.lblUserPass);
		userID.setText(um.getUserID());
		userName.setText(um.getName());
		userPass.setText(um.getPassword());
		
		return result;
	}
}
