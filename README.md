# carga-lectiva-springboot
En GitHub crea el secreto de Codespaces REST_API_KEY y permite su uso en este repositorio. Usa el mismo valor en las propiedades de Apps Script y en Postman. Abre el Codespace con Java 21 y ejecuta desde la raíz:

```sh
./gradlew clean build
./gradlew bootRun
```

En Ports, comparte el puerto 8080 como Public y copia su dirección HTTPS en REST_BASE_URL (sin /api). Mantén el proceso en ejecución.

Todas las peticiones usan el encabezado X-API-Key. GET /api/parametros devuelve la configuración, GET /api/catalogos devuelve carreras y departamentos y POST /api/parametros/actualizar acepta un JSON como `{"clave":"MAX_HORAS_DOCENTE","valor":36}`. Repite el primer GET para verificar el cambio. Reiniciar restaura los valores iniciales.
