package by.chmut.giftit.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
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
    private static final String JPG_TYPE = "jpg";
    private static final String NEW_PREFIX = "new";
    private static final int MAX_SIZE_FILE = 65500;
    private static final int TARGET_WIDTH = 450;
    private static final int TARGET_HEIGHT = 450;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute(MESSAGE_PARAMETER_NAME, "");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty(TMPDIR)));
        ServletFileUpload upload = new ServletFileUpload(factory);
        String uploadPath = req.getServletContext().getRealPath("/upload/");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        File storeFile = null;
        String fileName = null;
        String filePath = null;
        try {
            List<FileItem> formItems = upload.parseRequest(req);
            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        fileName = new File(item.getName()).getName();
                        filePath = uploadPath + fileName;
                        storeFile = new File(filePath);
                        item.write(storeFile);
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("Error with upload file ", exception);
            req.getSession().setAttribute(MESSAGE_PARAMETER_NAME,
                    "Error with upload file " + exception.getMessage());
        }
        if (!JPG_TYPE.equals(FilenameUtils.getExtension(fileName))) {
            logger.error("Only JPG files accepted");
            req.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, "Only JPG files accepted");
        }
        if (storeFile != null && storeFile.length() > MAX_SIZE_FILE) {
            filePath = uploadPath + NEW_PREFIX + fileName;
            File newFile = new File(filePath);
            try (FileOutputStream stream = new FileOutputStream(newFile)) {
                byte[] resize = resize(storeFile);
                if (resize != null) {
                    stream.write(resize);
                }
            } catch (IOException exception) {
                logger.error("Error with upload file ", exception);
                req.getSession().setAttribute(MESSAGE_PARAMETER_NAME,
                        "Error with upload file " + exception.getMessage());
            }
        }
        req.getSession().setAttribute(UPLOAD_FILENAME_ATTRIBUTE_NAME, fileName);
        req.getSession().setAttribute(UPLOAD_FILE_ATTRIBUTE_NAME, filePath);
    }

    private byte[] resize(File image) throws IOException {
        BufferedImage originalImage = ImageIO.read(image);
        originalImage = Scalr.resize(originalImage, TARGET_WIDTH, TARGET_HEIGHT);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(originalImage, JPG_TYPE, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return imageInByte;
    }
}
