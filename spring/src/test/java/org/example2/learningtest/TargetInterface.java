package org.example2.learningtest;

public interface TargetInterface {
    void hello();

    void hello(String name);

    int minus(int a, int b) throws RuntimeException;

    int plus(int a, int b);
}
