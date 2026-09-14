package mission;

import java.util.ArrayList;
import java.util.List;

/**
 * Resultado completo de resolver una misión.
 *
 * 1. Las lineas de texto.
 * 2. Los datos de dibujo.
 *
 * Se hacen con dos ArrayList que crecen en paralelo.
 *
 */


public class MissionOutcome {
    private final List<String> lines = new ArrayList<String>();
    private final List<Object> drawings = new ArrayList<Object>();

    public void addCase(String line, Object drawing){
        lines.add(line);
        drawings.add(drawing);
    }

    public List<String> getLines(){
        return lines;
    }

    public List<Object> getDrawings(){
        return drawings;
    }

    public int getCaseCount(){
        return lines.size();
    }


    /**
     * Juntamos las lineas en un solo texto.
     */

    public String getFullOutput(){
        StringBuilder sb = new StringBuilder(); //Se utiliza StringBuilder para concatenar dentro de un ciclo.
        for (int i = 0; i < lines.size(); i++){
            if(i>0){
                sb.append("\n"); //Un salto entre lineas pero no al final.
            }
            sb.append(lines.get(i));
        }
        return sb.toString();
    }
}
