package de.nilsim.collapse;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.backends.gwt.GwtFileHandle;
import com.badlogic.gdx.backends.gwt.preloader.AssetDownloader;
import com.badlogic.gdx.backends.gwt.preloader.AssetFilter;
import com.badlogic.gdx.files.FileHandle;
import com.google.gwt.dom.client.ImageElement;

public class WebFileHandleResolver implements FileHandleResolver {
	private AssetDownloader downloader = new AssetDownloader();

	@Override
	public FileHandle resolve(String fileName) {
		return new WebFileHandle(fileName);
	}

	private class WebFileHandle extends GwtFileHandle {

		private String url;

		public WebFileHandle(String path) {
			super(path);
			url = path;
			preloader.images.put(url, preloader.images.values().next()); // Placeholder because we have to wait for the assetdownloader
			downloader.load(path, AssetFilter.AssetType.Image, "image/jpg", new AssetDownloader.AssetLoaderListener<Object>() {
				@Override
				public void onProgress(double amount) {

				}

				@Override
				public void onFailure() {

				}

				@Override
				public void onSuccess(Object result) {
					preloader.images.put(url, (ImageElement) result);
				}
			});
		}

		@Override
		public String path() {
			return url;
		}
	}
}
