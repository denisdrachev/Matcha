package matcha.image.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Drop;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.exception.context.image.LoadImageException;
import matcha.exception.db.image.*;
import matcha.image.model.Image;
import matcha.image.model.ImageModel;
import matcha.model.rowMapper.ImageRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageDB {

    private final JdbcTemplate jdbcTemplate;

    public Integer getImageCountById(int imageId) {
        log.info("Get image count by id: {}", imageId);
        try {
            Integer imagesCount = jdbcTemplate.queryForObject(Select.selectImagesCountById,
                    Integer.class, imageId);
            log.info("Get image count by id {} result: {}", imageId, imagesCount);
            return imagesCount;
        } catch (Exception e) {
            log.warn("Exception. getImageCountById: {}", e.getMessage());
            throw new GetImageCountByIdDBException();
        }
    }

    public Image getImageById(String imageId) {
        log.info("Get image by id: {}", imageId);
        try {
            Image image = jdbcTemplate.queryForObject(Select.selectImageById, new ImageRowMapper(), imageId);
            log.info("Get image by id {} result: {}", imageId, image);
            return image;
        } catch (Exception e) {
            log.warn("Exception. getImageById: {}", e.getMessage());
            throw new GetImageByIdDBException();
        }
    }

    public Integer insertImage(ImageModel image) {
        log.info("Insert image: {}", image);
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(Insert.insertImage, new String[]{"id"});
                    ps.setInt(1, image.getIndex());
                    ps.setString(2, image.getSrc());
                    return ps;
                }
            }, keyHolder);
            log.info("Insert image result: {}", keyHolder.getKey());
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            log.warn("Exception. insertImage: {}", e.getMessage());
            throw new InsertImageDBException();
        }
    }

    public void updateImageById(Image image) {
        log.info("Update image by id: {}", image);
        try {
            int update = jdbcTemplate.update(Update.updateImageById,
                    image.getIndex(), image.getSrc(), image.isAvatar(), image.getId());
            log.info("Update image by id result: {}", update);
        } catch (Exception e) {
            log.warn("Exception. updateImageById: {}", e.getMessage());
            throw new UpdateImageByIdDBException();
        }
    }

    public void dropImageById(String id) {
        try {
            log.info("Drop image by id: ".concat(id));
            int drop = jdbcTemplate.update(Drop.deleteImageById, id);
            log.info("Drop image by id result: ".concat(String.valueOf(drop)));
        } catch (Exception e) {
            log.warn("Exception. insertImage: {}", e.getMessage());
            throw new DropImageByIdDBException();
        }
    }

    public List<Image> getImagesByUserId(int userId) {
        log.info("Get images by user id: {}", userId);
        try {
            List<Image> images = jdbcTemplate.query(Select.selectImageById, new ImageRowMapper(), userId);
            log.info("Get images by id result: {}", images);
            return images;
        } catch (Exception e) {
            log.warn("Exception. getImageByUserId: {}", e.getMessage());
            throw new LoadImageException();
        }
    }
}
