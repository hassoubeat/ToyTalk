/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import lombok.Getter;

/**
 * GPIOの処理を定義するクラス
 * @author hassoubeat
 */
public class GPIO {
    
    // シングルトンインスタンス
    private static GPIO gpioInstance = new GPIO();
    
    private GpioController gpio;
    
    @Getter
    // ToyTalkが動作している間点灯するLEDを制御するGPIOピン
    private GpioPinDigitalOutput POWER_LED;
    
    @Getter
    // ToyTalkがToyManagerと接続している間点灯するLEDを制御するGPIOピン
    private GpioPinDigitalOutput CONNECT_LED;
    
    @Getter
    // ToyTalkの左ボタンを制御するGPIOピン
    private GpioPinDigitalInput LEFT_BUTTON;
    
    @Getter
    // ToyTalkの右ボタンを制御するGPIOピン
    private GpioPinDigitalInput RIGHT_BUTTON;
    
    @Getter
    // ToyTalkの決定ボタンを制御するGPIOピン
    private GpioPinDigitalInput ENTER_BUTTON;
    
    @Getter
    // ToyTalkのModeボタンを制御するGPIOピン
    private GpioPinDigitalInput MODE_BUTTON;
    
    @Getter
    // ToyTalkのSYNCボタンを制御するGPIOピン
    private GpioPinDigitalInput SYNC_BUTTON;
    
    // コンストラクタ
    private GPIO() {
        gpio = GpioFactory.getInstance();
        
        // 各ピンの定義(同時にシャットダウン時の動作も合わせて定義する)
        POWER_LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "POWER_LED", PinState.LOW);
        POWER_LED.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        
        CONNECT_LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "CONNECT_LED", PinState.LOW);
        CONNECT_LED.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        
        LEFT_BUTTON = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, "LEFT_BUTTON", PinPullResistance.PULL_DOWN);
        LEFT_BUTTON.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        
        RIGHT_BUTTON = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "RIGHT_BUTTON", PinPullResistance.PULL_DOWN);
        RIGHT_BUTTON.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        
        ENTER_BUTTON = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, "ENTER_BUTTON", PinPullResistance.PULL_DOWN);
        ENTER_BUTTON.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        
        MODE_BUTTON = gpio.provisionDigitalInputPin(RaspiPin.GPIO_12, "MODE_BUTTON", PinPullResistance.PULL_DOWN);
        MODE_BUTTON.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        
        SYNC_BUTTON = gpio.provisionDigitalInputPin(RaspiPin.GPIO_13, "SYNC_BUTTON", PinPullResistance.PULL_DOWN);
        SYNC_BUTTON.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        
    }
    
    /**
     * GPIOのスタートアップを実行する
     */
    public void startup() {
        // 電源LEDをONにする
        POWER_LED.high();
    }
    
    /**
     * GPIOのシャットダウンを実行する
     */
    public void shutdown() {
        gpio.isShutdown();
    }
    
    /**
     * GPIOのシングルトンインスタンスを取得する
     * 
     * @return 
     */
    public static GPIO getInstance(){
        return gpioInstance;
    }
    
    
    
    
    
    
    
}
