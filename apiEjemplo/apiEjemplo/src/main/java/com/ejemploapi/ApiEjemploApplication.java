package com.ejemploapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

@SpringBootApplication
@RestController
public class ApiEjemploApplication {

    private static final Map<Integer, int[]> arraysVerificados = new HashMap<>();
    private static int totalArrays = 0;

    public static void main(String[] args) {
        SpringApplication.run(ApiEjemploApplication.class, args);
    }

    @PostMapping("/smallest")
    public ResponseEntity<?> getMenorPositivo(@RequestBody Map<String, int[]> request) {
        int[] array = request.get("array");
        int resultado = arreglo(array);
        if (resultado <= 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se puede calcular el menor entero positivo.");
        }
        actualizarEstadisticas(array);
        Map<String, Integer> respuesta = new HashMap<>();
        respuesta.put("result", resultado);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> getStats(@RequestBody Map<String, Integer> request) {
        int numero = request.get("numero");
        int conteo = obtenerConteo(numero);
        double ratio = (double) conteo / totalArrays;
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", conteo);
        stats.put("total", totalArrays);
        stats.put("ratio", ratio);
        return ResponseEntity.ok(stats);
    }

    private int arreglo(int[] A) {
        Arrays.sort(A); // Ordenar el array para facilitar la búsqueda
        int menorPositivo = 1;
        for (int i = 0; i < A.length; i++) {
            if (A[i] == menorPositivo) {
                menorPositivo++; // Si el elemento actual coincide con el menor positivo, incrementarlo
            } else if (A[i] > menorPositivo) {
                return menorPositivo; // Si el elemento actual es mayor que el menor positivo, se encontró el valor
            }
        }
        return menorPositivo; // Si no se encontró ningún valor mayor que el menor positivo, el menor positivo es el siguiente
    }

    private synchronized void actualizarEstadisticas(int[] array) {
        totalArrays++;
        int hash = Arrays.hashCode(array);
        arraysVerificados.put(hash, array);
    }

    private synchronized int obtenerConteo(int numero) {
        int conteo = 0;
        for (int[] array : arraysVerificados.values()) {
            if (arreglo(array) == numero) {
                conteo++;
            }
        }
        return conteo;
    }
}
