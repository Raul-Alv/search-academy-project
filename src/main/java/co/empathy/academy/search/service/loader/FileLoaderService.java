package co.empathy.academy.search.service.loader;

import org.springframework.web.multipart.MultipartFile;

public interface FileLoaderService {
    void loadMovies(MultipartFile file);
}
