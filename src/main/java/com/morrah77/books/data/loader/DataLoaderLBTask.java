package com.morrah77.books.data.loader;

import liquibase.change.custom.CustomTaskChange;
import liquibase.change.custom.CustomTaskRollback;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DataLoaderLBTask implements CustomTaskChange, CustomTaskRollback {
    private String file;
    private String fieldSeparator = ";";
    private ResourceAccessor resourceAccessor;

    @Autowired
    DataSource dataSource;

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {
        return;
    }

    @Override
    public void setFileOpener(ResourceAccessor accessor) {
        this.resourceAccessor = accessor;
    }

    @Override
    public ValidationErrors validate(Database var1) {
        return null;
    }

    /**
     * Data loader
     *
     * Data example:
     * ASIN;Title;Auther;Genre
     * 2182718;Improve Your Bowls;Tony Allcock;Sports & Outdoors
     * 7210361;Asthma-Free Naturally: Everything You Need to Know About Taking Control of Your Asthma--Featuring the Buteyko Breathing Method Suitable for Adults and Children;Patrick G. McKeown;Health, Fitness & Dieting
     * 7262833;Seeing Red;Graham Poll;Biographies & Memoirs
     * 7444397;Dare to Dream: Life as One Direction;One Direction;Teen & Young Adult
     *
     * @param database
     * @throws CustomChangeException
     */

    @Override
    public void execute(Database database) throws CustomChangeException {
        JdbcConnection connection = (JdbcConnection) database.getConnection();
        try {
            Set<InputStream> streams = resourceAccessor.getResourcesAsStream(file);
            Iterator<InputStream> iterator = streams.iterator();
            while (iterator.hasNext()) {
                InputStream inputStream = iterator.next();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();
                if (line == null) {
                    continue;
                }
                String[] header = line.split(fieldSeparator);
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(fieldSeparator);
                    String rawSQL = "INSERT INTO " + database.escapeTableName(null, null, "book") + " VALUES (" +
                            fields[0] + "," +
                            "'" + database.escapeStringForDatabase(fields[1]) + "'," +
                            "(SELECT id FROM author WHERE title='" + database.escapeStringForDatabase(fields[2].trim().replaceAll("^\"|\"$","")) + "')," +
                            "(SELECT id FROM genre WHERE title='" + database.escapeStringForDatabase(fields[3].trim().replaceAll("^\"|\"$","")) + "')" +
                            ") ON CONFLICT DO NOTHING";
                    connection.prepareStatement(rawSQL).execute();
                }
            }

        } catch (Exception e) {
            throw new CustomChangeException(e);
        }

    }

    @Override
    public void rollback(Database database) {

    }
}
