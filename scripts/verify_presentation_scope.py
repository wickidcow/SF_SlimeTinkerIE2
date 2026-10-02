#!/usr/bin/env python3
"""Release-specific preservation check for 2.0.7; update deliberately for future work."""
from pathlib import Path
import hashlib
import subprocess
import xml.etree.ElementTree as ET

BASE = '051a008d56bce6c1db40d93c7d5c3f3038421509'
PATH = 'src/main/java/io/github/sefiraat/slimetinker/utils/ItemUtils.java'
HELPER = 'src/main/java/io/github/sefiraat/slimetinker/utils/TinkerItemPresentation.java'

def old(path):
    return subprocess.check_output(['git', 'show', f'{BASE}:{path}'])

def blob(data):
    return hashlib.sha1(b'blob ' + str(len(data)).encode() + b'\0' + data).hexdigest()

source = old(PATH)
assert blob(source) == '707cd6e386bdbfdbf283936674deb755906b3e84'
expected = source.decode().replace('import net.md_5.bungee.api.ChatColor;\n', '')
expected = expected.replace('public final class ItemUtils {', 'public final class ItemUtils {\n\n    // Retained legacy output protocol of public String formatting helpers.\n    private static final String LEGACY_WHITE = "\\u00a7f";')
expected = expected.replace('ChatColor.WHITE', 'LEGACY_WHITE').replace('im.setLore(lore);', 'im.lore(TinkerItemPresentation.lore(lore));').replace('im.setDisplayName(name);', 'im.displayName(TinkerItemPresentation.line(name));')
assert Path(PATH).read_bytes() == expected.encode(), 'Non-presentation ItemUtils change'
assert blob(Path(HELPER).read_bytes()) == '845eefcda71d9ce846f3fea0b806a8a829788bfa'
changed = subprocess.check_output(['git', 'diff', '--name-only', BASE, 'HEAD', '--', 'src/main']).decode().splitlines()
assert set(changed) == {PATH, HELPER}, ('Unexpected production/resource changes', changed)
ns = {'m': 'http://maven.apache.org/POM/4.0.0'}
before, after = ET.fromstring(old('pom.xml')), ET.parse('pom.xml').getroot()
for name in ('groupId', 'artifactId', 'licenses', 'developers', 'scm', 'distributionManagement', 'profiles'):
    assert ET.tostring(before.find(f'm:{name}', ns)) == ET.tostring(after.find(f'm:{name}', ns)), f'Changed Maven identity/credits: {name}'
for node in before.findall('m:dependencies/m:dependency', ns):
    group = node.findtext('m:groupId', namespaces=ns)
    artifact = node.findtext('m:artifactId', namespaces=ns)
    if (group, artifact) in {('io.papermc.paper', 'paper-api'), ('com.github.SlimefunGuguProject', 'Slimefun4')}:
        continue
    matching = [d for d in after.findall('m:dependencies/m:dependency', ns) if d.findtext('m:groupId', namespaces=ns) == group and d.findtext('m:artifactId', namespaces=ns) == artifact]
    assert len(matching) == 1 and ET.tostring(node) == ET.tostring(matching[0]), ('Changed dependency', group, artifact)
print('Only the final item-presentation boundary changed; runtime identities, resources, dependencies and credits retained.')
