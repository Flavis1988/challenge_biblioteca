package com.biblioteca.demo.principal;

import com.biblioteca.demo.model.*;
import com.biblioteca.demo.repository.AutorRepository;
import com.biblioteca.demo.repository.LibroRepository;
import com.biblioteca.demo.service.ConsumoAPI;
import com.biblioteca.demo.service.ConvierteDatos;
import java.util.ArrayList;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private List<DatosLibros> datosLibros = new ArrayList<>();
    private List<DatosLibros> datosAutor = new ArrayList<>();
    private LibroRepository repositorio;
    private AutorRepository repositorioAutor;

    public Principal(LibroRepository repositorio, AutorRepository repositorioAutor) {
        this.repositorio = repositorio;
        this.repositorioAutor = repositorioAutor;
    }


    public void muestraElMenu() {

        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    Elija una opción:
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un intervalo de años
                    5 - Listar libros por idiomas
                    0 - Salir
        """;
            System.out.println(menu);
            while (!teclado.hasNextInt()){
                System.out.println("Por favor ingresá un número válido");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }
    private Datos getDatosLibros() {

        System.out.println("Ingrese el nombre del libro");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
//        System.out.println(json);
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        return datos;
    }
    private void buscarLibroPorTitulo() {
        Datos datos = getDatosLibros();

        Libro libro = new Libro(datos.resultados().get(0));
        Autor autor = new Autor(libro.getAutor().get(0));
        repositorio.save(libro);
        repositorioAutor.save(autor);
        System.out.println(libro.toString());
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = repositorio.findAll();
        if (libros.isEmpty()){
            System.out.println("No hay Libros registrados");
            return;
        }
        System.out.println("LOS LIBROS REGISTRADOS SON:");
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = repositorioAutor.findAll();
        if (autores.isEmpty()){
            System.out.println("No hay autores registrados");
            return;
        }
        System.out.println("Los autores registrados son:");
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.println("Escribe el año en el que deseas buscar: ");
        var año = teclado.nextInt();
        teclado.nextLine();
        if(año < 0) {
            System.out.println("El año no puede ser negativo, intenta de nuevo");
            return;
        }
        List<Autor> autoresPorAño = repositorioAutor.findByFechaDeNacimientoLessThanEqualAndFechaDeFallecidoGreaterThanEqual(año, año);
        if (autoresPorAño.isEmpty()) {
            System.out.println("No hay autores registrados en ese año");
            return;
        }
        System.out.println("----- LOS AUTORES VIVOS REGISTRADOS EN EL AÑO " + año + " SON: -----\n");
        autoresPorAño.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Escribe el idioma por el que deseas buscar: ");
        String menu = """
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """;
        System.out.println(menu);
        var idioma = teclado.nextLine();
        if (!idioma.equals("es") && !idioma.equals("en") && !idioma.equals("fr") && !idioma.equals("pt")) {
            System.out.println("Idioma no válido, intenta de nuevo");
            return;
        }
        List<Libro> librosPorIdioma = repositorio.findByIdiomasContaining(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma");
            return;
        }
        System.out.println("----- LOS LIBROS REGISTRADOS EN EL IDIOMA SELECCIONADO SON: -----\n");
        librosPorIdioma.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }
}
