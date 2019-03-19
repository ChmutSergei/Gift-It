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

/**
 * The Upload controller class serves only for uploading files of type jpg by users.
 *
 * @author Sergei Chmut.
 */
@WebServlet(urlPatterns = "/upload")
public class UploadController extends HttpServlet {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * The constant TMPDIR.
     */
    private static final String TMPDIR = "java.io.tmpdir";
    /**
     * The constant JPG_TYPE.
     */
    private static final String JPG_TYPE = "jpg";
    /**
     * The constant NEW_PREFIX.
     */
    private static final String NEW_PREFIX = "new";
    /**
     * The constant MAX_SIZE_FILE.
     */
    private static final int MAX_SIZE_FILE = 65500;
    /**
     * The constant TARGET_WIDTH.
     */
    private static final int TARGET_WIDTH = 450;
    /**
     * The constant TARGET_HEIGHT.
     */
    private static final int TARGET_HEIGHT = 450;

    /**
     * Overridden doPost method processing requests sent by the POST method with file to upload data.
     * The method parses the request and, if there is a valid transferred file, saves it.
     * If the file is incorrect gives an error message.
     * It also calls the method to resize the file if necessary.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty(TMPDIR)));
        ServletFileUpload upload = new ServletFileUpload(factory);
        String uploadPath = request.getServletContext().getRealPath(DEFAULT_UPLOAD_PATH);
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        File storeFile = null;
        String fileName = null;
        String filePath = null;
        try {
            List<FileItem> formItems = upload.parseRequest(request);
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
            request.getSession().setAttribute(MESSAGE_PARAMETER_NAME,
                    "Error with upload file " + exception.getMessage());
        }
        if (JPG_TYPE.equals(FilenameUtils.getExtension(fileName))) {
            if (storeFile != null && storeFile.length() > MAX_SIZE_FILE) {
                filePath = saveNewFileWithCorrectSize(request, fileName, storeFile, uploadPath);
            }
            request.getSession().setAttribute(UPLOAD_FILENAME_ATTRIBUTE_NAME, fileName);
            request.getSession().setAttribute(UPLOAD_FILE_ATTRIBUTE_NAME, filePath);
        } else {
            logger.error("Only JPG files accepted");
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, "Only JPG files accepted");
        }
    }

    /**
     * Save new file with correct size string.
     *
     * @param request    the request object that is passed to the servlet
     * @param fileName   source file name for resizing
     * @param storeFile  the store file
     * @param uploadPath the path for upload file
     * @return the new file path for resizing file
     */
    private String saveNewFileWithCorrectSize(HttpServletRequest request, String fileName, File storeFile, String uploadPath) {
        String filePath = uploadPath + NEW_PREFIX + fileName;
        File newFile = new File(filePath);
        try (FileOutputStream stream = new FileOutputStream(newFile)) {
            byte[] resize = resize(storeFile);
            if (resize != null) {
                stream.write(resize);
            }
        } catch (IOException exception) {
            logger.error("Error with upload file ", exception);
            request.getSession().setAttribute(MESSAGE_PARAMETER_NAME,
                    "Error with upload file " + exception.getMessage());
        }
        return filePath;
    }

    /**
     * Resizing file with parameters TARGET_WIDTH, TARGET_HEIGHT.
     *
     * @param image the image file
     * @return the new byte [ ] of image file
     * @throws IOException if an input or output error is
     *                     detected when handles image
     */
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
