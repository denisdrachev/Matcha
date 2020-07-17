package matcha.db;

import lombok.AllArgsConstructor;
import matcha.db.crud.Drop;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.model.Image;
import matcha.model.ImageElem;
import matcha.model.Profile;
import matcha.properties.ConfigProperties;
import matcha.properties.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration
@AllArgsConstructor
public class SpringJdbcConfig {

    JdbcTemplate jdbcTemplate;
    ConfigProperties properties;

    @Bean
    public void createAllTables() {

        Drop.getAll().stream().forEach(s -> jdbcTemplate.execute(s));

        Properties.getAllTablesPath().stream().forEach(s -> createTableBySql(s));

        jdbcTemplate.update(Insert.insertImage, 0, "ABCD");
        jdbcTemplate.update(Insert.insertUser, "loginnnn", "password".getBytes(), null, "Artur", "Kamnev", "fermer@gmail.com", 1, 0, new Date(), "salt_test".getBytes(), null);
        jdbcTemplate.update(Insert.insertProfile, 22, 1, 0, "Simple fermer", "fermer", null,  1);
        jdbcTemplate.update(Insert.insertLocation, 1, 55.6634545, 37.5102081, Calendar.getInstance().getTime());
        jdbcTemplate.update(Insert.insertRaiting, 7, 1);
        jdbcTemplate.update(Insert.insertBlacklist, 1, 1);
        jdbcTemplate.update(Insert.insertImageLikeEvent, 1, 1, 1, 1);

        jdbcTemplate.query(Select.selectImage, new BeanPropertyRowMapper(ImageElem.class)).forEach(System.out::println);
        jdbcTemplate.query(Select.selectProfile, new BeanPropertyRowMapper(Profile.class)).forEach(System.out::println);
    }

    @PreDestroy
    public void preDestroy() {
//        try {
//            System.out.println("CONNECTION!");
//            dataSource.getConnection().close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    private void createTableBySql(String pathToSql) {
        try {
            String content = Files.readString(Paths.get(pathToSql));
            jdbcTemplate.execute(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

