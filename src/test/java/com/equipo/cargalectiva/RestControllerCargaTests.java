package com.equipo.cargalectiva;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RestControllerCargaTests {
 @Test void consultaActualizaYRechazaLimitesInvalidos() throws Exception {
  var api=new RestControllerCarga("prueba");
  assertEquals(40,((Number)api.parametros("prueba").get("MAX_HORAS_DOCENTE")).intValue());
  api.actualizar("prueba",new RestControllerCarga.Cambio("MAX_HORAS_DOCENTE",36));
  assertEquals(36,api.parametros("prueba").get("MAX_HORAS_DOCENTE"));
  assertThrows(RestControllerCarga.ApiError.class,()->api.actualizar("prueba",new RestControllerCarga.Cambio("MIN_HORAS_DOCENTE",37)));
  assertEquals(8,api.parametros("prueba").get("MIN_HORAS_DOCENTE"));
  assertThrows(RestControllerCarga.ApiError.class,()->api.actualizar("prueba",new RestControllerCarga.Cambio("MAX_HORAS_DOCENTE",1.5)));
  assertThrows(RestControllerCarga.ApiError.class,()->api.actualizar("prueba",new RestControllerCarga.Cambio("NO_EXISTE",1)));
  assertThrows(RestControllerCarga.ApiError.class,()->api.parametros("incorrecta"));
  assertTrue(api.catalogos("prueba").containsKey("carreras"));
 }
}
