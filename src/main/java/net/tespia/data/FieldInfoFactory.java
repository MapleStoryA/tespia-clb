package net.tespia.data;

import net.tespia.common.Position;
import net.tespia.provider.*;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldInfoFactory {

    private static FieldInfoFactory INSTANCE;
    private Map<Integer, FieldInfo> fields = new ConcurrentHashMap<>();
    private MapleDataProvider provider;
    private List<MapleDataDirectoryEntry> dirs;

    public static final FieldInfoFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FieldInfoFactory();
        }
        return INSTANCE;
    }

    public static void main(String[] args) {
        FieldInfoFactory.getInstance().load(new File("wz/Map.wz/Map"));
        FieldInfoFactory.getInstance().getInfo(100000000);

    }

    public void load(File file) {
        this.provider = MapleDataProviderFactory.getDataProvider(file);
        this.dirs = provider.getRoot().getSubdirectories();

    }

    public FieldInfo getInfo(int fieldID) {
        if (this.fields.containsKey(fieldID)) {
            return this.fields.get(fieldID);
        }
        String digit = String.format("%09d", fieldID);
        String path = "Map" + digit.substring(0, 1);
        MapleData data = this.provider.getData(path + "/" + digit + ".img");
        FieldInfo map = new FieldInfo();
        map.setFieldID(fieldID);
        mapInformationFields(map, data);
        mapFootholdTree(map, data);
        mapPortals(map, data);
        mapLifeInformation(map, data);
        fields.put(fieldID, map);
        return map;
    }

    private void mapLifeInformation(FieldInfo map, MapleData mapData) {
        MapleData lifes = mapData.getChildByPath("life");
        if (lifes == null) {
            return;
        }
        for (MapleData data : lifes) {
            final LifeInfo life = new LifeInfo();
            life.setTemplateID(MapleDataTool.getInt(data.getChildByPath("id")));
            life.setCy(MapleDataTool.getInt(data.getChildByPath("cy")));
            MapleData dF = data.getChildByPath("f");
            if (dF != null) {
                life.setF(MapleDataTool.getInt(dF));
            }
            life.setFoothold(MapleDataTool.getInt(data.getChildByPath("fh")));
            life.setRx0(MapleDataTool.getInt(data.getChildByPath("rx0")));
            life.setRx1(MapleDataTool.getInt(data.getChildByPath("rx1")));
            int x = MapleDataTool.getInt(data.getChildByPath("x"));
            int y = MapleDataTool.getInt(data.getChildByPath("y"));
            life.setPosition(new Position(x, y));
            if (MapleDataTool.getInt("hide", data, 0) == 1) {
                //life.setHide(true);
            }
            String type = MapleDataTool.getString(data.getChildByPath("type"));
            life.setType(type);
            map.addLife(life);
        }
    }

    private void mapPortals(FieldInfo map, MapleData data) {
        MapleData portals = data.getChildByPath("portal");
        if (portals == null) {
            return;
        }
        for (MapleData portal : portals) {
            int pt = MapleDataTool.getInt(portal.getChildByPath("pt"));
            PortalInfo info = new PortalInfo();
            if (pt == PortalInfo.DOOR_PORTAL) {
                info.setType(PortalInfo.PortalType.DOOR);
            } else {
                info.setType(PortalInfo.PortalType.PORTAL);
            }
            info.setId(Integer.valueOf(portal.getName()));
            info.setName(MapleDataTool.getString(portal.getChildByPath("pn")));
            info.setTarget(MapleDataTool.getString(portal.getChildByPath("tn")));
            info.setTargetMapID(MapleDataTool.getInt(portal.getChildByPath("tm")));
            info.setPosition(new Position(MapleDataTool.getInt(portal.getChildByPath("x")), MapleDataTool.getInt(portal.getChildByPath("y"))));
            map.addPortal(info);
        }
    }

    private void mapInformationFields(FieldInfo map, MapleData mapData) {
        MapleData mobRate = mapData.getChildByPath("info/mobRate");
        if (mobRate != null) {
            map.setMobRate(((Float) mobRate.getData()).floatValue());
        }
        //map.setClock(mapData.getChildByPath("clock") != null); //clock was changed in wz to have x,y,width,height
        map.setEverlast(MapleDataTool.getInt(mapData.getChildByPath("info/everlast"), 0) > 0);
        map.setTown(MapleDataTool.getInt(mapData.getChildByPath("info/town"), 0) > 0);
        map.setSoaring(MapleDataTool.getInt(mapData.getChildByPath("info/needSkillForFly"), 0) > 0);
        map.setPersonalShop(MapleDataTool.getInt(mapData.getChildByPath("info/personalShop"), 0) > 0);
        map.setForceMove(MapleDataTool.getInt(mapData.getChildByPath("info/lvForceMove"), 0));
        map.setHPDec(MapleDataTool.getInt(mapData.getChildByPath("info/decHP"), 0));
        map.setHPDecInterval(MapleDataTool.getInt(mapData.getChildByPath("info/decHPInterval"), 10000));
        map.setHPDecProtect(MapleDataTool.getInt(mapData.getChildByPath("info/protectItem"), 0));
        map.setForcedReturnMap(MapleDataTool.getInt(mapData.getChildByPath("info/forcedReturn"), 999999999));
        map.setTimeLimit(MapleDataTool.getInt(mapData.getChildByPath("info/timeLimit"), -1));
        map.setFieldLimit(MapleDataTool.getInt(mapData.getChildByPath("info/fieldLimit"), 0));
        map.setOnFirstUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onFirstUserEnter"), ""));
        map.setOnUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onUserEnter"), ""));
        map.setRecoveryRate(MapleDataTool.getFloat(mapData.getChildByPath("info/recovery"), 1));
        map.setFixedMob(MapleDataTool.getInt(mapData.getChildByPath("info/fixedMobCapacity"), 0));
        map.setConsumeItemCoolTime(MapleDataTool.getInt(mapData.getChildByPath("info/consumeItemCoolTime"), 0));
    }

    private void mapFootholdTree(FieldInfo map, MapleData mapData) {
        if (mapData.getChildByPath("foothold") == null) {
            return;
        }
        Point uBound = new Point();
        MapleFoothold fh;
        List<MapleFoothold> allFootholds = new LinkedList<>();
        Point lBound = new Point();
        for (MapleData footRoot : mapData.getChildByPath("foothold")) {
            for (MapleData footCat : footRoot) {
                for (MapleData footHold : footCat) {
                    fh = new MapleFoothold(new Point(
                            MapleDataTool.getInt(footHold.getChildByPath("x1")), MapleDataTool.getInt(footHold.getChildByPath("y1"))), new Point(
                            MapleDataTool.getInt(footHold.getChildByPath("x2")), MapleDataTool.getInt(footHold.getChildByPath("y2"))), Integer.parseInt(footHold.getName()));
                    fh.setPrev((short) MapleDataTool.getInt(footHold.getChildByPath("prev")));
                    fh.setNext((short) MapleDataTool.getInt(footHold.getChildByPath("next")));

                    if (fh.getX1() < lBound.x) {
                        lBound.x = fh.getX1();
                    }
                    if (fh.getX2() > uBound.x) {
                        uBound.x = fh.getX2();
                    }
                    if (fh.getY1() < lBound.y) {
                        lBound.y = fh.getY1();
                    }
                    if (fh.getY2() > uBound.y) {
                        uBound.y = fh.getY2();
                    }
                    allFootholds.add(fh);
                }
            }
        }
        MapleFootholdTree fTree = new MapleFootholdTree(lBound, uBound);
        for (MapleFoothold foothold : allFootholds) {
            fTree.insert(foothold);
        }
        map.setFootholds(fTree);
    }

    public Map<Integer, FieldInfo> getFields() {
        return this.fields;
    }

}
