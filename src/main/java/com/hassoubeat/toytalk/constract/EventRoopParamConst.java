/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.constract;

/**
 *
 * @author hassoubeat
 */
public class EventRoopParamConst{
    // ループ有無フラグ(0010 0000 0000 0000 0000)
    public final int IS_ROOP = 0x20000;
    // 日次ループフラグ(0001 0000 0000 0000 0000)
    public final int IS_EVERY_DAY_ROOP = 0x10000;
    // 週次ループフラグ(0000 1000 0000 0000 0000)
    public final int IS_EVERY_WEEK_ROOP = 0x08000;
    // 月次ループフラグ(0000 0100 0000 0000 0000)
    public final int IS_EVERY_MOUTH_ROOP = 0x04000;
    // 年次ループフラグ(0000 0010 0000 0000 0000)
    public final int IS_EVERY_YEAR_ROOP = 0x02000;
    // ループ基準フラグ... 日付基準のループか曜日基準のループかを判断するフラグ このフラグが立っている時は【日付】基準(0000 0001 0000 0000 0000)
    public final int IS_ROOP_STANDARD_DAY = 0x01000;
    // ループ日曜日フラグ ... 週次ループの時にどの曜日に通知するかを設定するフラグ (0000 0000 1000 0000 0000)
    public final int IS_ROOP_SUNDAY = 0x00800;
    // ループ月曜日フラグ ... 週次ループの時にどの曜日に通知するかを設定するフラグ (0000 0000 0100 0000 0000)
    public final int IS_ROOP_MONDAY = 0x00400;
    // ループ火曜日フラグ ... 週次ループの時にどの曜日に通知するかを設定するフラグ (0000 0000 0010 0000 0000)
    public final int IS_ROOP_TUESDAY = 0x00200;
    // ループ水曜日フラグ ... 週次ループの時にどの曜日に通知するかを設定するフラグ (0000 0000 0001 0000 0000)
    public final int IS_ROOP_WEDNESDAY = 0x00100;
    // ループ木曜日フラグ ... 週次ループの時にどの曜日に通知するかを設定するフラグ (0000 0000 0000 1000 0000)
    public final int IS_ROOP_THURSDAY = 0x00080;
    // ループ金曜日フラグ ... 週次ループの時にどの曜日に通知するかを設定するフラグ (0000 0000 0000 0100 0000)
    public final int IS_ROOP_FRIDAY = 0x00040;
    // ループ土曜日フラグ ... 週次ループの時にどの曜日に通知するかを設定するフラグ (0000 0000 0000 0010 0000)
    public final int IS_ROOP_SATURDAY = 0x00020;
    // ループ間隔設定ビット ... ループ間隔を定義するビットは末尾から何ビットかを定義する
    public final int IS_ROOP_INTERVAL_BIT = 0xfffe0;

    public EventRoopParamConst() {
    }
    
}
