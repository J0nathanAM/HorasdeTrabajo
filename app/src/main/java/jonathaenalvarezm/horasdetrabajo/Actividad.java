package jonathaenalvarezm.horasdetrabajo;

import java.io.Serializable;

/**
 * Created by IDS Comercial on 04/10/2017.
 */

public class Actividad implements Serializable {
    private int idActividad;
    private String nombreActividad;

    public Actividad(int idActividad, String nombreActividad) {
        this.idActividad = idActividad;
        this.nombreActividad = nombreActividad;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }
}
