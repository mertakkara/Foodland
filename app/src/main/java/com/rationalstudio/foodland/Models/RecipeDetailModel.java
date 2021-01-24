package com.rationalstudio.foodland.Models;

public class RecipeDetailModel{
	private String image;
	private String recip;
	private String name;
	private int id;
	public  RecipeDetailModel(String image,String recip,String name,int id){
		this.image=image;
		this.recip = recip;
		this.name = name;
		this.id = id;

	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setRecip(String recip){
		this.recip = recip;
	}

	public String getRecip(){
		return recip;
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
			"RecipeDetailModel{" + 
			"image = '" + image + '\'' + 
			",recip = '" + recip + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}
