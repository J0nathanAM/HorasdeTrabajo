package jonathaenalvarezm.horasdetrabajo;

import java.io.Serializable;

/**
 * Created by IDS Comercial on 04/10/2017.
 */

public class Proyecto implements Serializable {

    private int idPreyecto;
    private String nombreProyecto;

    public Proyecto(int idPreyecto, String nombreProyecto) {
        this.idPreyecto = idPreyecto;
        this.nombreProyecto = nombreProyecto;
    }

    public int getIdPreyecto() {
        return idPreyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }
}
