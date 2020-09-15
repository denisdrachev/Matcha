package matcha.image.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.image.db.ImageDB;
import matcha.image.model.ImageModel;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageManipulator {

    private final ImageDB imageDB;

    public Integer insertImage(ImageModel image) {
        return imageDB.insertImage(image);
    }

    public ImageModel getImageById(String imageId) {
        return imageDB.getImageById(imageId);
    }

    public void dropImageById(String id) {
        imageDB.dropImageById(id);
    }
}
