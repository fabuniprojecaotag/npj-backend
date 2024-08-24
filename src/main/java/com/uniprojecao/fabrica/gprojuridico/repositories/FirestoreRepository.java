package com.uniprojecao.fabrica.gprojuridico.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.*;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.AtendimentoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.ProcessoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Envolvido;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoFull;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.*;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.google.cloud.firestore.Query.Direction.DESCENDING;
import static com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository.DocumentSnapshotService.*;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.*;

@Repository
@Primary
public class FirestoreRepository {

    public static Firestore firestore;

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    public Firestore firestore() {
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setProjectId(projectId)
                .build();
        return options.getService();
    }

    @PostConstruct
    private void init() {
        var clazz = this.getClass().getSimpleName();
        if (clazz == FirestoreRepository.class.getSimpleName()) {
            firestore = firestore();
        }
    }

    public static Object insert(String collectionName, Object payload) throws Exception {
        Object data;
        String id;

        switch (collectionName) {
            case ASSISTIDOS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, Assistido.class);
                var assistido = (Assistido) data;
                id = assistido.getCpf();
                firestore.collection(collectionName).document(id).set(data).get();
                return assistido;
            case ATENDIMENTOS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, Atendimento.class);
                var atendimento = (Atendimento) data;
                id = atendimento.getId();
                firestore.collection(collectionName).document(id).set(data).get();
                return atendimento;
            case MEDIDAS_JURIDICAS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, MedidaJuridica.class);
                var medidaJuridica = (MedidaJuridica) data;
                id = medidaJuridica.getId();
                firestore.collection(collectionName).document(id).set(data).get();
                return medidaJuridica;
            case PROCESSOS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, Processo.class);
                var processo = (Processo) data;
                id = processo.getNumero();
                firestore.collection(collectionName).document(id).set(data).get();
                return processo;
            case USUARIOS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, Usuario.class);
                var usuario = (Usuario) data;
                id = usuario.getId();
                firestore.collection(collectionName).document(id).set(data).get();
                return usuario;
            default:
                throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        }
    }

    public static Map<String, Object> findAll(
            String collectionName,
            String startAfter,
            int pageSize,
            Filter filter,
            String returnType
    )
            throws Exception
    {
        CollectionReference collection = firestore.collection(collectionName);
        String[] fieldNames = getSpecificFieldNamesToReturnClassInstance(collectionName, returnType);

        Query query = (filter != null) ?
                collection.orderBy("__name__").where(filter).select(fieldNames).limit(pageSize) :
                collection.orderBy("__name__").select(fieldNames).limit(pageSize);

        if (startAfter != null) {
            DocumentSnapshot lastDoc = firestore.collection(collectionName).document(startAfter).get().get();
            query = query.startAfter(lastDoc);
        }

        ApiFuture<QuerySnapshot> future = query.get();

        List<Object> list = new ArrayList<>();

        for (QueryDocumentSnapshot snapshot : future.get()) {
            var document = convertSnapshot(collectionName, snapshot, returnType);
            list.add(document);
        }

        Object lastDoc = list.get(list.size() - 1);
        int collectionSize = future.get().size();

        return Map.of(
                "list", list,
                "lastDoc", lastDoc,
                "count", collectionSize);
    }

    public static Object findById(String collectionName, String id) throws Exception {
        DocumentReference document = firestore.collection(collectionName).document(id);
        DocumentSnapshot snapshot = document.get().get();
        if (!snapshot.exists()) return null;
        return convertSnapshot(collectionName, snapshot, null);
    }

    public static String findLastDocumentId(String collectionName) {
        try {
            var list = firestore
                    .collection(collectionName)
                    .orderBy(FieldPath.documentId(), DESCENDING)
                    .limit(1)
                    .get()
                    .get()
                    .getDocuments();
            return !list.isEmpty() ? list.get(0).getId() : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(String collectionName, Map<String, Object> data, String recordId, String classType) {
        Class<?> clazz;
        Map<String, Object> filteredData;
        Map<String, Object> processedData;

        switch (collectionName) {
            case ASSISTIDOS_COLLECTION:
                clazz = identifyChildClass(Assistido.class.getSimpleName(), classType);
                filteredData = filterValidKeys(data, clazz);
                processedData = processNestedKeysIntoOne(filteredData);
                firestore.collection(collectionName).document(recordId).update(processedData);
                break;
            case ATENDIMENTOS_COLLECTION:
                clazz = identifyChildClass(Atendimento.class.getSimpleName(), classType);
                filteredData = filterValidKeys(data, clazz);
                processedData = processNestedKeysIntoOne(filteredData);
                firestore.collection(collectionName).document(recordId).update(processedData);
                break;
            case MEDIDAS_JURIDICAS_COLLECTION:
                filteredData = filterValidKeys(data, MedidaJuridica.class);
                processedData = processNestedKeysIntoOne(filteredData);
                firestore.collection(collectionName).document(recordId).update(processedData);
                break;
            case PROCESSOS_COLLECTION:
                filteredData = filterValidKeys(data, Processo.class);
                processedData = processNestedKeysIntoOne(filteredData);
                firestore.collection(collectionName).document(recordId).update(processedData);
                break;
            case USUARIOS_COLLECTION:
                clazz = identifyChildClass(Usuario.class.getSimpleName(), classType);
                filteredData = filterValidKeys(data, clazz);
                processedData = processNestedKeysIntoOne(filteredData);
                firestore.collection(collectionName).document(recordId).update(processedData);
                break;
            default:
                throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        }
    }

    public static void delete(String collectionName, List<String> ids) {
        for (var id : ids) {
            firestore.collection(collectionName).document(id).delete();
        }
    }

    static class DocumentSnapshotService {
        static Object convertSnapshot(String collection, DocumentSnapshot snapshot, String returnType) {
            return switch (collection) {
                case ASSISTIDOS_COLLECTION -> {
                    if (returnType == "min") yield snapshotToAssistido(snapshot, true, false);
                    if (returnType == "autoComplete") yield snapshotToAssistido(snapshot, false, true);
                    yield snapshotToAssistido(snapshot, false, false);
                }
                case ATENDIMENTOS_COLLECTION -> {
                    if (returnType == "min") yield snapshotToAtendimento(snapshot, true, false, false);
                    if (returnType == "autoComplete") yield snapshotToAtendimento(snapshot, false, true, false);
                    if (returnType == "forAssistido") yield snapshotToAtendimento(snapshot, false, false, true);
                    yield snapshotToAtendimento(snapshot, false, false, false);
                }
                case MEDIDAS_JURIDICAS_COLLECTION -> snapshot.toObject(MedidaJuridica.class);
                case PROCESSOS_COLLECTION -> {
                    if (returnType == "forAssistido") yield snapshotToProcessoVinculado(snapshot);
                    yield snapshot.toObject(Processo.class);
                }
                case USUARIOS_COLLECTION -> {
                    if (returnType == "min") yield snapshotToUsuario(snapshot, true, false);
                    yield snapshotToUsuario(snapshot, false, false);
                }
                default -> throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
            };
        }

        public static Object snapshotToAssistido(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                                 Boolean returnAutocomplete) {

            if (snapshot == null) return null;

            if (returnMinDTO) {
                var object = snapshot.toObject(AssistidoMinDTO.class);
                object.setCpf(snapshot.getId());
                return object;
            }

            if (returnAutocomplete) {
                return snapshot.toObject(AssistidoAutocomplete.class);
            }

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

        public static Object snapshotToAtendimento(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                                   Boolean returnAutocomplete,
                                                   Boolean returnAtendimentosDeAssistidoDTO) {
            if (returnMinDTO) {
                var assistidoMap = convertUsingReflection(snapshot.get("envolvidos.assistido"), false);

                // Defini o atributo "assistido"
                var assistidoEnvolvido =
                        new Envolvido(
                                (String) assistidoMap.get("id"),
                                (String) assistidoMap.get("nome"));

                var date = snapshot.getCreateTime().toDate();

                LocalDateTime localDateTime = date.toInstant()
                        .atZone(ZoneId.of("America/Sao_Paulo"))
                        .toLocalDateTime();

                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                String formattedDateTime = localDateTime.format(formatter);


                return new AtendimentoMinDTO(
                        snapshot.getId(),
                        (String) snapshot.get("area"),
                        (String) snapshot.get("status"),
                        assistidoEnvolvido,
                        formattedDateTime
                );
            }

            if (returnAutocomplete) {
                return new AtendimentoAutocomplete(
                        snapshot.getId()
                );
            }

            if (returnAtendimentosDeAssistidoDTO) {
                var assistidoMap = convertUsingReflection(snapshot.get("envolvidos.assistido"), false);
                var estagiarioMap = convertUsingReflection(snapshot.get("envolvidos.estagiario"), false);

                // Defini o atributo "assistido"
                var assistidoEnvolvido =
                        new Envolvido(
                                (String) assistidoMap.get("id"),
                                (String) assistidoMap.get("nome"));

                // Defini o atributo "estagiario"
                var estagiarioEnvolvido =
                        new Envolvido(
                                (String) estagiarioMap.get("id"),
                                (String) estagiarioMap.get("nome"));

                return new AtendimentoVinculadoDTO(
                        snapshot.getId(),
                        (String) snapshot.get("area"),
                        (String) snapshot.get("status"),
                        assistidoEnvolvido,
                        estagiarioEnvolvido,
                        (String) snapshot.get("instante")
                );
            }

            if (snapshot == null) return null;

            String area = snapshot.getString("area");
            if (Objects.equals(area, "Trabalhista")) {
                return snapshot.toObject(AtendimentoTrabalhista.class);
            } else if (Objects.equals(area, "Civil") || Objects.equals(area, "Criminal") || Objects.equals(area, "Família")) {
                return snapshot.toObject(AtendimentoCivil.class);
            }
            return null;
        }

        public static Object snapshotToUsuario(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                               Boolean returnAutocomplete) {
            if (snapshot == null) return null;

            boolean dadosEstagiario = snapshot.contains("matricula");

            if (returnMinDTO) {
                if (dadosEstagiario) {
                    var object = snapshot.toObject(EstagiarioMinDTO.class);
                    object.setId(snapshot.getId());
                    return object;
                } else {
                    var object = snapshot.toObject(UsuarioMinDTO.class);
                    object.setId(snapshot.getId());
                    return object;
                }
            }

            if (returnAutocomplete) {
                return snapshot.toObject(UsuarioAutocomplete.class);
            }

            return (dadosEstagiario) ? snapshot.toObject(Estagiario.class) : snapshot.toObject(Usuario.class);
        }

        public static ProcessoVinculadoDTO snapshotToProcessoVinculado(DocumentSnapshot snapshot) {
            return new ProcessoVinculadoDTO(
                    snapshot.getId(),
                    (String) snapshot.get("vara"),
                    (String) snapshot.get("status")
            );
        }
    }
}
