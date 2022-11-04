package org.oddishwolf.api.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class ScriptsReader {

    public static String readScript(String script) {
        Path path = Path.of(PropertiesUtil.get("scripts.path"), script);
        String sql;
        try {
            sql = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Can't read file. Check if he exist");
        }

        return sql;
    }
}
