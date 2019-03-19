package dbimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class CatalogParser {
    private static final Logger logger = LoggerFactory.getLogger(CatalogParser.class);

    public Stream<Path> listFiles(String catalogPath) throws IOException {
        logger.info("Parsing catalog(" + catalogPath + ")");
        return Files.list(Paths.get(catalogPath)).filter(Files::isRegularFile);
    }

    public void moveFile(Path filePath, String catalogToMovePath) {
        logger.info("Moving file(" + filePath + "to catalog(" + catalogToMovePath + ")");
        try {
            Files.move(filePath, Paths.get(catalogToMovePath, filePath.getFileName().toString()));
        } catch (IOException e) {
            logger.warn("Error during moving file(" + filePath +"): " + e);
        }
    }

    public static void main(String[] args) throws IOException {
        CatalogParser catalogParser = new CatalogParser();
        final Stream<Path> pathStream = catalogParser.listFiles("D:\\_actual");
        pathStream.forEach(path -> System.out.println(path.getFileName()));
        catalogParser.moveFile(Paths.get("D:\\tmp"), "D:\\_actual");
    }
}
