package org.oddishwolf.api.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Make sure that your properties changed for src/test/resources.
 * After you can comment @Disabled and test this isolated (!)
 */

@Disabled
@Tag("fast")
@Tag("util_scriptsReader")
public class ScriptsReaderTest {

    @Test
    void readScriptReturnCorrectNumberOfCharacters() {
        String script = ScriptsReader.readScript("test_scripts_reader_1.txt");
        assertThat(script).hasSize(935);
    }

    @Test
    void readScriptReturnZeroNumberOfCharacters() {
        String script = ScriptsReader.readScript("test_scripts_reader_2.txt");
        assertThat(script).hasSize(0);
    }
}
