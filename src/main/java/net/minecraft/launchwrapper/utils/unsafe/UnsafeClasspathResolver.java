package net.minecraft.launchwrapper.utils.unsafe;

import net.minecraft.launchwrapper.utils.ClasspathResolver;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;

public class UnsafeClasspathResolver implements ClasspathResolver {

    @SuppressWarnings("unchecked")
    @Override
    public URL[] resolve(ClassLoader loader) throws Throwable {
        if (loader.getClass().getName().startsWith("jdk.internal.loader.ClassLoaders$")) {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);

            // jdk.internal.loader.ClassLoaders.AppClassLoader.ucp
            Field ucpField;
            try {
                ucpField = loader.getClass().getDeclaredField("ucp");
            } catch (NoSuchFieldException | SecurityException e) {
                ucpField = loader.getClass().getSuperclass().getDeclaredField("ucp");
            }

            long ucpFieldOffset = unsafe.objectFieldOffset(ucpField);
            Object ucpObject = unsafe.getObject(loader, ucpFieldOffset);

            // jdk.internal.loader.URLClassPath.path
            Field pathField = ucpField.getType().getDeclaredField("path");
            long pathFieldOffset = unsafe.objectFieldOffset(pathField);
            ArrayList<URL> path = (ArrayList<URL>) unsafe.getObject(ucpObject, pathFieldOffset);

            return path.toArray(new URL[0]);
        }

        throw new Exception();
    }
}
