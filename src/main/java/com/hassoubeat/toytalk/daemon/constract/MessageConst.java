/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.daemon.constract;

/**
 *
 * @author hassoubeat
 */
public enum MessageConst {
    // TIR(ToyTalkで発生するインフォメーションメッセージ)
    SUCCESS_REGIST_EVENT_TO_SCHEDULE("TIR_01000001", "イベントをスケジューラに登録しました。"),
    SUCCESS_CLEAR_EVENT("TIR_01000002", "スケジューラに登録されたイベントを全削除しました。"),
    
    // TER(ToyTalkで発生する想定内エラーメッセージ)
    UN_CONNECTED_NETWORK("TER_01000001", "インターネットに未接続です。"),
    REQUEST_PARAM_INVALID("TER_01000002", "リクエストに誤りがあります。"),
    ACCESS_FILTER_UN_APPROVAL("TER_01000003", "アクセスフィルターが未承認です。"),
    ACCESS_TOKEN_EXPIRED("TER_01000004", "アクセストークンの有効期限切れです。"),
    FAILED_AUTHORIZATION("TER_01000005", "認証に失敗しました。"),
    
    
    // TCR(ToyTalkで発生する想定外エラーメッセージ)
    SYSTEM_ERROR("TCR_01000001", "システムエラーが発生しました。")
    ;
    
    private final String id;
    private final String message;
    
    private MessageConst(final String id, final String message) {
        this.id = id;
        this.message = message;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getMessage() {
        return this.message;
    }
}
