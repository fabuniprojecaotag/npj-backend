package com.uniprojecao.fabrica.gprojuridico;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.UsuarioRepository;
import jakarta.annotation.Nullable;
import org.mockito.MockedStatic;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.data.UsuarioData.seedWithUsuario;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class Utils {
    // save() method is tested with seedDatabase() method
    public static boolean seedDatabase(boolean databaseEmpty) {
        if (databaseEmpty) {
            var underTest = (mock(UsuarioRepository.class));
            List<Usuario> list = seedWithUsuario();

            when(underTest.save(anyString(), any())).thenCallRealMethod();

            for (var item : list) {
                underTest.save(item.getEmail(), item);
            }

            return false;
        }
        return true;
    }

    // deleteAll() method is tested with clearDatabase() method
    public static boolean clearDatabase(@Nullable QueryFilter queryFilter) {
        var underTest = (mock(UsuarioRepository.class));
        int limit = 20;

        try (MockedStatic<BaseRepository> baseRepository = mockStatic(BaseRepository.class)) {
            baseRepository.when(() -> underTest.deleteAll(limit, queryFilter)).thenCallRealMethod();
        }

        doCallRealMethod().when(underTest).deleteAll(limit, queryFilter);
        when(underTest.findAll(limit, queryFilter)).thenCallRealMethod();

        underTest.deleteAll(limit, queryFilter);
        var list = underTest.findAll(limit, queryFilter);

        return list.isEmpty();
    }
}
