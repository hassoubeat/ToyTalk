/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.gpio;

import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author hassoubeat
 */
public class Lcd16_2Viewer extends Viewer{
    
    final private int LCD_ROWS = 2;
    final private int LCD_ROW1 = 0;
    final private int LCD_ROW2 = 1;
    final private int LCD_COLUMNS = 16;
    final private int LCD_BITS = 4;
    final private Pin LCD_RS_PIN = RaspiPin.GPIO_11;
    final private Pin LCD_STROBE_PIN = RaspiPin.GPIO_10;
    final private Pin LCD_BIT1_PIN = RaspiPin.GPIO_26;
    final private Pin LCD_BIT2_PIN = RaspiPin.GPIO_27;
    final private Pin LCD_BIT3_PIN = RaspiPin.GPIO_28;
    final private Pin LCD_BIT4_PIN = RaspiPin.GPIO_29;
    
    final private GpioLcdDisplay lcd;

    public Lcd16_2Viewer() {
        // LCDディスプレイの初期化
       lcd = new GpioLcdDisplay(LCD_ROWS, LCD_COLUMNS, LCD_RS_PIN, LCD_STROBE_PIN, LCD_BIT1_PIN, LCD_BIT2_PIN, LCD_BIT3_PIN, LCD_BIT4_PIN);
       lcd.clear();
    }
    
    @Override
    public void displayStartingView() {
        // 【起動中画面】を表示する
        lcd.clear();
        lcd.write(LCD_ROW1, "ToyTalk", LCDTextAlignment.ALIGN_CENTER);
        lcd.write(LCD_ROW2, "Stating...", LCDTextAlignment.ALIGN_CENTER);
        setShowingViewId(STARTING_VIEW);
    }

    @Override
    public void displayServerConnectingView() {
        // 【サーバ接続中画面】を表示する
        lcd.clear();
        lcd.write(LCD_ROW1, "Server", LCDTextAlignment.ALIGN_CENTER);
        lcd.write(LCD_ROW2, "Connecting...", LCDTextAlignment.ALIGN_CENTER);
        setShowingViewId(SERVER_CONNECTING_VIEW);
    }

    @Override
    public void displayTopView() {
        // 【トップ画面】を表示する
        lcd.clear();
        LocalDateTime now = LocalDateTime.now();
        lcd.write(LCD_ROW1, now.format(DateTimeFormatter.ofPattern("MM/dd HH:mm:ss")));
        lcd.write(LCD_ROW2, "Top View");
        setShowingViewId(TOP_VIEW);
    }

    @Override
    public void displayModeSelectView() {
        // 【モード選択画面】を表示する
        lcd.clear();
        lcd.write(LCD_ROW1, "MODE SELECT", LCDTextAlignment.ALIGN_CENTER);
        lcd.write(LCD_ROW2, MODE_SELECT_MENU_LIST.get(getSelectingMenuItemIndex()));
        setShowingViewId(MODE_SELECT_VIEW);
    }
    
    @Override
    public void displayRequestParamInvalidView() {
        // 【リクエストパラメータ不備画面】を表示する
        lcd.clear();
        lcd.write(LCD_ROW1, "Bad RequestParam");
        lcd.write(LCD_ROW2, "PleaseCheckConf");
        setShowingViewId(REQUEST_PARAM_INVALID_VIEW);
    }
    
    @Override
    public void displayAccessFilterUnApprovalView() {
        // 【アクセスフィルター未承認画面】を表示する
        lcd.clear();
        lcd.write(LCD_ROW1, "Access Approval");
        lcd.write(LCD_ROW2, "From ToyManager");
        setShowingViewId(ACCESS_FILTER_UN_APPROVAL_VIEW);
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
        // 【ネットワーク未接続画面】を表示する
        lcd.clear();
        lcd.write(LCD_ROW1, "Net UnConnect", LCDTextAlignment.ALIGN_CENTER);
        lcd.write(LCD_ROW2, "Please Wifi Set", LCDTextAlignment.ALIGN_CENTER);
        setShowingViewId(NETWORK_UNCONNECT_VIEW);
    }
    
    @Override
    public void displayEventDataResetView() {
        // 【イベント再取得画面】を表示する
        lcd.clear();
        lcd.write(LCD_ROW1, "Event Reseting...", LCDTextAlignment.ALIGN_CENTER);
        lcd.write(LCD_ROW2, "Please Wait", LCDTextAlignment.ALIGN_CENTER);
        setShowingViewId(EVENT_RESET_MODE);
    }

    @Override
    public void displayToyInitView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void displayShutdownView() {
        // 【シャットダウン画面】を表示する
        lcd.clear();
        lcd.write(LCD_ROW1, "Shutdown...");
        lcd.clear();
        setShowingViewId(SHUTDOWN_VIEW);
    }
}
