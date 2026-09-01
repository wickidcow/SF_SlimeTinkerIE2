package io.github.sefiraat.slimetinker.items.tinkermaterials.setup;

import io.github.sefiraat.slimetinker.SlimeTinker;
import io.github.sefiraat.slimetinker.items.Materials;
import io.github.sefiraat.slimetinker.items.tinkermaterials.TinkerMaterial;
import io.github.sefiraat.slimetinker.utils.Ids;
import io.github.sefiraat.slimetinker.utils.SkullTextures;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

@SuppressWarnings("SpellCheckingInspection")
public final class TinkersMaterialsSlimefunWarfare {

    private static final Map<String, TinkerMaterial> CM_MAP = new HashMap<>();

    private TinkersMaterialsSlimefunWarfare() {
        throw new UnsupportedOperationException("Utility Class");
    }

    static {
        registerSegganesson();
        TinkerMaterial slimesteel = registerSlimesteel();
        registerReinforcedSlimesteel(slimesteel);
        registerOsmium();
        registerOsmiumSuperalloy();
        registerUnpatentablium();
    }

    private static void registerSegganesson() {
        ItemStack source = resolveWarfareItem("SEGGANESSON");
        if (source == null) {
            return;
        }

        TinkerMaterial material = new TinkerMaterial(Ids.SEGGANESSON, source, "#4bacbf")
            .setLiquidTexture(SkullTextures.ALLOY_BLUE_PALE)
            .setTraitToolHead(Traits.SFW_SEGGANESSON_HEAD)
            .setTraitToolRod(Traits.SFW_SEGGANESSON_ROD)
            .setTraitArmorPlates(Traits.SFW_SEGGANESSON_PLATES)
            .setTraitArmorLinks(Traits.SFW_SEGGANESSON_LINKS)
            .setFormIngot("SEGGANESSON")
            .build();

        CM_MAP.put(Ids.SEGGANESSON, material);
    }

    private static TinkerMaterial registerSlimesteel() {
        ItemStack source = resolveWarfareItem("SLIMESTEEL_INGOT");
        if (source == null) {
            return null;
        }

        TinkerMaterial material = new TinkerMaterial(Ids.SLIMESTEEL, source, "#aed197")
            .setLiquidTexture(SkullTextures.ALLOY_GREEN)
            .setTraitToolBinder(Traits.SFW_SLIMESTEEL_BINDING)
            .setTraitArmorGambeson(Traits.SFW_SLIMESTEEL_GAMBESON)
            .setFormNugget(Materials.NUGGET_CAST_SLIMESTEEL.getItemId())
            .setFormIngot("SLIMESTEEL_INGOT")
            .setFormBlock(Materials.BLOCK_CAST_SLIMESTEEL.getItemId())
            .addAlloyRecipe(
                TinkersMaterialsCore.getCmMap().get(Ids.STEEL).getLiquidItemStack(1),
                TinkersMaterialsCore.getCmMap().get(Ids.SLIME).getLiquidItemStack(1)
            )
            .build();

        CM_MAP.put(Ids.SLIMESTEEL, material);
        return material;
    }

    private static void registerReinforcedSlimesteel(TinkerMaterial slimesteel) {
        ItemStack source = resolveWarfareItem("REINFORCED_SLIMESTEEL_INGOT");
        if (source == null) {
            return;
        }
        if (slimesteel == null) {
            warn("Skipping Warfare Tinker material REINFORCED_SLIMESTEEL_INGOT because SLIMESTEEL_INGOT is unavailable.");
            return;
        }

        TinkerMaterial material = new TinkerMaterial(Ids.REINFORCED_SLIMESTEEL, source, "#aed197")
            .setLiquidTexture(SkullTextures.ALLOY_GREEN)
            .setTraitToolHead(Traits.SFW_REINFORCED_SLIMESTEEL_HEAD)
            .setTraitToolRod(Traits.SFW_REINFORCED_SLIMESTEEL_ROD)
            .setTraitArmorPlates(Traits.SFW_REINFORCED_SLIMESTEEL_PLATES)
            .setTraitArmorLinks(Traits.SFW_REINFORCED_SLIMESTEEL_LINKS)
            .setFormNugget(Materials.NUGGET_CAST_REINFORCED_SLIMESTEEL.getItemId())
            .setFormIngot("REINFORCED_SLIMESTEEL_INGOT")
            .setFormBlock(Materials.BLOCK_CAST_REINFORCED_SLIMESTEEL.getItemId())
            .addAlloyRecipe(
                slimesteel.getLiquidItemStack(1),
                TinkersMaterialsCore.getCmMap().get(Ids.SLIME).getLiquidItemStack(9),
                TinkersMaterialsCore.getCmMap().get(Ids.DAMASCUS_STEEL).getLiquidItemStack(1),
                TinkersMaterialsCore.getCmMap().get(Ids.HARDENED_METAL).getLiquidItemStack(1),
                TinkersMaterialsCore.getCmMap().get(Ids.CORINTHIAN_BRONZE).getLiquidItemStack(1),
                TinkersMaterialsCore.getCmMap().get(Ids.ALUMINUM_BRONZE).getLiquidItemStack(1)
            )
            .build();

        CM_MAP.put(Ids.REINFORCED_SLIMESTEEL, material);
    }

