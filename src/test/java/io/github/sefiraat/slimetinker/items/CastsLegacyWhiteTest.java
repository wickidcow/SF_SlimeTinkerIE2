package io.github.sefiraat.slimetinker.items;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Guard historical cast-lore strings while removing deprecated Bukkit colors. */
class CastsLegacyWhiteTest {

    @Test
    void whiteCodeRemainsTheExactLegacySectionSequence() {
        // Former org.bukkit.ChatColor.WHITE.toString(): section sign + 'f'.
        assertEquals("\u00a7f", Casts.LEGACY_WHITE);
    }

    @Test
    void inputAmountTextRetainsOriginalSpacingAndColor() {
        assertEquals("Input : \u00a7f9 Units",
            Casts.INPUT_DESC + Casts.LEGACY_WHITE + 9 + Casts.UNIT_DESC);
    }

    @Test
    void outputTextRetainsOriginalSpacingAndColor() {
        assertEquals("Output : \u00a7f1 Nugget",
            Casts.OUTPUT_DESC + Casts.LEGACY_WHITE + "1 Nugget");
    }
}
