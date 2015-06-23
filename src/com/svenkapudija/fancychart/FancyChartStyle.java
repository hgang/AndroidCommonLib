package com.svenkapudija.fancychart;

import android.graphics.Color;

public class FancyChartStyle {
	
	public String colorMain = "#ff9c00";

	private boolean drawBackgroundBelowLine;
	
	private int pointColor;
	
	public void setPointColor(int pointColor) {
		this.pointColor = pointColor;
	}

	private int horizontalGridColor;
	private int verticalGridColor;
	
	private int xAxisLegendColor;
	private int yAxisLegendColor;
	
	private int boxColor;

	private int pointRadius;
	private int pointStrokeWidth;
	
	private int legendTextSize;
	private int boxTextSize;
	private int boxTextColor;
	
	private int dataLineWidth;
	private int gridLineWidth;
	private int selectedBoxStrokeWidth;
	
	private int chartPaddingLeft;
	private int chartPaddingRight;
	private int chartPaddingTop;
	private int chartPaddingBottom;
	
	private int legendCircleWidth;
	private int selectedColor;
	private int selectedLineWidth;
	private int selectedTextColor;
	private int legendCricleRadius;
	
	public int getLegendCricleRadius() {
		return legendCricleRadius;
	}
	public void setLegendCricleRadius(int legendCricleRadius) {
		this.legendCricleRadius = legendCricleRadius;
	}
	public int getSelectedTextColor() {
		return selectedTextColor;
	}
	public void setSelectedTextColor(int selectedTextColor) {
		this.selectedTextColor = selectedTextColor;
	}
	public FancyChartStyle() {
//		f7c77a ,f1e2ca
		horizontalGridColor = Color.parseColor("#7fffffff");//网格线
		verticalGridColor = horizontalGridColor;
		
		xAxisLegendColor = Color.parseColor("#ffffff");//x,y轴
		yAxisLegendColor = xAxisLegendColor;
		
		boxColor = Color.parseColor("#ff0000");
		
		boxTextColor = Color.parseColor("#0000ff");

		pointColor = Color.parseColor(colorMain);//原点内部颜色
		
		pointRadius = 9;
		pointStrokeWidth = 3;
		
		legendTextSize = 24;
		boxTextSize = 14;
		
		dataLineWidth = 2;//线宽度
		gridLineWidth = 1;//网格线宽度
		selectedBoxStrokeWidth = 3;
		
		chartPaddingLeft = 20;
		chartPaddingRight = 40;
		chartPaddingTop = 20;
		chartPaddingBottom = 20;
		
		legendCircleWidth = 3;//底部字体圆形宽度
		selectedColor = Color.parseColor("#ffffff");//选中线的颜色
		selectedLineWidth = 4;
		selectedTextColor = Color.parseColor(colorMain);
		legendCricleRadius = 20;
		
//		drawBackgroundBelowLine = true; //画线的同时也涂满其背景
	}
	public int getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(int selectedColor) {
		this.selectedColor = selectedColor;
	}

	public int getSelectedLineWidth() {
		return selectedLineWidth;
	}

	public void setSelectedLineWidth(int selectedLineWidth) {
		this.selectedLineWidth = selectedLineWidth;
	}

	public int getLegendCircleWidth() {
		return legendCircleWidth;
	}

	public void setLegendCircleWidth(int legendCircleWidth) {
		this.legendCircleWidth = legendCircleWidth;
	}

	public boolean drawBackgroundBelowLine() {
		return drawBackgroundBelowLine;
	}
	
	public void setDrawBackgroundBelowLine(boolean drawBackgroundBelowLine) {
		this.drawBackgroundBelowLine = drawBackgroundBelowLine;
	}
	
	public void setBoxTextColor(int boxTextColor) {
		this.boxTextColor = boxTextColor;
	}
	
	public int getBoxTextColor() {
		return boxTextColor;
	}
	
	public int getBoxTextSize() {
		return boxTextSize;
	}
	
	public void setBoxTextSize(int boxTextSize) {
		this.boxTextSize = boxTextSize;
	}
	
	public int getPointStrokeWidth() {
		return pointStrokeWidth;
	}
	
	public void setPointStrokeWidth(int pointStrokeWidth) {
		this.pointStrokeWidth = pointStrokeWidth;
	}
	
	public int getChartPaddingBottom() {
		return chartPaddingBottom;
	}
	
	public int getChartPaddingLeft() {
		return chartPaddingLeft;
	}
	
	public int getChartPaddingRight() {
		return chartPaddingRight;
	}
	
	public int getChartPaddingTop() {
		return chartPaddingTop;
	}
	
	public int getSelectedBoxStrokeWidth() {
		return selectedBoxStrokeWidth;
	}
	
	public void setSelectedBoxStrokeWidth(int selectedBoxStrokeWidth) {
		this.selectedBoxStrokeWidth = selectedBoxStrokeWidth;
	}
	
	public int getGridLineWidth() {
		return gridLineWidth;
	}
	
	public void setGridLineWidth(int gridLineWidth) {
		this.gridLineWidth = gridLineWidth;
	}
	
	public int getDataLineWidth() {
		return dataLineWidth;
	}
	
	public void setDataLineWidth(int dataLineWidth) {
		this.dataLineWidth = dataLineWidth;
	}
	
	public void setBackgroundColor(int backgroundColor) {
		this.pointColor = backgroundColor;
	}

	public void setHorizontalGridColor(int horizontalGridColor) {
		this.horizontalGridColor = horizontalGridColor;
	}

	public void setVerticalGridColor(int verticalGridColor) {
		this.verticalGridColor = verticalGridColor;
	}

	public void setxAxisLegendColor(int xAxisLegendColor) {
		this.xAxisLegendColor = xAxisLegendColor;
	}

	public void setyAxisLegendColor(int yAxisLegendColor) {
		this.yAxisLegendColor = yAxisLegendColor;
	}

	public void setBoxColor(int boxColor) {
		this.boxColor = boxColor;
	}

	public void setLegendTextSize(int legendTextSize) {
		this.legendTextSize = legendTextSize;
	}

	public void setPointRadius(int pointRadius) {
		this.pointRadius = pointRadius;
	}

	public int getPointRadius() {
		return pointRadius;
	}

	public int getLegendTextSize() {
		return legendTextSize;
	}
	
	public int getHorizontalGridColor() {
		return horizontalGridColor;
	}

	public int getVerticalGridColor() {
		return verticalGridColor;
	}

	public int getxAxisLegendColor() {
		return xAxisLegendColor;
	}

	public int getyAxisLegendColor() {
		return yAxisLegendColor;
	}

	public int getBoxColor() {
		return boxColor;
	}
	
	public int getPointColor() {
		return pointColor;
	}
	
}
