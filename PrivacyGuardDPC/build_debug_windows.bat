
@echo off
REM Build debug APK locally using the Gradle wrapper
setlocal
cd /d %~dp0
if not exist gradlew (
  echo gradlew not found. Open the project root where gradlew exists.
  exit /b 1
)
call gradlew assembleDebug || goto :eof
echo.
echo === Build complete ===
echo APK located at: app\build\outputs\apk\debug\app-debug.apk
