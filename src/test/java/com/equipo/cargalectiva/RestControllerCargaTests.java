package com.equipo.cargalectiva;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RestControllerCargaTests {
 @Test void consultaActualizaYRechazaLimitesInvalidos() throws Exception {
  var api=new RestControllerCarga();
  assertEquals(40,((Number)api.parametros().get("MAX_HORAS_DOCENTE")).intValue());
  api.actualizar(new RestControllerCarga.Cambio("MAX_HORAS_DOCENTE",36));
  assertEquals(36,api.parametros().get("MAX_HORAS_DOCENTE"));
  assertThrows(RestControllerCarga.ApiError.class,()->api.actualizar(new RestControllerCarga.Cambio("MIN_HORAS_DOCENTE",37)));
  assertEquals(8,api.parametros().get("MIN_HORAS_DOCENTE"));
  assertThrows(RestControllerCarga.ApiError.class,()->api.actualizar(new RestControllerCarga.Cambio("MAX_HORAS_DOCENTE",1.5)));
  assertThrows(RestControllerCarga.ApiError.class,()->api.actualizar(new RestControllerCarga.Cambio("NO_EXISTE",1)));
  assertTrue(api.catalogos().containsKey("carreras"));
 }
}
