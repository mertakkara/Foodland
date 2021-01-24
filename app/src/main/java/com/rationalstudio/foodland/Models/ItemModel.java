package com.rationalstudio.foodland.Models;

import java.util.List;

public class ItemModel{
	private List<ItemItem> item;

	public void setItem(List<ItemItem> item){
		this.item = item;
	}

	public List<ItemItem> getItem(){
		return item;
	}

	@Override
 	public String toString(){
		return 
			"ItemModel{" + 
			"item = '" + item + '\'' + 
			"}";
		}
}