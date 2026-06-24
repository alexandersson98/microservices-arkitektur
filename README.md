# Microservices-arkitektur — Bibliotekssystem

Det här är vårt grupprojekt i kursen, där vi har byggt om ett tidigare Library API-projekt
(en monolit) till en riktig microservices-arkitektur. Tanken var att lära oss hur man delar
upp en applikation i flera fristående tjänster som pratar med varandra via ett API Gateway,
hittar varandra via en service registry, och skyddar sig med JWT-autentisering.

Vi är ett team på tre personer och har jobbat agilt med feature-branches, Pull Requests och
kodgranskning genom hela projektet.

---

## Innehåll

- Arkitektur
- Tjänsterna
- Teknikstack
- Komma igång lokalt
- Testning
- CI/CD
- API-dokumentation
- Kända begränsningar

---

## Arkitektur

```
                         ┌─────────────────┐
                         │   Frontend      │
                         │  (Vite/JS SPA)  │
                         └────────┬────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │   API Gateway   │   :8080
                         │ (Spring Cloud   │
                         │    Gateway)     │
                         └────────┬────────┘
                    JWT-validering, routing
                    ┌─────────────┴─────────────┐
                    ▼                           ▼
          ┌──────────────────┐        ┌──────────────────┐
          │   User-Service   │        │  Library-Service │
          │      :8082       │◄──────►│      :8081       │
          │  (H2-databas)    │     OpenFeign(PostgreSQL) │
          └─────────┬────────┘        └─────────┬────────┘
                    │                            │
                    └──────────┬─────────────────┘
                               ▼
                     ┌──────────────────┐
                     │  Eureka Server   │   :8761
                     │(Service Registry)│
                     └──────────────────┘

                     ┌──────────────────┐     ┌──────────────────┐
                     │      Vault       │     │      Redis       │
                     │   (secrets)      │     │    (caching)     │
                     │      :8200       │     │      :6379       │
                     └──────────────────┘     └──────────────────┘
```

**Hur det hänger ihop:**
- Alla requests går in via **API Gateway**, som routar vidare till rätt tjänst beroende på URL.
- **Eureka** håller koll på var varje tjänst körs, så Gateway (och tjänsterna sinsemellan)
  slipper hårdkodade adresser.
- **User-Service** hanterar inloggning, registrering och medlemmar. Den skapar JWT-tokens
  vid inloggning.
- **Library-Service** hanterar böcker, författare och lån. Den pratar med User-Service via
  **OpenFeign** för att hämta medlemsinformation vid utlåning, och skickar med JWT-token
  vidare i anropet.
- **Vault** lagrar den gemensamma `jwt.secret`-nyckeln som alla tjänster använder för att
  signera och validera tokens.
- **Redis** används för caching i Library-Service, t.ex. på listor över böcker och lån som
  läses ofta men ändras sällan.

---

## Tjänsterna

| Tjänst | Port | Beskrivning | Databas |
|---|---|---|---|
| `eureka-server` | 8761 | Service registry — håller reda på var tjänsterna körs | — |
| `api-gateway` | 8080 | Enda ingångspunkten för all trafik, routar och validerar JWT | — |
| `user-service` | 8082 | Inloggning, registrering, medlemmar, roller | H2 (in-memory) |
| `library-service` | 8081 | Böcker, författare, lån | PostgreSQL |
| `frontend` | 5173 | Enkel SPA som pratar med backend via Gateway | — |

---

## Teknikstack

- **Java 21** + **Spring Boot 3.5**
- **Spring Cloud Gateway** — API Gateway
- **Netflix Eureka** — Service Discovery
- **Spring Security + JWT (JJWT)** — autentisering och rollbaserad åtkomstkontroll
- **OpenFeign** — kommunikation mellan tjänsterna
- **HashiCorp Vault** — hantering av hemligheter (t.ex. `jwt.secret`)
- **PostgreSQL** / **H2** — databaser
- **Redis** — caching
- **TestContainers + MockMvc** — integrationstester
- **JaCoCo** — testtäckning
- **GitHub Actions** — CI/CD
- **Docker / docker-compose** — infrastruktur (Vault, Postgres, Redis)

---

## Komma igång lokalt

### Förkrav

Innan du börjar, se till att du har installerat:

- **Java 21** (JDK)
- **Maven**
- **Docker Desktop** (måste vara igång — krävs både för `docker-compose` och för
  TestContainers-testerna)
- **Vault CLI** (för att hantera secrets manuellt i utvecklingsläge)

### Steg 1 — Starta infrastrukturen (Vault, Postgres, Redis)

