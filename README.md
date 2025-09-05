# Notes API (Spring Boot)
Endpoints (base: `/api`):
- `GET /health` -> `OK`
- `GET /notes` -> list notes
- `POST /notes` -> create note `{title, content}`
- `GET /notes/{id}` -> get by id
- `PUT /notes/{id}` -> update
- `DELETE /notes/{id}` -> delete
- `POST /notes/{id}/share` -> returns public share URL
- `GET /share/{publicId}` -> fetch shared note

## Run locally
```bash
mvn spring-boot:run
```

## Deploy (Render)
- New Web Service -> Build command: `mvn -B -DskipTests package`
- Start command: `java -jar target/notes-api-0.0.1-SNAPSHOT.jar`
- Environment: `JAVA_VERSION=17`
- Optional: set `CORS_ALLOWED_ORIGIN` to your Vercel URL.
