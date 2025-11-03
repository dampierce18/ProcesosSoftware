package sistema.minimarket.consola;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {
	/*public static void main(String[] args) {
	    java.util.Scanner sc = new java.util.Scanner(System.in);
	    String rol = (args.length > 0) ? args[0] : "";
	    MainApp app = new MainApp();

	    if (rol.equalsIgnoreCase("admin")) {
	        app.menuAdministrador(sc);
	    } else if (rol.equalsIgnoreCase("empleado")) {
	        app.menuEmpleado(sc);
	    } else {
	        System.out.println("Acceso no autorizado. Inicie sesión desde la interfaz gráfica.");
	    }

	    sc.close();
	}*/


    public static void main(String[] args) {
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
                case 1:
                    iniciarSesion(sc);
                    break;
                case 2:
                    System.out.println("Saliendo del sistema...");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    break;
            }
        }

        sc.close();
    }
	
	
    private static void iniciarSesion(Scanner sc) {
        System.out.print("\nUsuario: ");
        String usuario = sc.nextLine();

        System.out.print("Contraseña: ");
        String contrasena = sc.nextLine();

        if (usuario.equals("admin") && contrasena.equals("1234")) {
            menuAdministrador(sc);
        } else if (usuario.equals("empleado") && contrasena.equals("1234")) {
            menuEmpleado(sc);
        } else {
            System.out.println("Credenciales incorrectas. Intente nuevamente.");
        }
    }

    private static void menuAdministrador(Scanner sc) {
        int opcion = 0;
        do {
            System.out.println("\n=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Procesar pedido");
            System.out.println("2. Procesar venta");
            System.out.println("3. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                	procesarPedidoSimulado(sc);
                    break;
                case 2:
                    procesarVentaSimulada(sc);
                    break;
                case 3:
                    System.out.println("Cerrando sesión...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (opcion != 3);
    }

    private static void menuEmpleado(Scanner sc) {
        int opcion = 0;
        do {
            System.out.println("\n=== MENÚ EMPLEADO ===");
            System.out.println("1. Procesar venta");
            System.out.println("2. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    procesarVentaSimulada(sc);
                    break;
                case 2:
                    System.out.println("Cerrando sesión...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (opcion != 2);
    }

    private static void procesarVentaSimulada(Scanner sc) {
        System.out.println("\n=== PROCESAR VENTA ===");

        // Simulación: código y fecha automáticos
        String codigoVenta = "V-" + (int)(Math.random() * 10000);
        String fecha = "07/10/2025"; // fecha simulada

        System.out.println("Código de venta generado: " + codigoVenta);
        System.out.println("Fecha de venta: " + fecha);

        // Inventario simulado
        String[] nombres = {"Arroz", "Azúcar", "Aceite", "Leche", "Fideos"};
        double[] precios = {3.5, 2.8, 8.0, 4.5, 2.2};

        double montoTotal = 0;
        boolean agregarMas = true;

        while (agregarMas) {
            System.out.println("\n--- PRODUCTOS DISPONIBLES ---");
            for (int i = 0; i < nombres.length; i++) {
                System.out.println((i + 1) + ". " + nombres[i] + " - S/ " + precios[i]);
            }

            System.out.print("Seleccione el número del producto: ");
            int seleccion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            if (seleccion < 1 || seleccion > nombres.length) {
                System.out.println("❌ Opción inválida, intente nuevamente.");
            } else {
                String productoSeleccionado = nombres[seleccion - 1];
                double precio = precios[seleccion - 1];

                System.out.print("Cantidad: ");
                int cantidad = sc.nextInt();
                sc.nextLine();

                double subtotal = precio * cantidad;
                montoTotal += subtotal;

                System.out.println("✅ Producto agregado: " + productoSeleccionado);
                System.out.println("   Cantidad: " + cantidad);
                System.out.println("   Subtotal: S/ " + subtotal);
            }

            System.out.print("\n¿Desea agregar otro producto? (s/n): ");
            String continuar = sc.nextLine().toLowerCase();
            agregarMas = continuar.equals("s");
        }

        System.out.println("\n=== RESUMEN DE LA VENTA ===");
        System.out.println("Código: " + codigoVenta);
        System.out.println("Fecha: " + fecha);
        System.out.println("Monto total: S/ " + montoTotal);
        System.out.println("\nVenta registrada correctamente (simulada).");
    }
    
    private static void procesarPedidoSimulado(Scanner sc) {
        // Inventario simulado
        String[] nombres = {"Arroz", "Azúcar", "Aceite", "Leche", "Fideos"};
        double[] precios = {3.5, 2.8, 8.0, 4.5, 2.2};
        int[] stock = {10, 12, 5, 8, 15};

        // Lista de pedidos pendientes (cada pedido es un arreglo de cantidades)
        List<int[]> pedidosPendientes = new ArrayList<>();

        boolean salirSubmenu = false;

        while (!salirSubmenu) {
            System.out.println("\n=== Procesar Pedido ===");
            System.out.println("1. Hacer nuevo pedido");
            System.out.println("2. Ver / Confirmar pedidos en curso");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    // === HACER NUEVO PEDIDO ===
                    int[] cantidadesPedido = new int[nombres.length];
                    boolean continuar = true;

                    while (continuar) {
                        System.out.println("\n--- Productos disponibles ---");
                        for (int i = 0; i < nombres.length; i++) {
                            System.out.printf("[%d] %s - S/%.2f - Stock actual: %d\n",
                                    i + 1, nombres[i], precios[i], stock[i]);
                        }

                        System.out.print("Seleccione un producto para añadir al pedido (0 para finalizar): ");
                        int prod = sc.nextInt();
                        sc.nextLine();

                        if (prod == 0) {
                            continuar = false;
                        } else if (prod > 0 && prod <= nombres.length) {
                            System.out.print("Ingrese cantidad a pedir: ");
                            int cant = sc.nextInt();
                            sc.nextLine();

                            if (cant > 0) {
                                cantidadesPedido[prod - 1] += cant;
                                System.out.println(cant + " unidades de " + nombres[prod - 1] + " añadidas al pedido.");
                            } else {
                                System.out.println("Cantidad inválida.");
                            }
                        } else {
                            System.out.println("Opción inválida.");
                        }
                    }

                    // Verificar si se añadió algo
                    boolean hayProductos = false;
                    for (int c : cantidadesPedido) {
                        if (c > 0) {
                            hayProductos = true;
                            break;
                        }
                    }

                    if (hayProductos) {
                        pedidosPendientes.add(cantidadesPedido);
                        System.out.println("Pedido registrado correctamente. Ahora figura como 'en curso'.");
                    } else {
                        System.out.println("No se añadieron productos. Pedido cancelado.");
                    }
                    break;

                case 2:
                    // === VER / CONFIRMAR PEDIDOS ===
                    if (pedidosPendientes.isEmpty()) {
                        System.out.println("\nNo hay pedidos en curso actualmente.");
                        break;
                    }

                    System.out.println("\n--- Pedidos en curso ---");
                    for (int i = 0; i < pedidosPendientes.size(); i++) {
                        System.out.println("Pedido #" + (i + 1) + ":");
                        int[] pedido = pedidosPendientes.get(i);
                        for (int j = 0; j < nombres.length; j++) {
                            if (pedido[j] > 0) {
                                System.out.printf("   %s: %d unidades\n", nombres[j], pedido[j]);
                            }
                        }
                    }

                    System.out.print("\nSeleccione el número del pedido que llegó (0 para volver): ");
                    int pedidoConfirmar = sc.nextInt();
                    sc.nextLine();

                    if (pedidoConfirmar > 0 && pedidoConfirmar <= pedidosPendientes.size()) {
                        int[] pedido = pedidosPendientes.get(pedidoConfirmar - 1);

                        // Aumentar el stock
                        for (int j = 0; j < nombres.length; j++) {
                            stock[j] += pedido[j];
                        }

                        pedidosPendientes.remove(pedidoConfirmar - 1);
                        System.out.println("Pedido confirmado. Stock actualizado correctamente.");
                    } else if (pedidoConfirmar != 0) {
                        System.out.println("Opción inválida.");
                    }
                    break;

                case 0:
                    salirSubmenu = true;
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    
}
