# Руководство системного администратора EduSphere

**Стандарт:** ГОСТ 19.506-79

---

## 1. Требования к серверному окружению

| Компонент | Минимум | Рекомендуется |
|-----------|---------|---------------|
| CPU | 4 ядра | 8+ ядер |
| RAM | 8 GB | 16 GB |
| Диск | 50 GB SSD | 100 GB SSD |
| ОС | Linux (Ubuntu 22.04+) / Windows Server | Linux |
| Java | 21 (Temurin) | 21 LTS |
| PostgreSQL | 16 | 16 |
| Docker | 24+ | 24+ |
| Docker Compose | 2.20+ | 2.20+ |

### Порты

| Сервис | Порт |
|--------|------|
| API Gateway | 8080 |
| Auth Service | 8081 |
| User Service | 8082 |
| Materials Service | 8083 |
| Curriculum Service | 8084 |
| Adaptive Learning | 8085 |
| Grading Service | 8086 |
| Analytics Service | 8087 |
| AR Session Service | 8088 |
| PostgreSQL | 5432 |
| Redis | 6379 |
| Frontend | 3000 |

---

## 2. Установка

### 2.1. Docker Compose (рекомендуется)

```bash
git clone <repository>
cd edusphere
docker compose up -d
```

### 2.2. Локальная разработка

```bash
# PostgreSQL
docker compose up -d postgres redis

# Backend
mvn clean install -DskipTests
mvn -pl auth-service spring-boot:run &
mvn -pl api-gateway spring-boot:run &

# Frontend
cd frontend && npm install && npm run dev
```

---

## 3. Конфигурация

### 3.1. Переменные окружения

| Переменная | Описание | По умолчанию |
|-----------|----------|-------------|
| `DB_HOST` | Хост PostgreSQL | localhost |
| `DB_PORT` | Порт PostgreSQL | 5432 |
| `DB_USER` | Пользователь БД | edusphere |
| `DB_PASSWORD` | Пароль БД | edusphere |
| `JWT_SECRET` | Секрет JWT (≥32 символов) | — |
| `ENCRYPTION_KEY` | Base64 AES-256 ключ | auto-generate |

### 3.2. Генерация ключа шифрования

```bash
openssl rand -base64 32
```

Установите результат в `ENCRYPTION_KEY` для всех сервисов.

---

## 4. API и протоколы

- **Протокол:** REST over HTTP/HTTPS
- **Формат:** JSON
- **Аутентификация:** JWT Bearer Token
- **Базовый URL:** `http://<host>:8080/api`

### Основные эндпоинты

```
POST /api/auth/register     — Регистрация
POST /api/auth/login        — Авторизация
GET  /api/materials         — Список материалов
POST /api/curriculum/plans/generate — Генерация плана ИИ
GET  /api/grading/grades/student/{id} — Оценки ученика
GET  /api/analytics/dashboard/student/{id} — Дашборд
GET  /api/ar/sessions/active — Активные AR-сессии
```

---

## 5. Интеграция с внешними системами

### LMS (Moodle, Canvas)

- REST API через API Gateway
- Webhook на `/api/curriculum/plans/generate` для импорта программ
- Синхронизация оценок через `/api/grading/grades`

### CRM

- Экспорт данных пользователей через User Service API
- Webhook уведомления о регистрации

---

## 6. Резервное копирование и восстановление

### 6.1. Резервное копирование PostgreSQL

```bash
docker exec edusphere-postgres pg_dumpall -U edusphere > backup_$(date +%Y%m%d).sql
```

### 6.2. Восстановление

```bash
docker exec -i edusphere-postgres psql -U edusphere < backup_20260101.sql
```

### 6.3. Рекомендации

- Ежедневное инкрементальное резервное копирование
- Еженедельное полное резервное копирование
- Хранение копий минимум 30 дней
- Тестирование восстановления ежемесячно

---

## 7. Обновление

```bash
docker compose pull
docker compose up -d --build
```

Миграции БД выполняются автоматически через Hibernate `ddl-auto: update`.

---

## 8. Мониторинг

- Health checks: `GET /actuator/health` на API Gateway
- Логи: `docker compose logs -f <service>`
- Метрики AR: `/api/ar/sessions/{id}/metrics`
