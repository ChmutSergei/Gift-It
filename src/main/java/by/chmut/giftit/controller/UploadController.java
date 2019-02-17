package by.chmut.giftit.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static by.chmut.giftit.constant.AttributeName.*;

@WebServlet(urlPatterns = "/upload")
public class UploadController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();
    private static final String TMPDIR = "java.io.tmpdir";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute(MESSAGE_PARAMETER_NAME, "");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty(TMPDIR)));
        ServletFileUpload upload = new ServletFileUpload(factory);
        String uploadPath = req.getServletContext().getRealPath("");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        File storeFile = null;
        try {
            List<FileItem> formItems = upload.parseRequest(req);
            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + fileName;
                        req.getSession().setAttribute(UPLOAD_FILENAME_ATTRIBUTE_NAME, fileName);
                        req.getSession().setAttribute(UPLOAD_FILE_ATTRIBUTE_NAME, filePath);
                        storeFile = new File(filePath);
                        item.write(storeFile);
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("Error with upload file ", exception);
            req.getSession().setAttribute(MESSAGE_PARAMETER_NAME, "Error with upload file " + exception.getMessage());
        }
        if (storeFile != null && storeFile.length() > 65500) {
            String filePath = uploadPath + "newSize.jpg";
            File newFile = new File(filePath);
            try (FileOutputStream stream = new FileOutputStream(newFile)) {
                byte[] resize = resize(storeFile);
                if (resize != null) {
                    stream.write(resize);
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] resize(File image) {
        try {
            BufferedImage originalImage = ImageIO.read(image);
            originalImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 153, 153);
            //To save with original ratio uncomment next line and comment the above.
//            originalImage= Scalr.resize(originalImage, 153, 128);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