    private static void registerOsmium() {
        ItemStack source = resolveWarfareItem("OSMIUM_INGOT");
        if (source == null) {
            return;
        }

        TinkerMaterial material = new TinkerMaterial(Ids.OSMIUM, source, "#8dd6c0")
            .setLiquidTexture(SkullTextures.ALLOY_BLUE_PALE)
            .setTraitToolHead(Traits.SFW_OSMIUM_HEAD)
            .setTraitToolRod(Traits.SFW_OSMIUM_ROD)
            .setTraitArmorPlates(Traits.SFW_OSMIUM_PLATES)
            .setTraitArmorLinks(Traits.SFW_OSMIUM_LINKS)
            .setFormNugget(Materials.NUGGET_CAST_OSMIUM.getItemId())
            .setFormIngot("OSMIUM_INGOT")
            .setFormBlock(Materials.BLOCK_CAST_OSMIUM.getItemId())
            .build();

        CM_MAP.put(Ids.OSMIUM, material);
    }

    private static void registerOsmiumSuperalloy() {
        ItemStack source = resolveWarfareItem("OSMIUM_SUPERALLOY");
        if (source == null) {
            return;
        }

        TinkerMaterial material = new TinkerMaterial(Ids.OSMIUM_SUPERALLOY, source, "#8dd6c0")
            .setLiquidTexture(SkullTextures.ALLOY_BLUE_PALE)
            .setTraitToolHead(Traits.SFW_OSMIUM_SUPERALLOY_HEAD)
            .setTraitToolRod(Traits.SFW_OSMIUM_SUPERALLOY_ROD)
            .setTraitArmorPlates(Traits.SFW_OSMIUM_SUPERALLOY_PLATES)
            .setTraitArmorLinks(Traits.SFW_OSMIUM_SUPERALLOY_LINKS)
            .setFormNugget(Materials.NUGGET_CAST_OSMIUM_SUPER_ALLOY.getItemId())
            .setFormIngot("OSMIUM_SUPERALLOY")
            .setFormBlock(Materials.BLOCK_CAST_OSMIUM_SUPER_ALLOY.getItemId())
            .build();

        CM_MAP.put(Ids.OSMIUM_SUPERALLOY, material);
    }

    private static void registerUnpatentablium() {
        ItemStack source = resolveWarfareItem("UNPATENTABLIUM");
        if (source == null) {
            return;
        }

        TinkerMaterial material = new TinkerMaterial(Ids.UNPATENTABLIUM, source, "#8dd6c0")
            .setLiquidTexture(SkullTextures.ALLOY_BLUE_PALE)
            .setTraitToolHead(Traits.SFW_UNPATENTABLIUM_HEAD)
            .setTraitToolRod(Traits.SFW_UNPATENTABLIUM_ROD)
            .setTraitArmorPlates(Traits.SFW_UNPATENTABLIUM_PLATES)
            .setTraitArmorLinks(Traits.SFW_UNPATENTABLIUM_LINKS)
            .setFormNugget(Materials.NUGGET_CAST_UNPATENTABILUM.getItemId())
            .setFormIngot("UNPATENTABLIUM")
            .setFormBlock(Materials.BLOCK_CAST_UNPATENTABILUM.getItemId())
            .build();

        CM_MAP.put(Ids.UNPATENTABLIUM, material);
    }

    private static ItemStack resolveWarfareItem(String itemId) {
        SlimefunItem item = SlimefunItem.getById(itemId);
        if (item == null) {
            warn("Skipping Warfare Tinker material " + itemId + ": Slimefun item is not registered.");
            return null;
        }

        ItemStack stack = item.getItem();
        if (stack == null) {
            warn("Skipping Warfare Tinker material " + itemId + ": registered Slimefun item has no ItemStack.");
            return null;
        }
        return stack;
    }

    private static void warn(String message) {
        SlimeTinker plugin = SlimeTinker.getInstance();
        if (plugin != null) {
            plugin.getLogger().warning(message);
        }
    }

    public static Map<String, TinkerMaterial> getCmMap() {
        return CM_MAP;
    }
}
