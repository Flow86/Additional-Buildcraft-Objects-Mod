/** 
 * Copyright (C) 2011-2013 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package abo.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.TriggerParameter;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.Gate.GateKind;
import buildcraft.transport.GateVanilla;
import buildcraft.transport.Pipe;

/**
 * @author Flow86
 * 
 */
public class ItemGateSettingsDuplicator extends ABOItem {
	static class GateSlot {
		public GateSlot() {
			trigger = 0;
			triggerParameter = null;
			action = 0;
		}

		int trigger;
		ItemStack triggerParameter;
		int action;

		public void writeToNBT(NBTTagCompound nbt) {
			nbt.setInteger("trigger", trigger);
			if (triggerParameter != null) {
				NBTTagCompound nbtparam = new NBTTagCompound();
				triggerParameter.writeToNBT(nbtparam);
				nbt.setCompoundTag("triggerParameter", nbtparam);
			}
			nbt.setInteger("action", action);
		}

		public void readFromNBT(NBTTagCompound nbt) {
			trigger = nbt.getInteger("trigger");
			triggerParameter = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("triggerParameter"));
			action = nbt.getInteger("action");
		}
	};

	protected static class GateSettings {
		public GateSettings(GateKind kind) {
			this.kind = kind;
			slots = new GateSlot[8];
			for (int i = 0; i < 8; ++i) {
				slots[i] = new GateSlot();
			}
		}

		private final GateKind kind;
		private boolean isAutarchic;
		private final GateSlot[] slots;

		public void writeToNBT(NBTTagCompound stackTagCompound) {
			NBTTagCompound nbt = new NBTTagCompound();

			nbt.setInteger("gate", kind.ordinal());
			nbt.setBoolean("hasPulser", isAutarchic);

			for (int i = 0; i < 8; ++i) {
				NBTTagCompound nbtslot = new NBTTagCompound();
				slots[i].writeToNBT(nbtslot);
				nbt.setCompoundTag("Slot" + i, nbtslot);
			}

			stackTagCompound.setCompoundTag("GateSettings", nbt);
		}

		public static GateSettings createFromNBT(NBTTagCompound stackTagCompound) {
			if (!stackTagCompound.hasKey("GateSettings"))
				return null;

			NBTTagCompound nbt = stackTagCompound.getCompoundTag("GateSettings");

			GateSettings gS = new GateSettings(GateKind.values()[nbt.getInteger("gate")]);
			gS.isAutarchic = nbt.getBoolean("hasPulser");
			for (int i = 0; i < 8; ++i) {
				gS.slots[i].readFromNBT(nbt.getCompoundTag("Slot" + i));
			}

			return gS;
		}
	};

	public ItemGateSettingsDuplicator(int itemID) {
		super(itemID);
		setMaxStackSize(1);
		setNoRepair();
		setFull3D();
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side, float var8, float var9, float var10) {
		if (worldObj.isRemote)
			return super.onItemUseFirst(itemStack, entityPlayer, worldObj, x, y, z, side, var8, var9, var10);

		Pipe pipe = BlockGenericPipe.getPipe(worldObj, x, y, z);

		if (BlockGenericPipe.isValid(pipe)) {
			if (pipe.hasGate()) {
				if (itemStack.stackTagCompound == null)
					itemStack.stackTagCompound = new NBTTagCompound();

				if (entityPlayer.isSneaking() && itemStack.stackTagCompound.hasKey("GateSettings")) {
					// apply settings to gate
					GateSettings gS = GateSettings.createFromNBT(itemStack.stackTagCompound);
					if (gS.kind == pipe.gate.kind && gS.isAutarchic == (pipe.gate instanceof GateVanilla && ((GateVanilla) pipe.gate).hasPulser())) {
						for (int i = 0; i < 8; ++i) {
							pipe.activatedActions[i] = ActionManager.actions[gS.slots[i].action];
							pipe.activatedTriggers[i] = ActionManager.triggers[gS.slots[i].trigger];
							pipe.triggerParameters[i] = new TriggerParameter();
							pipe.triggerParameters[i].set(gS.slots[i].triggerParameter);
						}

						entityPlayer.sendChatToPlayer("Gate settings pasted");
					}

				} else {
					// get settings from gate
					GateSettings gS = new GateSettings(pipe.gate.kind);

					if (pipe.gate instanceof GateVanilla) {
						gS.isAutarchic = ((GateVanilla) pipe.gate).hasPulser();
					}

					for (int i = 0; i < 8; ++i) {
						if (pipe.activatedTriggers[i] != null) {
							gS.slots[i].trigger = pipe.activatedTriggers[i].getId();
						}

						if (pipe.triggerParameters[i] != null) {
							gS.slots[i].triggerParameter = pipe.triggerParameters[i].getItemStack();
						}

						if (pipe.activatedActions[i] != null) {
							gS.slots[i].action = pipe.activatedActions[i].getId();
						}
					}
					gS.writeToNBT(itemStack.stackTagCompound);

					entityPlayer.sendChatToPlayer("Gate settings copied");
				}

				return true;
			}
		}

		return super.onItemUseFirst(itemStack, entityPlayer, worldObj, x, y, z, side, var8, var9, var10);
	}
}
