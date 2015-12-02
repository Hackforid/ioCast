package com.smilehacker.iocast.base.test;

/**
 * Created by kleist on 15/12/2.
 */
public abstract class TestP<A extends InterfaceA> implements InterfaceB {

    private A interfaceA;

    public void init() {
        interfaceA = createA();
        interfaceA.setB(this);
    }

    public abstract A createA();
}
