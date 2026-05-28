package app.senia.encuentralo.service;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.model.Resultado;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportService {

    public byte[] exportarResultadosCSV(List<Resultado> resultados) {
        StringWriter stringWriter = new StringWriter();

        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            String[] header = {"ID", "Nombre", "Tel\u00e9fono", "Direcci\u00f3n", "Num Reviews", "Valoraci\u00f3n", "Categor\u00edas", "Etiquetas", "URL"};
            csvWriter.writeNext(header);

            for (Resultado r : resultados) {
                List<String> nombresCategorias = new ArrayList<>();
                if (r.getCategorias() != null) {
                    for (Categoria c : r.getCategorias()) {
                        nombresCategorias.add(c.getNombre());
                    }
                }
                String categorias = String.join("; ", nombresCategorias);

                List<String> nombresEtiquetas = new ArrayList<>();
                if (r.getEtiquetas() != null) {
                    for (Etiqueta e : r.getEtiquetas()) {
                        nombresEtiquetas.add(e.getNombre());
                    }
                }
                String etiquetas = String.join("; ", nombresEtiquetas);

                String[] row = {
                        r.getId().toString(),
                        r.getNombre(),
                        r.getTelefono(),
                        r.getDireccion(),
                        r.getNumReviews().toString(),
                        String.valueOf(r.getValoracion()),
                        categorias,
                        etiquetas,
                        r.getUrl()
                };
                csvWriter.writeNext(row);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al generar CSV", e);
        }

        String csvContent = stringWriter.toString();
        byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);

        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] result = new byte[bom.length + csvBytes.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(csvBytes, 0, result, bom.length, csvBytes.length);

        return result;
    }
}
