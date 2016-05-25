package com.github.hilo.widget.interpolator;

import android.animation.TimeInterpolator;

public class BackOut implements TimeInterpolator {
	@Override public float getInterpolation(float t) {
		float s = param_s;
		return (t -= 1) * t * ((s + 1) * t + s) + 1;
	}

	protected float param_s = 1.70158f;

	public BackOut amount(float s) {
		param_s = s;
		return this;
	}
}