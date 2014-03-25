package com.dimasdanz.keamananpintu.logmodel;

import java.util.HashMap;
import java.util.List;

import com.dimasdanz.keamananpintu.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class LogExpandableListAdapter extends BaseExpandableListAdapter {
	private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<LogModel>> _listDataChild;
 
    public LogExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<LogModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }
 
    //Child View 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
 
        final LogModel lm = (LogModel)getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_log, null);
        }
 
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItemLog);
        TextView txtListDesc = (TextView) convertView.findViewById(R.id.lblListItemDetailLog);
 
        txtListChild.setText(lm.getName());
        txtListDesc.setText(lm.getTime());
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }
    //End of Child View
 
    //Group View
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_log, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeaderLog);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
    }
    //End of Group View
    
    //Utilities
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    public void refreshView(){
    	this.notifyDataSetChanged();
    }
}
