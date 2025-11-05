package minimarket.negocio;

import minimarket.modelo.*;
import minimarket.registro.RegistroVentas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestorVentasTest {

    private GestorVentas gestorVentas;
    private RegistroVentas registroVentas;
    
    private Venta venta1;
    private Venta venta2;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        registroVentas = mock(RegistroVentas.class);
        gestorVentas = new GestorVentas(registroVentas);
        
        // Crear productos de prueba con stock inicial
        producto1 = new Producto("Arroz", "001", 10, 3.5, "Granos"); // Stock: 10
        producto2 = new Producto("Aceite", "002", 5, 8.0, "Aceites"); // Stock: 5
        
        // Crear ventas de prueba
        venta1 = new Venta("V-001", LocalDate.of(2024, 1, 15));
        venta2 = new Venta("V-002", LocalDate.of(2024, 1, 20));
    }

    @Test
    @DisplayName("Debería validar stock correctamente cuando hay stock suficiente")
    void testValidarStock_Suficiente() {
        // When & Then
        assertTrue(gestorVentas.validarStock(producto1, 5)); // Stock: 10, cantidad: 5
        assertTrue(gestorVentas.validarStock(producto1, 10)); // Stock: 10, cantidad: 10 (límite)
    }

    @Test
    @DisplayName("Debería validar stock como falso cuando no hay stock suficiente")
    void testValidarStock_Insuficiente() {
        // When & Then
        assertFalse(gestorVentas.validarStock(producto1, 11)); // Stock: 10, cantidad: 11
        assertFalse(gestorVentas.validarStock(producto2, 6)); // Stock: 5, cantidad: 6
    }

    @Test
    @DisplayName("Debería validar stock como falso con parámetros inválidos")
    void testValidarStock_ParametrosInvalidos() {
        // When & Then
        assertFalse(gestorVentas.validarStock(null, 5));
        assertFalse(gestorVentas.validarStock(producto1, 0));
        assertFalse(gestorVentas.validarStock(producto1, -1));
    }

    @Test
    @DisplayName("Debería agregar detalle correctamente cuando hay stock suficiente")
    void testAgregarDetalle_Exitoso() {
        // Given
        Venta venta = new Venta("V-TEST", LocalDate.now());
        int cantidad = 3;

        // When
        boolean resultado = gestorVentas.agregarDetalle(venta, producto1, cantidad);

        // Then
        assertTrue(resultado);
        assertEquals(1, venta.getDetalles().size());
        assertEquals(producto1, venta.getDetalles().get(0).getProducto());
        assertEquals(cantidad, venta.getDetalles().get(0).getCantidad());
    }

    @Test
    @DisplayName("Debería fallar al agregar detalle sin stock suficiente")
    void testAgregarDetalle_SinStock() {
        // Given
        Venta venta = new Venta("V-TEST", LocalDate.now());
        int cantidadExcesiva = 15; // Stock de producto1 es 10

        // When
        boolean resultado = gestorVentas.agregarDetalle(venta, producto1, cantidadExcesiva);

        // Then
        assertFalse(resultado);
        assertTrue(venta.getDetalles().isEmpty());
    }

    @Test
    @DisplayName("Debería fallar al agregar detalle con parámetros nulos")
    void testAgregarDetalle_ParametrosNulos() {
        // Given
        Venta venta = new Venta("V-TEST", LocalDate.now());

        // When & Then
        assertFalse(gestorVentas.agregarDetalle(null, producto1, 2));
        assertFalse(gestorVentas.agregarDetalle(venta, null, 2));
    }

    @Test
    @DisplayName("Debería procesar venta correctamente cuando todo es válido")
    void testProcesarVenta_Exitoso() {
        // Given
        Venta venta = new Venta("V-001", LocalDate.now());
        gestorVentas.agregarDetalle(venta, producto1, 2); // Stock inicial: 10
        gestorVentas.agregarDetalle(venta, producto2, 1); // Stock inicial: 5

        // When
        boolean resultado = gestorVentas.procesarVenta(venta);

        // Then
        assertTrue(resultado);
        verify(registroVentas, times(1)).registrarVenta(venta);
        assertEquals(8, producto1.getStock()); // 10 - 2 = 8
        assertEquals(4, producto2.getStock()); // 5 - 1 = 4
    }

    @Test
    @DisplayName("Debería fallar al procesar venta nula")
    void testProcesarVenta_VentaNula() {
        // When
        boolean resultado = gestorVentas.procesarVenta(null);

        // Then
        assertFalse(resultado);
        verify(registroVentas, never()).registrarVenta(any());
    }

    @Test
    @DisplayName("Debería fallar al procesar venta sin detalles")
    void testProcesarVenta_SinDetalles() {
        // Given
        Venta venta = new Venta("V-001", LocalDate.now());
        // No se agregan detalles

        // When
        boolean resultado = gestorVentas.procesarVenta(venta);

        // Then
        assertFalse(resultado);
        verify(registroVentas, never()).registrarVenta(any());
    }

    @Test
    @DisplayName("Debería fallar al procesar venta con detalles nulos")
    void testProcesarVenta_DetallesNulos() {
        // Given
        Venta venta = mock(Venta.class);
        when(venta.getDetalles()).thenReturn(null);

        // When
        boolean resultado = gestorVentas.procesarVenta(venta);

        // Then
        assertFalse(resultado);
        verify(registroVentas, never()).registrarVenta(any());
    }

    @Test
    @DisplayName("Debería fallar al procesar venta cuando no hay stock durante la validación")
    void testProcesarVenta_SinStockEnValidacion() {
        // Given
        Venta venta = new Venta("V-001", LocalDate.now());
        
        // Agregar detalle que consume todo el stock
        gestorVentas.agregarDetalle(venta, producto2, 5); // Stock: 5
        
        // Simular que otro proceso consumió stock entre la agregación y el procesamiento
        producto2.reducirStock(1); // Ahora stock es 4, pero la venta espera 5

        // When
        boolean resultado = gestorVentas.procesarVenta(venta);

        // Then
        assertFalse(resultado);
        verify(registroVentas, never()).registrarVenta(venta);
        // El stock no debería haberse reducido
        assertEquals(4, producto2.getStock());
    }

    @Test
    @DisplayName("Debería anular venta y restaurar stock correctamente")
    void testAnularVenta_Exitoso() {
        // Given
        Venta venta = new Venta("V-001", LocalDate.now());
        gestorVentas.agregarDetalle(venta, producto1, 3);
        gestorVentas.agregarDetalle(venta, producto2, 2);
        
        // Procesar la venta primero (reduce stock)
        gestorVentas.procesarVenta(venta);
        
        int stockInicialProducto1 = producto1.getStock(); // 7
        int stockInicialProducto2 = producto2.getStock(); // 3

        // When
        gestorVentas.anularVenta(venta);

        // Then
        verify(registroVentas, times(1)).eliminarVenta(venta);
        assertEquals(stockInicialProducto1 + 3, producto1.getStock()); // 7 + 3 = 10
        assertEquals(stockInicialProducto2 + 2, producto2.getStock()); // 3 + 2 = 5
    }

    @Test
    @DisplayName("No debería hacer nada al anular venta nula")
    void testAnularVenta_VentaNula() {
        // When
        gestorVentas.anularVenta(null);

        // Then
        verify(registroVentas, never()).eliminarVenta(any());
    }

    @Test
    @DisplayName("Debería obtener historial de ventas del registro")
    void testObtenerHistorialVentas() {
        // Given
        List<Venta> ventasEsperadas = Arrays.asList(venta1, venta2);
        when(registroVentas.obtenerVentas()).thenReturn(ventasEsperadas);

        // When
        List<Venta> resultado = gestorVentas.obtenerHistorialVentas();

        // Then
        assertEquals(ventasEsperadas, resultado);
        verify(registroVentas, times(1)).obtenerVentas();
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay ventas en el historial")
    void testObtenerHistorialVentas_Vacio() {
        // Given
        when(registroVentas.obtenerVentas()).thenReturn(new ArrayList<>());

        // When
        List<Venta> resultado = gestorVentas.obtenerHistorialVentas();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debería calcular correctamente el monto total al procesar venta")
    void testProcesarVenta_CalculoMontoTotal() {
        // Given
        Venta venta = new Venta("V-001", LocalDate.now());
        gestorVentas.agregarDetalle(venta, producto1, 3); // 3 * 3.5 = 10.5
        gestorVentas.agregarDetalle(venta, producto2, 2); // 2 * 8.0 = 16.0
        // Total esperado: 26.5

        // When
        boolean resultado = gestorVentas.procesarVenta(venta);

        // Then
        assertTrue(resultado);
        assertEquals(26.5, venta.getMontoTotal());
        verify(registroVentas, times(1)).registrarVenta(venta);
    }

    @Test
    @DisplayName("Debería manejar múltiples detalles del mismo producto")
    void testAgregarDetalle_MismoProductoMultipleVeces() {
        // Given
        Venta venta = new Venta("V-TEST", LocalDate.now());

        // When - Agregar el mismo producto dos veces
        boolean resultado1 = gestorVentas.agregarDetalle(venta, producto1, 2);
        boolean resultado2 = gestorVentas.agregarDetalle(venta, producto1, 3);

        // Then
        assertTrue(resultado1);
        assertTrue(resultado2);
        assertEquals(2, venta.getDetalles().size());
        assertEquals(producto1, venta.getDetalles().get(0).getProducto());
        assertEquals(producto1, venta.getDetalles().get(1).getProducto());
    }

    @Test
    @DisplayName("Debería procesar venta con un solo detalle")
    void testProcesarVenta_UnSoloDetalle() {
        // Given
        Venta venta = new Venta("V-001", LocalDate.now());
        gestorVentas.agregarDetalle(venta, producto1, 1);

        // When
        boolean resultado = gestorVentas.procesarVenta(venta);

        // Then
        assertTrue(resultado);
        verify(registroVentas, times(1)).registrarVenta(venta);
        assertEquals(9, producto1.getStock()); // 10 - 1 = 9
    }

    @Test
    @DisplayName("Debería mantener integridad del stock cuando falla el procesamiento")
    void testProcesarVenta_IntegridadStock() {
        // Given
        Venta venta = new Venta("V-001", LocalDate.now());
        gestorVentas.agregarDetalle(venta, producto1, 2);
        // Agregamos manualmente un detalle con stock insuficiente
        venta.agregarDetalle(new DetalleVenta(producto2, 10)); // sin validar

        int stockInicialProducto1 = producto1.getStock();
        int stockInicialProducto2 = producto2.getStock();

        // When
        boolean resultado = gestorVentas.procesarVenta(venta);

        // Then
        assertFalse(resultado);
        assertEquals(stockInicialProducto1, producto1.getStock());
        assertEquals(stockInicialProducto2, producto2.getStock());
        verify(registroVentas, never()).registrarVenta(any());
    }


    @Test
    @DisplayName("Debería inyectar correctamente la dependencia por constructor")
    void testInyeccionDependencia() {
        // When
        GestorVentas gestor = new GestorVentas(registroVentas);

        // Then
        assertNotNull(gestor);
        // Verificar que se usa la dependencia inyectada
        gestor.obtenerHistorialVentas();
        verify(registroVentas, times(1)).obtenerVentas();
    }

    @Test
    @DisplayName("Debería manejar correctamente ventas con diferentes productos")
    void testVentasConDiferentesProductos() {
        // Given
        Producto producto3 = new Producto("Azúcar", "003", 20, 2.5, "Endulzantes");
        Venta venta = new Venta("V-001", LocalDate.now());
        
        gestorVentas.agregarDetalle(venta, producto1, 1);
        gestorVentas.agregarDetalle(venta, producto2, 1);
        gestorVentas.agregarDetalle(venta, producto3, 2);

        // When
        boolean resultado = gestorVentas.procesarVenta(venta);

        // Then
        assertTrue(resultado);
        assertEquals(9, producto1.getStock()); // 10 - 1
        assertEquals(4, producto2.getStock()); // 5 - 1
        assertEquals(18, producto3.getStock()); // 20 - 2
        verify(registroVentas, times(1)).registrarVenta(venta);
    }
}