package xyz.jayphen.openlan.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(method = "getWindowTitle", at = @At("RETURN"), cancellable = true)
	public void getWindowTitleMixinOpenLan(CallbackInfoReturnable<String> cir) {
		if(cir.getReturnValue().endsWith("(LAN)")) {
			cir.setReturnValue(cir.getReturnValue() + " (OpenLAN)");
		}
	}
}
