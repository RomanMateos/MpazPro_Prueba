package cl.duoc.unidadesservice;

import cl.duoc.unidadesservice.model.Unidad;
import cl.duoc.unidadesservice.repository.UnidadRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UnidadesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnidadesServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner cargarDatosIniciales(UnidadRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Unidad("Comprensión lectora de cuentos", "Lenguaje y Comunicación", "OA03"));
                repo.save(new Unidad("Sumas y restas hasta el 100", "Matemática", "OA09"));
                repo.save(new Unidad("Ciclo de vida de los animales", "Ciencias Naturales", "OA01"));
                repo.save(new Unidad("Pueblos originarios de Chile", "Historia y Geografía", "OA02"));
            }
        };
    }
}
