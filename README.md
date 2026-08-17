# API Test Automation

[![API Automation](https://github.com/Harigovindbn/api-test-automation/actions/workflows/api-tests.yml/badge.svg)](https://github.com/Harigovindbn/api-test-automation/actions/workflows/api-tests.yml)

A working API automation portfolio project that tests the public [Restful Booker](https://restful-booker.herokuapp.com/) training API with RestAssured and Postman/Newman.

The framework covers authentication, schema contracts, negative cases, response-time checks, and a complete booking lifecycle. Every created booking is read back from the service to verify persisted state and is deleted during cleanup.

## Implemented coverage

| Area | Automated validation |
| --- | --- |
| Health | Status code and response-time threshold |
| Authentication | Valid token and invalid credential behavior |
| Contracts | JSON schemas for auth, booking IDs, bookings, and create responses |
| CRUD | Create, read, full update, partial update, delete, and verify deletion |
| Negative testing | Unknown booking and invalid authentication |
| State validation | GET verification after create and PATCH operations |
| Reporting | Allure request/response attachments, Surefire XML, Newman JUnit, and Newman HTML |
| CI/CD | RestAssured and Postman jobs on every pull request and push to `main` |

## Technology

- Java 17+ and Maven
- REST Assured 6
- JUnit 5 and AssertJ
- JSON Schema Validator
- Allure JUnit integration and REST Assured attachments
- Postman Collection v2.1
- Newman with JUnit and HTML reporters
- GitHub Actions

## Project structure

```text
.
├── .github/workflows/api-tests.yml
├── postman/
│   ├── Restful-Booker.postman_collection.json
│   └── Restful-Booker.postman_environment.json
├── scripts/
├── src/test/java/com/harigovind/api/
│   ├── client/       # Reusable endpoint clients
│   ├── config/       # Environment configuration
│   ├── model/        # Request and response records
│   ├── spec/         # Shared RestAssured specifications
│   ├── tests/        # API scenarios
│   └── util/         # Isolated test-data builders
├── src/test/resources/schemas/
├── package.json
└── pom.xml
```

## Run the RestAssured suite

Requirements: JDK 17 or newer and Maven 3.9 or newer.

```powershell
git clone https://github.com/Harigovindbn/api-test-automation.git
cd api-test-automation
mvn clean test
```

Run only smoke tests:

```powershell
mvn -Dgroups=smoke test
```

Generate the Allure HTML report after a test run:

```powershell
mvn allure:report
```

The raw results are written to `target/allure-results`, and the generated report is written under `target/site/allure-maven-plugin`.

## Run the Postman collection

Requirements: Node.js 20 or newer.

```powershell
npm install
npm run test:postman
```

Newman creates:

- `target/newman/newman-results.xml`
- `target/newman/newman-report.html`

## Configuration

The RestAssured tests read these optional environment variables:

| Variable | Default |
| --- | --- |
| `API_BASE_URL` | `https://restful-booker.herokuapp.com` |
| `API_USERNAME` | `admin` |
| `API_PASSWORD` | `password123` |
| `API_RESPONSE_TIME_MS` | `10000` |

The default username and password are public credentials provided by the training API. For another environment, set the variables before running Maven.

```powershell
$env:API_BASE_URL = "https://your-api.example.com"
$env:API_USERNAME = "your-user"
$env:API_PASSWORD = "your-password"
mvn clean test
```

Postman variables can be changed in `postman/Restful-Booker.postman_environment.json` or overridden with Newman CLI options.

## CI evidence

GitHub Actions runs two independent jobs:

1. The Java framework executes the JUnit and RestAssured suite and generates Allure evidence.
2. Newman executes the exported Postman collection and generates JUnit XML and an HTML report.

Reports are uploaded as workflow artifacts even when a test fails, making failed requests and assertions available for investigation.

## Test-design decisions

- Endpoint calls are isolated in client classes instead of test methods.
- Request and response data use immutable Java records.
- Tests create unique records rather than depending on the API's preloaded data.
- CRUD assertions verify both immediate responses and persisted server state.
- Cleanup runs even when a lifecycle assertion fails.
- Secrets are configurable and are not hard-coded into the CI workflow.

## License

This project is released under the MIT License.
