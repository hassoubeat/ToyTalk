/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.exception;

/**
 * ToyTalk内で想定されない例外が行われた場合
 * @author hassoubeat
 */
public class ToyTalkException extends RuntimeException{

    public ToyTalkException() {
    }
    
    public ToyTalkException(String msg) {
        super(msg);
    }
    
    public ToyTalkException(String msg, Throwable ex) {
        super(msg, ex);
    }
    
    
}
