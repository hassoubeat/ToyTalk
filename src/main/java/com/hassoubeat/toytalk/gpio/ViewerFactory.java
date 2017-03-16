/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.gpio;

/**
 *
 * @author hassoubeat
 */
public class ViewerFactory {
    
    private static final Viewer VIEWER_INSTANCE = new Lcd16_2Viewer();

    public ViewerFactory() {
    }
    
    public static Viewer getInstace() {
        return VIEWER_INSTANCE;
    }
    
}
