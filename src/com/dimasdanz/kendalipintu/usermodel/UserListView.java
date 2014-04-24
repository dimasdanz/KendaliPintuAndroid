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

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class UserListView extends ListView implements OnScrollListener{
	private View footer;
	private boolean isLoading;
	private boolean isLoaded;
	private EndlessListener listener;
	private UserAdapter adapter;
	
	
	public UserListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOnScrollListener(this);
	}
	
	public UserListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnScrollListener(this);
	}

	public UserListView(Context context) {
		super(context);
		this.setOnScrollListener(this);
	}
	
	public void setListener(EndlessListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (getAdapter() == null)
			return ;
		
		if (getAdapter().getCount() == 0)
			return ;
		
		int l = visibleItemCount + firstVisibleItem;
		if (l >= totalItemCount && !isLoading && !isLoaded) {
			this.addFooterView(footer);
			isLoading = true;
			listener.loadData();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}
	
	public void setLoadingView(int resId) {
		LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = (View) inflater.inflate(resId, null);
		this.addFooterView(footer);
	}

	public void setAdapter(UserAdapter adapter) {		
		super.setAdapter(adapter);
		this.adapter = adapter;
		this.removeFooterView(footer);
	}

	
	public void addNewData(List<UserModel> data) {
		this.removeFooterView(footer);
		if(data == null){
			isLoaded = true;
		}else{
			adapter.addAll(data);
			adapter.notifyDataSetChanged();
			isLoaded = false;
			isLoading = false;
		}
	}

	public EndlessListener setListener() {
		return listener;
	}


	public static interface EndlessListener {
		public void loadData();
	}
}
