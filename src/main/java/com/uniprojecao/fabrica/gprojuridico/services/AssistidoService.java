package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoCivilDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoTrabalhistaDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.AssistidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssistidoService {

    @Autowired
    AssistidoRepository repository;

    public Map<String, Object> insert(AssistidoDTO data) {
        var id = data.getCpf();

        var result = repository.saveWithCustomId(id, passDtoToEntity(data));

        return Map.of(
                "object", result,
                "id", id
        );
    }

    public List<AssistidoMinDTO> findAll(String limit, String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                        !(filter.isEmpty()) &&
                        !(value.isEmpty());

        QueryFilter queryFilter = (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;

        return repository.findAll(Integer.parseInt(limit), queryFilter);
    }

    public Object findById(String id) {
        return repository.findById(id);
    }

    public void update(String id, Map<String, Object> data) {
        repository.update(id, data);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                        !(filter.isEmpty()) &&
                        !(value.isEmpty());

        QueryFilter queryFilter = (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;
        repository.deleteAll(Integer.parseInt(limit), queryFilter);
    }

    private static Assistido passDtoToEntity(AssistidoDTO dto) {
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
        assistido.setEndereco(new Assistido.Endereco(
                dto.getEndereco().getLogradouro(),
                dto.getEndereco().getBairro(),
                dto.getEndereco().getNumero(),
                dto.getEndereco().getComplemento(),
                dto.getEndereco().getCep(),
                dto.getEndereco().getCidade()));
        return assistido;
    }
}

