package de.nilsim.collapse;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WebFileHandleResolver implements FileHandleResolver {

	@Override
	public FileHandle resolve(String fileName) {
		return new WebFileHandle(fileName);
	}

	private static class WebFileHandle extends FileHandle {

		private String url;

		public WebFileHandle(String path) {
			super(path);
			url = path;
		}

		@Override
		public InputStream read() {
			try {
				return new URL(url).openStream();
			} catch (IOException e) {
				throw new GdxRuntimeException(e);
			}
		}
	}
}
