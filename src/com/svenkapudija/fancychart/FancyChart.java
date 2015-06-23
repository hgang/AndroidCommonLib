package com.svenkapudija.fancychart;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.svenkapudija.fancychart.data.AxisValue;
import com.svenkapudija.fancychart.data.ChartData;
import com.svenkapudija.fancychart.data.Point;
public class FancyChart extends View {

	private static final String TAG = FancyChart.class.getName();

	private static int VERTICAL_LEGEND_WIDTH = 30;//左边字体宽度
	private static int VERTICAL_LEGEND_MARGIN_RIGHT = 20;//左边字体到右边图表的距离
	
	private static int HORIZONTAL_LEGEND_HEIGHT = 10;//底部字体高度
	private static int HORIZONTAL_LEGEND_MARGIN_TOP = 40;//地步字体到上边图表的距离
	
	private static int TOUCH_THRESHOLD = 20;

	private FancyChartStyle chartStyle;
	private FancyChartPointListener onPointClickListener;
	
	private List<ChartData> chartData;
	private int selectedIndex = 0;
	private List<PointF> bottomPoints;
	private int maxY = 200;
	
	public FancyChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.chartData = new ArrayList<ChartData>();
		this.chartStyle = new FancyChartStyle();
		bottomPoints = new ArrayList<PointF>();
	}
	
	public void addData(ChartData data) {
		if(data.getXValues().size() == 0 && data.getPoints().size() > 0) {
			data.automaticallyAddXValues();
		}
		
		if(data.getYValues().size() == 0 && data.getPoints().size() > 0) {
			data.automaticallyAddYValues();
		}
		
		chartData.add(data);
	}
	
	public void setOnPointClickListener(FancyChartPointListener onPointClickListener) {
		this.onPointClickListener = onPointClickListener;
	}
	
	public FancyChartStyle getChartStyle() {
		return chartStyle;
	}
	
	public List<ChartData> getChartData() {
		return chartData;
	}
	
	public ChartData getLastChartData() {
		return chartData.get(chartData.size()-1);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		drawVerticalLinesAndLegend(canvas);
		drawHorizontalLinesAndLegend(canvas);
		calculateCanvasCoordinates();
		
		if(chartStyle.drawBackgroundBelowLine()) {
			for(ChartData data : chartData) {
				int minCanvasX = VERTICAL_LEGEND_WIDTH + VERTICAL_LEGEND_MARGIN_RIGHT + chartStyle.getChartPaddingLeft();
				int minCanvasY = getHeight()-HORIZONTAL_LEGEND_HEIGHT-HORIZONTAL_LEGEND_MARGIN_TOP-chartStyle.getChartPaddingBottom();
				
				int maxCanvasX = getWidth()-chartStyle.getChartPaddingRight();
				
				Path path = new Path();
				path.moveTo(minCanvasX, minCanvasY);
				
				List<Point> points = data.getPoints();
				for(Point point : points) {
					path.lineTo(point.canvasX, point.canvasY);
				}
				
				path.lineTo(maxCanvasX, minCanvasY);
				path.lineTo(minCanvasX, minCanvasY);
				path.close();
				
				Paint paintBelowLine = new Paint();
				paintBelowLine.setColor(data.getBelowLineColor());
				paintBelowLine.setStyle(Paint.Style.FILL);
				paintBelowLine.setAntiAlias(true);
				
				canvas.drawPath(path, paintBelowLine);
			}
		}
		
		Paint backgroundColorPaint = new Paint();
		backgroundColorPaint.setColor(chartStyle.getPointColor());
		backgroundColorPaint.setStyle(Paint.Style.FILL);
		backgroundColorPaint.setAntiAlias(true);
		
		Paint paintLine = new Paint();
		paintLine.setStyle(Paint.Style.FILL);
		paintLine.setStrokeWidth(chartStyle.getDataLineWidth());
		paintLine.setAntiAlias(true);
		for(int i=0;i<chartData.size();i++){
			ChartData data = chartData.get(i);
			Paint paintSelectedBorder = new Paint();
			paintSelectedBorder.setColor(data.getBelowLineColor());
			paintSelectedBorder.setStrokeWidth(chartStyle.getSelectedBoxStrokeWidth());
			paintSelectedBorder.setStyle(Paint.Style.FILL_AND_STROKE);
			paintSelectedBorder.setAntiAlias(true);
			
			paintLine.setColor(data.getLineColor());
			List<Point> points = data.getPoints();
			
			//画点之间的线
			drawLinesBetweenPoints(points, canvas, paintLine);
			//画原点
			for(int j=0;j<points.size();j++){
				Point point = points.get(j);
				if (j == selectedIndex) {
					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius()+chartStyle.getPointStrokeWidth(), paintSelectedBorder);
					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius(), paintLine);
				}else{
					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius(), paintLine);
					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius()-chartStyle.getPointStrokeWidth(), backgroundColorPaint);
				}
