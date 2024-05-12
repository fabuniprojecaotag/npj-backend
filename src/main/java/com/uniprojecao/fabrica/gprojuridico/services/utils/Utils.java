package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoFull;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.*;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.EnderecoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.ProcessoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoCivilDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoFullDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoTrabalhistaDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.atendimento.*;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.EstagiarioDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.cloud.firestore.Filter.*;

public class Utils {
    public static <T> Map<String, Object> convertUsingReflection(T object, Boolean useSuperClass) {
        if (object instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked") // Suppress warning as it's safe due to the instanceof check
            Map<String, Object> castedMap = (Map<String, Object>) object;
            return castedMap;
        }

        Map<String, Object> map = new HashMap<>();
        Class<?> t = object.getClass();
        Field[] fields;

        if (useSuperClass) {
            fields = getAllFields(t);
        } else {
            fields = t.getDeclaredFields();
        }

        for (Field field: fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return map;
    }

    // Helper method to get all fields, including those from superclasses
    private static Field[] getAllFields(Class<?> clazz) {
        Map<String, Field> fieldsMap = new HashMap<>();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!fieldsMap.containsKey(field.getName())) {
                    fieldsMap.put(field.getName(), field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fieldsMap.values().toArray(new Field[0]);
    }

    public static Filter filter(QueryFilter filter) {
        var filterType = filter.filterType();
        var field = filter.field();
        var value = filter.value();

        return switch (filterType) {
            case EQUAL -> equalTo(field, value);
            case GREATER_THAN -> greaterThan(field, value);
            case GREATER_THAN_OR_EQUAL -> greaterThanOrEqualTo(field, value);
            case LESS_THAN -> lessThan(field, value);
            case LESS_THAN_OR_EQUAL -> lessThanOrEqualTo(field, value);
            case NOT_EQUAL -> notEqualTo(field, value);
        };
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean validateText(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static QueryFilter initFilter(String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                        !(filter.isEmpty()) &&
                        !(value.isEmpty());

        return (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;
    }

    public static URI createUri(String id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

    public static class ModelMapper {
        private static final org.modelmapper.ModelMapper modelMapper = new org.modelmapper.ModelMapper();

        public static AssistidoDTO toDto(Assistido entity) {
            if (entity instanceof AssistidoCivil) {
                return modelMapper.map(entity, AssistidoCivilDTO.class);
            } else if (entity instanceof AssistidoTrabalhista) {
                return modelMapper.map(entity, AssistidoTrabalhistaDTO.class);
            }
            return modelMapper.map(entity, AssistidoFullDTO.class);
        }

        public static Assistido toEntity(AssistidoDTO dto) {
            if (dto instanceof AssistidoCivilDTO) {
                return modelMapper.map(dto, AssistidoCivil.class);
            } else if (dto instanceof AssistidoTrabalhistaDTO) {
                return modelMapper.map(dto, AssistidoTrabalhista.class);
            }
            return modelMapper.map(dto, AssistidoFull.class);
        }

        public static ProcessoDTO toDto(Processo entity) {
            return modelMapper.map(entity, ProcessoDTO.class);
        }

        public static Processo toEntity(ProcessoDTO dto) {
            return modelMapper.map(dto, Processo.class);
        }

        public static UsuarioDTO toDto(Usuario entity) {
            if (entity instanceof Estagiario) {
                return modelMapper.map(entity, EstagiarioDTO.class);
            }
            return modelMapper.map(entity, UsuarioDTO.class);
        }

        public static Usuario toEntity(UsuarioDTO dto) {
            if (dto instanceof EstagiarioDTO) {
                return modelMapper.map(dto, Estagiario.class);
            }
            return modelMapper.map(dto, Usuario.class);
        }
    }

    public static class ManualMapper {
        // TODO: necessário entender mapeamento com herança de classe base abstrata no ModelMapper
        public static AtendimentoDTO toDto(Atendimento entity) {
            var o = convertUsingReflection(entity, false);

            if (entity.getArea() == "Trabalhista") {
                var at = new AtendimentoTrabalhistaDTO();
                var f = (FichaTrabalhista) o.get("ficha");
                var historicoDTO = entity.getHistorico().stream().map(
                        e -> new AtendimentoDTO.EntradaHistoricoDTO(e.getId(), e.getTitulo(), e.getDescricao(), e.getInstante(),
                                new AtendimentoDTO.EntradaHistoricoDTO.UsuarioMinDTO(
                                        e.getCriadoPor().getEmail(),
                                        e.getCriadoPor().getNome(),
                                        e.getCriadoPor().getRole()))
                ).toList();
                at.setId(entity.getId());
                at.setStatus(entity.getStatus());
                at.setArea(entity.getArea());
                at.setInstante(entity.getInstante());
                at.setHistorico(historicoDTO);
                at.setEnvolvidos(entity.getEnvolvidos());
                var testemunhasDTO = f.getTestemunhas()
                        .stream()
                        .map(t -> new FichaDTO.TestemunhaDTO(t.getNome(), t.getQualificacao(), new EnderecoDTO(
                                t.getEndereco().getLogradouro(),
                                t.getEndereco().getBairro(),
                                t.getEndereco().getNumero(),
                                t.getEndereco().getComplemento(),
                                t.getEndereco().getCep(),
                                t.getEndereco().getCidade()
                        )))
                        .toList();
                at.setFicha(new FichaTrabalhistaDTO(
                        f.getAssinatura(),
                        f.getDadosSensiveis(),
                        f.getMedidaJuridica(),
                        testemunhasDTO,
                        f.getReclamado(),
                        f.getRelacaoEmpregaticia(),
                        f.getDocumentosDepositadosNpj(),
                        f.getOutrasInformacoes()));

                return at;
            } else {
                var ac = new AtendimentoCivilDTO();
                var f = (FichaCivil) o.get("ficha");
                var historicoDTO = entity.getHistorico().stream().map(
                        e -> new AtendimentoDTO.EntradaHistoricoDTO(e.getId(), e.getTitulo(), e.getDescricao(), e.getInstante(),
                                new AtendimentoDTO.EntradaHistoricoDTO.UsuarioMinDTO(
                                        e.getCriadoPor().getEmail(),
                                        e.getCriadoPor().getNome(),
                                        e.getCriadoPor().getRole()))
                ).toList();
                ac.setId(entity.getId());
                ac.setStatus(entity.getStatus());
                ac.setArea(entity.getArea());
                ac.setInstante(entity.getInstante());
                ac.setHistorico(historicoDTO);
                ac.setEnvolvidos(entity.getEnvolvidos());

                var testemunhasDTO = f.getTestemunhas()
                        .stream()
                        .map(t -> new FichaDTO.TestemunhaDTO(t.getNome(), t.getQualificacao(), new EnderecoDTO(
                                t.getEndereco().getLogradouro(),
                                t.getEndereco().getBairro(),
                                t.getEndereco().getNumero(),
                                t.getEndereco().getComplemento(),
                                t.getEndereco().getCep(),
                                t.getEndereco().getCidade()
                        )))
                        .toList();
                ac.setFicha(new FichaCivilDTO(
                        f.getAssinatura(),
                        f.getDadosSensiveis(),
                        testemunhasDTO,
                        f.getParteContraria(),
                        f.getMedidaJuridica()
                ));

                return ac;
            }
        }

        // TODO: necessário entender mapeamento com herança de classe base abstrata no ModelMapper
        public static Atendimento toEntity(AtendimentoDTO dto) {
            var o = convertUsingReflection(dto, false);

            if (dto.getArea() == "Trabalhista") {
                var ficha = (FichaTrabalhistaDTO) o.get("ficha");
                return new AtendimentoTrabalhista(
                        dto.getId(),
                        dto.getStatus(),
                        dto.getArea(),
                        dto.getInstante(),
                        dto.getHistorico()
                                .stream()
                                .map(e -> new Atendimento.EntradaHistorico(
                                                e.getId(),
                                                e.getTitulo(),
                                                e.getDescricao(),
                                                e.getInstante(),
                                                new Atendimento.EntradaHistorico.UsuarioMin(
                                                        e.getCriadoPor().getEmail(),
                                                        e.getCriadoPor().getNome(),
                                                        e.getCriadoPor().getRole()
                                                )
                                        )
                                ).toList(),
                        dto.getEnvolvidos(),
                        new FichaTrabalhista(
                                dto.getFicha().getAssinatura(),
                                dto.getFicha().getDadosSensiveis(),
                                dto.getFicha().getMedidaJuridica(),
                                dto.getFicha().getTestemunhas()
                                        .stream()
                                        .map(t -> new Ficha.Testemunha(
                                                        t.getNome(),
                                                        t.getQualificacao(),
                                                        new Endereco(
                                                                t.getEndereco().getLogradouro(),
                                                                t.getEndereco().getBairro(),
                                                                t.getEndereco().getNumero(),
                                                                t.getEndereco().getComplemento(),
                                                                t.getEndereco().getCep(),
                                                                t.getEndereco().getCidade()
                                                        )
                                                )
                                        ).toList(),
                                ficha.getReclamado(),
                                ficha.getRelacaoEmpregaticia(),
                                ficha.getDocumentosDepositadosNpj(),
                                ficha.getOutrasInformacoes()
                        )
                );
            }
            else {
                var ficha = (FichaCivilDTO) o.get("ficha");
                return new AtendimentoCivil(
                        dto.getId(),
                        dto.getStatus(),
                        dto.getArea(),
                        dto.getInstante(),
                        dto.getHistorico()
                                .stream()
                                .map(e -> new Atendimento.EntradaHistorico(
                                                e.getId(),
                                                e.getTitulo(),
                                                e.getDescricao(),
                                                e.getInstante(),
                                                new Atendimento.EntradaHistorico.UsuarioMin(
                                                        e.getCriadoPor().getEmail(),
                                                        e.getCriadoPor().getNome(),
                                                        e.getCriadoPor().getRole()
                                                )
                                        )
                                ).toList(),
                        dto.getEnvolvidos(),
                        new FichaCivil(
                                dto.getFicha().getAssinatura(),
                                dto.getFicha().getDadosSensiveis(),
                                dto.getFicha().getTestemunhas()
                                        .stream()
                                        .map(t -> new Ficha.Testemunha(
                                                        t.getNome(),
                                                        t.getQualificacao(),
                                                        new Endereco(
                                                                t.getEndereco().getLogradouro(),
                                                                t.getEndereco().getBairro(),
                                                                t.getEndereco().getNumero(),
                                                                t.getEndereco().getComplemento(),
                                                                t.getEndereco().getCep(),
                                                                t.getEndereco().getCidade()
                                                        )
                                                )
                                        ).toList(),
                                ficha.getParteContraria(),
                                ficha.getMedidaJuridica())
                        );
            }
        }
    }
}
