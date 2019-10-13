package com.direwolf20.buildinggadgets.api.materials;

import com.direwolf20.buildinggadgets.api.materials.inventory.IUniqueObject;
import com.direwolf20.buildinggadgets.api.util.NBTKeys;
import com.google.common.collect.*;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class OrMaterialListEntry extends SubMaterialListEntry {
    static final MaterialListEntry.Serializer<SubMaterialListEntry> SERIALIZER = new SubMaterialListEntry.Serializer() {
        @Override
        protected SubMaterialListEntry create(ImmutableList<MaterialListEntry<?>> subEntries, ImmutableList<SimpleMaterialListEntry> constantEntries, CompoundNBT nbt, boolean persisted) {
            return new OrMaterialListEntry(subEntries, constantEntries);
        }
    }.setRegistryName(NBTKeys.OR_SERIALIZER_ID);

    OrMaterialListEntry(ImmutableList<MaterialListEntry<?>> subEntries, ImmutableList<SimpleMaterialListEntry> simpleEntries, boolean simplified) {
        super(subEntries, simpleEntries, simplified);
    }

    OrMaterialListEntry(ImmutableList<MaterialListEntry<?>> subEntries, ImmutableList<SimpleMaterialListEntry> constantEntries) {
        super(subEntries, constantEntries);
    }

    OrMaterialListEntry(ImmutableList<MaterialListEntry<?>> subEntries) {
        super(subEntries);
    }

    @Override
    public PeekingIterator<ImmutableMultiset<IUniqueObject<?>>> iterator() {
        if (! getAllSubEntries().findFirst().isPresent())
            return Iterators.peekingIterator(Iterators.singletonIterator(ImmutableMultiset.of()));
        Iterator<MaterialListEntry<?>> entryIterator = getAllSubEntries().iterator();
        return Iterators.peekingIterator(new AbstractIterator<ImmutableMultiset<IUniqueObject<?>>>() {
            private Iterator<ImmutableMultiset<IUniqueObject<?>>> itemIterator;

            @Override
            protected ImmutableMultiset<IUniqueObject<?>> computeNext() {
                if (itemIterator == null) {
                    if (entryIterator.hasNext())
                        itemIterator = entryIterator.next().iterator();
                    else
                        return endOfData();
                }
                if (! itemIterator.hasNext()) {
                    itemIterator = null;
                    return computeNext();
                }
                return itemIterator.next();
            }
        });
    }

    @Override
    public MaterialListEntry.Serializer<SubMaterialListEntry> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected List<MaterialListEntry<?>> orderAndSimplifyEntries(List<OrMaterialListEntry> orEntries,
                                                                 List<AndMaterialListEntry> andEntries,
                                                                 List<SimpleMaterialListEntry> simpleEntries) {
        List<MaterialListEntry<?>> remainder = new ArrayList<>();
        //Theoretically we could evaluate the Set intersection here and add that as a constant entry
        //I believe that to be too much overhead for too little gain though
        getSubEntries().stream().map(MaterialListEntry::simplify).forEach(entry -> {
            if (entry instanceof AndMaterialListEntry)
                andEntries.add((AndMaterialListEntry) entry);
            else if (entry instanceof OrMaterialListEntry) {
                simpleEntries.addAll(((SubMaterialListEntry) entry).getConstantEntries());
                pullUpInnerEntries((SubMaterialListEntry) entry, orEntries, andEntries, simpleEntries, remainder);
            } else //Cannot pull out simple Entries! They are alternatives after all!
                remainder.add(entry);
        });
        return remainder;
    }

    @Override
    protected SubMaterialListEntry createFrom(ImmutableList<MaterialListEntry<?>> subEntries, ImmutableList<SimpleMaterialListEntry> constantEntry, boolean simplified) {
        return new OrMaterialListEntry(subEntries, constantEntry, simplified);
    }
}
