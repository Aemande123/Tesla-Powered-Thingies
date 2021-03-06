package net.ndrei.teslapoweredthingies.items

import net.minecraft.item.Item
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistry
import net.ndrei.teslacorelib.annotations.AutoRegisterItem

/**
 * Created by CF on 2017-06-30.
 */
@AutoRegisterItem
object AshItem : BaseThingyItem("ash") {
//    override val recipe: IRecipe?
//        get() = ShapedOreRecipe(null, ItemStack(Items.DYE, 1, 15),
//                "xx",
//                "xx",
//                'x', this
//        )

    override fun registerItem(registry: IForgeRegistry<Item>) {
        super.registerItem(registry)
        OreDictionary.registerOre("ash", AshItem)
    }
}
