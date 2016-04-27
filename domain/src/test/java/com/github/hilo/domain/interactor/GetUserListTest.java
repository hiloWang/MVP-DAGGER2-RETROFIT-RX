package com.github.hilo.domain.interactor;

import com.github.hilo.domain.executor.PostExecutionThread;
import com.github.hilo.domain.executor.ThreadExecutor;
import com.github.hilo.domain.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by Administrator on 2016/4/22.
 */
public class GetUserListTest {

	private GetUserList getUserList;

	@Mock private UserRepository mockUserRepository;
	@Mock private ThreadExecutor mockThreadExecutor;
	@Mock private PostExecutionThread mockExecutionThread;

	@Before public void setup() {
		MockitoAnnotations.initMocks(this);
		getUserList = new GetUserList(mockUserRepository,mockThreadExecutor,mockExecutionThread);
	}

	@Test public void testGetUserListUseCaseObservableHappyCase() {
		getUserList.buildUseCaseObservable();

		// 1. 验证的基本方法: verify(mock).someMethod(…)来验证方法的调用
		verify(mockUserRepository).user();
		//        verify(mockUserRepository).user(1); // worry way
		// 2. 验证未曾执行的方法: 确定user(1)这个构造方法没有被执行
		verify(mockUserRepository,never()).user(1);
		// 3. 查询多余的方法调用 verifyNoMoreInteractions()方法可以传入多个mock对象作为参数
		verifyNoMoreInteractions(mockUserRepository);
		// 4. 查询没有交互的mock对象: verifyZeroInteractions()也是一个测试工具，源码和verifyNoMoreInteractions()
		// 的实现是一样的，为了提高逻辑的可读性，所以只不过名字不同。
		// 本例中用来确认传入的对象没有任何交互
		verifyZeroInteractions(mockThreadExecutor);
		verifyZeroInteractions(mockExecutionThread);

	}
}
