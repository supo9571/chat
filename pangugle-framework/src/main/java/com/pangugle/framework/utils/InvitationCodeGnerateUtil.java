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
package com.pangugle.framework.utils;

import java.util.Random;

/**分享码生成工具类
 * @author XXX
 * @create 2018-11-08 10:42
 */
public class InvitationCodeGnerateUtil {

    public static String generateInviteCode() {
        int len = 8;
        /*char[] chars = {'Q', 'W', 'E', '8', 'S', '2', 'D', 'Z',
                'X', '9', 'C', '7', 'P', '5', 'K', '3',
                'M', 'J', 'U', 'F', 'R', '4', 'V', 'Y',
                'T', 'N', '6', 'B', 'G', 'H', 'A', 'L'};*/
        char[] chars = {'1','2','3','4','5','6','7','8','9'};
        Random random = new Random();
        char[] inviteChars = new char[len];
        for (int i = 0; i < len; i++) {
            inviteChars[i] = chars[random.nextInt(chars.length)];
        }
        return String.valueOf(inviteChars).toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }

}
