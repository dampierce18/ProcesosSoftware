package minimarket.negocio;

import minimarket.modelo.*;
import minimarket.registro.RegistroPedidos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestorPedidosTest {

    private GestorPedidos gestorPedidos;
    private RegistroPedidos registroPedidos;
    
    private Pedido pedido1;
    private Pedido pedido2;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        registroPedidos = mock(RegistroPedidos.class);
        gestorPedidos = new GestorPedidos(registroPedidos);
        
        // Crear productos de prueba
        producto1 = new Producto("Arroz", "001", 10, 3.5, "Granos");
        producto2 = new Producto("Aceite", "002", 15, 8.0, "Aceites");
        
        // Crear pedidos de prueba con la sintaxis correcta
        pedido1 = new Pedido("PED-001", LocalDate.of(2024, 1, 15));
        pedido2 = new Pedido("PED-002", LocalDate.of(2024, 1, 20));
    }

    @Test
    @DisplayName("Debería agregar detalle correctamente cuando los parámetros son válidos")
    void testAgregarDetalle_Exitoso() {
        // Given
        Pedido pedido = new Pedido("PED-TEST", LocalDate.now());
        int cantidad = 2;

        // When
        boolean resultado = gestorPedidos.agregarDetalle(pedido, producto1, cantidad);

        // Then
        assertTrue(resultado);
        assertEquals(1, pedido.getDetalles().size());
        assertEquals(producto1, pedido.getDetalles().get(0).getProducto());
        assertEquals(cantidad, pedido.getDetalles().get(0).getCantidad());
    }

    @Test
    @DisplayName("Debería fallar al agregar detalle con pedido nulo")
    void testAgregarDetalle_PedidoNulo() {
        // When
        boolean resultado = gestorPedidos.agregarDetalle(null, producto1, 2);

        // Then
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Debería fallar al agregar detalle con producto nulo")
    void testAgregarDetalle_ProductoNulo() {
        // Given
        Pedido pedido = new Pedido("PED-TEST", LocalDate.now());

        // When
        boolean resultado = gestorPedidos.agregarDetalle(pedido, null, 2);

        // Then
        assertFalse(resultado);
        assertTrue(pedido.getDetalles().isEmpty());
    }

    @Test
    @DisplayName("Debería fallar al agregar detalle con cantidad inválida")
    void testAgregarDetalle_CantidadInvalida() {
        // Given
        Pedido pedido = new Pedido("PED-TEST", LocalDate.now());

        // When - Cantidad cero
        boolean resultado1 = gestorPedidos.agregarDetalle(pedido, producto1, 0);
        
        // When - Cantidad negativa
        boolean resultado2 = gestorPedidos.agregarDetalle(pedido, producto1, -1);

        // Then
        assertFalse(resultado1);
        assertFalse(resultado2);
        assertTrue(pedido.getDetalles().isEmpty());
    }

    @Test
    @DisplayName("Debería agregar múltiples detalles al mismo pedido")
    void testAgregarMultiplesDetalles() {
        // Given
        Pedido pedido = new Pedido("PED-TEST", LocalDate.now());

        // When
        boolean resultado1 = gestorPedidos.agregarDetalle(pedido, producto1, 2);
        boolean resultado2 = gestorPedidos.agregarDetalle(pedido, producto2, 1);

        // Then
        assertTrue(resultado1);
        assertTrue(resultado2);
        assertEquals(2, pedido.getDetalles().size());
    }

    @Test
    @DisplayName("Debería procesar pedido correctamente cuando es válido")
    void testProcesarPedido_Exitoso() {
        // Given
        Pedido pedido = new Pedido("PED-001", LocalDate.now());
        gestorPedidos.agregarDetalle(pedido, producto1, 2); // 2 * 3.5 = 7.0
        gestorPedidos.agregarDetalle(pedido, producto2, 1); // 1 * 8.0 = 8.0
        // Total esperado: 15.0

        // When
        boolean resultado = gestorPedidos.procesarPedido(pedido);

        // Then
        assertTrue(resultado);
        verify(registroPedidos, times(1)).registrarPedido(pedido);
    }

    @Test
    @DisplayName("Debería fallar al procesar pedido nulo")
    void testProcesarPedido_PedidoNulo() {
        // When
        boolean resultado = gestorPedidos.procesarPedido(null);

        // Then
        assertFalse(resultado);
        verify(registroPedidos, never()).registrarPedido(any());
    }

    @Test
    @DisplayName("Debería fallar al procesar pedido sin detalles")
    void testProcesarPedido_SinDetalles() {
        // Given
        Pedido pedido = new Pedido("PED-001", LocalDate.now());
        // No se agregan detalles

        // When
        boolean resultado = gestorPedidos.procesarPedido(pedido);

        // Then
        assertFalse(resultado);
        verify(registroPedidos, never()).registrarPedido(any());
    }

    @Test
    @DisplayName("Debería fallar al procesar pedido con lista de detalles nula")
    void testProcesarPedido_DetallesNulos() {
        // Given
        Pedido pedido = mock(Pedido.class);
        when(pedido.getDetalles()).thenReturn(null);

        // When
        boolean resultado = gestorPedidos.procesarPedido(pedido);

        // Then
        assertFalse(resultado);
        verify(registroPedidos, never()).registrarPedido(any());
    }

    @Test
    @DisplayName("Debería cancelar pedido existente")
    void testCancelarPedido_Exitoso() {
        // Given
        Pedido pedido = new Pedido("PED-001", LocalDate.now());

        // When
        gestorPedidos.cancelarPedido(pedido);

        // Then
        verify(registroPedidos, times(1)).eliminarPedido(pedido);
    }

    @Test
    @DisplayName("No debería hacer nada al cancelar pedido nulo")
    void testCancelarPedido_PedidoNulo() {
        // When
        gestorPedidos.cancelarPedido(null);

        // Then
        verify(registroPedidos, never()).eliminarPedido(any());
    }

    @Test
    @DisplayName("Debería obtener historial de pedidos del registro")
    void testObtenerHistorialPedidos() {
        // Given
        List<Pedido> pedidosEsperados = Arrays.asList(pedido1, pedido2);
        when(registroPedidos.obtenerPedidos()).thenReturn(pedidosEsperados);

        // When
        List<Pedido> resultado = gestorPedidos.obtenerHistorialPedidos();

        // Then
        assertEquals(pedidosEsperados, resultado);
        verify(registroPedidos, times(1)).obtenerPedidos();
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay pedidos en el historial")
    void testObtenerHistorialPedidos_Vacio() {
        // Given
        when(registroPedidos.obtenerPedidos()).thenReturn(new ArrayList<>());

        // When
        List<Pedido> resultado = gestorPedidos.obtenerHistorialPedidos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debería calcular correctamente el monto total al procesar pedido")
    void testProcesarPedido_CalculoMontoTotal() {
        // Given
        Pedido pedido = new Pedido("PED-001", LocalDate.now());
        gestorPedidos.agregarDetalle(pedido, producto1, 3); // 3 * 3.5 = 10.5
        gestorPedidos.agregarDetalle(pedido, producto2, 2); // 2 * 8.0 = 16.0
        // Total esperado: 26.5

        // When
        boolean resultado = gestorPedidos.procesarPedido(pedido);

        // Then
        assertTrue(resultado);
        assertEquals(26.5, pedido.getMontoTotal());
        verify(registroPedidos, times(1)).registrarPedido(pedido);
    }

    @Test
    @DisplayName("Debería manejar pedido con múltiples productos del mismo tipo")
    void testAgregarDetalle_MismoProductoMultipleVeces() {
        // Given
        Pedido pedido = new Pedido("PED-TEST", LocalDate.now());

        // When - Agregar el mismo producto dos veces
        boolean resultado1 = gestorPedidos.agregarDetalle(pedido, producto1, 2);
        boolean resultado2 = gestorPedidos.agregarDetalle(pedido, producto1, 3);

        // Then
        assertTrue(resultado1);
        assertTrue(resultado2);
        assertEquals(2, pedido.getDetalles().size());
        // Ambos detalles deben tener el mismo producto
        assertEquals(producto1, pedido.getDetalles().get(0).getProducto());
        assertEquals(producto1, pedido.getDetalles().get(1).getProducto());
    }

    @Test
    @DisplayName("Debería procesar pedido con un solo detalle")
    void testProcesarPedido_UnSoloDetalle() {
        // Given
        Pedido pedido = new Pedido("PED-001", LocalDate.now());
        gestorPedidos.agregarDetalle(pedido, producto1, 1);

        // When
        boolean resultado = gestorPedidos.procesarPedido(pedido);

        // Then
        assertTrue(resultado);
        verify(registroPedidos, times(1)).registrarPedido(pedido);
    }

    @Test
    @DisplayName("Debería mantener el estado consistente después de cancelar pedido")
    void testCancelarPedido_EstadoConsistente() {
        // Given
        Pedido pedido = new Pedido("PED-001", LocalDate.now());
        gestorPedidos.agregarDetalle(pedido, producto1, 2);

        // When
        gestorPedidos.cancelarPedido(pedido);

        // Then - El pedido debería mantener sus detalles
        assertEquals(1, pedido.getDetalles().size());
        verify(registroPedidos, times(1)).eliminarPedido(pedido);
    }

    @Test
    @DisplayName("Debería inyectar correctamente la dependencia por constructor")
    void testInyeccionDependencia() {
        // When
        GestorPedidos gestor = new GestorPedidos(registroPedidos);

        // Then
        assertNotNull(gestor);
        // Verificar que se usa la dependencia inyectada
        gestor.obtenerHistorialPedidos();
        verify(registroPedidos, times(1)).obtenerPedidos();
    }

    @Test
    @DisplayName("Debería manejar correctamente pedidos con diferentes fechas")
    void testPedidosConDiferentesFechas() {
        // Given
        LocalDate fecha1 = LocalDate.of(2024, 1, 10);
        LocalDate fecha2 = LocalDate.of(2024, 1, 15);
        Pedido pedidoConFecha1 = new Pedido("PED-F1", fecha1);
        Pedido pedidoConFecha2 = new Pedido("PED-F2", fecha2);

        gestorPedidos.agregarDetalle(pedidoConFecha1, producto1, 1);
        gestorPedidos.agregarDetalle(pedidoConFecha2, producto2, 1);

        // When
        boolean resultado1 = gestorPedidos.procesarPedido(pedidoConFecha1);
        boolean resultado2 = gestorPedidos.procesarPedido(pedidoConFecha2);

        // Then
        assertTrue(resultado1);
        assertTrue(resultado2);
        assertEquals(fecha1, pedidoConFecha1.getFecha());
        assertEquals(fecha2, pedidoConFecha2.getFecha());
        verify(registroPedidos, times(2)).registrarPedido(any());
    }

    @Test
    @DisplayName("Debería calcular subtotal correctamente en detalles")
    void testCalculoSubtotalEnDetalles() {
        // Given
        Pedido pedido = new Pedido("PED-TEST", LocalDate.now());
        gestorPedidos.agregarDetalle(pedido, producto1, 2); // 2 * 3.5 = 7.0
        gestorPedidos.agregarDetalle(pedido, producto2, 3); // 3 * 8.0 = 24.0

        // When
        double montoTotal = pedido.getMontoTotal();

        // Then
        assertEquals(31.0, montoTotal);
        assertEquals(7.0, pedido.getDetalles().get(0).getSubtotal());
        assertEquals(24.0, pedido.getDetalles().get(1).getSubtotal());
    }
}