package co.empathy.academy.search.service.loader;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileLoaderService {
    void createIndex();
    void loadMovies(MultipartFile basics, MultipartFile ratings,
                    MultipartFile akas, MultipartFile principals,
                    MultipartFile crew, MultipartFile episodes);
}
