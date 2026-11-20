#!/bin/sh
set -eu
cd "$(dirname "$0")"
puerto="${PORT:-8080}"
if command -v lsof >/dev/null 2>&1 && lsof -nP -iTCP:"$puerto" -sTCP:LISTEN >/dev/null 2>&1; then
  printf 'El puerto %s ya está en uso. Comprueba la instancia existente en Postman.\n' "$puerto"
  exit 0
fi
printf 'Iniciando Spring Boot en http://localhost:%s\n' "$puerto"
exec ./gradlew bootRun
