package com.smilehacker.iocast.base.test;

/**
 * Created by kleist on 15/12/2.
 */
public class TestPImple extends TestP<AImpl> {
    @Override
    public AImpl createA() {
        return new AImpl();
    }
}
