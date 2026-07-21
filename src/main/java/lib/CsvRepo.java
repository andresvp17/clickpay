package lib;

import java.nio.file.Path;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Optional;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.util.List;
import java.util.ArrayList;

public abstract class CsvRepo<T, ID> {
    protected final Path filePath;
    protected final Class<T> cls;
    protected List<T> cache;

    public CsvRepo(Path filePath, Class<T> cls) {
        this.filePath = filePath;
        this.cls = cls;
        this.cache = loadFromFile();
    }

    public Optional<T> findByID(ID id) {
        return cache.stream()
                .filter(entity -> getID(entity).equals(id))
                .findFirst();
    }

    public List<T> findAll() {
        return new ArrayList<>(cache);
    }

    public void save(T entity) {
        if (findByID(getID(entity)).isPresent()) {
            throw new IllegalArgumentException("Entidad con id " + getID(entity) + " ya existe");
        }

        cache.add(entity);
        flushToFile();
    }

    public void update(T entity) {
        ID id = getID(entity);
        for (int i = 0; i < cache.size(); i++) {
            if (getID(cache.get(i)).equals(id)) {
                cache.set(i, entity);
                flushToFile();
                return;
            }
        }
    }

    public void delete(ID id) {
        boolean removed = cache.removeIf(entity -> getID(entity).equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Entidad con la ID " + id + " no fue encontrada");
        }

        flushToFile();
    }

    protected abstract ID getID(T entity);

    protected List<T> loadFromFile() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>(cache);
        }

        try (Reader reader = Files.newBufferedReader(filePath)) {
            return new CsvToBeanBuilder<T>(reader)
                    .withType(cls)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new RuntimeException("Fallo cargando el CSV desde " + filePath, e);
        }
    }

    protected void flushToFile() {
        try {
            Files.createDirectories(filePath.getParent());
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                        .withOrderedResults(true)
                        .build();

                beanToCsv.write(cache);
            }

        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new RuntimeException("Fallo en escribir el CSV en" + filePath, e);
        }
    }
}