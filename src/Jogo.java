import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Jogo {

    private final List<List<Posicoes>> posicoes;

    public Jogo(List<List<Posicoes>> posicoes) {
        this.posicoes = posicoes;
    }

    public List<List<Posicoes>> getPosicoes() {
        return posicoes;
    }

    public StatusEnum getStatus(){
        //Flat map pega os valores da lista mais interna e verifica. Se não houver posições fixas e nulas, o jogo já foi iniciado. O ! inverte, portanto o jgo não foi iniciado
        if(posicoes.stream()
                .flatMap(Collection::stream)
                .noneMatch(p -> !p.isFIXO() && nonNull(p.getAtual()))){
            return StatusEnum.NAO_INICIADO;
        }

        //tem alguma posição na lista que não está com o status atual preenchido? Se tiver, incompleto, se não, jogo completo
        return posicoes.stream()
                .flatMap(Collection::stream)
                .anyMatch(p -> isNull(p.getAtual())) ? StatusEnum.INCOMPLETO : StatusEnum.COMPLETO;
    }

    public boolean erros(){
        if(getStatus() == StatusEnum.NAO_INICIADO){
            return false;
        }
        //se alguma posicoes estiver com um valor diferente do esperado, o jogo tem erros.
        return posicoes.stream()
                .flatMap(Collection::stream)
                .anyMatch(p -> nonNull(p.getAtual()) && !p.getAtual().equals(p.getESPERADO()));
    }

    public boolean mudarValor(final int col, final int lin, final int valor){
        Posicoes posicao = posicoes.get(col).get(lin);
        if(posicao.isFIXO()){
            return false;
        }
        posicao.setAtual(valor);
        return true;
    }

    public boolean limparValor(final int col, final int lin){
        Posicoes posicao = posicoes.get(col).get(lin);
        if(posicao.isFIXO()){
            return false;
        }
        posicao.limparPosicao();
        return true;
    }

    public void resetar(){
        posicoes.forEach(p -> p.forEach(Posicoes::limparPosicao));
    }

    public boolean jogoFinalizado(){
        return !erros() && getStatus().equals(StatusEnum.COMPLETO);
    }
}
