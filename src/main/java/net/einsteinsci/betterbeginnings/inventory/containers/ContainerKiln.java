package net.einsteinsci.betterbeginnings.inventory.containers;

import net.einsteinsci.betterbeginnings.inventory.slots.SlotConfigurableAccess;
import net.einsteinsci.betterbeginnings.inventory.slots.SlotOutput;
import net.einsteinsci.betterbeginnings.register.FuelRegistry;
import net.einsteinsci.betterbeginnings.register.FuelRegistry.FuelConsumerType;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityKiln;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityKilnBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

public class ContainerKiln extends ContainerSpecializedFurnace<TileEntityKiln>
{	
	public ContainerKiln(EntityPlayer player, TileEntityKiln tileEntityKiln)
	{
		super(tileEntityKiln);
		PlayerInvWrapper playerInv = (PlayerInvWrapper) player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotItemHandler(itemHandler, TileEntityKiln.SLOT_INPUT, 56, 17));
		addSlotToContainer(new SlotConfigurableAccess(itemHandler, TileEntityKiln.SLOT_FUEL, 56, 53));
		addSlotToContainer(new SlotOutput(itemHandler, TileEntityKiln.SLOT_OUTPUT, 116, 35));

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
	public ItemStack transferStackInSlot(EntityPlayer player, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == TileEntityKilnBase.SLOT_OUTPUT)
			{
				if (!mergeItemStack(itemstack1, TileEntityKilnBase.SLOT_OUTPUT + 1, 39, true))
				{
					return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (par2 != TileEntityKiln.SLOT_FUEL && par2 != TileEntityKiln.SLOT_INPUT)
			{
				if (KilnRecipeHandler.instance().getSmeltingResult(itemstack1) != null)
				{
					if (!mergeItemStack(itemstack1, TileEntityKiln.SLOT_INPUT, TileEntityKiln.SLOT_INPUT + 1, false))
					{
						return null;
					}
				}
				else if (FuelRegistry.getBurnTime(FuelConsumerType.KILN, itemstack1) > 0)
				{
					if (!mergeItemStack(itemstack1, TileEntityKiln.SLOT_FUEL, TileEntityKiln.SLOT_FUEL + 1, false))
					{
						return null;
					}
				}
				else if (par2 > TileEntityKiln.SLOT_OUTPUT && par2 < 30)
				{
					if (!mergeItemStack(itemstack1, 30, 39, false))
					{
						return null;
					}
				}
				else if (par2 >= 30 && par2 < 39 && !mergeItemStack(itemstack1, TileEntityKiln.SLOT_OUTPUT + 1, 30, false))
				{
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, TileEntityKiln.SLOT_OUTPUT + 1, 39, true))
			{
				return null;
			}
			if (itemstack1.getCount() == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount())
			{
				return null;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}
}
