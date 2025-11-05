package minimarket.negocio;

import minimarket.modelo.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventarioTest {

    private Inventario inventario;
    private Producto producto1;
    private Producto producto2;
    private Producto producto3;

    @BeforeEach
    void setUp() {
        inventario = new Inventario();
        
        producto1 = new Producto("Arroz", "P001", 10, 3.50, "Granos");
        producto2 = new Producto("Aceite", "P002", 5, 12.00, "Aceites");
        producto3 = new Producto("Azúcar", "P003", 8, 4.20, "Endulzantes");
    }

    @Test
    @DisplayName("Debería agregar productos al inventario correctamente")
    void testAgregarProducto() {
        // When
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto2);

        // Then
        List<Producto> productos = inventario.getProductos();
        assertEquals(2, productos.size());
        assertTrue(productos.contains(producto1));
        assertTrue(productos.contains(producto2));
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando el inventario está vacío")
    void testGetProductos_InventarioVacio() {
        // When
        List<Producto> productos = inventario.getProductos();

        // Then
        assertNotNull(productos);
        assertTrue(productos.isEmpty());
    }

    @Test
    @DisplayName("Debería buscar producto por nombre existente")
    void testBuscarPorNombre_ProductoExistente() {
        // Given
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto2);
        inventario.agregarProducto(producto3);

        // When
        Producto encontrado = inventario.buscarPorNombre("aceite");

        // Then
        assertNotNull(encontrado);
        assertEquals("Aceite", encontrado.getNombre());
        assertEquals("P002", encontrado.getCodigo());
        assertEquals(12.00, encontrado.getPrecio());
        assertEquals(5, encontrado.getStock());
        assertEquals("Aceites", encontrado.getCategoria());
    }

    @Test
    @DisplayName("Debería retornar null al buscar por nombre no existente")
    void testBuscarPorNombre_ProductoNoExistente() {
        // Given
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto2);

        // When
        Producto encontrado = inventario.buscarPorNombre("leche");

        // Then
        assertNull(encontrado);
    }

    @Test
    @DisplayName("Debería buscar producto por nombre ignorando mayúsculas/minúsculas")
    void testBuscarPorNombre_CaseInsensitive() {
        // Given
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto3);

        // When & Then
        assertNotNull(inventario.buscarPorNombre("ARROZ"));
        assertNotNull(inventario.buscarPorNombre("arroz"));
        assertNotNull(inventario.buscarPorNombre("ArRoZ"));
    }

    @Test
    @DisplayName("Debería buscar producto por código existente")
    void testBuscarPorCodigo_ProductoExistente() {
        // Given
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto2);
        inventario.agregarProducto(producto3);

        // When
        Producto encontrado = inventario.buscarPorCodigo("P003");

        // Then
        assertNotNull(encontrado);
        assertEquals("Azúcar", encontrado.getNombre());
        assertEquals("P003", encontrado.getCodigo());
        assertEquals(4.20, encontrado.getPrecio());
        assertEquals(8, encontrado.getStock());
        assertEquals("Endulzantes", encontrado.getCategoria());
    }

    @Test
    @DisplayName("Debería retornar null al buscar por código no existente")
    void testBuscarPorCodigo_ProductoNoExistente() {
        // Given
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto2);

        // When
        Producto encontrado = inventario.buscarPorCodigo("P999");

        // Then
        assertNull(encontrado);
    }

    @Test
    @DisplayName("Debería buscar producto por código ignorando mayúsculas/minúsculas")
    void testBuscarPorCodigo_CaseInsensitive() {
        // Given
        inventario.agregarProducto(producto2);

        // When & Then
        assertNotNull(inventario.buscarPorCodigo("p002"));
        assertNotNull(inventario.buscarPorCodigo("P002"));
    }

    @Test
    @DisplayName("Debería retornar null al buscar en inventario vacío")
    void testBuscarEnInventarioVacio() {
        // When
        Producto porNombre = inventario.buscarPorNombre("arroz");
        Producto porCodigo = inventario.buscarPorCodigo("P001");

        // Then
        assertNull(porNombre);
        assertNull(porCodigo);
    }

    @Test
    @DisplayName("Debería manejar búsqueda con nombres nulos o vacíos")
    void testBuscarPorNombre_NuloOVacio() {
        // Given
        inventario.agregarProducto(producto1);

        // When & Then
        assertNull(inventario.buscarPorNombre(null));
        assertNull(inventario.buscarPorNombre(""));
        assertNull(inventario.buscarPorNombre("   "));
    }

    @Test
    @DisplayName("Debería manejar búsqueda con códigos nulos o vacíos")
    void testBuscarPorCodigo_NuloOVacio() {
        // Given
        inventario.agregarProducto(producto1);

        // When & Then
        assertNull(inventario.buscarPorCodigo(null));
        assertNull(inventario.buscarPorCodigo(""));
        assertNull(inventario.buscarPorCodigo("   "));
    }

    @Test
    @DisplayName("Debería mantener el orden de inserción de productos")
    void testOrdenDeProductos() {
        // When
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto2);
        inventario.agregarProducto(producto3);

        // Then
        List<Producto> productos = inventario.getProductos();
        assertEquals(3, productos.size());
        assertEquals(producto1, productos.get(0));
        assertEquals(producto2, productos.get(1));
        assertEquals(producto3, productos.get(2));
    }

    @Test
    @DisplayName("Debería permitir agregar productos duplicados")
    void testAgregarProductosDuplicados() {
        // When
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto1); // Mismo producto otra vez

        // Then
        List<Producto> productos = inventario.getProductos();
        assertEquals(2, productos.size());
        assertEquals(producto1, productos.get(0));
        assertEquals(producto1, productos.get(1));
    }

    @Test
    @DisplayName("Debería encontrar productos con categorías diferentes")
    void testProductosConDiferentesCategorias() {
        // Given
        Producto producto4 = new Producto("Leche", "P004", 15, 5.80, "Lácteos");
        Producto producto5 = new Producto("Atún", "P005", 7, 8.50, "Conservas");
        
        inventario.agregarProducto(producto4);
        inventario.agregarProducto(producto5);

        // When
        Producto lacteo = inventario.buscarPorNombre("leche");
        Producto conserva = inventario.buscarPorCodigo("P005");

        // Then
        assertNotNull(lacteo);
        assertEquals("Lácteos", lacteo.getCategoria());
        assertNotNull(conserva);
        assertEquals("Conservas", conserva.getCategoria());
    }
    
    @Test
    @DisplayName("Debería mostrar productos correctamente en consola")
    void testMostrarProductos() {
        Producto producto1 = new Producto("Arroz", "P001", 10, 3.50, "Granos");
        Producto producto2 = new Producto("Aceite", "P002", 5, 12.00, "Aceites");
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto2);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        

        inventario.mostrarProductos();
        
        System.setOut(originalOut);
        
        String output = outContent.toString();
        assertTrue(output.contains("--- Productos disponibles ---"));
        assertTrue(output.contains("1] Arroz - S/3.50 - Stock: 10"));
        assertTrue(output.contains("2] Aceite - S/12.00 - Stock: 5"));
    }
}