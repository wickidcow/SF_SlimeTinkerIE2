#!/usr/bin/env bash
# Install compile-only, checksum-pinned APIs. No downloaded implementation is shaded.
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
mkdir -p "$ROOT/.build-apis"
fetch_checked() {
  local url="$1" out="$2" sha="$3"
  curl --fail --location --retry 3 --connect-timeout 20 --max-time 180 "$url" -o "$out.tmp"
  printf '%s  %s\n' "$sha" "$out.tmp" | sha256sum -c -
  mv "$out.tmp" "$out"
}
fetch_checked 'https://github.com/wickidcow/Slimefun-Legacy/releases/download/v4.1.63/Slimefun-Legacy4.1.63.jar' "$ROOT/.build-apis/Slimefun-Legacy4.1.63.jar" '993b257c54efe19b58cc8f4946d4d39387f7787fe263c9f27123343159344c42'
fetch_checked 'https://github.com/wickidcow/SF_NetworksExp/releases/download/v1.0.47/SF_Networks1.0.47.jar' "$ROOT/.build-apis/Networks.jar" 'b9d63570c30f4d0195393b567bddcf48973e1c103302526ecebbb3f67106e9ed'
mvn -B -ntp install:install-file -Dfile="$ROOT/.build-apis/Slimefun-Legacy4.1.63.jar" -DgroupId=com.github.wickidcow -DartifactId=Slimefun-Legacy -Dversion=4.1.63 -Dpackaging=jar -DgeneratePom=true
mvn -B -ntp install:install-file -Dfile="$ROOT/.build-apis/Networks.jar" -DgroupId=dev.sefiraat -DartifactId=networks -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true
