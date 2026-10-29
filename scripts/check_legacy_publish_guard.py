#!/usr/bin/env python3
"""Fail closed if SlimeTinker's legacy release workflow can publish on any event.

Existing v2.0.7 release bytes differ from current builds. Publication must be
replaced by a separately reviewed distinct-version, native-gated workflow.
"""
from __future__ import annotations

import re
import sys
from pathlib import Path

JOB = re.compile(r"(?ms)^  (?P<name>[a-zA-Z_][\w-]*):\s*\n(?P<body>.*?)(?=^  [a-zA-Z_][\w-]*:\s*$|\Z)")
JOB_IF = re.compile(r"(?m)^    if:\s*(?P<condition>.*?)\s*$")
WRITE = re.compile(r"(?m)^\s+contents:\s*write\s*$")


def violations(workflow: str) -> list[str]:
    """Return concrete publication policy violations, not syntax/parser errors."""
    jobs = {match.group('name'): match.group('body') for match in JOB.finditer(workflow)}
    if 'build' not in jobs or 'publish' not in jobs:
        return ['Missing expected build or disabled historical publish job']
    errors: list[str] = []
    conditions = JOB_IF.findall(jobs['publish'])
    if conditions != ['false']:
        errors.append(f'Historical publish job must have literal if: false (found {conditions!r})')
    if 'gh release ' in jobs['build'] or 'gh api repos/' in jobs['build']:
        errors.append('Build job contains a direct release API invocation')
    if WRITE.search(jobs['build']):
        errors.append('Build job may not have contents: write')
    if not re.search(r'(?m)^    needs:\s*build\s*$', jobs['publish']):
        errors.append('Disabled publish job must retain the expected build dependency')
    return errors


def main(argv: list[str] | None = None) -> int:
    args = argv if argv is not None else sys.argv[1:]
    if len(args) != 1:
        print('Usage: check_legacy_publish_guard.py <workflow.yml>', file=sys.stderr)
        return 2
    try:
        text = Path(args[0]).read_text(encoding='utf-8')
    except (OSError, UnicodeError) as error:
        print(f'Cannot read workflow: {error}', file=sys.stderr)
        return 2
    failures = violations(text)
    for failure in failures:
        print('FAIL: ' + failure, file=sys.stderr)
    if failures:
        return 1
    print('PASS: historical SlimeTinker publish job is unreachable on push, PR and dispatch')
    return 0


if __name__ == '__main__':
    raise SystemExit(main())
