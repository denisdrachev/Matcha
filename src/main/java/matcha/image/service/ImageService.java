package matcha.image.service;

import lombok.AllArgsConstructor;
import matcha.image.manipulation.ImageManipulator;
import matcha.image.model.ImageModel;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageService implements ImageInterface {

    private ImageManipulator imageManipulator;

    @Override
    public Integer insertImage(ImageModel image) {
        return imageManipulator.insertImage(image);
    }
}
