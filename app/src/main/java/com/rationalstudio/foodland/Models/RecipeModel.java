package com.rationalstudio.foodland.Models;

public class RecipeModel{
	private String image;
	private String name;
	private int id;


	public RecipeModel( String image, String name, int id) {
		this.image = image;
		this.name = name;
		this.id = id;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
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
			"RecipeModel{" + 
			"image = '" + image + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}
