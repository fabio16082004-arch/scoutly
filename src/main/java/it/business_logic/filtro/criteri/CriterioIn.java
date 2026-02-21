package it.business_logic.filtro.criteri;

import java.util.Collections;
import java.util.List;

public class CriterioIn implements CriterioFiltro{
    private final String campo;
    private final List<?> valori;

    public CriterioIn(String campo, List<?> valori) {
        this.campo = campo;
        this.valori = valori;
    }

    @Override
    public void applica(StringBuilder sql, List<Object> params) {
        if (valori != null && !valori.isEmpty()) {
            sql.append(" AND ").append(campo).append(" IN (");
            sql.append(String.join(",", Collections.nCopies(valori.size(), "?")));
            sql.append(") ");
            params.addAll(valori);
        }
    }
}
