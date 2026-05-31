package app.senia.encuentralo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.EtiquetaRepository;
import app.senia.encuentralo.repository.ResultadoRepository;
import app.senia.encuentralo.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EtiquetaServiceTest {

    @Mock
    private EtiquetaRepository etiquetaRepo;

    @Mock
    private UsuarioRepository usuarioRepo;

    @Mock
    private ResultadoRepository resultadoRepo;

    @InjectMocks
    private EtiquetaService etiquetaService;

    @Captor
    private ArgumentCaptor<Resultado> resultadoCaptor;

    @Test
    void obtenerEtiquetasUsuario_devuelveEtiquetas() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        List<Etiqueta> etiquetas = List.of(new Etiqueta("tag1", usuario), new Etiqueta("tag2", usuario));
        when(etiquetaRepo.findByUsuarioId(1)).thenReturn(etiquetas);

        List<Etiqueta> resultado = etiquetaService.obtenerEtiquetasUsuario(1);

        assertThat(resultado).hasSize(2);
    }

    @Test
    void guardarEtiqueta_creaYGuarda() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(etiquetaRepo.save(any(Etiqueta.class))).thenAnswer(i -> i.getArgument(0));

        Etiqueta etiqueta = etiquetaService.guardarEtiqueta(1, "nueva");

        assertThat(etiqueta.getNombre()).isEqualTo("nueva");
        assertThat(etiqueta.getUsuario()).isEqualTo(usuario);
        verify(etiquetaRepo).save(any(Etiqueta.class));
    }

    @Test
    void guardarEtiqueta_usuarioNoExiste_lanzaExcepcion() {
        when(usuarioRepo.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> etiquetaService.guardarEtiqueta(999, "test"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void borrarEtiqueta_eliminaDeResultadosYRepo() {
        Resultado resultado = new Resultado();
        Etiqueta etiqueta = new Etiqueta("test", null);
        etiqueta.setId(1);
        resultado.setEtiquetas(new ArrayList<>(List.of(etiqueta)));

        when(resultadoRepo.findByEtiquetasId(1)).thenReturn(List.of(resultado));

        etiquetaService.borrarEtiqueta(1);

        assertThat(resultado.getEtiquetas()).isEmpty();
        verify(resultadoRepo).save(resultado);
        verify(etiquetaRepo).deleteById(1);
    }

    @Test
    void etiquetarResultado_asignaEtiqueta() {
        Resultado resultado = new Resultado();
        resultado.setId(1);
        resultado.setEtiquetas(new ArrayList<>());
        Etiqueta etiqueta = new Etiqueta("test", null);
        etiqueta.setId(1);

        when(resultadoRepo.findById(1)).thenReturn(Optional.of(resultado));
        when(etiquetaRepo.findById(1)).thenReturn(Optional.of(etiqueta));

        etiquetaService.etiquetarResultado(1, 1);

        assertThat(resultado.getEtiquetas()).hasSize(1);
        assertThat(resultado.getEtiquetas().get(0).getNombre()).isEqualTo("test");
        verify(resultadoRepo).save(resultado);
    }

    @Test
    void etiquetarResultado_sinEtiquetasPrevias_inicializaLista() {
        Resultado resultado = new Resultado();
        resultado.setId(1);
        resultado.setEtiquetas(null);
        Etiqueta etiqueta = new Etiqueta("test", null);
        etiqueta.setId(1);

        when(resultadoRepo.findById(1)).thenReturn(Optional.of(resultado));
        when(etiquetaRepo.findById(1)).thenReturn(Optional.of(etiqueta));

        etiquetaService.etiquetarResultado(1, 1);

        assertThat(resultado.getEtiquetas()).hasSize(1);
    }

    @Test
    void etiquetarResultado_etiquetaYaAsignada_noDuplica() {
        Etiqueta etiqueta = new Etiqueta("test", null);
        etiqueta.setId(1);
        Resultado resultado = new Resultado();
        resultado.setId(1);
        resultado.setEtiquetas(new ArrayList<>(List.of(etiqueta)));

        when(resultadoRepo.findById(1)).thenReturn(Optional.of(resultado));
        when(etiquetaRepo.findById(1)).thenReturn(Optional.of(etiqueta));

        etiquetaService.etiquetarResultado(1, 1);

        assertThat(resultado.getEtiquetas()).hasSize(1);
        verify(resultadoRepo, never()).save(resultado);
    }

    @Test
    void desetiquetarResultado_eliminaEtiqueta() {
        Etiqueta etiqueta = new Etiqueta("test", null);
        etiqueta.setId(1);
        Resultado resultado = new Resultado();
        resultado.setId(1);
        resultado.setEtiquetas(new ArrayList<>(List.of(etiqueta)));

        when(resultadoRepo.findById(1)).thenReturn(Optional.of(resultado));

        etiquetaService.desetiquetarResultado(1, 1);

        assertThat(resultado.getEtiquetas()).isEmpty();
        verify(resultadoRepo).save(resultado);
    }

    @Test
    void desetiquetarResultado_sinEtiquetas_noFalla() {
        Resultado resultado = new Resultado();
        resultado.setId(1);
        resultado.setEtiquetas(null);

        when(resultadoRepo.findById(1)).thenReturn(Optional.of(resultado));

        etiquetaService.desetiquetarResultado(1, 1);

        verify(resultadoRepo, never()).save(resultado);
    }
}
