package deokar.kaustubh.pawsrescuecenter.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/images")
public class ImageController {

    @GetMapping(value="/{imagename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("imagename") String imageName) throws IOException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/images/"+imageName);
        return resourceAsStream.readAllBytes();
    }

}
