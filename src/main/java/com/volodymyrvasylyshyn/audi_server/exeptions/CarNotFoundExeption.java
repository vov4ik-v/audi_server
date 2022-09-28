package com.volodymyrvasylyshyn.audi_server.exeptions;

public class CarNotFoundExeption extends IllegalArgumentException {
    public CarNotFoundExeption(String s) {
        super(s);
    }
}
