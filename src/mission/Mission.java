package mission;

public interface Mission {

    //Nombre que se muestra en el selector de Misiones.
    String getName();

    //Cargamos el ejemplo
    String getSampleInput();

    //Resuelve la misión completa sobre el texto pegado por el usuario.
    MissionOutcome solve(String input);
}
