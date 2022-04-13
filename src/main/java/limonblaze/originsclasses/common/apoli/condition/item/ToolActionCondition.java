package limonblaze.originsclasses.common.apoli.condition.item;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nonnull;

public class ToolActionCondition extends ItemCondition<FieldConfiguration<String>> {

    public ToolActionCondition() {
        super(FieldConfiguration.codec(Codec.STRING, "tool_action"));
    }

    @Override
    protected boolean check(FieldConfiguration<String> configuration, @Nonnull Level level, ItemStack stack) {
        return stack.canPerformAction(ToolAction.get(configuration.value()));
    }

}
