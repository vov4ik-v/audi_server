package com.volodymyrvasylyshyn.audi_server.exeptions;

public class EmailNotFoundExeption extends IllegalArgumentException {
    public EmailNotFoundExeption(String s) {
        super(s);
    }
}
