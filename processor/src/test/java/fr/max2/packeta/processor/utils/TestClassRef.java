package fr.max2.packeta.processor.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.max2.packeta.api.processor.network.ConstSize;
import fr.max2.packeta.api.processor.network.GenerateNetwork;
import fr.max2.packeta.api.processor.network.GeneratePacket;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;

public class TestClassRef
{
	
	@Test
	public void testValues()
	{
		assertEquals(GenerateNetwork.class.getCanonicalName(), ClassRef.NETWORK_ANNOTATION);
		assertEquals(GeneratePacket.class.getCanonicalName(), ClassRef.PACKET_ANNOTATION);
		assertEquals(ConstSize.class.getCanonicalName(), ClassRef.CONSTSIZE_ANNOTATION);
		
		
		assertEquals(Mod.class.getCanonicalName(), ClassRef.FORGE_MOD_ANNOTATION);
		assertEquals(INBTSerializable.class.getCanonicalName(), ClassRef.NBT_SERIALIZABLE_INTERFACE);
	}
	
}
