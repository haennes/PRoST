package de.unipassau.fim.fsinfo.kdv;

import static org.mockito.Mockito.when;

import de.unipassau.fim.fsinfo.kdv.data.dao.ShopItem;
import de.unipassau.fim.fsinfo.kdv.service.FileStorageService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class FileStorageServiceTest {

  @Test
  void saveItemPictureNonPng(@TempDir Path path) throws IOException {
    ShopItem item = new ShopItem("test", "testCategory", "Test Item", 420.69d);
    MultipartFile mockFile = Mockito.mock(MultipartFile.class);
    when(mockFile.getContentType()).thenReturn("hallo Welt");

    FileStorageService service = new FileStorageService(path.toString(), true);
    File savedImage = new File(path + "/items/test.png");

    Assertions.assertFalse(service.saveItemPicture(item, mockFile));
    Assertions.assertFalse(savedImage.exists());
  }

  @Test
  void saveItemPicture(@TempDir Path path) throws IOException {
    ShopItem item = new ShopItem("test", "testCategory", "Test Item", 420.69d);
    MockMultipartFile multipartFile = new MockMultipartFile(
        "do-not-show", // request parameter name
        "unimportant.png", // original file name
        "image/png", // content type
        new byte[0] // file content
    );
    File expectedFile = new File(path + "/items/test.png");

    FileStorageService service = new FileStorageService(path.toString(), true);

    Assertions.assertTrue(service.saveItemPicture(item, multipartFile));
    Assertions.assertTrue(expectedFile.exists());
  }

  @Test
  void getItemPicture(@TempDir Path path) throws IOException, InterruptedException {
    ShopItem item = new ShopItem("test", "testCategory", "Test Item", 420.69d);
    MockMultipartFile multipartFile = new MockMultipartFile(
        "do-not-show", // request parameter name
        "unimportant.png", // original file name
        "image/png", // content type
        new byte[0] // file content
    );
    File expectedFile = new File(path + "/items/test.png");

    FileStorageService service = new FileStorageService(path.toString(), true);

    Assertions.assertTrue(service.saveItemPicture(item, multipartFile));

    Assertions.assertTrue(service.getItemPicture(item).isPresent());
    Assertions.assertEquals(service.getItemPicture(item).get().getAbsolutePath(),
        expectedFile.getAbsolutePath());
  }

  @Test
  void getItemPictureMissing(@TempDir Path path) throws IOException {
    ShopItem item = new ShopItem("test", "testCategory", "Test Item", 420.69d);
    FileStorageService service = new FileStorageService(path.toString(), true);

    Optional<File> picture = service.getItemPicture(item);

    Assertions.assertFalse(picture.isPresent());
  }
}