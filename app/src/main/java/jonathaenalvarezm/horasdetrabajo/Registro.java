package jonathaenalvarezm.horasdetrabajo;

import java.io.Serializable;

/**
 * Created by Jonathan EAM on 03/10/2017.
 */

public class Registro implements Serializable{

    private int id;
    private String dia;
    private String fecha;
    private String horas;
    private String comentario;
    private int idSemana;
    private int idProyecto;
    private int idActividad;

    public Registro(int id, String dia, String fecha, String horas, String comentario, int idSemana, int idActividad,int idProyecto) {
        this.id = id;
        this.dia = dia;
        this.fecha = fecha;
        this.horas = horas;
        this.comentario = comentario;
        this.idSemana = idSemana;
        this.idProyecto = idProyecto;
        this.idActividad = idActividad;
    }

    public int getId() {
        return id;
    }

    public String getDia() {
        return dia;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHoras() {
        return horas;
    }

    public String getComentario() {
        return comentario;
    }

    public int getIdSemana() {
        return idSemana;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public int getIdActividad() {
        return idActividad;
    }
}
