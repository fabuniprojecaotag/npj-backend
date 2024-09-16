package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelacaoEmpregaticia {
    private String dataAdmissao;
    private String dataSaida;
    private String funcaoExercida;
    private String valorSalarioCtps;
    private Boolean salarioAnotadoCtps;
    private String valorUltimaRemuneracao;
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
    private String saldoSalario;
    private String avisoPrevioIndenizado;
    private String _13SalarioProporcional;
    private String feriasVencidas;
    private String feriasProporcionais;
    private String umTercoConstitucionalFerias;
    private String comissoes;
    private String outrasInformacoes;
}
