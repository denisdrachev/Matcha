package matcha.image.service;

import matcha.image.model.ImageModel;

interface ImageInterface {

    Integer insertImage(ImageModel image) throws Exception;
}
