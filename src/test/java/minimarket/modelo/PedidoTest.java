package minimarket.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class PedidoTest {

    private Pedido pedido;
    private Producto arroz;
    private Producto azucar;

    @BeforeEach
    void setUp() {
        pedido = new Pedido("P001", LocalDate.of(2025, 11, 4));

        arroz = new Producto("Arroz", "001", 10, 3.5, "Granos");
        azucar = new Producto("Azúcar", "002", 20, 2.0, "Dulces");

        DetallePedido detalle1 = new DetallePedido(arroz, 2); // 3.5 * 2 = 7.0
        DetallePedido detalle2 = new DetallePedido(azucar, 3); // 2.0 * 3 = 6.0

        pedido.agregarDetalle(detalle1);
        pedido.agregarDetalle(detalle2);
    }

    @Test
    void testAgregarDetalle() {
        assertEquals(2, pedido.getDetalles().size());
        assertEquals("Arroz", pedido.getDetalles().get(0).getProducto().getNombre());
    }

    @Test
    void testCalcularMontoTotal() {
        assertEquals(13.0, pedido.calcularMontoTotal(), 0.001);
    }

    @Test
    void testGetMontoTotalDelegadoACalcularMontoTotal() {
        assertEquals(pedido.calcularMontoTotal(), pedido.getMontoTotal(), 0.001);
    }

    @Test
    void testToStringContieneInformacionCorrecta() {
        String texto = pedido.toString();

        assertTrue(texto.contains("Pedido P001"));
        assertTrue(texto.contains("Monto total: S/ 13.0"));
        assertTrue(texto.contains("Arroz"));
        assertTrue(texto.contains("Azúcar"));
    }

    @Test
    void testPedidoSinDetallesMontoCero() {
        Pedido pedidoVacio = new Pedido("P002", LocalDate.now());
        assertEquals(0.0, pedidoVacio.calcularMontoTotal(), 0.001);
        assertTrue(pedidoVacio.toString().contains("Monto total: S/ 0.0"));
    }
    
    @Test
    void testGetCodigoRetornaCodigoCorrecto() {
        assertEquals("P001", pedido.getCodigo());
    }
}
