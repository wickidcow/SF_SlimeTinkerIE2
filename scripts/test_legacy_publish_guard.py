"""Policy tests that prove the exact historical publication trigger cannot revive."""
from __future__ import annotations

import subprocess
import sys
import tempfile
import unittest
from pathlib import Path

from check_legacy_publish_guard import violations

SCRIPT = Path(__file__).with_name('check_legacy_publish_guard.py')
WORKFLOW = Path(__file__).resolve().parents[1] / '.github' / 'workflows' / 'build.yml'

FIXTURE = '''name: Build SlimeTinker
jobs:
  build:
    runs-on: ubuntu-latest
    permissions:
      contents: read
    steps:
      - run: echo compile
  publish:
    needs: build
    if: false
    runs-on: ubuntu-latest
    permissions:
      contents: write
    steps:
      - run: gh release create v2.0.7
'''


class DisabledHistoricalPublicationTests(unittest.TestCase):
    def test_policy_accepts_dormant_release(self):
        self.assertEqual([], violations(FIXTURE))

    def test_auto_publish_on_master_push_fails(self):
        altered = FIXTURE.replace('    if: false', "    if: github.event_name == 'push' && github.ref == 'refs/heads/master'")
        self.assertIn('literal if: false', violations(altered)[0])

    def test_dispatch_true_and_boolean_true_fail(self):
        for condition in ('true', "github.event_name == 'workflow_dispatch'"):
            with self.subTest(condition=condition):
                self.assertTrue(violations(FIXTURE.replace('    if: false', f'    if: {condition}')))

    def test_missing_or_duplicate_gate_fails(self):
        self.assertTrue(violations(FIXTURE.replace('    if: false\n', '')))
        self.assertTrue(violations(FIXTURE.replace('    if: false\n', '    if: false\n    if: true\n')))

    def test_no_releases_in_build_job(self):
        changed = FIXTURE.replace('      - run: echo compile', '      - run: gh release create v2.0.7')
        self.assertTrue(violations(changed))

    def test_build_cannot_acquire_release_write_permissions(self):
        changed = FIXTURE.replace('      contents: read', '      contents: write')
        self.assertTrue(violations(changed))

    def test_missing_job_or_dependency_fails(self):
        self.assertTrue(violations(FIXTURE.replace('  publish:\n', '  retired:\n')))
        self.assertTrue(violations(FIXTURE.replace('    needs: build\n', '')))

    def test_actual_workflow_and_cli(self):
        actual = WORKFLOW.read_text(encoding='utf-8')
        self.assertEqual([], violations(actual))
        with tempfile.TemporaryDirectory() as directory:
            file = Path(directory) / 'build.yml'
            file.write_text(actual, encoding='utf-8')
            result = subprocess.run([sys.executable, str(SCRIPT), str(file)], capture_output=True, text=True)
            self.assertEqual(0, result.returncode, result.stdout + result.stderr)
            file.write_text(actual.replace('    if: false', '    if: true'), encoding='utf-8')
            result = subprocess.run([sys.executable, str(SCRIPT), str(file)], capture_output=True, text=True)
            self.assertEqual(1, result.returncode, result.stdout + result.stderr)

    def test_missing_workflow_file_fails_closed(self):
        result = subprocess.run([sys.executable, str(SCRIPT), '/definitely/not/a/workflow.yml'], capture_output=True, text=True)
        self.assertNotEqual(0, result.returncode)


if __name__ == '__main__':
    unittest.main()
