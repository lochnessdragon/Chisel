package team.chisel.common.util;

import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minecraft.block.properties.PropertyHelper;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@EqualsAndHashCode(callSuper = true)
public class PropertyAnyInteger extends PropertyHelper<Integer> {

    @Getter
    private final ImmutableSet<Integer> allowedValues;

    protected PropertyAnyInteger(String name, int min, int max) {
        super(name, Integer.class);

        if (min < 0) {
            throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
        } else if (max < min) {
            throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
        } else {
            Set<Integer> set = Sets.newHashSet();
            for (int i = min; i <= max; ++i) {
                set.add(Integer.valueOf(i));
            }
            this.allowedValues = ImmutableSet.copyOf(set);
        }
    }

    public static PropertyAnyInteger create(String name, int min, int max) {
        return new PropertyAnyInteger(name, min, max);
    }

    @Override
    public String getName(Integer value) {
        return value.toString();
    }
}