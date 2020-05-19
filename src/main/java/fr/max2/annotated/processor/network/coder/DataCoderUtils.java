package fr.max2.annotated.processor.network.coder;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;

import fr.max2.annotated.processor.network.coder.handler.IDataHandler;
import fr.max2.annotated.processor.network.coder.handler.NamedDataHandler;
import fr.max2.annotated.processor.network.model.IFunctionBuilder;
import fr.max2.annotated.processor.network.model.IPacketBuilder;
import fr.max2.annotated.processor.utils.exceptions.IncompatibleTypeException;

public class DataCoderUtils
{
	private DataCoderUtils() { }
	
	public static String writeBuffer(String type, String value)
	{
		return "buf.write" + type + "(" + value + ");";
	}
	
	public static String readBuffer(String type)
	{
		return "buf.read" + type + "()";
	}
	
	public static void addBufferInstructions(String type, String saveValue, BiConsumer<IFunctionBuilder, String> loadValue, IPacketBuilder builder)
	{
		builder.encoder().add(writeBuffer(type, saveValue));
		loadValue.accept(builder.decoder(), readBuffer(type));
	}
	
	public static Supplier<DataCoder> simpleCoder(String type)
	{
		return () -> new SimpleCoder(type);
	}
	
	public static IDataHandler simpleHandler(String className, String typeName)
	{
		return new NamedDataHandler(className, simpleCoder(typeName));
	}
	
	public static IDataHandler simpleHandler(Class<?> clazz, String typeName)
	{
		return simpleHandler(clazz.getTypeName(), typeName);
	}
	
	public static void requireDefaultConstructor(Types typeHelper, TypeMirror type)
	{
		Element elem = typeHelper.asElement(type);
		if (elem == null)
			return; // Unknown type, assume it has a default constructor
		
		if (elem.getModifiers().contains(Modifier.ABSTRACT))
			throw new IncompatibleTypeException("The type '" + type + "' is abstract and can't be instantiated");
		
		if (!ElementFilter.constructorsIn(elem.getEnclosedElements()).stream().anyMatch(cons -> cons.getParameters().isEmpty()))
			throw new IncompatibleTypeException("The type '" + type + "' doesn't have a default constructor");
	}
	
	public static class SimpleCoder extends DataCoder
	{
		private final String typeName;
		
		public SimpleCoder(String typeName)
		{
			this.typeName = typeName;
		}

		@Override
		public void addInstructions(IPacketBuilder builder, String saveAccessExpr, BiConsumer<IFunctionBuilder, String> setExpr)
		{
			DataCoderUtils.addBufferInstructions(this.typeName, saveAccessExpr, setExpr, builder);
		}
	}
}