package mmm.coffee.metacode.common.toml;

import com.fasterxml.jackson.databind.ObjectMapper;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.toml.io.PackageLayoutReader;
import org.apache.commons.lang3.arch.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class PackageLayoutToPackageDataDictionaryMapperTest {

    private static final String SAMPLE_FILE = "classpath:test-package-layout.json";

    PackageLayoutReader readerUnderTest = new PackageLayoutReader(new ObjectMapper(), new DefaultResourceLoader());

    PackageLayout packageLayout;

    PackageLayoutToPackageDataDictionaryMapper mapperUnderTest = new PackageLayoutToPackageDataDictionaryMapper();


    @BeforeEach
    public void setUp() throws IOException {
        packageLayout = readerUnderTest.readLayout(SAMPLE_FILE);
    }

    @Test
    void shouldMapToDictionary() {
        PackageDataDictionary dictionary = mapperUnderTest.map(packageLayout);

        assertThat(dictionary).isNotNull();

        assertThat(dictionary.packageName("Controller")).isNotEmpty();
        assertThat(dictionary.packageName(Archetype.Controller)).isNotEmpty();

        Archetype[] values = Archetype.values();
        List<String> lines = new ArrayList<>();
        for (Archetype v : values) {
            String pkgName = dictionary.packageName(v);
            lines.add(String.format("class: %-30s, package: %s", v.toString(), pkgName));
        }
        // Sort the lines for easier readibility
        lines.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        lines.forEach(l -> System.out.println(l));


    }



}
