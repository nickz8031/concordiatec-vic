package com.concordiatec.vic.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.concordiatec.vic.R;
import com.concordiatec.vic.model.LocalSelectedCategory;
import com.concordiatec.vic.model.ShopGroup;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CategoryExpandAdapter implements ExpandableListAdapter {
	
	private Context context;
	private List<ShopGroup> groups;
	private List<List<ShopGroup>> childs;
	Map<Integer, View> groupViews= new HashMap<Integer, View>();
	Map<String, View> childViews= new HashMap<String, View>();
	
	public CategoryExpandAdapter(Context context , List<ShopGroup> groups, List<List<ShopGroup>> childs) {
		this.groups = groups;
		this.childs = childs;
		this.context = context;
	}
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int count = childs.get(groupPosition).size();
		if( count % 2 > 0 ){
			count = count/2 + 1;
		}else{
			count = count/2;
		}
		return count;
	}

	@Override
	public ShopGroup getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public List<ShopGroup> getChild(int groupPosition, int childPosition) {
		List<ShopGroup> tmp = new ArrayList<ShopGroup>();
		int realPos;
		if( childPosition > 1 ){
			realPos = childPosition + ( childPosition -1 );
		}else if( childPosition == 1 ){
			realPos = childPosition + 1;
		}else{
			realPos = 0;
		}
		tmp.add( childs.get(groupPosition).get(realPos) );
		if( realPos+1 < childs.get(groupPosition).size() ){
			tmp.add( childs.get(groupPosition).get(realPos+1) );
		}
		return tmp;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if( groupViews.get(groupPosition) == null ){
			ShopGroup groupData = getGroup(groupPosition);
			convertView = LayoutInflater.from(context).inflate(R.layout.ex_li_multi_category_group, null);
			RelativeLayout groupLayout = (RelativeLayout) convertView.findViewById(R.id.group_category);
			ImageView icon = (ImageView) convertView.findViewById(R.id.category_group_icon);
			TextView name = (TextView) convertView.findViewById(R.id.category_group_title);
			ImageView rightIcon = (ImageView) convertView.findViewById(R.id.category_right_icon);
			name.setText( groupData.getName() );
			
			if( childs.get(groupPosition).size() < 1 ){			
				
				if( isSelected( groupData.getId() ) ){
					changeSelectImage(rightIcon , true);
				}else{
					changeSelectImage(rightIcon , false);
				}
				groupLayout.setOnClickListener(new ChildClickListener( groupData , rightIcon));
			}else{
				if( isExpanded ){
					rightIcon.setImageResource( R.drawable.drop_up_24 );
				}else{
					rightIcon.setImageResource( R.drawable.drop_down_24 );
				}
			}
		}else{
			convertView = groupViews.get(groupPosition);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		String mapKey = groupPosition + "--"+childPosition;
		if( childViews.get(mapKey) == null ){
			List<ShopGroup> childData = getChild(groupPosition, childPosition);
			convertView = LayoutInflater.from(context).inflate(R.layout.ex_li_multi_category_child, null);
			RelativeLayout child_1 = (RelativeLayout) convertView.findViewById(R.id.child_1_layout);
			ImageView icon_1 = (ImageView) convertView.findViewById(R.id.category_child_1);
			TextView name_1 = (TextView) convertView.findViewById(R.id.category_child_1_title);
			ImageView rightIcon_1 = (ImageView) convertView.findViewById(R.id.child_1_right_icon);

			RelativeLayout child_2 = (RelativeLayout) convertView.findViewById(R.id.child_2_layout);
			ImageView icon_2 = (ImageView) convertView.findViewById(R.id.category_child_2);
			TextView name_2 = (TextView) convertView.findViewById(R.id.category_child_2_title);
			ImageView rightIcon_2 = (ImageView) convertView.findViewById(R.id.child_2_right_icon);
				
			
			name_1.setText( childData.get(0).getName() );
			child_1.setOnClickListener(new ChildClickListener( childData.get(0) , rightIcon_1));
			
			if( isSelected( childData.get(0).getId() ) ){
				changeSelectImage(rightIcon_1 , true);
			}else{
				changeSelectImage(rightIcon_1 , false);
			}
			
			
			if( childData.size() > 1 ){
				child_2.setVisibility(View.VISIBLE);
				name_2.setText( childData.get(1).getName() );
				child_2.setOnClickListener(new ChildClickListener(childData.get(1) ,rightIcon_2));
				if( isSelected( childData.get(1).getId() ) ){
					changeSelectImage(rightIcon_2 , true);
				}else{
					changeSelectImage(rightIcon_2 , false);
				}
			}else{
				child_2.setVisibility(View.INVISIBLE);
			}
		}else{
			convertView = childViews.get(mapKey);
		}
		return convertView;
	}
	/*--- Custom ---*/
	private class ChildClickListener implements OnClickListener{
		private ImageView iconView;
		private ShopGroup group;
		public ChildClickListener(ShopGroup group , ImageView iconView) {
			this.iconView = iconView;
			this.group = group;
		}
		@Override
		public void onClick(View v) {
			if( isSelected( group.getId() ) ){
				changeSelectImage(iconView , false);
				unSelect( group.getId() );
			}else{
				changeSelectImage(iconView , true);
				doSelect( group );
			}
		}
	}
	private boolean isSelected( int id ){
		return new Select().from(LocalSelectedCategory.class).where("selected_id = ?", id).executeSingle() != null ? true : false;
	}
	
	private void unSelect( int id ){
		new Delete().from(LocalSelectedCategory.class).where("selected_id = ?", id).execute();
	}
	private void doSelect( ShopGroup group ){
		if( !isSelected(group.getId()) ){
			LocalSelectedCategory category = new LocalSelectedCategory();
			category.selected_id = group.getId();
			category.name = group.getName();
			category.save();
		}
	}
	private void changeSelectImage( ImageView view , boolean isSelect ){
		if( isSelect ){
			view.setImageResource( R.drawable.ic_toggle_star_24 );
		}else{
			view.setImageResource( R.drawable.ic_toggle_star_24_gray );
		}
	}
	/*--- Custom end---*/
	
	
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		return groupId*1000000 + childId;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		return groupId*1000000;
	}
	
}
