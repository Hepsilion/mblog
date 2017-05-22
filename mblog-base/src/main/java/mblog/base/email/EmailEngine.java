/**
 * 
 */
package mblog.base.email;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.SpringResourceLoader;

/**
 * @author langhsu
 *
 */
@Component
public class EmailEngine implements ResourceLoaderAware{
	private VelocityEngine velocityEngine;
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	@Value("#{configProperties['velocity.resource']}")
	private String velocityLoaderPath;
	
	
	public VelocityEngine getEngine() {
		if (velocityEngine == null) {
			velocityEngine = new VelocityEngine();
			initSpringResourceLoader(velocityEngine, velocityLoaderPath);
			velocityEngine.init();
		}
		return velocityEngine;
	}
	
	protected void initSpringResourceLoader(VelocityEngine velocityEngine, String resourceLoaderPath) {
		velocityEngine.setProperty(
				RuntimeConstants.RESOURCE_LOADER, SpringResourceLoader.NAME);
		velocityEngine.setProperty(
				SpringResourceLoader.SPRING_RESOURCE_LOADER_CLASS, SpringResourceLoader.class.getName());
		velocityEngine.setProperty(
				SpringResourceLoader.SPRING_RESOURCE_LOADER_CACHE, "true");
		velocityEngine.setApplicationAttribute(
				SpringResourceLoader.SPRING_RESOURCE_LOADER, getResourceLoader());
		velocityEngine.setApplicationAttribute(
				SpringResourceLoader.SPRING_RESOURCE_LOADER_PATH, resourceLoaderPath);
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
}
