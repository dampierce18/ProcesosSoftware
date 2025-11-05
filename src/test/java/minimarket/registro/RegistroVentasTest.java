package minimarket.registro;

import minimarket.modelo.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegistroVentasTest {

    private RegistroVentas registroVentas;
    private Venta venta1;
    private Venta venta2;

    @BeforeEach
    void setUp() {
        registroVentas = new RegistroVentas();
        
        // Crear ventas de ejemplo para las pruebas
        // Asumiendo que Venta tiene un constructor apropiado
        venta1 = new Venta(/* parámetros de la venta 1 */);
        venta2 = new Venta(/* parámetros de la venta 2 */);
    }

    @Test
    @DisplayName("Debería registrar una venta correctamente")
    void testRegistrarVenta() {
        // When
        registroVentas.registrarVenta(venta1);
        
        // Then
        List<Venta> ventas = registroVentas.obtenerVentas();
        assertEquals(1, ventas.size());
        assertTrue(ventas.contains(venta1));
    }

    @Test
    @DisplayName("Debería registrar múltiples ventas correctamente")
    void testRegistrarMultiplesVentas() {
        // When
        registroVentas.registrarVenta(venta1);
        registroVentas.registrarVenta(venta2);
        
        // Then
        List<Venta> ventas = registroVentas.obtenerVentas();
        assertEquals(2, ventas.size());
        assertTrue(ventas.contains(venta1));
        assertTrue(ventas.contains(venta2));
    }

    @Test
    @DisplayName("Debería eliminar una venta existente")
    void testEliminarVentaExistente() {
        // Given
        registroVentas.registrarVenta(venta1);
        registroVentas.registrarVenta(venta2);
        
        // When
        registroVentas.eliminarVenta(venta1);
        
        // Then
        List<Venta> ventas = registroVentas.obtenerVentas();
        assertEquals(1, ventas.size());
        assertFalse(ventas.contains(venta1));
        assertTrue(ventas.contains(venta2));
    }

    @Test
    @DisplayName("No debería lanzar excepción al eliminar venta inexistente")
    void testEliminarVentaInexistente() {
        // Given
        registroVentas.registrarVenta(venta1);
        Venta ventaInexistente = new Venta(/* parámetros diferentes */);
        
        // When & Then - No debería lanzar excepción
        assertDoesNotThrow(() -> registroVentas.eliminarVenta(ventaInexistente));
        
        // Verificar que la venta original sigue existiendo
        List<Venta> ventas = registroVentas.obtenerVentas();
        assertEquals(1, ventas.size());
        assertTrue(ventas.contains(venta1));
    }

    @Test
    @DisplayName("Debería retornar copia de la lista de ventas")
    void testObtenerVentasRetornaCopia() {
        // Given
        registroVentas.registrarVenta(venta1);
        
        // When
        List<Venta> copia1 = registroVentas.obtenerVentas();
        List<Venta> copia2 = registroVentas.obtenerVentas();
        
        // Then - Las copias deberían ser diferentes objetos pero con mismo contenido
        assertNotSame(copia1, copia2, "Debería retornar copias diferentes");
        assertEquals(copia1, copia2, "El contenido de las copias debería ser igual");
        
        // Modificar una copia no debería afectar al original
        copia1.clear();
        List<Venta> ventasOriginal = registroVentas.obtenerVentas();
        assertEquals(1, ventasOriginal.size(), "La lista original no debería modificarse");
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay ventas")
    void testObtenerVentasCuandoEstaVacio() {
        // When
        List<Venta> ventas = registroVentas.obtenerVentas();
        
        // Then
        assertNotNull(ventas);
        assertTrue(ventas.isEmpty());
    }

    @Test
    @DisplayName("Debería manejar correctamente el caso de lista vacía en mostrarHistorial")
    void testMostrarHistorialCuandoEstaVacio() {
        // Este test verifica que no hay excepciones cuando no hay ventas
        assertDoesNotThrow(() -> registroVentas.mostrarHistorial());
    }

    @Test
    @DisplayName("Debería ejecutar mostrarHistorial sin excepciones con ventas existentes")
    void testMostrarHistorialConVentas() {
        // Given
        registroVentas.registrarVenta(venta1);
        registroVentas.registrarVenta(venta2);
        
        // When & Then - No debería lanzar excepción
        assertDoesNotThrow(() -> registroVentas.mostrarHistorial());
    }

    @Test
    @DisplayName("Debería mantener el orden de inserción de las ventas")
    void testOrdenDeVentas() {
        // Given
        registroVentas.registrarVenta(venta1);
        registroVentas.registrarVenta(venta2);
        
        // When
        List<Venta> ventas = registroVentas.obtenerVentas();
        
        // Then - Debería mantener el orden de inserción
        assertEquals(venta1, ventas.get(0));
        assertEquals(venta2, ventas.get(1));
    }
}