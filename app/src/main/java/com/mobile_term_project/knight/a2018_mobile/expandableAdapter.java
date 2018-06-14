package com.mobile_term_project.knight.a2018_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @brief used for showing information of searched medicine
 * @author Joo wan Kim
 * @date 2018.05.18
 * @version 1.0.0.1
 */

public class expandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> arrayGroup;
    private HashMap<String, ArrayList<String>> arrayChild;

    /**
     * @brief constructor of expandable adapter
     * @param context context in Show_medicine_info
     * @param arrayGroup an array including subjects of each detail
     * @param arrayChild including details following each subject
     */
    public expandableAdapter(Context context, ArrayList<String> arrayGroup, HashMap<String, ArrayList<String>> arrayChild) {
        super();
        this.context = context;
        this.arrayGroup = arrayGroup;
        this.arrayChild = arrayChild;
    }

    /**
     * @brief  get group count
     * @return size of arrayGroup
     */
    @Override
    public int getGroupCount(){
        return arrayGroup.size();
    }

    /**
     * @brief get size of children from each group
     * @param groupPosition index in arrayGroup
     * @return size of groupPosition th arrayChild
     */
    @Override
    public int getChildrenCount(int groupPosition){
        return arrayChild.get(arrayGroup.get(groupPosition)).size();
    }

    /**
     * @brief get group item
     * @param groupPosition index of the item user wants
     * @return group item
     */
    @Override
    public Object getGroup(int groupPosition){
        return arrayGroup.get(groupPosition);
    }

    /**
     * @brief get child item
     * @param groupPosition index in arrayGroup
     * @param childPosition index in arrayChild
     * @return child item
     */
    @Override
    public Object getChild(int groupPosition, int childPosition){
        return arrayChild.get(arrayGroup.get(groupPosition)).get(childPosition);
    }

    /**
     * @brief get group id
     * @param groupPosition index of the item
     * @return index of group item
     */
    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    /**
     * @brief get child id
     * @param grouptPosition index in group
     * @param childPosition index of the item
     * @return index of the child item
     */
    @Override
    public long getChildId(int grouptPosition, int childPosition){
        return childPosition;
    }

    /**
     * @brief inflate each group view
     * @param groupPosition index of group item
     * @param isExpanded state of group item
     * @param convertView it would be returned
     * @param parent
     * @return converted view
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        String groupName = arrayGroup.get(groupPosition);
        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (LinearLayout) inflater.inflate(R.layout.listview_item_group, null);
        }
        TextView textGroup = (TextView) v.findViewById(R.id.textGroup);
        textGroup.setText(groupName);

        return v;
    }

    /**
     * @brief inflate each child in group
     * @param groupPosition index of group item
     * @param childPosition index of child item
     * @param isLastChild
     * @param convertView it would be returned
     * @param parent
     * @return converted view
     */
    @Override
    public View getChildView(int groupPosition,int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        String childName = arrayChild.get(arrayGroup.get(groupPosition)).get(childPosition);
        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (LinearLayout)inflater.inflate(R.layout.listview_item_child, null);
        }
        TextView textChild = (TextView) v.findViewById(R.id.textChild);
        textChild.setText(childName);

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition){
        return true;
    }

    @Override
    public boolean hasStableIds(){
        return false;
    }
}
