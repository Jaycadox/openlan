package xyz.jayphen.openlan.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.Clipboard;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.jayphen.openlan.client.OpenlanClient;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
	@Shadow @Final private MinecraftClient client;
	
	@Inject(method = "openToLan", at = @At("HEAD"))
	public void openToLanMixin(GameMode gameMode, boolean cheatsAllowed, int port, CallbackInfoReturnable<Boolean> cir) {
		var directory = FabricLoader.getInstance().getGameDir().resolve("openlan/");
		var ext = System.getProperty("os.name").toLowerCase().contains("windows") ? ".bat" : ".sh";
		try {
			if(Files.exists(directory.resolve("bootstrap" + ext))) {
				String[] params = { directory.resolve("bootstrap" + ext).toAbsolutePath().toString(), "" + port };
				Runtime runtime = Runtime.getRuntime();
				OpenlanClient.exposeProcess = runtime.exec(params);
				BufferedReader reader = new BufferedReader(new InputStreamReader(OpenlanClient.exposeProcess.getInputStream()));
				new Thread(() -> {
					try {
						while(OpenlanClient.exposeProcess.isAlive()) {
							String line = reader.readLine();
							if(line == null) continue;
							var toast = new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION, Text.literal("OpenLAN"), Text.literal(
									"Domain has been copied to clipboard"));
							client.getToastManager().add(toast);
							MinecraftClient.getInstance().keyboard.setClipboard(line);
							break;
						}
					} catch(Exception ignored) { }
				}).start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
