package xyz.jayphen.openlan.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.zip.ZipFile;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class OpenlanClient implements ClientModInitializer {
	public static Process exposeProcess = null;
	@Override
	public void onInitializeClient() {
		var fileName = "tunnel_linux";
		var suffix = "_linux";
		if(System.getProperty("os.name").toLowerCase().contains("windows"))
		{
			fileName = "tunnel.exe";
			suffix = "";
		}
		else if(System.getProperty("os.name").toLowerCase().contains("darwin"))
		{
			fileName = "tunnel_macos";
			suffix = "_macos";
		}
		
		var directory = FabricLoader.getInstance().getGameDir().resolve("openlan/");
		if(Files.exists(directory.resolve("tunnel.exe"))) return;
		try {
			System.out.println("[openlan] Downloading tunnel...");
			URL                 website = new URL("https://github.com/Jaycadox/openlan-files/raw/main/tunnel" + suffix + ".zip");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			Files.createDirectory(directory);
			FileOutputStream    fos = new FileOutputStream(directory.resolve("tunnel" + suffix + ".zip").toAbsolutePath().toString());
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			
			var zip = new ZipFile(directory.resolve("tunnel" + suffix + ".zip").toAbsolutePath().toString());
			var is = new BufferedInputStream(zip.getInputStream(zip.getEntry(fileName)));
			int    currentByte;
			byte[] data = new byte[2048];
			fos = new FileOutputStream(directory.resolve(fileName).toAbsolutePath().toString());
			var dest = new BufferedOutputStream(fos, 2048);
			while ((currentByte = is.read(data, 0, 2048)) != -1) {
				dest.write(data, 0, currentByte);
			}
			dest.flush();
			dest.close();
			is.close();
			fos.close();
			zip.close();
			Files.deleteIfExists(directory.resolve("tunnel" + suffix + ".zip"));
			Files.writeString(directory.resolve(suffix.isEmpty() ? "bootstrap.bat" : "bootstrap.sh"), suffix.isEmpty() ? ("""
@echo off
set argument1=%1
""" + directory.resolve("tunnel.exe").toAbsolutePath() + " --port %argument1%") : "./tunnel" + suffix + " --port $1"
			);
		} catch(Exception ignored) {}

		
	}
}
