package com.rationalstudio.foodland.Models;

import java.util.List;

public class FoodModel{
	private List<CursorItem> cursor;

	public void setCursor(List<CursorItem> cursor){
		this.cursor = cursor;
	}

	public List<CursorItem> getCursor(){
		return cursor;
	}

	@Override
 	public String toString(){
		return 
			"FoodModel{" + 
			"cursor = '" + cursor + '\'' + 
			"}";
		}
}