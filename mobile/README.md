# EduSphere Mobile (AR)

Мобильное приложение для AR-классов на React Native.

## Требования

- Android 14+ с ARCore
- iOS 18+ с ARKit
- Node.js 20+
- React Native CLI

## Запуск

```bash
npm install
npx react-native start
npx react-native run-android  # или run-ios
```

## AR-технологии

- **Android**: ARCore через ViroReact
- **iOS**: ARKit через ViroReact
- Целевые метрики: >30 FPS, задержка <150 мс

## API

Приложение подключается к API Gateway на `http://10.0.2.2:8080` (Android emulator).
Для реального устройства укажите IP сервера в `src/api/client.ts`.
