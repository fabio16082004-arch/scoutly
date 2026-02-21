package it.business_logic.filtro.criteri;

import java.util.List;

public class CriterioUguale implements CriterioFiltro {
    private final String campo;
    private final Object valore;

    public CriterioUguale(String campo, Object valore) {
        this.campo = campo;
        this.valore = valore;
    }

    @Override
    public void applica(StringBuilder sql, List<Object> params) {
        if (valore != null) {
            sql.append(" AND ").append(campo).append(" = ? ");
            params.add(valore);
        }
    }
}
