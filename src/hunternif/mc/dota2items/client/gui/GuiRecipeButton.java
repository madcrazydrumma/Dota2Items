package hunternif.mc.dota2items.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiRecipeButton extends GuiButton {
	protected static RenderItem itemRenderer = new RenderItem();
	
	public ItemStack itemStack;
	public boolean displayEnabled;
	public boolean selected;
	
	public GuiRecipeButton(int id, int x, int y, ItemStack itemStack) {
		super(id, x, y, 18, 18, "");
		this.itemStack = itemStack;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.drawButton) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			// Render background:
			mc.renderEngine.bindTexture(GuiShopBuy.texture);
			boolean isMouseOver = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int u = 230;
			int v = 23;
			if (!displayEnabled) {
				v += this.height * 2;
			} else if (selected) {
				v += this.height * 1;
			} else if (isMouseOver) {
				v += this.height * 3;
			}
			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
			
			// Render item on top of the button:
			if (itemStack != null) {
				FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
				if (font == null) font = mc.fontRenderer;
				itemRenderer.renderItemAndEffectIntoGUI(font, mc.renderEngine, itemStack, this.xPosition + 1, this.yPosition + 1);
				itemRenderer.renderItemOverlayIntoGUI(font, mc.renderEngine, itemStack, this.xPosition + 1, this.yPosition + 1, (String)null);
			}
		}
	}
}
