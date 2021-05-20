package lacliz.refinedui.mixin;

import lacliz.refinedui.Util;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

import static lacliz.refinedui.RefinedUI.getConfig;

@Mixin(InGameHud.class)
public class InGameHud_Mixin {

    private static final int COLOR = 16777215, LIGHT = 15728880;

    @Shadow @Final private ItemRenderer itemRenderer;
    @Shadow @Final private MinecraftClient client;

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    // cache of the total counts of each item in player inventory
    // recalculated once per frame
    // dev note: chose to recalculate once per frame because attempting to intercept all inventory-altering
    //      events is a delicate solution that may be broken by any mods accessing things in unorthodox ways.
    //  And when the counts become desynchronized from the actual inventory this functionality becomes useless.
    //  Lastly, iterating the player's inventory once per frame is rather low overhead anyways, even if it leaves
    //      a bad taste in my mouth.
    private Map<Item, Integer> refinedui_invCountsCache = new HashMap<>();

    @Inject(method = "renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V",
            at = @At("RETURN"))
    public void post_renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (getConfig().hotbarCounts && !stack.isEmpty()) {
            // adapted from ItemRenderer.renderGuiItemOverlay
            ItemRenderer ir = this.itemRenderer;
            TextRenderer tr = this.client.textRenderer;
            final int yOffset = -16;

            int ct = refinedui_invCountsCache.get(stack.getItem());
            // vanilla stacks go to 64, giving a max inventory contents of 2368, excluding armor and crafting window
            //  thats 4 digits, which we scale the display for
            // si abbreviation can be used after that, if a modded item makes it possible to have 10k+ items in player's
            //  inv at a time
            String countLabel = String.valueOf(ct);
            if (countLabel.length() > 4) countLabel = "9999";
            int w = tr.getWidth(countLabel);
            float rx = x + 19 - 2 - w,  // coordinates where label will be drawn
                    ry = y + 6 + 3 + yOffset;

            MatrixStack matrixStack = new MatrixStack();
            matrixStack.translate(0.0D, 0.0D, ir.zOffset + 200.0F);
            // scale down to keep large numbers from overlapping
            Util.scaleAbout(matrixStack, rx + w, ry, 0d, .7f, .7f, 1f);
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            tr.draw(countLabel, rx, ry, COLOR, true,
                    matrixStack.peek().getModel(), immediate, false, 0, LIGHT);
            immediate.draw();
        }
    }

    @Inject(method = "renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V",
            at = @At("HEAD"))
    public void pre_renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        ClientPlayerEntity cpe = MinecraftClient.getInstance().player;
        if (cpe == null) return;
        // cache inventory counts
        refinedui_invCountsCache = Util.itemCounts(cpe.inventory);
        // draw number of empty slots
        if (getConfig().emptySlotCount) {
            int nEmptySlots = Util.nEmptySlots(cpe.inventory);
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            TextRenderer tr = this.client.textRenderer;
            int xBuffer = 8;
            int x = (int) (this.scaledWidth / 2f + 91 + xBuffer);  // right side of hotbar + buffer space
            int y = (int) (this.scaledHeight - 23 / 2f - tr.fontHeight / 2f);  // center of hotbar, accounting for text height
            this.client.textRenderer.draw(String.valueOf(nEmptySlots),
                    x, y, COLOR, true, matrices.peek().getModel(),
                    immediate, false, 0, LIGHT);
            immediate.draw();
        }
    }

}
