package com.rationalstudio.foodland.Models;

public class CursorItem{
	private String date;
	private String image;
	private int userId;
	private String name;
	private int id;

	public CursorItem(String date, String image, int userId, String name, int id) {
		this.date = date;
		this.image = image;
		this.userId = userId;
		this.name = name;
		this.id = id;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
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
				"CursorItem{" +
						"date = '" + date + '\'' +
						",image = '" + image + '\'' +
						",user_id = '" + userId + '\'' +
						",name = '" + name + '\'' +
						",id = '" + id + '\'' +
						"}";
	}
}
