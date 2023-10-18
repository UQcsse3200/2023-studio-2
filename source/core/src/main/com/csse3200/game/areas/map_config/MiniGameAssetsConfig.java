package com.csse3200.game.areas.map_config;

import com.csse3200.game.services.ResourceService;

import java.util.Arrays;
import java.util.Objects;

public class MiniGameAssetsConfig {
    public String[] texturePaths = null;
    public String[] textureAtlasPaths = null;
    public String[] backgroundMusicPath = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MiniGameAssetsConfig that = (MiniGameAssetsConfig) o;

        // Placeholder methods, TBD
        if (!Arrays.equals(texturePaths, that.texturePaths)) return false;

        if (!Arrays.equals(textureAtlasPaths, that.textureAtlasPaths)) return false;

        return Objects.equals(backgroundMusicPath, that.backgroundMusicPath);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(texturePaths);
        result = 31 * result + Arrays.hashCode(textureAtlasPaths);
        result = 31 * result + (backgroundMusicPath != null ? backgroundMusicPath.hashCode() : 0);
        return result;
    }

    /**
     * Loads all assets contained within this config class
     * @param resourceService Resource service to be loaded into
     */
    public void load(ResourceService resourceService) {
        if (texturePaths != null)
            resourceService.loadTextures(texturePaths);
        if (textureAtlasPaths != null)
            resourceService.loadTextureAtlases(textureAtlasPaths);
        if (backgroundMusicPath != null)
            //resourceService.loadMusic(new String[] {backgroundMusicPath});
            resourceService.loadMusic(backgroundMusicPath);
    }

    /**
     * Unloads all assets contained within this config class
     * @param resourceService Resource service to be unloaded from
     */
    public void unload(ResourceService resourceService) {
        if (texturePaths != null)
            resourceService.unloadAssets(texturePaths);
        if (textureAtlasPaths != null)
            resourceService.unloadAssets(textureAtlasPaths);
        if (backgroundMusicPath != null)
            //resourceService.unloadAssets(new String[] {backgroundMusicPath});
            resourceService.unloadAssets(backgroundMusicPath);
    }
}
