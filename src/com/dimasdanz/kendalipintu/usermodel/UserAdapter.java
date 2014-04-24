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
