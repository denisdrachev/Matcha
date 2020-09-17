package matcha.image.service;

import lombok.AllArgsConstructor;
import matcha.image.manipulation.ImageManipulator;
import matcha.image.model.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ImageService implements ImageInterface {

    private ImageManipulator imageManipulator;

    @Override
    public void saveImage(Image image) {
        imageManipulator.saveImage(image);
    }

    @Override
    public List<Image> getImagesByUserId(int userId) {
        return imageManipulator.getImagesByUserId(userId);
    }

    public void saveImages(List<Image> images) {
        images.forEach(this::saveImage);
    }
}
