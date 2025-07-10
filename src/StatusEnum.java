public enum StatusEnum {

    NAO_INICIADO("Não iniciado"),
    INCOMPLETO("Incompleto"),
    COMPLETO("Completo");

    private String legenda;

    StatusEnum(final String legenda) {
        this.legenda = legenda;
    }

    public String getLegenda() {
        return legenda;
    }
}
