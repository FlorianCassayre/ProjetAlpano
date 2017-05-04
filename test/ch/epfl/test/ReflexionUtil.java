package ch.epfl.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflexionUtil
{
    private ReflexionUtil()
    {}

    public static <T> T invokeMethod(Object object, String name, Class[] types, Object[] arguments)
    {
        final Method method;
        try
        {
            method = object.getClass().getDeclaredMethod(name, types);
            method.setAccessible(true);
            return (T) method.invoke(object, arguments);
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            throw new IllegalStateException(e);
        }
    }
}
