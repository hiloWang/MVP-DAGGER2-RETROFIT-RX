package com.github.hilo.widget.interpolator;

import android.animation.TimeInterpolator;

public class CircInOut implements TimeInterpolator {
	@Override public float getInterpolation(float t) {
		if ((t *= 2) < 1) return -0.5f * ((float)Math.sqrt(1 - t * t) - 1);
		return 0.5f * ((float)Math.sqrt(1 - (t -= 2) * t) + 1);
	}
}
