package io.github.sefiraat.slimetinker.items.workstations.smeltery;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.sefiraat.slimetinker.items.Materials;
import io.github.sefiraat.slimetinker.utils.ThemeUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * Place-to-build guide item for the Tinker's Smeltery.
 *
 * <p>This is a Slimefun 4 compatible backport of the Slimefun 5 Smeltery
 * assembler behaviour. The existing controller remains placeable normally;
 * using this guide item builds the complete 3x3 vertical structure.</p>
 */
public class TinkersSmelteryMulti extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public TinkersSmelteryMulti(ItemGroup itemGroup,
                                SlimefunItemStack item,
                                RecipeType recipeType,
                                ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return event -> {
            event.cancel();

            if (!event.getClickedBlock().isPresent()) {
                return;
            }

            Player player = event.getPlayer();
            Block center = event.getClickedBlock().get()
                .getRelative(event.getClickedFace())
                .getRelative(0, 1, 0);

            Block[] blocks = getTargetBlocks(center, player);
            SlimefunItemStack[] layout = getLayout();

            for (Block block : blocks) {
                if (!block.isEmpty()) {
                    player.sendMessage(ThemeUtils.WARNING + "The Tinker's Smeltery needs a clear 3x3 vertical space.");
                    return;
                }

                if (!Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.PLACE_BLOCK)) {
                    player.sendMessage(ThemeUtils.WARNING + "You cannot build the Tinker's Smeltery here.");
                    return;
                }
            }

            for (int i = 0; i < blocks.length; i++) {
                placeSlimefunBlock(blocks[i], layout[i]);
            }

            if (player.getGameMode() != GameMode.CREATIVE) {
                ItemStack held = event.getItem();
                held.setAmount(Math.max(0, held.getAmount() - 1));
            }
        };
    }

    @Nonnull
    private static Block[] getTargetBlocks(@Nonnull Block center, @Nonnull Player player) {
        Block[] blocks = new Block[9];
        Vector direction = player.getLocation().getDirection();
        boolean spanX = Math.abs(direction.getZ()) >= Math.abs(direction.getX());
        int index = 0;

        for (int row = 0; row < 3; row++) {
            int yOffset = 1 - row;

            for (int column = 0; column < 3; column++) {
                int lateral = column - 1;
                blocks[index++] = spanX
                    ? center.getRelative(lateral, yOffset, 0)
                    : center.getRelative(0, yOffset, lateral);
            }
        }

        return blocks;
    }

    @Nonnull
    private static SlimefunItemStack[] getLayout() {
        return new SlimefunItemStack[] {
            Materials.SEARED_BRICK_BLOCK, Materials.SPOUT, Materials.SEARED_BRICK_BLOCK,
            Materials.SEARED_BRICK_BLOCK, Materials.SMELTERY_CONTROLLER, Materials.SEARED_BRICK_BLOCK,
            Materials.SEARED_BRICK_BLOCK, Materials.SEARED_TANK, Materials.SEARED_BRICK_BLOCK
        };
    }

    private static void placeSlimefunBlock(@Nonnull Block block, @Nonnull SlimefunItemStack item) {
        block.setType(item.getType(), false);
        BlockStorage.addBlockInfo(block, "id", item.getItemId(), true);
    }
}
