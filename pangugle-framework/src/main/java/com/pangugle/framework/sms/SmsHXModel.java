/*
 * Copyright (C) 2019  即时通讯网(www.pangugle.com) & Jack Pangugle.
 * The pangugle project. All rights reserved.
 * 
 * 【本产品为著作权产品，合法授权后请放心使用，禁止外传！】
 * 【本次授权给：<xxx网络科技有限公司>，授权编号：<授权编号-xxx>】
 * 
 * 本系列产品在国家版权局的著作权登记信息如下：
 * 1）国家版权局登记名（简称）和证书号：Project_name（软著登字第xxxxx号）
 * 著作权所有人：厦门盘古网络科技有限公司
 * 
 * 违法或违规使用投诉和举报方式：
 * 联系邮件：2624342267@qq.com
 * 联系微信：pangugle
 * 联系QQ：2624342267
 * 官方社区：http://www.pangugle.com
 */
package com.pangugle.framework.sms;

/**
 * Created by Administrator on 2019/4/8 0008.
 */
public class SmsHXModel {
    /**
     * {"returnstatus":"Success", ---------- 返回状态值：成功返回Success 失败返回：Fail
     * "message":"操作成功",---------- 相关的错误描述
     * "remainpoint":"-4",----------- 返回余额
     * "taskID":"1504080852350206",-----------  返回本次任务的序列ID
     * "successCounts":"1"}---------------成功短信数：当提交成功后返回的短信数
     */
    String returnstatus;
    String message;
    String remainpoint;
    String taskID;
    Integer successCounts;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemainpoint() {
        return remainpoint;
    }

    public void setRemainpoint(String remainpoint) {
        this.remainpoint = remainpoint;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public Integer getSuccessCounts() {
        return successCounts;
    }

    public void setSuccessCounts(Integer successCounts) {
        this.successCounts = successCounts;
    }

    public String getReturnstatus() {
        return returnstatus;
    }

    public void setReturnstatus(String returnstatus) {
        this.returnstatus = returnstatus;
    }
}