//				if(point.isSelected) {
//					// Draw selected point
//					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius()+chartStyle.getPointStrokeWidth(), paintSelectedBorder);
//					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius(), paintLine);
//					drawPopupBox(canvas, point, data);
//				} else {
//					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius(), paintLine);
//					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius()-chartStyle.getPointStrokeWidth(), backgroundColorPaint);
//				}
			}
		}
	}

	private void drawPopupBox(Canvas canvas, Point point, ChartData data) {
		String title = point.title;
		String subtitle = point.subtitle;
		if(title == null || subtitle == null) {
			return;
		}
		
		Paint paintSelectedBorder = new Paint();
		paintSelectedBorder.setColor(data.getBelowLineColor());
		paintSelectedBorder.setStrokeWidth(chartStyle.getSelectedBoxStrokeWidth());
		paintSelectedBorder.setStyle(Paint.Style.FILL_AND_STROKE);
		paintSelectedBorder.setAntiAlias(true);
		
		int maxTextRowLength = Math.max(title.length(), subtitle.length());
		
		// TODO: Crashes on smaller text lengths
		int rWidth = (int) (maxTextRowLength*(chartStyle.getBoxTextSize()/1.75));
		int rHeight = (int) (chartStyle.getBoxTextSize()*1.78*2);
		
		RectF borderRectangle = new RectF(point.canvasX-(rWidth/2), point.canvasY-(rHeight/2)-chartStyle.getPointRadius()*6, point.canvasX+(rWidth/2), point.canvasY+(rHeight/2)-chartStyle.getPointRadius()*6);
		canvas.drawRoundRect(borderRectangle, 5, 5, paintSelectedBorder);
		
		Paint rectangle = new Paint();
		rectangle.setColor(data.getBelowLineColor());
		rectangle.setStyle(Paint.Style.FILL);
		rectangle.setAntiAlias(true);
		
		RectF rInside = new RectF(borderRectangle.left+chartStyle.getSelectedBoxStrokeWidth(), borderRectangle.top+chartStyle.getSelectedBoxStrokeWidth(), borderRectangle.right-chartStyle.getSelectedBoxStrokeWidth(), borderRectangle.bottom-chartStyle.getSelectedBoxStrokeWidth());
		canvas.drawRoundRect(rInside, 5, 5, rectangle);
		
		drawTextInsideBox(canvas, title, subtitle, rInside);
		drawTriangleAtBottom(canvas, borderRectangle, rectangle, data);
	}

	private void drawTriangleAtBottom(Canvas canvas, RectF borderRectangle, Paint rectangle, ChartData data) {
		Paint paintSelectedBorder = new Paint();
		paintSelectedBorder.setColor(data.getBelowLineColor());
		paintSelectedBorder.setStrokeWidth(chartStyle.getSelectedBoxStrokeWidth());
		paintSelectedBorder.setStyle(Paint.Style.FILL_AND_STROKE);
		paintSelectedBorder.setAntiAlias(true);
		
		int triangleSideSize = 10;
		
		float centerX = (borderRectangle.right-borderRectangle.left)/2+borderRectangle.left;
		float bottomY = (borderRectangle.bottom);
		
		Path triangleBorder = new Path();
		triangleBorder.moveTo(centerX-triangleSideSize, bottomY);
		triangleBorder.lineTo(centerX+triangleSideSize, bottomY);
		triangleBorder.lineTo(centerX, bottomY+triangleSideSize);
		triangleBorder.lineTo(centerX-triangleSideSize, bottomY);
		triangleBorder.close();
		canvas.drawPath(triangleBorder, paintSelectedBorder);
		
		int triangleSideSizeSmall = 10-chartStyle.getSelectedBoxStrokeWidth()+1;
		float centerXSmall = (borderRectangle.right-borderRectangle.left)/2+borderRectangle.left;
		float bottomYSmall = (borderRectangle.bottom)-chartStyle.getSelectedBoxStrokeWidth();
		
		Path triangle = new Path();
		triangle.moveTo(centerXSmall-triangleSideSizeSmall, bottomYSmall);
		triangle.lineTo(centerXSmall+triangleSideSizeSmall, bottomYSmall);
		triangle.lineTo(centerXSmall, bottomYSmall+triangleSideSizeSmall);
		triangle.lineTo(centerXSmall-triangleSideSizeSmall, bottomYSmall);
		triangle.close();
		canvas.drawPath(triangle, rectangle);
	}

	private void drawTextInsideBox(Canvas canvas, String title, String subtitle, RectF rInside) {
		Paint textPaint = new Paint();
		textPaint.setColor(chartStyle.getBoxTextColor());
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(chartStyle.getBoxTextSize());
		
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(title, rInside.left + 7, rInside.top + 20, textPaint);
		textPaint.setTypeface(Typeface.DEFAULT);
		canvas.drawText(subtitle, rInside.left + 7, rInside.top + 20 + 15, textPaint);
	}

	private void drawLinesBetweenPoints(List<Point> points, Canvas canvas, Paint paintLine) {
		for(int i = 0; i < points.size()-1; i++) {
			Point startPoint = points.get(i);
			Point endPoint = points.get(i+1);
			
			canvas.drawLine(startPoint.canvasX, startPoint.canvasY, endPoint.canvasX, endPoint.canvasY, paintLine);
		}
	}
	
	private void calculateCanvasCoordinates() {
		int minCanvasX = VERTICAL_LEGEND_WIDTH + VERTICAL_LEGEND_MARGIN_RIGHT + chartStyle.getChartPaddingLeft();
		int maxCanvasX = getWidth()-chartStyle.getChartPaddingRight();
		
		int minCanvasY = chartStyle.getChartPaddingTop();
		int maxCanvasY = getHeight()-HORIZONTAL_LEGEND_HEIGHT-HORIZONTAL_LEGEND_MARGIN_TOP-chartStyle.getChartPaddingBottom();
		for(ChartData data : chartData) {
			List<Point> points = data.getPoints();
			for(Point point : points) {
				float newXValue = transformTo(getMinX(), getMaxX(), minCanvasX, maxCanvasX, point.x);
//				float newYValue = (maxCanvasY+chartStyle.getChartPaddingBottom())-transformTo(getMinY(), getMaxY(), minCanvasY, maxCanvasY, point.y);
				float newYValue = transformY(maxCanvasY,minCanvasY,point.y);
				point.canvasX = newXValue;
				point.canvasY = newYValue;
			}
		}
	}
	
	private float transformY(int canvasMax, int canvasMin, float y) {
		canvasMin = canvasMin+20;
		return canvasMax-(canvasMax-canvasMin)*y/maxY;
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			float x = event.getX();
			float y = event.getY();
			
//			boolean selected = false;
			Log.e("hegang", "bottomPoints.size() = "+bottomPoints.size());
			for(int i=0;i<bottomPoints.size();i++){
				PointF point = bottomPoints.get(i);
				float minX = point.x-TOUCH_THRESHOLD;
				float maxX = point.x+TOUCH_THRESHOLD;
				float minY = point.y-TOUCH_THRESHOLD;
				float maxY = point.y+TOUCH_THRESHOLD;
				if((x > minX && x < maxX) && (y > minY && y < maxY)){
					selectedIndex = i;
					Log.e("hegang", "selectedIndex = "+selectedIndex);
					if(onPointClickListener != null) {
						onPointClickListener.onClick(selectedIndex);
					}
					invalidate();
				}
			}
//			for(ChartData data : chartData) {
//				List<Point> points = data.getPoints();
//				
//				for(Point point : points) {
//					float minX = point.canvasX-TOUCH_THRESHOLD;
//					float maxX = point.canvasX+TOUCH_THRESHOLD;
//					
//					float minY = point.canvasY-TOUCH_THRESHOLD;
//					float maxY = point.canvasY+TOUCH_THRESHOLD;
//					
//					if((x > minX && x < maxX) && (y > minY && y < maxY)) {
//						// Point pressed!
//						if(point.isSelected) {
//							point.isSelected = false;
//						} else if(selected == false) {
//							selected = true;
//							point.isSelected = true;
//							
//							
//						}
//						
//						invalidate();
//					} else {
//						point.isSelected = false;
//					}
//				}
//			}
		}
		
		return true;
	}
	
	private double getMinX() {
		double minX = -1;
		
		for(ChartData data : chartData) {
			if(minX == -1 || Double.compare(minX, data.getMinX()) < 0) {
				minX = data.getMinX();
			}
		}
		
		return minX == -1 ? 0 : minX;
	}
	
	private double getMaxX() {
		double maxX = 0;
		
		for(ChartData data : chartData) {
			if(Double.compare(data.getMaxX(), maxX) > 0) {
				maxX = data.getMaxX();
			}
		}
		
		return maxX;
	}
	
	private double getMinY() {
		double minY = -1;
		
		for(ChartData data : chartData) {
			if(minY == -1 || Double.compare(minY, data.getMinY()) < 0) {
				minY = data.getMinY();
			}
		}
		
		return minY == -1 ? 0 : minY;
	}
	
	public void setMaxY(int maxY){
		this.maxY = maxY;
	}
	private double getMaxY() {
//		double maxY = 0;
//		
//		for(ChartData data : chartData) {
//			if(Double.compare(data.getMaxY(), maxY) > 0) {
//				maxY = data.getMaxY();
//			}
//		}
//		
//		return maxY;
		return maxY;
	}
	
	private void drawHorizontalLinesAndLegend(Canvas canvas) {
		int canvasHeight = canvas.getHeight()-HORIZONTAL_LEGEND_HEIGHT-HORIZONTAL_LEGEND_MARGIN_TOP;
		int canvasMin = chartStyle.getChartPaddingTop();
		int canvasMax = canvasHeight-chartStyle.getChartPaddingBottom();
		
		Paint paint = new Paint();
		paint.setColor(chartStyle.getHorizontalGridColor());
		paint.setStrokeWidth(chartStyle.getGridLineWidth());
		
		Paint paintLegend = new Paint();
		paintLegend.setColor(chartStyle.getyAxisLegendColor());
		paintLegend.setStyle(Paint.Style.FILL);
		paintLegend.setTextSize(chartStyle.getLegendTextSize());
		paintLegend.setAntiAlias(true);
		
		double min = getMinY();
		double max = getMaxY();
		
		int size = 10;//10根线
		int tep = maxY/size;
		float step = ((canvasMax-canvasMin)-tep)/size;
		//画十根线
		for(int i=0;i<size+1;i++){
			float y = canvasMax-i*step;
			canvas.drawLine(VERTICAL_LEGEND_WIDTH + VERTICAL_LEGEND_MARGIN_RIGHT+chartStyle.getChartPaddingLeft(), y, getWidth()-chartStyle.getChartPaddingRight(), y, paint);
			String text = String.valueOf(i*tep);
			canvas.drawText(text, 20 - text.length()*6, y+5, paintLegend);
		}
//		List<AxisValue> yValues = new ArrayList<AxisValue>();
//		//获取y轴上最大的集合
//		for(ChartData data : chartData) {
//			if(yValues.size() == 0 || Double.compare(data.getMaxY(), yValues.get(yValues.size()-1).value) > 0) {
//				yValues = data.getYValues();
//			}
//		}
//		
//		List<AxisValue> reduced = new ArrayList<AxisValue>();
//		if(yValues.size() > 10) {
//			int step = yValues.size()/8;
//			
//			for(int i = 0; i < yValues.size(); i += step) {
//				reduced.add(yValues.get(i));
//			}
//			
//			min = reduced.get(0).value;
//			max = reduced.get(reduced.size()-1).value;
//		} else {
//			reduced = yValues;
//		}
//		
//		for(AxisValue value : reduced) {
//			float y = canvasHeight-transformTo(min, max, canvasMin, canvasMax, value.value);
//			canvas.drawLine(VERTICAL_LEGEND_WIDTH + VERTICAL_LEGEND_MARGIN_RIGHT+chartStyle.getChartPaddingLeft(), y, getWidth()-chartStyle.getChartPaddingRight(), y, paint);
//			
//			if(value.title != null) {
//				int length = value.title.length();
//				canvas.drawText(value.title, 20 - length*6, y+5, paintLegend);
//			}
//		}
	}
	
	private void drawVerticalLinesAndLegend(Canvas canvas) {
		double min = getMinX();
		double max = getMaxX();
		
		int canvasWidth = canvas.getWidth();
		int canvasMin = VERTICAL_LEGEND_WIDTH + VERTICAL_LEGEND_MARGIN_RIGHT + chartStyle.getChartPaddingLeft();
		int canvasMax = canvasWidth-chartStyle.getChartPaddingRight();
		
		Paint paint = new Paint();
		paint.setColor(chartStyle.getVerticalGridColor());
		paint.setStrokeWidth(chartStyle.getGridLineWidth());
		Paint circlePaint = new Paint();
		circlePaint.setColor(chartStyle.getVerticalGridColor());
		circlePaint.setStrokeWidth(chartStyle.getLegendCircleWidth());
		circlePaint.setStyle(Style.STROKE);
		circlePaint.setAntiAlias(true);
		
		Paint paintLegend = new Paint();
		paintLegend.setColor(chartStyle.getxAxisLegendColor());
		paintLegend.setStyle(Paint.Style.FILL);
		paintLegend.setTextSize(chartStyle.getLegendTextSize());
		paintLegend.setAntiAlias(true);
		
		List<AxisValue> xValues = new ArrayList<AxisValue>();
		//获取x轴上最大的集合
		for(ChartData data : chartData) {
			if(xValues.size() == 0 || Double.compare(data.getMaxX(), xValues.get(xValues.size()-1).value) > 0) {
				xValues = data.getXValues();
			}
		}
		boolean empty = bottomPoints.isEmpty();
		for(int i=0;i<xValues.size();i++){
			AxisValue value = xValues.get(i);
			float x = transformTo(min, max, canvasMin, canvasMax, value.value);
			if (i == selectedIndex) {
				paint.setColor(chartStyle.getSelectedColor());
				paint.setStrokeWidth(chartStyle.getSelectedLineWidth());
			} else {
				paint.setColor(chartStyle.getVerticalGridColor());
				paint.setStrokeWidth(chartStyle.getGridLineWidth());
			}
			float y = getHeight()-HORIZONTAL_LEGEND_HEIGHT-HORIZONTAL_LEGEND_MARGIN_TOP-chartStyle.getChartPaddingBottom();
			canvas.drawLine(x, 0, x, y, paint);
			if(value.title != null) {
				int length = value.title.length();
				if (i == selectedIndex){
					circlePaint.setStyle(Style.FILL);
					circlePaint.setColor(chartStyle.getSelectedColor());
					paintLegend.setColor(chartStyle.getSelectedTextColor());
				}else{
					circlePaint.setColor(chartStyle.getVerticalGridColor());
					circlePaint.setStyle(Style.STROKE);
					paintLegend.setColor(chartStyle.getxAxisLegendColor());
				}
				if(empty){
					bottomPoints.add(new PointF(x, y+chartStyle.getChartPaddingBottom()+chartStyle.getLegendCricleRadius()));
				}
				canvas.drawCircle(x, y+chartStyle.getChartPaddingBottom()+chartStyle.getLegendCricleRadius(), chartStyle.getLegendCricleRadius(), circlePaint);
//				canvas.drawCircle(x-chartStyle.getLegendTextSize()/2+length*chartStyle.getLegendTextSize()/2, getHeight()-chartStyle.getLegendTextSize(), 20, circlePaint);
//				canvas.drawText(value.title, x/*-length*6/2*/-chartStyle.getLegendTextSize()/2, getHeight()-chartStyle.getLegendTextSize()/2, paintLegend);
				canvas.drawText(value.title, x-length*chartStyle.getLegendTextSize()/3, y+chartStyle.getChartPaddingBottom()+chartStyle.getLegendCricleRadius()+chartStyle.getLegendTextSize()/3, paintLegend);
			}
		}
	}
	
	private float transformTo(double oldMin, double oldMax, int newMin, int newMax, double oldValue) {
		double oldRange = (oldMax - oldMin);
		if (oldRange == 0.0) {
			return VERTICAL_LEGEND_WIDTH + VERTICAL_LEGEND_MARGIN_RIGHT + chartStyle.getChartPaddingLeft();
		}
		double newRange = (newMax - newMin);
		double newValue = (((oldValue - oldMin) * newRange) / oldRange) + newMin;
		
		return (float) newValue;
	}
	
	@Override
	public boolean isInEditMode() {
		return true;
	}

	public void clearValues() {
		chartData.clear();
		selectedIndex = 0;
		invalidate();
	}

	class FancyLine{
		float startX,startY,stopX,stopY;
		boolean selected = false;

		public FancyLine(float startX, float startY, float stopX, float stopY) {
			this.startX = startX;
			this.startY = startY;
			this.stopX = stopX;
			this.stopY = stopY;
		}
		
	}
}
