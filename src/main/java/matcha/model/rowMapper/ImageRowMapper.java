package matcha.model.rowMapper;

import matcha.image.model.ImageModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageRowMapper implements RowMapper<ImageModel> {

    @Override
    public ImageModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        ImageModel imageElem = new ImageModel();
        imageElem.setId(rs.getInt("id"));
        imageElem.setIndex(rs.getInt("index"));
        imageElem.setSrc(rs.getString("src"));
        return imageElem;
    }
}