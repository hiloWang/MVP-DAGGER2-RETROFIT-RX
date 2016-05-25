package com.github.hilo.widget.interpolator;

import android.animation.TimeInterpolator;

public class ExpoOut implements TimeInterpolator {
	@Override public float getInterpolation(float t) {
		return (t == 1) ? 1 : -(float)Math.pow(2,-10 * t) + 1;
	}
}