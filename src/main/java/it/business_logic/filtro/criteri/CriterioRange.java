package it.business_logic.filtro.criteri;


import java.util.List;

public class CriterioRange implements CriterioFiltro {
    private final String campo;
    private final Object min;
    private final Object max;

    public CriterioRange(String campo, Object min, Object max) {
        this.campo = campo;
        this.min = min;
        this.max = max;
    }

    @Override
    public void applica(StringBuilder sql, List<Object> params) {
        if (min != null) {
            sql.append(" AND ").append(campo).append(" >= ? ");
            params.add(min);
        }
        if (max != null) {
            sql.append(" AND ").append(campo).append(" <= ? ");
            params.add(max);
        }
    }
}

