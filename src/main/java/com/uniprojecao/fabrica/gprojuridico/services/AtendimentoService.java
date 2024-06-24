package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.Autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.dto.atendimento.AtendimentoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ManualMapper.toDto;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ManualMapper.toEntity;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class AtendimentoService extends BaseService {

    AtendimentoRepository repository = new AtendimentoRepository();
    private static final String collectionName = ATENDIMENTOS_COLLECTION;

    public AtendimentoService() {
        super(collectionName);
    }

    private String defineId(AtendimentoDTO dto) {
        DocumentSnapshot doc = repository.findLast(); // Obtém o último documento
        String id = (doc != null) ? doc.getId() : null; // Armazena o id
        String newId = (id != null) ? incrementId(id) : "ATE00001"; // Incrementa o id
        dto.setId(newId);
        return newId;
    }

    private String incrementId(String id) {
        String numbers = id.substring(3); // numbers = "nnnnn" of {"ATE" + "nnnnn"}
        int increment = Integer.parseInt(numbers) + 1;

        Matcher matcher = Pattern.compile("0").matcher(numbers);
        var remainingZeros = new StringBuilder();

        while (matcher.find()) {
            remainingZeros.append("0");
        }

        return "ATE" + remainingZeros + increment; // e.g. "ATE00092", which is equivalent to "ATE" + "000" + "92"
    }

    public AtendimentoDTO insert(AtendimentoDTO dto) {
        String customId = defineId(dto);
        BaseRepository.save(collectionName, customId, toEntity(dto));
        return dto;
    }

    public List<AtendimentoMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public List<AtendimentoAutocomplete> findAllMin(String limit, String field, String filter, String value) {
        return repository.findAllMin(parseInt(limit), initFilter(field, filter, value));
    }

    public AtendimentoDTO findById(String id) {
        var atendimento = repository.findById(id);
        return (atendimento != null) ? toDto(atendimento) : null;
    }
}
