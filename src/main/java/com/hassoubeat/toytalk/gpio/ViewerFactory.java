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
    
    private ViewerFactory() {
        
    }
    
    public static Viewer getInstace() {
        return new ConsoleViewer();
    }
    
}
