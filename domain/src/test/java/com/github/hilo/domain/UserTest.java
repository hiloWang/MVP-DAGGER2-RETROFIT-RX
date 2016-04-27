package com.github.hilo.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Administrator on 2016/4/23.
 */
public class UserTest {
	private static final boolean ERROR = true;
	private User user;

	@Before public void setup() {
		user = new User(ERROR);
	}

	@Test public void testUserConstructorHappyCase() {
		boolean userError = user.isError();

		Assert.assertThat(userError,is(ERROR));
	}
}
