package team.chisel.common.item;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.util.NBTUtil;

@NoArgsConstructor
public class PacketChiselMode implements IMessage {
    
    private int slot;
    private @Nonnull IChiselMode mode;
    
    public PacketChiselMode(int slot, @Nonnull IChiselMode mode) {
        this.slot = slot;
        this.mode = mode;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slot);
        ByteBufUtils.writeUTF8String(buf, this.mode.name());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slot = buf.readInt();
        this.mode = ChiselMode.fromString(ByteBufUtils.readUTF8String(buf));
    }

    public static class Handler implements IMessageHandler<PacketChiselMode, IMessage> {
        
        @Override
        public IMessage onMessage(PacketChiselMode message, MessageContext ctx) {
            ctx.getServerHandler().playerEntity.getServer().addScheduledTask(() -> {
                ItemStack stack = ctx.getServerHandler().playerEntity.inventory.getStackInSlot(message.slot);
                if (stack != null) {
                    NBTUtil.setChiselMode(stack, message.mode);
                }
            });
            return null;
        }
    }
}