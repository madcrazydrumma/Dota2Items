package hunternif.mc.dota2items.client.render;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.entity.TemporaryEntity;
import hunternif.mc.dota2items.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderMidasEffect extends RenderEntity {
	private static final ResourceLocation textureLight = new ResourceLocation(Dota2Items.ID + ":textures/effects/light.png");
	private static final ResourceLocation textureRings = new ResourceLocation(Dota2Items.ID + ":textures/effects/midas.png");
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float partialTick) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDepthMask(false);

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);

		TemporaryEntity fx = (TemporaryEntity) entity;
		float ageFraq = fx.getDurationFraq(partialTick);
		float alpha = 1;
		if (ageFraq > 0.6f) {
			alpha *= 1f - (ageFraq - 0.6f)/0.4f;
		}
		
		// Render column of light:
		Minecraft.getMinecraft().renderEngine.bindTexture(textureLight);
		double radius = 0.5;
		double height = 4;
		RenderHelper.drawTexturedCyllinder(radius, height, 0, 16, alpha * 0.5f);
		
		// Render glowing rings:
		Minecraft.getMinecraft().renderEngine.bindTexture(textureRings);
		float baseAngle = ageFraq * 0.25f;
		radius = 0.7;
		height = 2;
		RenderHelper.drawTexturedCyllinder(radius, height, baseAngle, 20, alpha);
		
		baseAngle *= 0.25f;
		radius = 1.5;
		height = 1.2;
		RenderHelper.drawTexturedCyllinder(radius, height, baseAngle, 40, alpha * 0.5f);

		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
}
