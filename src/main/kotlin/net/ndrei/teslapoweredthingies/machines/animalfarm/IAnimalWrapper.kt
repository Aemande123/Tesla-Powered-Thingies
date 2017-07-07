package net.ndrei.teslapoweredthingies.machines.animalfarm

import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * Created by CF on 2017-07-07.
 */
interface IAnimalWrapper {
    val animal: EntityAnimal

    /**
     * @param stack
     * * the stack with what one supposes consists food
     * *
     * @return
     * * true - yes, the animal will eat that
     * * false - no way, the animal would hate being force feed that stuff
     */
    fun isFood(stack: ItemStack): Boolean

    /**
     * @return
     * * true - this animal can breed
     * * false - this animal could be a minor or something
     */
    fun breedable(): Boolean

    /**
     * @param wrapper
     * * test if these 2 animals can be mated
     * *
     * @return
     * * true - these two horny animals can make sweet sweet love
     * * false - they don't quite like each others
     */
    fun canMateWith(wrapper: IAnimalWrapper): Boolean

    /**
     * @param stack
     * * the stack of food items to feed from
     * *
     * @return
     * * the amount of food consumed
     * * if return is 0 the mating failed
     */
    fun mate(player: EntityPlayer, stack: ItemStack, wrapper: IAnimalWrapper): Int

    /**
     * @param stack
     * * the stack of items the animal should be sheared with (axes not allows)
     * *
     * @return
     * * true - yes, this animal is sheep-y
     * * false - this animal has nothing to be sheared off it
     * * (also returns a fake false in case of mooshrooms)
     */
    fun canBeShearedWith(stack: ItemStack): Boolean

    /**
     * @return
     * * true - this animal has something to shear off it
     * * false - this animal is bald
     */
    fun shearable(): Boolean

    /**
     * @param stack
     * * the stack of items the animal should be sheared with (swords not allows)
     * *
     * @param fortune
     * * how much fortune the shearer believes he has
     * *
     * @return
     * * the list of items that were sheared off this poor animal
     */
    fun shear(stack: ItemStack, fortune: Int): List<ItemStack>

    /**
     * @return
     * * true - this animal provides milk (or any other fluid)
     * * false - this animal is dry as sand
     */
    fun canBeMilked(): Boolean

    /**
     * @return
     * * returns the ItemStack containing the bucket of liquid milked from this animal
     */
    fun milk(): ItemStack

    /**
     * @return
     * * true - this animal can be used to feel a bowl with what some might consider food
     * * false - this animal doesn't know the usage of a bowl
     */
    fun canBeBowled(): Boolean

    /**
     * @return
     * * return the ItemStack representing the bowl of soup provided by this animal
     */
    fun bowl(): ItemStack
}
