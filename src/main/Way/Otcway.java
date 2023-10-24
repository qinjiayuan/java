package main.Way;

import main.intefacation.Otc;

public class Otcway implements Otc {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int multiply(int a, int b) {
        return a * b;
    }
}
