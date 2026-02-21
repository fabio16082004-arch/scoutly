package it.business_logic.filtro;

import it.business_logic.filtro.criteri.*;
import java.util.ArrayList;
import java.util.List;

public class CalciatoreFiltro {
    private final List<CriterioFiltro> criteri;

    private CalciatoreFiltro(Builder builder) {
        this.criteri = List.copyOf(builder.criteri);
    }

    public void applicaFiltri(StringBuilder sql, List<Object> params) {
        for (CriterioFiltro c : criteri) {
            c.applica(sql, params);
        }
    }

    public static class Builder {
        private final List<CriterioFiltro> criteri = new ArrayList<>();
        private final String stagione;

        public Builder(String stagione) {
            this.stagione = stagione;
            criteri.add(new CriterioUguale("p.stagione", stagione));
        }

        public Builder withEta(Integer min, Integer max) {
            criteri.add(new CriterioAnni("c.dataNascita", min, max));
            return this;
        }

        public Builder withRuoli(List<String> sigleRuoli) {
            criteri.add(new CriterioIn("rc.Sigla", sigleRuoli));
            return this;
        }

        public Builder withSquadra(Integer idSquadra) {
            criteri.add(new CriterioUguale("sq.idSquadra", idSquadra));
            return this;
        }

        public Builder withCampionato(String campionato) {
            criteri.add(new CriterioUguale("sq.campionato", campionato));
            return this;
        }

        public Builder withAnniContratto(Integer minAnni, Integer maxAnni) {
            criteri.add(new CriterioAnni("co.dataFine", minAnni, maxAnni));
            return this;
        }

        public Builder withStipendio(Float min, Float max) {
            criteri.add(new CriterioRange("co.stipendio", min, max));
            return this;
        }

        public Builder withMinutiGiocati(Integer min) {
            criteri.add(new CriterioRange("st.minutiGiocati", min, null));
            return this;
        }

        public Builder withGol(Integer min) {
            criteri.add(new CriterioRange("st.gol", min, null));
            return this;
        }

        public Builder withAssist(Integer min) {
            criteri.add(new CriterioRange("st.assist", min, null));
            return this;
        }

        public Builder withXG(Double min) {
            criteri.add(new CriterioRange("st.xG", min, null));
            return this;
        }

        public Builder withXA(Double min) {
            criteri.add(new CriterioRange("st.xA", min, null));
            return this;
        }

        public Builder withTiriTotali(Integer min) {
            criteri.add(new CriterioRange("st.tiriTotali", min, null));
            return this;
        }

        public Builder withTiriInPorta(Integer min) {
            criteri.add(new CriterioRange("st.tiriInPorta", min, null));
            return this;
        }

        public Builder withDribbling(Integer min) {
            criteri.add(new CriterioRange("st.dribblingRiusciti", min, null));
            return this;
        }

        public Builder withTocchiArea(Integer min) {
            criteri.add(new CriterioRange("st.tocchiInAreaAvversaria", min, null));
            return this;
        }

        public Builder withContrasti(Integer min) {
            criteri.add(new CriterioRange("st.contrastiVinti", min, null));
            return this;
        }

        public Builder withDuelliAerei(Integer min) {
            criteri.add(new CriterioRange("st.duelliAereiVinti", min, null));
            return this;
        }

        public Builder withPassaggiChiave(Integer min) {
            criteri.add(new CriterioRange("st.passaggiChiave", min, null));
            return this;
        }

        public Builder withCross(Integer min) {
            criteri.add(new CriterioRange("st.crossRiusciti", min, null));
            return this;
        }

        public Builder withPassaggiRealizzati(Integer min) {
            criteri.add(new CriterioRange("st.passaggiRealizzati", min, null));
            return this;
        }

        public Builder withParate(Integer min) {
            criteri.add(new CriterioRange("st.parate", min, null));
            return this;
        }

        public Builder withCleanSheet(Integer min) {
            criteri.add(new CriterioRange("st.cleanSheet", min, null));
            return this;
        }

        public Builder withMaxGialli(Integer max) {
            criteri.add(new CriterioRange("st.cartelliniGialli", null, max));
            return this;
        }

        public Builder withMaxRossi(Integer max) {
            criteri.add(new CriterioRange("st.cartelliniRossi", null, max));
            return this;
        }

        public CalciatoreFiltro build() {
            return new CalciatoreFiltro(this);
        }
    }
}