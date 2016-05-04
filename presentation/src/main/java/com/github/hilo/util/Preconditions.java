package com.github.hilo.util;


import android.os.Looper;

public final class Preconditions {
	public static void checkArgument(boolean assertion,String message) {
		if (!assertion) {
			throw new IllegalArgumentException(message);
		}
	}

	public static <T> T checkNotNull(T value,String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}
		return value;
	}

	public static void checkUiThread() {
		if (Looper.getMainLooper() != Looper.myLooper()) {
			throw new IllegalStateException("Must be called from the menu thread. Was: " + Thread.currentThread());
		}
	}

	private Preconditions() {
		throw new AssertionError("No instances.");
	}
}
