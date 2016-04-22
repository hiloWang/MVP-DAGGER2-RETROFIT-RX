package com.github.hilo.domain.exception;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2016/4/22.
 */
public class DefaultErrorBunleTest {

    DefaultErrorBundle defaultErrorBundle;

    @Mock
    private Exception mockException;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        defaultErrorBundle = new DefaultErrorBundle(mockException);
    }

    @Test
    public void testGetErrorMessageinteraction() {
        String defaultErrorMessage = defaultErrorBundle.getErrorMessage();

        // 1.验证方法调用（mockException是否调用了getMessage()）
        verify(mockException).getMessage();
        // 2.Junit 验证ErrorMessage是否Equals
        Assert.assertEquals(defaultErrorMessage, mockException.getMessage());
        // 3. 见名知意
        when(defaultErrorMessage == null || defaultErrorMessage == "").thenThrow(new RuntimeException("defaultErrorMessage is null"));
        // 4. 第一次调用doNothing, 第二次doThrow抛异常
        doNothing().doThrow(new RuntimeException("test void method exception")).when(mockException).printStackTrace();
    }
}
