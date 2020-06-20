package net.tespia.data;

import net.tespia.provider.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.slf4j.LoggerFactory;

public class ItemFactory {
    
   private static final org.slf4j.Logger log = LoggerFactory.getLogger(ItemFactory.class);


  private Map<EquipType, Map<Integer, EquipData>> equips = new ConcurrentHashMap<>();
  private Map<Integer, EquipType> types = new ConcurrentHashMap<>();
  private static final ItemFactory INSTANCE = new ItemFactory();


  public static ItemFactory getInstance() {
    return INSTANCE;
  }

  public EquipType getType(int itemID) {
    return this.types.get(itemID);
  }

  public EquipData getEquip(EquipType type, int itemID) {
    if (!equips.containsKey(type)) {
      return null;
    }
    if (!this.equips.get(type).containsKey(itemID)) {
      return null;
    }
    return this.equips.get(type).get(itemID);
  }

  private Optional<Integer> getField(MapleData data, String path) {
    MapleData field = data.getChildByPath(path);
    if (field == null || field.getData() == null) {
      return Optional.empty();
    } else {
      return Optional.of(MapleDataTool.getInt(field));
    }
  }

  private void getField(MapleData root, String path, Consumer<Integer> c) {
    try {
      if ("info".equals(path)) {
        return;
      }
      getField(root, path).ifPresent(c);
    } catch (Exception e) {
      log.error("Could not load path " + root.getName() + " - " + path);
    }
  }


  void loadFromXML(File file) {
    MapleDataProvider etc = MapleDataProviderFactory.getDataProvider(file);
    List<MapleDataDirectoryEntry> dirs = etc.getRoot().getSubdirectories();
    for (EquipType type : EquipType.values()) {
      dirs.parallelStream().forEach(e -> {
        if (e.getName().equals(type.name())) {
          log.info("Loading Character.wz :: " + type.name());
          Map<Integer, EquipData> equips = new HashMap<>();
          e.getFiles().parallelStream().forEach(f -> {
            int itemID = Integer.valueOf(f.getName().replace(".img", ""));
            String name = type.name() + "/" + f.getName();
            MapleData info = etc.getData(name).getChildByPath("info");
            EquipData equip = new EquipData();
            equip.itemID = itemID;
            getField(info, "reqJob", equip::setReqJob);
            getField(info, "reqLevel", equip::setReqLevel);
            getField(info, "reqSTR", equip::setReqSTR);
            getField(info, "reqDEX", equip::setReqDEX);
            getField(info, "reqINT", equip::setReqINT);
            getField(info, "reqLUK", equip::setReqLUK);
            getField(info, "reqPOP", equip::setReqPOP);
            getField(info, "incSTR", equip::setIncSTR);
            getField(info, "incDEX", equip::setIncDEX);
            getField(info, "incINT", equip::setIncINT);
            getField(info, "incLUK", equip::setIncLUK);
            getField(info, "incPDD", equip::setIncPDD);
            getField(info, "incMDD", equip::setIncMDD);
            getField(info, "incMAD", equip::setIncMAD);
            getField(info, "incPAD", equip::setIncPAD);
            getField(info, "tuc", equip::setTuc);
            if (equip.tuc == 0) {
              equip.tuc = 7;
            }
            getField(info, "price", equip::setPrice);
            getField(info, "cash", equip::setCash);
            getField(info, "cursed", equip::setCursed);
            getField(info, "success", equip::setSuccess);
            getField(info, "equipTradeBlock", equip::setEquipTradeBlock);
            getField(info, "durability", equip::setDurability);

            if (EquipData.isMagicWeapon(itemID)) {
              getField(info, "elemDefault", equip::setElemDefault);
              getField(info, "incRMAS", equip::setIncRMAS);
              getField(info, "incRMAF", equip::setIncRMAF);
              getField(info, "incRMAL", equip::setIncRMAL);
              getField(info, "incRMAI", equip::setIncRMAI);
            }
            types.put(itemID, type);
            equips.put(itemID, equip);
          });
          log.info("Loaded {} equips.", equips.size());
          this.equips.put(type, equips);
        }
      });
    }
  }
}
