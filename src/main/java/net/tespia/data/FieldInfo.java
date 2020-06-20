package net.tespia.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class FieldInfo {

    private int fieldID;
    private boolean isCloud;
    private boolean isTown;
    private boolean isSwing;
    private int returnMapID;
    private int forceReturnMapID;
    private double mobRate;
    private boolean fly;
    private String onFirstUserEnter;
    private String onUserEnter;
    private Map<String, PortalInfo> portals = new HashMap<>();
    private LinkedList<LifeInfo> life = new LinkedList<>();
    private boolean clock;
    private boolean everlast;
    private boolean town;
    private boolean soaring;
    private boolean personalShop;
    private int forceMove;
    private int HPDec;
    private int HPDecInterval;
    private int HPDecProtect;
    private int forcedReturnMap;
    private int timeLimit;
    private int fieldLimit;
    private String firstUserEnter;
    private float recoveryRate;
    private String userEnter;
    private int fixedMob;
    private int consumeItemCoolTime;
    private float monsterRate;
    private MapleFootholdTree footholds;

    public boolean isClock() {
        return clock;
    }

    public void addPortal(PortalInfo info) {
        this.portals.put(info.getName(), info);
    }

    public void addLife(LifeInfo life) {
        this.life.add(life);
    }

    public int getFieldID() {
        return fieldID;
    }

    public void setFieldID(int fieldID) {
        this.fieldID = fieldID;
    }

    public boolean isIsCloud() {
        return isCloud;
    }

    public void setIsCloud(boolean isCloud) {
        this.isCloud = isCloud;
    }

    public boolean isIsTown() {
        return isTown;
    }

    public void setIsTown(boolean isTown) {
        this.isTown = isTown;
    }

    public boolean isIsSwing() {
        return isSwing;
    }

    public void setIsSwing(boolean isSwing) {
        this.isSwing = isSwing;
    }

    public int getReturnMapID() {
        return returnMapID;
    }

    public void setReturnMapID(int returnMapID) {
        this.returnMapID = returnMapID;
    }

    public int getForceReturnMapID() {
        return forceReturnMapID;
    }

    public void setForceReturnMapID(int forceReturnMapID) {
        this.forceReturnMapID = forceReturnMapID;
    }

    public double getMobRate() {
        return mobRate;
    }

    public void setMobRate(double mobRate) {
        this.mobRate = mobRate;
    }

    public boolean isFly() {
        return fly;
    }

    public void setFly(boolean fly) {
        this.fly = fly;
    }

    public String getOnFirstUserEnter() {
        return onFirstUserEnter;
    }

    public void setOnFirstUserEnter(String onFirstUserEnter) {
        this.onFirstUserEnter = onFirstUserEnter;
    }

    public String getOnUserEnter() {
        return onUserEnter;
    }

    public void setOnUserEnter(String onUserEnter) {
        this.onUserEnter = onUserEnter;
    }

    public Map<String, PortalInfo> getPortals() {
        return portals;
    }

    public PortalInfo getPortalById(byte id) {
        Optional<PortalInfo> opt = portals.values()
                .stream()
                .filter(e -> e.getId() == id)
                .findFirst();

        if (opt.isPresent()) {
            return opt.get();
        } else {
            return portals.values().stream().findFirst().get();
        }
    }

    public void setPortals(Map<String, PortalInfo> portals) {
        this.portals = portals;
    }

    public LinkedList<LifeInfo> getLife() {
        return life;
    }

    public void setLife(LinkedList<LifeInfo> life) {
        this.life = life;
    }

    public boolean isEverlast() {
        return everlast;
    }

    public void setEverlast(boolean everlast) {
        this.everlast = everlast;
    }

    public boolean isTown() {
        return town;
    }

    public void setTown(boolean town) {
        this.town = town;
    }

    public boolean isSoaring() {
        return soaring;
    }

    public void setSoaring(boolean soaring) {
        this.soaring = soaring;
    }

    public boolean isPersonalShop() {
        return personalShop;
    }

    public void setPersonalShop(boolean personalShop) {
        this.personalShop = personalShop;
    }

    public int getForceMove() {
        return forceMove;
    }

    public void setForceMove(int forceMove) {
        this.forceMove = forceMove;
    }

    public int getHPDec() {
        return HPDec;
    }

    public void setHPDec(int HPDec) {
        this.HPDec = HPDec;
    }

    public int getHPDecInterval() {
        return HPDecInterval;
    }

    public void setHPDecInterval(int HPDecInterval) {
        this.HPDecInterval = HPDecInterval;
    }

    public int getHPDecProtect() {
        return HPDecProtect;
    }

    public void setHPDecProtect(int HPDecProtect) {
        this.HPDecProtect = HPDecProtect;
    }

    public int getForcedReturnMap() {
        return forcedReturnMap;
    }

    public void setForcedReturnMap(int forcedReturnMap) {
        this.forcedReturnMap = forcedReturnMap;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getFieldLimit() {
        return fieldLimit;
    }

    public void setFieldLimit(int fieldLimit) {
        this.fieldLimit = fieldLimit;
    }

    public String getFirstUserEnter() {
        return firstUserEnter;
    }

    public void setFirstUserEnter(String firstUserEnter) {
        this.firstUserEnter = firstUserEnter;
    }

    public float getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(float recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public String getUserEnter() {
        return userEnter;
    }

    public void setUserEnter(String userEnter) {
        this.userEnter = userEnter;
    }

    public int getFixedMob() {
        return fixedMob;
    }

    public void setFixedMob(int fixedMob) {
        this.fixedMob = fixedMob;
    }

    public int getConsumeItemCoolTime() {
        return consumeItemCoolTime;
    }

    public void setConsumeItemCoolTime(int consumeItemCoolTime) {
        this.consumeItemCoolTime = consumeItemCoolTime;
    }

    public float getMonsterRate() {
        return monsterRate;
    }

    public void setMonsterRate(float monsterRate) {
        this.monsterRate = monsterRate;
    }

    public MapleFootholdTree getFootholds() {
        return footholds;
    }

    public void setFootholds(MapleFootholdTree footholds) {
        this.footholds = footholds;
    }

}
