package furgl.infinitory.impl.dependencies;

import java.util.Optional;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import furgl.infinitory.config.Config;
import furgl.infinitory.config.Config.DropsOnDeath;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class ClothConfigModMenuDependency implements Dependency, ModMenuApi {
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			ConfigBuilder builder = ConfigBuilder.create()
					.setParentScreen(parent)
					.setTitle(MutableText.of(new TranslatableTextContent("config.infinitory.name")))
					.setSavingRunnable(() -> Config.writeToFile(false));
			ConfigCategory category = builder.getOrCreateCategory(
					MutableText.of(new TranslatableTextContent("config.infinitory.category.general"))
			);
			category.addEntry(builder.entryBuilder()
					.startIntField(
//							new TranslatableText("config.infinitory.option.maxItemStackSize")
							MutableText.of(new TranslatableTextContent("config.infinitory.option.maxItemStackSize"))
							, Config.maxStackSize)
					.setTooltip(
//							new TranslatableText("config.infinitory.option.maxItemStackSize.tooltip")
							MutableText.of(new TranslatableTextContent("config.infinitory.option.maxItemStackSize.tooltip"))
					)
					.setDefaultValue(Integer.MAX_VALUE)
					.setSaveConsumer(value -> Config.maxStackSize = value)
					.build());
			category.addEntry(builder.entryBuilder()
					.startIntField(
//							new TranslatableText("config.infinitory.option.maxExtraInventorySlots")
							MutableText.of(
									new TranslatableTextContent("config.infinitory.option.maxExtraInventorySlots")
							)
							, Config.maxExtraSlots)
					.setTooltip(
//							new TranslatableText("config.infinitory.option.maxExtraInventorySlots.tooltip")
							MutableText.of(new TranslatableTextContent("config.infinitory.option.maxExtraInventorySlots.tooltip"))
					)
					.setDefaultValue(Integer.MAX_VALUE)
					.setSaveConsumer(value -> Config.maxExtraSlots = value)
					.build());
			category.addEntry(builder.entryBuilder()
					.startEnumSelector(
//							new TranslatableText("config.infinitory.option.itemsToDropOnDeath")
							MutableText.of(new TranslatableTextContent("config.infinitory.option.itemsToDropOnDeath"))
							, DropsOnDeath.class, DropsOnDeath.UP_TO_STACK)
					.setEnumNameProvider(e ->
//							new TranslatableText("config.infinitory.option", e.ordinal()+1)
							MutableText.of(new TranslatableTextContent("config.infinitory.option", e.ordinal()+1))
					)
					.setTooltip(
//							new TranslatableText("config.infinitory.option.itemsToDropOnDeath.tooltip")
							MutableText.of(new TranslatableTextContent("config.infinitory.option.itemsToDropOnDeath.tooltip"))
					)
					.setTooltipSupplier(drop -> Optional.of(new Text[] {
//							new TranslatableText("config.infinitory.option.itemsToDropOnDeath."+drop.ordinal())
							MutableText.of(new TranslatableTextContent("config.infinitory.option.itemsToDropOnDeath."+drop.ordinal()))
					}))
					.setDefaultValue(DropsOnDeath.UP_TO_STACK)
					.setSaveConsumer(value -> Config.dropsOnDeath = value)
					.build());
			category.addEntry(builder.entryBuilder()
					.startBooleanToggle(
							MutableText.of(new TranslatableTextContent("config.infinitory.option.expandedCrafting"))

							, Config.expandedCrafting)
					.setTooltip(
							MutableText.of(new TranslatableTextContent("config.infinitory.option.expandedCrafting.tooltip"))

					)
					.setDefaultValue(true)
					.setSaveConsumer(value -> Config.expandedCrafting = value)
					.requireRestart()
					.build());
			return builder.build();
		};
	}
	
}