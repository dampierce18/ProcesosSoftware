package minimarket.consola;

import minimarket.modelo.*;
import minimarket.negocio.*;
import minimarket.registro.*;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;

public class MainApp {

    public static void main(String[] args) {
    	Inventario inventario = new Inventario();
    	RegistroVentas registroventas = new RegistroVentas();
    	RegistroPedidos registropedidos = new RegistroPedidos();
    	
    	inventario.agregarProducto(new Producto("Arroz", "P001", 10, 3.5, "Granos"));
        inventario.agregarProducto(new Producto("Azúcar", "P002", 12, 2.8, "Granos"));
        inventario.agregarProducto(new Producto("Aceite", "P003", 5, 8.0, "Abarrotes"));
        inventario.agregarProducto(new Producto("Leche", "P004", 8, 4.5, "Lácteos"));
        inventario.agregarProducto(new Producto("Fideos", "P005", 15, 2.2, "Pastas"));
    	
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== MINIMARKET ===");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> iniciarSesion(sc, inventario, registroventas);
                case 2 -> {
                    System.out.println("Saliendo del sistema...");
                    salir = true;
                }
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        }

        sc.close();
    }

    private static void iniciarSesion(Scanner sc, Inventario inv, RegistroVentas reg) {
        System.out.print("\nUsuario: ");
        String usuario = sc.nextLine();

        System.out.print("Contraseña: ");
        String contrasena = sc.nextLine();

        if (usuario.equals("admin") && contrasena.equals("1234")) {
            menuAdministrador(sc, inv, reg);
        } else if (usuario.equals("empleado") && contrasena.equals("1234")) {
            menuEmpleado(sc, inv, reg);
        } else {
            System.out.println("Credenciales incorrectas. Intente nuevamente.");
        }
    }

    private static void menuAdministrador(Scanner sc, Inventario inv, RegistroVentas reg) {
        int opcion;
        do {
            System.out.println("\n=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Procesar pedido");
            System.out.println("2. Procesar venta");
            System.out.println("3. Consultar ventas");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> procesarPedidoSimulado(sc);
                case 2 -> procesarVentaSimulada(sc, inv, reg);
                case 3 -> consultarVentas(sc, reg);
                case 4 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 4);
    }

    private static void menuEmpleado(Scanner sc, Inventario inv, RegistroVentas reg) {
        int opcion;
        do {
            System.out.println("\n=== MENÚ EMPLEADO ===");
            System.out.println("1. Procesar venta");
            System.out.println("2. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> procesarVentaSimulada(sc, inv, reg);
                case 2 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 2);
    }

    private static void procesarVentaSimulada(Scanner sc, Inventario inventario, RegistroVentas registro) {
        System.out.println("\n=== PROCESAR VENTA ===");

        GestorVentas gestor = new GestorVentas(registro);
        GestorConsultas consultas = new GestorConsultas(registro); // 🔹 agregado

        String codigoVenta = "V-" + (int) (Math.random() * 10000);
        LocalDate fecha = LocalDate.now();
        Venta venta = new Venta(codigoVenta, fecha);

        boolean agregarMas = true;
        while (agregarMas) {
            System.out.println("\n--- PRODUCTOS DISPONIBLES ---");
            inventario.mostrarProductos();

            System.out.print("Seleccione el número del producto: ");
            int seleccion = sc.nextInt();
            sc.nextLine();

            if (seleccion < 1 || seleccion > inventario.getProductos().size()) {
                System.out.println("❌ Opción inválida.");
                continue;
            }

            Producto producto = inventario.getProductos().get(seleccion - 1);
            System.out.print("Cantidad: ");
            int cantidad = sc.nextInt();
            sc.nextLine();

            boolean agregado = gestor.agregarDetalle(venta, producto, cantidad);
            if (agregado) {
                System.out.println("✅ Producto agregado: " + producto.getNombre());
            } else {
                System.out.println("❌ No se pudo agregar (sin stock suficiente).");
            }

            System.out.print("\n¿Desea agregar otro producto? (s/n): ");
            agregarMas = sc.nextLine().equalsIgnoreCase("s");
        }

        if (gestor.procesarVenta(venta)) {
            System.out.println("\n=== RESUMEN DE LA VENTA ===");
            System.out.println(venta);

            System.out.println("\n=== HISTORIAL DE VENTAS ===");
            registro.mostrarHistorial();
        } else {
            System.out.println("❌ No se pudo procesar la venta.");
        }
    }

    private static void consultarVentas(Scanner sc, RegistroVentas registro) {
        System.out.println("\n=== CONSULTAR VENTAS ===");

        // Simulación: se crea un registro con datos de prueba
  
        GestorConsultas consultas = new GestorConsultas(registro);

        // Aquí podrías cargar ventas existentes desde persistencia en el futuro
        System.out.println("Ingrese fecha de inicio (YYYY-MM-DD): ");
        LocalDate inicio = LocalDate.parse(sc.nextLine());
        System.out.println("Ingrese fecha de fin (YYYY-MM-DD): ");
        LocalDate fin = LocalDate.parse(sc.nextLine());

        List<Venta> ventas = consultas.obtenerVentasEntre(inicio, fin);

        System.out.println("\nVentas encontradas: " + ventas.size());
        for (Venta v : ventas) {
            System.out.println(v);
        }

        double total = consultas.calcularMontoTotal(inicio, fin);
        long cantidad = consultas.contarVentas(inicio, fin);
        List<Producto> masVendido = consultas.obtenerProductoMasVendido(inicio, fin);

        System.out.println("\n=== RESUMEN ===");
        System.out.println("Periodo: " + inicio + " a " + fin);
        System.out.println("Cantidad de ventas: " + cantidad);
        System.out.println("Monto total: S/ " + total);
        if (masVendido != null && !masVendido.isEmpty()) {
            System.out.print("Producto(s) más vendido(s): ");
            for (Producto p : masVendido) {
                System.out.print(p.getNombre() + " ");
            }
            System.out.println();
        } else {
            System.out.println("Producto más vendido: N/A");
        }
    }

    private static void procesarPedidoSimulado(Scanner sc) {
        System.out.println("\n=== PROCESAR PEDIDO ===");

        RegistroPedidos registroPedidos = new RegistroPedidos();
        GestorPedidos gestorPedidos = new GestorPedidos(registroPedidos);

        Producto[] inventario = {
            new Producto("Arroz", "P001", 10, 3.5, "Granos"),
            new Producto("Azúcar", "P002", 12, 2.8, "Granos"),
            new Producto("Aceite", "P003", 5, 8.0, "Abarrotes"),
            new Producto("Leche", "P004", 8, 4.5, "Lácteos"),
            new Producto("Fideos", "P005", 15, 2.2, "Pastas")
        };

        String codigo = "PED-" + (int)(Math.random() * 10000);
        LocalDate fecha = LocalDate.now();
        Pedido pedido = new Pedido(codigo, fecha);

        boolean agregarMas = true;
        while (agregarMas) {
            System.out.println("\n--- PRODUCTOS DISPONIBLES ---");
            for (int i = 0; i < inventario.length; i++) {
                Producto p = inventario[i];
                System.out.printf("%d. %s - S/ %.2f\n", i + 1, p.getNombre(), p.getPrecio());
            }

            System.out.print("Seleccione el número del producto: ");
            int seleccion = sc.nextInt();
            sc.nextLine();

            if (seleccion < 1 || seleccion > inventario.length) {
                System.out.println("❌ Opción inválida.");
                continue;
            }

            Producto producto = inventario[seleccion - 1];
            System.out.print("Cantidad solicitada: ");
            int cantidad = sc.nextInt();
            sc.nextLine();

            boolean agregado = gestorPedidos.agregarDetalle(pedido, producto, cantidad);
            if (agregado) {
                System.out.println("✅ Producto agregado al pedido: " + producto.getNombre());
            } else {
                System.out.println("❌ No se pudo agregar el producto.");
            }

            System.out.print("\n¿Desea agregar otro producto? (s/n): ");
            agregarMas = sc.nextLine().equalsIgnoreCase("s");
        }

        if (gestorPedidos.procesarPedido(pedido)) {
            System.out.println("\n=== RESUMEN DEL PEDIDO ===");
            System.out.println(pedido);

            System.out.println("\n=== HISTORIAL DE PEDIDOS ===");
            registroPedidos.mostrarHistorial();
        } else {
            System.out.println("❌ No se pudo procesar el pedido.");
        }
    }
}
