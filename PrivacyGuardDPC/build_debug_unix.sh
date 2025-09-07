
#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
chmod +x gradlew || true
./gradlew assembleDebug
echo
echo "=== Build complete ==="
echo "APK located at: app/build/outputs/apk/debug/app-debug.apk"
