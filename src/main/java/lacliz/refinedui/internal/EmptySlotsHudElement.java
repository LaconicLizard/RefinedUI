package lacliz.refinedui.internal;

import lacliz.refinedui.RefinedUI;
import lacliz.refinedui.Util;
import laconiclizard.hudelements.api.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class EmptySlotsHudElement extends HudElement {

    public static final EmptySlotsHudElement INSTANCE = new EmptySlotsHudElement();

    private final ItemStack itemStack = new ItemStack(Items.CHEST);

    private EmptySlotsHudElement() {
        super(0, 0);
    }

    @Override public void save() {
        RUIConfig config = RefinedUI.getConfig();
        config.emptySlotCountX = getX();
        config.emptySlotCountY = getY();
        RefinedUI.getConfigHolder().save();
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity cpe = client.player;
        if (cpe == null) return;
        client.getItemRenderer().renderInGui(itemStack, getX(), getY());
        int nEmptySlots = Util.nEmptySlots(cpe.inventory);
        client.textRenderer.draw(matrices, String.valueOf(nEmptySlots),
                getX() + 17, getY() + (getHeight() - client.textRenderer.fontHeight + 2) / 2f,
                0xffffff);
    }

    @Override public int getWidth() {
        return 32;
    }

    @Override public int getHeight() {
        return 16;
    }

}
