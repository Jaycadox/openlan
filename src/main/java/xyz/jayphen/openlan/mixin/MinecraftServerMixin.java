package xyz.jayphen.openlan.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.jayphen.openlan.client.OpenlanClient;

import java.io.IOException;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@SuppressWarnings("deprecation")
	@Inject(method = "shutdown", at = @At("HEAD"))
	public void stop(CallbackInfo ci) {
		if(OpenlanClient.exposeProcess != null) {
			System.out.println("trying to destroy lan server");
			OpenlanClient.exposeProcess.destroy();
			Runtime rt = Runtime.getRuntime();
			try {
				if (System.getProperty("os.name").toLowerCase().contains("windows"))
						rt.exec("taskkill /IM tunnel.exe /F");
                else
					rt.exec("kill -9 tunnel");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
