package com.volodymyrvasylyshyn.audi_server.exeptions;

public class AudiModelNotFoundException  extends IllegalArgumentException {
    public AudiModelNotFoundException(String s) {
        super(s);
    }
}
