
# PrivacyGuard DPC (Android 12+) — IG-only Identifier Blocking

**Goal**: Make this app the *Device Owner* (DPC) and block Instagram (`com.instagram.android`) from reading device identifiers on Android 12+, without touching your VPN or other apps.

## What it does
- Sets AppOps for `READ_DEVICE_IDENTIFIERS` and `READ_PHONE_STATE` to `MODE_IGNORED` for target apps.
- Forces dangerous permission `READ_PHONE_STATE` to **DENIED** for target apps via DevicePolicyManager.
- Default target: **Instagram only**. You can expand the blocklist in UI.
- No VpnService, no traffic interception, no DNS changes.

## Build
- Android Studio (JDK 17), Gradle 8.x, AGP compatible.
- Open the project and build `app` module.

## Become Device Owner (TEST / PoC via ADB)
> The device must be factory-reset or in the initial setup state.

```
adb install app-release.apk
adb shell dpm set-device-owner "com.example.privacyguard/.admin.AdminReceiver"
```

If succeeded, launch the app and tap **"一鍵：只鎖 Instagram 識別碼讀取"**.

## QR Provisioning (ENTERPRISE)
Prepare a standard Android Enterprise QR with these extras (replace URL and checksum):

```json
{
  "android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME": "com.example.privacyguard/.admin.AdminReceiver",
  "android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM": "BASE64_APK_CERT_SHA256",
  "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION": "https://yourdomain.com/apks/privacyguarddpc.apk",
  "android.app.extra.PROVISIONING_SKIP_USER_CONSENT": true,
  "android.app.extra.PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED": true
}
```

## Verify
```
adb shell cmd appops get com.instagram.android
# Expect READ_DEVICE_IDENTIFIERS=ignore, READ_PHONE_STATE=ignore
```

## Notes
- ANDROID_ID remains app-scoped; IG still has its own but cannot correlate across apps.
- GAID is system-wide; disable globally in Settings if you want (affects all apps).
- System/privileged apps are out of scope for DPC controls.


---

## Build an APK without Android Studio

### Option A — GitHub Actions (one-click in the cloud)
1. Push this project to a new GitHub repo.
2. Open **Actions** tab → run **Android Debug APK** workflow.
3. When it finishes, download the artifact: **app-debug-apk** → `app-debug.apk`.

### Option B — Local build scripts
- **Windows**: double‑click `build_debug_windows.bat`
- **macOS/Linux**: run `./build_debug_unix.sh`

The APK will be at:
```
app/build/outputs/apk/debug/app-debug.apk
```

> Debug APK is already signed with the debug keystore and can be installed via:
```
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### (Optional) Release signing
Generate a keystore:
```
keytool -genkeypair -v -keystore pgdpc.jks -alias pg -keyalg RSA -keysize 2048 -validity 3650
```
Then configure signing in `app/build.gradle` (SigningConfigs) and build `assembleRelease`.
