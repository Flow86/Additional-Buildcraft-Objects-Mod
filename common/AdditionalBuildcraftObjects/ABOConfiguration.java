package AdditionalBuildcraftObjects;

import java.io.File;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class ABOConfiguration extends Configuration {
	public ABOConfiguration(File file) {
		super(file);
	}

	@Override
	public void save() {
		Property versionProp = null;

		if (!generalProperties.containsKey("version")) {
			versionProp = new Property();
			versionProp.setName("version");
			generalProperties.put("version", versionProp);
		} else
			versionProp = generalProperties.get("version");

		versionProp.value = ABO.VERSION;

		super.save();
	}
}
