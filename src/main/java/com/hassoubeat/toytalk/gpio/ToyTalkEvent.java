/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.gpio;

import com.hassoubeat.toytalk.daemon.constract.MessageConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.quartz.QuartzManager;
import com.hassoubeat.toytalk.rest.RestCall;
import com.hassoubeat.toytalk.util.UtilLogic;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


import static java.lang.Thread.sleep;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hassoubeat
 */
public class ToyTalkEvent {
    
    // ロガー
    private static final Logger logger = LoggerFactory.getLogger(ToyTalkEvent.class);
    // GPIO処理の管理クラス(シングルトン)
    private GPIO gpio;
    // 表示処理の管理クラス
    private Viewer viewer;
    // イベントの管理クラス
    private QuartzManager quartzManager;
    // Utilロジック
    private UtilLogic utilLogic = UtilLogic.getInstace();
    // RestCallクラス
    private RestCall restCall = new RestCall();

    // コンストラクタ
    public ToyTalkEvent() {
        gpio = GPIO.getInstance();
        viewer = ViewerFactory.getInstace();
        quartzManager = new QuartzManager();
        
        // ボタンのイベント定義
        addButtonEvent();
    }
    
    /**
     * ToyTalk起動時処理
     */
    // TODO インターセプターの検討
    public void startup() throws InterruptedException {
        // 電源LEDの点灯(POWER_LED)
        gpio.powerLedOn();
        gpio.connectLedOn();
        viewer.displayStartingView();
        
        gpio.connectLedOn();
        boolean isNetworkConnection = utilLogic.isOnlineCheck();
        gpio.connectLedOff();
        
        if (!isNetworkConnection) {
            // ネットワークが未接続(Wifiなどが設定されていなかった場合)
            viewer.displayNetworkUnconnectView();
            logger.warn("{}:{}", MessageConst.UN_CONNECTED_NETWORK.getId(), MessageConst.UN_CONNECTED_NETWORK.getMessage());
            sleep(4000);
            return;
        } else {
            // ネットワーク接続済
            
            // イベントの取得
            List<RestEvent> restEventList = restCall.fetchAllEvent();
            
            if (restEventList == null) {
                // イベントが取得できなかった場合
                return;
            }
             
            for (RestEvent restEvent: restEventList) {
                quartzManager.addEvent(restEvent);
            }
        }
    }
    
    /**
     * 運用稼働を開始するメソッド
     */
    public void run() {
        // TOP画面を表示する
        viewer.displayTopView();
        while (viewer.getShowingViewId().equals(Viewer.TOP_VIEW)) {
            // TOP画面が表示され続けている間、現在時刻を表示しつづける
            viewer.displayTopView();
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                // TODO
            }
        }
    }
    
    /**
     * シャットダウン処理を実行する
     */
    public void shutdown() {
        // シャットダウン画面を表示して、シャットダウン処理を実行する
        viewer.displayShutdownView();
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            // TODO
        }
        gpio.shutdown();
    }
    
    /**
     * ハードボタンのイベントを定義する
     */
    private void addButtonEvent() {
        // 左ボタン押下時のイベント定義
        gpio.getLEFT_BUTTON().addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                logger.debug("LEFT_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                    // 押下時のみ実行
                    switch(viewer.getShowingViewId()) {
                        case Viewer.MODE_SELECT_VIEW:
                            // 現在表示中の画面が【モード選択画面】だった時、選択カーソルを左に一つ進める
                            viewer.setSelectingMenuItemIndex(viewer.getSelectingMenuItemIndex() - 1);
                            if (viewer.getSelectingMenuItemIndex() < 0) {
                                // インデックスがマイナスになった場合(存在しないカーソル値)
                                viewer.setSelectingMenuItemIndex(Viewer.MODE_SELECT_MENU_LIST.size() - 1);
                            }
                            viewer.displayModeSelectView();
                            break;
                        default:
                            // 動作が指定されていない画面だった時(特になにもしない)
                            break;    
                    }
                } 
            }
        });
        
        // 右ボタン押下時のイベント定義
        gpio.getRIGHT_BUTTON().addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                logger.debug("RIGHT_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                    // 押下時のみ実行
                    switch(viewer.getShowingViewId()) {
                        case Viewer.MODE_SELECT_VIEW:
                            // 現在表示中の画面が【モード選択画面】だった時、選択カーソルを右に一つ進める
                            viewer.setSelectingMenuItemIndex(viewer.getSelectingMenuItemIndex() + 1);
                            if (viewer.getSelectingMenuItemIndex() > (Viewer.MODE_SELECT_MENU_LIST.size() - 1)) {
                                // インデックスがモード選択配列のインデックス数を超過した場合、先頭に戻す
                                viewer.setSelectingMenuItemIndex(0);
                            }
                            viewer.displayModeSelectView();
                            break;
                        default:
                            // 動作が指定されていない画面だった時(特になにもしない)
                            break;    
                    }
                }
                
            }
        });
        
        // 決定ボタン押下時のイベント定義
        gpio.getENTER_BUTTON().addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event){
                logger.debug("ENTER_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                    // 押下時のみ実行
                    switch(viewer.getShowingViewId()) {
                        case Viewer.MODE_SELECT_VIEW:
                            // 現在表示中の画面が【モード選択画面】だった時、現在のカーソルにメニューを実行する
                            String selectingMenu = Viewer.MODE_SELECT_MENU_LIST.get(viewer.getSelectingMenuItemIndex());
                            switch(selectingMenu) {
                                case Viewer.EVENT_RESET_MODE:
                                    // イベント再取得モードの実行
                                    viewer.displayEventDataResetView();
                                    quartzManager.eventClear();
                                    List<RestEvent> restEventList;
                                    restEventList = restCall.fetchAllEvent();
                                    if (restEventList == null) {
                                        break;
                                    }
                                    for (RestEvent restEvent : restEventList) {
                                        quartzManager.addEvent(restEvent);
                                    }
                                    run();
                                    break;
                                case Viewer.TOY_INIT:
                                    // TODO ToyTalk初期化モードの実行
                                    break;
                                case Viewer.RETURN_TOP:
                                    // トップ画面に遷移する
//                                    run();
                            }
                            
                            break;
                        default:
                            // 動作が指定されていない画面だった時(特になにもしない)
                            break;   
                    }
                }
            }
        });
        
        // Modeボタン押下時のイベント定義
        gpio.getMODE_BUTTON().addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                logger.debug("MODE_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                   // 押下時のみ実行
                    switch(viewer.getShowingViewId()) {
                        case Viewer.TOP_VIEW:
                            // 現在表示中の画面が【TOP画面】だった時、モード選択画面を表示する
                            
                            // モード選択インデックスを0に初期化する
                            viewer.setSelectingMenuItemIndex(0);
                            viewer.displayModeSelectView();
                            break;
                        default:
                            // 動作が指定されていない画面だった時(特になにもしない)
                            break;    
                    }
                }
            }
        });
        
        // Syncボタン押下時のイベント定義
        gpio.getSYNC_BUTTON().addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                logger.debug("SYNC_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                    // 押下時のみ実行
                    switch(viewer.getShowingViewId()) {
                        case Viewer.TOP_VIEW:
                            // TODO 差分同期を実施する
                            
                            break;
                        default:
                            // 動作が指定されていない画面だった時(特になにもしない)
                            break;    
                    }
                }
            }
        });
    }
    
}
