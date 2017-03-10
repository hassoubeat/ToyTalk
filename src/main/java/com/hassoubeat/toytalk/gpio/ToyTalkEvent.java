/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.gpio;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import static java.lang.Thread.sleep;

/**
 *
 * @author hassoubeat
 */
public class ToyTalkEvent {
    
    // GPIO処理の管理クラス(シングルトン)
    private GPIO gpio;
    // 表示処理の管理クラス(シングルトン)
    private Viewer viewer;
    
    // 表示中ViewのID
    private String showingViewId = "";
    
    // 選択中のメニューインデックス番号
    private int selectingMenuItemIndex;

    // コンストラクタ
    public ToyTalkEvent() {
        gpio = GPIO.getInstance();
        viewer = ViewerFactory.getInstace();
    }
    
    /**
     * ToyTalk起動時処理
     */
    public void startup() {
        // 電源LEDの点灯(POWER_LED)
        gpio.startup();
        showingViewId = Viewer.STARTING_VIEW;
        viewer.displayStartingView();
        
        // TODO ネットワーク接続有無確認 サーバ接続中LEDの点灯とコンソール出力
        // TODO true 次へ
        // TODO false TOP画面に遷移
        // TODO 設定ファイルからアクセストークンを取得してRESTAPIを実行 
        // TODO 実行結果の戻り値で正しく実行できたか判断
        // TODO true
        // TODO false(認証失敗コード返却)
        // TODO false(システムアップデート有) システムアップデートの実行
        // TODO 
        // TODO 同期でRESTを実行する
        
        // ボタンのイベント定義
        
        // 左ボタン押下時のイベント定義
        gpio.getLEFT_BUTTON().addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println("LEFT_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                    // 押下時のみ実行
                    switch(showingViewId) {
                        case Viewer.MODE_SELECT_VIEW:
                            // 現在表示中の画面が【モード選択画面】だった時、選択カーソルを左に一つ進める
                            selectingMenuItemIndex -= 1;
                            if (selectingMenuItemIndex < 0) {
                                // インデックスがマイナスになった場合(存在しないカーソル値)
                                selectingMenuItemIndex = Viewer.MODE_SELECT_MENU_LIST.size() - 1;
                            }
                            
                            viewer.displayModeSelectView(selectingMenuItemIndex);
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
                System.out.println("RIGHT_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                    // 押下時のみ実行
                    switch(showingViewId) {
                        case Viewer.MODE_SELECT_VIEW:
                            // 現在表示中の画面が【モード選択画面】だった時、選択カーソルを右に一つ進める
                            selectingMenuItemIndex += 1;
                            if (selectingMenuItemIndex > (Viewer.MODE_SELECT_MENU_LIST.size() - 1)) {
                                // インデックスがモード選択配列のインデックス数を超過した場合、先頭に戻す
                                selectingMenuItemIndex = 0;
                            }
                            
                            viewer.displayModeSelectView(selectingMenuItemIndex);
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
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println("ENTER_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                    // 押下時のみ実行
                    switch(showingViewId) {
                        case Viewer.MODE_SELECT_VIEW:
                            // 現在表示中の画面が【モード選択画面】だった時、現在のカーソルにメニューを実行する
                            String selectingMenu = Viewer.MODE_SELECT_MENU_LIST.get(selectingMenuItemIndex);
                            switch(selectingMenu) {
                                case Viewer.EVENT_RESET_MODE:
                                    // TODO イベント再取得モードの実行
                                    break;
                                case Viewer.TOY_INIT:
                                    // TODO ToyTalk初期化モードの実行
                                    break;
                                case Viewer.RETURN_TOP:
                                    // TOP画面を表示する
                                    try {
                                        run();
                                    } catch (InterruptedException ex) {
                                        System.out.println(ex);
                                    }
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
                System.out.println("MODE_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                   // 押下時のみ実行
                    switch(showingViewId) {
                        case Viewer.TOP_VIEW:
                            // 現在表示中の画面が【TOP画面】だった時、モード選択画面を表示する
                            selectingMenuItemIndex = 0;
                            
                            showingViewId = Viewer.MODE_SELECT_VIEW;
                            viewer.displayModeSelectView(selectingMenuItemIndex);
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
                System.out.println("SYNC_BUTTON_CLICK:" + event.getState().toString());
                if (event.getState() == PinState.HIGH) {
                    // 押下時のみ実行
                    switch(showingViewId) {
                        case Viewer.TOP_VIEW:
                            // TODO 現在表示中の画面が【TOP画面】だった時、イベント画面を表示する
                            
                            break;
                        default:
                            // 動作が指定されていない画面だった時(特になにもしない)
                            break;    
                    }
                }
            }
        });
    }
    
    /**
     * 運用稼働を開始するメソッド
     */
    public void run() throws InterruptedException {
        // TOP画面を表示する
        showingViewId = Viewer.TOP_VIEW;
        while (showingViewId.equals(Viewer.TOP_VIEW)) {
            // TOP画面が表示され続けている間、現在時刻を表示しつづける
            viewer.displayTopView();
            sleep(1000);
        }
        
    }
    
    /**
     * シャットダウン処理を実行する
     */
    public void shutdown() {
        // シャットダウン画面を表示して、シャットダウン処理を実行する
        showingViewId = Viewer.SHUTDOWN_VIEW;
        viewer.displayShutdownView();
        gpio.shutdown();
    }
    
}
