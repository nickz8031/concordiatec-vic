package com.concordiatec.vilnet.model;

import java.io.Serializable;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
@SuppressWarnings("serial")
@Table(name="selected_category")
public class LocalSelectedCategory extends Model implements Serializable {
	@Column(name="selected_id")
	public int selected_id;
	@Column(name="name")
	public String name;
	
	public LocalSelectedCategory() {
		super();
	}
	
}
