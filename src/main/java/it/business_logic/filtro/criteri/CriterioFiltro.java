package it.business_logic.filtro.criteri;

import java.util.List;

public interface CriterioFiltro {
    void applica(StringBuilder sql, List<Object> params);
}
