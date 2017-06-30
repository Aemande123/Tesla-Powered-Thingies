package net.ndrei.teslapoweredthingies.integrations.jei

import com.google.common.collect.Lists
import mezz.jei.api.IGuiHelper
import mezz.jei.api.IModRegistry
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.ndrei.teslapoweredthingies.TeslaThingiesMod
import net.ndrei.teslapoweredthingies.machines.FluidBurnerBlock
import net.ndrei.teslapoweredthingies.machines.fluidburner.FluidBurnerCoolantRecipe
import net.ndrei.teslapoweredthingies.machines.fluidburner.FluidBurnerFuelRecipe
import net.ndrei.teslapoweredthingies.machines.fluidburner.FluidBurnerRecipes

/**
 * Created by CF on 2017-06-30.
 */
class FluidBurnerCategory(guiHelper: IGuiHelper)
    : BaseCategory<FluidBurnerCategory.FluidBurnerRecipeWrapper>() {

    //#region class implementation

    private val background: IDrawable
    private val fuelOverlay: IDrawable
    private val coolantOverlay: IDrawable

    init {
        this.background = guiHelper.createDrawable(TeslaThingiesMod.JEI_TEXTURES, 0, 66, 124, 66)
        this.fuelOverlay = guiHelper.createDrawable(TeslaThingiesMod.JEI_TEXTURES, 8, 74, 8, 27)
        this.coolantOverlay = guiHelper.createDrawable(TeslaThingiesMod.JEI_TEXTURES, 20, 74, 8, 27)
    }

    override fun getUid(): String {
        return FluidBurnerCategory.UID
    }

    override fun getTitle(): String {
        return FluidBurnerBlock.localizedName
    }

    override fun getBackground(): IDrawable {
        return this.background
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: FluidBurnerRecipeWrapper, ingredients: IIngredients) {
        val fluids = recipeLayout.fluidStacks

        val capacity = if (recipeWrapper.coolant != null)
            Math.max(recipeWrapper.fuel.amount, recipeWrapper.coolant.amount)
        else
            recipeWrapper.fuel.amount
        fluids.init(0, true, 8, 8, 8, 27, capacity, false, this.fuelOverlay)
        fluids.set(0, ingredients.getInputs(FluidStack::class.java)[0])
        if (ingredients.getInputs(FluidStack::class.java).size == 2) {
            fluids.init(1, true, 20, 8, 8, 27, capacity, false, this.coolantOverlay)
            fluids.set(1, ingredients.getInputs(FluidStack::class.java)[1])
        }
    }

    //#endregion

    class FluidBurnerRecipeWrapper(val fuel: FluidBurnerFuelRecipe, val coolant: FluidBurnerCoolantRecipe?)
        : IRecipeWrapper {

        override fun getIngredients(ingredients: IIngredients) {
            if (this.coolant == null) {
                ingredients.setInput(FluidStack::class.java, FluidStack(this.fuel.fluid, this.fuel.amount))
            } else {
                ingredients.setInputs(FluidStack::class.java, Lists.newArrayList(
                        FluidStack(this.fuel.fluid, this.fuel.amount),
                        FluidStack(this.coolant.fluid, this.coolant.amount)
                ))
            }
        }

        @SideOnly(Side.CLIENT)
        override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
            super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY)

            var ticks = this.fuel.baseTicks
            if (this.coolant != null)
                ticks = Math.round(ticks.toFloat() * this.coolant.timeMultiplier)

            val duration = String.format("%,d ticks", ticks)
            val power = String.format("%,d T", ticks * 80)
            minecraft.fontRenderer.drawString(duration, 36, 12, 0x007F7F)
            minecraft.fontRenderer.drawString(power, 36, 12 + minecraft.fontRenderer.FONT_HEIGHT, 0x007F7F)
        }
    }

    companion object {
        val UID = "FluidBurner"

        fun register(registry: IModRegistry, guiHelper: IGuiHelper) {
            registry.addRecipeCategories(FluidBurnerCategory(guiHelper))
            registry.addRecipeCategoryCraftingItem(ItemStack(FluidBurnerBlock), UID)

            val recipes = Lists.newArrayList<FluidBurnerRecipeWrapper>()
            for (fuel in FluidBurnerRecipes.fuels) {
                recipes.add(FluidBurnerRecipeWrapper(fuel, null))
                for (coolant in FluidBurnerRecipes.coolants) {
                    recipes.add(FluidBurnerRecipeWrapper(fuel, coolant))
                }
            }
            registry.addRecipes(recipes, UID)
        }
    }
}
