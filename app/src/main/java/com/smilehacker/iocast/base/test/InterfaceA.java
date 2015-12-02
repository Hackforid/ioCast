package com.smilehacker.iocast.base.test;

/**
 * Created by kleist on 15/12/2.
 */
public interface InterfaceA<B extends InterfaceB>{

    void setB(B b);
    void getB();
}
