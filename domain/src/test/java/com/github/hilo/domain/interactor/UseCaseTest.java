package com.github.hilo.domain.interactor;

import com.github.hilo.domain.executor.PostExecutionThread;
import com.github.hilo.domain.executor.ThreadExecutor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;

/**
 * Created by Administrator on 2016/4/22.
 */
public class UseCaseTest {

    private UseCaseTestClass useCase;
    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.useCase = new UseCaseTestClass(mockThreadExecutor, mockPostExecutionThread);
    }

    @Test
    public void testBuildUseCaseObservableReturnCorrectResult() {
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        TestScheduler testScheduler = new TestScheduler();

        // 当调用mockPostExecutionThread.getScheduler()方法时,期待返回testScheduler对象, 如果不写这句话, 那么会在执行execute() 源码内抛nullpoint
        given(mockPostExecutionThread.getScheduler()).willReturn(testScheduler);
        // 执行
        useCase.execute(testSubscriber);
        // 测试1
        Assert.assertThat(testSubscriber.getOnNextEvents().size(), is(0));
        // 解除
        useCase.unsubscribe();
        // 测试2
        Assert.assertThat(testSubscriber.isUnsubscribed(), is(true));

        // 测试3
        TestSubscriber<Integer> testSubscriber2 = new TestSubscriber<>();
        useCase.execute(testSubscriber2);
        Assert.assertThat(testSubscriber2.isUnsubscribed(), is(false));

    }

    @Test
    public void testSubscriptionWhenExecutingUseCase() {
        // 如果不给他一个Scheduler的话, 会抛nullpoint异常， 所以Assert.assertThat(testSubscriber.isUnsubscribed(), is(true)); 在抛出异常时，会默认解除testSubscriber的订阅
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
//        given(mockPostExecutionThread.getScheduler()).willReturn(new TestScheduler());
        useCase.execute(testSubscriber);
        // ----------------------------------------------------------------------------

        Mockito.when(!testSubscriber.isUnsubscribed()).thenThrow(new NullPointerException("scheduler must be not null"));
    }

    public static class UseCaseTestClass extends UseCase {

        protected UseCaseTestClass(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
            super(threadExecutor, postExecutionThread);
        }

        @Override
        protected Observable buildUseCaseObservable() {
            return Observable.empty();
        }

        @Override
        public void execute(Subscriber UseCaseSubscriber) {
            super.execute(UseCaseSubscriber);
        }

        @Override
        public void unsubscribe() {
            super.unsubscribe();
        }
    }

}
