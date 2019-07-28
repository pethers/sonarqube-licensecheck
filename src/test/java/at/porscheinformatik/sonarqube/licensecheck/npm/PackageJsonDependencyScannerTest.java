package at.porscheinformatik.sonarqube.licensecheck.npm;

import static at.porscheinformatik.sonarqube.licensecheck.TestUtils.createSensorContext;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import at.porscheinformatik.sonarqube.licensecheck.Dependency;
import at.porscheinformatik.sonarqube.licensecheck.interfaces.Scanner;

public class PackageJsonDependencyScannerTest
{
    private static final File TEST_RESOURCES = new File("src/test/resources");

    @Test
    public void testHappyPath() throws IOException
    {
        Scanner scanner = new PackageJsonDependencyScanner(false);

        Set<Dependency> dependencies = scanner.scan(createSensorContext(TEST_RESOURCES));

        assertThat(dependencies, hasSize(2));
        assertThat(dependencies, containsInAnyOrder(
            new Dependency("angular", "1.5.0", "MIT"),
            new Dependency("arangojs", "5.6.0", "Apache-2.0")));
    }

    @Test
    public void testTransitive() throws IOException
    {
        Scanner scanner = new PackageJsonDependencyScanner(true);

        Set<Dependency> dependencies = scanner.scan(createSensorContext(TEST_RESOURCES));

        assertThat(dependencies, hasSize(4));
        assertThat(dependencies, containsInAnyOrder(
            new Dependency("angular", "1.5.0", "MIT"),
            new Dependency("arangojs", "5.6.0", "Apache-2.0"),
            new Dependency("linkedlist", "1.0.1", "LGPLv3"),
            new Dependency("retry", "0.10.1", "MIT")));
    }

    @Test
    public void testNoPackageJson() throws IOException
    {
        Scanner scanner = new PackageJsonDependencyScanner(false);

        Set<Dependency> dependencies = scanner.scan(createSensorContext(new File("src")));

        assertThat(dependencies, hasSize(0));
    }

    @Test
    public void testNoNodeModules() throws IOException
    {
        Scanner scanner = new PackageJsonDependencyScanner(false);

        Set<Dependency> dependencies =
            scanner.scan(createSensorContext(new File(TEST_RESOURCES, "node_modules/arangojs")));

        assertThat(dependencies, hasSize(0));
    }
}
