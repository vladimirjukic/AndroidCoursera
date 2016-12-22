package com.example.dailyselfie;

public class SelfieRow {
	String name;
	String imagePath;

	public SelfieRow(String name, String imagePath) {
		this.name = name;
		this.imagePath = imagePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
