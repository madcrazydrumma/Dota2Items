package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.util.IntVec3;
import hunternif.mc.dota2items.util.SideHit;
import hunternif.mc.dota2items.util.TreeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class QuellingBlade extends TargetBlockItem {
	// Quelling Blade is an axe, so why not make it effective against wood
	public static final Block[] blocksEffectiveAgainst = ItemAxe.blocksEffectiveAgainst;
	// Like an iron axe it is
	public float efficiencyOnProperMaterial = 6.0F;
	
	/** Look for the tree trunk minus this delta on the Y axis from the click point. */
	public static final int trunkSearchDeltaY = 3;
	/** Look for the tree trunk in a horizontal radius from the click point. */
	public static final int trunkSearchRadius = 3;

	public QuellingBlade(int id) {
		super(id);
		setCooldown(5);
	}
	
	@Override
	public float getStrVsBlock(ItemStack itemStack, Block block) {
		return block != null && (block.blockMaterial == Material.wood || block.blockMaterial == Material.plants || block.blockMaterial == Material.vine) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(itemStack, block);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, int blockID, int x, int y, int z, EntityLivingBase player) {
		if ((double)Block.blocksList[blockID].getBlockHardness(world, x, y, z) != 0.0D) {
				itemStack.damageItem(1, player);
		}
		return true;
	}

	@Override
	protected void onUseOnBlock(ItemStack stack, EntityPlayer player, int x, int y, int z, int side) {
		World world = player.worldObj;
		// Looking for a tree
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == Block.vine) {
			// Hit vine while probably aiming at what was behind it:
			switch(side) {
			case SideHit.NORTH:
				x++;
				break;
			case SideHit.SOUTH:
				x--;
				break;
			case SideHit.EAST:
				z++;
				break;
			case SideHit.WEST:
				z--;
				break;
			}
			// Now Try again
			block = Block.blocksList[world.getBlockId(x, y, z)];
		}
		if (block == Block.snow) {
			// Hit snow while probably aiming at what was below it:
			y--;
			// Now Try again
			block = Block.blocksList[world.getBlockId(x, y, z)];
		}
		if (block != Block.wood && block != Block.leaves) {
			return;
		} else if (block == Block.wood || block == Block.leaves) {
			IntVec3 trunkBase = null;
			if (block == Block.wood) {
				// Supposedly hit the trunk
				trunkBase = new IntVec3(x, TreeUtil.getTreeTrunkBaseY(world, x, y, z), z);
			} else {
				// Hit some leaves. Looking for the closest tree trunk around
				trunkBase = TreeUtil.findTreeTrunkInBox(world, x, y, z, trunkSearchDeltaY, trunkSearchRadius);
			}
			if (trunkBase != null && trunkBase.y > 0) {
				// Yep, found a tree
				if (!world.isRemote) {
					startCooldown(stack, player);
				}
				TreeUtil.removeTree(world, trunkBase, true);
				world.playSoundEffect(trunkBase.x, trunkBase.y, trunkBase.z, Sound.TREE_FALL.getName(), 1.0f, 1.0f);
			}
		}
	}
}
