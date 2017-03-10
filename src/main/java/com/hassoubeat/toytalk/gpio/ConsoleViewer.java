/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.gpio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author hassoubeat
 */
public class ConsoleViewer implements Viewer{
    
    @Override
    public void displayStartingView() {
        // 【起動中画面】を表示する
        System.out.println("");
        System.out.println("");
        System.out.println("---- startup… ----");
        System.out.println("");
        System.out.println("");
    }

    @Override
    public void displayServerConnectingView() {
        // 【サーバ接続中画面】を表示する
        System.out.println("");
        System.out.println("");
        System.out.println("---- server connecting… ----");
        System.out.println("");
        System.out.println("");
    }

    @Override
    public void displayTopView() {
        // 【トップ画面】を表示する
        System.out.println("");
        System.out.println("");
        System.out.println("----  Top View ----");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        System.out.println("-------------------");
        System.out.println("");
        System.out.println("");
    }

    @Override
    public void displayModeSelectView(int cursol) {
        // 【モード選択画面】を表示する
        System.out.println("");
        System.out.println("");
        int i = 0;
        for (String item : MODE_SELECT_MENU_LIST) {            
            if (i == cursol) {
                // 選択中のアイテムにカーソルを出力する
                System.out.println("・" + item + " ←");
            } else {
                // モード選択要素を表示する
                System.out.println("・" + item);
            }
            i++;
        }
        System.out.println("");
        System.out.println("");
    }

    @Override
    public void displayAuthFailedView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void displaySystemUpdatingView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void displaySyncingEventView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void displayNetworkUnconnectView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void displayShutdownView() {
        // 【シャットダウン画面】を表示する
        System.out.println("");
        System.out.println("");
        System.out.println("---- shutdown… ----");
        System.out.println("");
        System.out.println("");
    }
}
