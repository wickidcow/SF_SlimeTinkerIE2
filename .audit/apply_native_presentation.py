from pathlib import Path
import hashlib

def blob(data):
    return hashlib.sha1(b'blob ' + str(len(data)).encode() + b'\0' + data).hexdigest()

path=Path('src/main/java/io/github/sefiraat/slimetinker/utils/ItemUtils.java')
assert blob(path.read_bytes())=='707cd6e386bdbfdbf283936674deb755906b3e84'
text=path.read_text()
assert text.count('im.setLore(lore);')==2 and text.count('im.setDisplayName(name);')==1
text=text.replace('import net.md_5.bungee.api.ChatColor;\n','')
text=text.replace('public final class ItemUtils {','public final class ItemUtils {\n\n    // Retained legacy output protocol of public String formatting helpers.\n    private static final String LEGACY_WHITE = "\\u00a7f";')
text=text.replace('ChatColor.WHITE','LEGACY_WHITE').replace('im.setLore(lore);','im.lore(TinkerItemPresentation.lore(lore));').replace('im.setDisplayName(name);','im.displayName(TinkerItemPresentation.line(name));')
path.write_text(text)
assert blob(path.read_bytes())=='e02923e4b531c2d5a27f7a2e6d94b3711cf0572a'
path=Path('pom.xml')
assert blob(path.read_bytes())=='ced58e062ac85137eee0b0826f52b5440fc8740e'
text=path.read_text().replace('    <dependencies>','    <dependencies>\n        <dependency><groupId>org.junit.jupiter</groupId><artifactId>junit-jupiter</artifactId><version>5.12.2</version><scope>test</scope></dependency>',1)
text=text.replace('        <plugins>\n            <plugin>','        <plugins>\n            <plugin><groupId>org.apache.maven.plugins</groupId><artifactId>maven-surefire-plugin</artifactId><version>3.5.3</version></plugin>\n            <plugin>',1)
path.write_text(text)
assert blob(path.read_bytes())=='facec8d2c3d4680c709568bb20ae65ca31f3960c'
