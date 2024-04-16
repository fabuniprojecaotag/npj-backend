package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoFull;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoCivilDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoFullDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoTrabalhistaDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.ResourceNotFoundException;

import java.util.Map;

public class AssistidoUtils {
    public static Object snapshotToAssistido(DocumentSnapshot snapshot, Boolean returnMinDTO) {

        if (returnMinDTO) return snapshot.toObject(AssistidoMinDTO.class);

        Boolean dadosFCivil =
                snapshot.contains("naturalidade") &&
                        snapshot.contains("dataNascimento") &&
                        snapshot.contains("dependentes");
        Boolean dadosFTrabalhista =
                snapshot.contains("ctps") &&
                        snapshot.contains("pis") &&
                        snapshot.contains("empregadoAtualmente");

        if (dadosFCivil && dadosFTrabalhista) return snapshot.toObject(AssistidoFull.class);
        else if (dadosFCivil) return snapshot.toObject(AssistidoCivil.class);
        else if (dadosFTrabalhista) return snapshot.toObject(AssistidoTrabalhista.class);
        else throw new RuntimeException("Error to convert snapshot into Assistido.");
    }

    public static AssistidoDTO AssistidoToDTO(Assistido result) {
        if (result instanceof AssistidoFull assistidoFull) return new AssistidoFullDTO(assistidoFull);
        else if (result instanceof AssistidoCivil assistidoCivil) return new AssistidoCivilDTO(assistidoCivil);
        else if (result instanceof AssistidoTrabalhista assistidoTrabalhista) return new AssistidoTrabalhistaDTO(assistidoTrabalhista);
        else throw new ResourceNotFoundException();
    }

    public static Assistido dtoToAssistido(AssistidoDTO dto) {
        Assistido assistido = getAssistido(dto);

        if (dto instanceof AssistidoCivilDTO assistidoCivilDTO) {
            AssistidoCivil ac = (AssistidoCivil) assistido;
            ac.setNaturalidade(assistidoCivilDTO.getNaturalidade());
            ac.setDataNascimento(assistidoCivilDTO.getDataNascimento());
            ac.setDependentes(assistidoCivilDTO.getDependentes());

            return ac;

        } else if (dto instanceof AssistidoTrabalhistaDTO assistidoTrabalhistaDTO) {
            AssistidoTrabalhista at = (AssistidoTrabalhista) assistido;

            at.setCtps(new AssistidoTrabalhista.Ctps(
                    assistidoTrabalhistaDTO.getCtps().getNumero(),
                    assistidoTrabalhistaDTO.getCtps().getSerie(),
                    assistidoTrabalhistaDTO.getCtps().getUf()));
            at.setPis(assistidoTrabalhistaDTO.getPis());
            at.setEmpregadoAtualmente(assistidoTrabalhistaDTO.getEmpregadoAtualmente());

            return at;
        }
        return null;
    }

    private static Assistido getAssistido(AssistidoDTO dto) {
        Assistido assistido = new AssistidoCivil();

        assistido.setNome(dto.getNome());
        assistido.setRg(dto.getRg());
        assistido.setCpf(dto.getCpf());
        assistido.setNacionalidade(dto.getNacionalidade());
        assistido.setEscolaridade(dto.getEscolaridade());
        assistido.setEstadoCivil(dto.getEstadoCivil());
        assistido.setProfissao(dto.getProfissao());
        assistido.setTelefone(dto.getTelefone());
        assistido.setEmail(dto.getEmail());
        assistido.setFiliacao(new Assistido.Filiacao(
                dto.getFiliacao().getMae(),
                dto.getFiliacao().getPai()));
        assistido.setRemuneracao(dto.getRemuneracao());
        assistido.setEndereco(
                Map.of(
                        "residencial", dto.getEndereco().get("residencial"),
                        "comercial", dto.getEndereco().get("comercial")
                )
        );
        return assistido;
    }
}
