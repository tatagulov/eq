package io.github.tatagulov.eq.generator;

import io.github.tatagulov.eq.metadata.api.DataBase;
import io.github.tatagulov.eq.metadata.metadata.DatabaseExtractor;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

@Mojo( name = "generate" ,defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class Generator extends AbstractMojo {

    @Parameter( property = "userName")
    public String userName;

    @Parameter( property = "password")
    public String password;

    @Parameter( property = "url")
    public String url;

    @Parameter( property = "driver")
    public String driver;

    @Parameter( property = "targetFolder")
    public String targetFolder;

    @Parameter( property = "packageName")
    public String packageName;

    @Parameter( property = "dbName")
    public String dbName;

    @Parameter(property = "schemaPattern")
    public String schemaPattern;

    @Parameter(property = "extractorClassName")
    public String extractorClassName;


    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Class.forName(driver);
            DataBase dataBase;
            Connection connection = DriverManager.getConnection(url,userName,password);
            try {
                Class<?> extractorClass = Class.forName(extractorClassName);
                DatabaseExtractor databaseExtractor = (DatabaseExtractor) extractorClass.newInstance();
                dataBase = databaseExtractor.extract(connection,dbName,schemaPattern);
            } finally {
                connection.close();
            }

            FileUtils.deleteDirectory(new File(targetFolder));
            new DataBaseGenerator(dataBase,packageName,targetFolder);

        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException("generated error",e);
        }
    }
}
