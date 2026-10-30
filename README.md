# Order Pipeline

An event-driven order and inventory system built with Java 21, Spring Boot and Apache Kafka.

```
Order API ──► order-events ──► Inventory Service
 (8080)        (Kafka topic)      (8081)
```

- **order-service** accepts orders over REST and publishes them to Kafka.
- **inventory-service** consumes order events and deducts stock in PostgreSQL.

## Running locally

Requirements: Java 21 and Docker.

```bash
# Start Kafka (KRaft mode) and PostgreSQL
docker compose up -d

# In separate terminals
cd order-service && ./mvnw spring-boot:run
cd inventory-service && ./mvnw spring-boot:run
```

On Windows PowerShell, use `.\mvnw.cmd spring-boot:run`.

Stop the infrastructure with `docker compose down`. Add `-v` to also delete the Kafka and Postgres data.

## Configuration

Each service reads its settings from `application.yml`, with local defaults that can be overridden by environment variables:

| Variable | Default |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/<service db>` |
| `DB_USERNAME` / `DB_PASSWORD` | `pipeline` / `pipeline` (local only) |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` |
