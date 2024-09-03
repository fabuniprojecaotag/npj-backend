package com.uniprojecao.fabrica.gprojuridico.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.EstagiarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.AtendimentoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.ProcessoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoFull;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Envolvido;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.cloud.firestore.Query.Direction.DESCENDING;
import static com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl.DocumentSnapshotService.convertSnapshot;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.*;

@Repository
@Primary
@NoArgsConstructor
public class FirestoreRepositoryImpl implements FirestoreRepository {

    private static Firestore firestore;

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    private String collectionName;

    public FirestoreRepositoryImpl(String collectionName) {
        this.collectionName = collectionName;
    }

    @PostConstruct
    private void init() {
        firestore = firestore();
    }

    private Firestore firestore() {
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setProjectId(projectId)
                .build();
        return options.getService();
    }

    @Override
    public Object insert(String customId, Object data) throws Exception {
        firestore.collection(collectionName).document(customId).set(data).get();
        return data;
    }

    @Override
    public Map<String, Object> findAll(String startAfter, int pageSize, Filter filter, String returnType)
            throws Exception {

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
        int totalSize = collection.get().get().size();

        List<Object> docPage = new ArrayList<>();

        for (QueryDocumentSnapshot snapshot : future.get()) {
            var document = convertSnapshot(collectionName, snapshot, returnType);
            docPage.add(document);
        }

        Object firstDoc = "Not available";
        Object lastDoc = "Not available";

        if (docPage.size() != 0) {
            firstDoc = docPage.get(0);
            lastDoc = docPage.get(docPage.size() - 1);
        }

        int collectionSize = future.get().size();

        return Map.of(
                "list", docPage,
                "firstDoc", firstDoc,
                "lastDoc", lastDoc,
                "pageSize", collectionSize,
                "totalSize", totalSize);
    }

    @Override
    public Object findById(String id) throws Exception {
        DocumentReference document = firestore.collection(collectionName).document(id);
        DocumentSnapshot snapshot = document.get().get();
        if (!snapshot.exists()) return null;
        return convertSnapshot(collectionName, snapshot, null);
    }

    public String findLastDocumentId() throws Exception {
        List<QueryDocumentSnapshot> list = firestore
                .collection(collectionName)
                .orderBy(FieldPath.documentId(), DESCENDING)
                .limit(1)
                .get()
                .get()
                .getDocuments();
        return !list.isEmpty() ? list.get(0).getId() : null;
    }

    @Override
    public void update(String recordId, Map<String, Object> data, Class<?> clazz) {
        Map<String, Object> processedData = getProcessedAndValidDataToInsertAsMap(data, clazz);
        firestore.collection(collectionName).document(recordId).update(processedData);
    }

    @Override
    public void delete(List<String> ids) {
        for (var id : ids) {
            firestore.collection(collectionName).document(id).delete();
        }
    }

    static class DocumentSnapshotService {
        static Object convertSnapshot(String collection, DocumentSnapshot snapshot, String returnType) {
            boolean isReturnTypeNull = returnType == null;
            boolean isReturnTypeEqualsToMin = false;
            boolean isReturnTypeEqualsToAutoComplete = false;
            boolean isReturnTypeEqualsToForAssistido = false;

            if (!isReturnTypeNull) {
                isReturnTypeEqualsToMin = returnType.equals("min");
                isReturnTypeEqualsToAutoComplete = returnType.equals("autoComplete");
                isReturnTypeEqualsToForAssistido = returnType.equals("forAssistido");
            }

            return switch (collection) {
                case ASSISTIDOS_COLLECTION -> {
                    if (isReturnTypeEqualsToMin) yield snapshotToAssistido(snapshot, true, false);
                    if (isReturnTypeEqualsToAutoComplete) yield snapshotToAssistido(snapshot, false, true);
                    yield snapshotToAssistido(snapshot, false, false);
                }
                case ATENDIMENTOS_COLLECTION -> {
                    if (isReturnTypeEqualsToMin) yield snapshotToAtendimento(snapshot, true, false, false);
                    if (isReturnTypeEqualsToAutoComplete) yield snapshotToAtendimento(snapshot, false, true, false);
                    if (isReturnTypeEqualsToForAssistido) yield snapshotToAtendimento(snapshot, false, false, true);
                    yield snapshotToAtendimento(snapshot, false, false, false);
                }
                case MEDIDAS_JURIDICAS_COLLECTION -> snapshot.toObject(MedidaJuridica.class);
                case PROCESSOS_COLLECTION -> {
                    if (isReturnTypeEqualsToForAssistido) yield snapshotToProcessoVinculado(snapshot);
                    yield snapshot.toObject(Processo.class);
                }
                case USUARIOS_COLLECTION -> {
                    if (isReturnTypeEqualsToMin) yield snapshotToUsuario(snapshot, true, false);
                    if (isReturnTypeEqualsToAutoComplete) yield snapshotToUsuario(snapshot, false, true);
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
