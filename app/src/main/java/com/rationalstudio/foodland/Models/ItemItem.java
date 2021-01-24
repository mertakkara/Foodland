package com.rationalstudio.foodland.Models;

public class ItemItem{
	private String item;
	private int id;


	public ItemItem(String item, int id) {
		this.item = item;
		this.id = id;
	}


	public void setItem(String item){
		this.item = item;
	}

	public String getItem(){
		return item;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"ItemItem{" + 
			"item = '" + item + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}
