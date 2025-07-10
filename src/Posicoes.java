public class Posicoes {
    private Integer atual;
    private final int ESPERADO;
    private final boolean FIXO;

    public Posicoes(int ESPERADO, boolean FIXO) {
        this.ESPERADO = ESPERADO;
        this.FIXO = FIXO;
        if(FIXO){
            atual = ESPERADO;
        }
    }

    public void setAtual(Integer atual) {
        if(FIXO) return;
        this.atual = atual;
    }

    public Integer getAtual() {
        return atual;
    }

    public void limparPosicao(){
        setAtual(null);
    }

    public boolean isFIXO() {
        return FIXO;
    }

    public int getESPERADO() {
        return ESPERADO;
    }


}
