package matcha.event.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.event.model.Event;
import matcha.exception.db.InsertEventDBException;
import matcha.exception.db.LoadEventsException;
import matcha.model.rowMapper.EventRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventDB {

    private final JdbcTemplate jdbcTemplate;

    //    public Integer getImageCountById(int imageId) {
//        log.info("Get image count by id: {}", imageId);
//        try {
//            Integer imagesCount = jdbcTemplate.queryForObject(Select.selectImagesCountById,
//                    Integer.class, imageId);
//            log.info("Get image count by id {} result: {}", imageId, imagesCount);
//            return imagesCount;
//        } catch (Exception e) {
//            log.warn("Exception. getImageCountById: {}", e.getMessage());
//            throw new GetImageCountByIdDBException();
//        }
//    }
//
//    public Image getImageById(String imageId) {
//        log.info("Get image by id: {}", imageId);
//        try {
//            Image image = jdbcTemplate.queryForObject(Select.selectImageById, new ImageRowMapper(), imageId);
//            log.info("Get image by id {} result: {}", imageId, image);
//            return image;
//        } catch (Exception e) {
//            log.warn("Exception. getImageById: {}", e.getMessage());
//            throw new GetImageByIdDBException();
//        }
//    }
//
    public Integer insertEvent(Event event) {
        log.info("Insert event: {}", event);
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(Insert.insertEvent, new String[]{"id"});
                    ps.setString(1, event.getType());
                    ps.setString(2, event.getLogin());
                    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    ps.setBoolean(4, event.isActive());
                    ps.setString(5, event.getData());
                    return ps;
                }
            }, keyHolder);
            log.info("Insert event result: {}", keyHolder.getKey());
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            log.warn("Exception. insertEvent: {}", e.getMessage());
            throw new InsertEventDBException();
        }
    }

    //
//    public void updateImageById(Image image) {
//        log.info("Update image by id: {}", image);
//        try {
//            int update = jdbcTemplate.update(Update.updateImageById, image.getSrc(), image.isAvatar(), image.getId());
//            log.info("Update image by id result: {}", update);
//        } catch (Exception e) {
//            log.warn("Exception. updateImageById: {}", e.getMessage());
//            throw new UpdateImageByIdDBException();
//        }
//    }
//
//    public void dropImageById(String id) {
//        try {
//            log.info("Drop image by id: ".concat(id));
//            int drop = jdbcTemplate.update(Drop.deleteImageById, id);
//            log.info("Drop image by id result: ".concat(String.valueOf(drop)));
//        } catch (Exception e) {
//            log.warn("Exception. insertImage: {}", e.getMessage());
//            throw new DropImageByIdDBException();
//        }
//    }
//
//    public List<Image> getImagesByProfileId(int profileId) {
//        log.info("Get images by profile id: {}", profileId);
//        try {
//            List<Image> images = jdbcTemplate.query(Select.selectImageByProfileId, new ImageRowMapper(), profileId);
//            log.info("Get images by id profile: {}", images);
//            return images;
//        } catch (Exception e) {
//            log.warn("Exception. getImagesByProfileId: {}", e.getMessage());
//            throw new LoadImageException();
//        }
//    }
//
    public List<Event> getAllEvents() {
        log.info("Get all events");
        try {
            List<Event> events = jdbcTemplate.query(Select.selectEvents, new EventRowMapper());
            log.info("Get all events count: {}", events.size());
            return events;
        } catch (Exception e) {
            log.warn("Exception. getAllEvents: {}", e.getMessage());
            throw new LoadEventsException();
        }
    }
}
