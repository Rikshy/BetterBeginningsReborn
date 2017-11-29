package net.einsteinsci.betterbeginnings.inventory.containers;

import net.einsteinsci.betterbeginnings.inventory.slots.SlotOutput;
import net.einsteinsci.betterbeginnings.register.FuelRegistry;
import net.einsteinsci.betterbeginnings.register.FuelRegistry.FuelConsumerType;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityEnderSmelter;
import net.einsteinsci.betterbeginnings.tileentity.TileEntitySmelterBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

public class ContainerEnderSmelter extends ContainerSpecializedFurnace<TileEntityEnderSmelter>
{	
	public ContainerEnderSmelter(EntityPlayer player, TileEntityEnderSmelter tileSmelter)
	{
		super(tileSmelter);
		PlayerInvWrapper playerInv = (PlayerInvWrapper) player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotItemHandler(itemHandler, TileEntitySmelterBase.INPUT, 46, 17));
		addSlotToContainer(new SlotItemHandler(itemHandler, TileEntitySmelterBase.FUEL, 56, 53));
		addSlotToContainer(new SlotOutput(itemHandler, TileEntityEnderSmelter.OUTPUT, 116, 35));
		addSlotToContainer(new SlotItemHandler(itemHandler, TileEntitySmelterBase.BOOSTER, 66, 17));

		int i;
		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				addSlotToContainer(new SlotItemHandler(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			addSlotToContainer(new SlotItemHandler(playerInv, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int fromId)
	{
		ItemStack movedStackDupe = null;
		Slot slot = (Slot)inventorySlots.get(fromId);

		if (slot != null && slot.getHasStack())
		{
			ItemStack movedStack = slot.getStack();
			movedStackDupe = movedStack.copy();

			if (fromId == TileEntityEnderSmelter.OUTPUT)
			{
				if (!mergeItemStack(movedStack, 4, 40, true))
				{
					return null;
				}
				slot.onSlotChange(movedStack, movedStackDupe);
			}
			else if (fromId != TileEntityEnderSmelter.FUEL && fromId != TileEntityEnderSmelter.INPUT &&
					fromId != TileEntityEnderSmelter.BOOSTER)
			{
				if (SmelterRecipeHandler.instance().getSmeltingResult(movedStack) != null)
				{
					if (!mergeItemStack(movedStack,
					                    TileEntityEnderSmelter.INPUT,
					                    TileEntityEnderSmelter.INPUT + 1,
					                    false))
					{
						return null;
					}
				}
				else if (movedStack.getItem() == Item.getItemFromBlock(Blocks.GRAVEL))
				{
					if (!mergeItemStack(movedStack,
					                    TileEntityEnderSmelter.BOOSTER,
					                    TileEntityEnderSmelter.BOOSTER + 1,
					                    false))
					{
						return null;
					}
				}
				else if (FuelRegistry.getBurnTime(FuelConsumerType.ENDER_SMELTER, movedStack) > 0)
				{
					if (!mergeItemStack(movedStack,
					                    TileEntityEnderSmelter.FUEL,
					                    TileEntityEnderSmelter.FUEL + 1,
					                    false))
					{
						return null;
					}
				}
				else if (fromId >= 4 && fromId < 31)
				{
					if (!mergeItemStack(movedStack, 31, 40, false))
					{
						return null;
					}
				}
				else if (fromId >= 31 && fromId < 40 && !mergeItemStack(movedStack, 4, 31, false))
				{
					return null;
				}
			}
			else if (!mergeItemStack(movedStack, 4, 40, false))
			{
				return null;
			}
			if (movedStack.getCount() == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (movedStack.getCount() == movedStackDupe.getCount())
			{
				return null;
			}
			slot.onTake(player, movedStack);
		}
		return movedStackDupe;
	}

	public boolean merge(ItemStack stack, int startSlot, int endSlot, boolean searchFromBottom)
	{
		return mergeItemStack(stack, startSlot, endSlot, searchFromBottom);
	}
}
