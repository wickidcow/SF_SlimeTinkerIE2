package io.github.sefiraat.slimetinker.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

/** Generated-text API boundary tests, not a simulation of tool traits or player combat. */
class TinkerItemPresentationTest {
    @Test void plainGeneratedNamesDoNotInheritItemItalics() {
        var result=TinkerItemPresentation.line("Iron-Copper-Oak Pickaxe");
        assertEquals("Iron-Copper-Oak Pickaxe",plain(result));
        assertEquals(TextDecoration.State.FALSE,result.decoration(TextDecoration.ITALIC));
    }
    @Test void explicitItalicsRemainExplicit() {
        assertEquals(TextDecoration.State.TRUE,TinkerItemPresentation.line("§oNamed tool").decoration(TextDecoration.ITALIC));
    }
    @Test void explicitBoldAndClassicColorRemain() {
        var result=TinkerItemPresentation.line("§c§lRed tool");
        assertEquals(NamedTextColor.RED,result.color());
        assertEquals(TextDecoration.State.TRUE,result.decoration(TextDecoration.BOLD));
    }
    @Test void oldBungeeHexColorIsNotReducedToNearestNamedColor() {
        var result=TinkerItemPresentation.line("§x§a§1§b§2§c§3Copper");
        assertEquals(TextColor.color(0xa1b2c3),result.color());
        assertEquals("Copper",plain(result));
    }
    @Test void formattedTextSegmentsKeepTheirPriorSerializedMeaning() {
        String value="§aIron-§bCopper-§6Oak §fPickaxe";
        assertEquals(value,LegacyComponentSerializer.legacySection().serialize(TinkerItemPresentation.line(value)));
    }
    @Test void literalAmpersandsAreNotNewFormattingCodes() {
        assertEquals("A&B &cMaterials",plain(TinkerItemPresentation.line("A&B &cMaterials")));
    }
    @Test void levelExperienceAndModifierTextRemainExact() {
        var lines=List.of("§aLevel: §f12§7 (17 / 4096)","§aModifier Slots: §f3","§eLuck Level 2§7 - (MAX)");
        assertEquals(List.of("Level: 12 (17 / 4096)","Modifier Slots: 3","Luck Level 2 - (MAX)"),
                TinkerItemPresentation.lore(lines).stream().map(TinkerItemPresentationTest::plain).toList());
    }
    @Test void orderDuplicatesAndBlankSeparatorsAreRetained() {
        var lines=List.of("","§bSame","§bSame","", "Last");
        assertEquals(List.of("","Same","Same","","Last"),TinkerItemPresentation.lore(lines).stream().map(TinkerItemPresentationTest::plain).toList());
    }
    @Test void renderingDoesNotMutateItsSourceList() {
        var source=new ArrayList<>(List.of("First","Second"));
        var result=TinkerItemPresentation.lore(source);
        result.clear();
        assertEquals(List.of("First","Second"),source);
    }
    @Test void emptyDescriptionRemainsEmpty() {
        assertEquals("",plain(TinkerItemPresentation.line("")));
        assertTrue(TinkerItemPresentation.lore(List.of()).isEmpty());
    }
    @Test void unicodeAndPunctuationSurvive() {
        assertEquals("Ω → Tool's (3/9) #42",plain(TinkerItemPresentation.line("§aΩ → Tool's (3/9) #42")));
    }
    @Test void malformedGeneratedInputsFailBeforeAnyItemSetterCanBeCalled() {
        assertThrows(NullPointerException.class,()->TinkerItemPresentation.line(null));
        assertThrows(NullPointerException.class,()->TinkerItemPresentation.lore(null));
        var source=new ArrayList<String>(); source.add("valid"); source.add(null);
        assertThrows(NullPointerException.class,()->TinkerItemPresentation.lore(source));
        assertEquals(2,source.size());
    }
    @Test void deterministicGeneratedLayoutsRetainPlainTextAndInputValues() {
        Random random=new Random(950126L);
        String[] colors={"§a","§b","§7","§f","§6","§x§a§1§b§2§c§3"};
        for(int example=0;example<1000;example++) {
            List<String> generated=new ArrayList<>(), expected=new ArrayList<>();
            for(int i=random.nextInt(20);i>0;i--) {
                String value="Material-"+i+" ("+random.nextInt(9999)+"/10000)";
                expected.add(value); generated.add(colors[random.nextInt(colors.length)]+value);
            }
            var before=List.copyOf(generated);
            var result=TinkerItemPresentation.lore(generated);
            assertEquals(expected,result.stream().map(TinkerItemPresentationTest::plain).toList());
            assertEquals(before,generated);
        }
    }
    private static String plain(Component value) { return PlainTextComponentSerializer.plainText().serialize(value); }
}
