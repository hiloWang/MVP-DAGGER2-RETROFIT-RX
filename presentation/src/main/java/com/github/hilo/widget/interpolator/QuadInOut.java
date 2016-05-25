package com.github.hilo.widget.interpolator;

import android.animation.TimeInterpolator;

public class QuadInOut implements TimeInterpolator {
	@Override public float getInterpolation(float t) {
		if ((t *= 2) < 1) return 0.5f * t * t;
		return -0.5f * ((--t) * (t - 2) - 1);
	}
}