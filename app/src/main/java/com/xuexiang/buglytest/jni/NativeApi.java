package com.xuexiang.buglytest.jni;

/**
 * @author xuexiang
 * @since 2018/11/19 下午1:55
 */
public class NativeApi {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * 测试java崩溃
     */
    public native void testJavaCrash();

    /**
     * 测试native崩溃
     */
    public native void testNativeCrash();
}
