package org.codeontology.build.maven;

import org.apache.commons.lang3.StringUtils;
import org.codeontology.build.DependenciesLoader;
import org.codeontology.build.Project;
import org.codeontology.extraction.project.ProjectVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MavenProject extends Project {

    private org.apache.maven.project.MavenProject mavenProject;
    private File buildFile;
    private MavenLoader loader;
    private boolean setUp;

    public MavenProject(File project) {
        super(project);
        setUp();
    }

    private void setUp() {
        if (!setUp) {
            mavenProject = new org.apache.maven.project.MavenProject();
            buildFile = new File(getPath() + "/pom.xml");
            mavenProject.setFile(buildFile);
            loader = new MavenLoader(this);
            setUp = true;
        }
    }

    @Override
    protected Collection<Project> findSubProjects() {
        setUp();
        try {
            Set<File> modules = new HashSet<>();
            File pom = new File(mavenProject.getBasedir() + "/pom.xml");
            Scanner scanner = new Scanner(pom);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String match = StringUtils.substringBetween(line, "<module>", "</module>");

                if (!(match == null) && !match.equals("")) {
                    modules.add(new File(mavenProject.getBasedir() + "/" + match));
                    System.out.println("Module: " + mavenProject.getBasedir() + "/" + match);
                }
            }

            mavenProject.getModules().forEach(module -> {
                        System.out.println("module: " + mavenProject.getBasedir() + "/" + module);
                        modules.add(new File(mavenProject.getBasedir() + "/" + module));
                    }
            );

            return initSubProjects(modules);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DependenciesLoader<? extends MavenProject> getLoader() {
        return loader;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public File getBuildFile() {
        return buildFile;
    }

    @Override
    public File getProjectDirectory() {
        if (!setUp) {
            return super.getProjectDirectory();
        }
        return mavenProject.getBasedir();
    }
}
