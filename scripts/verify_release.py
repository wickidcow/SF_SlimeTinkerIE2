#!/usr/bin/env python3
"""Check a baseline-built plugin and actual JUnit reports; never invent coverage."""
import re
import struct
import sys
import xml.etree.ElementTree as ET
import zipfile
from pathlib import Path

jar, version = Path(sys.argv[1]), sys.argv[2]
assert re.fullmatch(r'\d+(?:\.\d+)+', version), version
reports = list(Path('evidence').glob('tests-*/TEST-*.xml'))
assert len(reports) >= 3, 'Missing API-lane test reports'
for path in reports:
    suite = ET.parse(path).getroot()
    assert int(suite.get('tests', '0')) > 0, (path, suite.attrib)
    assert all(int(suite.get(k, '0')) == 0 for k in ('failures', 'errors', 'skipped')), (path, suite.attrib)
    print(path, suite.attrib)
with zipfile.ZipFile(jar) as z:
    names = z.namelist()
    assert len(names) == len(set(names)), 'Duplicate JAR entries'
    assert z.testzip() is None, 'Corrupt JAR'
    descriptor = z.read('plugin.yml').decode()
    assert re.search(r'^name:\s*SlimeTinker\s*$', descriptor, re.M), descriptor
    assert re.search(r'^version:\s*[\"\x27]?' + re.escape(version) + r'[\"\x27]?\s*$', descriptor, re.M), descriptor
    assert any(n.endswith('/TinkerItemPresentation.class') for n in names), 'Renderer absent from distributable'
    count = 0
    for name in names:
        assert not name.startswith(('org/junit/', 'org/mockito/', 'org/mockbukkit/', 'be/seeseemelk/mockbukkit/', 'io/github/thebusybiscuit/slimefun4/', 'io/github/sefiraat/networks/')), name
        if not name.endswith('.class'):
            continue
        overlay = re.match(r'META-INF/versions/(\d+)/', name)
        if overlay and int(overlay.group(1)) > 21:
            continue
        data = z.read(name)
        assert len(data) >= 8 and data[:4] == b'\xca\xfe\xba\xbe', name
        minor, major = struct.unpack('>HH', data[4:8])
        assert major <= 65 and minor != 65535, (name, minor, major)
        count += 1
    assert count > 0, 'No Java classes'
    print(f'{jar}: {count} applicable Java 21 classes verified')
