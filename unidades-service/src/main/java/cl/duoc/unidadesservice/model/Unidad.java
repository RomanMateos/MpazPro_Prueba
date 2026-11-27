package cl.duoc.unidadesservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "unidades")
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 80, message = "El título debe tener entre 3 y 80 caracteres")
    private String titulo;

    @NotBlank(message = "La asignatura es obligatoria")
    @Pattern(
        regexp = "^(Lenguaje y Comunicación|Matemática|Ciencias Naturales|Historia y Geografía)$",
        message = "Asignatura inválida"
    )
    private String asignatura;

    @NotBlank(message = "El OA es obligatorio")
    @Pattern(regexp = "^OA\\d{2}$", message = "El OA debe tener formato OA seguido de 2 dígitos (ej. OA03)")
    private String oa;

    public Unidad() {}

    public Unidad(String titulo, String asignatura, String oa) {
        this.titulo = titulo;
        this.asignatura = asignatura;
        this.oa = oa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }
    public String getOa() { return oa; }
    public void setOa(String oa) { this.oa = oa; }
}
