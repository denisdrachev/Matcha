package matcha.model.rowMapper;

import matcha.model.ImageElem;
import matcha.model.Profile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageRowMapper implements RowMapper<ImageElem> {

    @Override
    public ImageElem mapRow(ResultSet rs, int rowNum) throws SQLException {
        ImageElem imageElem = new ImageElem();
        imageElem.setId(rs.getInt("id"));
        imageElem.setIndex(rs.getInt("index"));
        imageElem.setSrc(rs.getString("src"));
        return imageElem;
    }
}