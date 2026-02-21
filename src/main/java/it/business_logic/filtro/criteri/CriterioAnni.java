package it.business_logic.filtro.criteri;

import java.util.List;

public class CriterioAnni implements CriterioFiltro {
    private final String colonnaData;
    private final Integer minAnni;
    private final Integer maxAnni;

    public CriterioAnni(String colonnaData, Integer minAnni, Integer maxAnni) {
        this.colonnaData = colonnaData;
        this.minAnni = minAnni;
        this.maxAnni = maxAnni;
    }

    @Override
    public void applica(StringBuilder sql, List<Object> params) {
        if (minAnni != null) {
            sql.append(" AND EXTRACT(YEAR FROM AGE(NOW(), ").append(colonnaData).append(")) >= ? ");
            params.add(minAnni);
        }

        if (maxAnni != null) {
            sql.append(" AND EXTRACT(YEAR FROM AGE(NOW(), ").append(colonnaData).append(")) <= ? ");
            params.add(maxAnni);
        }
    }
}
