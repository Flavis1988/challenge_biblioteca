package com.biblioteca.demo.repository;

import com.biblioteca.demo.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    List<Autor> findByFechaDeNacimientoLessThanEqualAndFechaDeFallecidoGreaterThanEqual(int anoInicial, int anoFinal);

}
