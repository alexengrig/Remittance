package dev.alexengrig.remittance.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class SerializationUtil {

    private SerializationUtil() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

    public static <T extends Serializable> void serializeToFile(T object, String filename)
            throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(Paths.get(filename)))) {
            output.writeObject(object);
        }
    }

    public static <T extends Serializable> T deserializeFromFile(String filename)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(Paths.get(filename)))) {
            @SuppressWarnings("unchecked")
            T target = (T) input.readObject();
            return target;
        }
    }
}
