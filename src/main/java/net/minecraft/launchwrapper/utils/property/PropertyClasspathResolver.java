package net.minecraft.launchwrapper.utils.property;

import net.minecraft.launchwrapper.utils.ClasspathResolver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class PropertyClasspathResolver implements ClasspathResolver {
    @Override
    public URL[] resolve(ClassLoader loader) throws Throwable {
        String cp = System.getProperty("java.class.path");
        String[] elements = cp.split(File.pathSeparator);

        if (elements.length == 0) {
            throw new Exception();
        }

        URL[] urls = new URL[elements.length];
        for (int i = 0; i < elements.length; i++) {
            try {
                URL url = new File(elements[i]).toURI().toURL();
                urls[i] = url;
            } catch (MalformedURLException ignored) {

            }
        }
        return urls;
    }
}
