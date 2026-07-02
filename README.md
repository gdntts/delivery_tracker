# Delivery Tracker

API de rastreamento de localização de entregadores em tempo real. Recebe atualizações de coordenadas GPS via REST e as propaga instantaneamente para clientes conectados via WebSocket, sem necessidade de polling.

---

## Tecnologias

- **Java 21** + **Spring Boot 4.1**
- **PostgreSQL** — persistência e notificações nativas via `LISTEN/NOTIFY`
- **Spring WebSocket + STOMP** — comunicação em tempo real
- **Spring Data JPA + Hibernate** — mapeamento objeto-relacional
- **Flyway** — migrations e controle de versão do banco
- **Lombok** — redução de boilerplate
- **SpringDoc OpenAPI** — documentação interativa da API

---

## Pré-requisitos

- Java 21+
- Maven 3.9+
- PostgreSQL 15+ rodando localmente

---

## Configuração

### 1. Banco de dados

Crie o banco no PostgreSQL:

```sql
CREATE DATABASE delivery_tracker;
```

### 2. Variáveis de ambiente

Crie o arquivo `src/main/resources/application-local.properties` com as credenciais:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/delivery_tracker
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

> O perfil `local` já está ativo por padrão em `application.properties`. O Flyway cria todas as tabelas automaticamente na primeira execução.

### 3. Executar

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

---

## Endpoints

### `POST /api/location`

Recebe a posição atual de um entregador e persiste no banco. Dispara notificação WebSocket para os clientes que acompanham o pedido.

**Corpo da requisição:**
```json
{
  "orderId": "uuid-do-pedido",
  "lat": -23.5505,
  "lng": -46.6333
}
```

**Resposta:** `201 Created` (sem corpo)

**Erro — pedido não encontrado:**
```json
{
  "timestamp": "2026-06-22T14:30:00",
  "status": 400,
  "error": "Business Rule Violation",
  "messages": ["Pedido não encontrado: uuid-do-pedido"]
}
```

**Documentação interativa (Swagger):** `http://localhost:8080/swagger-ui.html`

---

## WebSocket

### Conexão

```
ws://localhost:8080/ws
```

Compatível com SockJS para ambientes que não suportam WebSocket nativo.

### Inscrição em um pedido

Após conectar via STOMP, inscreva-se no tópico do pedido que deseja acompanhar:

```
/topic/order.{orderId}
```

### Payload recebido

```json
{
  "orderId": "uuid-do-pedido",
  "lat": -23.5505,
  "lng": -46.6333
}
```

### Exemplo com JavaScript (SockJS + STOMP)

```javascript
const client = new Client({
  webSocketFactory: () => new SockJS('http://localhost:8080/ws')
});

client.onConnect = () => {
  client.subscribe('/topic/order.{orderId}', (message) => {
    const location = JSON.parse(message.body);
    console.log(location.lat, location.lng);
  });
};

client.activate();
```

---

## Estrutura do projeto

```
src/main/java/dev/gustavodntts/deliverytracker/
├── config/
│   ├── CorsConfig.java                 # política de CORS
│   └── WebSocketConfig.java            # broker STOMP e endpoint /ws
├── controller/
│   └── LocationController.java         # POST /api/location
├── domain/
│   ├── Order.java                      # entidade pedido
│   ├── DeliveryTracking.java           # posição atual (1 por pedido)
│   ├── LocationHistory.java            # histórico de posições
│   └── enums/OrderStatus.java
├── dto/
│   ├── LocationRequest.java            # corpo da requisição HTTP
│   └── LocationEvent.java              # payload do WebSocket
├── exception/
│   ├── GlobalExceptionHandler.java     # handler global de erros
│   ├── BusinessRuleException.java      # exceção de regra de negócio
│   └── ErrorResponse.java             # formato padrão de resposta de erro
├── listener/
│   └── LocationNotificationListener.java  # escuta pg_notify e publica no WS
├── repository/
│   ├── OrderRepository.java
│   ├── DeliveryTrackingRepository.java
│   └── LocationHistoryRepository.java
└── service/
    └── LocationService.java            # regras de negócio e orquestração

src/main/resources/
├── application.properties
├── application-local.properties        # credenciais locais (não versionar)
└── db/migration/
    ├── V1__create_orders.sql
    ├── V2__create_delivery_status.sql
    ├── V3__create_location_history.sql
    ├── V4__create_notify_trigger.sql   # trigger pg_notify no PostgreSQL
    ├── V5__create_indexes.sql
    ├── V6__refactor_naming.sql
    └── V7__insert_test_data.sql        # pedido de teste para desenvolvimento
```

---

## Fluxo de dados

```
Entregador  →  POST /api/location
                     │
               LocationService
                     ├──► delivery_tracking (upsert — posição atual)
                     ├──► location_history  (insert — histórico)
                     └──► WebSocket /topic/order.{id}  ──► Cliente
                     │
               PostgreSQL trigger
                     └──► pg_notify  ──► LocationNotificationListener
                                               └──► WebSocket /topic/order.{id}  ──► Cliente
```

O banco de dados aciona um trigger a cada atualização em `delivery_tracking`, que emite uma notificação via `pg_notify`. O `LocationNotificationListener` escuta esse canal em background e também publica no WebSocket — garantindo que atualizações feitas diretamente no banco também cheguem aos clientes.

---

## Banco de dados

O schema é gerenciado pelo Flyway. As migrations rodam automaticamente ao iniciar a aplicação.

| Tabela | Descrição |
|---|---|
| `orders` | Pedidos de entrega com status e referência ao entregador |
| `delivery_tracking` | Posição atual do entregador (1 linha por pedido, sobrescrita a cada update) |
| `location_history` | Histórico completo de todas as posições registradas |

Para adicionar mudanças no schema, crie um novo arquivo de migration — nunca edite os existentes:

```
src/main/resources/db/migration/V8__descricao_da_mudanca.sql
```
