package lacliz.refinedui.mixin;

import lacliz.refinedui.RefinedUI;
import lacliz.refinedui.Util;
import lacliz.refinedui.internal.RUIConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(InGameHud.class)
public class InGameHud_Mixin {

    private static final int COLOR = 16777215, LIGHT = 15728880;

    @Shadow @Final private ItemRenderer itemRenderer;
    @Shadow @Final private MinecraftClient client;

    // cache of the total counts of each item in player inventory
    // recalculated once per frame
    // dev note: chose to recalculate once per frame because attempting to intercept all inventory-altering
    //      events is a delicate solution that may be broken by any mods accessing things in unorthodox ways.
    //  And when the counts become desynchronized from the actual inventory this functionality becomes useless.
    //  Lastly, iterating the player's inventory once per frame is rather low overhead anyways, even if it leaves
    //      a bad taste in my mouth.
    private Map<Item, Integer> refinedui_invCountsCache = new HashMap<>();
    private final List<Vec3i> refinedui_hotbarItemCoords = new ArrayList<>();

    @Inject(method = "renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V",
            at = @At("RETURN"))
    public void post_renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (!stack.isEmpty()) {
            refinedui_hotbarItemCoords.add(new Vec3i(x, y, refinedui_invCountsCache.get(stack.getItem())));
        }
    }

    @Inject(method = "renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V",
            at = @At("HEAD"))
    public void pre_renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        ClientPlayerEntity cpe = MinecraftClient.getInstance().player;
        if (cpe == null) return;
        // cache inventory counts
        refinedui_invCountsCache = Util.itemCounts(cpe.inventory);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V",
            at = @At("RETURN"))
    public void post_render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        int x, y, count;
        for (Vec3i v : refinedui_hotbarItemCoords) {
            x = v.getX();
            y = v.getY();
            count = v.getZ();
            RUIConfig config = RefinedUI.getConfig();
            if (config.hotbarCounts && count != 0) {
                // adapted from ItemRenderer.renderGuiItemOverlay
                ItemRenderer ir = this.itemRenderer;
                TextRenderer tr = this.client.textRenderer;

                // vanilla stacks go to 64, giving a max inventory contents of 2368, excluding armor and crafting window
                //  thats 4 digits, which we scale the display for
                String countLabel = String.valueOf(count);
                if (countLabel.length() > 4) countLabel = "9999";
                int w = tr.getWidth(countLabel);
                float rx = x + 19 - 2 - w + config.hotbarCountsXOffset,  // coordinates where label will be drawn
                        ry = y + 6 + 3 - 16 + config.hotbarCountsYOffset;
                float scale = config.hotbarCountsScale;

                MatrixStack matrixStack = new MatrixStack();
                matrixStack.translate(0.0D, 0.0D, ir.zOffset + 200.0F);
                // scale down to keep large numbers from overlapping
                Util.scaleAbout(matrixStack, rx + w, ry, 0d, scale, scale, 1f);
                VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
                tr.draw(countLabel, rx, ry, COLOR, true,
                        matrixStack.peek().getModel(), immediate, false, 0, LIGHT);
                immediate.draw();
            }
        }
        refinedui_hotbarItemCoords.clear();
    }

}
