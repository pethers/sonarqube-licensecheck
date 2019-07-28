package at.porscheinformatik.sonarqube.licensecheck;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

public class TestUtils
{
    private TestUtils()
    {
    }

    public static SensorContext createSensorContext(File folder) throws IOException
    {
        SensorContext context = mock(SensorContext.class);

        FileSystem fs = mock(FileSystem.class);
        when(context.fileSystem()).thenReturn(fs);
        when(fs.baseDir()).thenReturn(folder);

        FilePredicates predicates = mock(FilePredicates.class);
        FilePredicate pomXml = mock(FilePredicate.class);
        when(predicates.hasRelativePath("pom.xml")).thenReturn(pomXml);
        FilePredicate packageJson = mock(FilePredicate.class);
        when(predicates.hasRelativePath("package.json")).thenReturn(packageJson);
        when(fs.predicates()).thenReturn(predicates);

        InputFile pomXmlFile = mock(InputFile.class);
        when(fs.inputFile(pomXml)).thenReturn(pomXmlFile);
        InputFile packageJsonFile = mock(InputFile.class);
        when(packageJsonFile.inputStream()).then(__ -> new FileInputStream(new File(folder, "package.json")));
        when(fs.inputFile(packageJson)).thenReturn(packageJsonFile);

        return context;
    }

}
