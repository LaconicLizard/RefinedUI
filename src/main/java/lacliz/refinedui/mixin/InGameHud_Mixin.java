package lacliz.refinedui.mixin;

import lacliz.refinedui.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHud_Mixin {

    @Shadow @Final private ItemRenderer itemRenderer;
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V",
            at = @At("RETURN"))
    public void post_renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (Config.get().hotbarCounts && !stack.isEmpty()) {
            // adapted from ItemRenderer.renderGuiItemOverlay
            ItemRenderer ir = this.itemRenderer;
            TextRenderer tr = this.client.textRenderer;
            ClientPlayerEntity cpe = MinecraftClient.getInstance().player;
            if (cpe == null) return;
            final int yOffset = -16;

            MatrixStack matrixStack = new MatrixStack();
            String countLabel = String.valueOf(cpe.inventory.count(stack.getItem()));
            matrixStack.translate(0.0D, 0.0D, ir.zOffset + 200.0F);
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            tr.draw(countLabel, (float) (x + 19 - 2 - tr.getWidth(countLabel)),
                    (float) (y + 6 + 3 + yOffset), 16777215, true,
                    matrixStack.peek().getModel(), immediate, false, 0, 15728880);
            immediate.draw();
        }
    }

}