Vi har valt att köra Vault manuellt i dev-mode istället för via docker-compose, eftersom
det är enklare att jobba med lokalt. Postgres och Redis körs via docker-compose:

```bash
docker-compose up -d
```

Starta sedan Vault i ett eget terminalfönster:

```bash
vault server -dev -dev-root-token-id="godis"
```

> Vault körs i in-memory dev-mode, vilket betyder att all data försvinner när processen
> stängs av. Du måste lägga in `jwt.secret` igen varje gång du startar om Vault.

Lägg in den gemensamma JWT-hemliga nyckeln (samma nyckel måste användas av alla tjänster):

```bash
$env:VAULT_ADDR="http://127.0.0.1:8200"
$env:VAULT_TOKEN="godis"
vault kv put secret/boilerroom-labb1 jwt.secret="Xk0DbEEP0QjmKzdjqg9Dmr8yseS/oqz4OAkSg6AIMW5Z2xRimuEQsiV1fyf8vZXZumQCkzndn1qdfOd/aVzJ2A=="
```

### Steg 2 — Starta Eureka Server

```bash
cd eureka-server
mvn spring-boot:run
```

Vänta tills den startat, kontrollera sedan att den är igång genom att öppna
`http://localhost:8761` i webbläsaren — du ska se Eurekas dashboard.

### Steg 3 — Starta API Gateway

I ett nytt terminalfönster:

```bash
cd api-gateway
mvn spring-boot:run
```

### Steg 4 — Starta de fristående tjänsterna

I egna terminalfönster:

```bash
cd user-service
mvn spring-boot:run
```

```bash
cd library-service
mvn spring-boot:run
```

Kontrollera i Eureka-dashboarden (`http://localhost:8761`) att båda tjänsterna dyker upp
med status `UP`.

### Steg 5 — Starta frontend (valfritt)

```bash
cd frontend
npm install
npm run dev
```

### Ordning spelar roll

Starta alltid i den här ordningen, eftersom varje steg är beroende av föregående:

```
Vault + Postgres + Redis → Eureka → API Gateway → User-Service / Library-Service → Frontend
```

---

## Testning

Vi har valt att skriva en heltäckande testsvit (enligt uppgiftskraven, minst en tjänst)
för **User-Service**, med både enhetstester och integrationstester via **TestContainers**
och **MockMvc**. Integrationstesterna startar en riktig PostgreSQL-databas i en Docker-
container under testkörningen, så att testerna är så verklighetstrogna som möjligt istället
för att mocka bort databasen.

Kör testerna:

```bash
cd user-service
mvn test
```

> OBS: Docker Desktop måste vara igång för att TestContainers-testerna ska fungera!

### Testtäckning (JaCoCo)

Efter att testerna körts genereras en täckningsrapport automatiskt. Öppna den i webbläsaren:

```
user-service/target/site/jacoco/index.html
```

Vi ligger på cirka **97 % instruktionstäckning** i User-Service. En skärmdump av rapporten
finns sparad i [`docs/`](docs).

---

## CI/CD

Vi har satt upp en GitHub Actions-pipeline (`.github/workflows/ci.yml`) som körs automatiskt
vid varje push och Pull Request mot `main`. Pipelinen:

1. Checkar ut koden
2. Sätter upp Java 21
3. Bygger och testar samtliga fyra backend-tjänster (`eureka-server`, `api-gateway`,
   `user-service`, `library-service`) med `mvn verify`

Det betyder att om någon pushar kod som inte kompilerar eller får tester att gå fel, syns
det direkt i PR:en innan något hinner mergas in i `main`.

---

## API-dokumentation

Varje tjänst har sin egen Swagger UI när den körs:

- User-Service: `http://localhost:8082/swagger-ui.html`
- Library-Service: `http://localhost:8081/swagger-ui.html`

---

## Kända begränsningar

Vi vill vara ärliga om några saker vi är medvetna om men inte hunnit/valt att lösa fullt ut:

- **JWT skapas i User-Service, inte i Gateway.** Gateway validerar tokens centralt för all
  trafik, men det är fortfarande User-Service som genererar token vid inloggning (Gateway
  routar bara vidare login-requesten). Vi har stämt av detta med läraren.
- **Vault körs i dev-mode** och är inte persistent. I en produktionsmiljö hade vi använt en
  riktig Vault-instans med persistent lagring.
- **Library-Service saknar egna TestContainers-tester** — uppgiften krävde heltäckande
  tester för minst en tjänst, och vi valde att lägga allt fokus där på User-Service.
