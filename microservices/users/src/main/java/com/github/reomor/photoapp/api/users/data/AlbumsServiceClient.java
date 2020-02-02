package com.github.reomor.photoapp.api.users.data;

import com.github.reomor.photoapp.api.users.config.EndpointsConst;
import com.github.reomor.photoapp.api.users.ui.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = EndpointsConst.ALBUMS_MS, fallback = AlbumsFallback.class)
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albums")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);
}
@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

    @Override
    public List<AlbumResponseModel> getAlbums(String id) {
        return new ArrayList<>();
    }
}
