package minimarket.negocio;

import minimarket.modelo.*;
import minimarket.registro.RegistroVentas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestorConsultasTest {

    private GestorConsultas gestorConsultas;
    private RegistroVentas registroVentas;
    
    private Venta venta1;
    private Venta venta2;
    private Venta venta3;
    private Producto producto1;
    private Producto producto2;
    private Producto producto3;

    @BeforeEach
    void setUp() {
        registroVentas = mock(RegistroVentas.class);
        gestorConsultas = new GestorConsultas(registroVentas);
        
        // Crear productos de prueba con la sintaxis correcta
        producto1 = new Producto("Arroz", "001", 10, 3.5, "Granos");
        producto2 = new Producto("Aceite", "002", 15, 8.0, "Aceites");
        producto3 = new Producto("Azúcar", "003", 20, 2.5, "Endulzantes");
        
        // Crear ventas de prueba con diferentes fechas
        venta1 = crearVenta("V001", LocalDate.of(2024, 1, 15), 
                Arrays.asList(new DetalleVenta(producto1, 2), new DetalleVenta(producto2, 1)), 15.0);
        
        venta2 = crearVenta("V002", LocalDate.of(2024, 1, 20), 
                Arrays.asList(new DetalleVenta(producto1, 3), new DetalleVenta(producto3, 2)), 15.5);
        
        venta3 = crearVenta("V003", LocalDate.of(2024, 2, 1), 
                Arrays.asList(new DetalleVenta(producto2, 4)), 32.0);
    }

    private Venta crearVenta(String codigo, LocalDate fecha, List<DetalleVenta> detalles, double montoTotal) {
        Venta venta = new Venta();
        venta.setCodigo(codigo);
        venta.setFecha(fecha);
        venta.setDetalles(detalles);
        venta.setMontoTotal(montoTotal);
        return venta;
    }

    @Test
    @DisplayName("Debería obtener ventas dentro del rango de fechas")
    void testObtenerVentasEntre() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);
        List<Venta> todasLasVentas = Arrays.asList(venta1, venta2, venta3);
        
        when(registroVentas.obtenerVentas()).thenReturn(todasLasVentas);

        // When
        List<Venta> resultado = gestorConsultas.obtenerVentasEntre(inicio, fin);

        // Then
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(venta1));
        assertTrue(resultado.contains(venta2));
        assertFalse(resultado.contains(venta3));
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay ventas en el rango")
    void testObtenerVentasEntre_SinVentasEnRango() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 3, 1);
        LocalDate fin = LocalDate.of(2024, 3, 31);
        List<Venta> todasLasVentas = Arrays.asList(venta1, venta2, venta3);
        
        when(registroVentas.obtenerVentas()).thenReturn(todasLasVentas);

        // When
        List<Venta> resultado = gestorConsultas.obtenerVentasEntre(inicio, fin);

        // Then
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debería incluir ventas en las fechas límite")
    void testObtenerVentasEntre_IncluyeFechasLimite() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 1, 15);
        LocalDate fin = LocalDate.of(2024, 2, 1);
        List<Venta> todasLasVentas = Arrays.asList(venta1, venta2, venta3);
        
        when(registroVentas.obtenerVentas()).thenReturn(todasLasVentas);

        // When
        List<Venta> resultado = gestorConsultas.obtenerVentasEntre(inicio, fin);

        // Then
        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(venta1)); // fecha igual a inicio
        assertTrue(resultado.contains(venta2));
        assertTrue(resultado.contains(venta3)); // fecha igual a fin
    }

    @Test
    @DisplayName("Debería calcular correctamente el monto total en rango")
    void testCalcularMontoTotal() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(venta1, venta2, venta3));

        // When
        double resultado = gestorConsultas.calcularMontoTotal(inicio, fin);

        // Then
        assertEquals(30.5, resultado); // 15.0 + 15.5
    }

    @Test
    @DisplayName("Debería retornar 0 cuando no hay ventas para calcular monto total")
    void testCalcularMontoTotal_SinVentas() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 3, 1);
        LocalDate fin = LocalDate.of(2024, 3, 31);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(venta1, venta2, venta3));

        // When
        double resultado = gestorConsultas.calcularMontoTotal(inicio, fin);

        // Then
        assertEquals(0.0, resultado);
    }

    @Test
    @DisplayName("Debería contar correctamente las ventas en rango")
    void testContarVentas() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(venta1, venta2, venta3));

        // When
        long resultado = gestorConsultas.contarVentas(inicio, fin);

        // Then
        assertEquals(2, resultado);
    }

    @Test
    @DisplayName("Debería retornar 0 al contar ventas cuando no hay en rango")
    void testContarVentas_SinVentas() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 3, 1);
        LocalDate fin = LocalDate.of(2024, 3, 31);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(venta1, venta2, venta3));

        // When
        long resultado = gestorConsultas.contarVentas(inicio, fin);

        // Then
        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Debería identificar el producto más vendido en rango")
    void testObtenerProductoMasVendido() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(venta1, venta2, venta3));

        // When
        List<Producto> resultado = gestorConsultas.obtenerProductoMasVendido(inicio, fin);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Arroz", resultado.get(0).getNombre()); // Arroz: 2 + 3 = 5 unidades
        assertEquals("001", resultado.get(0).getCodigo());
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay productos vendidos en rango")
    void testObtenerProductoMasVendido_SinVentas() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 3, 1);
        LocalDate fin = LocalDate.of(2024, 3, 31);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(venta1, venta2, venta3));

        // When
        List<Producto> resultado = gestorConsultas.obtenerProductoMasVendido(inicio, fin);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debería manejar empate en productos más vendidos")
    void testObtenerProductoMasVendido_Empate() {
        // Given - Crear ventas que generen empate
        Producto producto4 = new Producto("Sal", "004", 25, 1.0, "Condimentos");
        Producto producto5 = new Producto("Harina", "005", 30, 4.0, "Harinas");
        
        Venta ventaEmpate1 = crearVenta("V004", LocalDate.of(2024, 1, 10), 
                Arrays.asList(new DetalleVenta(producto4, 3)), 3.0);
        Venta ventaEmpate2 = crearVenta("V005", LocalDate.of(2024, 1, 12), 
                Arrays.asList(new DetalleVenta(producto5, 3)), 12.0);
        
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 15);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(ventaEmpate1, ventaEmpate2));

        // When
        List<Producto> resultado = gestorConsultas.obtenerProductoMasVendido(inicio, fin);

        // Then - Debería retornar lista con ambos productos empatados
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        
        // Verificar que contiene ambos productos
        List<String> nombres = resultado.stream()
                .map(Producto::getNombre)
                .collect(Collectors.toList());
        
        assertTrue(nombres.contains("Sal"));
        assertTrue(nombres.contains("Harina"));
    }

    @Test
    @DisplayName("Debería manejar ventas sin detalles")
    void testObtenerProductoMasVendido_VentasSinDetalles() {
        // Given
        Venta ventaSinDetalles = new Venta();
        ventaSinDetalles.setCodigo("V006");
        ventaSinDetalles.setFecha(LocalDate.of(2024, 1, 5));
        ventaSinDetalles.setMontoTotal(0.0);
        
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 10);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(ventaSinDetalles));

        // When
        List<Producto> resultado = gestorConsultas.obtenerProductoMasVendido(inicio, fin);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debería manejar diferentes categorías de productos")
    void testObtenerProductoMasVendido_DiferentesCategorias() {
        // Given
        Producto leche = new Producto("Leche", "006", 20, 2.8, "Lácteos");
        Producto pan = new Producto("Pan", "007", 15, 1.5, "Panadería");
        
        Venta ventaCategorias = crearVenta("V007", LocalDate.of(2024, 1, 8), 
                Arrays.asList(new DetalleVenta(leche, 5), new DetalleVenta(pan, 2)), 17.0);
        
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 10);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(ventaCategorias));

        // When
        List<Producto> resultado = gestorConsultas.obtenerProductoMasVendido(inicio, fin);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Leche", resultado.get(0).getNombre());
        assertEquals("Lácteos", resultado.get(0).getCategoria());
    }

    @Test
    @DisplayName("Debería manejar registro de ventas vacío")
    void testRegistroVentasVacio() {
        // Given
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList());

        // When
        List<Venta> ventas = gestorConsultas.obtenerVentasEntre(inicio, fin);
        double monto = gestorConsultas.calcularMontoTotal(inicio, fin);
        long conteo = gestorConsultas.contarVentas(inicio, fin);
        List<Producto> productos = gestorConsultas.obtenerProductoMasVendido(inicio, fin);

        // Then
        assertTrue(ventas.isEmpty());
        assertEquals(0.0, monto);
        assertEquals(0, conteo);
        assertNotNull(productos);
        assertTrue(productos.isEmpty());
    }

    @Test
    @DisplayName("Debería calcular correctamente con precios reales")
    void testCalculosConPreciosReales() {
        // Given - Crear productos con precios reales
        Producto arroz = new Producto("Arroz", "001", 10, 3.5, "Granos");
        Producto aceite = new Producto("Aceite", "002", 15, 8.0, "Aceites");
        
        // Venta con 2 arrozes (7.0) + 1 aceite (8.0) = 15.0
        Venta ventaReal = crearVenta("V008", LocalDate.of(2024, 1, 25), 
                Arrays.asList(
                    new DetalleVenta(arroz, 2), // 2 * 3.5 = 7.0
                    new DetalleVenta(aceite, 1) // 1 * 8.0 = 8.0
                ), 15.0);
        
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(ventaReal));

        // When
        double montoTotal = gestorConsultas.calcularMontoTotal(inicio, fin);
        long conteoVentas = gestorConsultas.contarVentas(inicio, fin);
        List<Producto> productosMasVendidos = gestorConsultas.obtenerProductoMasVendido(inicio, fin);

        // Then
        assertEquals(15.0, montoTotal);
        assertEquals(1, conteoVentas);
        assertEquals(1, productosMasVendidos.size());
        assertEquals("Arroz", productosMasVendidos.get(0).getNombre()); // 2 unidades vs 1 unidad
    }

    @Test
    @DisplayName("Debería manejar empate múltiple entre varios productos")
    void testObtenerProductoMasVendido_EmpateMultiple() {
        // Given - Tres productos con misma cantidad vendida
        Producto p1 = new Producto("Sal", "008", 25, 1.0, "Condimentos");
        Producto p2 = new Producto("Harina", "009", 30, 4.0, "Harinas");
        Producto p3 = new Producto("Azúcar", "010", 40, 2.5, "Endulzantes");
        
        Venta v1 = crearVenta("V009", LocalDate.of(2024, 1, 10), 
                Arrays.asList(new DetalleVenta(p1, 5)), 5.0);
        Venta v2 = crearVenta("V010", LocalDate.of(2024, 1, 12), 
                Arrays.asList(new DetalleVenta(p2, 5)), 20.0);
        Venta v3 = crearVenta("V011", LocalDate.of(2024, 1, 15), 
                Arrays.asList(new DetalleVenta(p3, 5)), 12.5);
        
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 20);
        
        when(registroVentas.obtenerVentas()).thenReturn(Arrays.asList(v1, v2, v3));

        // When
        List<Producto> resultado = gestorConsultas.obtenerProductoMasVendido(inicio, fin);

        // Then
        assertEquals(3, resultado.size());
        
        List<String> nombresEsperados = Arrays.asList("Sal", "Harina", "Azúcar");
        List<String> nombresResultados = resultado.stream()
                .map(Producto::getNombre)
                .collect(Collectors.toList());
        
        assertTrue(nombresResultados.containsAll(nombresEsperados));
    }
}