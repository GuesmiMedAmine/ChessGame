package modele.deco;

import modele.plateau.Case;
import java.util.List;

public abstract class Deco {
    public abstract List<Case> getCasesAccessibles();
}