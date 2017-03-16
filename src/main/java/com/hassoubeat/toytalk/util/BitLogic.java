/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.util;

import com.hassoubeat.toytalk.daemon.constract.EventRoopParamConst;

/**
 * ビット演算のロジック
 * @author hassoubeat
 */
public class BitLogic {
    
    static private final BitLogic bitLogic = new BitLogic();
    
    /**
     * 引数として渡されたパラメータでビットAnd演算を実施する
     * 
     * @param param1
     * @param param2
     * @return param1とparam2のand演算結果
     */
    public int bitAnd(int param1, int param2) {
        return param1 & param2;
    }
    
    /**
     * 引数として渡されたパラメータでビットOr演算を実施する
     * 
     * @param param1
     * @param param2
     * @return param1とparam2のOr演算結果
     */
    public int bitOr(int param1, int param2) {
        return param1 | param2;
    }
    
    /**
     * 引数として渡されたパラメータでビットXOr演算を実施する
     * 
     * @param param1
     * @param param2
     * @return param1とparam2のXor演算結果
     */
    public int bitXor(int param1,int param2) {
        return param1 ^ param2;
    }
    
    /**
     * 引数として渡されたパラメータをビット反転する
     * 
     * @param param
     * @return paramのビット反転結果
     */
    public int bitNot(int param) {
        return ~param;
    }
    
    /**
     * 引数として渡されたパラメータでビットが立っていた場合Trueを返却する
     * 
     * @param param1
     * @param param2
     * @return param1とparam2のand演算結果
     */
    public boolean bitCheck(int param1, int param2) {
        if ((param1 & param2) > 0) {
            return true;
        }
        
        return false;
    }
    
    /**
     * インスタンスの取得
     */
    public static BitLogic getInstace() {
        return bitLogic;
    }
    
    
}
