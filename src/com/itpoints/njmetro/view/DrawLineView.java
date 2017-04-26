package com.itpoints.njmetro.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawLineView extends View {
	Paint paint = new Paint();
	View startView;
	View endView;
	private int angle;

	public DrawLineView(Context context, View startView, View endView, int angle) {
		super(context);
		paint.setColor(Color.WHITE);
		this.startView = startView;
		this.endView = endView;
		this.angle = angle;
	}

	@SuppressLint("NewApi")
	public void onDraw(Canvas canvas) {
		float r = startView.getWidth() / 2;

		float startX = (float) (startView.getX() + r + r * Math.cos(angle * 3.14 / 180));
		float startY = (float) (startView.getY() + r + r * Math.sin(angle * 3.14 / 180));

		float endX = (float) (endView.getX() + r + r * Math.cos((180 - angle) * 3.14 / 180));
		float endY = (float) (endView.getY() + r + r * Math.sin((180 + angle) * 3.14 / 180));

		canvas.drawLine(startX, startY, endX, endY, paint);
	}

}