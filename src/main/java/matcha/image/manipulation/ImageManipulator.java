package matcha.image.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.image.db.ImageDB;
import matcha.image.model.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageManipulator {

    private final ImageDB imageDB;

    public void saveImage(Image image) {
        imageDB.updateImageById(image);
    }

    public Image getImageById(String imageId) {
        return imageDB.getImageById(imageId);
    }

    public void deleteImageById(String id) {
        imageDB.dropImageById(id);
    }

    public List<Image> getImagesByUserId(int userId) {
        return imageDB.getImagesByUserId(userId);
    }
}
