package io.github.sefiraat.slimetinker.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/** Renders already-generated legacy descriptions; does not inspect or mutate item data. */
final class TinkerItemPresentation {
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private TinkerItemPresentation() {}

    static Component line(String value) {
        return LEGACY.deserialize(Objects.requireNonNull(value, "generated description"))
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    static List<Component> lore(List<String> generated) {
        Objects.requireNonNull(generated, "generated lore");
        List<Component> result = new ArrayList<>(generated.size());
        for (String value : generated) result.add(line(value));
        return result;
    }
}
