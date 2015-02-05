package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.List;
import com.concordiatec.vic.adapter.CategoryExpandAdapter;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.model.ShopGroup;
import com.concordiatec.vic.util.ProgressUtil;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class MultiCategoryActivity extends SubPageSherlockActivity {
	private ExpandableListView listView;
	private String[] groupStrs = {
			"음식점","카페/제과","은행",
			"공공기관","자동차","주유소",
			"마트/슈퍼","숙박","영화/공연장",
			"병원","약국","부동산",
			"나들이/문화","오락서비스","생활서비스",
			"교통","기타"};
	private String[][] childStrs = {
			{"한식","양식","일식","중식","치킨","패밀리레스토랑","패스트푸드","세계요리","분식","부페","도시락","배달음식","술집","기타"},
			{"카페/디저트","베이커리","키즈카페"},
			{},
			{"관공서","우체국"},
			{"차량정비","주차장","세차장"},
			{"주유소","LGP충전소"},
			{"마트","슈퍼","유기농마트","재래시장","편의점"},
			{"모텔","여관/여인숙","민박/게스트하우스","펜션","호텔","유스호스텔","콘도/리조트","캠핑장"},
			{"영화관","공연장"},
			{"치과","피부과","한의원","한방병원","흉부외과","내과","보건소","비뇨기과","산부인과","성형외과","소아과","신경외과","안과","외과","응급실","이비인후과","기타"},
			{},{},{},
			{"PC방","스크린골프","볼링장","멀티방","노래방","당구장","기타레포츠"},
			{"백화점/면세점","목욕/찜질방","헬스/체육시설","헤어샵","화장품","세탁소","보육시설","미용","안경점","애견샵","자전거","문구점","꽃집","철물점"},
			{"지하철역","버스정류장"},
			{}
	};
	private List<ShopGroup> groups;
	@Override
	public void onBackPressed() {
		if( ProgressUtil.isShowing() ){
			return;
		}
		super.onBackPressed();
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_category);
		setTitle(getString(R.string.category_select));
		listView = (ExpandableListView) findViewById(R.id.ex_list);
		groups = new ArrayList<ShopGroup>();
		List<List<ShopGroup>> childs = new ArrayList<List<ShopGroup>>();
		ShopGroup group;
		ShopGroup childGroup;
		List<ShopGroup> childsList;
		for (int i = 0; i < groupStrs.length; i++) {
			group = new ShopGroup();
			group.setId( i );
			group.setName( groupStrs[i] );
			groups.add(group);
			childsList = new ArrayList<ShopGroup>();
			for (int j = 0; j < childStrs[i].length; j++) {
				childGroup = new ShopGroup();
				childGroup.setId( i*10000 + j );
				childGroup.setName( childStrs[i][j] );
				childsList.add( childGroup );
			}
			
			childs.add( childsList );
		}
		
		
		CategoryExpandAdapter adapter = new CategoryExpandAdapter(this, groups, childs);
		listView.setAdapter(adapter);
		listView.setOnGroupExpandListener(new ExpandListExpandListener());
	}
	
	private final class ExpandListExpandListener implements OnGroupExpandListener{
		@Override
		public void onGroupExpand(int groupPosition) {
			for (int i = 0; i < groups.size(); i++) {  
                if (groupPosition != i) {  
                	listView.collapseGroup(i);  
                }  
            }  
		}
	}
	
	
}
