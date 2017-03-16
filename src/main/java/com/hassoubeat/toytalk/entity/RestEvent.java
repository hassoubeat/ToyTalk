/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.entity;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * ToyTalkから取得する際のEventエンティティ
 * @author hassoubeat
 */
@XmlRootElement
public class RestEvent {
    
    @Getter
    @Setter
    private int id;
    
    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private String content;
    
    @Getter
    @Setter
    private Date startDate;
    
    @Getter
    @Setter
    private Date endDate;
    
    @Getter
    @Setter
    private int roop;
    
    @Getter
    @Setter
    private Date roopEndDate;
    
    @Getter
    @Setter
    private Integer toyId;
    
    @Getter
    @Setter
    private Integer accountId;
    
    @Getter
    @Setter
    private Integer FacetId;

    public RestEvent() {
    }
    
    
    
    
    
    
    
}
