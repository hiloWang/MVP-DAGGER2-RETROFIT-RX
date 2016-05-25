package com.github.hilo.widget.interpolator;

import android.animation.TimeInterpolator;

public class ExpoIn implements TimeInterpolator {
	@Override public float getInterpolation(float input) {
		return (input == 0) ? 0 : (float)Math.pow(2,10 * (input - 1));
	}
}
