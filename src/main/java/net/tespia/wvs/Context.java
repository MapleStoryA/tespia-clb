/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.wvs;

import net.tespia.GameScripting;
import net.tespia.net.ClientSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sin
 */
public class Context {

    private int accountID;
    private int gender;
    private int gradeCode;
    private String userID;
    private int purchaseExp;
    private int chatBlockReason;
    private long dtChatUnblockDate;
    private long dtRegisterDate;
    private byte countryID;
    private Map<Integer, User> users = new HashMap<>();
    private int numberOfChars = 0;
    private byte maxChannel;
    private byte currentChannel;


    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(int gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getPurchaseExp() {
        return purchaseExp;
    }

    public void setPurchaseExp(int purchaseExp) {
        this.purchaseExp = purchaseExp;
    }

    public int getChatBlockReason() {
        return chatBlockReason;
    }

    public void setChatBlockReason(int chatBlockReason) {
        this.chatBlockReason = chatBlockReason;
    }

    public long getDtChatUnblockDate() {
        return dtChatUnblockDate;
    }

    public void setDtChatUnblockDate(long dtChatUnblockDate) {
        this.dtChatUnblockDate = dtChatUnblockDate;
    }

    public long getDtRegisterDate() {
        return dtRegisterDate;
    }

    public void setDtRegisterDate(long dtRegisterDate) {
        this.dtRegisterDate = dtRegisterDate;
    }

    public void setCountryID(byte countryID) {
        this.countryID = countryID;
    }

    public void addUser(User user) {
        users.put(numberOfChars++, user);
    }

    public User getUser(int index) {
        return users.get(index);
    }

    public void updateSocket(ClientSocket socket) {
        this.users.values().forEach(e -> {
            e.setSocket(socket);
        });
    }

    public byte getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(byte currentChannel) {
        this.currentChannel = currentChannel;
    }

    public void setMaxChannel(byte maxChannel) {
        this.maxChannel = maxChannel;
    }

    public byte getMaxChannel() {
        return maxChannel;
    }
}
