package com.svenkapudija.fancychart.data;

public class Point {
	
	public String title = "";
	public String subtitle = "";
	
	public boolean isSelected;
	public int x;
	public float y;
	
	public float canvasX;
	public float canvasY;
	
	public Point(int x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return x + ", " + y;
	}
	
}