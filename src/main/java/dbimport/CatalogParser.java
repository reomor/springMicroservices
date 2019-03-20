package dbimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

@Component
public class CatalogParser {
    private static final Logger logger = LoggerFactory.getLogger(CatalogParser.class);

    @Value("${catalogs.processed}")
    private String processedCatalog;

    public Stream<Path> listFiles(String catalogPath) throws IOException {
        logger.info("Parsing catalog(" + catalogPath + ")");
        return Files.list(Paths.get(catalogPath)).filter(Files::isRegularFile);
    }

    public void moveFile(Path filePath) {
        logger.info("Moving file(" + filePath + " to catalog(" + processedCatalog + ")");
        try {
            Path path = Paths.get(processedCatalog);
            Files.move(filePath, Paths.get(processedCatalog, filePath.getFileName().toString()));
        } catch (IOException e) {
            logger.warn("Error during moving file(" + filePath +"): " + e);
        }
    }

    public static void main(String[] args) throws IOException {
        CatalogParser catalogParser = new CatalogParser();
        final Stream<Path> pathStream = catalogParser.listFiles("D:\\_actual");
        pathStream.forEach(path -> System.out.println(path.getFileName()));
        catalogParser.moveFile(Paths.get("D:\\tmp"));
    }
}
