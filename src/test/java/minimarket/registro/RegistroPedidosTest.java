package minimarket.registro;

import minimarket.modelo.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegistroPedidosTest {

    private RegistroPedidos registroPedidos;
    private Pedido pedido1;
    private Pedido pedido2;

    @BeforeEach
    void setUp() {
        registroPedidos = new RegistroPedidos();
        
        // Crear pedidos de ejemplo para las pruebas
        // Asumiendo que Pedido tiene un constructor apropiado
        pedido1 = new Pedido("PED-001", LocalDate.now());
        pedido2 = new Pedido("PED-002", LocalDate.now());
    }

    @Test
    @DisplayName("Debería registrar un pedido correctamente")
    void testRegistrarPedido() {
        // When
        registroPedidos.registrarPedido(pedido1);
        
        // Then
        List<Pedido> pedidos = registroPedidos.obtenerPedidos();
        assertEquals(1, pedidos.size());
        assertTrue(pedidos.contains(pedido1));
    }

    @Test
    @DisplayName("Debería registrar múltiples pedidos correctamente")
    void testRegistrarMultiplesPedidos() {
        // When
        registroPedidos.registrarPedido(pedido1);
        registroPedidos.registrarPedido(pedido2);
        
        // Then
        List<Pedido> pedidos = registroPedidos.obtenerPedidos();
        assertEquals(2, pedidos.size());
        assertTrue(pedidos.contains(pedido1));
        assertTrue(pedidos.contains(pedido2));
    }

    @Test
    @DisplayName("Debería eliminar un pedido existente")
    void testEliminarPedidoExistente() {
        // Given
        registroPedidos.registrarPedido(pedido1);
        registroPedidos.registrarPedido(pedido2);
        
        // When
        registroPedidos.eliminarPedido(pedido1);
        
        // Then
        List<Pedido> pedidos = registroPedidos.obtenerPedidos();
        assertEquals(1, pedidos.size());
        assertFalse(pedidos.contains(pedido1));
        assertTrue(pedidos.contains(pedido2));
    }

    @Test
    @DisplayName("No debería lanzar excepción al eliminar pedido inexistente")
    void testEliminarPedidoInexistente() {
        // Given
        registroPedidos.registrarPedido(pedido1);
        Pedido pedidoInexistente = new Pedido(/* parámetros diferentes */);
        
        // When & Then - No debería lanzar excepción
        assertDoesNotThrow(() -> registroPedidos.eliminarPedido(pedidoInexistente));
        
        // Verificar que el pedido original sigue existiendo
        List<Pedido> pedidos = registroPedidos.obtenerPedidos();
        assertEquals(1, pedidos.size());
        assertTrue(pedidos.contains(pedido1));
    }

    @Test
    @DisplayName("Debería retornar copia de la lista de pedidos")
    void testObtenerPedidosRetornaCopia() {
        // Given
        registroPedidos.registrarPedido(pedido1);
        
        // When
        List<Pedido> copia1 = registroPedidos.obtenerPedidos();
        List<Pedido> copia2 = registroPedidos.obtenerPedidos();
        
        // Then - Las copias deberían ser diferentes objetos pero con mismo contenido
        assertNotSame(copia1, copia2, "Debería retornar copias diferentes");
        assertEquals(copia1, copia2, "El contenido de las copias debería ser igual");
        
        // Modificar una copia no debería afectar al original
        copia1.clear();
        List<Pedido> pedidosOriginal = registroPedidos.obtenerPedidos();
        assertEquals(1, pedidosOriginal.size(), "La lista original no debería modificarse");
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay pedidos")
    void testObtenerPedidosCuandoEstaVacio() {
        // When
        List<Pedido> pedidos = registroPedidos.obtenerPedidos();
        
        // Then
        assertNotNull(pedidos);
        assertTrue(pedidos.isEmpty());
    }

    @Test
    @DisplayName("Debería manejar correctamente el caso de lista vacía en mostrarHistorial")
    void testMostrarHistorialCuandoEstaVacio() {
        // Este test verifica que no hay excepciones cuando no hay pedidos
        assertDoesNotThrow(() -> registroPedidos.mostrarHistorial());
    }

    @Test
    @DisplayName("Debería ejecutar mostrarHistorial sin excepciones con pedidos existentes")
    void testMostrarHistorialConPedidos() {
        // Given
        registroPedidos.registrarPedido(pedido1);
        registroPedidos.registrarPedido(pedido2);
        
        // When & Then - No debería lanzar excepción
        assertDoesNotThrow(() -> registroPedidos.mostrarHistorial());
    }

    @Test
    @DisplayName("Debería mantener el orden de inserción de los pedidos")
    void testOrdenDePedidos() {
        // Given
        registroPedidos.registrarPedido(pedido1);
        registroPedidos.registrarPedido(pedido2);
        
        // When
        List<Pedido> pedidos = registroPedidos.obtenerPedidos();
        
        // Then - Debería mantener el orden de inserción
        assertEquals(pedido1, pedidos.get(0));
        assertEquals(pedido2, pedidos.get(1));
    }

    @Test
    @DisplayName("Debería permitir eliminar todos los pedidos")
    void testEliminarTodosLosPedidos() {
        // Given
        registroPedidos.registrarPedido(pedido1);
        registroPedidos.registrarPedido(pedido2);
        
        // When
        registroPedidos.eliminarPedido(pedido1);
        registroPedidos.eliminarPedido(pedido2);
        
        // Then
        List<Pedido> pedidos = registroPedidos.obtenerPedidos();
        assertTrue(pedidos.isEmpty());
    }

    @Test
    @DisplayName("Debería manejar correctamente pedidos duplicados")
    void testPedidosDuplicados() {
        // Given
        registroPedidos.registrarPedido(pedido1);
        registroPedidos.registrarPedido(pedido1); // Registrar el mismo pedido dos veces
        
        // When
        List<Pedido> pedidos = registroPedidos.obtenerPedidos();
        
        // Then - Debería permitir duplicados (depende del diseño de la aplicación)
        assertEquals(2, pedidos.size());
        
        // When - Eliminar uno de los duplicados
        registroPedidos.eliminarPedido(pedido1);
        
        // Then - Debería quedar un pedido
        pedidos = registroPedidos.obtenerPedidos();
        assertEquals(1, pedidos.size());
    }
}