package app.web.gprojuridico.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelacaoEmpregaticia {

    private String dataAdmissao;
    private String dataSaida;
    private String funcaoExercida;
    private Integer valorSalarioCtps;
    private Boolean salarioAnotadoCtps;
    private Integer valorUltimaRemuneracao;
    private Boolean ctpsAssinadaCerto;
    private String dispensa;
    private String jornadaTrabalho;
    private String tempoAlmoco;
    private Boolean faziaHorasExtras;
    private String horarioHorasExtras;
    private String trabalhavaDomingosFeriados;
    private Boolean recebiaGratificacoes;
    private Boolean cumpriuAvisoPrevio;
    private Boolean temFeriasVencidasGozar;
    private Boolean recebeu13SalarioAnoAnterior;
    private Boolean fgtsDepositado;
    private Boolean recebeuGuiasSaqueFgts;
    private Boolean recebeuFormSeguroDesemprego;
    private Boolean inssRecolhido;
    private String pagaAlgumaVerba;
    private Integer saldoSalario;
    private String avisoPrevioIndenizado;
    private String _13SalarioProporcional;
    private String feriasVencidas;
    private String feriasProporcionais;
    private Integer umTercoConstitucionalFerias;
    private String comissoes;
    private String outrasInformacoes;
}
